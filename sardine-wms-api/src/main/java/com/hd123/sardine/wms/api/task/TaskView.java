/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	TaskView.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月23日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.task;

import java.math.BigDecimal;
import java.sql.Date;

import com.hd123.sardine.wms.common.entity.Entity;
import com.hd123.sardine.wms.common.entity.SourceBill;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * 指令视图
 * <p>
 * 综合上架指令、退仓上架指令、退货下架指令、移库指令、拣货单、补货单、装车、退货交接指令
 * 
 * @author zhangsai
 *
 */
public class TaskView extends Entity {
  private static final long serialVersionUID = -4383562195138151127L;

  private String billNumber;
  private String state;
  private String owner;
  private String companyUuid;
  private UCN article;
  private String articleSpec;
  private String qpcStr;
  private String munit;
  private String fromBinCode;
  private String fromContainerBarcode;
  private String toBinCode;
  private String toContainerBarcode;
  private BigDecimal qty;
  private String caseQtyStr;
  private BigDecimal realQty;
  private String realCaseQtyStr;
  private String operator;
  private Date operateTime;
  private SourceBill sourceBill;

  public String getBillNumber() {
    return billNumber;
  }

  public void setBillNumber(String billNumber) {
    this.billNumber = billNumber;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getCompanyUuid() {
    return companyUuid;
  }

  public void setCompanyUuid(String companyUuid) {
    this.companyUuid = companyUuid;
  }

  public UCN getArticle() {
    return article;
  }

  public void setArticle(UCN article) {
    this.article = article;
  }

  public String getArticleSpec() {
    return articleSpec;
  }

  public void setArticleSpec(String articleSpec) {
    this.articleSpec = articleSpec;
  }

  public String getQpcStr() {
    return qpcStr;
  }

  public void setQpcStr(String qpcStr) {
    this.qpcStr = qpcStr;
  }

  public String getMunit() {
    return munit;
  }

  public void setMunit(String munit) {
    this.munit = munit;
  }

  public String getFromBinCode() {
    return fromBinCode;
  }

  public void setFromBinCode(String fromBinCode) {
    this.fromBinCode = fromBinCode;
  }

  public String getFromContainerBarcode() {
    return fromContainerBarcode;
  }

  public void setFromContainerBarcode(String fromContainerBarcode) {
    this.fromContainerBarcode = fromContainerBarcode;
  }

  public String getToBinCode() {
    return toBinCode;
  }

  public void setToBinCode(String toBinCode) {
    this.toBinCode = toBinCode;
  }

  public String getToContainerBarcode() {
    return toContainerBarcode;
  }

  public void setToContainerBarcode(String toContainerBarcode) {
    this.toContainerBarcode = toContainerBarcode;
  }

  public BigDecimal getQty() {
    return qty;
  }

  public void setQty(BigDecimal qty) {
    this.qty = qty;
  }

  public String getCaseQtyStr() {
    return caseQtyStr;
  }

  public void setCaseQtyStr(String caseQtyStr) {
    this.caseQtyStr = caseQtyStr;
  }

  public BigDecimal getRealQty() {
    return realQty;
  }

  public void setRealQty(BigDecimal realQty) {
    this.realQty = realQty;
  }

  public String getRealCaseQtyStr() {
    return realCaseQtyStr;
  }

  public void setRealCaseQtyStr(String realCaseQtyStr) {
    this.realCaseQtyStr = realCaseQtyStr;
  }

  public String getOperator() {
    return operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }

  public Date getOperateTime() {
    return operateTime;
  }

  public void setOperateTime(Date operateTime) {
    this.operateTime = operateTime;
  }
  
  public SourceBill getSourceBill() {
    return sourceBill;
  }

  public void setSourceBill(SourceBill sourceBill) {
    this.sourceBill = sourceBill;
  }
}
