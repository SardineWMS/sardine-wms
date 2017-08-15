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

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.stock.StockService;
import com.hd123.sardine.wms.api.stock.StockShiftRule;
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
    stockService.changeState(
        new SourceBill(task.getTaskType().getCaption(), task.getUuid(), task.getTaskNo()),
        Arrays.asList(shiftRule), StockState.normal, StockState.locked);
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

    if (TaskType.Pickup.equals(task.getTaskType()) == false
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
      stockService.changeState(
          new SourceBill(task.getTaskType().getCaption(), task.getUuid(), task.getTaskNo()),
          Arrays.asList(shiftRule), StockState.locked, StockState.normal);
    } else if (TaskType.Pickup.equals(task.getTaskType())) {
      StockShiftRule shiftRule = new StockShiftRule();
      shiftRule.setArticleUuid(task.getArticle().getUuid());
      shiftRule.setBinCode(task.getFromBinCode());
      shiftRule.setOwner(ApplicationContextUtil.getCompanyUuid());
      shiftRule.setContainerBarcode(task.getFromContainerBarcode());
      shiftRule.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      shiftRule.setQty(task.getQty());
      shiftRule.setOperateBillUuid(task.getUuid());
      shiftRule.setState(StockState.forPick);
      stockService.shiftOut(
          new SourceBill(task.getTaskType().getCaption(), task.getUuid(), task.getTaskNo()),
          Arrays.asList(shiftRule));
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
      stockService.changeState(
          new SourceBill(task.getTaskType().getCaption(), task.getUuid(), task.getTaskNo()),
          Arrays.asList(shiftRule), StockState.forMoveOut, StockState.normal);

      shiftRule.setBinCode(task.getToBinCode());
      shiftRule.setOwner(ApplicationContextUtil.getCompanyUuid());
      shiftRule.setContainerBarcode(task.getToContainerBarcode());
      shiftRule.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      shiftRule.setQty(task.getQty());
      shiftRule.setState(StockState.forMoveIn);
      stockService.shiftOut(
          new SourceBill(task.getTaskType().getCaption(), task.getUuid(), task.getTaskNo()),
          Arrays.asList(shiftRule));
    }
  }
}
