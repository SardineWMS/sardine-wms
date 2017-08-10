/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-core
 * 文件名：	PStockCmd.java
 * 模块说明：	
 * 修改历史：
 * 2014-4-23 - WUJING - 创建。
 */
package com.hd123.sardine.wms.dao.stock;

import java.io.Serializable;

/**
 * @author WUJING
 * 
 */
public class StockCmd implements Serializable {

  private static final long serialVersionUID = -5537959458293049870L;

  private String workId;
  private String billUuid;
  private String billNumber;
  private String billType;

  public String getWorkId() {
    return workId;
  }

  public void setWorkId(String workId) {
    this.workId = workId;
  }

  public String getBillUuid() {
    return billUuid;
  }

  public void setBillUuid(String billUuid) {
    this.billUuid = billUuid;
  }

  public String getBillNumber() {
    return billNumber;
  }

  public void setBillNumber(String billNumber) {
    this.billNumber = billNumber;
  }

  public String getBillType() {
    return billType;
  }

  public void setBillType(String billType) {
    this.billType = billType;
  }
}
