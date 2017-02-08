/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ArticleSupplier.java
 * 模块说明：	
 * 修改历史：
 * 2017年1月17日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.article;

import com.hd123.sardine.wms.common.entity.Entity;

/**
 * 商品供应商
 * <p>
 * 供应商取自基本资料-供应商<br>
 * 同一商品的供应商不允许重复，且只有一个默认供应商
 * 
 * @author zhangsai
 *
 */
public class ArticleSupplier extends Entity {
  private static final long serialVersionUID = -8198902429554688071L;

  private String articleUuid;
  private String supplierUuid;
  private String supplierCode;
  private String supplierName;
  private boolean default_;

  /** 商品uuid */
  public String getArticleUuid() {
    return articleUuid;
  }

  public void setArticleUuid(String articleUuid) {
    this.articleUuid = articleUuid;
  }

  /** 供应商uuid */
  public String getSupplierUuid() {
    return supplierUuid;
  }

  public void setSupplierUuid(String supplierUuid) {
    this.supplierUuid = supplierUuid;
  }

  /** 供应商代码 */
  public String getSupplierCode() {
    return supplierCode;
  }

  public void setSupplierCode(String supplierCode) {
    this.supplierCode = supplierCode;
  }

  /** 供应商名称 */
  public String getSupplierName() {
    return supplierName;
  }

  public void setSupplierName(String supplierName) {
    this.supplierName = supplierName;
  }

  /** 是否默认供应商 */
  public boolean isDefault_() {
    return default_;
  }

  public void setDefault_(boolean default_) {
    this.default_ = default_;
  }
}
