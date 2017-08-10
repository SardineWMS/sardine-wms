/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-api
 * 文件名：	StockState.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-21 - Gao JingYu - 创建。
 */
package com.hd123.sardine.wms.api.stock;

/**
 * 库存状态。
 * 
 * @author zhangsai
 */
public enum StockState {
  normal("正常"), //
  locked("锁定"), //
  forMoveIn("待移库进"), //
  forMoveOut("待移库出"), //
  forPick("待捡货");

  private StockState(String caption) {
    assert caption != null;
    this.caption = caption;
  }

  private String caption;

  public String getCaption() {
    return caption;
  }
}
