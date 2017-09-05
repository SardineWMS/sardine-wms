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
import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBill;
import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBillItem;
import com.hd123.sardine.wms.api.out.pickup.PickType;
import com.hd123.sardine.wms.api.out.pickup.PickUpBill;
import com.hd123.sardine.wms.api.out.pickup.PickUpBillItem;
import com.hd123.sardine.wms.api.out.pickup.PickUpBillService;
import com.hd123.sardine.wms.api.out.pickup.PickUpBillState;
import com.hd123.sardine.wms.api.out.wave.WaveBinUsage;
import com.hd123.sardine.wms.api.stock.Stock;
import com.hd123.sardine.wms.api.stock.StockFilter;
import com.hd123.sardine.wms.api.stock.StockService;
import com.hd123.sardine.wms.api.stock.StockShiftRule;
import com.hd123.sardine.wms.api.stock.StockState;
import com.hd123.sardine.wms.common.entity.OperateMode;
import com.hd123.sardine.wms.common.entity.SourceBill;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.exception.WMSException;
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
  private StockBatchUtils stockBatchUtils;
  
  @Autowired
  private PickUpBillService pickUpBillService;

  public void lockStock(AcceptanceBill acceptanceBill) throws WMSException {
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
      List<Stock> stocks = stockService.query(stockFilter);
      BigDecimal acceptanceQty = acceptanceItem.getQty();
      for (Stock stock : stocks) {
        if (BigDecimal.ZERO.compareTo(acceptanceQty) >= 0)
          break;
        if (stock.getStockComponent().getQty().compareTo(BigDecimal.ZERO) <= 0)
          continue;

        StockShiftRule lockRule = new StockShiftRule();
        lockRule.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
        lockRule.setOwner(ApplicationContextUtil.getCompanyUuid());
        lockRule.setArticleUuid(acceptanceItem.getArticle().getUuid());
        lockRule.setBinCode(acceptanceItem.getBinCode());
        lockRule.setContainerBarcode(acceptanceItem.getContainerBarCode());
        lockRule.setStockBatch(stock.getStockComponent().getStockBatch());
        if (acceptanceQty.compareTo(stock.getStockComponent().getQty()) >= 0) {
          lockRule.setQty(stock.getStockComponent().getQty());
        } else {
          lockRule.setQty(acceptanceQty);
        }
        acceptanceItem.setPlanQty(acceptanceItem.getPlanQty().add(lockRule.getQty()));
        acceptanceItem.setPlanCaseQtyStr(
            QpcHelper.qtyToCaseQtyStr(acceptanceItem.getPlanQty(), acceptanceItem.getQpcStr()));
        acceptanceQty = acceptanceQty.subtract(lockRule.getQty());
        outRules.add(lockRule);
      }
    }
    stockService.changeState(
        new SourceBill("要货单", acceptanceBill.getUuid(), acceptanceBill.getBillNumber()), outRules,
        StockState.normal, StockState.locked);
  }
  
  

  public void rollBackStock(AcceptanceBill acceptanceBill)
      throws WMSException {
    Assert.assertArgumentNotNull(acceptanceBill, "acceptanceBill");

    SourceBill sourceBill = new SourceBill("要货单", acceptanceBill.getUuid(),
        acceptanceBill.getBillNumber());
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
    stockService.changeState(sourceBill, outRules, StockState.locked, StockState.normal);
  }

  public void generateAndSavePickUpTasks(AcceptanceBill acceptanceBill) throws WMSException {
    Assert.assertArgumentNotNull(acceptanceBill, "acceptanceBill");

    List<PickUpBillItem> pickItems = new ArrayList<PickUpBillItem>();
    for (AcceptanceBillItem item : acceptanceBill.getItems()) {
      if (item.getPlanQty().compareTo(BigDecimal.ZERO) <= 0)
        continue;
      PickUpBillItem pickItem = new PickUpBillItem();
      pickItem.setAlcNtcBillItemUuid(item.getUuid());
      pickItem.setAlcNtcBillNumber(acceptanceBill.getBillNumber());
      pickItem.setAlcNtcBillUuid(acceptanceBill.getUuid());
      pickItem.setArticle(item.getArticle());
      pickItem.setArticleSpec(item.getArticleSpec());
      pickItem.setMunit(item.getMunit());
      pickItem.setBinUsage(WaveBinUsage.DynamicPickBin);
      pickItem.setQpcStr(item.getQpcStr());
      pickItem.setQty(item.getPlanQty());
      pickItem.setSourceBinCode(item.getBinCode());
      pickItem.setSourceContainerBarcode(item.getContainerBarCode());
      pickItem.setCaseQtyStr(item.getPlanCaseQtyStr());
      pickItems.add(pickItem);
    }

    if (pickItems.isEmpty() == false) {
      PickUpBill pickBill = new PickUpBill();
      pickBill.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      pickBill.setCustomer(acceptanceBill.getCustomer());
      pickBill.setDeliveryType(acceptanceBill.getDeliveryType());
      pickBill.setMethod(OperateMode.ManualBill);
      pickBill.setPickArea(new UCN("-", "-", "-"));
      pickBill.setPickOrder("-");
      pickBill.setState(PickUpBillState.approved);
      pickBill.setSourceBill(new SourceBill("领用单", acceptanceBill.getUuid(), acceptanceBill.getBillNumber()));
      pickBill.setType(PickType.PartContainer);
      pickBill.setItems(pickItems);
      pickUpBillService.saveNew(pickBill);
    }
  }

  public void abortUnFinishPickTasks(String acceptanceBillUuid) throws WMSException {
    /*
     * Assert.assertArgumentNotNull(acceptanceBillUuid, "acceptanceBillUuid");
     * 
     * PageQueryDefinition definition = new PageQueryDefinition();
     * definition.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
     * definition.setPageSize(0);
     * definition.put(TaskService.QUERY_FIELD_SOURCEBILLUUID,
     * acceptanceBillUuid); definition.put(TaskService.QUERY_FIELD_TASKTYPE,
     * TaskType.Pickup); definition.put(TaskService.QUERY_FIELD_STATE,
     * TaskState.Initial); PageQueryResult<Task> initialTasks =
     * taskService.query(definition); for (Task task :
     * initialTasks.getRecords()) { taskService.abort(task.getUuid(),
     * task.getVersion()); } definition.put(TaskService.QUERY_FIELD_STATE,
     * TaskState.InProgress); PageQueryResult<Task> inProgressTasks =
     * taskService.query(definition); for (Task task :
     * inProgressTasks.getRecords()) { taskService.abort(task.getUuid(),
     * task.getVersion()); }
     */}
}
