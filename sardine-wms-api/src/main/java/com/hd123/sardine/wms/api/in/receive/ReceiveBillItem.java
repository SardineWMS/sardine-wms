/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ReceiveBillItem.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-20 - WUJING - 创建。
 */
package com.hd123.sardine.wms.api.in.receive;

import java.math.BigDecimal;
import java.util.Date;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.Entity;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * @author WUJING
 * 
 */
public class ReceiveBillItem extends Entity {

  private static final long serialVersionUID = -8345269229607203571L;

  private int line;
  private String receiveBillUuid;
  private String binCode;
  private String qpcStr;
  private BigDecimal qpc;
  private BigDecimal qty;
  private String caseQtyStr;
  private Date produceDate;
  private Date validDate;
  private String stockBatch;
  private String containerBarcode;
  private UCN article;
  private String articleSpec;
  private String munit;
  private Date receiveDate;
  private String orderBillLineUuid;
  private BigDecimal price;
  private BigDecimal amount;
  
  /** 商品单价，取自订单明细price */
  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  /** 金额 = price*qty */
  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public int getLine() {
    return line;
  }

  public void setLine(int line) {
    this.line = line;
  }

  public String getMunit() {
    return munit;
  }

  public void setMunit(String munit) {
    this.munit = munit;
  }

  public String getOrderBillLineUuid() {
    return orderBillLineUuid;
  }

  public void setOrderBillLineUuid(String orderBillLineUuid) {
    this.orderBillLineUuid = orderBillLineUuid;
  }

  /** 收货单UUID */
  public String getReceiveBillUuid() {
    return receiveBillUuid;
  }

  public void setReceiveBillUuid(String receiveBillUuid) {
    this.receiveBillUuid = receiveBillUuid;
  }

  /** 包装规格 */
  public String getQpcStr() {
    return qpcStr;
  }

  public void setQpcStr(String qpcStr) {
    this.qpcStr = qpcStr;
  }

  /** 单品包装数量 */
  public BigDecimal getQpc() {
    return qpc;
  }

  public void setQpc(BigDecimal qpc) {
    this.qpc = qpc;
  }

  /** 数量 */
  public BigDecimal getQty() {
    return qty;
  }

  public void setQty(BigDecimal qty) {
    this.qty = qty;
  }

  /** 生产日期 */
  public Date getProduceDate() {
    return produceDate;
  }

  public void setProduceDate(Date produceDate) {
    this.produceDate = produceDate;
  }

  /** 到效日期 */
  public Date getValidDate() {
    return validDate;
  }

  /** 货位代码 */
  public String getBinCode() {
    return binCode;
  }

  public void setBinCode(String binCode) {
    this.binCode = binCode;
  }

  public void setValidDate(Date validDate) {
    this.validDate = validDate;
  }

  /** 批次 */
  public String getStockBatch() {
    return stockBatch;
  }

  public void setStockBatch(String stockBatch) {
    this.stockBatch = stockBatch;
  }

  /** 容器条形码 */
  public String getContainerBarcode() {
    return containerBarcode;
  }

  public void setContainerBarcode(String containerBarcode) {
    this.containerBarcode = containerBarcode;
  }

  public UCN getArticle() {
    return article;
  }

  public void setArticle(UCN article) {
    this.article = article;
  }

  /** 件数 */
  public String getCaseQtyStr() {
    return caseQtyStr;
  }

  public void setCaseQtyStr(String caseQtyStr) {
    this.caseQtyStr = caseQtyStr;
  }

  public String getArticleSpec() {
    return articleSpec;
  }

  public void setArticleSpec(String articleSpec) {
    this.articleSpec = articleSpec;
  }

  public Date getReceiveDate() {
    return receiveDate;
  }

  public void setReceiveDate(Date receiveDate) {
    this.receiveDate = receiveDate;
  }

  public void validate() {
    Assert.assertArgumentNotNull(qpcStr, "qpcStr");
    Assert.assertArgumentNotNull(qpc, "qpc");
    Assert.assertArgumentNotNull(qty, "qty");

    if (qty.compareTo(BigDecimal.ZERO) <= 0)
      throw new IllegalArgumentException("qty不能小于等于0。");
    Assert.assertArgumentNotNull(produceDate, "produceDate");
    Assert.assertArgumentNotNull(validDate, "validDate");
    Assert.assertArgumentNotNull(containerBarcode, "containerBarcode");
  }
}
