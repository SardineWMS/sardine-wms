/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	StockDao.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月11日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.stock;

import java.util.List;

import com.hd123.sardine.wms.api.stock.OnWayStock;
import com.hd123.sardine.wms.api.stock.Stock;
import com.hd123.sardine.wms.api.stock.StockExtendInfo;
import com.hd123.sardine.wms.api.stock.StockFilter;
import com.hd123.sardine.wms.api.stock.StockLog;

/**
 * 库粗 DAO
 * 
 * @author zhangsai
 *
 */
public interface StockDao {

  Stock getByPrimaryKey(String binCode, String containerBarcode, String stockBatch,
      String articleUuid);

  Stock get(String uuid);

  OnWayStock getByTaskNo(String stockUuid, String taskNo);

  void insert(Stock stock);

  void update(Stock stock);

  void remove(String uuid, long version);

  void insertOnWayStock(OnWayStock onWayStock);

  void updateOnWayStock(OnWayStock onWayStock);

  void removeOnWayStock(String uuid);

  List<StockExtendInfo> queryStockExtendInfo(StockFilter stockFilter);

  List<Stock> query(StockFilter stockFilter);

  void insertLog(StockLog log);

  boolean binHasStock(String binCode);

  boolean containerHasStock(String containerBarcode);
}
