/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-api
 * 文件名：	PickUpBillItem.java
 * 模块说明：	
 * 修改历史：
 * 2014-8-5 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.out.pickup;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.basicInfo.container.Container;
import com.hd123.sardine.wms.common.entity.Entity;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.validator.Validator;

/**
 * @author zhangsai
 * 
 */
public class PickUpBillItem extends Entity implements Validator {
  private static final long serialVersionUID = -2636761783201988340L;

  private int line;
  private String pickUpBillUuid;
  private String sourceBinCode;
  private String sourceContainerBarcode = Container.VIRTUALITY_CONTAINER;
  private UCN article;
  private String articleSpec;
  private String munit;
  private String qpcStr;

  private BigDecimal qty;
  private String caseQtyStr;
  private BigDecimal realQty = BigDecimal.ZERO;
  private String realCaseQtyStr;

  private PickUpItemState state = PickUpItemState.initial;
  private String alcNtcBillItemUuid;
  private Date pickTime;
  private String remark;

  public int getLine() {
    return line;
  }

  public void setLine(int line) {
    this.line = line;
  }

  /** 拣货单uuid */
  public String getPickUpBillUuid() {
    return pickUpBillUuid;
  }

  public void setPickUpBillUuid(String pickUpBillUuid) {
    this.pickUpBillUuid = pickUpBillUuid;
  }

  /** 商品 */
  public UCN getArticle() {
    return article;
  }

  public void setArticle(UCN article) {
    Assert.assertArgumentNotNull(article, "article");
    this.article = article;
  }

  /** 规格 */
  public String getQpcStr() {
    return qpcStr;
  }

  public void setQpcStr(String qpcStr) {
    Assert.assertArgumentNotNull(qpcStr, "qpcStr");
    this.qpcStr = qpcStr;
  }

  /** 数量 */
  public BigDecimal getQty() {
    return qty;
  }

  public void setQty(BigDecimal qty) {
    Assert.assertArgumentNotNull(qty, "qty");
    this.qty = qty;
  }

  /** 件数 */
  public String getCaseQtyStr() {
    return caseQtyStr;
  }

  public void setCaseQtyStr(String caseQtyStr) {
    this.caseQtyStr = caseQtyStr;
  }

  @JsonIgnore
  /* 待拣货数量 */
  public BigDecimal getToPickQty() {
    return getQty().subtract(getRealQty());
  }

  /** 实际拣货数量 */
  public BigDecimal getRealQty() {
    return realQty;
  }

  public void setRealQty(BigDecimal realQty) {
    this.realQty = realQty;
  }

  /** 实际拣货件数 */
  public String getRealCaseQtyStr() {
    return realCaseQtyStr;
  }

  public void setRealCaseQtyStr(String realCaseQtyStr) {
    this.realCaseQtyStr = realCaseQtyStr;
  }

  /** 拣货单明细状态 */
  public PickUpItemState getState() {
    return state;
  }

  public void setState(PickUpItemState state) {
    this.state = state;
  }

  /** 配货通知单明细uuid */
  public String getAlcNtcBillItemUuid() {
    return alcNtcBillItemUuid;
  }

  public void setAlcNtcBillItemUuid(String alcNtcBillItemUuid) {
    this.alcNtcBillItemUuid = alcNtcBillItemUuid;
  }

  /** 拣货时间 */
  public Date getPickTime() {
    return pickTime;
  }

  public void setPickTime(Date pickTime) {
    this.pickTime = pickTime;
  }

  /** 来源货位 */
  public String getSourceBinCode() {
    return sourceBinCode;
  }

  public void setSourceBinCode(String sourceBinCode) {
    Assert.assertArgumentNotNull(sourceBinCode, "sourceBinCode");
    this.sourceBinCode = sourceBinCode;
  }

  /** 来源容器 ，只有整托盘拣货时有效 */
  public String getSourceContainerBarcode() {
    return sourceContainerBarcode;
  }

  public void setSourceContainerBarcode(String sourceContainerBarcode) {
    Assert.assertArgumentNotNull(sourceContainerBarcode, "sourceContainerBarcode");
    this.sourceContainerBarcode = sourceContainerBarcode;
  }

  public String getArticleSpec() {
    return articleSpec;
  }

  public void setArticleSpec(String articleSpec) {
    this.articleSpec = articleSpec;
  }

  public String getMunit() {
    return munit;
  }

  public void setMunit(String munit) {
    this.munit = munit;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  @Override
  public void validate() {
    Assert.assertArgumentNotNull(sourceContainerBarcode, "sourceContainerBarcode");
    Assert.assertArgumentNotNull(sourceBinCode, "sourceBinCode");
    Assert.assertArgumentNotNull(qty, "qty");
    Assert.assertArgumentNotNull(qpcStr, "qpcStr");
    Assert.assertArgumentNotNull(article, "article");
  }
}
