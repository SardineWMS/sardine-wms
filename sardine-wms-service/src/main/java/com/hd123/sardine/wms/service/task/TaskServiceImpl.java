/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	TaskServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月12日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.bin.Bin;
import com.hd123.sardine.wms.api.basicInfo.bin.BinService;
import com.hd123.sardine.wms.api.basicInfo.bin.BinState;
import com.hd123.sardine.wms.api.basicInfo.container.Container;
import com.hd123.sardine.wms.api.basicInfo.container.ContainerService;
import com.hd123.sardine.wms.api.basicInfo.container.ContainerState;
import com.hd123.sardine.wms.api.stock.OnWayStock;
import com.hd123.sardine.wms.api.stock.OnWayStockOutRule;
import com.hd123.sardine.wms.api.stock.ShiftOutRule;
import com.hd123.sardine.wms.api.stock.Stock;
import com.hd123.sardine.wms.api.stock.StockService;
import com.hd123.sardine.wms.api.task.Task;
import com.hd123.sardine.wms.api.task.TaskService;
import com.hd123.sardine.wms.api.task.TaskState;
import com.hd123.sardine.wms.api.task.TaskType;
import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.dao.task.TaskDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;

/**
 * @author zhangsai
 *
 */
public class TaskServiceImpl extends BaseWMSService implements TaskService {

  @Autowired
  private TaskDao taskDao;

  @Autowired
  private StockService stockService;

  @Autowired
  private BinService binService;

  @Autowired
  private ContainerService containerService;

  @Override
  public void insert(List<Task> tasks)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(tasks, "tasks");

    if (tasks.isEmpty())
      return;

    List<OnWayStock> onWayStocks = new ArrayList<OnWayStock>();
    for (Task task : tasks) {
      task.validate();
      task.setUuid(UUIDGenerator.genUUID());
      task.setTaskNo(null);
      task.setCreator(ApplicationContextUtil.getLoginUser());
      task.setCreateTime(new Date());
      taskDao.insert(task);

      OnWayStock fromOnWayStock = new OnWayStock();
      fromOnWayStock.setBinCode(task.getFromBinCode());
      fromOnWayStock.setContainerBarcode(task.getFromContainerBarcode());
      fromOnWayStock.setQty(task.getQty().negate());
      fromOnWayStock.setStockBatch(task.getStockBatch());
      fromOnWayStock.setTaskNo(task.getTaskNo());
      fromOnWayStock.setTaskType(task.getTaskType());
      onWayStocks.add(fromOnWayStock);

      OnWayStock toOnWayStock = new OnWayStock();
      toOnWayStock.setBinCode(task.getToBinCode());
      toOnWayStock.setContainerBarcode(task.getToContainerBarcode());
      toOnWayStock.setQty(task.getQty());
      toOnWayStock.setStockBatch(task.getStockBatch());
      toOnWayStock.setTaskNo(task.getTaskNo());
      toOnWayStock.setTaskType(task.getTaskType());
      onWayStocks.add(toOnWayStock);
    }

    stockService.shiftInOnWayStock(onWayStocks);
  }

  @Override
  public void abort(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");

    Task task = taskDao.get(uuid);
    if (task == null)
      throw new WMSException("作废的指令不存在！");
    PersistenceUtils.checkVersion(version, task, "指令", task.getTaskNo());

    if (task.getTaskType().equals(TaskType.Putaway))
      throw new WMSException("上架指令不能作废！");

    task.setState(TaskState.Aborted);
    task.setOperator(ApplicationContextUtil.getLoginUser());
    task.setBeginOperateTime(new Date());
    task.setEndOperateTime(new Date());
    taskDao.update(task);

    shiftOutOnWayStock(task);
  }

  private void shiftOutOnWayStock(Task task) throws WMSException {
    assert task != null;

    List<OnWayStockOutRule> shiftRules = new ArrayList<OnWayStockOutRule>();
    OnWayStockOutRule fromShiftRule = new OnWayStockOutRule();
    fromShiftRule.setArticleUuid(task.getArticle().getUuid());
    fromShiftRule.setBinCode(task.getFromBinCode());
    fromShiftRule.setContainerBarcode(task.getFromContainerBarcode());
    fromShiftRule.setQty(task.getQty().negate());
    fromShiftRule.setStockBatch(task.getStockBatch());
    fromShiftRule.setTaskNo(task.getTaskNo());
    fromShiftRule.setTaskType(task.getTaskType());
    shiftRules.add(fromShiftRule);
    OnWayStockOutRule toShiftRule = new OnWayStockOutRule();
    toShiftRule.setArticleUuid(task.getArticle().getUuid());
    toShiftRule.setBinCode(task.getToBinCode());
    toShiftRule.setContainerBarcode(task.getToContainerBarcode());
    toShiftRule.setQty(task.getQty());
    toShiftRule.setStockBatch(task.getStockBatch());
    toShiftRule.setTaskNo(task.getTaskNo());
    toShiftRule.setTaskType(task.getTaskType());
    shiftRules.add(toShiftRule);

    stockService.shiftOutOnWayStock(shiftRules);
  }

  @Override
  public void putaway(String uuid, long version, String toBinCode)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");

    Task task = taskDao.get(uuid);
    if (task == null)
      throw new WMSException("指定的指令不存在！");
    PersistenceUtils.checkVersion(version, task, "指令", task.getTaskNo());

    if (task.getTaskType().equals(TaskType.Putaway) == false)
      throw new WMSException("当前指令不是上架指令！");

    shiftOutOnWayStock(task);

    if (StringUtil.isNullOrBlank(toBinCode) == false)
      task.setToBinCode(toBinCode);
    task.setOperator(ApplicationContextUtil.getLoginUser());
    task.setBeginOperateTime(new Date());
    task.setEndOperateTime(new Date());
    taskDao.update(task);

    ShiftOutRule outRule = new ShiftOutRule();
    outRule.setArticleUuid(task.getArticle().getUuid());
    outRule.setBinCode(task.getFromBinCode());
    outRule.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    outRule.setContainerBarcode(task.getFromContainerBarcode());
    outRule.setQpcStr(task.getQpcStr());
    outRule.setSourceBillUuid(task.getSourceBillUuid());
    outRule.setStockBatch(task.getStockBatch());
    outRule.setSupplierUuid(task.getSupplier().getUuid());

    List<Stock> outStocks = stockService.shiftOut(TaskType.Putaway.getCaption(), task.getTaskNo(),
        Arrays.asList(outRule));
    for (Stock stock : outStocks) {
      stock.setBinCode(task.getToBinCode());
      stock.setContainerBarcode(task.getToContainerBarcode());
      stock.setUuid(null);
    }
    stockService.shiftIn(TaskType.Putaway.getCaption(), task.getTaskNo(), outStocks);

    binAndContainer(task);
  }

  private void binAndContainer(Task task) throws WMSException {
    assert task != null;

    if (stockService.binHasStock(task.getFromBinCode()) == false) {
      Bin fromBin = binService.getBinByCode(task.getFromBinCode());
      assert fromBin != null;
      binService.changeState(fromBin.getUuid(), fromBin.getVersion(), BinState.free);
    }
    if (stockService.containerHasStock(task.getFromContainerBarcode()) == false) {
      Container fromContainer = containerService.getByBarcode(task.getFromContainerBarcode(),
          ApplicationContextUtil.getCompanyUuid());
      if (fromContainer != null)
        containerService.change(fromContainer.getUuid(), fromContainer.getVersion(),
            ContainerState.STACONTAINERIDLE, null);
    }

    Bin toBin = binService.getBinByCode(task.getToBinCode());
    assert toBin != null;
    if (toBin.getState().equals(BinState.free))
      binService.changeState(toBin.getUuid(), toBin.getVersion(), BinState.using);

    Container toContainer = containerService.getByBarcode(task.getToContainerBarcode(),
        ApplicationContextUtil.getCompanyUuid());
    if (toContainer != null && toContainer.getState().equals(ContainerState.STACONTAINERIDLE))
      containerService.change(toContainer.getUuid(), toContainer.getVersion(),
          ContainerState.STACONTAINERUSEING, task.getToBinCode());
  }

  @Override
  public PageQueryResult<Task> query(PageQueryDefinition definition) {
    Assert.assertArgumentNotNull(definition, "definition");

    PageQueryResult<Task> pgr = new PageQueryResult<Task>();
    List<Task> list = taskDao.query(definition);
    PageQueryUtil.assignPageInfo(pgr, definition);
    pgr.setRecords(list);
    return pgr;
  }
}
