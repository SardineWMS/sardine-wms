/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-api
 * 文件名：	StockService.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-21 - Gao JingYu - 创建。
 */
package com.hd123.sardine.wms.api.stock;

import java.util.List;

import com.hd123.sardine.wms.common.entity.SourceBill;
import com.hd123.sardine.wms.common.exception.WMSException;

/**
 * 库存管理 | 接口
 * 
 * @author Gao JingYu
 */
public interface StockService {

  /**
   * 转入库存。
   * 
   * @param sourceBill
   *          操作单据,not null。
   * @param stocks
   *          库存记录,not null。
   * @return 库存变化记录。
   * @throws IllegalArgumentException
   * @throws DBOperServiceException
   * @throws WMSException
   */
  void shiftIn(SourceBill sourceBill, List<StockShiftIn> stocks) throws WMSException;

  /**
   * 转出库存。
   * 
   * @param sourceBill
   *          操作单据,not null。
   * @param rules
   *          库存变化规则，not null。
   * @return 库存变化记录。
   * @throws StockException
   *           当库存不够或不存在时抛出。
   * @throws IllegalArgumentException
   * @throws DBOperServiceException
   */
  List<StockChangement> shiftOut(SourceBill sourceBill, List<StockShiftRule> rules)
      throws WMSException;

  /**
   * 库存转移。
   * 
   * @param sourceBill
   *          操作单据，not null。
   * @param rules
   *          库存变化规则。
   * @param target
   *          转移库存目标。
   * @return 库存变化记录。
   * @throws WMSException
   * @throws IllegalArgumentException
   * @throws DBOperServiceException
   */
  List<StockChangement> shift(SourceBill sourceBill, List<StockShiftRule> rules,
      StockShiftTarget target) throws WMSException;

  /**
   * 根据库存规则，改变库存状态。
   * <p>
   * 忽略规则中状态。
   * 
   * @param sourceBill
   *          操作单据,not null。
   * @param rules
   *          库存变化规则，not null。
   * @param fromState
   *          来源库存状态， not null。
   * @param toState
   *          目标库存状态， not null。
   * @return 转出库存变化记录。
   * @throws WMSException
   *           当库存不够或不存在时抛出。
   * @throws IllegalArgumentException
   * @throws DBOperServiceException
   */
  List<StockChangement> changeState(SourceBill sourceBill, List<StockShiftRule> rules,
      StockState fromState, StockState toState) throws WMSException;

  /**
   * 查询库存。
   * 
   * @param filter
   *          查询条件，not null
   * @return 符合条件的库存记录。
   * @throws IllegalArgumentException
   * @throws DBOperServiceException
   */
  List<Stock> query(StockFilter filter);

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
   * 查询库存以及库存对应的货位信息
   * <p>
   * 该方法关联表众多，尽量不要使用该方法
   * 
   * @param filter
   * @return
   */
  List<StockMajorInfo> queryStockMajorInfo(StockMajorFilter filter);
}
