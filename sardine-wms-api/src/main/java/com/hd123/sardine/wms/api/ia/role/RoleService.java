/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	RoleService.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月23日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.ia.role;

import com.hd123.sardine.wms.common.entity.OperateContext;
import com.hd123.sardine.wms.common.exception.EntityNotFoundException;
import com.hd123.sardine.wms.common.exception.IAException;
import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * 橘色服务：接口
 * 
 * @author zhangsai
 *
 */
public interface RoleService {

  PageQueryResult<Role> query(PageQueryDefinition definition) throws IllegalArgumentException;

  String insert(Role role, OperateContext operCtx) throws IllegalArgumentException, IAException;

  void update(Role role, OperateContext operCtx) throws IllegalArgumentException,
      EntityNotFoundException, VersionConflictException, IAException;

  void online(String uuid, long version, OperateContext operCtx)
      throws IllegalArgumentException, EntityNotFoundException, VersionConflictException;

  void offline(String uuid, long version, OperateContext operCtx)
      throws IllegalArgumentException, EntityNotFoundException, VersionConflictException;

  void remove(String uuid, long version, OperateContext operCtx)
      throws IllegalArgumentException, EntityNotFoundException, VersionConflictException;
}
