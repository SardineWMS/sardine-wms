/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-core
 * 文件名：	PStockShiftRule.java
 * 模块说明：	
 * 修改历史：
 * 2014-4-10 - WUJING - 创建。
 */
package com.hd123.sardine.wms.dao.stock;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.hd123.sardine.wms.api.stock.StockState;

/**
 * @author WUJING
 * 
 */
public class PStockShiftRule implements Serializable {

  private static final long serialVersionUID = 569662174461769937L;

  private String workId;
  private String owner;
  private String companyUuid;
  private String binCode;
  private String containerBarcode;
  private String articleUuid;
  private StockState state = StockState.normal;
  private BigDecimal qty;
  private String qpcStr;
  private String stockUuid;
  private String supplierUuid;
  private Date productionDate;
  private String stockBatch;
  private String sourceLineUuid;
  private int sourceLineNumber;
  private String operateBillUuid;

  public String getWorkId() {
    return workId;
  }

  public void setWorkId(String workId) {
    this.workId = workId;
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

  public String getArticleUuid() {
    return articleUuid;
  }

  public void setArticleUuid(String articleUuid) {
    this.articleUuid = articleUuid;
  }

  public String getQpcStr() {
    return qpcStr;
  }

  public void setQpcStr(String qpcStr) {
    this.qpcStr = qpcStr;
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

  public String getStockUuid() {
    return stockUuid;
  }

  public void setStockUuid(String stockUuid) {
    this.stockUuid = stockUuid;
  }

  public String getSupplierUuid() {
    return supplierUuid;
  }

  public void setSupplierUuid(String supplierUuid) {
    this.supplierUuid = supplierUuid;
  }

  public Date getProductionDate() {
    return productionDate;
  }

  public void setProductionDate(Date productionDate) {
    this.productionDate = productionDate;
  }

  public String getStockBatch() {
    return stockBatch;
  }

  public void setStockBatch(String stockBatch) {
    this.stockBatch = stockBatch;
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

  public String getOperateBillUuid() {
    return operateBillUuid;
  }

  public void setOperateBillUuid(String operateBillUuid) {
    this.operateBillUuid = operateBillUuid;
  }
}
