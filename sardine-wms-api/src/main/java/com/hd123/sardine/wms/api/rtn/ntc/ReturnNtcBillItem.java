/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ReturnNtcBillItem.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月27日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.rtn.ntc;

import java.math.BigDecimal;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.Entity;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * 退仓通知单明细|实体
 * 
 * @author yangwenzhu
 *
 */
public class ReturnNtcBillItem extends Entity {
  private static final long serialVersionUID = 6906720811415493968L;

  public static final int LENGTH_CODE = 30;

  private String returnNtcBillUuid;
  private int line;
  private UCN article;
  private String munit;
  private String qpcStr;
  private UCN supplier;
  private BigDecimal qty = BigDecimal.ZERO;
  private String caseQtyStr;
  private BigDecimal realQty = BigDecimal.ZERO;
  private String realCaseQtyStr;
  private String reason;
  private BigDecimal price = BigDecimal.ZERO;
  private BigDecimal amount = BigDecimal.ZERO;
  private String aritcleSpec;

  public String getReturnNtcBillUuid() {
    return returnNtcBillUuid;
  }

  /** 退仓通知单UUID */
  public void setReturnNtcBillUuid(String returnNtcBillUuid) {
    Assert.assertArgumentNotNull(returnNtcBillUuid, "returnNtcBillUuid");
    this.returnNtcBillUuid = returnNtcBillUuid;
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
    if (BigDecimal.ZERO.compareTo(qty) >= 0)
      throw new IllegalArgumentException("数量必须大于0。");
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

  public BigDecimal getRealQty() {
    return realQty;
  }

  /** 已退数量 */
  public void setRealQty(BigDecimal realQty) {
    Assert.assertArgumentNotNull(realQty, "realQty");
    if (BigDecimal.ZERO.compareTo(realQty) > 0)
      throw new IllegalArgumentException("已退数量不能小于0。");
    this.realQty = realQty;
  }

  public String getRealCaseQtyStr() {
    return realCaseQtyStr;
  }

  /** 已退件数 */
  public void setRealCaseQtyStr(String realCaseQtyStr) {
    Assert.assertArgumentNotNull(realCaseQtyStr, "realCaseQtyStr");
    Assert.assertStringNotTooLong(realCaseQtyStr, LENGTH_CODE, "realCaseQtyStr");
    this.realCaseQtyStr = realCaseQtyStr;
  }

  public String getReason() {
    return reason;
  }

  /** 退仓原因 */
  public void setReason(String reason) {
    Assert.assertStringNotTooLong(reason, LENGTH_CODE, "reason");
    this.reason = reason;
  }

  public BigDecimal getPrice() {
    return price;
  }

  /** 单价 */
  public void setPrice(BigDecimal price) {
    Assert.assertArgumentNotNull(price, "price");
    if (BigDecimal.ZERO.compareTo(price) > 0)
      throw new IllegalArgumentException("单价不能小于0。");
    this.price = price;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  /** 金额 */
  public void setAmount(BigDecimal amount) {
    Assert.assertArgumentNotNull(amount, "amount");
    if (BigDecimal.ZERO.compareTo(amount) > 0)
      throw new IllegalArgumentException("金额不能小于0。");
    this.amount = amount;
  }

  public String getAritcleSpec() {
    return aritcleSpec;
  }

  /** 规格 */
  public void setAritcleSpec(String aritcleSpec) {
    this.aritcleSpec = aritcleSpec;
  }

  public void validate() {
    Assert.assertArgumentNotNull(amount, "amount");
    Assert.assertArgumentNotNull(article, "article");
    Assert.assertArgumentNotNull(munit, "munit");
    Assert.assertArgumentNotNull(price, "price");
    Assert.assertArgumentNotNull(qpcStr, "qpcStr");
    Assert.assertArgumentNotNull(qty, "qty");
    Assert.assertArgumentNotNull(supplier, "supplier");
    Assert.assertArgumentNotNull(caseQtyStr, "caseQtyStr");

    if (BigDecimal.ZERO.compareTo(qty) >= 0)
      throw new IllegalArgumentException("数量必须大于0");
    if (BigDecimal.ZERO.compareTo(realQty) > 0)
      throw new IllegalArgumentException("已退数量不能小于0");
    if (BigDecimal.ZERO.compareTo(amount) > 0)
      throw new IllegalArgumentException("金额不能小于0");
    if (BigDecimal.ZERO.compareTo(price) > 0)
      throw new IllegalArgumentException("单价不能小于0");
  }
}
