/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ReceiveBillState.java
 * 模块说明：	
 * 修改历史：
 * 2017-3-20 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.in.receive;

/**
 * @author zhangsai
 * 
 */
public enum ReceiveBillState {

  Initial("未审核"), Audited("已审核"), InProgress("进行中");

  private String caption;

  private ReceiveBillState(String caption) {
    this.caption = caption;
  }

  public String getCaption() {
    return caption;
  }
}
