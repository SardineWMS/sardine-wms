/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	RtnSupplierNtcBill.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月15日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.rtn.rtnsupplierntc;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.SourceBill;
import com.hd123.sardine.wms.common.entity.StandardEntity;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.exception.WMSException;

/**
 * @author yangwenzhu
 *
 */
public class RtnSupplierNtcBill extends StandardEntity {
  private static final long serialVersionUID = -7112528754963407964L;
  public static final String CAPTION = "供应商退货通知单";

  private String billNumber;
  private RtnSupplierNtcBillState state = RtnSupplierNtcBillState.Initial;
  private UCN supplier;
  private UCN wrh;
  private SourceBill sourceBill;
  private Date rtnDate;
  private String totalCaseQtyStr;
  private BigDecimal totalAmount;
  private String unshelvedCaseQtyStr;
  private BigDecimal unshelvedAmount;
  private String remark;
  private String companyUuid;
  private List<RtnSupplierNtcBillItem> items = new ArrayList<>();

  /** 单号 */
  public String getBillNumber() {
    return billNumber;
  }

  public void setBillNumber(String billNumber) {
    Assert.assertArgumentNotNull(billNumber, "billNumber");
    this.billNumber = billNumber;
  }

  /** 状态 */
  public RtnSupplierNtcBillState getState() {
    return state;
  }

  public void setState(RtnSupplierNtcBillState state) {
    Assert.assertArgumentNotNull(state, "state");
    this.state = state;
  }

  /** 供应商 */
  public UCN getSupplier() {
    return supplier;
  }

  public void setSupplier(UCN supplier) {
    Assert.assertArgumentNotNull(supplier, "supplier");
    this.supplier = supplier;
  }

  /** 仓位 */
  public UCN getWrh() {
    return wrh;
  }

  public void setWrh(UCN wrh) {
    Assert.assertArgumentNotNull(wrh, "wrh");
    this.wrh = wrh;
  }

  /** 来源单据 */
  public SourceBill getSourceBill() {
    return sourceBill;
  }

  public void setSourceBill(SourceBill sourceBill) {
    this.sourceBill = sourceBill;
  }

  /** 退货时间 */
  public Date getRtnDate() {
    return rtnDate;
  }

  public void setRtnDate(Date rtnDate) {
    Assert.assertArgumentNotNull(rtnDate, "rtnDate");
    this.rtnDate = rtnDate;
  }

  /** 退货总件数 */
  public String getTotalCaseQtyStr() {
    return totalCaseQtyStr;
  }

  public void setTotalCaseQtyStr(String totalCaseQtyStr) {
    Assert.assertArgumentNotNull(totalCaseQtyStr, "totalCaseQtyStr");
    this.totalCaseQtyStr = totalCaseQtyStr;
  }

  /** 退货总金额 */
  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
    Assert.assertArgumentNotNull(totalAmount, "totalAmount");
    this.totalAmount = totalAmount;
  }

  /** 下架总件数 */
  public String getUnshelvedCaseQtyStr() {
    return unshelvedCaseQtyStr;
  }

  public void setUnshelvedCaseQtyStr(String unshelvedCaseQtyStr) {
    Assert.assertArgumentNotNull(unshelvedCaseQtyStr, "unshelveCaseQtyStr");
    this.unshelvedCaseQtyStr = unshelvedCaseQtyStr;
  }

  /** 下架总金额 */
  public BigDecimal getUnshelvedAmount() {
    return unshelvedAmount;
  }

  public void setUnshelvedAmount(BigDecimal unshelvedAmount) {
    Assert.assertArgumentNotNull(unshelvedAmount, "unshelvedAmount");
    this.unshelvedAmount = unshelvedAmount;
  }

  public String getRemark() {
    return remark;
  }

  /** 说明 */
  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getCompanyUuid() {
    return companyUuid;
  }

  public void setCompanyUuid(String companyUuid) {
    this.companyUuid = companyUuid;
  }

  public List<RtnSupplierNtcBillItem> getItems() {
    return items;
  }

  public void setItems(List<RtnSupplierNtcBillItem> items) {
    Assert.assertArgumentNotNull(items, "items");
    this.items = items;
  }

  public void validate() throws WMSException {
    Assert.assertArgumentNotNull(items, "items");
    Assert.assertArgumentNotNull(rtnDate, "rtnDate");
    Assert.assertArgumentNotNull(state, "state");
    Assert.assertArgumentNotNull(supplier, "supplier");
    Assert.assertArgumentNotNull(totalAmount, "totalAmount");
    Assert.assertArgumentNotNull(totalCaseQtyStr, "totalCaseQtyStr");
    Assert.assertArgumentNotNull(unshelvedAmount, "unshelvedAmount");
    Assert.assertArgumentNotNull(unshelvedCaseQtyStr, "unshelvedCaseQtyStr");

    if (CollectionUtils.isEmpty(items))
      throw new WMSException("明细行不能为空！");
    for (int i = 0; i < items.size(); i++) {
      RtnSupplierNtcBillItem iItem = items.get(i);
      iItem.validate();
      for (int j = i + 1; j < items.size(); j++) {
        RtnSupplierNtcBillItem jItem = items.get(j);
        jItem.validate();
        if (iItem.getArticle().getUuid().equals(jItem.getArticle().getUuid())
            && iItem.getQpcStr().equals(jItem.getQpcStr())) {
          throw new WMSException(MessageFormat.format("第{0}行和第{1}行明细商品和规格重复", i, j));
        }
      }
    }
  }
}
