/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-api
 * 文件名：	RplType.java
 * 模块说明：	
 * 修改历史：
 * 2014-8-12 - WUJING - 创建。
 */
package com.hd123.sardine.wms.api.out.rpl;

/**
 * @author WUJING
 * 
 */
public enum RplType {
  WholeContainer("整托"),

  PartContainer("非整托");

  private String caption;

  private RplType(String caption) {
    this.caption = caption;
  }

  public String getCaption() {
    return caption;
  }

}
