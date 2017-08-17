/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-api
 * 文件名：	WaveStock.java
 * 模块说明：	
 * 修改历史：
 * 2014-8-21 - Gao JingYu - 创建。
 */
package com.hd123.sardine.wms.service.out.wave;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.hd123.sardine.wms.api.out.wave.WaveBinUsage;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * 波次库存。
 * <p>
 * 用于波次运算。
 * 
 * @author Gao JingYu
 * 
 */
public class WaveStock implements Serializable, Comparable<WaveStock> {
  private static final long serialVersionUID = -8582722517223673497L;

  private UCN warehouse;
  private String binCode;
  private WaveBinUsage waveBinUsage;
  private boolean wholeContainerUsable;
  private String containerBarcode;
  private String articleUuid;
  private String qpcStr;
  private BigDecimal usableQty = BigDecimal.ZERO;
  private BigDecimal pickQty = BigDecimal.ZERO;
  private Date produceDate;

  public String getArticleUuid() {
    return articleUuid;
  }

  public void setArticleUuid(String articleUuid) {
    this.articleUuid = articleUuid;
  }

  /** 货位代码 */
  public String getBinCode() {
    return binCode;
  }

  public void setBinCode(String binCode) {
    this.binCode = binCode;
  }

  /**
   * 可用库存
   * <p>
   * <ul>
   * <li>存储位：正常状态库存-不符合配送控制天数的库存；</li>
   * <li>动态拣货位：正常状态库存-不符合配送控制天数的库存；</li>
   * <li>整件拣货位：正常状态库存+移库入状态库存-待拣货状态库存-待补货出状态库存；</li>
   * <li>拆零拣货位：正常状态库存+移库入状态库存+待补货入状态库存-待拣货状态库存。</li>
   * </ul>
   * 
   */
  public BigDecimal getUsableQty() {
    return usableQty;
  }

  public void setUsableQty(BigDecimal usableQty) {
    this.usableQty = usableQty;
  }

  /** 拣货数量 */
  public BigDecimal getPickQty() {
    return pickQty;
  }

  public void addPickQty(BigDecimal pickQty) {
    this.pickQty = this.pickQty.add(pickQty);
  }

  /** 剩余库存 */
  public BigDecimal getAvailableQty() {
    return usableQty.subtract(pickQty);
  }

  /** 是否有剩余库存 */
  public boolean isAvailable() {
    return getAvailableQty().compareTo(BigDecimal.ZERO) > 0;
  }

  /** 规格 */
  public String getQpcStr() {
    return qpcStr;
  }

  public void setQpcStr(String qpcStr) {
    this.qpcStr = qpcStr;
  }

  /** 仓位 */
  public UCN getWarehouse() {
    return warehouse;
  }

  public void setWarehouse(UCN warehouse) {
    this.warehouse = warehouse;
  }

  /** 波次货位用途 */
  public WaveBinUsage getWaveBinUsage() {
    return waveBinUsage;
  }

  public void setWaveBinUsage(WaveBinUsage waveBinUsage) {
    this.waveBinUsage = waveBinUsage;
  }

  /**
   * 是否可以整托。
   * <p>
   * 容器库存混载、锁定、移库出、移库出，或存在不满足最低配送天数库存，都不能整托。
   */
  public boolean isWholeContainerUsable() {
    return wholeContainerUsable && BigDecimal.ZERO.compareTo(pickQty) == 0;
  }

  public void setWholeContainerUsable(boolean wholeContainerUsable) {
    this.wholeContainerUsable = wholeContainerUsable;
  }

  public String getContainerBarcode() {
    return containerBarcode;
  }

  public void setContainerBarcode(String containerBarcode) {
    this.containerBarcode = containerBarcode;
  }

  /** 生产日期 */
  public Date getProduceDate() {
    return produceDate;
  }

  public void setProduceDate(Date produceDate) {
    this.produceDate = produceDate;
  }

  @Override
  public int compareTo(WaveStock o) {
    assert produceDate != null;
    assert o.getProduceDate() != null;
    return this.produceDate.compareTo(o.getProduceDate());
  }
}
