/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	UnshelvedInfo.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月21日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.rtn.rtnsupplierntc;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author yangwenzhu
 *
 */
public class UnshelvedInfo implements Serializable {
  private static final long serialVersionUID = 2994473635106338597L;

  private String rtnSupplierNtcBillNumber;
  private String articleUuid;
  private String qpcStr;
  private BigDecimal unshelvedQty;

  public String getRtnSupplierNtcBillNumber() {
    return rtnSupplierNtcBillNumber;
  }

  public void setRtnSupplierNtcBillNumber(String rtnSupplierNtcBillNumber) {
    this.rtnSupplierNtcBillNumber = rtnSupplierNtcBillNumber;
  }

  public String getArticleUuid() {
    return articleUuid;
  }

  public void setArticleUuid(String articleUuid) {
    this.articleUuid = articleUuid;
  }

  public String getQpcStr() {
    return qpcStr;
  }

  public void setQpcStr(String qpcStr) {
    this.qpcStr = qpcStr;
  }

  public BigDecimal getUnshelvedQty() {
    return unshelvedQty;
  }

  public void setUnshelvedQty(BigDecimal unshelvedQty) {
    this.unshelvedQty = unshelvedQty;
  }

}
