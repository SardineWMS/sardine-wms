/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	CategoryStorageAreaConfigDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月15日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.config.categorystorageareaconfig.impl;

import java.util.List;
import java.util.Map;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.config.categorystorageareaconfig.CategoryStorageAreaConfig;
import com.hd123.sardine.wms.common.dao.NameSpaceSupport;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.dao.basicInfo.config.categorystorageareaconfig.CategoryStorageAreaConfigDao;

/**
 * @author fanqingqing
 *
 */
public class CategoryStorageAreaConfigDaoImpl extends NameSpaceSupport
        implements CategoryStorageAreaConfigDao {

    @Override
    public void insertCategoryStorageAreaConfig(
            CategoryStorageAreaConfig categoryStorageAreaConfig) {
        Assert.assertArgumentNotNull(categoryStorageAreaConfig, "categoryStorageAreaConfig");

        insert(MAPPER_INSERT, categoryStorageAreaConfig);
    }

    @Override
    public void removeCategoryStorageAreaConfig(String categoryUuid, long version) {
        Assert.assertArgumentNotNull(categoryUuid, "categoryUuid");
        Assert.assertArgumentNotNull(version, "version");

        Map<String, Object> map = ApplicationContextUtil.map();
        map.put("categoryUuid", categoryUuid);
        map.put("version", version);
        int i = getSqlSession().delete(generateStatement(MAPPER_REMOVE), map);
        PersistenceUtils.optimisticVerify(i);
    }

    @Override
    public void updateCategoryStorageAreaConfig(
            CategoryStorageAreaConfig categoryStorageAreaConfig) {
        Assert.assertArgumentNotNull(categoryStorageAreaConfig, "categoryStorageAreaConfig");

        int i = getSqlSession().update(generateStatement(MAPPER_UPDATE), categoryStorageAreaConfig);
        PersistenceUtils.optimisticVerify(i);
    }

    @Override
    public CategoryStorageAreaConfig getStorageAreaByCategory(String categoryUuid) {
        if (StringUtil.isNullOrBlank(categoryUuid))
            return null;

        Map<String, Object> map = ApplicationContextUtil.map();
        map.put("categoryUuid", categoryUuid);
        return getSqlSession().selectOne(generateStatement(MAPPER_GET), map);
    }

    @Override
    public List<CategoryStorageAreaConfig> query(PageQueryDefinition definition) {
        Assert.assertArgumentNotNull(definition, "definition");

        definition.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
        definition.put("parentCompanyUuid", ApplicationContextUtil.getParentCompanyUuid());
        return getSqlSession().selectList(generateStatement(MAPPER_QUERY_BYPAGE), definition);
    }

}
