/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ShipTaskFilter.java
 * 模块说明：	
 * 修改历史：
 * 2017年9月7日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.tms.shipbill;

import com.hd123.sardine.wms.api.out.alcntc.DeliverySystem;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;

/**
 * @author zhangsai
 *
 */
public class ShipTaskFilter extends PageQueryDefinition {
	private static final long serialVersionUID = -2800718390578716618L;

	private String articleCodeLike;
	private String binCodeLike;
	private String containerBarcodeLike;
	private String customerCodeLike;
	private String supplierCodeLike;
	private String line;
	private String deliveryType;
	private String deliverySystem;
	
	public String getDeliverySystem() {
		return deliverySystem;
	}

	public void setDeliverySystem(String deliverySystem) {
		this.deliverySystem = deliverySystem;
		put("deliverySystem", deliverySystem);
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
		put("line", line);
	}

	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
		put("deliveryType", deliveryType);
	}
	public String getArticleCodeLike() {
		return articleCodeLike;
	}

	public void setArticleCodeLike(String articleCodeLike) {
		this.articleCodeLike = articleCodeLike;
		put("articleCodeLike", articleCodeLike);
	}

	public String getBinCodeLike() {
		return binCodeLike;
	}

	public void setBinCodeLike(String binCodeLike) {
		this.binCodeLike = binCodeLike;
		put("binCodeLike", binCodeLike);
	}

	public String getContainerBarcodeLike() {
		return containerBarcodeLike;
	}

	public void setContainerBarcodeLike(String containerBarcodeLike) {
		this.containerBarcodeLike = containerBarcodeLike;
		put("containerBarcodeLike", containerBarcodeLike);
	}

	public String getCustomerCodeLike() {
		return customerCodeLike;
	}

	public void setCustomerCodeLike(String customerCodeLike) {
		this.customerCodeLike = customerCodeLike;
		put("customerCodeLike", customerCodeLike);
	}

	public String getSupplierCodeLike() {
		return supplierCodeLike;
	}

	public void setSupplierCodeLike(String supplierCodeLike) {
		this.supplierCodeLike = supplierCodeLike;
		put("supplierCodeLike", supplierCodeLike);
	}
}
