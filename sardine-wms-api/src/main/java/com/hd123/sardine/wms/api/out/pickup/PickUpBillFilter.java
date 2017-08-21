/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-api
 * 文件名：	PickUpBillFilter.java
 * 模块说明：	
 * 修改历史：
 * 2014-8-5 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.out.pickup;

import com.hd123.sardine.wms.common.query.PageQueryDefinition;

/**
 * 拣货单查询条件
 * 
 * @author zhangsai
 * 
 */
public class PickUpBillFilter extends PageQueryDefinition {
  private static final long serialVersionUID = 7895700902465858571L;

  private String billNumberLike;
  private String waveBillNumberLike;
  private PickUpBillState stateEquals;
  private String customerUuidEquals;
  private String operaterMethodEquals;
  private PickType pickTypeEquals;
  private String pickerUuidEquals;
  private String deliveryTypeEquals;
  private String pickMethodEquals;
  
  public String getWaveBillNumberLike() {
    return waveBillNumberLike;
  }

  public void setWaveBillNumberLike(String waveBillNumberLike) {
    this.waveBillNumberLike = waveBillNumberLike;
  }

  public String getCustomerUuidEquals() {
    return customerUuidEquals;
  }

  public void setCustomerUuidEquals(String customerUuidEquals) {
    this.customerUuidEquals = customerUuidEquals;
  }

  /** 单号类似于 */
  public String getBillNumberLike() {
    return billNumberLike;
  }

  public void setBillNumberLike(String billNumberLike) {
    this.billNumberLike = billNumberLike;
  }
  
  /** 状态等于 */
  public PickUpBillState getStateEquals() {
    return stateEquals;
  }

  public void setStateEquals(PickUpBillState stateEquals) {
    this.stateEquals = stateEquals;
  }

  public String getOperaterMethodEquals() {
    return operaterMethodEquals;
  }

  public void setOperaterMethodEquals(String operaterMethodEquals) {
    this.operaterMethodEquals = operaterMethodEquals;
  }

  /** 拣货类型 */
  public PickType getPickTypeEquals() {
    return pickTypeEquals;
  }

  public void setPickTypeEquals(PickType pickTypeEquals) {
    this.pickTypeEquals = pickTypeEquals;
  }

  public String getPickerUuidEquals() {
    return pickerUuidEquals;
  }

  public void setPickerUuidEquals(String pickerUuidEquals) {
    this.pickerUuidEquals = pickerUuidEquals;
  }

  public String getDeliveryTypeEquals() {
    return deliveryTypeEquals;
  }

  public void setDeliveryTypeEquals(String deliveryTypeEquals) {
    this.deliveryTypeEquals = deliveryTypeEquals;
  }

  public String getPickMethodEquals() {
    return pickMethodEquals;
  }

  public void setPickMethodEquals(String pickMethodEquals) {
    this.pickMethodEquals = pickMethodEquals;
  }
}
