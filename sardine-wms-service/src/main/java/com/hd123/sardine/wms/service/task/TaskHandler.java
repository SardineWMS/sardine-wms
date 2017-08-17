/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	TaskHandler.java
 * 模块说明：	
 * 修改历史：
 * 2017年7月28日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.task;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.basicInfo.bin.Bin;
import com.hd123.sardine.wms.api.basicInfo.bin.BinService;
import com.hd123.sardine.wms.api.basicInfo.bin.BinState;
import com.hd123.sardine.wms.api.basicInfo.container.Container;
import com.hd123.sardine.wms.api.basicInfo.container.ContainerService;
import com.hd123.sardine.wms.api.basicInfo.container.ContainerState;
import com.hd123.sardine.wms.api.stock.StockChangement;
import com.hd123.sardine.wms.api.stock.StockService;
import com.hd123.sardine.wms.api.stock.StockShiftIn;
import com.hd123.sardine.wms.api.stock.StockShiftRule;
import com.hd123.sardine.wms.api.stock.StockShiftTarget;
import com.hd123.sardine.wms.api.stock.StockState;
import com.hd123.sardine.wms.api.task.Task;
import com.hd123.sardine.wms.api.task.TaskType;
import com.hd123.sardine.wms.common.entity.SourceBill;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.service.util.StockBatchUtils;

/**
 * 指令相关库存和货位容器处理
 * 
 * @author zhangsai
 *
 */
public class TaskHandler {

  @Autowired
  private StockService stockService;

  @Autowired
  private StockBatchUtils stockBatchUtils;

  @Autowired
  private BinService binService;

  @Autowired
  private ContainerService containerService;

  /**
   * 锁定指令对应的库存
   * <p>
   * 将上架指令、退仓上架指令、退货下架指令、装车指令、退货交接指令等对应的来源货位库存改为锁定状态，目标货位不变
   * 
   * @param task
   *          指令， not null
   * @throws WMSException
   *           库存不足时抛出异常
   */
  public void lockStock(Task task) throws WMSException {
    Assert.assertArgumentNotNull(task, "task");

    SourceBill sourceBill = new SourceBill(task.getTaskType().getCaption(), task.getUuid(),
        task.getTaskNo());
    if (TaskType.Move.equals(task.getTaskType()) == false
        && TaskType.Rpl.equals(task.getTaskType()) == false) {
      StockShiftRule shiftRule = new StockShiftRule();
      shiftRule.setArticleUuid(task.getArticle().getUuid());
      shiftRule.setBinCode(task.getFromBinCode());
      shiftRule.setOwner(ApplicationContextUtil.getCompanyUuid());
      shiftRule.setContainerBarcode(task.getFromContainerBarcode());
      shiftRule.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      shiftRule.setProductionBatch(stockBatchUtils.genProductionBatch(task.getProductionDate()));
      shiftRule.setStockBatch(task.getStockBatch());
      shiftRule.setSupplierUuid(task.getSupplier().getUuid());
      shiftRule.setQty(task.getQty());
      stockService.changeState(sourceBill, Arrays.asList(shiftRule), StockState.normal,
          StockState.locked);
    } else {
      StockShiftRule shiftRule = new StockShiftRule();
      shiftRule.setArticleUuid(task.getArticle().getUuid());
      shiftRule.setBinCode(task.getFromBinCode());
      shiftRule.setOwner(ApplicationContextUtil.getCompanyUuid());
      shiftRule.setContainerBarcode(task.getFromContainerBarcode());
      shiftRule.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      shiftRule.setProductionBatch(stockBatchUtils.genProductionBatch(task.getProductionDate()));
      shiftRule.setStockBatch(task.getStockBatch());
      shiftRule.setState(StockState.normal);
      shiftRule.setSupplierUuid(task.getSupplier().getUuid());
      shiftRule.setQty(task.getQty());

      List<StockChangement> changements = stockService.changeState(sourceBill,
          Arrays.asList(shiftRule), StockState.normal, StockState.forMoveOut);
      List<StockShiftIn> shiftIns = new ArrayList<StockShiftIn>();
      for (StockChangement changement : changements) {
        StockShiftIn shiftIn = new StockShiftIn();
        shiftIn.setStockComponent(changement.getStockComponent());
        shiftIn.getStockComponent().setState(StockState.forMoveIn);
        shiftIns.add(shiftIn);
      }
      stockService.shiftIn(sourceBill, shiftIns);
    }
  }

  /**
   * 释放指令锁定的库存
   * <p>
   * 将上架指令、退仓上架指令、退货下架指令、装车指令、退货交接指令等锁定的库存改为正常状态<br>
   * 拣货指令将待拣货库存出库<br>
   * 补货指令将来源货位库存变为正常，目标货位库存出库
   * 
   * @param task
   *          指令，not null
   * @throws WMSException
   *           库存不足时抛出异常
   */
  public void releaseStock(Task task) throws WMSException {
    Assert.assertArgumentNotNull(task, "task");

    SourceBill sourceBill = new SourceBill(task.getTaskType().getCaption(), task.getUuid(),
        task.getTaskNo());
    if (TaskType.Move.equals(task.getTaskType()) == false
        && TaskType.Rpl.equals(task.getTaskType()) == false) {
      StockShiftRule shiftRule = new StockShiftRule();
      shiftRule.setArticleUuid(task.getArticle().getUuid());
      shiftRule.setBinCode(task.getFromBinCode());
      shiftRule.setOwner(ApplicationContextUtil.getCompanyUuid());
      shiftRule.setContainerBarcode(task.getFromContainerBarcode());
      shiftRule.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      shiftRule.setProductionBatch(stockBatchUtils.genProductionBatch(task.getProductionDate()));
      shiftRule.setStockBatch(task.getStockBatch());
      shiftRule.setSupplierUuid(task.getSupplier().getUuid());
      shiftRule.setQty(task.getQty());
      shiftRule.setState(StockState.locked);
      shiftRule.setOperateBillUuid(task.getUuid());
      stockService.changeState(sourceBill, Arrays.asList(shiftRule), StockState.locked,
          StockState.normal);
    } else {
      StockShiftRule shiftRule = new StockShiftRule();
      shiftRule.setArticleUuid(task.getArticle().getUuid());
      shiftRule.setBinCode(task.getFromBinCode());
      shiftRule.setOwner(ApplicationContextUtil.getCompanyUuid());
      shiftRule.setContainerBarcode(task.getFromContainerBarcode());
      shiftRule.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      shiftRule.setQty(task.getQty());
      shiftRule.setOperateBillUuid(task.getUuid());
      shiftRule.setState(StockState.forMoveOut);
      stockService.changeState(sourceBill, Arrays.asList(shiftRule), StockState.forMoveOut,
          StockState.normal);

      shiftRule.setBinCode(task.getToBinCode());
      shiftRule.setOwner(ApplicationContextUtil.getCompanyUuid());
      shiftRule.setContainerBarcode(task.getToContainerBarcode());
      shiftRule.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      shiftRule.setQty(task.getQty());
      shiftRule.setState(StockState.forMoveIn);
      stockService.shiftOut(sourceBill, Arrays.asList(shiftRule));
    }
  }

  public void shiftStock(Task task) throws WMSException {
    Assert.assertArgumentNotNull(task, "task");

    releaseStock(task);

    if (task.getRealQty().compareTo(BigDecimal.ZERO) <= 0)
      return;

    SourceBill sourceBill = new SourceBill(task.getTaskType().getCaption(), task.getUuid(),
        task.getTaskNo());

    StockShiftRule shiftRule = new StockShiftRule();
    shiftRule.setArticleUuid(task.getArticle().getUuid());
    shiftRule.setBinCode(task.getFromBinCode());
    shiftRule.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    shiftRule.setContainerBarcode(task.getFromContainerBarcode());
    shiftRule.setOperateBillUuid(task.getUuid());
    shiftRule.setOwner(task.getOwner());
    shiftRule.setProductionBatch(stockBatchUtils.genProductionBatch(task.getProductionDate()));
    shiftRule.setQpcStr(task.getQpcStr());
    shiftRule.setQty(task.getRealQty());
    shiftRule.setStockBatch(task.getStockBatch());
    shiftRule.setSupplierUuid(task.getSupplier().getUuid());
    StockShiftTarget shiftTarget = new StockShiftTarget();
    shiftTarget.setBinCode(task.getToBinCode());
    shiftTarget.setContainerBarCode(task.getToContainerBarcode());
    shiftTarget.setState(StockState.normal);
    stockService.shift(sourceBill, Arrays.asList(shiftRule), shiftTarget);
  }

  public void manageBinAndContainer(Task task) throws WMSException {
    Assert.assertArgumentNotNull(task, "task");

    Bin fromBin = binService.getBinByCode(task.getFromBinCode());
    if (stockService.hasBinStock(task.getFromBinCode()) == false)
      binService.changeState(fromBin.getUuid(), fromBin.getVersion(), BinState.free);

    if (Objects.equals(task.getFromContainerBarcode(), Container.VIRTUALITY_CONTAINER) == false) {
      Container fromContainer = containerService.getByBarcode(task.getFromContainerBarcode());
      if (fromContainer == null)
        throw new WMSException("指令来源容器" + task.getFromContainerBarcode() + "不存在！");
      containerService.recycle(fromContainer.getUuid(), fromContainer.getVersion());
    }

    Bin toBin = binService.getBinByCode(task.getToBinCode());
    if (toBin.getState().equals(BinState.free))
      binService.changeState(toBin.getUuid(), toBin.getVersion(), BinState.using);

    if (Objects.equals(task.getFromContainerBarcode(), task.getToContainerBarcode()) == false
        && Objects.equals(task.getToContainerBarcode(), Container.VIRTUALITY_CONTAINER) == false) {
      Container toContainer = containerService.getByBarcode(task.getToContainerBarcode());
      if (toContainer == null)
        throw new WMSException("指令目标容器" + task.getToContainerBarcode() + "不存在！");
      if (ContainerState.STACONTAINERLOCK.equals(toContainer.getState()) == false)
        throw new WMSException("指令目标容器" + task.getToContainerBarcode() + "状态不是锁定！");
      containerService.using(toContainer.getUuid(), toContainer.getVersion(), task.getToBinCode());
    }
  }
}
