/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	WaveBillService.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月21日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.out.wave;

import java.util.List;

import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * 波次单服务|接口
 * 
 * @author yangwenzhu
 *
 */
public interface WaveBillService {
  public static final String QUERY_BILLNUMBER_LIKE = "billNumber";
  public static final String QUERY_STATE_EQUALS = "state";
  public static final String QUERY_WAVETYPE_EQUALS = "type";

  public static final String ORDER_BILLNUMBER = "billNumber";

  /**
   * 新建波次单
   * 
   * @param bill
   *          波次单，not null
   * @return 新建波次单UUID
   * @throws IllegalArgumentException
   *           bill为空抛出
   * @throws WMSException
   */
  String saveNew(WaveBill bill) throws IllegalArgumentException, WMSException;

  /**
   * 修改波次单
   * 
   * @param bill
   *          波次单，not null
   * @throws IllegalArgumentException
   *           bill为空时抛出
   * @throws VersionConflictException
   *           波次单已被其他用户修改
   * @throws WMSException
   */
  void saveModify(WaveBill bill)
      throws IllegalArgumentException, VersionConflictException, WMSException;

  /**
   * 删除波次单
   * 
   * @param uuid
   *          uuid,not null
   * @param version
   *          版本号,not null
   * @throws IllegalArgumentException
   *           uuid,version为空时抛出
   * @throws VersionConflictException
   *           波次单已被其他用户修改
   * @throws WMSException
   */
  void remove(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException;

  /**
   * 启动波次单
   * 
   * @param uuid
   *          uuid,not null
   * @param version
   *          版本号,not null
   * @return 返回波次包含的所有商品
   * @throws IllegalArgumentException
   *           uuid,version为空时抛出
   * @throws VersionConflictException
   *           波次单已被其他用户修改
   * @throws WMSException
   */
  List<String> start(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException;

  /**
   * 执行商品补货拣货
   * 
   * @param uuid
   *          波次UUID，not null
   * @param billNumber
   *          波次单号。not null
   * @param articleUuids
   *          商品UUID集合， not null
   * @throws IllegalArgumentException
   * @throws WMSException
   */
  void executeArticle(String uuid, String billNumber, List<String> articleUuids)
      throws WMSException;

  /**
   * 
   * @param uuid
   * @param billNumber
   */
  void generatePickUpBill(String uuid, String billNumber) throws WMSException;

  /**
   * 校验波次生成结果，并修改波次状态
   * 
   * @param billNumber
   *          波次单号，not null
   * @throws IllegalArgumentException
   * @throws DBOperServiceException
   */
  void startComplete(String billNumber) throws WMSException;

  /**
   * 启动异常
   * <p>
   * <ol>
   * <li>修改波次单状态：启动异常</li>
   * </ol>
   * 
   * @param billNumber
   *          波次单号，not null
   * @param errorMessage
   *          错误信息
   * @throws IllegalArgumentException
   */
  void startException(String billNumber, String errorMessage) throws WMSException;

  /**
   * 回滚波次单
   * 
   * 启动异常、启动完成-->未启动
   * 
   * @param uuid
   *          波次单Uuid
   * @param version
   *          版本号
   * @throws WMSException
   * @throws IllegalArgumentException
   * @throws VersionConflictException
   */
  void rollBack(String uuid, long version) throws WMSException;

  /**
   * 确认波次单
   * 
   * @param uuid
   *          波次单uuid，not null
   * @param version
   *          版本号
   * @throws IllegalArgumentException
   * @throws VersionConflictException
   * @throws WMSException
   */
  void confirm(String uuid, long version) throws WMSException;

  /**
   * 根据UUID， 获取波次单
   * 
   * @param uuid
   *          uuid,为空则返回null
   * @return 波次单
   */
  WaveBill get(String uuid);

  /**
   * 根据单号，获取波次单
   * 
   * @param billNumber
   *          billNumber,为空则返回null
   * @return 波次单
   */
  WaveBill getByBillNumber(String billNumber);

  /**
   * 分页获取波次单
   * 
   * @param definition
   *          分页条件，not null
   * @return 分页结果集
   */
  PageQueryResult<WaveBill> query(PageQueryDefinition definition);
}
