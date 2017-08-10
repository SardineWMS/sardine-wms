/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	PickTask.java
 * 模块说明：	
 * 修改历史：
 * 2017年7月26日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.out.wave;

import java.math.BigDecimal;
import java.util.Date;

import com.hd123.sardine.wms.api.basicInfo.pickarea.OperateMode;
import com.hd123.sardine.wms.common.entity.Entity;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * 拣货指令
 * 
 * @author zhangsai
 *
 */
public class PickTask extends Entity {
  private static final long serialVersionUID = -6372617670984842698L;

  private String taskGroupNumber;
  private String taskNo;
  private UCN article;
  private String qpcStr;
  private BigDecimal qty;
  private String caseQtyStr;
  private BigDecimal volume;
  private Date productionDate;
  private Date validDate;
  private String stockBatch;
  private UCN supplier;
  private UCN wrh;
  private String deliveryType;
  private String fromBinCode;
  private String fromContainerBarcode;
  private String toBinCode;
  private String toContainerBarcode;
  private String owner;
  private String sourceBillType;
  private String sourceBillNumber;
  private String sourceBillUuid;
  private int sourceBillLine;
  private String sourceBillLineUuid;
  private UCN creator;
  private OperateMode type;
  private Date createTime;
  private String companyUuid;
  private String relateBillNumber;

  public String getTaskGroupNumber() {
    return taskGroupNumber;
  }

  public void setTaskGroupNumber(String taskGroupNumber) {
    this.taskGroupNumber = taskGroupNumber;
  }

  public String getTaskNo() {
    return taskNo;
  }

  public void setTaskNo(String taskNo) {
    this.taskNo = taskNo;
  }

  public UCN getArticle() {
    return article;
  }

  public void setArticle(UCN article) {
    this.article = article;
  }

  public String getQpcStr() {
    return qpcStr;
  }

  public void setQpcStr(String qpcStr) {
    this.qpcStr = qpcStr;
  }

  public BigDecimal getQty() {
    return qty;
  }

  public void setQty(BigDecimal qty) {
    this.qty = qty;
  }

  public String getCaseQtyStr() {
    return caseQtyStr;
  }

  public void setCaseQtyStr(String caseQtyStr) {
    this.caseQtyStr = caseQtyStr;
  }

  public BigDecimal getVolume() {
    return volume;
  }

  public void setVolume(BigDecimal volume) {
    this.volume = volume;
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

  public String getStockBatch() {
    return stockBatch;
  }

  public void setStockBatch(String stockBatch) {
    this.stockBatch = stockBatch;
  }

  public UCN getSupplier() {
    return supplier;
  }

  public void setSupplier(UCN supplier) {
    this.supplier = supplier;
  }

  public UCN getWrh() {
    return wrh;
  }

  public void setWrh(UCN wrh) {
    this.wrh = wrh;
  }

  public String getDeliveryType() {
    return deliveryType;
  }

  public void setDeliveryType(String deliveryType) {
    this.deliveryType = deliveryType;
  }

  public String getFromBinCode() {
    return fromBinCode;
  }

  public void setFromBinCode(String fromBinCode) {
    this.fromBinCode = fromBinCode;
  }

  public String getFromContainerBarcode() {
    return fromContainerBarcode;
  }

  public void setFromContainerBarcode(String fromContainerBarcode) {
    this.fromContainerBarcode = fromContainerBarcode;
  }

  public String getToBinCode() {
    return toBinCode;
  }

  public void setToBinCode(String toBinCode) {
    this.toBinCode = toBinCode;
  }

  public String getToContainerBarcode() {
    return toContainerBarcode;
  }

  public void setToContainerBarcode(String toContainerBarcode) {
    this.toContainerBarcode = toContainerBarcode;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getSourceBillType() {
    return sourceBillType;
  }

  public void setSourceBillType(String sourceBillType) {
    this.sourceBillType = sourceBillType;
  }

  public String getSourceBillNumber() {
    return sourceBillNumber;
  }

  public void setSourceBillNumber(String sourceBillNumber) {
    this.sourceBillNumber = sourceBillNumber;
  }

  public String getSourceBillUuid() {
    return sourceBillUuid;
  }

  public void setSourceBillUuid(String sourceBillUuid) {
    this.sourceBillUuid = sourceBillUuid;
  }

  public int getSourceBillLine() {
    return sourceBillLine;
  }

  public void setSourceBillLine(int sourceBillLine) {
    this.sourceBillLine = sourceBillLine;
  }

  public String getSourceBillLineUuid() {
    return sourceBillLineUuid;
  }

  public void setSourceBillLineUuid(String sourceBillLineUuid) {
    this.sourceBillLineUuid = sourceBillLineUuid;
  }

  public UCN getCreator() {
    return creator;
  }

  public void setCreator(UCN creator) {
    this.creator = creator;
  }

  public OperateMode getType() {
    return type;
  }

  public void setType(OperateMode type) {
    this.type = type;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public String getCompanyUuid() {
    return companyUuid;
  }

  public void setCompanyUuid(String companyUuid) {
    this.companyUuid = companyUuid;
  }
  
  public String getRelateBillNumber() {
    return relateBillNumber;
  }

  public void setRelateBillNumber(String relateBillNumber) {
    this.relateBillNumber = relateBillNumber;
  }
}
