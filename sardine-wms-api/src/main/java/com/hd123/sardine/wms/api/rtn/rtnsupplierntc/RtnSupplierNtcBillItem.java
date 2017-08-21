/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	RtnSupplierNtcBillItem.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月15日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.rtn.rtnsupplierntc;

import java.math.BigDecimal;
import java.util.Objects;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.Entity;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.exception.WMSException;

/**
 * @author yangwenzhu
 *
 */
public class RtnSupplierNtcBillItem extends Entity {
  private static final long serialVersionUID = -8277310413666642042L;

  private String rtnSupplierNtcBillUuid;
  private UCN article;
  private String qpcStr;
  private String munit;
  private BigDecimal qty;
  private String caseQtyStr;
  private BigDecimal price;
  private String rtnReason;
  private BigDecimal amount;
  private String unshelvedQtyStr;
  private BigDecimal unshelvedQty;
  private int line;

  public int getLine() {
    return line;
  }

  public void setLine(int line) {
    this.line = line;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  /** 供应商退货通知单UUID */
  public String getRtnSupplierNtcBillUuid() {
    return rtnSupplierNtcBillUuid;
  }

  public void setRtnSupplierNtcBillUuid(String rtnSupplierNtcBillUuid) {
    this.rtnSupplierNtcBillUuid = rtnSupplierNtcBillUuid;
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

  /** 单价 */
  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  /** 退货理由 */
  public String getRtnReason() {
    return rtnReason;
  }

  public void setRtnReason(String rtnReason) {
    this.rtnReason = rtnReason;
  }

  /** 已下架件数 */
  public String getUnshelvedQtyStr() {
    return unshelvedQtyStr;
  }

  public void setUnshelvedQtyStr(String unshelvedQtyStr) {
    this.unshelvedQtyStr = unshelvedQtyStr;
  }

  /** 已下架数量 */
  public BigDecimal getUnshelvedQty() {
    return unshelvedQty;
  }

  public void setUnshelvedQty(BigDecimal unshelvedQty) {
    this.unshelvedQty = unshelvedQty;
  }

  public void validate() throws WMSException {
    Assert.assertArgumentNotNull(article, "article");
    Assert.assertArgumentNotNull(munit, "munit");
    Assert.assertArgumentNotNull(price, "price");
    Assert.assertArgumentNotNull(qpcStr, "qpcStr");
    Assert.assertArgumentNotNull(qty, "qty");
    Assert.assertArgumentNotNull(caseQtyStr, "caseQtyStr");

    if (qty.compareTo(BigDecimal.ZERO) <= 0)
      throw new WMSException("退仓数量必须大于零");
    if (price.compareTo(BigDecimal.ZERO) <= 0)
      throw new WMSException("单价必须大于零");
    if (Objects.isNull(unshelvedQty) == false && unshelvedQty.compareTo(BigDecimal.ZERO) < 0)
      throw new WMSException("已下架数量不能小于零");
  }

}
