/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-api
 * 文件名：	StockMajorFilter.java
 * 模块说明：	
 * 修改历史：
 * 2014-8-26 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.stock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangsai
 * 
 */
public class StockMajorFilter implements Serializable {
  private static final long serialVersionUID = -3174275230375720354L;

  private List<String> articleUuids = new ArrayList<String>();
  private String companyUuid;

  public String getCompanyUuid() {
    return companyUuid;
  }

  public void setCompanyUuid(String companyUuid) {
    this.companyUuid = companyUuid;
  }

  public List<String> getArticleUuids() {
    return articleUuids;
  }

  public void setArticleUuids(List<String> articleUuids) {
    this.articleUuids = articleUuids;
  }
}
