/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	CategoryStorageAreaConfigDao.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月14日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.config.categorystorageareaconfig;

import java.util.List;

import com.hd123.sardine.wms.api.basicInfo.config.categorystorageareaconfig.CategoryStorageAreaConfig;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;

/**
 * @author fanqingqing
 *
 */
public interface CategoryStorageAreaConfigDao {
    void insertCategoryStorageAreaConfig(CategoryStorageAreaConfig categoryStorageAreaConfig);

    void removeCategoryStorageAreaConfig(String categoryUuid, long version);

    void updateCategoryStorageAreaConfig(CategoryStorageAreaConfig categoryStorageAreaConfig);

    CategoryStorageAreaConfig getStorageAreaByCategory(String categoryUuid);

    List<CategoryStorageAreaConfig> query(PageQueryDefinition definition);

}
