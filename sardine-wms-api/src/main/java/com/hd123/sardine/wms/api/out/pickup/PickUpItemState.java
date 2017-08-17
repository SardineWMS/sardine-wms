/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-api
 * 文件名：	PickUpItemState.java
 * 模块说明：	
 * 修改历史：
 * 2014-8-5 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.out.pickup;

/**
 * 拣货单明细状态：枚举
 * 
 * @author zhangsai
 * 
 */
public enum PickUpItemState {
  /** 未处理 */
  initial("未处理"),

  /** 进行中 */
  inProgress("进行中"),

  /** 已完成 */
  finished("已完成"),
  
  /** 异常 */
  exception("异常"),

  /** 跳过 */
  skip("跳过"),

  /** 缺货 */
  stockOut("缺货");

  private String caption;

  private PickUpItemState(String caption) {
    this.caption = caption;
  }

  public String getCaption() {
    return caption;
  }
}
