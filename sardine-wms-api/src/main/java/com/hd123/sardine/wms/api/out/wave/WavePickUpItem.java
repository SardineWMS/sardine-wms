/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-core
 * 文件名：	WavePickUpItem.java
 * 模块说明：	
 * 修改历史：
 * 2014-8-21 - Gao JingYu - 创建。
 */
package com.hd123.sardine.wms.api.out.wave;

import java.math.BigDecimal;

import com.hd123.sardine.wms.api.out.pickup.PickType;
import com.hd123.sardine.wms.common.entity.OperateMode;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * 波次拣货明细。
 * 
 * @author Gao JingYu
 * 
 */

public class WavePickUpItem {
  private UCN customer;
  private String deliveryType;

  private OperateMode operateMethod;
  private PickType type;
  private UCN pickArea;

  private String binCode;
  private String containerBarcode;
  private UCN article;
  private String articleSpec;
  private String munit;
  private String qpcStr;
  private BigDecimal qty;
  private String caseQtyStr;

  private BigDecimal itemVolume;
  private BigDecimal volume;

  private String alcNtcBillItemUuid;

  private String waveBillNumber;

  private String waveUuid;
  private WaveBinUsage binUsage;
  private String companyUuid;
  private String pickOrder;

  public String getPickOrder() {
    return pickOrder;
  }

  public void setPickOrder(String pickOrder) {
    this.pickOrder = pickOrder;
  }

  public String getCompanyUuid() {
    return companyUuid;
  }

  public void setCompanyUuid(String companyUuid) {
    this.companyUuid = companyUuid;
  }

  public UCN getCustomer() {
    return customer;
  }

  public void setCustomer(UCN customer) {
    this.customer = customer;
  }

  public UCN getArticle() {
    return article;
  }

  public void setArticle(UCN article) {
    this.article = article;
  }

  public String getArticleSpec() {
    return articleSpec;
  }

  public void setArticleSpec(String articleSpec) {
    this.articleSpec = articleSpec;
  }

  public String getMunit() {
    return munit;
  }

  public void setMunit(String munit) {
    this.munit = munit;
  }

  public void setDeliveryType(String deliveryType) {
    this.deliveryType = deliveryType;
  }

  public void setOperateMethod(OperateMode operateMethod) {
    this.operateMethod = operateMethod;
  }

  public WaveBinUsage getBinUsage() {
    return binUsage;
  }

  public void setBinUsage(WaveBinUsage binUsage) {
    this.binUsage = binUsage;
  }

  public String getWaveBillNumber() {
    return waveBillNumber;
  }

  public void setWaveBillNumber(String waveBillNumber) {
    this.waveBillNumber = waveBillNumber;
  }

  public String getDeliveryType() {
    return deliveryType;
  }

  public OperateMode getOperateMethod() {
    return operateMethod;
  }

  public PickType getType() {
    return type;
  }

  public void setType(PickType type) {
    this.type = type;
  }

  public UCN getPickArea() {
    return pickArea;
  }

  public void setPickArea(UCN pickArea) {
    this.pickArea = pickArea;
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

  public String getQpcStr() {
    return qpcStr;
  }

  public void setQpcStr(String qpcStr) {
    this.qpcStr = qpcStr;
  }

  /** 拣货数量 */
  public BigDecimal getQty() {
    return qty;
  }

  public void setQty(BigDecimal qty) {
    this.qty = qty;
  }

  public String getAlcNtcBillItemUuid() {
    return alcNtcBillItemUuid;
  }

  public void setAlcNtcBillItemUuid(String alcNtcBillItemUuid) {
    this.alcNtcBillItemUuid = alcNtcBillItemUuid;
  }

  public BigDecimal getItemVolume() {
    return itemVolume;
  }

  public void setItemVolume(BigDecimal itemVolume) {
    this.itemVolume = itemVolume;
  }

  public BigDecimal getVolume() {
    return volume;
  }

  public void setVolume(BigDecimal volume) {
    this.volume = volume;
  }

  public String getCaseQtyStr() {
    return caseQtyStr;
  }

  public void setCaseQtyStr(String caseQtyStr) {
    this.caseQtyStr = caseQtyStr;
  }

  /** 生成该明细的波次标识 */
  public String getWaveUuid() {
    return waveUuid;
  }

  public void setWaveUuid(String waveUuid) {
    this.waveUuid = waveUuid;
  }
}
