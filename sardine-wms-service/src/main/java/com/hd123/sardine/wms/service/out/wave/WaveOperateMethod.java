/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	wms-wms-core
 * 文件名：	WaveOperateMethod.java
 * 模块说明：	
 * 修改历史：
 * 2015-4-29 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.out.wave;

/**
 * @author zhangsai
 * 
 */
public enum WaveOperateMethod {
  ManualBill("手工单据"),
  
  HandTerminal("手持终端");
  
  private String caption;

  private WaveOperateMethod(String caption) {
    this.caption = caption;
  }

  public String getCaption() {
    return this.caption;
  }
}
