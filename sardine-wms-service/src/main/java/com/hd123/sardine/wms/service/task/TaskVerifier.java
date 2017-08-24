/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	TaskVerifier.java
 * 模块说明：	
 * 修改历史：
 * 2017年7月28日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.task;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.bin.Bin;
import com.hd123.sardine.wms.api.basicInfo.bin.BinService;
import com.hd123.sardine.wms.api.basicInfo.bin.BinState;
import com.hd123.sardine.wms.api.basicInfo.bin.BinUsage;
import com.hd123.sardine.wms.api.basicInfo.container.Container;
import com.hd123.sardine.wms.api.basicInfo.container.ContainerService;
import com.hd123.sardine.wms.api.basicInfo.container.ContainerState;
import com.hd123.sardine.wms.api.stock.Stock;
import com.hd123.sardine.wms.api.stock.StockFilter;
import com.hd123.sardine.wms.api.stock.StockService;
import com.hd123.sardine.wms.api.stock.StockState;
import com.hd123.sardine.wms.api.task.Task;
import com.hd123.sardine.wms.api.task.TaskState;
import com.hd123.sardine.wms.api.task.TaskType;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.service.util.StockBatchUtils;

/**
 * @author zhangsai
 *
 */
public class TaskVerifier {

  @Autowired
  private StockService stockService;

  @Autowired
  private StockBatchUtils stockBatchUtils;

  @Autowired
  private BinService binService;

  @Autowired
  private ContainerService containerService;

  public List<Stock> verifySourceStock(Task task) throws WMSException {
    Assert.assertArgumentNotNull(task, "task");

    task.validate();
    StockFilter stockFilter = new StockFilter();
    stockFilter.setPageSize(0);
    stockFilter.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    stockFilter.setArticleUuid(task.getArticle().getUuid());
    stockFilter.setContainerBarcode(task.getFromContainerBarcode());
    stockFilter.setBinCode(task.getFromBinCode());
    stockFilter.setQpcStr(task.getQpcStr());
    stockFilter.setProductionBatch(stockBatchUtils.genProductionBatch(task.getProductionDate()));
    stockFilter.setStockBatch(task.getStockBatch());
    List<Stock> stocks = stockService.query(stockFilter);

    if (CollectionUtils.isEmpty(stocks))
      throw new WMSException("指令来源库存不足！");

    BigDecimal normalStockQty = calculateStockQty(stocks);
    if (normalStockQty.compareTo(task.getQty()) < 0)
      throw new WMSException("指令来源货位库存不足！");

    if ((TaskType.Putaway.equals(task.getTaskType())
        || TaskType.RtnPutaway.equals(task.getTaskType())
        || TaskType.RtnShelf.equals(task.getTaskType())) && hasOtherStock(stocks))
      throw new WMSException("当前指令不允许存在非正常库存！");
    return stocks;
  }

  private BigDecimal calculateStockQty(List<Stock> stocks) {
    if (CollectionUtils.isEmpty(stocks))
      return BigDecimal.ZERO;

    BigDecimal normalStockQty = BigDecimal.ZERO;
    for (Stock stock : stocks) {
      if (stock.getStockComponent().getState().equals(StockState.normal) == false)
        continue;
      normalStockQty = normalStockQty.add(stock.getStockComponent().getQty());
    }
    return normalStockQty;
  }

  private boolean hasOtherStock(List<Stock> stocks) {
    if (CollectionUtils.isNotEmpty(stocks))
      return false;

    for (Stock stock : stocks) {
      if (stock.getStockComponent().getState().equals(StockState.normal) == false)
        return true;
    }
    return false;
  }

  public void verifyAndRefreshTaskState(Task task) throws WMSException {
    Assert.assertArgumentNotNull(task, "task");

    if (task.getState().equals(TaskState.Initial) == false
        && task.getState().equals(TaskState.InProgress) == false)
      throw new WMSException("当前指令状态为" + task.getState().getCaption() + "，不能执行！");

    if (task.getState().equals(TaskState.Initial))
      task.setBeginOperateTime(new Date());
  }

  public void verifyAndRefreshPutawayTask(Task task, String toBinCode, String toContainerBarcode)
      throws WMSException {
    Assert.assertArgumentNotNull(task, "task");

    verifyAndRefreshTaskState(task);

    if (task.getTaskType().equals(TaskType.Putaway) == false
        && task.getTaskType().equals(TaskType.RtnPutaway) == false)
      throw new WMSException("当前指令不是上架或退仓上架指令！");

    if (StringUtil.isNullOrBlank(toBinCode) == false) {
      Bin toBin = binService.getBinByCode(toBinCode);
      if (toBin == null)
        throw new WMSException("上架目标货位" + toBinCode + "不存在！");
      if (TaskType.Putaway.equals(task.getTaskType())
          && BinUsage.PickUpStorageBin.equals(toBin.getUsage()) == false
          && BinUsage.StorageBin.equals(toBin.getUsage()) == false)
        throw new WMSException("目标货位是" + toBin.getUsage() + "上架的目标货位必须是存储位或者拣货存储位！");
      if (TaskType.RtnPutaway.equals(task.getTaskType())) {
        if (toBin.getUsage().equals(BinUsage.SupplierStorageBin) == false)
          throw new WMSException("目标货位是" + toBin.getUsage() + "上架的目标货位必须是供应商退货位！");
        if (toBin.getState().equals(BinState.free))
          binService.lock(toBin.getUuid(), toBin.getVersion());
        else if (toBin.getState().equals(BinState.using)) {
          StockFilter sf = new StockFilter();
          sf.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
          sf.setBinCode(toBinCode);
          List<Stock> stocks = stockService.query(sf);
          for (Stock stock : stocks) {
            if (stock.getStockComponent().getSupplier().getUuid()
                .equals(task.getSupplier().getUuid()) == false)
              throw new WMSException("目标货位" + toBinCode + "存在其他供应商的库存，请使用其他货位！");
          }
          binService.lock(toBin.getUuid(), toBin.getVersion());
        } else {
          throw new WMSException(
              "目标货位" + toBinCode + "状态为" + toBin.getState().getCaption() + "，不能上架，请使用其他货位！");
        }
      }
      task.setToBinCode(toBinCode);
    }

    if (StringUtil.isNullOrBlank(task.getToBinCode()))
      throw new WMSException("指令目标货位为空，无法上架！");

    Bin toBin = binService.getBinByCode(task.getToBinCode());
    if (ApplicationContextUtil.manageContainer() && (BinUsage.StorageBin.equals(toBin.getUsage())
        || BinUsage.SupplierStorageBin.equals(toBin.getUsage()))) {
      if (StringUtil.isNullOrBlank(toContainerBarcode))
        throw new WMSException("当前仓库管理容器，目标容器不能为空！");
      if (Objects.equals(toContainerBarcode, task.getFromContainerBarcode()))
        throw new WMSException("指令的目标容器不能是来源容器！");
      Container toContainer = containerService.getByBarcode(toContainerBarcode);
      if (toContainer == null)
        throw new WMSException("目标容器" + toContainerBarcode + "不存在！");
      if (toContainer.getState().equals(ContainerState.STACONTAINERIDLE) == false
          && toContainer.getState().equals(ContainerState.STACONTAINERUSEING) == false)
        throw new WMSException("目标容器状态不是空闲或者已使用，无法上架！");
      if (toContainer.getState().equals(ContainerState.STACONTAINERUSEING)
          && toContainer.getPosition().equals(task.getToBinCode()) == false)
        throw new WMSException("上架的目标容器已被其他货位使用，请更换！");
      containerService.lock(toContainer.getUuid(), toContainer.getVersion());
    } else {
      task.setToContainerBarcode(Container.VIRTUALITY_CONTAINER);
    }
  }

  public void verifyAndRefreshUnShelveTask(Task task, String toBinCode, String toContainerBarcode)
      throws WMSException {
    Assert.assertArgumentNotNull(task, "task");
    Assert.assertArgumentNotNull(toContainerBarcode, "toContainerBarcode");

    verifyAndRefreshTaskState(task);

    if (task.getTaskType().equals(TaskType.RtnShelf) == false)
      throw new WMSException("当前指令不是退货下架指令！");

    if (StringUtil.isNullOrBlank(toBinCode) == false) {
      Bin toBin = binService.getBinByCode(toBinCode);
      if (toBin == null)
        throw new WMSException("上架目标货位" + toBinCode + "不存在！");
      if (BinUsage.SupplierCollectBin.equals(toBin.getUsage()) == false)
        throw new WMSException("目标货位是" + toBin.getUsage() + "，不是供应商集货位！");
      task.setToBinCode(toBinCode);
    }

    if (StringUtil.isNullOrBlank(task.getToBinCode()))
      throw new WMSException("指令目标货位为空，无法上架！");

    task.setToContainerBarcode(toContainerBarcode);
    if (Objects.equals(task.getFromContainerBarcode(), task.getToContainerBarcode()))
      throw new WMSException("下架的目标容器不能是来源容器！");
    Container toContainer = containerService.getByBarcode(toContainerBarcode);
    if (toContainer == null)
      throw new WMSException("目标容器" + toContainerBarcode + "不存在！");
    if (toContainer.getState().equals(ContainerState.STACONTAINERIDLE) == false
        && toContainer.getState().equals(ContainerState.STACONTAINERUSEING) == false)
      throw new WMSException("目标容器状态不是空闲或者已使用，无法下架！");
    if (toContainer.getState().equals(ContainerState.STACONTAINERUSEING)
        && toContainer.getPosition().equals(task.getToBinCode()) == false)
      throw new WMSException("下架的目标容器已被其他货位使用，请更换！");
    containerService.lock(toContainer.getUuid(), toContainer.getVersion());
  }
}
