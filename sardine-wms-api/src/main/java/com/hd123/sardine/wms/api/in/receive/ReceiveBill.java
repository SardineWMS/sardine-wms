/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ReceiveBill.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-20 - WUJING - 创建。
 */
package com.hd123.sardine.wms.api.in.receive;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.StandardEntity;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * @author WUJING
 * 
 */
public class ReceiveBill extends StandardEntity {

  private static final long serialVersionUID = -7222233711042552662L;
  public static final String CAPTION = "收货单";

  private String billNumber;
  private UCN receiver;
  private ReceiveBillState state = ReceiveBillState.Initial;
  private UCN supplier;
  private String orderBillNumber;
  private UCN wrh;
  private ReceiveBillMethod method;
  private String caseQtyStr;
  private String companyUuid;
  private BigDecimal totalAmount;
  private String note;
  
  private List<ReceiveBillItem> items = new ArrayList<ReceiveBillItem>();

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public String getBillNumber() {
    return billNumber;
  }

  public void setBillNumber(String billNumber) {
    this.billNumber = billNumber;
  }
  
  public String getOrderBillNumber() {
    return orderBillNumber;
  }

  public void setOrderBillNumber(String orderBillNumber) {
    this.orderBillNumber = orderBillNumber;
  }

  public String getCompanyUuid() {
    return companyUuid;
  }

  public void setCompanyUuid(String companyUuid) {
    this.companyUuid = companyUuid;
  }

  /** 收货人 */
  public UCN getReceiver() {
    return receiver;
  }

  public void setReceiver(UCN receiver) {
    this.receiver = receiver;
  }

  /** 状态 */
  public ReceiveBillState getState() {
    return state;
  }

  public void setState(ReceiveBillState state) {
    this.state = state;
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

  /** 收货方式 */
  public ReceiveBillMethod getMethod() {
    return method;
  }

  public void setMethod(ReceiveBillMethod method) {
    this.method = method;
  }

  public List<ReceiveBillItem> getItems() {
    return items;
  }

  public void setItems(List<ReceiveBillItem> items) {
    this.items = items;
  }

  /** 件数 */
  public String getCaseQtyStr() {
    return caseQtyStr;
  }

  public void setCaseQtyStr(String caseQtyStr) {
    this.caseQtyStr = caseQtyStr;
  }
  
  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }

  public void validate() {
    Assert.assertArgumentNotNull(receiver, "receiver");
    Assert.assertArgumentNotNull(state, "state");
    Assert.assertArgumentNotNull(method, "method");
    Assert.assertArgumentNotNull(orderBillNumber, "orderBillNumber");

    for (ReceiveBillItem item : items)
      item.validate();
  }
}
