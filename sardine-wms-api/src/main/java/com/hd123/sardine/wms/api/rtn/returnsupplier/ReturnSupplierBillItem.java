/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	SupplierReturnBillItem.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月21日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.rtn.returnsupplier;

import java.math.BigDecimal;
import java.util.Date;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.Entity;
import com.hd123.sardine.wms.common.entity.SourceBill;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.validator.Validator;

/**
 * 供应商退货单明细实体
 * 
 * @author fanqingqing
 *
 */
public class ReturnSupplierBillItem extends Entity implements Validator {
  private static final long serialVersionUID = -6696446554302802779L;

  private String returnSupplierBillUuid;
  private int line;
  private UCN article;
  private String spec = "-";
  private String qpcStr;
  private String munit = "-";
  private String binCode;
  private String containerBarcode;
  private BigDecimal qty;
  private String caseQtyStr;
  private Date productionDate;
  private Date validDate;
  private Date returnSupplierDate;
  private String stockBatch;
  private BigDecimal amount = BigDecimal.ZERO;
  private SourceBill sourceBill;
  private UCN supplier;
  private String owner;
  private BigDecimal price = BigDecimal.ZERO;

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }
  
  public SourceBill getSourceBill() {
    return sourceBill;
  }

  public void setSourceBill(SourceBill sourceBill) {
    this.sourceBill = sourceBill;
  }

  /** 供应商退货单UUID */
  public String getReturnSupplierBillUuid() {
    return returnSupplierBillUuid;
  }

  public void setReturnSupplierBillUuid(String returnSupplierBillUuid) {
    this.returnSupplierBillUuid = returnSupplierBillUuid;
  }

  /** 行号 */
  public int getLine() {
    return line;
  }

  public void setLine(int line) {
    this.line = line;
  }

  /** 商品 */
  public UCN getArticle() {
    return article;
  }

  public void setArticle(UCN article) {
    this.article = article;
  }

  /** 商品规格 */
  public String getSpec() {
    return spec;
  }

  public void setSpec(String spec) {
    this.spec = spec;
  }

  /** 包装规格 */
  public String getQpcStr() {
    return qpcStr;
  }

  public void setQpcStr(String qpcStr) {
    this.qpcStr = qpcStr;
  }

  /** 单位 */
  public String getMunit() {
    return munit;
  }

  public void setMunit(String munit) {
    this.munit = munit;
  }

  /** 货位 */
  public String getBinCode() {
    return binCode;
  }

  public void setBinCode(String binCode) {
    this.binCode = binCode;
  }

  /** 容器 */
  public String getContainerBarcode() {
    return containerBarcode;
  }

  public void setContainerBarcode(String containerBarcode) {
    this.containerBarcode = containerBarcode;
  }

  /** 数量 */
  public BigDecimal getQty() {
    return qty;
  }

  public void setQty(BigDecimal qty) {
    this.qty = qty;
  }

  /** 件数 */
  public String getCaseQtyStr() {
    return caseQtyStr;
  }

  public void setCaseQtyStr(String caseQtyStr) {
    this.caseQtyStr = caseQtyStr;
  }

  /** 生产日期 */
  public Date getProductionDate() {
    return productionDate;
  }

  public void setProductionDate(Date productionDate) {
    this.productionDate = productionDate;
  }

  /** 到效日期 */
  public Date getValidDate() {
    return validDate;
  }

  public void setValidDate(Date validDate) {
    this.validDate = validDate;
  }

  public String getStockBatch() {
    return stockBatch;
  }

  public void setStockBatch(String stockBatch) {
    this.stockBatch = stockBatch;
  }

  /** 退货日期 */
  public Date getReturnSupplierDate() {
    return returnSupplierDate;
  }

  public void setReturnSupplierDate(Date returnSupplierDate) {
    this.returnSupplierDate = returnSupplierDate;
  }

  /** 单价 */
  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public UCN getSupplier() {
    return supplier;
  }

  public void setSupplier(UCN supplier) {
    this.supplier = supplier;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  @Override
  public void validate() {
    Assert.assertArgumentNotNull(article, "article");
    Assert.assertArgumentNotNull(article.getUuid(), "article.uuid");
    Assert.assertArgumentNotNull(article.getCode(), "article.code");
    Assert.assertArgumentNotNull(article.getName(), "article.name");
    Assert.assertArgumentNotNull(qpcStr, "qpcStr");
    Assert.assertArgumentNotNull(binCode, "binCode");
    Assert.assertArgumentNotNull(containerBarcode, "containerBarcode");
    Assert.assertArgumentNotNull(productionDate, "productionDate");
    Assert.assertArgumentNotNull(validDate, "validDate");
    Assert.assertArgumentNotNull(qty, "qty");
    Assert.assertArgumentNotNull(caseQtyStr, "caseQtyStr");
    Assert.assertArgumentNotNull(sourceBill, "sourceBill");
    Assert.assertArgumentNotNull(stockBatch, "stockBatch");
    Assert.assertArgumentNotNull(price, "price");
  }
}
