/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-api
 * 文件名：	PickType.java
 * 模块说明：	
 * 修改历史：
 * 2014-8-5 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.out.pickup;

/**
 * 拣货类型：枚举
 * 
 * @author zhangsai
 * 
 */
public enum PickType {
  /** 整托盘 */
  WholeContainer("整托"),
  
  /** 非整托 */
  PartContainer("非整托");

  private String caption;

  private PickType(String caption) {
    this.caption = caption;
  }

  public String getCaption() {
    return caption;
  }
}
