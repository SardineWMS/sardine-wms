/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	TaskState.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月12日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.task;

/**
 * 指令类型
 * 
 * @author zhangSai
 *
 */
public enum TaskType {

  /** 上架指令 */
  Putaway("上架指令"),

  /** 补货指令 */
  Rpl("补货指令"),

  /** 退仓上架指令 */
  RtnPutaway("退仓上架指令"),

  /** 退货下架指令 */
  RtnShelf("退货下架指令"),

  /** 装车指令 */
  Move("移库指令");

  private String caption;

  private TaskType(String caption) {
    this.caption = caption;
  }

  public String getCaption() {
    return caption;
  }
}
