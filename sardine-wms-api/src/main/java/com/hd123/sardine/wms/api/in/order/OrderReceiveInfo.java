/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-api
 * 文件名：	OrderBillReceive.java
 * 模块说明：	
 * 修改历史：
 * 2014-4-18 - WUJING - 创建。
 */
package com.hd123.sardine.wms.api.in.order;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hd123.rumba.commons.lang.Assert;

/**
 * @author WUJING
 * 
 */
public class OrderReceiveInfo implements Serializable {
  private static final long serialVersionUID = 3721412604395389228L;

  private String articleUuid;
  private String qpcStr;
  private BigDecimal qty = BigDecimal.ZERO;

  public String getArticleUuid() {
    return articleUuid;
  }

  public void setArticleUuid(String articleUuid) {
    Assert.assertAttributeNotNull(articleUuid, "articleUuid");
    this.articleUuid = articleUuid;
  }

  public String getQpcStr() {
    return qpcStr;
  }

  public void setQpcStr(String qpcStr) {
    Assert.assertAttributeNotNull(qpcStr, "qpcStr");
    this.qpcStr = qpcStr;
  }

  public BigDecimal getQty() {
    return qty;
  }

  public void setQty(BigDecimal qty) {
    Assert.assertAttributeNotNull(qty, "qty");

    this.qty = qty;
  }
}
