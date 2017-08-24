/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-core
 * 文件名：	StockDao.java
 * 模块说明：	
 * 修改历史：
 * 2014-4-10 - WUJING - 创建。
 */
package com.hd123.sardine.wms.dao.stock;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.hd123.sardine.wms.api.stock.Stock;
import com.hd123.sardine.wms.api.stock.StockFilter;
import com.hd123.sardine.wms.api.stock.StockMajorFilter;
import com.hd123.sardine.wms.api.stock.StockMajorInfo;

/**
 * @author WUJING
 * 
 */
public interface StockDao {

  /**
   * 根据查询条件查询库存
   * 
   * @param filter
   * @return 库存列表
   */
  List<Stock> queryStocks(StockFilter filter);

  /**
   * 查询库存
   * 
   * @param filter
   *          查询条件，not null
   * @return 查询结果
   * @throws IllegalArgumentException
   * @throws DBOperServiceException
   */
  List<StockMajorInfo> queryMajorInfo(StockMajorFilter filter);

  /**
   * 新增库存
   * 
   * @param stock
   */
  void insertStock(Stock stock);

  /**
   * 编辑库存，只针对库存数量修改
   * 
   * @param uuid
   *          库存UUID
   * @param version
   *          库存版本号
   * @param qty
   *          要修改的数量，可正可负
   * @param modifyDate
   *          修改时间
   */
  void updateStock(String uuid, long version, BigDecimal qty, Date modifyDate);

  /**
   * 删除库存
   * 
   * @param uuid
   *          库存UUID
   * @param version
   *          库存版本号
   */
  void removeStock(String uuid, long version);

  /**
   * 插入库存操作日志
   * 
   * @param log
   */
  void insertStockLog(StockOperLog log);
}
