/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	OrderBillItem.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月13日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.in.order;

import java.math.BigDecimal;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.Entity;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * 订单明细：实体
 * <p>
 * 同一订单下商品+规格不能重复
 * 
 * @author zhangsai
 *
 */
public class OrderBillItem extends Entity {
  private static final long serialVersionUID = -3943087270888461290L;

  private int line;
  private String orderBillUuid;
  private UCN article;
  private String articleSpec;
  private String qpcStr;
  private String munit;
  private BigDecimal qty;
  private String caseQtyStr;
  private BigDecimal receivedQty;
  private String receivedCaseQtyStr;
  private BigDecimal price;

  /** 行号 */
  public int getLine() {
    return line;
  }

  public void setLine(int line) {
    this.line = line;
  }

  /** 订单UUID */
  public String getOrderBillUuid() {
    return orderBillUuid;
  }

  public void setOrderBillUuid(String orderBillUuid) {
    this.orderBillUuid = orderBillUuid;
  }

  /** 商品 */
  public UCN getArticle() {
    return article;
  }

  public void setArticle(UCN article) {
    this.article = article;
  }

  /** 规格 */
  public String getQpcStr() {
    return qpcStr;
  }

  public void setQpcStr(String qpcStr) {
    this.qpcStr = qpcStr;
  }

  /** 计量单位 */
  public String getMunit() {
    return munit;
  }

  public void setMunit(String munit) {
    this.munit = munit;
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

  /** 已收货数量 */
  public BigDecimal getReceivedQty() {
    return receivedQty;
  }

  public void setReceivedQty(BigDecimal receivedQty) {
    this.receivedQty = receivedQty;
  }

  /** 已收货件数 */
  public String getReceivedCaseQtyStr() {
    return receivedCaseQtyStr;
  }

  public void setReceivedCaseQtyStr(String receivedCaseQtyStr) {
    this.receivedCaseQtyStr = receivedCaseQtyStr;
  }

  /** 商品单价 */
  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public String getArticleSpec() {
    return articleSpec;
  }

  public void setArticleSpec(String articleSpec) {
    this.articleSpec = articleSpec;
  }

  public void validate() {
    Assert.assertArgumentNotNull(article, "article");
    Assert.assertArgumentNotNull(qpcStr, "qpcStr");
    Assert.assertArgumentNotNull(munit, "munit");
    Assert.assertArgumentNotNull(qty, "qty");

    if (BigDecimal.ZERO.compareTo(qty) >= 0)
      throw new IllegalArgumentException("商品" + article.getCode() + "订货数量必须大于0！");
  }
}
