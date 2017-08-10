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
 * @author zhangsai
 * 
 */
public class StockMajorInfo extends Entity {
  private static final long serialVersionUID = 7740331944373989003L;

  private String owner;
  private String companyUuid;
  private UCN supplier;
  private String binCode;
  private String containerBarcode;
  private UCN article;
  private String munit;
  private String articleSpec;
  private String stockBatch;
  private BigDecimal qty;
  private String qpcStr;
  private Date productionDate;
  private Date validDate;
  private StockState state;

  private UCN warehouse;
  private BinUsage binUsgae;
  private int alclmtdays = 0;

  public int getAlclmtdays() {
    return alclmtdays;
  }

  public void setAlclmtdays(int alclmtdays) {
    this.alclmtdays = alclmtdays;
  }

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

  public UCN getSupplier() {
    return supplier;
  }

  public void setSupplier(UCN supplier) {
    this.supplier = supplier;
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

  public UCN getArticle() {
    return article;
  }

  public void setArticle(UCN article) {
    this.article = article;
  }

  public String getMunit() {
    return munit;
  }

  public void setMunit(String munit) {
    this.munit = munit;
  }

  public String getArticleSpec() {
    return articleSpec;
  }

  public void setArticleSpec(String articleSpec) {
    this.articleSpec = articleSpec;
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

  public boolean isConformAlclmtdays() {
    if (alclmtdays <= 0)
      return true;

    if (DateUtils.addDays(new Date(), alclmtdays - 1).after(validDate))
      return false;

    return true;
  }
}
