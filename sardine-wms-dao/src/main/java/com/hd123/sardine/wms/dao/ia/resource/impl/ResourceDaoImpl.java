/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	ResourceDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月24日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.ia.resource.impl;

import java.util.List;

import com.hd123.sardine.wms.api.ia.resource.Resource;
import com.hd123.sardine.wms.common.dao.impl.BaseDaoImpl;
import com.hd123.sardine.wms.dao.ia.resource.ResourceDao;

/**
 * @author zhangsai
 *
 */
public class ResourceDaoImpl extends BaseDaoImpl<Resource> implements ResourceDao {

  @Override
  public void saveRoleResource(String roleUuid, String resourceUuid) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void saveUserResource(String userUuid, String resourceUUuid) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public List<Resource> queryOwnedResourceByRole(String roleUuid) {
    // TODO Auto-generated method stub
    return null;
  }

}
