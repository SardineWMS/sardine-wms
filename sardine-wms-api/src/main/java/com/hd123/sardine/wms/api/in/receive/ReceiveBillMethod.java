/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-api
 * 文件名：	ReceiveBillMethod.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-20 - WUJING - 创建。
 */
package com.hd123.sardine.wms.api.in.receive;

/**
 * @author WUJING
 * 
 */
public enum ReceiveBillMethod {

  ManualBill("手工单据"),

  HandTerminal("手持终端");

  private String caption;

  private ReceiveBillMethod(String caption) {
    this.caption = caption;
  }

  public String getCaption() {
    return caption;
  }
}
