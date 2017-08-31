/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ReturnBillItem.java
 * 模块说明：	
 * 修改历史：
 * 2017年7月4日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.rtn.customerreturn;

import java.math.BigDecimal;
import java.util.Date;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.basicInfo.bin.Bin;
import com.hd123.sardine.wms.api.basicInfo.container.Container;
import com.hd123.sardine.wms.common.entity.Entity;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * 退仓单明细|实体类
 * 
 * @author yangwenzhu
 *
 */
public class ReturnBillItem extends Entity {
  private static final long serialVersionUID = -7544640020608242304L;
  public static final int LENGTH_UUID = 32;
  public static final int LENGTH_CODE = 30;

  private String returnBillUuid;
  private String returnNtcBillItemUuid;
  private int line;
  private UCN article;
  private String munit;
  private String qpcStr;
  private UCN supplier;
  private BigDecimal qty = BigDecimal.ZERO;
  private String caseQtyStr;
  private ReturnType returnType;
  private Date productionDate;
  private Date validDate;
  private String stockBatch;
  private BigDecimal price = BigDecimal.ZERO;
  private BigDecimal amount = BigDecimal.ZERO;
  private String containerBarcode = Container.VIRTUALITY_CONTAINER;
  private String binCode = Bin.VIRTUALITY_BIN;
  private String articleSpec;

  public String getReturnBillUuid() {
    return returnBillUuid;
  }

  /** 退仓单uuid */
  public void setReturnBillUuid(String returnBillUuid) {
    Assert.assertArgumentNotNull(returnBillUuid, "returnBillUuid");
    Assert.assertStringNotTooLong(returnBillUuid, LENGTH_UUID, "returnBillUuid");
    this.returnBillUuid = returnBillUuid;
  }

  public String getReturnNtcBillItemUuid() {
    return returnNtcBillItemUuid;
  }

  /** 退仓通知单明细UUID */
  public void setReturnNtcBillItemUuid(String returnNtcBillItemUuid) {
    Assert.assertArgumentNotNull(returnNtcBillItemUuid, "returnNtcBillItemUuid");
    Assert.assertStringNotTooLong(returnNtcBillItemUuid, LENGTH_UUID, "returnNtcBillItemUuid");
    this.returnNtcBillItemUuid = returnNtcBillItemUuid;
  }

  public int getLine() {
    return line;
  }

  /** 行号 */
  public void setLine(int line) {
    this.line = line;
  }

  public UCN getArticle() {
    return article;
  }

  /** 商品 */
  public void setArticle(UCN article) {
    Assert.assertArgumentNotNull(article, "article");
    this.article = article;
  }

  public String getMunit() {
    return munit;
  }

  /** 单位 */
  public void setMunit(String munit) {
    Assert.assertArgumentNotNull(munit, "munit");
    Assert.assertStringNotTooLong(munit, LENGTH_CODE, "munit");
    this.munit = munit;
  }

  public String getQpcStr() {
    return qpcStr;
  }

  /** 包装规格 */
  public void setQpcStr(String qpcStr) {
    Assert.assertArgumentNotNull(qpcStr, "qpcStr");
    Assert.assertStringNotTooLong(qpcStr, LENGTH_CODE, "qpcStr");
    this.qpcStr = qpcStr;
  }

  public UCN getSupplier() {
    return supplier;
  }

  /** 供应商 */
  public void setSupplier(UCN supplier) {
    Assert.assertArgumentNotNull(supplier, "supplier");
    this.supplier = supplier;
  }

  public BigDecimal getQty() {
    return qty;
  }

  /** 数量 */
  public void setQty(BigDecimal qty) {
    Assert.assertArgumentNotNull(qty, "qty");
    this.qty = qty;
  }

  public String getCaseQtyStr() {
    return caseQtyStr;
  }

  /** 件数 */
  public void setCaseQtyStr(String caseQtyStr) {
    Assert.assertArgumentNotNull(caseQtyStr, "caseQtyStr");
    Assert.assertStringNotTooLong(caseQtyStr, LENGTH_CODE, "caseQtyStr");
    this.caseQtyStr = caseQtyStr;
  }

  public ReturnType getReturnType() {
    return returnType;
  }

  /** 退仓类型 */
  public void setReturnType(ReturnType returnType) {
    Assert.assertArgumentNotNull(returnType, "returnType");
    this.returnType = returnType;
  }

  public Date getProductionDate() {
    return productionDate;
  }

  /** 生产日期 */
  public void setProductionDate(Date productionDate) {
    Assert.assertArgumentNotNull(productionDate, "productionDate");
    this.productionDate = productionDate;
  }

  public Date getValidDate() {
    return validDate;
  }

  /** 到效期 */
  public void setValidDate(Date validDate) {
    Assert.assertArgumentNotNull(validDate, "validDate");
    this.validDate = validDate;
  }

  public String getStockBatch() {
    return stockBatch;
  }

  /** 批次 */
  public void setStockBatch(String stockBatch) {
    Assert.assertArgumentNotNull(stockBatch, "stockBatch");
    Assert.assertStringNotTooLong(stockBatch, LENGTH_CODE, "stockBatch");
    this.stockBatch = stockBatch;
  }

  public BigDecimal getPrice() {
    return price;
  }

  /** 单价 */
  public void setPrice(BigDecimal price) {
    Assert.assertArgumentNotNull(price, "price");
    this.price = price;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  /** 金额 */
  public void setAmount(BigDecimal amount) {
    Assert.assertArgumentNotNull(amount, "amount");
    this.amount = amount;
  }

  public String getContainerBarcode() {
    return containerBarcode;
  }

  /** 容器 */
  public void setContainerBarcode(String containerBarcode) {
    Assert.assertArgumentNotNull(containerBarcode, "containerBarcode");
    Assert.assertStringNotTooLong(containerBarcode, LENGTH_CODE, "containerBarcode");
    this.containerBarcode = containerBarcode;
  }

  public String getBinCode() {
    return binCode;
  }

  /** 货位 */
  public void setBinCode(String binCode) {
    Assert.assertArgumentNotNull(binCode, "binCode");
    this.binCode = binCode;
  }

  public String getArticleSpec() {
    return articleSpec;
  }

  /** 规格 */
  public void setArticleSpec(String articleSpec) {
    this.articleSpec = articleSpec;
  }

  public void validate() {
    Assert.assertArgumentNotNull(amount, "amount");
    Assert.assertArgumentNotNull(article, "article");
    Assert.assertArgumentNotNull(munit, "munit");
    Assert.assertArgumentNotNull(price, "price");
    Assert.assertArgumentNotNull(productionDate, "productionDate");
    Assert.assertArgumentNotNull(qpcStr, "qpcStr");
    Assert.assertArgumentNotNull(qty, "qty");
    Assert.assertArgumentNotNull(returnNtcBillItemUuid, "returnNtcBillItemUuid");
    Assert.assertArgumentNotNull(returnType, "returnType");
    Assert.assertArgumentNotNull(supplier, "supplier");
    Assert.assertArgumentNotNull(validDate, "validDate");
    Assert.assertArgumentNotNull(caseQtyStr, "caseQtyStr");
    Assert.assertArgumentNotNull(containerBarcode, "containerBarcode");
    Assert.assertArgumentNotNull(binCode, "binCode");
    Assert.assertStringNotTooLong(returnBillUuid, LENGTH_UUID, "returnBillUuid");
    Assert.assertStringNotTooLong(returnNtcBillItemUuid, LENGTH_UUID, "returnNtcBillItemUuid");
    Assert.assertStringNotTooLong(munit, LENGTH_CODE, "munit");
    Assert.assertStringNotTooLong(qpcStr, LENGTH_CODE, "qpcStr");
    Assert.assertStringNotTooLong(caseQtyStr, LENGTH_CODE, "caseQtyStr");
    Assert.assertStringNotTooLong(stockBatch, LENGTH_CODE, "stockBatch");
    Assert.assertStringNotTooLong(containerBarcode, LENGTH_CODE, "containerBarcode");

    if (BigDecimal.ZERO.compareTo(qty) >= 0)
      throw new IllegalArgumentException("数量必须大于0。");
    if (BigDecimal.ZERO.compareTo(price) > 0)
      throw new IllegalArgumentException("单价不能小于0");
    if (BigDecimal.ZERO.compareTo(amount) > 0)
      throw new IllegalArgumentException("金额不能小于0。");
  }

}
