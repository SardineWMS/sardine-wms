/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	RoleDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月24日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.ia.role.impl;

import com.hd123.sardine.wms.api.ia.role.Role;
import com.hd123.sardine.wms.common.dao.impl.BaseDaoImpl;
import com.hd123.sardine.wms.dao.ia.role.RoleDao;

/**
 * @author zhangsai
 *
 */
public class RoleDaoImpl  extends BaseDaoImpl<Role>  implements RoleDao {

  @Override
  public int insert(Role model) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int update(Role model) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int remove(String uuid, long version) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public Role get(String uuid) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Role getByCode(String code, String orgId) {
    // TODO Auto-generated method stub
    return null;
  }

}
