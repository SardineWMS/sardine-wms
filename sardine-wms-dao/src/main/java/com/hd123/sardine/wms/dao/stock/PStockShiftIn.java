/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-core
 * 文件名：	PStockShiftIn.java
 * 模块说明：	
 * 修改历史：
 * 2014-4-10 - WUJING - 创建。
 */
package com.hd123.sardine.wms.dao.stock;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.hd123.sardine.wms.api.stock.StockState;
import com.hd123.sardine.wms.common.entity.SourceBill;

/**
 * @author WUJING
 * 
 */
public class PStockShiftIn implements Serializable {

  private static final long serialVersionUID = 2572337756271149151L;

  private String workId;
  private String sourceLineUuid;
  private int sourceLineNumber;
  private String owner;
  private String companyUuid;
  private String supplierUuid;
  private String binCode;
  private String containerBarCode;
  private String articleUuid;
  private String stockBatch;
  private Date productionDate;
  private Date validDate;
  private SourceBill sourceBill;
  private SourceBill operateBill;
  private StockState state = StockState.normal;
  private BigDecimal qty;
  private String qpcStr;
  private String measureUnit;
  private Date instockTime = new Date();
  private BigDecimal price;

  public String getCompanyUuid() {
    return companyUuid;
  }

  public void setCompanyUuid(String companyUuid) {
    this.companyUuid = companyUuid;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public String getWorkId() {
    return workId;
  }

  public void setWorkId(String workId) {
    this.workId = workId;
  }

  public String getSourceLineUuid() {
    return sourceLineUuid;
  }

  public void setSourceLineUuid(String sourceLineUuid) {
    this.sourceLineUuid = sourceLineUuid;
  }

  public int getSourceLineNumber() {
    return sourceLineNumber;
  }

  public void setSourceLineNumber(int sourceLineNumber) {
    this.sourceLineNumber = sourceLineNumber;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getSupplierUuid() {
    return supplierUuid;
  }

  public void setSupplierUuid(String supplierUuid) {
    this.supplierUuid = supplierUuid;
  }

  public String getBinCode() {
    return binCode;
  }

  public void setBinCode(String binCode) {
    this.binCode = binCode;
  }

  public String getContainerBarCode() {
    return containerBarCode;
  }

  public void setContainerBarCode(String containerBarCode) {
    this.containerBarCode = containerBarCode;
  }

  public String getArticleUuid() {
    return articleUuid;
  }

  public void setArticleUuid(String articleUuid) {
    this.articleUuid = articleUuid;
  }

  public String getStockBatch() {
    return stockBatch;
  }

  public void setStockBatch(String stockBatch) {
    this.stockBatch = stockBatch;
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

  public SourceBill getSourceBill() {
    return sourceBill;
  }

  public void setSourceBill(SourceBill sourceBill) {
    this.sourceBill = sourceBill;
  }

  public StockState getState() {
    return state;
  }

  public void setState(StockState state) {
    this.state = state;
  }

  public BigDecimal getQty() {
    return qty;
  }

  public void setQty(BigDecimal qty) {
    this.qty = qty;
  }

  public String getQpcStr() {
    return qpcStr;
  }

  public void setQpcStr(String qpcStr) {
    this.qpcStr = qpcStr;
  }

  public String getMeasureUnit() {
    return measureUnit;
  }

  public void setMeasureUnit(String measureUnit) {
    this.measureUnit = measureUnit;
  }

  public Date getInstockTime() {
    return instockTime;
  }

  public void setInstockTime(Date instockTime) {
    this.instockTime = instockTime;
  }

  public SourceBill getOperateBill() {
    return operateBill;
  }

  public void setOperateBill(SourceBill operateBill) {
    this.operateBill = operateBill;
  }
}
