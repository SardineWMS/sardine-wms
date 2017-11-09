/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ReasonType.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月21日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.config.reasonconfig;

/**
 * @author fanqingqing
 *
 */
public enum ReasonType {

  DECINC("损溢原因"),

  MOVE("移库原因"),
  /** 退仓原因 */
  RTNNTC("退仓原因");

  private String caption;

  private ReasonType(String caption) {
    this.caption = caption;
  }

  public String getCaption() {
    return caption;
  }
}
