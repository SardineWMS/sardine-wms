/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ArticlePutawayBin.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月6日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.article;

/**
 * @author zhangsai
 *
 */
public enum ArticlePutawayBin {
  StorageBin("存储位"), PickUpBin("拣货位"), PreChoosePickUp("优先考虑拣货位");

  private String caption;

  private ArticlePutawayBin(String caption) {
    this.caption = caption;
  }

  public String getCaption() {
    return caption;
  }
}
