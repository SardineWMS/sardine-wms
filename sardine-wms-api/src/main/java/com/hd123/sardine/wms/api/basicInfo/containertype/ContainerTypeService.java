/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ContainerTypeService.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月6日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.containertype;

import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * @author fanqingqing
 *
 */
public interface ContainerTypeService {
  /** 代码 类似于 */
  public static final String QUERY_CODE_FIELD = "code";
  /** 名称 类似于 */
  public static final String QUERY_NAME_FIELD = "name";

  /**
   * 分页查询容器类型
   * 
   * @param definition
   *          搜索条件，not null。
   * @return 分页结果
   * @throws IllegalArgumentException
   */
  PageQueryResult<ContainerType> query(PageQueryDefinition definition)
      throws IllegalArgumentException;

  /**
   * 根据uuid查询容器类型。
   * 
   * @param uuid
   *          唯一标示，not null。
   * @return 容器类型
   */
  ContainerType get(String uuid);

  /**
   * 新增容器类型
   * 
   * @param containerType
   *          容器类型，not null。
   * @return 容器类型uuid
   * @throws IllegalArgumentException
   *           参数为空时抛出
   * @throws WMSException
   */
  String saveNew(ContainerType containerType) throws IllegalArgumentException, WMSException;

  /**
   * 更新容器类型信息
   * 
   * @param supplier
   *          容器类型，not null。
   * @throws IllegalArgumentException
   *           参数为空
   * @throws WMSException
   */
  void saveModify(ContainerType containerType) throws IllegalArgumentException, WMSException;

  /**
   * 删除供应商
   * 
   * @param uuid
   *          容器类型uuid，not null。
   * @param version
   *          版本号，not null。
   * @throws IllegalArgumentException
   *           参数为空
   * @throws WMSException
   */
  void remove(String uuid, long version) throws IllegalArgumentException, WMSException;
}
