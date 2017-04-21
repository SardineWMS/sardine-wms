/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	StockDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月11日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.stock.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.stock.OnWayStock;
import com.hd123.sardine.wms.api.stock.Stock;
import com.hd123.sardine.wms.api.stock.StockExtendInfo;
import com.hd123.sardine.wms.api.stock.StockFilter;
import com.hd123.sardine.wms.api.stock.StockLog;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.dao.stock.StockDao;

/**
 * 库存DAO 实现
 * 
 * @author zhangsai
 *
 */
public class StockDaoImpl extends SqlSessionDaoSupport implements StockDao {
  public static final String MAPPER_GETBYPRIMARYKEY = "getByPrimaryKey";
  public static final String MAPPER_GET = "get";
  public static final String MAPPER_GETBYTASKNO = "getByTaskNo";
  public static final String MAPPER_INSERT = "insert";
  public static final String MAPPER_UPDATE = "update";
  public static final String MAPPER_REMOVE = "remove";
  public static final String MAPPER_INSERTONWAYSTOCK = "insertOnWayStock";
  public static final String MAPPER_UPDATEONWAYSTOCK = "updateOnWayStock";
  public static final String MAPPER_REMOVEONWAYSTOCK = "removeOnWayStock";
  public static final String MAPPER_QUERYSTOCKEXTENDINFO = "queryStockExtendInfo";
  public static final String MAPPER_QUERY = "query";
  public static final String MAPPER_INSERTLOG = "insertLog";
  public static final String MAPPER_BINHASSTOCK = "binHasStock";
  public static final String MAPPER_CONTAINERHASSTOCK = "containerHasStock";

  public String generateStatement(String mapperId) {
    return this.getClass().getName() + "." + mapperId;
  }

  @Override
  public Stock getByPrimaryKey(String binCode, String containerBarcode, String stockBatch,
      String articleUuid) {
    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("binCode", binCode);
    map.put("containerBarcode", containerBarcode);
    map.put("stockBatch", stockBatch);
    map.put("articleUuid", articleUuid);

    List<Stock> stocks = getSqlSession().selectList(generateStatement(MAPPER_GETBYPRIMARYKEY), map);
    if (CollectionUtils.isEmpty(stocks) == false)
      return stocks.get(0);
    return null;
  }

  @Override
  public Stock get(String uuid) {
    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("uuid", uuid);

    List<Stock> stocks = getSqlSession().selectList(generateStatement(MAPPER_GET), map);
    if (CollectionUtils.isEmpty(stocks) == false)
      return stocks.get(0);
    return null;
  }

  @Override
  public OnWayStock getByTaskNo(String stockUuid, String taskNo) {
    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("taskNo", taskNo);
    map.put("stockUuid", stockUuid);

    List<OnWayStock> stocks = getSqlSession().selectList(generateStatement(MAPPER_GETBYTASKNO),
        map);
    if (CollectionUtils.isEmpty(stocks) == false)
      return stocks.get(0);
    return null;
  }

  @Override
  public void insert(Stock stock) {
    Assert.assertArgumentNotNull(stock, "stock");

    getSqlSession().insert(generateStatement(MAPPER_INSERT), stock);
  }

  @Override
  public void update(Stock stock) {
    Assert.assertArgumentNotNull(stock, "stock");

    int i = getSqlSession().update(generateStatement(MAPPER_UPDATE), stock);
    PersistenceUtils.optimisticVerify(i);
  }

  @Override
  public void remove(String uuid, long version) {
    Assert.assertArgumentNotNull(uuid, "uuid");

    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("uuid", uuid);
    map.put("version", version);
    int i = getSqlSession().delete(generateStatement(MAPPER_REMOVE), map);
    PersistenceUtils.optimisticVerify(i);
  }

  @Override
  public void insertOnWayStock(OnWayStock onWayStock) {
    Assert.assertArgumentNotNull(onWayStock, "onWayStock");

    getSqlSession().insert(generateStatement(MAPPER_INSERTONWAYSTOCK), onWayStock);
  }

  @Override
  public void updateOnWayStock(OnWayStock onWayStock) {
    Assert.assertArgumentNotNull(onWayStock, "onWayStock");

    int i = getSqlSession().update(generateStatement(MAPPER_UPDATEONWAYSTOCK), onWayStock);
    PersistenceUtils.optimisticVerify(i);
  }

  @Override
  public void removeOnWayStock(String uuid) {
    Assert.assertArgumentNotNull(uuid, "uuid");

    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("uuid", uuid);
    int i = getSqlSession().delete(generateStatement(MAPPER_REMOVEONWAYSTOCK), map);
    PersistenceUtils.optimisticVerify(i);
  }

  @Override
  public List<StockExtendInfo> queryStockExtendInfo(StockFilter stockFilter) {
    Assert.assertArgumentNotNull(stockFilter, "stockFilter");

    List<StockExtendInfo> result = getSqlSession().selectList(MAPPER_QUERYSTOCKEXTENDINFO,
        stockFilter);
    return result;
  }

  @Override
  public List<Stock> query(StockFilter stockFilter) {
    Assert.assertArgumentNotNull(stockFilter, "stockFilter");

    List<Stock> result = getSqlSession().selectList(MAPPER_QUERY, stockFilter);
    return result;
  }

  @Override
  public void insertLog(StockLog log) {
    Assert.assertArgumentNotNull(log, "log");

    getSqlSession().insert(generateStatement(MAPPER_INSERTLOG), log);
  }

  @Override
  public boolean binHasStock(String binCode) {
    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("binCode", binCode);

    List<String> result = getSqlSession().selectList(generateStatement(MAPPER_BINHASSTOCK), map);
    if (CollectionUtils.isEmpty(result) == false)
      return true;
    return false;
  }

  @Override
  public boolean containerHasStock(String containerBarcode) {
    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("containerBarcode", containerBarcode);

    List<String> result = getSqlSession().selectList(generateStatement(MAPPER_CONTAINERHASSTOCK),
        map);
    if (CollectionUtils.isEmpty(result) == false)
      return true;
    return false;
  }
}
