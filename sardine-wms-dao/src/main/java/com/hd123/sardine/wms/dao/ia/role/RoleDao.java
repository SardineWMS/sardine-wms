/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	RoleDao.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月23日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.ia.role;

import com.hd123.sardine.wms.api.ia.role.Role;
import com.hd123.sardine.wms.common.dao.BaseDao;

/**
 * @author zhangsai
 *
 */
public interface RoleDao  extends BaseDao<Role>  {

//  List<Role> query(PageQueryDefinition definition);
//
//  void insert(Role role);
//
//  void update(Role role);
//
//  Role getByUuid(String uuid);
//
  Role getByCode(String code, String orgId);
//
//  void remove(String uuid, long version);
}
