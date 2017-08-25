/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-api
 * 文件名：	RplBill.java
 * 模块说明：	
 * 修改历史：
 * 2014-8-12 - WUJING - 创建。
 */
package com.hd123.sardine.wms.api.out.rpl;

import java.util.ArrayList;
import java.util.List;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.OperateMode;
import com.hd123.sardine.wms.common.entity.StandardEntity;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.validator.Validator;

/**
 * 补货单 | 实体
 * 
 * @author WUJING
 * 
 */
public class RplBill extends StandardEntity implements Validator {
  public static final String CAPTION = "补货单";
  private static final long serialVersionUID = -4875882739612053329L;

  private String billNumber;
  private String companyUuid;
  private RplBillState state = RplBillState.inConfirm;
  private UCN pickArea;
  private OperateMode method = OperateMode.APP;
  private String waveBillNumber;
  private String waveBillUuid;
  private RplType type = RplType.WholeContainer;
  private UCN rpler;
  private String remark;

  private List<RplBillItem> items = new ArrayList<RplBillItem>();

  public String getBillNumber() {
    return billNumber;
  }

  public void setBillNumber(String billNumber) {
    this.billNumber = billNumber;
  }

  public String getCompanyUuid() {
    return companyUuid;
  }

  public void setCompanyUuid(String companyUuid) {
    this.companyUuid = companyUuid;
  }

  /** 状态 */
  public RplBillState getState() {
    return state;
  }

  public void setState(RplBillState state) {
    Assert.assertArgumentNotNull(state, "state");
    this.state = state;
  }

  /** 拣货分区 */
  public UCN getPickArea() {
    return pickArea;
  }

  public void setPickArea(UCN pickArea) {
    Assert.assertArgumentNotNull(pickArea, "pickArea");
    this.pickArea = pickArea;
  }

  /** 单据类型 */
  public OperateMode getMethod() {
    return method;
  }

  public void setMethod(OperateMode method) {
    this.method = method;
  }

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

  /** 补货类型 */
  public RplType getType() {
    return type;
  }

  public void setType(RplType type) {
    Assert.assertArgumentNotNull(type, "type");
    this.type = type;
  }

  /** 拣货人 */
  public UCN getRpler() {
    return rpler;
  }

  public void setRpler(UCN rpler) {
    this.rpler = rpler;
  }
  
  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public List<RplBillItem> getItems() {
    return items;
  }

  public void setItems(List<RplBillItem> items) {
    this.items = items;
  }

  @Override
  public void validate() {
    Assert.assertArgumentNotNull(state, "state");
    Assert.assertArgumentNotNull(pickArea, "pickArea");
    Assert.assertArgumentNotNull(method, "method");
    Assert.assertArgumentNotNull(waveBillNumber, "waveBillNumber");

    if (items.isEmpty())
      throw new IllegalArgumentException("补货单明细不能为空。");

    for (RplBillItem item : items)
      item.validate();

    for (int i = 0; i < items.size(); i++) {
      RplBillItem item = items.get(i);
      for (int j = i + 1; j < items.size(); j++) {
        RplBillItem jItem = items.get(j);
        if (item.getArticle().getUuid().equals(jItem.getArticle().getUuid())
            && item.getFromBinCode().equals(jItem.getFromBinCode())
            && item.getToBinCode().equals(jItem.getToBinCode())
            && item.getContainerBarcode().equals(jItem.getContainerBarcode())
            && item.getStockBatch().equals(jItem.getStockBatch())
            && item.getQpcStr().equals(jItem.getQpcStr())
            && item.getSupplier().equals(jItem.getSupplier())
            && item.getProductionBatch().equals(jItem.getProductionBatch()))
          throw new IllegalArgumentException("第" + i + "行与第" + j + "行重复。");
      }
    }
  }
}
