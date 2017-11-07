/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	OrderBillService.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月13日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.in.order;

import java.util.Date;
import java.util.List;

import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * 订单服务：接口
 * 
 * @author zhangsai
 *
 */
public interface OrderBillService {
  /** 查询条件 */
  public static final String QUERY_BILLNUMBER_LIKE = "billNumber";
  public static final String QUERY_WRHCODE_EQUALS = "wrhCode";
  public static final String QUERY_STATE_EQUALS = "state";
  public static final String QUERY_SUPPLIERCODE_EQUALS = "supplierCode";
  public static final String QUERY_ARTICLE_CONTAINS = "articleCode";
  public static final String QUERY_SOURCEBILL_LIKE = "sourceBillNumber";

  /** 排序字段 */
  public static final String ORDER_CODE_BILLNUMBER = "billNumber";

  /**
   * 新增订单
   * 
   * @param orderBill
   *          订单，not null
   * @return 单号
   * @throws IllegalArgumentException
   * @throws WMSException
   */
  String insert(OrderBill orderBill) throws IllegalArgumentException, WMSException;

  /**
   * 编辑订单
   * 
   * @param orderBill
   *          订单，not null
   * @throws IllegalArgumentException
   * @throws VersionConflictException
   * @throws WMSException
   */
  void update(OrderBill orderBill)
      throws IllegalArgumentException, VersionConflictException, WMSException;

  /**
   * 删除订单
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
   * 根据UUID获取订单
   * 
   * @param uuid
   * @return 订单
   */
  OrderBill get(String uuid);

  /**
   * 根据单号获取订单
   * 
   * @param billNumber
   *          单号
   * @return 订单
   */
  OrderBill getByBillNumber(String billNumber);

  /**
   * 分页查询订单
   * 
   * @param definition
   *          分页查询条件，not null
   * @return 分页结果数据
   */
  PageQueryResult<OrderBill> query(PageQueryDefinition definition);

  /**
   * 订单预约
   * 
   * @param uuid
   *          UUID，not null
   * @param version
   *          版本号
   * @param bookDate
   *          预约日期
   * @throws IllegalArgumentException
   * @throws VersionConflictException
   * @throws WMSException
   */
  void preBookReg(String uuid, long version, Date bookDate)
      throws IllegalArgumentException, VersionConflictException, WMSException;

  /**
   * 预检
   * 
   * @param uuid
   *          UUID，not null
   * @param version
   *          版本号
   * @throws IllegalArgumentException
   * @throws VersionConflictException
   * @throws WMSException
   */
  void preCheck(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException;

  /**
   * 完成收货单
   * 
   * @param uuid
   *          UUID，not null
   * @param version
   * @throws IllegalArgumentException
   * @throws VersionConflictException
   * @throws WMSException
   */
  void finish(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException;

  /**
   * 作废收货单
   * 
   * @param uuid
   *          UUID，not null
   * @param version
   * @throws IllegalArgumentException
   * @throws VersionConflictException
   * @throws WMSException
   */
  void abort(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException;

  /**
   * 收货回写订单收货数量和状态
   * 
   * @param billNumber
   *          订单单号，not null
   * @param receiveInfos
   *          收货信息，not null
   * @throws IllegalArgumentException
   * @throws VersionConflictException
   * @throws WMSException
   */
  void receive(String billNumber, List<OrderReceiveInfo> receiveInfos)
      throws IllegalArgumentException, VersionConflictException, WMSException;

  /**
   * 分页查询可收货订单
   * 
   * @param definition
   *          查询条件，not null
   * @return 查询结果集
   */
  PageQueryResult<OrderBill> queryCanReceiveOrderBills(PageQueryDefinition definition);
}
