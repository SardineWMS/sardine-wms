/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	StockService.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月11日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.stock;

import java.util.List;

import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;

/**
 * 库存服务接口
 * 
 * @author zhangsai
 *
 */
public interface StockService {

  /**
   * 库存入库
   * <p>
   * 正常库存入库<br>
   * 如果{@link Stock#getOnWayStocks()}不为empty时，同时入库在途库存
   * 
   * @param sourceBillType
   *          操作单据类型，not null
   * @param sourceBillNumber
   *          操作单据单号，not null
   * @param willInStocks
   *          将要入库的库存集合，not null
   * @return 入库后库存详情
   * @throws IllegalArgumentException
   * @throws VersionConflictException
   * @throws WMSExceptionk
   */
  List<Stock> shiftIn(String sourceBillType, String sourceBillNumber, List<Stock> willInStocks)
      throws IllegalArgumentException, VersionConflictException, WMSException;

  /**
   * 在途库存入库
   * 
   * @param onWayStocks
   *          在途库存集合，not null
   * @return 入库后库存详情
   * @throws IllegalArgumentException
   * @throws VersionConflictException
   * @throws WMSException
   */
  List<Stock> shiftInOnWayStock(List<OnWayStock> onWayStocks)
      throws IllegalArgumentException, VersionConflictException, WMSException;

  /**
   * 根据指定条件查询库存信息 <br>
   * 数量=可用库存数量
   * 
   * @param stockFilter
   *          库存查询条件，not null
   * @return 库存集合
   * @throws IllegalArgumentException
   */
  List<StockExtendInfo> queryStocks(StockFilter stockFilter) throws IllegalArgumentException;

  /**
   * 正常库存出库，不包含在途库存
   * 
   * @param sourceBillType
   *          操作单据类型，not null
   * @param sourceBillNumber
   *          操作单据单号，not null
   * @param shiftOutRules
   *          出库规则，not null
   * @return 出库的库存详情
   * @throws IllegalArgumentException
   * @throws VersionConflictException
   * @throws WMSException
   */
  List<Stock> shiftOut(String sourceBillType, String sourceBillNumber,
      List<ShiftOutRule> shiftOutRules)
      throws IllegalArgumentException, VersionConflictException, WMSException;

  /**
   * 在途库存出库
   * 
   * @param shiftRules
   *          在途库存出库规则，not null
   * @return 出库的库存详情
   * @throws IllegalArgumentException
   * @throws VersionConflictException
   * @throws WMSException
   */
  List<Stock> shiftOutOnWayStock(List<OnWayStockOutRule> shiftRules)
      throws IllegalArgumentException, VersionConflictException, WMSException;

  /**
   * 查询货位是否存在库存，包括正常库存和在途库存
   * 
   * @param binCode
   *          货位代码，为空则返回false
   * @return 有库存返回true，否则返回false
   */
  boolean binHasStock(String binCode);

  /**
   * 查询容器是否存在库存，包括正常库存和在途库存
   * 
   * @param containerBarcode
   *          容器条码，为空则返回false
   * @return 有库存返回true，否则返回false
   */
  boolean containerHasStock(String containerBarcode);

  List<Stock> query(StockFilter stockFilter);
}
