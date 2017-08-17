/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-api
 * 文件名：	StockMajorInfo.java
 * 模块说明：	
 * 修改历史：
 * 2014-8-25 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.stock;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import com.hd123.sardine.wms.api.basicInfo.bin.BinUsage;
import com.hd123.sardine.wms.common.entity.Entity;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * 库存信息，只适用于波次算法
 * 
 * @author zhangsai
 * 
 */
public class StockMajorInfo extends Entity {
  private static final long serialVersionUID = 7740331944373989003L;

  private String owner;
  private String companyUuid;
  private String supplierUuid;
  private String binCode;
  private String containerBarcode;
  private String articleUuid;
  private String munit;
  private String stockBatch;
  private BigDecimal qty;
  private String qpcStr;
  private Date productionDate;
  private Date validDate;
  private StockState state;
  private UCN warehouse;
  private BinUsage binUsgae;

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getCompanyUuid() {
    return companyUuid;
  }

  public void setCompanyUuid(String companyUuid) {
    this.companyUuid = companyUuid;
  }

  public String getSupplierUuid() {
    return supplierUuid;
  }

  public void setSupplierUuid(String supplierUuid) {
    this.supplierUuid = supplierUuid;
  }

  public String getArticleUuid() {
    return articleUuid;
  }

  public void setArticleUuid(String articleUuid) {
    this.articleUuid = articleUuid;
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

  public String getMunit() {
    return munit;
  }

  public void setMunit(String munit) {
    this.munit = munit;
  }

  public String getStockBatch() {
    return stockBatch;
  }

  public void setStockBatch(String stockBatch) {
    this.stockBatch = stockBatch;
  }

  public BigDecimal getQty() {
    return qty;
  }

  public void setQty(BigDecimal qty) {
    this.qty = qty;
  }

  public String getQpcStr() {
    return qpcStr;
  }

  public void setQpcStr(String qpcStr) {
    this.qpcStr = qpcStr;
  }

  public Date getProductionDate() {
    return productionDate;
  }

  public void setProductionDate(Date productionDate) {
    this.productionDate = productionDate;
  }

  public Date getValidDate() {
    return validDate;
  }

  public void setValidDate(Date validDate) {
    this.validDate = validDate;
  }

  public StockState getState() {
    return state;
  }

  public void setState(StockState state) {
    this.state = state;
  }

  public UCN getWarehouse() {
    return warehouse;
  }

  public void setWarehouse(UCN warehouse) {
    this.warehouse = warehouse;
  }

  public BinUsage getBinUsgae() {
    return binUsgae;
  }

  public void setBinUsgae(BinUsage binUsgae) {
    this.binUsgae = binUsgae;
  }

  public boolean isConformAlclmtdays(int alclmtdays) {
    if (alclmtdays <= 0)
      return true;

    if (DateUtils.addDays(new Date(), alclmtdays - 1).after(validDate))
      return false;

    return true;
  }
}
