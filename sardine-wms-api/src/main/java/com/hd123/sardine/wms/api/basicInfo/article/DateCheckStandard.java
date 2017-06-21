/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	DateCheckStandard.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-4 - WUJING - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.article;

/**
 * @author zhangsai
 * 
 */
public enum DateCheckStandard {
  produceDate("按生产日期"), expireDate("按到效日期"), none("不需要管理保质期");

  private String caption;

  private DateCheckStandard(String caption) {
    this.caption = caption;
  }

  public String getCaption() {
    return caption;
  }

}
