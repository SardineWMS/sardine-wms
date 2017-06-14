/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ArticleMoveRule.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月13日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.task;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hd123.rumba.commons.lang.Assert;

/**
 * 商品移库规则
 * 
 * @author zhangsai
 *
 */
public class ArticleMoveRule implements Serializable {
  private static final long serialVersionUID = -5059981831651047006L;

  private String articleUuid;
  private String articleCode;
  private String articleName;
  private String fromContainerBarcode;
  private String fromBinCode;
  private String toContainerBarcode;
  private String toBinCode;
  private String qpcStr;
  private String productDate;
  private BigDecimal qty;
  private String supplierUuid;
  private String supplierCode;
  private String supplierName;

  public String getArticleUuid() {
    return articleUuid;
  }

  public void setArticleUuid(String articleUuid) {
    this.articleUuid = articleUuid;
  }

  public String getArticleCode() {
    return articleCode;
  }

  public void setArticleCode(String articleCode) {
    this.articleCode = articleCode;
  }

  public String getArticleName() {
    return articleName;
  }

  public void setArticleName(String articleName) {
    this.articleName = articleName;
  }

  public String getFromContainerBarcode() {
    return fromContainerBarcode;
  }

  public void setFromContainerBarcode(String fromContainerBarcode) {
    this.fromContainerBarcode = fromContainerBarcode;
  }

  public String getFromBinCode() {
    return fromBinCode;
  }

  public void setFromBinCode(String fromBinCode) {
    this.fromBinCode = fromBinCode;
  }

  public String getToContainerBarcode() {
    return toContainerBarcode;
  }

  public void setToContainerBarcode(String toContainerBarcode) {
    this.toContainerBarcode = toContainerBarcode;
  }

  public String getToBinCode() {
    return toBinCode;
  }

  public void setToBinCode(String toBinCode) {
    this.toBinCode = toBinCode;
  }

  public String getQpcStr() {
    return qpcStr;
  }

  public void setQpcStr(String qpcStr) {
    this.qpcStr = qpcStr;
  }

  public String getProductDate() {
    return productDate;
  }

  public void setProductDate(String productDate) {
    this.productDate = productDate;
  }

  public BigDecimal getQty() {
    return qty;
  }

  public void setQty(BigDecimal qty) {
    this.qty = qty;
  }

  public String getSupplierUuid() {
    return supplierUuid;
  }

  public void setSupplierUuid(String supplierUuid) {
    this.supplierUuid = supplierUuid;
  }

  public String getSupplierCode() {
    return supplierCode;
  }

  public void setSupplierCode(String supplierCode) {
    this.supplierCode = supplierCode;
  }

  public String getSupplierName() {
    return supplierName;
  }

  public void setSupplierName(String supplierName) {
    this.supplierName = supplierName;
  }

  public void validate() {
    Assert.assertArgumentNotNull(fromContainerBarcode, "fromContainerBarcode");
    Assert.assertArgumentNotNull(fromBinCode, "fromBinCode");
    Assert.assertArgumentNotNull(toContainerBarcode, "toContainerBarcode");
    Assert.assertArgumentNotNull(toBinCode, "toBinCode");
    Assert.assertArgumentNotNull(articleUuid, "articleUuid");
    Assert.assertArgumentNotNull(qpcStr, "qpcStr");
    Assert.assertArgumentNotNull(productDate, "productDate");
    Assert.assertArgumentNotNull(qty, "qty");

    if (BigDecimal.ZERO.compareTo(qty) >= 0)
      throw new IllegalArgumentException("移库数量必须大于0！");
  }
}
