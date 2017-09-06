/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ShipCustomerInfo.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月15日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.tms.shipbill;

import java.math.BigDecimal;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.Entity;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.validator.Validator;

/**
 * 装车单客户明细实体
 * 
 * @author fanqingqing
 *
 */
public class ShipBillCustomerItem extends Entity implements Validator {
  private static final long serialVersionUID = -7764699855387821874L;

  private String shipBillUuid;
  private int line;
  private UCN customer;
  private String orderNo;
  private String totalCaseQty;
  private BigDecimal totalVolume = BigDecimal.ZERO;
  private int containerCount = 0;

  /** 装车单UUID */
  public String getShipBillUuid() {
    return shipBillUuid;
  }

  public void setShipBillUuid(String shipBillUuid) {
    this.shipBillUuid = shipBillUuid;
  }

  /** 行号 */
  public int getLine() {
    return line;
  }

  public void setLine(int line) {
    this.line = line;
  }

  /** 客户 */
  public UCN getCustomer() {
    return customer;
  }

  public void setCustomer(UCN customer) {
    this.customer = customer;
  }

  /** 顺序 */
  public String getOrderNo() {
    return orderNo;
  }

  public void setOrderNo(String orderNo) {
    this.orderNo = orderNo;
  }

  /** 总件数 */
  public String getTotalCaseQty() {
    return totalCaseQty;
  }

  public void setTotalCaseQty(String totalCaseQty) {
    this.totalCaseQty = totalCaseQty;
  }

  /** 总体积 */
  public BigDecimal getTotalVolume() {
    return totalVolume;
  }

  public void setTotalVolume(BigDecimal totalVolume) {
    this.totalVolume = totalVolume;
  }

  /** 容器数 */
  public int getContainerCount() {
    return containerCount;
  }

  public void setContainerCount(int containerCount) {
    this.containerCount = containerCount;
  }

  @Override
  public void validate() {
    Assert.assertArgumentNotNull(shipBillUuid, "shipBillUuid");
    Assert.assertArgumentNotNull(customer, "customer");
    Assert.assertArgumentNotNull(customer.getUuid(), "customer.uuid");
    Assert.assertArgumentNotNull(customer.getCode(), "customer.code");
    Assert.assertArgumentNotNull(customer.getName(), "customer.name");
    Assert.assertArgumentNotNull(totalCaseQty, "totalCaseQty");
    Assert.assertArgumentNotNull(totalVolume, "totalVolume");
  }

}
