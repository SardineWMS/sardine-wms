/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ShipBill.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月15日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.tms.shipbill;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.out.alcntc.DeliverySystem;
import com.hd123.sardine.wms.common.entity.StandardEntity;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.validator.Validator;

/**
 * 装车单实体
 * 
 * @author fanqingqing
 *
 */
public class ShipBill extends StandardEntity implements Validator {
  private static final long serialVersionUID = -8012818602330268139L;
  public static final String CAPTION = "配货通知单";

  private String companyUuid;
  private String billNumber;
  private String vehicleNum;
  private UCN carrier;
  private UCN driver;
  private DeliveryType deliveryType = DeliveryType.warehouse;
  private OperateMethod method = OperateMethod.ManualBill;
  private ShipBillState state = ShipBillState.Initial;
  private String totalCaseQty;
  private BigDecimal totalVolume = BigDecimal.ZERO;
  private BigDecimal totalWeight = BigDecimal.ZERO;
  private BigDecimal totalAmount = BigDecimal.ZERO;
  private int containerCount;
  private int customerCount;
  private DeliverySystem deliverySystem = DeliverySystem.tradition;
  private List<ShipBillContainerStock> containerStocks = new ArrayList<>();
  private List<ShipBillCustomerItem> customerItems = new ArrayList<>();
  private String remark;

  /** 单号 */
  public String getBillNumber() {
    return billNumber;
  }

  public void setBillNumber(String billNumber) {
    // Assert.assertArgumentNotNull(billNumber, "billNumber");
    this.billNumber = billNumber;
  }

  /** 车牌号 */
  public String getVehicleNum() {
    return vehicleNum;
  }

  public void setVehicleNum(String vehicleNum) {
    Assert.assertArgumentNotNull(vehicleNum, "vehicleNum");
    this.vehicleNum = vehicleNum;
  }

  /** 承运商 */
  public UCN getCarrier() {
    return carrier;
  }

  public void setCarrier(UCN carrier) {
    Assert.assertArgumentNotNull(carrier, "carrier");

    this.carrier = carrier;
  }

  /** 司机 */
  public UCN getDriver() {
    return driver;
  }

  public void setDriver(UCN driver) {
    Assert.assertArgumentNotNull(driver, "driver");

    this.driver = driver;
  }

  /** 配送方式 */
  public DeliveryType getDeliveryType() {
    return deliveryType;
  }

  public void setDeliveryType(DeliveryType deliveryType) {
    this.deliveryType = deliveryType;
  }

  /** 操作方式 */
  public OperateMethod getMethod() {
    return method;
  }

  public void setMethod(OperateMethod method) {
    Assert.assertArgumentNotNull(method, "method");
    this.method = method;
  }

  /** 状态 */
  public ShipBillState getState() {
    return state;
  }

  public void setState(ShipBillState state) {
    // Assert.assertArgumentNotNull(state, "state");
    this.state = state;
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
    Assert.assertArgumentNotNull(totalVolume, "totalVolume");
    this.totalVolume = totalVolume;
  }

  /** 总重量 */
  public BigDecimal getTotalWeight() {
    return totalWeight;
  }

  public void setTotalWeight(BigDecimal totalWeight) {
    Assert.assertArgumentNotNull(totalWeight, "totalWeight");
    this.totalWeight = totalWeight;
  }

  /** 总金额 */
  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
    Assert.assertArgumentNotNull(totalAmount, "totalAmount");
    this.totalAmount = totalAmount;
  }

  /** 公司 */
  public String getCompanyUuid() {
    return companyUuid;
  }

  public void setCompanyUuid(String companyUuid) {
    // Assert.assertArgumentNotNull(companyUuid, "companyUuid");
    this.companyUuid = companyUuid;
  }

  public int getContainerCount() {
    return containerCount;
  }

  public void setContainerCount(int containerCount) {
    this.containerCount = containerCount;
  }

  public int getCustomerCount() {
    return customerCount;
  }

  public void setCustomerCount(int customerCount) {
    this.customerCount = customerCount;
  }

  /** 装车容器库存明细 */
  public List<ShipBillContainerStock> getContainerStocks() {
    return containerStocks;
  }

  public void setContainerStocks(List<ShipBillContainerStock> containerStocks) {
    Assert.assertArgumentNotNull(containerStocks, "containerStocks");
    this.containerStocks = containerStocks;
  }

  /** 装车客户明细 */
  public List<ShipBillCustomerItem> getCustomerItems() {
    return customerItems;
  }

  public void setCustomerItems(List<ShipBillCustomerItem> customerItems) {
    // Assert.assertArgumentNotNull(customerItems, "customerItems");
    this.customerItems = customerItems;
  }

  /** 配送体系 */
  public DeliverySystem getDeliverySystem() {
    return deliverySystem;
  }

  public void setDeliverySystem(DeliverySystem deliverySystem) {
    Assert.assertArgumentNotNull(deliverySystem, "deliverySystem");
    this.deliverySystem = deliverySystem;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public void clearTotalInfo() {
    setTotalAmount(BigDecimal.ZERO);
    setTotalCaseQty("0");
    setTotalVolume(BigDecimal.ZERO);
    setTotalWeight(BigDecimal.ZERO);
    setContainerCount(0);
    setCustomerCount(0);
  }

  @Override
  public void validate() {
    Assert.assertArgumentNotNull(companyUuid, "companyUuid");
    Assert.assertArgumentNotNull(vehicleNum, "vehicleNum");
    Assert.assertArgumentNotNull(carrier, "carrier");
    Assert.assertArgumentNotNull(carrier.getUuid(), "carrier.uuid");
    Assert.assertArgumentNotNull(carrier.getCode(), "carrier.code");
    Assert.assertArgumentNotNull(carrier.getName(), "carrier.name");
    Assert.assertArgumentNotNull(driver, "driver");
    Assert.assertArgumentNotNull(driver.getUuid(), "driver.uuid");
    Assert.assertArgumentNotNull(driver.getCode(), "driver.code");
    Assert.assertArgumentNotNull(driver.getName(), "driver.name");
    Assert.assertArgumentNotNull(containerStocks, "containerStocks");
    Assert.assertArgumentNotNull(deliverySystem, "deliverySystem");
    if (containerStocks.isEmpty())
      throw new IllegalArgumentException("装车库存明细不能为空！");
  }

}
