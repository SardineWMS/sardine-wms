/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	ResourceServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月23日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.ia.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.sardine.wms.api.ia.resource.Resource;
import com.hd123.sardine.wms.api.ia.resource.ResourceService;
import com.hd123.sardine.wms.api.ia.resource.ResourceType;
import com.hd123.sardine.wms.dao.ia.resource.ResourceDao;

/**
 * 资源服务：实现
 * 
 * @author zhangsai
 *
 */
public class ResourceServiceImpl implements ResourceService {

  @Autowired
  private ResourceDao resourceDao;

  @Override
  public List<Resource> queryOwnedResource(String userUuid, ResourceType type)
      throws IllegalArgumentException {
    return null;
  }

  @Override
  public List<Resource> queryAllResourceByUser(String userUuid) throws IllegalArgumentException {
    return null;
  }

  @Override
  public List<Resource> queryAllResourceByRole(String roleUuid) throws IllegalArgumentException {
    return null;
  }

  @Override
  public void saveUserResource(String userUuid, List<String> resourceUuids)
      throws IllegalArgumentException {

  }

  @Override
  public void saveRoleResource(String roleUuid, List<String> resourceUuids) {

  }
}
