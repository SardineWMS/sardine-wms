/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-api
 * 文件名：	PickUpBillItemItem.java
 * 模块说明：	
 * 修改历史：
 * 2014-8-5 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.out.pickup;

import java.math.BigDecimal;
import java.util.Date;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.Entity;
import com.hd123.sardine.wms.common.validator.Validator;

/**
 * 拣货单库存明细：实体
 * 
 * @author zhangsai
 * 
 */
public class PickUpBillStockItem extends Entity implements Validator {
  private static final long serialVersionUID = -6159785665110755398L;

  private String pickUpBillItemUuid;
  private String articleUuid;
  private String qpcStr;
  private BigDecimal qty;
  private String caseQtyStr;
  private String targetContainerBarcode;
  private String stockBatch;
  private String productionBatch;
  private Date validDate;
  private Date productionDate;
  private String supplierUuid;

  /** 拣货单明细uuid */
  public String getPickUpBillItemUuid() {
    return pickUpBillItemUuid;
  }

  public void setPickUpBillItemUuid(String pickUpBillItemUuid) {
    Assert.assertArgumentNotNull(pickUpBillItemUuid, "pickUpBillItemUuid");
    this.pickUpBillItemUuid = pickUpBillItemUuid;
  }

  /** 规格 */
  public String getQpcStr() {
    return qpcStr;
  }

  public void setQpcStr(String qpcStr) {
    Assert.assertArgumentNotNull(qpcStr, "qpcStr");
    this.qpcStr = qpcStr;
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

  /** 批次 */
  public String getStockBatch() {
    return stockBatch;
  }

  /** 容器条码 */
  public String getTargetContainerBarcode() {
    return targetContainerBarcode;
  }

  public void setTargetContainerBarcode(String targetContainerBarcode) {
    this.targetContainerBarcode = targetContainerBarcode;
  }

  public void setStockBatch(String stockBatch) {
    Assert.assertArgumentNotNull(stockBatch, "stockBatch");
    this.stockBatch = stockBatch;
  }

  /** 批次 */
  public String getProductionBatch() {
    return productionBatch;
  }

  public void setProductionBatch(String productionBatch) {
    Assert.assertArgumentNotNull(productionBatch, "productionBatch");
    this.productionBatch = productionBatch;
  }

  public String getArticleUuid() {
    return articleUuid;
  }

  public void setArticleUuid(String articleUuid) {
    Assert.assertArgumentNotNull(articleUuid, "articleUuid");
    this.articleUuid = articleUuid;
  }

  public Date getValidDate() {
    return validDate;
  }

  public void setValidDate(Date validDate) {
    Assert.assertArgumentNotNull(validDate, "validDate");
    this.validDate = validDate;
  }

  public Date getProductionDate() {
    return productionDate;
  }

  public void setProductionDate(Date productionDate) {
    Assert.assertArgumentNotNull(productionDate, "productionDate");
    this.productionDate = productionDate;
  }

  public String getSupplierUuid() {
    return supplierUuid;
  }

  public void setSupplierUuid(String supplierUuid) {
    Assert.assertArgumentNotNull(supplierUuid, "supplierUuid");
    this.supplierUuid = supplierUuid;
  }

  @Override
  public void validate() {
    Assert.assertArgumentNotNull(qpcStr, "qpcStr");
    Assert.assertArgumentNotNull(qty, "qty");
    Assert.assertArgumentNotNull(caseQtyStr, "caseQtyStr");
    Assert.assertArgumentNotNull(targetContainerBarcode, "targetContainerBarcode");
    Assert.assertArgumentNotNull(productionBatch, "productionBatch");
    Assert.assertArgumentNotNull(stockBatch, "stockBatch");
    Assert.assertArgumentNotNull(supplierUuid, "supplierUuid");
    Assert.assertArgumentNotNull(productionDate, "productionDate");
    Assert.assertArgumentNotNull(validDate, "validDate");
    Assert.assertArgumentNotNull(articleUuid, "articleUuid");
  }
}
