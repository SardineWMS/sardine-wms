/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	AlcNtcBillItem.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月31日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.out.alcntc;

import java.math.BigDecimal;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.Entity;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * 配单明细|实体类
 * 
 * @author yangwenzhu
 *
 */
public class AlcNtcBillItem extends Entity {
  private static final long serialVersionUID = 5981670250287379589L;

  private String alcNtcBillUuid;
  private int line;
  private UCN article;
  private String qpcStr;
  private String munit;
  private AlcNtcBillItemState state = AlcNtcBillItemState.initial;
  private BigDecimal qty = BigDecimal.ZERO;
  private String caseQtyStr;
  private BigDecimal planQty = BigDecimal.ZERO;
  private String planCaseQtyStr;
  private BigDecimal realQty = BigDecimal.ZERO;
  private String realCaseQtyStr;
  private BigDecimal price = BigDecimal.ZERO;

  /** 配单UUID */
  public String getAlcNtcBillUuid() {
    return alcNtcBillUuid;
  }

  public void setAlcNtcBillUuid(String alcNtcBillUuid) {
    this.alcNtcBillUuid = alcNtcBillUuid;
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
  public String getQpcStr() {
    return qpcStr;
  }

  public void setQpcStr(String qpcStr) {
    this.qpcStr = qpcStr;
  }

  /** 包装单位 */
  public String getMunit() {
    return munit;
  }

  public void setMunit(String munit) {
    this.munit = munit;
  }

  public AlcNtcBillItemState getState() {
    return state;
  }

  public void setState(AlcNtcBillItemState state) {
    this.state = state;
  }

  /** 要货数量 */
  public BigDecimal getQty() {
    return qty;
  }

  public void setQty(BigDecimal qty) {
    this.qty = qty;
  }

  /** 要货件数 */
  public String getCaseQtyStr() {
    return caseQtyStr;
  }

  public void setCaseQtyStr(String caseQtyStr) {
    this.caseQtyStr = caseQtyStr;
  }

  /** 计划数量 */
  public BigDecimal getPlanQty() {
    return planQty;
  }

  public void setPlanQty(BigDecimal planQty) {
    this.planQty = planQty;
  }

  /** 计划件数 */
  public String getPlanCaseQtyStr() {
    return planCaseQtyStr;
  }

  public void setPlanCaseQtyStr(String planCaseQtyStr) {
    this.planCaseQtyStr = planCaseQtyStr;
  }

  /** 实际数量 */
  public BigDecimal getRealQty() {
    return realQty;
  }

  public void setRealQty(BigDecimal realQty) {
    this.realQty = realQty;
  }

  public String getRealCaseQtyStr() {
    return realCaseQtyStr;
  }

  /** 实际件数 */
  public void setRealCaseQtyStr(String realCaseQtyStr) {
    this.realCaseQtyStr = realCaseQtyStr;
  }

  /** 单价 */
  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public void validate() {
    Assert.assertArgumentNotNull(article, "article");
    Assert.assertArgumentNotNull(qpcStr, "qpcStr");
    Assert.assertArgumentNotNull(munit, "munit");
    Assert.assertArgumentNotNull(caseQtyStr, "caseQtyStr");

    if (qty.compareTo(BigDecimal.ZERO) <= 0)
      throw new IllegalArgumentException("要货数量不能小于等于零");
  }

}
