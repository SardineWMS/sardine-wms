/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	DecIncInvBillItem.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月17日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.inner.decincinv;

import java.math.BigDecimal;
import java.util.Date;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.Entity;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * 损益单明细实体
 * 
 * @author fanqingqing
 *
 */
public class DecIncInvBillItem extends Entity {
  private static final long serialVersionUID = -5616582630608721471L;

  private int line;
  private String decIncInvBillUuid;
  private String binCode;
  private String containerBarCode;
  private UCN article;
  private String measureUnit;
  private UCN supplier;
  private String qpcStr;
  private BigDecimal qty;
  private String caseQtyStr;
  private Date productionDate;
  private Date expireDate;
  private String stockBatch;
  private BigDecimal price;
  private BigDecimal amount;
  private String reason;
  private String articleSpec;

  /** 行号 */
  public int getLine() {
    return line;
  }

  public void setLine(int line) {
    this.line = line;
  }

  /** 损溢货位 */
  public String getBinCode() {
    return binCode;
  }

  public void setBinCode(String binCode) {
    this.binCode = binCode;
  }

  /** 损溢容器 */
  public String getContainerBarCode() {
    return containerBarCode;
  }

  public void setContainerBarCode(String containerBarCode) {
    this.containerBarCode = containerBarCode;
  }

  /** 商品 */
  public UCN getArticle() {
    return article;
  }

  public void setArticle(UCN article) {
    this.article = article;
  }

  /** 商品规格 */
  public String getQpcStr() {
    return qpcStr;
  }

  public void setQpcStr(String qpcStr) {
    this.qpcStr = qpcStr;
  }

  /** 损溢数量（损：负数 、溢：正数） */
  public BigDecimal getQty() {
    return qty;
  }

  public void setQty(BigDecimal qty) {
    // setCaseQtyStr(QpcHelper.qtyToCaseQtyStr(qty, qpcStr));
    this.qty = qty;
  }

  /** 损溢件数 */
  public String getCaseQtyStr() {
    return caseQtyStr;
  }

  public void setCaseQtyStr(String caseQtyStr) {
    this.caseQtyStr = caseQtyStr;
  }

  /** 商品生产日期 */
  public Date getProductionDate() {
    return productionDate;
  }

  public void setProductionDate(Date productionDate) {
    this.productionDate = productionDate;
  }

  /** 商品到校日期 */
  public Date getExpireDate() {
    return expireDate;
  }

  public void setExpireDate(Date expireDate) {
    this.expireDate = expireDate;
  }

  /** 批次 */
  public String getStockBatch() {
    return stockBatch;
  }

  public void setStockBatch(String stockBatch) {
    this.stockBatch = stockBatch;
  }

  /** 原因 */
  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  /** 供应商 */
  public UCN getSupplier() {
    return supplier;
  }

  public void setSupplier(UCN supplier) {
    this.supplier = supplier;
  }

  /** 商品计量单位 */
  public String getMeasureUnit() {
    return measureUnit;
  }

  public void setMeasureUnit(String measureUnit) {
    this.measureUnit = measureUnit;
  }

  /** 损溢单uuid */
  public String getDecIncInvBillUuid() {
    return decIncInvBillUuid;
  }

  public void setDecIncInvBillUuid(String decIncInvBillUuid) {
    this.decIncInvBillUuid = decIncInvBillUuid;
  }

  /** 商品单价 */
  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  /** 商品金额 */
  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getArticleSpec() {
    return articleSpec;
  }

  public void setArticleSpec(String articleSpec) {
    this.articleSpec = articleSpec;
  }

  public void validate() {
    Assert.assertArgumentNotNull(article, "article");
    Assert.assertArgumentNotNull(supplier, "supplier");
    Assert.assertArgumentNotNull(qpcStr, "qpcStr");
    Assert.assertArgumentNotNull(qty, "qty");
    Assert.assertArgumentNotNull(productionDate, "productionDate");
    Assert.assertArgumentNotNull(expireDate, "expireDate");
    Assert.assertArgumentNotNull(binCode, "binCode");
    Assert.assertArgumentNotNull(price, "price");
  }

}
