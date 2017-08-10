/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-core
 * 文件名：	RplBinUsage.java
 * 模块说明：	
 * 修改历史：
 * 2014-8-20 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.out.wave;

/**
 * 波次货位用途。
 * 
 * @author zhangsai
 */
public enum WaveBinUsage {

  /** 存储位 */
  StorageBin("存储位"),

  /** 动态拣货位 */
  DynamicPickBin("动态拣货位"),

  /** 固定拣货位 */
  FixBin("固定拣货位");

  private String caption;

  private WaveBinUsage(String caption) {
    this.caption = caption;
  }

  public String getCaption() {
    return caption;
  }
}
