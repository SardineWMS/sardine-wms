/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-api
 * 文件名：	Stock.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-21 - Gao JingYu - 创建。
 */
package com.hd123.sardine.wms.api.stock;

import java.util.Calendar;
import java.util.Date;

import com.hd123.rumba.commons.time.DateUtil;
import com.hd123.sardine.wms.common.entity.Entity;

/**
 * 库存
 * 
 * @author Gao JingYu
 */
public class Stock extends Entity {
  private static final long serialVersionUID = -8249258273409513364L;

  /** 虚拟库存信息 */
  public static final String VISUAL_STOCKINFO = "-";
  /** 虚拟供应商 */
  public static final String VISUAL_STOCKSUPPLIERINFO = "8888";
  /** 虚拟日期 */
  public static final Date VISUAL_STOCKDATE = DateUtil.newDate(8888, Calendar.DECEMBER, 31);
  /** 虚拟最大日期 */
  public static final String VISUAL_MAXDATE = "8888-12-31";

  private StockComponent stockComponent;
  private Date modified;

  /** 库存 */
  public StockComponent getStockComponent() {
    return stockComponent;
  }

  /**
   * 设置库存。
   * 
   * @param stockComponent
   *          not null。
   * @throws IllegalArgumentException
   */
  public void setStockComponent(StockComponent stockComponent) {
    this.stockComponent = stockComponent;
  }

  /** 修改时间，设计为数据库发生时间。 */
  public Date getModified() {
    return modified;
  }

  /**
   * 设置修改时间。
   * 
   * @param modified
   */
  public void setModified(Date modified) {
    this.modified = modified;
  }
}
