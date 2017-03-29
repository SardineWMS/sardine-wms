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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
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
    private ResourceDao dao;

    @Override
    public List<Resource> queryOwnedResourceByUser(String userUuid, ResourceType type)
            throws IllegalArgumentException {
        Assert.assertArgumentNotNull(userUuid, "userUuid");

        List<Resource> resources = new ArrayList<>();
        resources = dao.queryOwnedResourceByUser(userUuid);
        if (CollectionUtils.isEmpty(resources) || type == null)
            return resources;

        List<Resource> menuResources = new ArrayList<>();
        List<Resource> operateResources = new ArrayList<>();
        for (Resource resource : resources) {
            if (StringUtil.isNullOrBlank(resource.getUpperUuid()))
                menuResources.add(resource);
            else
                operateResources.add(resource);
        }
        if (ResourceType.menu.equals(type))
            return menuResources;
        return operateResources;
    }

    @Override
    public List<Resource> queryAllResourceByUser(String userUuid) throws IllegalArgumentException {
        Assert.assertArgumentNotNull(userUuid, "userUuid");

        List<Resource> allResources = dao.queryAllResource();
        List<Resource> ownedResources = dao.queryOwnedResourceByUser(userUuid);

        for (Resource resource : allResources) {
            if (ownedResources.contains(resource))
                resource.setOwned(true);
            else
                resource.setOwned(false);
        }

        return allResources;
    }

    @Override
    public List<Resource> queryOwnedResourceByRole(String roleUuid)
            throws IllegalArgumentException {
        Assert.assertArgumentNotNull(roleUuid, "roleUuid");

        List<Resource> resources = dao.queryOwnedResourceByRole(roleUuid);
        return resources;
    }

    @Override
    public List<Resource> queryAllResourceByRole(String roleUuid) throws IllegalArgumentException {
        Assert.assertArgumentNotNull(roleUuid, "roleUuid");

        List<Resource> allResources = dao.queryAllResource();
        List<Resource> ownedResources = dao.queryOwnedResourceByRole(roleUuid);

        for (Resource resource : allResources) {
            if (ownedResources.contains(resource))
                resource.setOwned(true);
            else
                resource.setOwned(false);
        }

        return allResources;
    }

    @Override
    public void saveUserResource(String userUuid, List<String> resourceUuids)
            throws IllegalArgumentException {
        Assert.assertArgumentNotNull(userUuid, "userUuid");

        dao.removeResourceByUser(userUuid);
        if (CollectionUtils.isEmpty(resourceUuids))
            return;
        for (String resourceUuid : resourceUuids)
            dao.saveUserResource(userUuid, resourceUuid);
    }

    @Override
    public void saveRoleResource(String roleUuid, List<String> resourceUuids)
            throws IllegalArgumentException {
        Assert.assertArgumentNotNull(roleUuid, "roleUuid");

        dao.removeResourceByRole(roleUuid);
        if (CollectionUtils.isEmpty(resourceUuids))
            return;
        for (String resourceUuid : resourceUuids)
            dao.saveRoleResource(roleUuid, resourceUuid);
    }

    @Override
    public void removeResourceByUser(String userUuid) throws IllegalArgumentException {
        Assert.assertArgumentNotNull(userUuid, "userUuid");

        dao.removeResourceByUser(userUuid);
    }

    @Override
    public void removeResourceByRole(String roleUuid) throws IllegalArgumentException {
        Assert.assertArgumentNotNull(roleUuid, "roleUuid");

        dao.removeResourceByRole(roleUuid);
    }
}
