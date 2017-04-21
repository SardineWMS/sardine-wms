/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	StockFilter.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月11日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.stock;

import com.hd123.sardine.wms.common.query.PageQueryDefinition;

/**
 * @author zhangsai
 *
 */
public class StockFilter extends PageQueryDefinition {
  private static final long serialVersionUID = -3011733549963203907L;

  private String binCode;
  private String containerBarcode;
  private String articleUuid;
  private String articleCode;
  private String supplierUuid;
  private String supplierCode;
  private String qpcStr;
  private String stockBatch;
  private String sourceBillUuid;
  private String sourcebillNumber;
  
  private String stockUuid;

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

  public String getArticleUuid() {
    return articleUuid;
  }

  public void setArticleUuid(String articleUuid) {
    this.articleUuid = articleUuid;
  }

  public String getArticleCode() {
    return articleCode;
  }

  public void setArticleCode(String articleCode) {
    this.articleCode = articleCode;
  }

  public String getSupplierUuid() {
    return supplierUuid;
  }

  public void setSupplierUuid(String supplierUuid) {
    this.supplierUuid = supplierUuid;
  }

  public String getSupplierCode() {
    return supplierCode;
  }

  public void setSupplierCode(String supplierCode) {
    this.supplierCode = supplierCode;
  }

  public String getQpcStr() {
    return qpcStr;
  }

  public void setQpcStr(String qpcStr) {
    this.qpcStr = qpcStr;
  }

  public String getStockBatch() {
    return stockBatch;
  }

  public void setStockBatch(String stockBatch) {
    this.stockBatch = stockBatch;
  }

  public String getSourceBillUuid() {
    return sourceBillUuid;
  }

  public void setSourceBillUuid(String sourceBillUuid) {
    this.sourceBillUuid = sourceBillUuid;
  }

  public String getSourcebillNumber() {
    return sourcebillNumber;
  }

  public void setSourcebillNumber(String sourcebillNumber) {
    this.sourcebillNumber = sourcebillNumber;
  }
}