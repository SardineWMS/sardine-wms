/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	OnWayStock.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月11日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.stock;

import java.math.BigDecimal;
import java.util.Date;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.common.entity.Entity;

/**
 * 在途库存
 * 
 * @author zhangsai
 *
 */
public class OnWayStock extends Entity {
  private static final long serialVersionUID = 6565887818125100165L;

  private String stockUuid;
  private String binCode;
  private String containerBarcode;
  private String stockBatch;
  private String articleUuid;
  private String qpcStr;
  private String supplierUuid;
  private Date productDate;
  private BigDecimal qty;
  private String billNumber;
  private String billType;
  private String billUuid;
  private int billLine;
  private String billLineUuid;

  public String getBillLineUuid() {
    return billLineUuid;
  }

  public void setBillLineUuid(String billLineUuid) {
    this.billLineUuid = billLineUuid;
  }

  public String getBillNumber() {
    return billNumber;
  }

  public void setBillNumber(String billNumber) {
    this.billNumber = billNumber;
  }

  public String getBillType() {
    return billType;
  }

  public void setBillType(String billType) {
    this.billType = billType;
  }

  public String getBillUuid() {
    return billUuid;
  }

  public void setBillUuid(String billUuid) {
    this.billUuid = billUuid;
  }

  public int getBillLine() {
    return billLine;
  }

  public void setBillLine(int billLine) {
    this.billLine = billLine;
  }

  public String getBinCode() {
    return binCode;
  }

  public void setBinCode(String binCode) {
    this.binCode = binCode;
  }

  public String getContainerBarcode() {
    return containerBarcode;
  }

  public void setContainerBarcode(String containerBarcode) {
    this.containerBarcode = containerBarcode;
  }

  public String getArticleUuid() {
    return articleUuid;
  }

  public void setArticleUuid(String articleUuid) {
    this.articleUuid = articleUuid;
  }

  public String getStockBatch() {
    return stockBatch;
  }

  public void setStockBatch(String stockBatch) {
    this.stockBatch = stockBatch;
  }
  
  public String getQpcStr() {
    return qpcStr;
  }

  public void setQpcStr(String qpcStr) {
    this.qpcStr = qpcStr;
  }

  public String getSupplierUuid() {
    return supplierUuid;
  }

  public void setSupplierUuid(String supplierUuid) {
    this.supplierUuid = supplierUuid;
  }
  
  public Date getProductDate() {
    return productDate;
  }

  public void setProductDate(Date productDate) {
    this.productDate = productDate;
  }

  /** 在途库存对应的库存UUID */
  public String getStockUuid() {
    return stockUuid;
  }

  public void setStockUuid(String stockUuid) {
    this.stockUuid = stockUuid;
  }

  /** 在途库存数量，可正可负不能为0,0的时候就没有这一条了 */
  public BigDecimal getQty() {
    return qty;
  }

  public void setQty(BigDecimal qty) {
    this.qty = qty;
  }

  public void validate() {
    Assert.assertArgumentNotNull(qty, "qty");
    if (qty.compareTo(BigDecimal.ZERO) == 0)
      throw new IllegalArgumentException("入库的库存数量不能等于0！");

    Assert.assertArgumentNotNull(billNumber, "billNumber");
    Assert.assertArgumentNotNull(billUuid, "billUuid");
    Assert.assertArgumentNotNull(billType, "billType");

    if (StringUtil.isNullOrBlank(stockUuid) == false)
      return;

    if (StringUtil.isNullOrBlank(binCode) || StringUtil.isNullOrBlank(containerBarcode)
        || StringUtil.isNullOrBlank(stockBatch) || StringUtil.isNullOrBlank(articleUuid))
      throw new IllegalArgumentException("库存主键不能为空！");
  }
}
