/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	HandoverTaskFilter.java
 * 模块说明：	
 * 修改历史：
 * 2017年9月8日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.rtn.returnsupplier;

import com.hd123.sardine.wms.common.query.PageQueryDefinition;

/**
 * @author zhangsai
 *
 */
public class HandoverTaskFilter extends PageQueryDefinition {
  private static final long serialVersionUID = 3778428534817895766L;

  private String supplierCodeLike;
  private String binCodeLike;
  private String containerBarcodeLike;
  private String articleCodeLike;

  public String getSupplierCodeLike() {
    return supplierCodeLike;
  }

  public void setSupplierCodeLike(String supplierCodeLike) {
    this.supplierCodeLike = supplierCodeLike;
  }

  public String getBinCodeLike() {
    return binCodeLike;
  }

  public void setBinCodeLike(String binCodeLike) {
    this.binCodeLike = binCodeLike;
  }

  public String getContainerBarcodeLike() {
    return containerBarcodeLike;
  }

  public void setContainerBarcodeLike(String containerBarcodeLike) {
    this.containerBarcodeLike = containerBarcodeLike;
  }

  public String getArticleCodeLike() {
    return articleCodeLike;
  }

  public void setArticleCodeLike(String articleCodeLike) {
    this.articleCodeLike = articleCodeLike;
  }
}
