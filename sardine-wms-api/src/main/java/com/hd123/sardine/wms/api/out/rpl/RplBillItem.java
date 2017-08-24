/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-api
 * 文件名：	RplBillItem.java
 * 模块说明：	
 * 修改历史：
 * 2014-8-12 - WUJING - 创建。
 */
package com.hd123.sardine.wms.api.out.rpl;

import java.math.BigDecimal;
import java.util.Date;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.stock.Stock;
import com.hd123.sardine.wms.common.entity.Entity;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.validator.Validator;

/**
 * 补货单明细
 * 
 * @author WUJING
 * 
 */
public class RplBillItem extends Entity implements Validator {

  private static final long serialVersionUID = 767569528092880576L;
  private String rplUuid;

  private int line;
  private String fromBinCode;
  private String containerBarcode;
  private String toBinCode;
  private UCN article;
  private String articleSpec;
  private String qpcStr;
  private String munit;
  private String caseQtyStr;
  private BigDecimal qty;
  private String realCaseQtyStr;
  private BigDecimal realQty = BigDecimal.ZERO;
  private String productionBatch = Stock.VISUAL_STOCKINFO;
  private String stockBatch = Stock.VISUAL_STOCKINFO;
  private UCN supplier = new UCN(Stock.VISUAL_STOCKSUPPLIERINFO, "", "");
  private Date productionDate = Stock.VISUAL_STOCKDATE;
  private Date validDate = Stock.VISUAL_STOCKDATE;
  private String owner;

  public int getLine() {
    return line;
  }

  public void setLine(int line) {
    this.line = line;
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

  public String getMunit() {
    return munit;
  }

  public void setMunit(String munit) {
    this.munit = munit;
  }

  /** 来源货位 */
  public String getFromBinCode() {
    return fromBinCode;
  }

  public void setFromBinCode(String fromBinCode) {
    Assert.assertArgumentNotNull(fromBinCode, "fromBinCode");
    this.fromBinCode = fromBinCode;
  }

  /** 目标货位 */
  public String getToBinCode() {
    return toBinCode;
  }

  public void setToBinCode(String toBinCode) {
    Assert.assertArgumentNotNull(toBinCode, "toBinCode");
    this.toBinCode = toBinCode;
  }

  /** 包装规格 */
  public String getQpcStr() {
    return qpcStr;
  }

  public void setQpcStr(String qpcStr) {
    Assert.assertArgumentNotNull(qpcStr, "qpcStr");
    this.qpcStr = qpcStr;
  }

  /** 件数 */
  public String getCaseQtyStr() {
    return caseQtyStr;
  }

  public void setCaseQtyStr(String caseQtyStr) {
    this.caseQtyStr = caseQtyStr;
  }

  public String getRplUuid() {
    return rplUuid;
  }

  public void setRplUuid(String rplUuid) {
    this.rplUuid = rplUuid;
  }

  /** 货主 */
  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  /** 数量 */
  public BigDecimal getQty() {
    return qty;
  }

  /***
   * 设置单品数量
   * 
   * @param qty
   *          单品数量，not null
   * @throws IllegalArgumentException
   */
  public void setQty(BigDecimal qty) {
    Assert.assertArgumentNotNull(qty, "qty");
    if (qty.compareTo(BigDecimal.ZERO) < 0)
      throw new IllegalArgumentException("数量不能小于0。");
    this.qty = qty;
  }

  /** 实际件数 */
  public String getRealCaseQtyStr() {
    return realCaseQtyStr;
  }

  public void setRealCaseQtyStr(String realCaseQtyStr) {
    this.realCaseQtyStr = realCaseQtyStr;
  }

  /** 实际数量 */
  public BigDecimal getRealQty() {
    return realQty;
  }

  /***
   * 设置实际数量
   * 
   * @param realQty
   *          实际数量，not null
   * @throws IllegalArgumentException
   */
  public void setRealQty(BigDecimal realQty) {
    if (realQty != null && realQty.compareTo(BigDecimal.ZERO) < 0)
      throw new IllegalArgumentException("实际数量不能小于0。");
    this.realQty = realQty;
  }

  /** 批号 */
  public String getProductionBatch() {
    return productionBatch;
  }

  public void setProductionBatch(String productionBatch) {
    Assert.assertArgumentNotNull(productionBatch, "produtionBatch");
    this.productionBatch = productionBatch;
  }

  /** 批次 */
  public String getStockBatch() {
    return stockBatch;
  }

  public void setStockBatch(String stockBatch) {
    Assert.assertArgumentNotNull(stockBatch, "stockBatch");
    this.stockBatch = stockBatch;
  }

  /** 来源容器 */
  public String getContainerBarcode() {
    return containerBarcode;
  }

  public void setContainerBarcode(String containerBarcode) {
    Assert.assertArgumentNotNull(containerBarcode, "containerBarcode");
    this.containerBarcode = containerBarcode;
  }

  /** 供应商 */
  public UCN getSupplier() {
    return supplier;
  }

  public void setSupplier(UCN supplier) {
    Assert.assertArgumentNotNull(supplier, "supplier");
    this.supplier = supplier;
  }

  /** 生产日期 */
  public Date getProductionDate() {
    return productionDate;
  }

  public void setProductionDate(Date productionDate) {
    Assert.assertArgumentNotNull(productionDate, "productionDate");
    this.productionDate = productionDate;
  }

  /** 到效日期 */
  public Date getValidDate() {
    return validDate;
  }

  public void setValidDate(Date validDate) {
    Assert.assertArgumentNotNull(validDate, "validDate");
    this.validDate = validDate;
  }

  @Override
  public void validate() {
    Assert.assertArgumentNotNull(fromBinCode, "fromBinCode");
    Assert.assertArgumentNotNull(article, "article");
    Assert.assertArgumentNotNull(containerBarcode, "containerBarcode");
    Assert.assertArgumentNotNull(toBinCode, "toBinCode");
    Assert.assertArgumentNotNull(qpcStr, "qpcStr");
    Assert.assertArgumentNotNull(qty, "qty");
    Assert.assertArgumentNotNull(productionBatch, "productionBatch");
    Assert.assertArgumentNotNull(stockBatch, "stockBatch");
    Assert.assertArgumentNotNull(supplier, "supplier");
    Assert.assertArgumentNotNull(validDate, "validDate");
    Assert.assertArgumentNotNull(productionDate, "productionDate");
  }
}
