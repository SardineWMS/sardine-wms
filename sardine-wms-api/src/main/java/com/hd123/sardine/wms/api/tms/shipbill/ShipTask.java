/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ShipTask.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月26日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.tms.shipbill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.hd123.sardine.wms.common.entity.SourceBill;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * @author zhangsai
 *
 */
public class ShipTask implements Serializable {
  private static final long serialVersionUID = -8541780046236301122L;

  private String pickupBillUuid;
  private String pickUpBillNumber;
  private String alcNtcBillUuid;
  private String alcNtcBillNumber;
  private UCN article;
  private UCN customer;
  private String qpcStr;
  private String articleSpec = "-";
  private String munit = "-";
  private SourceBill sourceBill;
  private String binCode;
  private String containerBarcode;
  private Date productionDate;
  private Date validDate;
  private BigDecimal qty;
  private String caseQtyStr;
  private UCN supplier;
  private String stockBatch;
  private String productionBatch;
  private String alcNtcBillItemUuid;

  public String getPickupBillUuid() {
    return pickupBillUuid;
  }

  public void setPickupBillUuid(String pickupBillUuid) {
    this.pickupBillUuid = pickupBillUuid;
  }

  public String getPickUpBillNumber() {
    return pickUpBillNumber;
  }

  public void setPickUpBillNumber(String pickUpBillNumber) {
    this.pickUpBillNumber = pickUpBillNumber;
  }

  public String getAlcNtcBillUuid() {
    return alcNtcBillUuid;
  }

  public void setAlcNtcBillUuid(String alcNtcBillUuid) {
    this.alcNtcBillUuid = alcNtcBillUuid;
  }

  public String getAlcNtcBillNumber() {
    return alcNtcBillNumber;
  }

  public void setAlcNtcBillNumber(String alcNtcBillNumber) {
    this.alcNtcBillNumber = alcNtcBillNumber;
  }

  public UCN getArticle() {
    return article;
  }

  public void setArticle(UCN article) {
    this.article = article;
  }

  public UCN getCustomer() {
    return customer;
  }

  public void setCustomer(UCN customer) {
    this.customer = customer;
  }

  public String getQpcStr() {
    return qpcStr;
  }

  public void setQpcStr(String qpcStr) {
    this.qpcStr = qpcStr;
  }

  public String getArticleSpec() {
    return articleSpec;
  }

  public void setArticleSpec(String articleSpec) {
    this.articleSpec = articleSpec;
  }

  public String getMunit() {
    return munit;
  }

  public void setMunit(String munit) {
    this.munit = munit;
  }

  public SourceBill getSourceBill() {
    return sourceBill;
  }

  public void setSourceBill(SourceBill sourceBill) {
    this.sourceBill = sourceBill;
  }

  public String getBinCode() {
    return binCode;
  }

  public void setBinCode(String binCode) {
    this.binCode = binCode;
  }

  public String getContainerBarcode() {
    return containerBarcode;
  }

  public void setContainerBarcode(String containerBarcode) {
    this.containerBarcode = containerBarcode;
  }

  public Date getProductionDate() {
    return productionDate;
  }

  public void setProductionDate(Date productionDate) {
    this.productionDate = productionDate;
  }

  public Date getValidDate() {
    return validDate;
  }

  public void setValidDate(Date validDate) {
    this.validDate = validDate;
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

  public UCN getSupplier() {
    return supplier;
  }

  public void setSupplier(UCN supplier) {
    this.supplier = supplier;
  }

  public String getStockBatch() {
    return stockBatch;
  }

  public void setStockBatch(String stockBatch) {
    this.stockBatch = stockBatch;
  }

  public String getProductionBatch() {
    return productionBatch;
  }

  public void setProductionBatch(String productionBatch) {
    this.productionBatch = productionBatch;
  }

  public String getAlcNtcBillItemUuid() {
    return alcNtcBillItemUuid;
  }

  public void setAlcNtcBillItemUuid(String alcNtcBillItemUuid) {
    this.alcNtcBillItemUuid = alcNtcBillItemUuid;
  }
}
