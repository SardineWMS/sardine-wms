/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	SerialArchService.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月14日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.tms.serialarch;

import java.util.List;

import com.hd123.sardine.wms.api.basicInfo.customer.Customer;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * 线路体系服务|接口
 * 
 * @author yangwenzhu
 *
 */
public interface SerialArchService {
  /**
   * 保存线路体系
   * 
   * @param arch
   *          线路体系，not null
   * @return 线路体系UUID
   * @throws IllegalArgumentException
   *           arch为空时抛出
   * @throws WMSException
   */
  String saveNewSerialArch(SerialArch arch) throws WMSException;

  /**
   * 查询线路体系
   * 
   * @param definition
   *          查询条件，not null
   * @return 查询结果
   * @throws IllegalArgumentException
   *           definition为空时抛出
   */
  PageQueryResult<SerialArch> query(PageQueryDefinition definition);

  /**
   * 新增运输线路
   * 
   * @param line
   *          运输线路，not null
   * @return 运输线路UUID
   * @throws IllegalArgumentException
   * @throws WMSException
   */
  String saveNewSerialArchLine(SerialArchLine line) throws WMSException;

  /**
   * 根据运输体系UUID查询运输线路
   * 
   * @param archUuid
   *          运输体系UUID， 为空则返回空集合
   * @return 运输线路集合
   */
  List<SerialArchLine> getLinesByArchUuid(String archUuid);

  /**
   * 查询当前登录组织下的线路体系树
   * 
   * @return
   */
  List<SerialArchInfo> queryTreeData();

  /**
   * 根据代码查询运输线路
   * 
   * @param code
   *          代码，为空则返回空
   * @return 运输线路
   * @throws WMSException
   */
  SerialArchLine getLineByCode(String code) throws WMSException;

  /**
   * 线路中添加客户
   * 
   * @param lineUuid
   * @param customerUuids
   * @throws WMSException
   * @throws IllegalArgumentException
   */
  String saveLineCustomer(String lineUuid, List<String> customerUuids) throws WMSException;

  /**
   * 查询运输线路下所有客户
   * 
   * @param lineUuid
   * @return
   */
  List<SerialArchLineCustomer> getCustomersByLineUuid(String lineUuid);

  /**
   * 根据UUID获取运输线路
   * 
   * @param lineUuid
   *          线路UUID，为空则返回null
   * @return 运输线路
   * @throws WMSException
   */
  SerialArchLine getLine(String lineUuid) throws WMSException;

  /**
   * 删除线路中的客户
   * 
   * @param customerUuid
   *          要删除客户UUID，not null
   * @param lineUuid
   *          线路UUID， not null
   * @throws WMSException
   */
  void removeCustomerFromLine(String customerUuid, String lineUuid) throws WMSException;

  /**
   * 线路体系中顺序上移
   * 
   * @param customerUuid
   *          客户UUID， not null
   * @param lineUuid
   *          线路UUID，not null
   * @param order
   *          线路中的原顺序,not null
   * @throws WMSException
   */
  void upOrder(String customerUuid, String lineUuid, int order) throws WMSException;

  /**
   * 线路体系中顺序下移
   * 
   * @param customerUuid
   *          客户UUID，not null
   * @param lineUuid
   *          线路UUID，not null
   * @param order
   *          线路中原的顺序,not null
   * @throws WMSException
   */
  void downOrder(String customerUuid, String lineUuid, int order) throws WMSException;

  /**
   * 置后
   * 
   * @param customerUuid
   *          客户UUID，not null
   * @param lineUuid
   *          线路UUID，not null
   * @param order
   *          线路中的原顺序，not null
   * @throws WMSException
   */
  void postponeCustomer(String customerUuid, String lineUuid, int order) throws WMSException;

  /**
   * 置顶
   * 
   * @param customerUuid
   *          客户UUID，not null
   * @param lineUuid
   *          线路UUID，not null
   * @param order
   *          线路中的原顺序，not null
   * @throws WMSException
   */
  void stickCustomer(String customerUuid, String lineUuid, int order) throws WMSException;

  /**
   * 根据线路分页获取客户
   * 
   * @param definition
   *          查询条件，not null
   * @return 分页结果集
   * @throws WMSException
   */
  PageQueryResult<SerialArchLineCustomer> queryCustomerByLine(PageQueryDefinition definition)
      throws WMSException;

  /**
   * 根据UUID获取线路体系
   * 
   * @param uuid
   *          uuid,为空则返回null
   * @return 线路体系
   */
  SerialArch get(String uuid);

  /**
   * 根据uuid删除线路
   * 
   * @param uuid
   *          线路UUID，not null
   * @param version
   *          版本号，not null
   * @exception IllegalArgumentException
   * @Exception VersionConflictException
   * @exception WMSException
   */
  void removeLine(String code) throws WMSException;

  /**
   * 查询没有加入线路中的客户列表
   * 
   * @param definition
   *          查询条件，not null
   * @return
   * @throws IllegalArgumentException
   */
  PageQueryResult<Customer> queryCustomerWithoutLine(PageQueryDefinition definition);
}
