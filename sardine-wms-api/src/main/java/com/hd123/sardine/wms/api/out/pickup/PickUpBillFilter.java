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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.common.query.OrderDir;

/**
 * 拣货单查询条件
 * 
 * @author zhangsai
 * 
 */
public class PickUpBillFilter implements Serializable {
  private static final long serialVersionUID = 7895700902465858571L;

  private String billNumberLike;
  private String waveNoLike;
  private PickUpBillState stateEquals;
  private String storeUuidEquals;
  private String deliveryTargetCode;
  private String operaterMethodEquals;
  private PickType pickTypeEquals;
  private String pickUpBinContain;
  private String articleCodeEquals;
  private String pickerUuidEquals;
  private String deliveryTypeEquals;
  private String pickMethodEquals;
  private String pickOrderLevelEquals;
  private String deliveryTargetUuid;

  private List<String> pickAreaUuids = new ArrayList<String>();
  private List<PickUpBillState> pickStates = new ArrayList<PickUpBillState>();

  private int pageSize = 0;
  private int page = 0;
  private String orgEquals;
  private String sortField;
  private OrderDir orderDir = OrderDir.asc;

  public List<String> getPickAreaUuids() {
    return pickAreaUuids;
  }

  public void setPickAreaUuids(List<String> pickAreaUuids) {
    this.pickAreaUuids = pickAreaUuids;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public String getOrgEquals() {
    return orgEquals;
  }

  public void setOrgEquals(String orgEquals) {
    this.orgEquals = orgEquals;
  }

  public String getSortField() {
    return sortField;
  }

  public void setSortField(String sortField) {
    this.sortField = sortField;
  }

  public OrderDir getOrderDir() {
    return orderDir;
  }

  public void setOrderDir(OrderDir orderDir) {
    this.orderDir = orderDir;
  }

  /** 单号类似于 */
  public String getBillNumberLike() {
    return billNumberLike;
  }

  public void setBillNumberLike(String billNumberLike) {
    this.billNumberLike = billNumberLike;
  }

  /** 批次号类似于 */
  public String getWaveNoLike() {
    return waveNoLike;
  }

  public void setWaveNoLike(String waveNoLike) {
    this.waveNoLike = waveNoLike;
  }

  /** 状态等于 */
  public PickUpBillState getStateEquals() {
    return stateEquals;
  }

  public void setStateEquals(PickUpBillState stateEquals) {
    this.stateEquals = stateEquals;
    addPickState(stateEquals);
  }

  /** 门店uuid等于 */
  public String getStoreUuidEquals() {
    return storeUuidEquals;
  }

  public void setStoreUuidEquals(String storeUuidEquals) {
    this.storeUuidEquals = storeUuidEquals;
  }

  public String getPickOrderLevelEquals() {
    return pickOrderLevelEquals;
  }

  public void setPickOrderLevelEquals(String pickOrderLevelEquals) {
    this.pickOrderLevelEquals = pickOrderLevelEquals;
  }

  public String getDeliveryTargetCode() {
    return deliveryTargetCode;
  }

  public void setDeliveryTargetCode(String deliveryTargetCode) {
    this.deliveryTargetCode = deliveryTargetCode;
  }

  public void addPickAreaUuid(String pickAreaUuidEquals) {
    if (StringUtil.isNullOrBlank(pickAreaUuidEquals))
      return;

    if (pickAreaUuids == null)
      pickAreaUuids = new ArrayList<String>();

    pickAreaUuids.add(pickAreaUuidEquals);
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

  public String getPickUpBinContain() {
    return pickUpBinContain;
  }

  public void setPickUpBinContain(String pickUpBinContain) {
    this.pickUpBinContain = pickUpBinContain;
  }

  public String getArticleCodeEquals() {
    return articleCodeEquals;
  }

  public void setArticleCodeEquals(String articleCodeEquals) {
    this.articleCodeEquals = articleCodeEquals;
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

  public List<PickUpBillState> getPickStates() {
    return pickStates;
  }

  public void setPickStates(List<PickUpBillState> pickStates) {
    this.pickStates = pickStates;
  }

  public void addPickState(PickUpBillState state) {
    if (state == null)
      return;

    if (pickStates == null)
      pickStates = new ArrayList<PickUpBillState>();

    pickStates.add(state);
  }

  public String getDeliveryTargetUuid() {
    return deliveryTargetUuid;
  }

  public void setDeliveryTargetUuid(String deliveryTargetUuid) {
    this.deliveryTargetUuid = deliveryTargetUuid;
  }
}
