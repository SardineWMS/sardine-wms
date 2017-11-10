/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	AcceptanceBillService.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月5日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.out.acceptance;

import java.math.BigDecimal;

import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * @author fanqingqing
 *
 */
public interface AcceptanceBillService {
  /** 查询条件 */
  public static final String QUERY_BILLNUMBER_LIKE = "billNumber";
  public static final String QUERY_WRHCODE_EQUALS = "wrhCode";
  public static final String QUERY_STATE_EQUALS = "state";
  public static final String QUERY_CUSTOMERCODE_LIKE = "customerCode";
  public static final String QUERY_DELIVERYSYSTEM_EQUALS = "deliverySystem";
  public static final String QUERY_DELIVERYTYPE_EQUALS = "deliveryType";
  public static final String QUERY_BINCODE_LIKE = "binCode";
  public static final String QUERY_CONTAINERBARCODE_LIKE = "containerBarcode";
  /** 排序字段 */
  public static final String ORDER_CODE_BILLNUMBER = "billNumber";
  public static final String ORDER_CODE_STATE = "state";
  public static final String ORDER_CODE_WRHCODE = "wrhCode";

  /**
   * 根据UUID获取领用单
   * 
   * @param uuid
   * @return 领用单
   */
  AcceptanceBill get(String uuid);

  /**
   * 根据单号获取领用单
   * 
   * @param billNumber
   *          单号
   * @return 领用单
   */
  AcceptanceBill getByBillNumber(String billNumber);

  /**
   * 分页查询领用单
   * 
   * @param definition
   *          分页查询条件，not null
   * @return 分页结果数据
   */
  PageQueryResult<AcceptanceBill> query(PageQueryDefinition definition);

  /**
   * 新增领用单
   * 
   * @param orderBill
   *          领用单，not null
   * @return 单号
   * @throws IllegalArgumentException
   * @throws WMSException
   */
  String insert(AcceptanceBill acceptanceBill) throws IllegalArgumentException, WMSException;

  /**
   * 编辑领用单
   * 
   * @param orderBill
   *          领用单，not null
   * @throws IllegalArgumentException
   * @throws VersionConflictException
   * @throws WMSException
   */
  void update(AcceptanceBill acceptanceBill)
      throws IllegalArgumentException, VersionConflictException, WMSException;

  /**
   * 删除领用单
   * 
   * @param uuid
   *          not nul
   * @param version
   *          版本号
   * @throws IllegalArgumentException
   * @throws VersionConflictException
   * @throws WMSException
   */
  void remove(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException;

  /**
   * 批准领用单
   * 
   * @param uuid
   *          领用单uuid，not null。
   * @param version
   *          领用单版本号，not null。
   * @throws IllegalArgumentException
   *           参数异常时抛出
   * @throws VersionConflictException
   *           版本冲突时抛出
   * @throws WMSException
   */
  void approve(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException;

  /**
   * 配货
   * 
   * @param uuid
   *          领用单UUID，not null。
   * @param version
   *          领用单版本号，not null。
   * @throws IllegalArgumentException
   *           参数异常时抛出
   * @throws VersionConflictException
   *           版本冲突时抛出
   * @throws WMSException
   */
  void beginAlc(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException;

  /**
   * 审核
   * 
   * @param uuid
   *          领用单UUID，not null。
   * @param version
   *          领用单版本号，not null。
   * @throws IllegalArgumentException
   *           参数异常时抛出
   * @throws VersionConflictException
   *           版本冲突时抛出
   * @throws WMSException
   */
  void finish(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException;

  /**
   * 作废
   * 
   * @param uuid
   *          领用单UUID，not null。
   * @param version
   *          领用单版本号，not null。
   * @throws IllegalArgumentException
   *           参数异常时抛出
   * @throws VersionConflictException
   *           版本冲突时抛出
   * @throws WMSException
   */
  void abort(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException;

  /**
   * 拣货回写要货单明细
   * 
   * @param itemUuid
   *          领用单明细UUID， not null
   * @param qty
   *          明细拣货数量， not null
   * @throws IllegalArgumentException
   * @throws WMSException
   */
  void pickUp(String itemUuid, BigDecimal qty) throws IllegalArgumentException, WMSException;
}
