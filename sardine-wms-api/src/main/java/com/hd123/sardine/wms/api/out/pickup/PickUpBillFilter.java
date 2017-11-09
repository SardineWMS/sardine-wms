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
	private String stateEquals;
	private String customerUuidEquals;
	private String articleCodeLike;
	private String pickTypeEquals;
	private String pickerUuidEquals;
	private String deliveryTypeEquals;
	private String methodEquals;
	private String deliverySystemEquals;
	private String fromContainerBarcodeLike;
	private String toContainerBarcodeLike;
	private String fromBinCodeLike;
	private String pickAreaEquals;

	public String getMethodEquals() {
		return methodEquals;
	}

	public void setMethodEquals(String methodEquals) {
		this.methodEquals = methodEquals;
		put("methodEquals", methodEquals);
	}

	public String getDeliverySystemEquals() {
		return deliverySystemEquals;

	}

	public void setDeliverySystemEquals(String deliverySystemEquals) {
		this.deliverySystemEquals = deliverySystemEquals;
		put("deliverySystemEquals", deliverySystemEquals);
	}

	public String getFromContainerBarcodeLike() {
		return fromContainerBarcodeLike;
	}

	public void setFromContainerBarcodeLike(String fromContainerBarcodeLike) {
		this.fromContainerBarcodeLike = fromContainerBarcodeLike;
		put("fromContainerBarcodeLike", fromContainerBarcodeLike);
	}

	public String getToContainerBarcodeLike() {
		return toContainerBarcodeLike;
	}

	public void setToContainerBarcodeLike(String toContainerBarcodeLike) {
		this.toContainerBarcodeLike = toContainerBarcodeLike;
		put("toContainerBarcodeLike", toContainerBarcodeLike);
	}

	public String getFromBinCodeLike() {
		return fromBinCodeLike;
	}

	public void setFromBinCodeLike(String fromBinCodeLike) {
		this.fromBinCodeLike = fromBinCodeLike;
		put("fromBinCodeLike", fromBinCodeLike);
	}

	public String getPickAreaEquals() {
		return pickAreaEquals;
	}

	public void setPickAreaEquals(String pickAreaEquals) {
		this.pickAreaEquals = pickAreaEquals;
		put("pickAreaEquals", pickAreaEquals);
	}

	public String getWaveBillNumberLike() {
		return waveBillNumberLike;
	}

	public void setWaveBillNumberLike(String waveBillNumberLike) {
		this.waveBillNumberLike = waveBillNumberLike;
		put("waveBillNumberLike", waveBillNumberLike);
	}

	public String getCustomerUuidEquals() {
		return customerUuidEquals;
	}

	public void setCustomerUuidEquals(String customerUuidEquals) {
		this.customerUuidEquals = customerUuidEquals;
		put("customerUuidEquals", customerUuidEquals);
	}

	/** 单号类似于 */
	public String getBillNumberLike() {
		return billNumberLike;
	}

	public void setBillNumberLike(String billNumberLike) {
		this.billNumberLike = billNumberLike;
		put("billNumberLike", billNumberLike);
	}

	/** 状态等于 */
	public String getStateEquals() {
		return stateEquals;
	}

	public void setStateEquals(String stateEquals) {
		this.stateEquals = stateEquals;
		put("stateEquals", stateEquals);
	}

	public String getArticleCodeLike() {
		return articleCodeLike;
	}

	public void setArticleCodeLike(String articleCodeLike) {
		this.articleCodeLike = articleCodeLike;
		put("articleCodeLike", articleCodeLike);
	}

	/** 拣货类型 */
	public String getPickTypeEquals() {
		return pickTypeEquals;
	}

	public void setPickTypeEquals(String pickTypeEquals) {
		this.pickTypeEquals = pickTypeEquals;
		put("pickTypeEquals", pickTypeEquals);
	}

	public String getPickerUuidEquals() {
		return pickerUuidEquals;
	}

	public void setPickerUuidEquals(String pickerUuidEquals) {
		this.pickerUuidEquals = pickerUuidEquals;
		put("pickerUuidEquals", pickerUuidEquals);
	}

	public String getDeliveryTypeEquals() {
		return deliveryTypeEquals;
	}

	public void setDeliveryTypeEquals(String deliveryTypeEquals) {
		this.deliveryTypeEquals = deliveryTypeEquals;
		put("deliveryTypeEquals", deliveryTypeEquals);
	}
}
