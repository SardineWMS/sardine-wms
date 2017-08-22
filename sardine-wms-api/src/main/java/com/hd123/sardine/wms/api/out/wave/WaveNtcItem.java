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

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.out.alcntc.AlcNtcBillState;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.validator.Validator;

/**
 * @author zhangsai
 * 
 */
public class WaveNtcItem implements Serializable, Validator {
  private static final long serialVersionUID = 7515623786449333509L;

  private String ntcBillUuid;
  private String waveBillNumber;
  private String ntcBillNumber;
  private AlcNtcBillState ntcBillState;
  private UCN customer;
  private String deliveryType;

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

  public String getNtcBillUuid() {
    return ntcBillUuid;
  }

  public void setNtcBillUuid(String ntcBillUuid) {
    this.ntcBillUuid = ntcBillUuid;
  }

  public String getWaveBillNumber() {
    return waveBillNumber;
  }

  public void setWaveBillNumber(String waveBillNumber) {
    this.waveBillNumber = waveBillNumber;
  }

  public String getNtcBillNumber() {
    return ntcBillNumber;
  }

  public void setNtcBillNumber(String ntcBillNumber) {
    this.ntcBillNumber = ntcBillNumber;
  }

  public AlcNtcBillState getNtcBillState() {
    return ntcBillState;
  }

  public void setNtcBillState(AlcNtcBillState ntcBillState) {
    this.ntcBillState = ntcBillState;
  }

  @Override
  public void validate() {
    Assert.assertArgumentNotNull(ntcBillNumber, "ntcBillNumber");
  }
}
