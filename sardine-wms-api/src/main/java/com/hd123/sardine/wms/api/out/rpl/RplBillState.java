/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-api
 * 文件名：	RplBillState.java
 * 模块说明：	
 * 修改历史：
 * 2014-8-12 - WUJING - 创建。
 */
package com.hd123.sardine.wms.api.out.rpl;

/**
 * @author WUJING
 * 
 */
public enum RplBillState {

  /** 待确认 */
  inConfirm("待确认"),

  /** 已批准 */
  approved("已批准"),

  /** 已审核 */
  audited("已审核"),

  /** 异常状态 */
  exception("异常"),

  /** 进行中 */
  inProgress("进行中");

  private RplBillState(String caption) {
    this.caption = caption;
  }

  private String caption;

  public String getCaption() {
    return caption;
  }

}
