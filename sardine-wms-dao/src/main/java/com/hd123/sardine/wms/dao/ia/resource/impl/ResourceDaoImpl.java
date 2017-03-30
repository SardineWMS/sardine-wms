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
import com.hd123.sardine.wms.dao.ia.resource.ResourceDao;

/**
 * @author zhangsai
 *
 */
public class ResourceDaoImpl extends SqlSessionDaoSupport implements ResourceDao {
    public static final String MAPPER_SAVEROLERESOURCE = "saveRoleResource";
    public static final String MAPPER_SAVEUSERRESOURCE = "saveUserResource";
    public static final String MAPPER_QUERYOWNEDRESOURCEBYROLE = "queryOwnedResourceByRole";
    public static final String MAPPER_QUERYALLRESOURCE = "queryAllResource";
    public static final String MAPPER_QUERYOWNEDRESOURCEBYUSER = "queryOwnedResourceByUser";
    public static final String MAPPER_REMOVERESOURCEBYUSER = "removeResourceByUser";
    public static final String MAPPER_REMOVERESOURCEBYROLE = "removeResourceByRole";

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
    public List<Resource> queryOwnedResourceByRole(String roleUuid) {
        return getSqlSession().selectList(generateStatement(MAPPER_QUERYOWNEDRESOURCEBYROLE),
                roleUuid);
    }

    @Override
    public List<Resource> queryAllResource() {
        return getSqlSession().selectList(generateStatement(MAPPER_QUERYALLRESOURCE), null);
    }

    @Override
    public List<Resource> queryOwnedResourceByUser(String userUuid) {
        return getSqlSession().selectList(generateStatement(MAPPER_QUERYOWNEDRESOURCEBYUSER),
                userUuid);
    }

    @Override
    public void removeResourceByUser(String userUuid) {
        getSqlSession().delete(generateStatement(MAPPER_REMOVERESOURCEBYUSER), userUuid);
    }

    @Override
    public void removeResourceByRole(String roleUuid) {
        getSqlSession().delete(generateStatement(MAPPER_REMOVERESOURCEBYROLE), roleUuid);
    }
}
