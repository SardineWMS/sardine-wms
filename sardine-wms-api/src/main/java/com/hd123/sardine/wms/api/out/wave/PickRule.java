/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	PickRule.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月17日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.out.wave;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hd123.sardine.wms.api.out.pickup.PickType;
import com.hd123.sardine.wms.common.entity.OperateMode;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * @author zhangsai
 *
 */
public class PickRule implements Serializable {
  private static final long serialVersionUID = 3892962628533538183L;

  private UCN customer;
  private String deliveryType;
  private OperateMode operateMethod;
  private PickType type;
  private UCN pickArea;
  private BigDecimal volume;
  private String waveUuid;

  public String getWaveUuid() {
    return waveUuid;
  }

  public void setWaveUuid(String waveUuid) {
    this.waveUuid = waveUuid;
  }

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

  public OperateMode getOperateMethod() {
    return operateMethod;
  }

  public void setOperateMethod(OperateMode operateMethod) {
    this.operateMethod = operateMethod;
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

  public BigDecimal getVolume() {
    return volume;
  }

  public void setVolume(BigDecimal volume) {
    this.volume = volume;
  }
}
