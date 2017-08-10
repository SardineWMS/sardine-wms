/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	AcceptanceBillItem.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月5日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.out.acceptance;

import java.math.BigDecimal;
import java.util.Date;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.Entity;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * 领用单明细实体
 * 
 * @author fanqingqing
 *
 */
public class AcceptanceBillItem extends Entity {
  private static final long serialVersionUID = -6379236706813119263L;

  private String acceptanceBillUuid;
  private int line;
  private UCN article;
  private UCN supplier;
  private String munit = "-";
  private String qpcStr;
  private String binCode;
  private String containerBarCode;
  private Date productionDate;
  private Date validDate;
  private BigDecimal qty;
  private String caseQtyStr;
  private BigDecimal planQty = BigDecimal.ZERO;
  private String planCaseQtyStr;
  private BigDecimal realQty = BigDecimal.ZERO;
  private String realCaseQtyStr;
  private BigDecimal price = BigDecimal.ZERO;

  public String getAcceptanceBillUuid() {
    return acceptanceBillUuid;
  }

  public void setAcceptanceBillUuid(String acceptanceBillUuid) {
    this.acceptanceBillUuid = acceptanceBillUuid;
  }

  public int getLine() {
    return line;
  }

  public void setLine(int line) {
    this.line = line;
  }

  public UCN getArticle() {
    return article;
  }

  public void setArticle(UCN article) {
    this.article = article;
  }

  public UCN getSupplier() {
    return supplier;
  }

  public void setSupplier(UCN supplier) {
    this.supplier = supplier;
  }

  public String getMunit() {
    return munit;
  }

  public void setMunit(String munit) {
    this.munit = munit;
  }

  public String getQpcStr() {
    return qpcStr;
  }

  public void setQpcStr(String qpcStr) {
    this.qpcStr = qpcStr;
  }

  public String getBinCode() {
    return binCode;
  }

  public void setBinCode(String binCode) {
    this.binCode = binCode;
  }

  public String getContainerBarCode() {
    return containerBarCode;
  }

  public void setContainerBarCode(String containerBarCode) {
    this.containerBarCode = containerBarCode;
  }

  public Date getProductionDate() {
    return productionDate;
  }

  public void setProductionDate(Date productionDate) {
    this.productionDate = productionDate;
  }

  public Date getValidDate() {
    return validDate;
  }

  public void setValidDate(Date validDate) {
    this.validDate = validDate;
  }

  public BigDecimal getQty() {
    return qty;
  }

  public void setQty(BigDecimal qty) {
    this.qty = qty;
  }

  public String getCaseQtyStr() {
    return caseQtyStr;
  }

  public void setCaseQtyStr(String caseQtyStr) {
    this.caseQtyStr = caseQtyStr;
  }

  public BigDecimal getPlanQty() {
    return planQty;
  }

  public void setPlanQty(BigDecimal planQty) {
    this.planQty = planQty;
  }

  public String getPlanCaseQtyStr() {
    return planCaseQtyStr;
  }

  public void setPlanCaseQtyStr(String planCaseQtyStr) {
    this.planCaseQtyStr = planCaseQtyStr;
  }

  public BigDecimal getRealQty() {
    return realQty;
  }

  public void setRealQty(BigDecimal realQty) {
    this.realQty = realQty;
  }

  public String getRealCaseQtyStr() {
    return realCaseQtyStr;
  }

  public void setRealCaseQtyStr(String realCaseQtyStr) {
    this.realCaseQtyStr = realCaseQtyStr;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public void validate() {
    Assert.assertArgumentNotNull(article, "article");
    Assert.assertArgumentNotNull(supplier, "supplier");
    Assert.assertArgumentNotNull(qpcStr, "qpcStr");
    Assert.assertArgumentNotNull(munit, "munit");
    Assert.assertArgumentNotNull(binCode, "binCode");
    Assert.assertArgumentNotNull(containerBarCode, "containerBarCode");
    Assert.assertArgumentNotNull(productionDate, "productionDate");
    Assert.assertArgumentNotNull(validDate, "validDate");
    Assert.assertArgumentNotNull(qty, "qty");
    Assert.assertArgumentNotNull(caseQtyStr, "caseQtyStr");
    Assert.assertArgumentNotNull(price, "price");

    if (BigDecimal.ZERO.compareTo(qty) >= 0)
      throw new IllegalArgumentException("商品" + article.getCode() + "领用数量必须大于0！");
  }
}
