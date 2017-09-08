/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	SupplierReturnBillHandler.java
 * 模块说明：	
 * 修改历史：
 * 2017年9月8日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.rtn.supplierreturn;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.bin.Bin;
import com.hd123.sardine.wms.api.basicInfo.bin.BinService;
import com.hd123.sardine.wms.api.basicInfo.bin.BinState;
import com.hd123.sardine.wms.api.basicInfo.container.Container;
import com.hd123.sardine.wms.api.basicInfo.container.ContainerService;
import com.hd123.sardine.wms.api.rtn.returnsupplier.ReturnSupplierBill;
import com.hd123.sardine.wms.api.rtn.returnsupplier.ReturnSupplierBillItem;
import com.hd123.sardine.wms.api.rtn.returnsupplier.ReturnSupplierBillState;
import com.hd123.sardine.wms.api.rtn.rtnsupplierntc.RtnSupplierNtcBillService;
import com.hd123.sardine.wms.api.stock.StockService;
import com.hd123.sardine.wms.api.stock.StockShiftRule;
import com.hd123.sardine.wms.api.stock.StockState;
import com.hd123.sardine.wms.api.tms.shipbill.OperateMethod;
import com.hd123.sardine.wms.common.entity.SourceBill;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.QpcHelper;
import com.hd123.sardine.wms.service.util.StockBatchUtils;

/**
 * @author zhangsai
 *
 */
public class SupplierReturnBillHandler {

  @Autowired
  private StockService stockService;

  @Autowired
  private StockBatchUtils stockBatchUtils;

  @Autowired
  private BinService binService;

  @Autowired
  private ContainerService containerService;

  @Autowired
  private RtnSupplierNtcBillService rtnSupplierNtcBillService;

  public List<ReturnSupplierBill> generateReturnSupplierBill(List<ReturnSupplierBillItem> items)
      throws WMSException {
    Assert.assertArgumentNotNull(items, "items");

    List<ReturnSupplierBill> result = new ArrayList<ReturnSupplierBill>();
    for (ReturnSupplierBillItem item : items) {
      Bin bin = binService.getBinByCode(item.getBinCode());
      if (bin == null)
        throw new WMSException("货位" + item.getBinCode() + "不存在！");
      ReturnSupplierBill bill = findReturnSupplierBill(result, item.getSupplier().getUuid(),
          bin.getWrh().getUuid(), item.getOwner());
      if (bill == null) {
        bill = new ReturnSupplierBill();
        bill.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
        bill.setMethod(OperateMethod.ManualBill);
        bill.setReturner(ApplicationContextUtil.getLoginUser());
        bill.setReturnSupplierDate(new Date());
        bill.setState(ReturnSupplierBillState.Initial);
        bill.setSupplier(item.getSupplier());
        bill.setTotalAmount(item.getPrice().multiply(item.getQty()));
        bill.setTotalCaseQty(item.getCaseQtyStr());
        bill.setWrh(bin.getWrh());
        bill.setRtnSupplierNtcBillNumber(item.getOwner());
        result.add(bill);
      } else {
        bill.setTotalAmount(bill.getTotalAmount().add(item.getPrice().multiply(item.getQty())));
        bill.setTotalCaseQty(QpcHelper.caseQtyStrAdd(bill.getTotalCaseQty(), item.getCaseQtyStr()));
      }
      bill.getItems().add(item);
    }
    return result;
  }

  private ReturnSupplierBill findReturnSupplierBill(List<ReturnSupplierBill> bills,
      String supplierUuid, String wrhUuid, String rtnSupplierNtcBillNumber) {
    assert bills != null;
    assert supplierUuid != null;
    assert wrhUuid != null;
    assert rtnSupplierNtcBillNumber != null;

    for (ReturnSupplierBill bill : bills) {
      if (supplierUuid.equals(bill.getSupplier().getUuid())
          && wrhUuid.equals(bill.getWrh().getUuid())
          && rtnSupplierNtcBillNumber.equals(bill.getRtnSupplierNtcBillNumber()))
        return bill;
    }
    return null;
  }

  public void stockOut(ReturnSupplierBill returnSupplierBill, List<ReturnSupplierBillItem> items)
      throws WMSException {
    Assert.assertArgumentNotNull(returnSupplierBill, "returnSupplierBill");
    Assert.assertArgumentNotNull(items, "items");

    if (items.isEmpty())
      return;

    List<StockShiftRule> rules = new ArrayList<StockShiftRule>();
    for (ReturnSupplierBillItem item : items) {
      StockShiftRule rule = new StockShiftRule();
      rule.setArticleUuid(item.getArticle().getUuid());
      rule.setBinCode(item.getBinCode());
      rule.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      rule.setContainerBarcode(item.getContainerBarcode());
      rule.setOwner(returnSupplierBill.getRtnSupplierNtcBillNumber());
      rule.setProductionBatch(stockBatchUtils.genProductionBatch(item.getProductionDate()));
      rule.setQpcStr(item.getQpcStr());
      rule.setQty(item.getQty());
      rule.setState(StockState.normal);
      rule.setStockBatch(item.getStockBatch());
      rule.setSupplierUuid(returnSupplierBill.getSupplier().getUuid());
      rules.add(rule);

      rtnSupplierNtcBillService.handover(returnSupplierBill.getRtnSupplierNtcBillNumber(),
          item.getArticle().getUuid(), item.getQty());
    }

    SourceBill sourceBill = new SourceBill(ReturnSupplierBill.CAPTION, returnSupplierBill.getUuid(),
        returnSupplierBill.getBillNumber());
    stockService.shiftOut(sourceBill, rules);
  }

  public void releaseBinAndContainer(ReturnSupplierBill returnSupplierBill,
      List<ReturnSupplierBillItem> items) throws WMSException {
    Assert.assertArgumentNotNull(returnSupplierBill, "returnSupplierBill");
    Assert.assertArgumentNotNull(items, "items");

    if (items.isEmpty())
      return;

    Set<String> binCodes = new HashSet<String>();
    Set<String> containerBarcodes = new HashSet<String>();
    for (ReturnSupplierBillItem item : items) {
      binCodes.add(item.getBinCode());
      if (StringUtil.isNullOrBlank(item.getContainerBarcode()) == false
          && Container.VIRTUALITY_CONTAINER.equals(item.getContainerBarcode()) == false)
        containerBarcodes.add(item.getContainerBarcode());
    }

    for (String binCode : binCodes) {
      Bin bin = binService.getBinByCode(binCode);
      if (stockService.hasBinStock(binCode))
        continue;
      binService.changeState(bin.getUuid(), bin.getVersion(), BinState.free);
    }

    for (String containerBarcode : containerBarcodes) {
      Container container = containerService.getByBarcode(containerBarcode);
      containerService.recycle(container.getUuid(), container.getVersion());
    }
  }
}
