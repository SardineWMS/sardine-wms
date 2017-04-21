/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ShiftOutRule.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月11日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.stock;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hd123.rumba.commons.lang.Assert;

/**
 * @author zhangsai
 *
 */
public class ShiftOutRule implements Serializable {
  private static final long serialVersionUID = -2501303122654993752L;

  private String binCode;
  private String containerBarcode;
  private String articleUuid;
  private String supplierUuid;
  private BigDecimal qty;
  private String companyUuid;

  // 可选参数
  private String stockUuid;

  private String productionBatch;
  private String qpcStr;
  private String stockBatch;
  private String sourceBillUuid;

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

  public String getSupplierUuid() {
    return supplierUuid;
  }

  public void setSupplierUuid(String supplierUuid) {
    this.supplierUuid = supplierUuid;
  }

  public BigDecimal getQty() {
    return qty;
  }

  public void setQty(BigDecimal qty) {
    this.qty = qty;
  }

  public String getCompanyUuid() {
    return companyUuid;
  }

  public void setCompanyUuid(String companyUuid) {
    this.companyUuid = companyUuid;
  }

  public String getStockUuid() {
    return stockUuid;
  }

  public void setStockUuid(String stockUuid) {
    this.stockUuid = stockUuid;
  }

  public String getProductionBatch() {
    return productionBatch;
  }

  public void setProductionBatch(String productionBatch) {
    this.productionBatch = productionBatch;
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

  public void validate() {
    Assert.assertArgumentNotNull(companyUuid, "companyUuid");
    Assert.assertArgumentNotNull(binCode, "binCode");
    Assert.assertArgumentNotNull(containerBarcode, "containerBarcode");
    Assert.assertArgumentNotNull(articleUuid, "articleUuid");
    Assert.assertArgumentNotNull(qty, "qty");
  }
}
