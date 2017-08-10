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

  private String altNtcItemUuid;
  private int line;
  private String articleUuid;
  private String articleCode;

  private BigDecimal alcQty;
  private BigDecimal pickQty = BigDecimal.ZERO;
  private String orderNo;

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

  public String getAltNtcItemUuid() {
    return altNtcItemUuid;
  }

  public void setAltNtcItemUuid(String altNtcItemUuid) {
    this.altNtcItemUuid = altNtcItemUuid;
  }
  
  public int getLine() {
    return line;
  }

  public void setLine(int line) {
    this.line = line;
  }

  public String getArticleUuid() {
    return articleUuid;
  }

  public void setArticleUuid(String articleUuid) {
    this.articleUuid = articleUuid;
  }

  public String getArticleCode() {
    return articleCode;
  }

  public void setArticleCode(String articleCode) {
    this.articleCode = articleCode;
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

  public String getOrderNo() {
    return orderNo;
  }

  public void setOrderNo(String orderNo) {
    this.orderNo = orderNo;
  }

  public void addPickQty(BigDecimal pickQty2) {
    this.pickQty = this.pickQty.add(pickQty2);
  }

  public BigDecimal getToPickUpQty() {
    return alcQty.subtract(pickQty);
  }
}
