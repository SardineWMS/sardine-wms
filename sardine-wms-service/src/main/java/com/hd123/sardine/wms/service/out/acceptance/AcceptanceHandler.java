/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	AcceptanceHandler.java
 * 模块说明：	
 * 修改历史：
 * 2017年7月20日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.out.acceptance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.basicInfo.bin.Bin;
import com.hd123.sardine.wms.api.basicInfo.container.Container;
import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBill;
import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBillItem;
import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBillState;
import com.hd123.sardine.wms.api.stock.StockExtendInfo;
import com.hd123.sardine.wms.api.stock.StockFilter;
import com.hd123.sardine.wms.api.stock.StockService;
import com.hd123.sardine.wms.api.stock.StockShiftRule;
import com.hd123.sardine.wms.api.stock.StockState;
import com.hd123.sardine.wms.api.task.Task;
import com.hd123.sardine.wms.api.task.TaskService;
import com.hd123.sardine.wms.api.task.TaskState;
import com.hd123.sardine.wms.api.task.TaskType;
import com.hd123.sardine.wms.common.entity.SourceBill;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.QpcHelper;
import com.hd123.sardine.wms.service.util.StockBatchUtils;

/**
 * @author zhangsai
 *
 */
public class AcceptanceHandler {

  @Autowired
  private StockService stockService;

  @Autowired
  private TaskService taskService;

  @Autowired
  private StockBatchUtils stockBatchUtils;

  public void shiftInOnWayStock(AcceptanceBill acceptanceBill) throws WMSException {
    Assert.assertArgumentNotNull(acceptanceBill, "acceptanceBill");

    StockFilter stockFilter = new StockFilter();
    stockFilter.setPageSize(0);
    stockFilter.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    List<StockShiftRule> outRules = new ArrayList<StockShiftRule>();
    for (AcceptanceBillItem acceptanceItem : acceptanceBill.getItems()) {
      stockFilter.setArticleUuid(acceptanceItem.getArticle().getUuid());
      stockFilter.setBinCode(acceptanceItem.getBinCode());
      stockFilter.setContainerBarcode(acceptanceItem.getContainerBarCode());
      stockFilter.setProductionBatch(
          stockBatchUtils.genProductionBatch(acceptanceItem.getProductionDate()));
      stockFilter.setQpcStr(acceptanceItem.getQpcStr());
      stockFilter.setSupplierUuid(acceptanceItem.getSupplier().getUuid());
      List<StockExtendInfo> infos = stockService.queryStockExtendInfo(stockFilter);
      BigDecimal acceptanceQty = acceptanceItem.getQty();
      for (StockExtendInfo info : infos) {
        if (BigDecimal.ZERO.compareTo(acceptanceQty) >= 0)
          break;
        if (info.getQty().compareTo(BigDecimal.ZERO) <= 0)
          continue;

        StockShiftRule outRule = new StockShiftRule();
        outRule.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
        outRule.setOwner(ApplicationContextUtil.getCompanyUuid());
        outRule.setArticleUuid(acceptanceItem.getArticle().getUuid());
        outRule.setBinCode(acceptanceItem.getBinCode());
        outRule.setContainerBarcode(acceptanceItem.getContainerBarCode());
        outRule.setStockBatch(info.getStockBatch());
        if (acceptanceQty.compareTo(info.getQty()) >= 0) {
          outRule.setQty(info.getQty());
        } else {
          outRule.setQty(acceptanceQty);
        }
        acceptanceItem.setPlanQty(acceptanceItem.getPlanQty().add(outRule.getQty()));
        acceptanceItem.setPlanCaseQtyStr(
            QpcHelper.qtyToCaseQtyStr(acceptanceItem.getPlanQty(), acceptanceItem.getQpcStr()));
        acceptanceQty = acceptanceQty.subtract(outRule.getQty());
        outRules.add(outRule);
      }
    }
    stockService.changeState(
        new SourceBill("要货单", acceptanceBill.getUuid(), acceptanceBill.getBillNumber()), outRules,
        StockState.normal, StockState.locked);
  }

  public void shiftOutOnWayStock(AcceptanceBill acceptanceBill) throws WMSException {
    Assert.assertArgumentNotNull(acceptanceBill, "acceptanceBill");

    List<StockShiftRule> outRules = new ArrayList<StockShiftRule>();
    for (AcceptanceBillItem acceptanceItem : acceptanceBill.getItems()) {
      if (acceptanceItem.getPlanQty().compareTo(BigDecimal.ZERO) <= 0)
        continue;

      StockShiftRule outRule = new StockShiftRule();
      outRule.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      outRule.setOwner(ApplicationContextUtil.getCompanyUuid());
      outRule.setArticleUuid(acceptanceItem.getArticle().getUuid());
      outRule.setBinCode(acceptanceItem.getBinCode());
      outRule.setContainerBarcode(acceptanceItem.getContainerBarCode());
      outRule.setOperateBillUuid(acceptanceBill.getUuid());
      outRule.setQty(acceptanceItem.getPlanQty());
    }
    stockService.changeState(
        new SourceBill("要货单", acceptanceBill.getUuid(), acceptanceBill.getBillNumber()), outRules,
        StockState.locked, StockState.normal);
  }

  public void generateAndSavePickUpTasks(AcceptanceBill acceptanceBill) throws WMSException {
    Assert.assertArgumentNotNull(acceptanceBill, "acceptanceBill");

    List<Task> pickTasks = new ArrayList<>();
    StockFilter stockFilter = new StockFilter();
    stockFilter.setPageSize(0);
    stockFilter.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    for (AcceptanceBillItem item : acceptanceBill.getItems()) {
      Task task = new Task();
      task.setArticle(item.getArticle());
      task.setFromBinCode(item.getBinCode());
      task.setFromContainerBarcode(item.getContainerBarCode());
      task.setToBinCode(Bin.VIRTUALITY_BIN);
      task.setToContainerBarcode(Container.VIRTUALITY_CONTAINER);
      task.setProductionDate(item.getProductionDate());
      task.setQpcStr(item.getQpcStr());
      task.setSourceBillLine(item.getLine());
      task.setSourceBillNumber(acceptanceBill.getBillNumber());
      task.setSourceBillUuid(acceptanceBill.getUuid());
      task.setSourceBillType(AcceptanceBill.CAPTION);
      task.setSupplier(item.getSupplier());
      task.setTaskType(TaskType.Pickup);
      task.setValidDate(item.getValidDate());
      task.setOwner(acceptanceBill.getCustomer().getUuid());
      task.setCreator(ApplicationContextUtil.getLoginUser());
      task.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      if (item.getPlanQty().compareTo(BigDecimal.ZERO) <= 0) {
        stockFilter.setBinCode(item.getBinCode());
        stockFilter.setContainerBarcode(item.getContainerBarCode());
        stockFilter.setArticleUuid(item.getArticle().getUuid());
        stockFilter.setQpcStr(item.getQpcStr());
        stockFilter.setSupplierUuid(item.getSupplier().getUuid());
        stockFilter
            .setProductionBatch(stockBatchUtils.genProductionBatch(item.getProductionDate()));
        List<StockExtendInfo> infos = stockService.queryStockExtendInfo(stockFilter);
        BigDecimal canAcceptanceQty = BigDecimal.ZERO;
        for (StockExtendInfo info : infos) {
          canAcceptanceQty = canAcceptanceQty.add(info.getQty());
        }
        if (item.getQty().compareTo(canAcceptanceQty) <= 0)
          task.setQty(item.getQty());
        else
          task.setQty(canAcceptanceQty);
        item.setPlanQty(item.getPlanQty().add(task.getQty()));
        item.setPlanCaseQtyStr(QpcHelper.qtyToCaseQtyStr(item.getPlanQty(), item.getQpcStr()));
      }
      task.setCaseQtyStr(QpcHelper.qtyToCaseQtyStr(task.getQty(), task.getQpcStr()));
      pickTasks.add(task);
    }

    if (AcceptanceBillState.Approved.equals(acceptanceBill.getState()))
      shiftInOnWayStock(acceptanceBill);
    taskService.insert(pickTasks);
  }

  public void abortUnFinishPickTasks(String acceptanceBillUuid) throws WMSException {
    Assert.assertArgumentNotNull(acceptanceBillUuid, "acceptanceBillUuid");

    PageQueryDefinition definition = new PageQueryDefinition();
    definition.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    definition.setPageSize(0);
    definition.put(TaskService.QUERY_FIELD_SOURCEBILLUUID, acceptanceBillUuid);
    definition.put(TaskService.QUERY_FIELD_TASKTYPE, TaskType.Pickup);
    definition.put(TaskService.QUERY_FIELD_STATE, TaskState.Initial);
    PageQueryResult<Task> initialTasks = taskService.query(definition);
    for (Task task : initialTasks.getRecords()) {
      taskService.abort(task.getUuid(), task.getVersion());
    }
    definition.put(TaskService.QUERY_FIELD_STATE, TaskState.InProgress);
    PageQueryResult<Task> inProgressTasks = taskService.query(definition);
    for (Task task : inProgressTasks.getRecords()) {
      taskService.abort(task.getUuid(), task.getVersion());
    }
  }
}
