/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-core
 * 文件名：	RplInfo.java
 * 模块说明：	
 * 修改历史：
 * 2014-8-20 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.out.wave;

import java.math.BigDecimal;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.basicInfo.pickarea.RplQtyMode;
import com.hd123.sardine.wms.api.out.wave.WaveBinUsage;
import com.hd123.sardine.wms.common.entity.OperateMode;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.validator.Validator;

/**
 * 补货需求信息
 * 
 * @author zhangsai
 * 
 */
public class RplRequestInfo implements Validator {

  private String articleUuid;
  private String binCode;
  private UCN wareHouse;
  private BigDecimal stockQty = BigDecimal.ZERO;
  private BigDecimal needRplQty = BigDecimal.ZERO;
  private BigDecimal rplQty = BigDecimal.ZERO;
  private String qpcStr;
  private WaveBinUsage rplBinUsage;
  private UCN pickArea;
  private String waveBillNumber;

  private String companyUuid;
  private OperateMode method;
  private RplQtyMode rplQtyMode;

  public RplQtyMode getRplQtyMode() {
    return rplQtyMode;
  }

  public void setRplQtyMode(RplQtyMode rplQtyMode) {
    this.rplQtyMode = rplQtyMode;
  }

  public OperateMode getMethod() {
    return method;
  }

  public void setMethod(OperateMode method) {
    this.method = method;
  }

  public String getCompanyUuid() {
    return companyUuid;
  }

  public void setCompanyUuid(String companyUuid) {
    this.companyUuid = companyUuid;
  }

  public void setRplQty(BigDecimal rplQty) {
    this.rplQty = rplQty;
  }
  
  public BigDecimal getRplQty() {
    return rplQty;
  }

  /**
   * 是否需要补货
   * <p>
   * 待补货数量大于已补货数量时需要补货
   * 
   * @return true：需要补货，false：不需要补货
   */
  public boolean needRpl() {
    return needRplQty.subtract(rplQty).compareTo(BigDecimal.ZERO) > 0;
  }

  /**
   * 还需要补货的数量
   * <p>
   * 要补货数量-已补货数量
   */
  public BigDecimal getToRplQty() {
    return needRplQty.subtract(rplQty);
  }

  public void addRplQty(BigDecimal rplQty) {
    this.rplQty = this.rplQty.add(rplQty);
  }

  public UCN getWareHouse() {
    return wareHouse;
  }

  public void setWareHouse(UCN wareHouse) {
    this.wareHouse = wareHouse;
  }

  /**
   * 库存可用数量
   */
  public BigDecimal getStockQty() {
    return stockQty;
  }

  public void setStockQty(BigDecimal stockQty) {
    this.stockQty = stockQty;
  }

  /** 当前货位需要补货的数量 */
  public BigDecimal getNeedRplQty() {
    return needRplQty;
  }

  public void setNeedRplQty(BigDecimal needRplQty) {
    this.needRplQty = needRplQty;
  }

  /** 补货商品uuid */
  public String getArticleUuid() {
    return articleUuid;
  }

  public void setArticleUuid(String articleUuid) {
    Assert.assertArgumentNotNull(articleUuid, "articleUuid");
    this.articleUuid = articleUuid;
  }

  /** 待补货货位 */
  public String getBinCode() {
    return binCode;
  }

  public void setBinCode(String binCode) {
    Assert.assertArgumentNotNull(binCode, "binCode");
    this.binCode = binCode;
  }

  /** 补货规格 */
  public String getQpcStr() {
    return qpcStr;
  }

  public void setQpcStr(String qpcStr) {
    this.qpcStr = qpcStr;
  }

  /** 待补货货位用途，参看{@link WaveBinUsage} */
  public WaveBinUsage getRplBinUsage() {
    return rplBinUsage;
  }

  public void setRplBinUsage(WaveBinUsage rplBinUsage) {
    Assert.assertArgumentNotNull(rplBinUsage, "rplBinUsage");
    this.rplBinUsage = rplBinUsage;
  }

  /** 拣货分区 */
  public UCN getPickArea() {
    return pickArea;
  }

  public void setPickArea(UCN pickArea) {
    Assert.assertArgumentNotNull(pickArea, "pickArea");
    this.pickArea = pickArea;
  }

  /** 波次号 */
  public String getWaveBillNumber() {
    return waveBillNumber;
  }

  public void setWaveBillNumber(String waveBillNumber) {
    Assert.assertArgumentNotNull(waveBillNumber, "waveBillNumber");
    this.waveBillNumber = waveBillNumber;
  }

  @Override
  public void validate() {
    Assert.assertArgumentNotNull(articleUuid, "articleUuid");
    Assert.assertArgumentNotNull(binCode, "binCode");
    Assert.assertArgumentNotNull(rplBinUsage, "rplBinUsage");
    Assert.assertArgumentNotNull(pickArea, "pickArea");
    Assert.assertArgumentNotNull(waveBillNumber, "waveBillNumber");
  }
}
