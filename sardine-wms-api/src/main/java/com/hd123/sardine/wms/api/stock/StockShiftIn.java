/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-api
 * 文件名：	StockShiftIn.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-21 - Gao JingYu - 创建。
 */
package com.hd123.sardine.wms.api.stock;

import java.io.Serializable;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.validator.Validator;

/**
 * 入库指令。
 * 
 * @author Gao JingYu
 * 
 */
public class StockShiftIn implements Serializable, Validator {
  private static final long serialVersionUID = -7845971703471779227L;

  /** UUID最大长度限制 */
  public static final int LENGTH_UUID = 32;

  private String sourceLineUuid;
  private int sourceLineNumber;
  private StockComponent stockComponent;

  /** 来源明细行UUID */
  public String getSourceLineUuid() {
    return sourceLineUuid;
  }

  /**
   * 设置来源行UUID
   * 
   * @param sourceLineUuid
   *          not null。
   * @throws IllegalArgumentException
   */
  public void setSourceLineUuid(String sourceLineUuid) {
    Assert.assertArgumentNotNull(sourceLineUuid, "sourceLineUuid");
    Assert.assertStringNotTooLong(sourceLineUuid, LENGTH_UUID, "sourceLineUuid");
    this.sourceLineUuid = sourceLineUuid;
  }

  /** 来源行行号 */
  public int getSourceLineNumber() {
    return sourceLineNumber;
  }

  public void setSourceLineNumber(int sourceLineNumber) {
    this.sourceLineNumber = sourceLineNumber;
  }

  /** 库存部件 */
  public StockComponent getStockComponent() {
    return stockComponent;
  }

  /**
   * 设置库存部件。
   * 
   * @param stockComponent
   *          not null。
   * @throws IllegalArgumentException
   */
  public void setStockComponent(StockComponent stockComponent) {
    Assert.assertArgumentNotNull(stockComponent, "stockComponent");
    this.stockComponent = stockComponent;
  }

  @Override
  public void validate() {
    Assert.assertArgumentNotNull(stockComponent, "stockcomponent");
    stockComponent.validate();
  }
}
