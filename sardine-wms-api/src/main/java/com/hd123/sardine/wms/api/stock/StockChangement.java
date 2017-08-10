/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-api
 * 文件名：	StockChangement.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-21 - Gao JingYu - 创建。
 */
package com.hd123.sardine.wms.api.stock;

import java.io.Serializable;

import com.hd123.rumba.commons.lang.Assert;

/**
 * 库存变化记录。
 * 
 * @author Gao JingYu
 */
public class StockChangement implements Serializable {
  private static final long serialVersionUID = 4778437292796646670L;

  private String stockUuid;
  private StockChangeDirection direction;
  private String sourceLineUuid;
  private int sourceLineNumber;
  private StockComponent stockComponent;

  /** 库存UUID */
  public String getStockUuid() {
    return stockUuid;
  }

  public void setStockUuid(String stockUuid) {
    this.stockUuid = stockUuid;
  }

  /** 库存变化方向 */
  public StockChangeDirection getDirection() {
    return direction;
  }

  public void setDirection(StockChangeDirection direction) {
    this.direction = direction;
  }

  /** 影响库存变化来源单据明细 */
  public String getSourceLineUuid() {
    return sourceLineUuid;
  }

  public void setSourceLineUuid(String sourceLineUuid) {
    this.sourceLineUuid = sourceLineUuid;
  }

  /** 库存部件 */
  public StockComponent getStockComponent() {
    return stockComponent;
  }

  /**
   * 库存部件
   * 
   * @param stockComponent
   *          not null。
   * @throws IllegalArgumentException
   */
  public void setStockComponent(StockComponent stockComponent) {
    Assert.assertArgumentNotNull(stockComponent, "stockComponent");
    this.stockComponent = stockComponent;
  }

  /** 影响库存变化来源单据明细行号 */
  public int getSourceLineNumber() {
    return sourceLineNumber;
  }

  public void setSourceLineNumber(int sourceLineNumber) {
    this.sourceLineNumber = sourceLineNumber;
  }

}
