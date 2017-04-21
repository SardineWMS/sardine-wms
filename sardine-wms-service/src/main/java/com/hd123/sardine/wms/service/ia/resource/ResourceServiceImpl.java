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
import com.hd123.sardine.wms.api.ia.resource.Resource;
import com.hd123.sardine.wms.api.ia.resource.ResourceService;
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
    public List<Resource> queryOwnedMenuResourceByUser(String userUuid)
            throws IllegalArgumentException {
        Assert.assertArgumentNotNull(userUuid, "userUuid");

        List<Resource> topMule = dao.queryOwnedTopMenuResourceByUser(userUuid);
        for (Resource t : topMule) {
            List<Resource> moduleMenus = dao.queryOwnedChildResourceByUser(userUuid, t.getUuid());
            t.getChildren().addAll(moduleMenus);
        }
        return topMule;
    }

    @Override
    public List<Resource> queryAllResourceByUser(String userUuid) throws IllegalArgumentException {
        Assert.assertArgumentNotNull(userUuid, "userUuid");

        List<Resource> allTopMules = dao.queryAllTopMenuResource();
        List<Resource> owmTopMules = dao.queryOwnedTopMenuResourceByUser(userUuid);
        for (Resource t : allTopMules) {
            if (owmTopMules.contains(t))
                t.setOwned(true);
            List<Resource> allModuleMenus = dao.queryAllChildResource(t.getUuid());
            List<Resource> ownModuleMenus = dao.queryOwnedChildResourceByUser(userUuid,
                    t.getUuid());
            for (Resource m : allModuleMenus) {
                if (ownModuleMenus.contains(m))
                    m.setOwned(true);
                List<Resource> allOperates = dao.queryAllChildResource(m.getUuid());
                List<Resource> ownOperates = dao.queryOwnedChildResourceByUser(userUuid,
                        m.getUuid());
                for (Resource o : allOperates) {
                    if (ownOperates.contains(o))
                        o.setOwned(true);
                }
                m.getChildren().addAll(allOperates);
            }
            t.getChildren().addAll(allModuleMenus);
        }

        return allTopMules;
    }

    @Override
    public List<Resource> queryAllResourceByRole(String roleUuid) throws IllegalArgumentException {
        Assert.assertArgumentNotNull(roleUuid, "roleUuid");

        List<Resource> allTopMules = dao.queryAllTopMenuResource();
        List<Resource> owmTopMules = dao.queryOwnedTopMenuResourceByRole(roleUuid);
        for (Resource t : allTopMules) {
            if (owmTopMules.contains(t))
                t.setOwned(true);
            List<Resource> allModuleMenus = dao.queryAllChildResource(t.getUuid());
            List<Resource> ownModuleMenus = dao.queryOwnedChildResourceByRole(roleUuid,
                    t.getUuid());
            for (Resource m : allModuleMenus) {
                if (ownModuleMenus.contains(m))
                    m.setOwned(true);
                List<Resource> allOperates = dao.queryAllChildResource(m.getUuid());
                List<Resource> ownOperates = dao.queryOwnedChildResourceByRole(roleUuid,
                        m.getUuid());
                for (Resource o : allOperates) {
                    if (ownOperates.contains(o))
                        o.setOwned(true);
                }
                m.getChildren().addAll(allOperates);
            }
            t.getChildren().addAll(allModuleMenus);
        }

        return allTopMules;
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

    @Override
    public List<Resource> queryOwnedResourceByUuser(String userUuid)
            throws IllegalArgumentException {
        Assert.assertArgumentNotNull(userUuid, "userUuid");
        List<Resource> allOwnedResources = new ArrayList<Resource>();
        List<Resource> topMenuResources = dao.queryOwnedTopMenuResourceByUser(userUuid);
        allOwnedResources.addAll(topMenuResources);
        for (Resource t : topMenuResources) {
            List<Resource> moduleMenuResources = dao.queryOwnedChildResourceByUser(userUuid,
                    t.getUuid());
            allOwnedResources.addAll(moduleMenuResources);
            for (Resource m : moduleMenuResources) {
                List<Resource> operatesResources = dao.queryOwnedChildResourceByUser(userUuid,
                        m.getUuid());
                allOwnedResources.addAll(operatesResources);
            }
        }
        return allOwnedResources;
    }

    @Override
    public List<Resource> queryOwnedResourceByRole(String roleUuid)
            throws IllegalArgumentException {
        Assert.assertArgumentNotNull(roleUuid, "roleUuid");
        List<Resource> allOwnedResources = new ArrayList<Resource>();
        List<Resource> topMenuResources = dao.queryOwnedTopMenuResourceByRole(roleUuid);
        allOwnedResources.addAll(topMenuResources);
        for (Resource t : topMenuResources) {
            List<Resource> moduleMenuResources = dao.queryOwnedChildResourceByRole(roleUuid,
                    t.getUuid());
            allOwnedResources.addAll(moduleMenuResources);
            for (Resource m : moduleMenuResources) {
                List<Resource> operatesResources = dao.queryOwnedChildResourceByRole(roleUuid,
                        m.getUuid());
                allOwnedResources.addAll(operatesResources);
            }
        }
        return allOwnedResources;
    }
}