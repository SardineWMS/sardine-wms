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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.hd123.sardine.wms.api.ia.resource.Resource;
import com.hd123.sardine.wms.common.utils.UserType;
import com.hd123.sardine.wms.dao.ia.resource.ResourceDao;

/**
 * @author zhangsai
 *
 */
public class ResourceDaoImpl extends SqlSessionDaoSupport implements ResourceDao {
  public static final String MAPPER_SAVEROLERESOURCE = "saveRoleResource";
  public static final String MAPPER_SAVEUSERRESOURCE = "saveUserResource";
  public static final String MAPPER_REMOVERESOURCEBYUSER = "removeResourceByUser";
  public static final String MAPPER_REMOVERESOURCEBYROLE = "removeResourceByRole";
  public static final String MAPPER_QUERYALLTOPMENURESOURCE = "queryAllTopMenuResource";
  public static final String MAPPER_QUERYOWNEDTOPMENURESOURCEBYROLE = "queryOwnedTopMenuResourceByRole";
  public static final String MAPPER_QUERYOWNEDTOPMENURESOURCEBYUSER = "queryOwnedTopMenuResourceByUser";
  public static final String MAPPER_QUERYALLCHILDRESOURCE = "queryAllChildResource";
  public static final String MAPPER_QUERYOWNEDCHILDRESOURCEBYROLE = "queryOwnedChildResourceByRole";
  public static final String MAPPER_QUERYOWNEDCHILDRESOURCEBYUSER = "queryOwnedChildResourceByUser";
  public static final String MAPPER_QUERYOWNEDOPERATEBYUSER = "queryOwnedOperateByUser";
  public static final String MAPPER_QUERYOWNEDTOPMENURESOURCEBYUSERTYPE = "queryOwnedTopMenuResourceByUserType";
  public static final String MAPPER_QUERYOWNEDOPERATEBYUSERTYPE = "queryOwnedOperateByUserType";

  public String generateStatement(String mapperId) {
    return this.getClass().getName() + "." + mapperId;
  }

  @Override
  public void saveRoleResource(String roleUuid, String resourceUuid) {
    Map<String, String> map = new HashMap<>();
    map.put("roleUuid", roleUuid);
    map.put("resourceUuid", resourceUuid);

    getSqlSession().insert(generateStatement(MAPPER_SAVEROLERESOURCE), map);
  }

  @Override
  public void saveUserResource(String userUuid, String resourceUuid) {
    Map<String, String> map = new HashMap<>();
    map.put("userUuid", userUuid);
    map.put("resourceUuid", resourceUuid);

    getSqlSession().insert(generateStatement(MAPPER_SAVEUSERRESOURCE), map);
  }

  @Override
  public void removeResourceByUser(String userUuid) {
    getSqlSession().delete(generateStatement(MAPPER_REMOVERESOURCEBYUSER), userUuid);
  }

  @Override
  public void removeResourceByRole(String roleUuid) {
    getSqlSession().delete(generateStatement(MAPPER_REMOVERESOURCEBYROLE), roleUuid);
  }

  @Override
  public List<Resource> queryAllTopMenuResource() {
    return getSqlSession().selectList(generateStatement(MAPPER_QUERYALLTOPMENURESOURCE), null);
  }

  @Override
  public List<Resource> queryOwnedTopMenuResourceByRole(String roleUuid) {
    return getSqlSession().selectList(generateStatement(MAPPER_QUERYOWNEDTOPMENURESOURCEBYROLE),
        roleUuid);
  }

  @Override
  public List<Resource> queryOwnedTopMenuResourceByUser(String userUuid) {
    return getSqlSession().selectList(generateStatement(MAPPER_QUERYOWNEDTOPMENURESOURCEBYUSER),
        userUuid);
  }

  @Override
  public List<Resource> queryAllChildResource(String resourceUuid) {
    return getSqlSession().selectList(generateStatement(MAPPER_QUERYALLCHILDRESOURCE),
        resourceUuid);
  }

  @Override
  public List<Resource> queryOwnedChildResourceByRole(String roleUuid, String resourceUuid) {
    Map<String, String> map = new HashMap<>();
    map.put("roleUuid", roleUuid);
    map.put("resourceUuid", resourceUuid);
    return getSqlSession().selectList(generateStatement(MAPPER_QUERYOWNEDCHILDRESOURCEBYROLE), map);
  }

  @Override
  public List<Resource> queryOwnedChildResourceByUser(String userUuid, String resourceUuid) {
    Map<String, String> map = new HashMap<>();
    map.put("userUuid", userUuid);
    map.put("resourceUuid", resourceUuid);
    return getSqlSession().selectList(generateStatement(MAPPER_QUERYOWNEDCHILDRESOURCEBYUSER), map);
  }

  @Override
  public List<Resource> queryOwnedOperateByUser(String userUuid) {
    return getSqlSession().selectList(generateStatement(MAPPER_QUERYOWNEDOPERATEBYUSER), userUuid);
  }

  @Override
  public List<Resource> queryOwnedTopMenuResourceByUserType(UserType userType) {
    return getSqlSession().selectList(generateStatement(MAPPER_QUERYOWNEDTOPMENURESOURCEBYUSERTYPE),
        userType.name());
  }

  @Override
  public List<Resource> queryOwnedOperateByUserType(UserType userType) {
    return getSqlSession().selectList(generateStatement(MAPPER_QUERYOWNEDOPERATEBYUSERTYPE),
        userType.name());
  }

  @Override
  public List<Resource> queryOwnedResourceByUserType(UserType userType) {
    return null;
  }
}
