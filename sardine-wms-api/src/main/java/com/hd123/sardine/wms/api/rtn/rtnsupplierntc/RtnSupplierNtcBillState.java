/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	RtnSupplierNtcBillState.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月15日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.rtn.rtnsupplierntc;

/**
 * @author yangwenzhu
 *
 */
public enum RtnSupplierNtcBillState {
  /** 未审核 */
  Initial("初始"),
  /** 进行中 */
  InProgress("进行中"),
  /** 已作废 */
  Aborted("已作废"),
  /** 已完成 */
  Finished("已完成");
  private String caption;

  public String getCaption() {
    return caption;
  }

  private RtnSupplierNtcBillState(String caption) {
    this.caption = caption;
  }
}
