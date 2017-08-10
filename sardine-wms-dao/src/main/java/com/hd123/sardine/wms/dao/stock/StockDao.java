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

import java.util.List;

import com.hd123.sardine.wms.api.stock.Stock;
import com.hd123.sardine.wms.api.stock.StockChangement;
import com.hd123.sardine.wms.api.stock.StockExtendInfo;
import com.hd123.sardine.wms.api.stock.StockFilter;
import com.hd123.sardine.wms.api.stock.StockMajorFilter;
import com.hd123.sardine.wms.api.stock.StockMajorInfo;
import com.hd123.sardine.wms.api.stock.StockState;

/**
 * @author WUJING
 * 
 */
public interface StockDao {
  /**
   * 执行清除存储过程。
   * 
   * @param workId
   */
  void executeClearProcedure(String workId);

  int executeShiftIn(String workId);

  int executeShiftOut(String workId);

  int executeShift(String workId, String binCode, String containerBarcode, StockState state);

  int executeChange(String workId, String targetState);

  void saveStockShiftIn(PStockShiftIn stockShiftIn);

  void saveStockShiftRule(PStockShiftRule rule);

  /**
   * 根据查询条件查询库存
   * 
   * @param filter
   * @return 库存列表
   */
  List<Stock> queryStocks(StockFilter filter);

  /**
   * 查询出库信息
   * 
   * @param workId
   * @return 库存变化记录列表
   */
  List<StockChangement> queryStocksChangement(String workId);

  List<StockShiftMessage> getStockMessage(String workId);

  /**
   * 保存操作单据
   * 
   * @param stockCmd
   *          操作单据信息
   */
  void saveStockCmd(StockCmd stockCmd);

  /**
   * 查询库存详情，包含商品UCN、供应商UCN
   * 
   * @param filter
   *          查询条件，not null
   * @return 返回结果集
   * @throws IllegalArgumentException
   * @throws DBOperServiceException
   */
  List<StockExtendInfo> queryStockExtendInfo(StockFilter filter);

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
}
