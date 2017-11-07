/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ReceiveBillService.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-20 - WUJING - 创建。
 */
package com.hd123.sardine.wms.api.in.receive;

import com.hd123.sardine.wms.common.exception.EntityNotFoundException;
import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * @author WUJING
 * 
 */
public interface ReceiveBillService {
  /** 排序字段代码 billno */
  public static final String FIELD_ORDER_BILLNO = "billNumber";
  public static final String FIELD_ORDER_ORDERBILL = "orderBill";
  public static final String FIELD_ORDER_SUPPLIERCODE = "supplierCode";
  public static final String FIELD_ORDER_CASEQTYSTR = "caseQtyStr";

  public static final String QUERY_BILLNO_FIELD = "billNumber";
  public static final String QUERY_STATE_FIELD = "state";
  public static final String QUERY_SUPPLIER_FIELD = "supplier";
  public static final String QUERY_WRH_FIELD = "wrh";
  public static final String QUERY_ORDERBILL_FIELD = "orderBill";
  public static final String QUERY_ARTICLE_CODE_FIELD = "articleCode";
  public static final String QUERY_RECEIVER_CODE_FIELD = "receiverCode";
  public static final String QUERY_CONTAINERBARCODE_FIELD = "containerBarcode";

  /**
   * 保存收货单
   * 
   * @param bill
   *          收货单，not null
   * @return 返回收货单uuid
   * @throws IllegalArgumentException
   *           当bill为空时抛出异常
   * @throws WMSException
   */
  String insert(ReceiveBill bill) throws WMSException;

  void update(ReceiveBill bill) throws WMSException;

  /**
   * 查询收货单，查询结果包含明细行
   * 
   * @param uuid
   *          收货单uuid
   * @return 返回查询到的收货单，存在则返回，不存在返回null
   */
  ReceiveBill get(String uuid);

  /**
   * 查询收货单，查询结果包含明细行
   * 
   * @param billno
   *          收货单单号
   * @return 返回查询到的收货单，存在则返回，不存在返回null
   */
  ReceiveBill getByBillNo(String billno);

  /**
   * 分页查询收货单,结果不包含明细行
   * 
   * @param definition
   *          查询filter not null
   * @return 返回分页数据，参看{@link PageQueryResult}
   * @throws IllegalArgumentException
   *           filter为空时抛出该异常
   */
  PageQueryResult<ReceiveBill> query(PageQueryDefinition definition);

  /**
   * 删除收货单 物理删除
   * 
   * @param uuid
   *          收货单uuid not null
   * @param version
   *          收货单版本号
   * @throws IllegalArgumentException
   *           uuid为空、operCtx为空时抛出该异常
   * @throws EntityNotFoundException
   *           要删除的收货单不存在时抛出该异常
   * @throws VersionConflictException
   *           版本冲突时抛出
   * @throws WMSException
   *           收货单状态不能删除时抛出异常
   */
  void remove(String uuid, long version) throws WMSException;

  /**
   * 审核收货单
   * 
   * @param uuid
   *          收货单uuid not null
   * @param version
   *          收货单版本号
   * @throws IllegalArgumentException
   *           uuid为空、operCtx为空时抛出该异常
   * @throws EntityNotFoundException
   *           要审核的收货单不存在时抛出该异常
   * @throws VersionConflictException
   *           版本冲突时抛出
   * @throws WMSException
   *           收货单状态不能审核时抛出异常
   */
  void audit(String uuid, long version) throws WMSException;
}
