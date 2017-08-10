/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-api
 * 文件名：	StockChangeDirection.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-21 - Gao JingYu - 创建。
 */
package com.hd123.sardine.wms.api.stock;

/**
 * 库存变化方向
 * 
 * @author Gao JingYu
 */
public enum StockChangeDirection {
  shiftIn("入库"), shiftOut("出库");

  private StockChangeDirection(String caption) {
    this.caption = caption;
  }

  private String caption;

  public String getCaption() {
    return caption;
  }
}
