/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-core
 * 文件名：	StockDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2014-4-10 - WUJING - 创建。
 */
package com.hd123.sardine.wms.dao.stock.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.stock.Stock;
import com.hd123.sardine.wms.api.stock.StockExtendInfo;
import com.hd123.sardine.wms.api.stock.StockFilter;
import com.hd123.sardine.wms.api.stock.StockMajorFilter;
import com.hd123.sardine.wms.api.stock.StockMajorInfo;
import com.hd123.sardine.wms.common.dao.NameSpaceSupport;
import com.hd123.sardine.wms.dao.stock.StockDao;
import com.hd123.sardine.wms.dao.stock.StockOperLog;

/**
 * @author WUJING
 * 
 */
public class StockDaoImpl extends NameSpaceSupport implements StockDao {
  private static final String QUERYSTOCKS = "queryStocks";
  private static final String QUERYSTOCKEXTENDINFO = "queryStockExtendInfo";
  private static final String QUERYMAJORINFO = "queryMajorInfo";
  private static final String INSERTSTOCK = "insertStock";
  private static final String UPDATESTOCK = "updateStock";
  private static final String REMOVESTOCK = "removeStock";
  private static final String INSERTSTOCKLOG = "insertStockLog";

  @Override
  public List<Stock> queryStocks(StockFilter filter) {
    Assert.assertArgumentNotNull(filter, "filter");

    return selectList(QUERYSTOCKS, filter);
  }

  @Override
  public List<StockExtendInfo> queryStockExtendInfo(StockFilter filter) {
    Assert.assertArgumentNotNull(filter, "filter");

    return selectList(QUERYSTOCKEXTENDINFO, filter);
  }

  @Override
  public List<StockMajorInfo> queryMajorInfo(StockMajorFilter filter) {
    Assert.assertArgumentNotNull(filter, "filter");

    return selectList(QUERYMAJORINFO, filter);
  }

  @Override
  public void insertStock(Stock stock) {
    Assert.assertArgumentNotNull(stock, "stock");

    insert(INSERTSTOCK, stock);
  }

  @Override
  public void updateStock(String uuid, long version, BigDecimal qty, Date modifyDate) {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(qty, "qty");
    Assert.assertArgumentNotNull(modifyDate, "modifyDate");

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("uuid", uuid);
    map.put("version", version);
    map.put("qty", qty);
    map.put("modifyDate", modifyDate);

    update(UPDATESTOCK, map);
  }

  @Override
  public void removeStock(String uuid, long version) {
    Assert.assertArgumentNotNull(uuid, "uuid");

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("uuid", uuid);
    map.put("version", version);

    delete(REMOVESTOCK, map);
  }

  @Override
  public void insertStockLog(StockOperLog log) {
    Assert.assertArgumentNotNull(log, "log");

    insert(INSERTSTOCKLOG, log);
  }
}
