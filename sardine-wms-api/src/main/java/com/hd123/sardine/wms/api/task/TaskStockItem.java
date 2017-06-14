/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	TaskStockItem.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月14日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.task;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhangsai
 *
 */
public class TaskStockItem implements Serializable {
  private static final long serialVersionUID = 7521725443966243104L;

  private String uuid;
  private String taskUuid;
  private String supplierUuid;
  private String binCode;
  private String containerBarcode;
  private String articleUuid;
  private String stockBatch;
  private Date productionDate;
  private Date validDate;
  private BigDecimal qty = BigDecimal.ZERO;
  private String qpcStr;
  private String measureUnit;
  private BigDecimal price = BigDecimal.ZERO;

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getTaskUuid() {
    return taskUuid;
  }

  public void setTaskUuid(String taskUuid) {
    this.taskUuid = taskUuid;
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

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }
}
