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

  private String articleUuid;
  private List<String> binCodes = new ArrayList<String>();
  private List<String> articleUuids = new ArrayList<String>();
  private String warehouseUuid;
  private String orgId;

  public List<String> getArticleUuids() {
    return articleUuids;
  }

  public void setArticleUuids(List<String> articleUuids) {
    this.articleUuids = articleUuids;
  }
  
  public String getArticleUuid() {
    return articleUuid;
  }

  public void setArticleUuid(String articleUuid) {
    this.articleUuid = articleUuid;
  }

  public List<String> getBinCodes() {
    return binCodes;
  }

  public void setBinCodes(List<String> binCodes) {
    this.binCodes = binCodes;
  }

  public String getWarehouseUuid() {
    return warehouseUuid;
  }

  public void setWarehouseUuid(String warehouseUuid) {
    this.warehouseUuid = warehouseUuid;
  }

  public String getOrgId() {
    return orgId;
  }

  public void setOrgId(String orgId) {
    this.orgId = orgId;
  }
}
