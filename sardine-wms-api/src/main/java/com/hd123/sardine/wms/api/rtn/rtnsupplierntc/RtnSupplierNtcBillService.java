/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	RtnSupplierNtcBillService.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月15日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.rtn.rtnsupplierntc;

import java.math.BigDecimal;

import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * @author yangwenzhu
 *
 */
public interface RtnSupplierNtcBillService {

  public static final String QUERY_BILLNUMBER_LIKE = "billNumberLike";
  public static final String QUERY_STATE_EQUALS = "stateEquals";
  public static final String QUERY_SUPPLIERCODE_LIKE = "supplierCodeLike";
  public static final String QUERY_SUPPLIERNAME_LIKE = "supplierNameLike";
  public static final String QUERY_SOURCEBILLNUMBER_LIKE = "sourceBillNumberLike";
  public static final String QUERY_WRH_EQUALS = "wrhEquals";
  public static final String QUERY_RTNDATE_LESSTHAN = "rtnDateLessThan";
  public static final String QUERY_RTNDATE_MORETHAN = "rtnDateMoreThan";
  public static final String QUERY_ARTICLENAME_CONTAIN = "articleNameContain";
  public static final String QUERY_ARTICLECODE_CONTAIN = "articleCodeContain";

  /**
   * 分页查询供应商退货通知单
   * 
   * @param definition
   *          分页查询条件,not null
   * @return 分页结果集
   */
  PageQueryResult<RtnSupplierNtcBill> query(PageQueryDefinition definition);

  /**
   * 根据UUID获取供应商退货通知单
   * 
   * @param uuid
   *          uuid,if null return null
   * @return 退货通知单实体
   */
  RtnSupplierNtcBill get(String uuid);

  /**
   * 新建供应商退货通知单
   * 
   * @param bill
   *          退货通知单，not null
   * @return 退货通知单UUID
   * @throws WMSException
   */
  String saveNew(RtnSupplierNtcBill bill) throws WMSException;

  /**
   * 修改供应商退货通知单
   * 
   * @param bill
   *          退货通知单，not null
   * @throws WMSException
   */
  void saveModify(RtnSupplierNtcBill bill) throws WMSException;

  /**
   * 作废供应商退货通知单
   * 
   * @param uuid
   *          要作废的退货通知单UUID，not null
   * @param version
   *          版本号，not null
   * @throws IllegalArgumentException
   * @throws WMSException
   */
  void abort(String uuid, long version) throws IllegalArgumentException, WMSException;

  /**
   * 完成供应商退货通知单
   * 
   * @param uuid
   *          要完成的退货通知单UUID，not null
   * @param version
   *          版本号，not null
   * @throws IllegalArgumentException
   * @throws WMSException
   */
  void finish(String uuid, long version) throws IllegalArgumentException, WMSException;

  /**
   * 下架
   * 
   * @param unshelvedInfo
   *          下架信息，not null
   * @throws IllegalArgumentException
   * @throws WMSException
   */
  void unshelve(String itemUuid, BigDecimal unshelvedQty)
      throws IllegalArgumentException, WMSException;

  /**
   * 退货交接回写实际退货数量
   * 
   * @param billNumber
   *          退货通知单单号， not null
   * @param articleUuid
   *          商品ID， not null
   * @param qty
   *          数量， not null
   * @throws WMSException
   *           实退数量大于下架数量
   */
  void handover(String billNumber, String articleUuid, BigDecimal qty) throws WMSException;

  /**
   * 生成下架指令
   * 
   * @param uuid
   *          供应商退货通知单UUID，not null
   * @param version
   *          版本号，not null
   * @return 下架指令
   * @throws IllegalArgumentException
   * @throws WMSException
   */
  void genUnshelveTask(String uuid, long version) throws IllegalArgumentException, WMSException;

  /**
   * 根据UUID获取明细
   * 
   * @param uuid
   *          UUID， 为空则返回空
   * @return 供应商退货通知单明细
   * @throws WMSException
   */
  RtnSupplierNtcBillItem getItem(String uuid) throws WMSException;
}
