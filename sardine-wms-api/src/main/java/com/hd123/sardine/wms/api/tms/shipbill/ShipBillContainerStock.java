/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ShipBillContainerStock.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月15日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.tms.shipbill;

import java.math.BigDecimal;
import java.util.Date;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.Entity;
import com.hd123.sardine.wms.common.entity.SourceBill;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.validator.Validator;

/**
 * 装车单容器库存实体
 * 
 * @author fanqingqing
 *
 */
public class ShipBillContainerStock extends Entity implements Validator {
  private static final long serialVersionUID = -1589580927820368381L;

  private String shipBillUuid;
  private int line;
  private UCN article;
  private UCN customer;
  private String qpcStr;
  private String spec = "-";
  private String munit = "-";
  private SourceBill sourceBill;
  private String binCode;
  private String containerBarcode;
  private Date productionDate;
  private Date validDate;
  private String stockBatch;
  private BigDecimal qty;
  private String caseQtyStr;
  private UCN supplier;
  private UCN shipper = new UCN();
  private BigDecimal price;

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  /** 装车员 */
  public UCN getShipper() {
    return shipper;
  }

  public void setShipper(UCN shipper) {
    this.shipper = shipper;
  }

  /** 装车单UUID */
  public String getShipBillUuid() {
    return shipBillUuid;
  }

  public void setShipBillUuid(String shipBillUuid) {
    // Assert.assertArgumentNotNull(shipBillUuid, "shipBillUuid");
    this.shipBillUuid = shipBillUuid;
  }

  public String getStockBatch() {
    return stockBatch;
  }

  public void setStockBatch(String stockBatch) {
    this.stockBatch = stockBatch;
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
    Assert.assertArgumentNotNull(article, "article");
    this.article = article;
  }

  /** 客户 */
  public UCN getCustomer() {
    return customer;
  }

  public void setCustomer(UCN customer) {
    Assert.assertArgumentNotNull(customer, "customer");
    this.customer = customer;
  }

  /** 包装规格 */
  public String getQpcStr() {
    return qpcStr;
  }

  public void setQpcStr(String qpcStr) {
    Assert.assertArgumentNotNull(qpcStr, "qpcStr");
    this.qpcStr = qpcStr;
  }

  /** 商品规格 */
  public String getSpec() {
    return spec;
  }

  public void setSpec(String spec) {
    Assert.assertArgumentNotNull(spec, "spec");
    this.spec = spec;
  }

  /** 计量单位 */
  public String getMunit() {
    return munit;
  }

  public void setMunit(String munit) {
    Assert.assertArgumentNotNull(munit, "munit");
    this.munit = munit;
  }

  /** 来源单号 */
  public SourceBill getSourceBill() {
    return sourceBill;
  }

  public void setSourceBill(SourceBill sourceBill) {
    Assert.assertArgumentNotNull(sourceBill, "sourceBill");
    this.sourceBill = sourceBill;
  }

  /** 货位 */
  public String getBinCode() {
    return binCode;
  }

  public void setBinCode(String binCode) {
    Assert.assertArgumentNotNull(binCode, "binCode");
    this.binCode = binCode;
  }

  /** 容器 */
  public String getContainerBarcode() {
    return containerBarcode;
  }

  public void setContainerBarcode(String containerBarcode) {
    Assert.assertArgumentNotNull(containerBarcode, "containerBarcode");
    this.containerBarcode = containerBarcode;
  }

  /** 生产日期 */
  public Date getProductionDate() {
    return productionDate;
  }

  public void setProductionDate(Date productionDate) {
    Assert.assertArgumentNotNull(productionDate, "productionDate");
    this.productionDate = productionDate;
  }

  /** 到校日期 */
  public Date getValidDate() {
    return validDate;
  }

  public void setValidDate(Date validDate) {
    Assert.assertArgumentNotNull(validDate, "validDate");
    this.validDate = validDate;
  }

  /** 数量 */
  public BigDecimal getQty() {
    return qty;
  }

  public void setQty(BigDecimal qty) {
    Assert.assertArgumentNotNull(qty, "qty");
    this.qty = qty;
  }

  /** 件数 */
  public String getCaseQtyStr() {
    return caseQtyStr;
  }

  public void setCaseQtyStr(String caseQtyStr) {
    this.caseQtyStr = caseQtyStr;
  }

  /** 供应商 */
  public UCN getSupplier() {
    return supplier;
  }

  public void setSupplier(UCN supplier) {
    Assert.assertArgumentNotNull(supplier, "supplier");
    this.supplier = supplier;
  }

  @Override
  public void validate() {
    Assert.assertArgumentNotNull(article, "article");
    Assert.assertArgumentNotNull(article.getUuid(), "article.uuid");
    Assert.assertArgumentNotNull(article.getCode(), "article.code");
    Assert.assertArgumentNotNull(article.getName(), "article.name");
    Assert.assertArgumentNotNull(customer, "customer");
    Assert.assertArgumentNotNull(customer.getUuid(), "customer.uuid");
    Assert.assertArgumentNotNull(customer.getCode(), "customer.code");
    Assert.assertArgumentNotNull(customer.getName(), "customer.name");
    Assert.assertArgumentNotNull(qpcStr, "qpcStr");
    Assert.assertArgumentNotNull(sourceBill, "sourceBill");
    Assert.assertArgumentNotNull(sourceBill.getBillUuid(), "sourceBill.billUuid");
    Assert.assertArgumentNotNull(sourceBill.getBillNumber(), "sourceBill.billNumber");
    Assert.assertArgumentNotNull(sourceBill.getBillType(), "sourceBill.billType");
    Assert.assertArgumentNotNull(binCode, "binCode");
    Assert.assertArgumentNotNull(containerBarcode, "containerBarcode");
    Assert.assertArgumentNotNull(productionDate, "productionDate");
    Assert.assertArgumentNotNull(validDate, "validDate");
    Assert.assertArgumentNotNull(qty, "qty");
    Assert.assertArgumentNotNull(supplier, "supplier");
    Assert.assertArgumentNotNull(supplier.getUuid(), "supplier.uuid");
    Assert.assertArgumentNotNull(supplier.getCode(), "supplier.code");
    Assert.assertArgumentNotNull(supplier.getName(), "supplier.name");
  }
}
