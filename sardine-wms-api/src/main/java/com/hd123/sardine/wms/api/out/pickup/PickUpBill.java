/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-api
 * 文件名：	PickUpBill.java
 * 模块说明：	
 * 修改历史：
 * 2014-8-5 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.out.pickup;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.out.wave.WaveBill;
import com.hd123.sardine.wms.common.entity.OperateMode;
import com.hd123.sardine.wms.common.entity.SourceBill;
import com.hd123.sardine.wms.common.entity.StandardEntity;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.validator.Validator;

/**
 * 拣货单：实体
 * 
 * @author zhangsai
 * 
 */
public class PickUpBill extends StandardEntity implements Validator {
  private static final long serialVersionUID = 7299422514258269815L;

  public static final String CAPTION = "拣货单";
  private PickUpBillState state = PickUpBillState.initial;

  private UCN customer;
  private String deliveryType;

  private UCN picker;
  private OperateMode method;
  private PickType type;
  private UCN pickArea;
  private String waveBillNumber;
  private String waveBillUuid;
  private String pickOrder;
  private BigDecimal volume;

  public BigDecimal getVolume() {
    return volume;
  }

  public void setVolume(BigDecimal volume) {
    this.volume = volume;
  }

  private List<PickUpBillItem> items = new ArrayList<PickUpBillItem>();

  @JsonIgnore
  public boolean isFinishPickUp() {
    for (PickUpBillItem item : items) {
      if ((item.getState().equals(PickUpItemState.finished)
          || item.getState().equals(PickUpItemState.stockOut)) == false)
        return false;
    }
    return true;
  }

  /** 状态 */
  public PickUpBillState getState() {
    return state;
  }

  public void setState(PickUpBillState state) {
    Assert.assertArgumentNotNull(state, "state");
    this.state = state;
  }

  /** 客户 */
  public UCN getCustomer() {
    return customer;
  }

  public void setCustomer(UCN customer) {
    this.customer = customer;
  }

  /** 拣货人 */
  public UCN getPicker() {
    return picker;
  }

  public void setPicker(UCN picker) {
    this.picker = picker;
  }

  /** 操作方式 */
  public OperateMode getMethod() {
    return method;
  }

  public void setMethod(OperateMode method) {
    Assert.assertArgumentNotNull(method, "method");
    this.method = method;
  }

  /** 拣货类型 */
  public PickType getType() {
    return type;
  }

  public void setType(PickType type) {
    Assert.assertArgumentNotNull(type, "type");
    this.type = type;
  }

  /** 拣货分区 */
  public UCN getPickArea() {
    return pickArea;
  }

  public void setPickArea(UCN pickArea) {
    this.pickArea = pickArea;
  }

  /** 波次 */
  public String getWaveBillNumber() {
    return waveBillNumber;
  }

  public void setWaveBillNumber(String waveBillNumber) {
    Assert.assertArgumentNotNull(waveBillNumber, "waveBillNumber");
    this.waveBillNumber = waveBillNumber;
  }

  public String getWaveBillUuid() {
    return waveBillUuid;
  }

  public void setWaveBillUuid(String waveBillUuid) {
    this.waveBillUuid = waveBillUuid;
  }

  /** 拣货顺序 */
  public String getPickOrder() {
    return pickOrder;
  }

  public void setPickOrder(String pickOrder) {
    Assert.assertArgumentNotNull(pickOrder, "pickOrder");
    this.pickOrder = pickOrder;
  }

  @JsonIgnore
  public String getWholePickContainerBarcode() {
    return items.get(0).getSourceContainerBarcode();
  }

  /** 拣货单明细 */
  public List<PickUpBillItem> getItems() {
    return items;
  }

  public void setItems(List<PickUpBillItem> items) {
    this.items = items;
  }

  /** 送货类型 */
  public String getDeliveryType() {
    return deliveryType;
  }

  public void setDeliveryType(String deliveryType) {
    Assert.assertArgumentNotNull(deliveryType, "deliveryType");
    this.deliveryType = deliveryType;
  }

  @JsonIgnore
  public SourceBill getWaveSourceBill() {
    return new SourceBill(WaveBill.CAPTION, getWaveBillUuid(), getWaveBillNumber());
  }

  @Override
  public void validate() {
    Assert.assertArgumentNotNull(method, "method");
    Assert.assertArgumentNotNull(state, "state");
    Assert.assertArgumentNotNull(pickOrder, "pickOrder");
    Assert.assertArgumentNotNull(waveBillNumber, "waveBillNumber");
    Assert.assertArgumentNotNull(type, "type");
    Assert.assertArgumentNotNull(customer, "customer");
    Assert.assertArgumentNotNull(deliveryType, "deliveryType");

    if (items.isEmpty())
      throw new IllegalArgumentException("拣货单明细不能为空。");

    for (PickUpBillItem item : items)
      item.validate();

    for (int i = 0; i < items.size(); i++) {
      PickUpBillItem item = items.get(i);
      for (int j = i + 1; j < items.size(); j++) {
        PickUpBillItem jItem = items.get(j);
        if (item.getArticle().getUuid().equals(jItem.getArticle().getUuid())
            && item.getQpcStr().equals(jItem.getQpcStr())
            && item.getSourceBinCode().equals(jItem.getSourceBinCode())
            && item.getSourceContainerBarcode().equals(jItem.getSourceContainerBarcode())
            && item.getAlcNtcBillItemUuid().equals(jItem.getAlcNtcBillItemUuid()))
          throw new IllegalArgumentException("第" + i + "行与第" + j + "行重复。");
      }
    }
  }
}