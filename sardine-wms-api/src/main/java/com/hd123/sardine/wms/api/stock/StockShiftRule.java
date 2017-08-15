/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-api
 * 文件名：	StockShiftRule.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-28 - Gao JingYu - 创建。
 */
package com.hd123.sardine.wms.api.stock;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.validator.Validator;

/**
 * 库存变化规则。
 * 
 * @author Gao JingYu
 */
public class StockShiftRule implements Serializable, Validator {
  private static final long serialVersionUID = 1281104592930551366L;

  // 必选参数
  private String owner;
  private String companyUuid;
  private String binCode;
  private String containerBarcode;
  private String articleUuid;
  private StockState state = StockState.normal;
  private BigDecimal qty;

  // 可选参数
  private String stockUuid;

  private String supplierUuid;
  private String productionBatch;
  private String qpcStr;
  private String stockBatch;
  private String operateBillUuid;
  private String sourceBillUuid;

  public String getSourceBillUuid() {
    return sourceBillUuid;
  }

  public void setSourceBillUuid(String sourceBillUuid) {
    this.sourceBillUuid = sourceBillUuid;
  }

  /** 库存UUID */
  public String getStockUuid() {
    return stockUuid;
  }

  public void setStockUuid(String stockUuid) {
    this.stockUuid = stockUuid;
  }

  /** 货主 */
  public String getOwner() {
    return owner;
  }

  /**
   * 设置货主。
   * 
   * @param owner
   *          not null。
   * @throws IllegalArgumentException
   */
  public void setOwner(String owner) {
    Assert.assertArgumentNotNull(owner, "owner");

    this.owner = owner;
  }
  
  public String getCompanyUuid() {
    return companyUuid;
  }

  public void setCompanyUuid(String companyUuid) {
    Assert.assertArgumentNotNull(companyUuid, "companyUuid");
    this.companyUuid = companyUuid;
  }

  /** 供应商UUID */
  public String getSupplierUuid() {
    return supplierUuid;
  }

  public void setSupplierUuid(String supplierUuid) {
    this.supplierUuid = supplierUuid;
  }

  /** 货位代码 */
  public String getBinCode() {
    return binCode;
  }

  public void setBinCode(String binCode) {
    this.binCode = binCode;
  }

  /** 容器条码 */
  public String getContainerBarcode() {
    return containerBarcode;
  }

  /**
   * 设置容器条码。
   * 
   * @param containerBarCode
   *          not null。
   * @throws IllegalArgumentException
   */
  public void setContainerBarcode(String containerBarcode) {
    Assert.assertArgumentNotNull(containerBarcode, "containerBarcode");
    this.containerBarcode = containerBarcode;
  }

  /** 商品UUID */
  public String getArticleUuid() {
    return articleUuid;
  }

  /**
   * 设置商品
   * 
   * @param articleUuid
   *          not null。
   * @throws IllegalArgumentException
   */
  public void setArticleUuid(String articleUuid) {
    Assert.assertArgumentNotNull(articleUuid, "productUuid");
    this.articleUuid = articleUuid;
  }

  /** 批号 */
  public String getProductionBatch() {
    return productionBatch;
  }

  public void setProductionBatch(String productionBatch) {
    this.productionBatch = productionBatch;
  }

  /** 批次 */
  public String getStockBatch() {
    return stockBatch;
  }

  public void setStockBatch(String stockBatch) {
    this.stockBatch = stockBatch;
  }

  /** 库存状态 */
  public StockState getState() {
    return state;
  }

  /**
   * 设置库存状态。
   * 
   * @param state
   *          not null。
   * @throws IllegalArgumentException
   */
  public void setState(StockState state) {
    Assert.assertArgumentNotNull(state, "state");
    this.state = state;
  }

  /** 数量 */
  public BigDecimal getQty() {
    return qty;
  }

  /**
   * 设置数量
   * 
   * @param qty
   *          not null,大于零。
   * @throws IllegalArgumentException
   */
  public void setQty(BigDecimal qty) {
    Assert.assertArgumentNotNull(qty, "qty");
    if (qty.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("数量不能小于零");
    }
    this.qty = qty;
  }

  /** 商品规格 */
  public String getQpcStr() {
    return qpcStr;
  }

  public void setQpcStr(String qpcStr) {
    this.qpcStr = qpcStr;
  }

  /** 操作单据 */
  public String getOperateBillUuid() {
    return operateBillUuid;
  }

  public void setOperateBillUuid(String operateBillUuid) {
    this.operateBillUuid = operateBillUuid;
  }

  @Override
  public void validate() {
    Assert.assertArgumentNotNull(owner, "owner");
    Assert.assertArgumentNotNull(companyUuid, "companyUuid");
    Assert.assertArgumentNotNull(binCode, "binCode");
    Assert.assertArgumentNotNull(containerBarcode, "containerBarcode");
    Assert.assertArgumentNotNull(articleUuid, "articleUuid");
    Assert.assertArgumentNotNull(state, "state");
    Assert.assertArgumentNotNull(qty, "qty");
  }
}