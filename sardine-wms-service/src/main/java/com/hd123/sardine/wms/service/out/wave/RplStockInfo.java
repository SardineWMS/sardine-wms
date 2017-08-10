/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-core
 * 文件名：	RplStockInfo.java
 * 模块说明：	
 * 修改历史：
 * 2014-8-20 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.out.wave;

import java.math.BigDecimal;
import java.util.Date;

import com.hd123.sardine.wms.common.entity.UCN;

public class RplStockInfo {

  private String owner;
  private String companyUuid;
  private UCN supplier;
  private String binCode;
  private String containerBarcode;
  private UCN article;
  private String productionBatch;
  private String stockBatch;
  private BigDecimal qpc;
  private BigDecimal usableQty = BigDecimal.ZERO;
  private BigDecimal rplQty = BigDecimal.ZERO;
  private String qpcStr;
  private Date productionDate;
  private Date validDate;

  private UCN warehouse;
  private WaveBinUsage binUsage;

  private boolean wholeContainerRplUse;

  /** 增加已补货数量 */
  public void addRplQty(BigDecimal rplQty) {
    this.rplQty = this.rplQty.add(rplQty);
  }

  /** 剩余库存 */
  public BigDecimal getAvailableQty() {
    return usableQty.subtract(rplQty);
  }

  /** 是否有剩余库存 */
  public boolean isAvailable() {
    return getAvailableQty().compareTo(BigDecimal.ZERO) > 0;
  }

  public BigDecimal getRplQty() {
    return rplQty;
  }

  public void setRplQty(BigDecimal rplQty) {
    this.rplQty = rplQty;
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

  public String getContainerBarcode() {
    return containerBarcode;
  }

  public void setContainerBarcode(String containerBarcode) {
    this.containerBarcode = containerBarcode;
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

  public UCN getArticle() {
    return article;
  }

  public void setArticle(UCN article) {
    this.article = article;
  }

  public String getProductionBatch() {
    return productionBatch;
  }

  public void setProductionBatch(String productionBatch) {
    this.productionBatch = productionBatch;
  }

  public String getStockBatch() {
    return stockBatch;
  }

  public void setStockBatch(String stockBatch) {
    this.stockBatch = stockBatch;
  }

  public BigDecimal getQpc() {
    return qpc;
  }

  public void setQpc(BigDecimal qpc) {
    this.qpc = qpc;
  }

  /**
   * 可用库存数量=正常状态库存-不满足配送控制天数的库存
   */
  public BigDecimal getUsableQty() {
    return usableQty;
  }

  public void setUsableQty(BigDecimal usableQty) {
    this.usableQty = usableQty;
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

  public UCN getWarehouse() {
    return warehouse;
  }

  public void setWarehouse(UCN warehouse) {
    this.warehouse = warehouse;
  }

  /** 是否可整托补 */
  public boolean isWholeContainerRplUse() {
    return wholeContainerRplUse;
  }

  public void setWholeContainerRplUse(boolean wholeContainerRplUse) {
    this.wholeContainerRplUse = wholeContainerRplUse;
  }

  /** 货位用途 */
  public WaveBinUsage getBinUsage() {
    return binUsage;
  }

  public void setBinUsage(WaveBinUsage binUsage) {
    this.binUsage = binUsage;
  }
}
