/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	BinState.java
 * 模块说明：	
 * 修改历史：
 * 2017-1-17 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.bin;

/**
 * 货位状态
 * 
 * @author zhangsai
 * 
 */

public enum BinState {
  /** 空闲 */
  free("空闲"),
  /** 使用中 */
  using("使用中"),
  /** 上架异常锁定 */
  errorLock("上架异常锁定"),
  /** 平移锁定 */
  moveLock("平移锁定"),
  /** 集货锁定 */
  collectionLock("集货锁定"),
  /** 装车锁定 */
  shipLock("装车锁定"),
  /** 封仓锁定 */
  closeLock("封仓锁定"),
  /** 上架锁定 */
  putawayLock("上架锁定");

  private String caption;

  private BinState(String caption) {
    this.caption = caption;
  }

  public String getCaption() {
    return caption;
  }
}
