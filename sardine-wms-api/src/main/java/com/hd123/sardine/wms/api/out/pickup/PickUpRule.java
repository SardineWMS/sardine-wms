/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-api
 * 文件名：	PickUpRule.java
 * 模块说明：	
 * 修改历史：
 * 2014-8-15 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.out.pickup;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.validator.Validator;

/**
 * @author zhangsai
 * 
 */
public class PickUpRule implements Serializable, Validator {
  private static final long serialVersionUID = -5746066036659866248L;

  private String containerBarcode;
  private String pickUpBillItemUuid;
  private BigDecimal qty;
  private String remark;
  private UCN picker;
  private String sourceBinCode;

  public String getSourceBinCode() {
    return sourceBinCode;
  }

  public void setSourceBinCode(String sourceBinCode) {
    this.sourceBinCode = sourceBinCode;
  }

  /** 容器条码，not null */
  public String getContainerBarcode() {
    return containerBarcode;
  }

  public void setContainerBarcode(String containerBarcode) {
    this.containerBarcode = containerBarcode;
  }

  /** 拣货单明细uuid，not null */
  public String getPickUpBillItemUuid() {
    return pickUpBillItemUuid;
  }

  public void setPickUpBillItemUuid(String pickUpBillItemUuid) {
    this.pickUpBillItemUuid = pickUpBillItemUuid;
  }

  /** 实际拣货数量，not null */
  public BigDecimal getQty() {
    return qty;
  }

  public void setQty(BigDecimal qty) {
    this.qty = qty;
  }

  /** 说明 */
  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }
  
  public UCN getPicker() {
    return picker;
  }

  public void setPicker(UCN picker) {
    Assert.assertArgumentNotNull(picker, "picker");
    this.picker = picker;
  }

  @Override
  public void validate() {
    Assert.assertArgumentNotNull(pickUpBillItemUuid, "pickUpBillItemUuid");
    Assert.assertArgumentNotNull(qty, "qty");
    Assert.assertArgumentNotNull(picker, "picker");

    if (BigDecimal.ZERO.compareTo(qty) > 0) {
      throw new IllegalArgumentException("拣货数量不能小于0。");
    }
  }
}
