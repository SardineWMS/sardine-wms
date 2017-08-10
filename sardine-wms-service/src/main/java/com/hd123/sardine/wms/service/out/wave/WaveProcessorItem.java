/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-core
 * 文件名：	WaveProcessorItem.java
 * 模块说明：	
 * 修改历史：
 * 2014-8-21 - Gao JingYu - 创建。
 */
package com.hd123.sardine.wms.service.out.wave;

import java.math.BigDecimal;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * 波次处理行。
 * 
 * @author Gao JingYu
 * 
 */
public class WaveProcessorItem {
  private UCN customer;
  private UCN warehouse;
  private String deliveryType;

  private String articleUuid;

  private BigDecimal alcQty = BigDecimal.ZERO;
  private BigDecimal pickQty = BigDecimal.ZERO;

  public UCN getCustomer() {
    return customer;
  }

  public void setCustomer(UCN customer) {
    this.customer = customer;
  }

  /** 仓位 */
  public UCN getWarehouse() {
    return warehouse;
  }

  public void setWarehouse(UCN warehouse) {
    this.warehouse = warehouse;
  }

  public String getDeliveryType() {
    return deliveryType;
  }

  public void setDeliveryType(String deliveryType) {
    this.deliveryType = deliveryType;
  }

  public String getArticleUuid() {
    return articleUuid;
  }

  public void setArticleUuid(String articleUuid) {
    this.articleUuid = articleUuid;
  }

  /**
   * 库存分配数量。
   * <p>
   * 根据可用库存，按库存分配逻辑，分配数量。
   */
  public BigDecimal getAlcQty() {
    return alcQty;
  }

  public void setAlcQty(BigDecimal alcQty) {
    this.alcQty = alcQty;
  }

  /**
   * 波次数量
   * <p>
   * 总拣货单数量。
   */
  public BigDecimal getPickQty() {
    return pickQty;
  }

  public void addPickQty(BigDecimal pickQty) {
    Assert.assertArgumentNotNull(pickQty, "pickQty");
    this.pickQty = this.pickQty.add(pickQty);
  }

  /** 获取待拣货数量 */
  public BigDecimal getToPickUpQty() {
    return alcQty.subtract(pickQty);
  }

  /** 是否需要拣货 */
  public boolean needPickUp() {
    return pickQty.compareTo(alcQty) < 0;
  }
}
