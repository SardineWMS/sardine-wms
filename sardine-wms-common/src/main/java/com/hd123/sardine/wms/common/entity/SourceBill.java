/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-common
 * 文件名：	SourceBill.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-28 - Gao JingYu - 创建。
 */
package com.hd123.sardine.wms.common.entity;

import java.io.Serializable;

/**
 * 来源单号
 * 
 * @author Gao JingYu
 */
public class SourceBill implements Serializable {
  private static final long serialVersionUID = -5826701791207651143L;

  private String billUuid;
  private String billNumber;
  private String billType;

  public SourceBill() {

  }

  public SourceBill(String billType, String billUuid, String billNumber) {
    this.billType = billType;
    this.billUuid = billUuid;
    this.billNumber = billNumber;
  }

  /** 来源单UUID */
  public String getBillUuid() {
    return billUuid;
  }

  public void setBillUuid(String billUuid) {
    this.billUuid = billUuid;
  }

  /** 来源单单号 */
  public String getBillNumber() {
    return billNumber;
  }

  public void setBillNumber(String billNumber) {
    this.billNumber = billNumber;
  }

  /** 来源单类型 */
  public String getBillType() {
    return billType;
  }

  public void setBillType(String billType) {
    this.billType = billType;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((billNumber == null) ? 0 : billNumber.hashCode());
    result = prime * result + ((billType == null) ? 0 : billType.hashCode());
    result = prime * result + ((billUuid == null) ? 0 : billUuid.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    SourceBill other = (SourceBill) obj;
    if (billNumber == null) {
      if (other.billNumber != null)
        return false;
    } else if (!billNumber.equals(other.billNumber))
      return false;
    if (billType == null) {
      if (other.billType != null)
        return false;
    } else if (!billType.equals(other.billType))
      return false;
    if (billUuid == null) {
      if (other.billUuid != null)
        return false;
    } else if (!billUuid.equals(other.billUuid))
      return false;
    return true;
  }

}
