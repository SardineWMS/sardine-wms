/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	BinTypeService.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月6日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.bintype;

import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * 容器类型|接口
 * 
 * @author yangwenzhu
 *
 */
public interface BinTypeService {
  public static final String QUERY_CODE_FIELD = "code";
  public static final String QUERT_NAME_FIELD = "name";

  public static final String ORDER_CODE_FIELD = "code";

  /**
   * 根据UUID获取货位类型
   * 
   * @param uuid
   *          要获取货位类型uuid，not null
   * @return 货位类型实体
   */
  BinType get(String uuid);

  /**
   * 根据code获取货位类型
   * 
   * @param code
   *          要获取货位类型的code，not null
   * @return 货位类型实体
   */
  BinType getByCode(String code);

  /**
   * 新建货位类型
   * 
   * @param binType
   *          货位类型，not null
   * @return 新建货位类型UUID
   * @throws IllegalArgumentException
   *           参数异常
   * @throws WMSException
   */
  String insert(BinType binType) throws IllegalArgumentException, WMSException;

  /**
   * 删除客户类型
   * 
   * @param uuid
   *          要删除货位类型的UUID，not null
   * @param version
   *          版本号，not null
   * @throws IllegalArgumentException
   *           参数异常
   * @throws WMSException
   */
  void remove(String uuid, long version) throws IllegalArgumentException, WMSException;

  /**
   * 修改货位类型
   * 
   * @param binType
   *          修改的货位类型，not null
   * @throws IllegalArgumentException
   *           参数异常
   * @throws WMSException
   */
  void update(BinType binType) throws IllegalArgumentException, WMSException;

  /**
   * 分页查询客户
   * 
   * @param definition
   *          分页条件
   * @return 货位类型集合
   */
  PageQueryResult<BinType> query(PageQueryDefinition definition);
}
