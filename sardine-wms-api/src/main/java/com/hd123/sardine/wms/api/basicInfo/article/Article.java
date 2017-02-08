/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	Article.java
 * 模块说明：	
 * 修改历史：
 * 2017年1月12日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.article;

import java.util.ArrayList;
import java.util.List;

import com.hd123.sardine.wms.common.entity.StandardEntity;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * 商品实体
 * <p>
 * uuid = orgid+code，在整个系统内不允许重复<br>
 * 同一组织下code不允许重复
 * 
 * @author zhangsai
 *
 */
public class Article extends StandardEntity {
  private static final long serialVersionUID = 4329860271781901045L;

  private String companyUuid;
  private String code;
  private String name;
  private String spec;
  private ArticleState state = ArticleState.normal;
  private int expDays;
  private UCN category;
  private boolean firstInFirstOut;

  private List<ArticleSupplier> articleSuppliers = new ArrayList<ArticleSupplier>();
  private List<ArticleBarcode> barcodes = new ArrayList<ArticleBarcode>();
  private List<ArticleQpc> qpcs = new ArrayList<ArticleQpc>();

  /** 组织uuid */
  public String getCompanyUuid() {
    return companyUuid;
  }

  public void setCompanyUuid(String companyUuid) {
    this.companyUuid = companyUuid;
  }

  /** 商品代码，长度最大不超过30 */
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  /** 商品名称，长度最大不超过100 */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /** 商品包装规格 */
  public String getSpec() {
    return spec;
  }

  public void setSpec(String spec) {
    this.spec = spec;
  }

  /** 商品状态 */
  public ArticleState getState() {
    return state;
  }

  public void setState(ArticleState state) {
    this.state = state;
  }

  /** 商品保质期天数，0表示不管理保质期 */
  public int getExpDays() {
    return expDays;
  }

  public void setExpDays(int expDays) {
    this.expDays = expDays;
  }

  /** 商品所属类别 */
  public UCN getCategory() {
    return category;
  }

  public void setCategory(UCN category) {
    this.category = category;
  }

  /** 商品出库时是否要遵守先进先出 */
  public boolean isFirstInFirstOut() {
    return firstInFirstOut;
  }

  public void setFirstInFirstOut(boolean firstInFirstOut) {
    this.firstInFirstOut = firstInFirstOut;
  }

  /** 提供该商品的供应商集合 */
  public List<ArticleSupplier> getArticleSuppliers() {
    return articleSuppliers;
  }

  public void setArticleSuppliers(List<ArticleSupplier> articleSuppliers) {
    this.articleSuppliers = articleSuppliers;
  }

  /** 该商品条码集合 */
  public List<ArticleBarcode> getBarcodes() {
    return barcodes;
  }

  public void setBarcodes(List<ArticleBarcode> barcodes) {
    this.barcodes = barcodes;
  }

  /** 商品规格集合 */
  public List<ArticleQpc> getQpcs() {
    return qpcs;
  }

  public void setQpcs(List<ArticleQpc> qpcs) {
    this.qpcs = qpcs;
  }
}
