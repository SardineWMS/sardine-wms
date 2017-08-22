/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-api
 * 文件名：	WaveBillItem.java
 * 模块说明：	
 * 修改历史：
 * 2014-8-18 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.out.wave;

import java.io.Serializable;

import com.hd123.sardine.wms.api.out.pickup.PickType;
import com.hd123.sardine.wms.api.out.pickup.PickUpBillState;
import com.hd123.sardine.wms.common.entity.OperateMode;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * @author zhangsai
 * 
 */
public class WavePickUpBillItem implements Serializable {
  private static final long serialVersionUID = 7515623786449333509L;

  private String waveBillNumber;
  private String pickUpBillNumber;
  private PickUpBillState pickUpBillState;
  private UCN customer;
  private String deliveryType;
  private OperateMode method;
  private PickType type;
  private UCN pickArea;
  private String pickOrder;

  public UCN getCustomer() {
    return customer;
  }

  public void setCustomer(UCN customer) {
    this.customer = customer;
  }

  public String getDeliveryType() {
    return deliveryType;
  }

  public void setDeliveryType(String deliveryType) {
    this.deliveryType = deliveryType;
  }

  public OperateMode getMethod() {
    return method;
  }

  public void setMethod(OperateMode method) {
    this.method = method;
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

  public String getPickOrder() {
    return pickOrder;
  }

  public void setPickOrder(String pickOrder) {
    this.pickOrder = pickOrder;
  }

  public String getPickUpBillNumber() {
    return pickUpBillNumber;
  }

  public void setPickUpBillNumber(String pickUpBillNumber) {
    this.pickUpBillNumber = pickUpBillNumber;
  }

  public PickUpBillState getPickUpBillState() {
    return pickUpBillState;
  }

  public void setPickUpBillState(PickUpBillState pickUpBillState) {
    this.pickUpBillState = pickUpBillState;
  }

  public String getWaveBillNumber() {
    return waveBillNumber;
  }

  public void setWaveBillNumber(String waveBillNumber) {
    this.waveBillNumber = waveBillNumber;
  }
}
