/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	OrderBill.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月13日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.in.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.StandardEntity;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.utils.QpcHelper;

/**
 * 订单：实体
 * 
 * @author zhangsai
 *
 */
public class OrderBill extends StandardEntity {
  private static final long serialVersionUID = -4153662922892515359L;

  private String billNumber;
  private String sourceBillNumber;
  private String billType;
  private UCN supplier;
  private UCN wrh;
  private Date expireDate;
  private Date bookedDate;
  private OrderBillState state = OrderBillState.Initial;
  private String companyUuid;
  private String totalCaseQtyStr;
  private String totalReceivedCaseQtyStr;
  private BigDecimal totalAmount;
  private BigDecimal totalReceiveAmount;
  private String note;

  private List<OrderBillItem> items = new ArrayList<OrderBillItem>();

  /** 单号 */
  public String getBillNumber() {
    return billNumber;
  }

  public void setBillNumber(String billNumber) {
    this.billNumber = billNumber;
  }

  /** 来源单号 */
  public String getSourceBillNumber() {
    return sourceBillNumber;
  }

  public void setSourceBillNumber(String sourceBillNumber) {
    this.sourceBillNumber = sourceBillNumber;
  }

  /** 单据类型 */
  public String getBillType() {
    return billType;
  }

  public void setBillType(String billType) {
    this.billType = billType;
  }

  /** 供应商 */
  public UCN getSupplier() {
    return supplier;
  }

  public void setSupplier(UCN supplier) {
    this.supplier = supplier;
  }

  /** 仓位 */
  public UCN getWrh() {
    return wrh;
  }

  public void setWrh(UCN wrh) {
    this.wrh = wrh;
  }

  /** 到效日期 */
  public Date getExpireDate() {
    return expireDate;
  }

  public void setExpireDate(Date expireDate) {
    this.expireDate = expireDate;
  }

  /** 预约到货日期 */
  public Date getBookedDate() {
    return bookedDate;
  }

  public void setBookedDate(Date bookedDate) {
    this.bookedDate = bookedDate;
  }

  /** 状态 */
  public OrderBillState getState() {
    return state;
  }

  public void setState(OrderBillState state) {
    this.state = state;
  }

  /** 组织ID */
  public String getCompanyUuid() {
    return companyUuid;
  }

  public void setCompanyUuid(String companyUuid) {
    this.companyUuid = companyUuid;
  }

  /** 订单总订货件数 */
  public String getTotalCaseQtyStr() {
    return totalCaseQtyStr;
  }

  public void setTotalCaseQtyStr(String totalCaseQtyStr) {
    this.totalCaseQtyStr = totalCaseQtyStr;
  }

  /** 已收货件数 */
  public String getTotalReceivedCaseQtyStr() {
    return totalReceivedCaseQtyStr;
  }

  public void setTotalReceivedCaseQtyStr(String totalReceivedCaseQtyStr) {
    this.totalReceivedCaseQtyStr = totalReceivedCaseQtyStr;
  }

  /** 总金额，明细金额之和 */
  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }

  public BigDecimal getTotalReceiveAmount() {
    return totalReceiveAmount;
  }

  public void setTotalReceiveAmount(BigDecimal totalReceiveAmount) {
    this.totalReceiveAmount = totalReceiveAmount;
  }

  /** 备注 */
  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  /** 明细 */
  public List<OrderBillItem> getItems() {
    return items;
  }

  public void setItems(List<OrderBillItem> items) {
    this.items = items;
  }

  public void refreshTotalCaseQtyStr() {
    totalCaseQtyStr = "0";
    for (OrderBillItem item : items) {
      item.setCaseQtyStr(QpcHelper.qtyToCaseQtyStr(item.getQty(), item.getQpcStr()));
      totalCaseQtyStr = QpcHelper.caseQtyStrAdd(totalCaseQtyStr, item.getCaseQtyStr());
    }
  }

  public void refreshTotalReceiveCaseQtyStr() {
    totalReceivedCaseQtyStr = "0";
    for (OrderBillItem item : items) {
      if (item.getReceivedQty() == null)
        item.setReceivedQty(BigDecimal.ZERO);
      item.setReceivedCaseQtyStr(
          QpcHelper.qtyToCaseQtyStr(item.getReceivedQty(), item.getQpcStr()));
      totalReceivedCaseQtyStr = QpcHelper.caseQtyStrAdd(totalReceivedCaseQtyStr,
          item.getReceivedCaseQtyStr());
    }
  }

  public void refreshTotalAmount() {
    totalAmount = BigDecimal.ZERO;
    for (OrderBillItem item : items) {
      totalAmount = totalAmount.add(item.getPrice().multiply(item.getQty()));
    }
  }

  public void refreshTotalReceiveAmount() {
    totalReceiveAmount = BigDecimal.ZERO;
    for (OrderBillItem item : items) {
      if (item.getReceivedQty() == null)
        item.setReceivedQty(BigDecimal.ZERO);
      totalReceiveAmount = totalReceiveAmount.add(item.getPrice().multiply(item.getReceivedQty()));
    }
  }

  public void validate() {
    Assert.assertArgumentNotNull(supplier, "supplier");
    Assert.assertArgumentNotNull(wrh, "wrh");
    Assert.assertArgumentNotNull(items, "items");

    for (int i = 0; i < items.size(); i++) {
      OrderBillItem item = items.get(i);
      item.validate();
      for (int j = i + 1; j < items.size(); j++) {
        OrderBillItem jItem = items.get(j);
        if (item.getArticle().getUuid().equals(jItem.getArticle().getUuid())
            && item.getQpcStr().equals(jItem.getQpcStr()))
          throw new IllegalArgumentException(
              "商品" + item.getArticle().getCode() + "规格" + item.getQpcStr() + "重复！");
      }
    }
  }
}
