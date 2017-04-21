/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	TaskState.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月12日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.in.order;

/**
 * 订单状态
 * 
 * @author zhangsai
 *
 */
public enum OrderBillState {

  Initial("初始"),

  PreBookReg("已预约"),

  PreChecked("已预检"),

  InProgress("进行中"),

  Finished("已完成"),

  Aborted("已作废");

  private String caption;

  private OrderBillState(String caption) {
    this.caption = caption;
  }

  public String getCaption() {
    return caption;
  }
}
