/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	WaveAlcNtcItem.java
 * 模块说明：	
 * 修改历史：
 * 2017年7月21日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.out.alcntc;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hd123.sardine.wms.api.basicInfo.customer.CustomerType;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * @author zhangsai
 *
 */
public class WaveAlcNtcItem implements Serializable {
  private static final long serialVersionUID = -247990980513819609L;

  private String alcNtcBillUuid;
  private String alcNtcBillNumber;
  private UCN customer;
  private CustomerType customerType;
  private UCN wrh;
  private String deliveryType;

  private String alcNtcItemUuid;
  private String articleUuid;

  private BigDecimal alcQty;
  private BigDecimal pickQty = BigDecimal.ZERO;
  private String pickOrder;
  
  private String waveUuid;
  private String companyUuid;

  public String getCompanyUuid() {
    return companyUuid;
  }

  public void setCompanyUuid(String companyUuid) {
    this.companyUuid = companyUuid;
  }

  public String getWaveUuid() {
    return waveUuid;
  }

  public void setWaveUuid(String waveUuid) {
    this.waveUuid = waveUuid;
  }

  public String getAlcNtcBillUuid() {
    return alcNtcBillUuid;
  }

  public void setAlcNtcBillUuid(String alcNtcBillUuid) {
    this.alcNtcBillUuid = alcNtcBillUuid;
  }
  
  public String getAlcNtcBillNumber() {
    return alcNtcBillNumber;
  }

  public void setAlcNtcBillNumber(String alcNtcBillNumber) {
    this.alcNtcBillNumber = alcNtcBillNumber;
  }

  public UCN getCustomer() {
    return customer;
  }

  public void setCustomer(UCN customer) {
    this.customer = customer;
  }

  public CustomerType getCustomerType() {
    return customerType;
  }

  public void setCustomerType(CustomerType customerType) {
    this.customerType = customerType;
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

  public String getAlcNtcItemUuid() {
    return alcNtcItemUuid;
  }

  public void setAlcNtcItemUuid(String alcNtcItemUuid) {
    this.alcNtcItemUuid = alcNtcItemUuid;
  }
  
  public String getArticleUuid() {
    return articleUuid;
  }

  public void setArticleUuid(String articleUuid) {
    this.articleUuid = articleUuid;
  }

  public BigDecimal getAlcQty() {
    return alcQty;
  }

  public void setAlcQty(BigDecimal alcQty) {
    this.alcQty = alcQty;
  }

  public BigDecimal getPickQty() {
    return pickQty;
  }

  public void setPickQty(BigDecimal pickQty) {
    this.pickQty = pickQty;
  }

  public String getPickOrder() {
    return pickOrder;
  }

  public void setPickOrder(String pickOrder) {
    this.pickOrder = pickOrder;
  }

  public void addPickQty(BigDecimal pickQty2) {
    this.pickQty = this.pickQty.add(pickQty2);
  }

  public BigDecimal getToPickUpQty() {
    return alcQty.subtract(pickQty);
  }
}
