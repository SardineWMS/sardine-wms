/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	StockOperLog.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月15日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.stock;

import java.math.BigDecimal;
import java.util.Date;

import com.hd123.sardine.wms.api.stock.StockState;
import com.hd123.sardine.wms.common.entity.Entity;
import com.hd123.sardine.wms.common.entity.SourceBill;

/**
 * @author zhangsai
 *
 */
public class StockOperLog extends Entity {
  private static final long serialVersionUID = 192598683812255877L;

  private String stockUuid;
  private String binCode;
  private String containerBarcode;
  private String companyUuid;
  private String articleUuid;
  private String qpcStr;
  private String productionBatch;
  private String stockBatch;
  private String supplierUuid;
  private String owner;
  private StockState state;
  private SourceBill operateBill;
  private BigDecimal qty;
  private BigDecimal beforeQty;
  private BigDecimal afterQty;
  private Date logTime;

  public String getStockUuid() {
    return stockUuid;
  }

  public void setStockUuid(String stockUuid) {
    this.stockUuid = stockUuid;
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
  
  public String getProductionBatch() {
    return productionBatch;
  }

  public void setProductionBatch(String productionBatch) {
    this.productionBatch = productionBatch;
  }

  public String getStockBatch() {
    return stockBatch;
  }

  public void setStockBatch(String stockBatch) {
    this.stockBatch = stockBatch;
  }

  public String getSupplierUuid() {
    return supplierUuid;
  }

  public void setSupplierUuid(String supplierUuid) {
    this.supplierUuid = supplierUuid;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public StockState getState() {
    return state;
  }

  public void setState(StockState state) {
    this.state = state;
  }

  public SourceBill getOperateBill() {
    return operateBill;
  }

  public void setOperateBill(SourceBill operateBill) {
    this.operateBill = operateBill;
  }

  public BigDecimal getQty() {
    return qty;
  }

  public void setQty(BigDecimal qty) {
    this.qty = qty;
  }

  public BigDecimal getBeforeQty() {
    return beforeQty;
  }

  public void setBeforeQty(BigDecimal beforeQty) {
    this.beforeQty = beforeQty;
  }

  public BigDecimal getAfterQty() {
    return afterQty;
  }

  public void setAfterQty(BigDecimal afterQty) {
    this.afterQty = afterQty;
  }

  public Date getLogTime() {
    return logTime;
  }

  public void setLogTime(Date logTime) {
    this.logTime = logTime;
  }
}
