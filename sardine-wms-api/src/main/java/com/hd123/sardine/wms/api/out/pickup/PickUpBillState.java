/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-api
 * 文件名：	PickUpBillState.java
 * 模块说明：	
 * 修改历史：
 * 2014-8-5 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.out.pickup;

/**
 * 拣货单状态：枚举
 * 
 * @author zhangsai
 * 
 */
public enum PickUpBillState {
  /** 待确认 */
  inConfirm("待确认"),

  /** 已批准 */
  approved("已批准"),

  /** 进行中 */
  inProgress("进行中"),

  /** 异常 */
  exception("异常"),

  /** 已审核 */
  audited("已审核");

  private PickUpBillState(String caption) {
    this.caption = caption;
  }

  private String caption;

  public String getCaption() {
    return caption;
  }
}
