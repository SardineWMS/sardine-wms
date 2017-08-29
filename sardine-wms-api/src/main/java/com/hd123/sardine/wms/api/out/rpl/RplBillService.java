/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	RplBillService.java
 * 模块说明：	
 * 修改历史：
 * 2014-8-12 - WUJING - 创建。
 */
package com.hd123.sardine.wms.api.out.rpl;

import java.util.List;

import com.hd123.sardine.wms.api.task.TaskView;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.exception.WMSException;

/**
 * 补货单 | 接口
 * 
 * @author WUJING
 */
public interface RplBillService {

  /**
   * 新增补货单
   * 
   * @param rplBill
   *          补货单 not null
   */
  void saveNew(RplBill rplBill) throws WMSException;

  /**
   * 根据uuid查询补货单
   * 
   * @param uuid
   *          补货单uuid，为空将返回null
   * @return 存在则返回，不存在返回null
   * @throws DBOperServiceException
   */
  RplBill get(String uuid);

  /**
   * 根据单号查询补货单
   * 
   * @param billNumber
   *          not null 单号
   * @return 存在则返回，不存在返回null
   * @throws DBOperServiceException
   */
  RplBill getByBillNumber(String billNumber);

  /**
   * 批准该波次下的所有补货单
   * <p>
   * <ol>
   * <li>修改补货单状态为“已批准”</li>
   * </ol>
   * 
   * @param waveBillNumber
   *          波次单号，not null
   * @throws IllegalArgumentException
   * @throws DBOperServiceException
   */
  void approveByWaveBillNumber(String waveBillNumber);

  /**
   * 根据波次号查询该波次生成的补货任务
   * 
   * @param waveBillNumber
   *          波次单号
   * @return 指令视图集合
   */
  List<TaskView> queryByWaveBillNumber(String waveBillNumber);

  /**
   * 查询波次下的所有补货明细
   * 
   * @param waveBillNumber
   *          波次单号， not null
   * @return 补货明细集合
   */
  List<RplBillItem> queryRplItems(String waveBillNumber);

  /**
   * 删除波次下的所有补货单
   * 
   * @param waveBillNumber
   *          波次单号
   */
  void removeByWaveBillNumber(String waveBillNumber);

  /**
   * 补货
   * 
   * @param rplItemUuids
   *          补货单明细UUID集合， not null
   * @param rpler
   *          补货人，为空时取当前登录人
   * @throws WMSException
   */
  void rpl(List<String> rplItemUuids, UCN rpler) throws WMSException;
}
