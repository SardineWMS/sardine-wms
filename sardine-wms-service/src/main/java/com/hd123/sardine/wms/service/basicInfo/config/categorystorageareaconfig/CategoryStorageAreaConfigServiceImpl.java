/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	CategoryStorageAreaConfigServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月14日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.service.basicInfo.config.categorystorageareaconfig;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.article.Article;
import com.hd123.sardine.wms.api.basicInfo.article.ArticleService;
import com.hd123.sardine.wms.api.basicInfo.category.Category;
import com.hd123.sardine.wms.api.basicInfo.category.CategoryService;
import com.hd123.sardine.wms.api.basicInfo.config.categorystorageareaconfig.CategoryStorageAreaConfig;
import com.hd123.sardine.wms.api.basicInfo.config.categorystorageareaconfig.CategoryStorageAreaConfigService;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.dao.basicInfo.config.categorystorageareaconfig.CategoryStorageAreaConfigDao;

/**
 * @author fanqingqing
 *
 */
public class CategoryStorageAreaConfigServiceImpl implements CategoryStorageAreaConfigService {

    @Autowired
    private CategoryStorageAreaConfigDao dao;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ArticleService articleService;

    @Override
    public void setCategoryStorageAreaConfig(String categoryUuid, String storageArea, long version)
            throws IllegalArgumentException, VersionConflictException, WMSException {
        Assert.assertArgumentNotNull(categoryUuid, "categoryUuid");

        CategoryStorageAreaConfig categoryStorageAreaConfig = dao
                .getStorageAreaByCategory(categoryUuid);
        if (categoryStorageAreaConfig == null) {
            if (StringUtil.isNullOrBlank(storageArea))
                return;
            Category category = categoryService.get(categoryUuid);
            if (category == null)
                throw new WMSException("商品类别不存在");
            CategoryStorageAreaConfig newCategoryStorageAreaConfig = new CategoryStorageAreaConfig();
            newCategoryStorageAreaConfig.setCategory(
                    new UCN(category.getUuid(), category.getCode(), category.getName()));
            newCategoryStorageAreaConfig.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
            newCategoryStorageAreaConfig.setStorageArea(storageArea);
            dao.insertCategoryStorageAreaConfig(newCategoryStorageAreaConfig);
            return;
        }

        PersistenceUtils.checkVersion(version, categoryStorageAreaConfig, "商品类别存储区域配置",
                categoryStorageAreaConfig.getCategory().getCode());
        if (StringUtil.isNullOrBlank(storageArea)) {
            dao.removeCategoryStorageAreaConfig(categoryUuid, version);
            return;
        }

        categoryStorageAreaConfig.setStorageArea(storageArea);
        dao.updateCategoryStorageAreaConfig(categoryStorageAreaConfig);
    }

    @Override
    public PageQueryResult<CategoryStorageAreaConfig> query(PageQueryDefinition definition)
            throws IllegalArgumentException {
        Assert.assertArgumentNotNull(definition, "definition");

        PageQueryResult<CategoryStorageAreaConfig> pgr = new PageQueryResult<CategoryStorageAreaConfig>();
        List<CategoryStorageAreaConfig> list = dao.query(definition);
        PageQueryUtil.assignPageInfo(pgr, definition);
        pgr.setRecords(list);
        return pgr;
    }

    @Override
    public String getStorageAreaByCategory(String categoryUuid) {
        if (StringUtil.isNullOrBlank(categoryUuid))
            return null;
        CategoryStorageAreaConfig categoryStorageAreaConfig = dao
                .getStorageAreaByCategory(categoryUuid);
        if (categoryStorageAreaConfig == null)
            return null;
        return categoryStorageAreaConfig.getStorageArea();
    }

    @Override
    public String getStorageAreaByArticle(String articleUuid) {
        if (StringUtil.isNullOrBlank(articleUuid))
            return null;
        Article article = articleService.get(articleUuid);
        if (article == null)
            return null;

        return getCategoryStorageArea(article.getCategory().getUuid());
    }

    private String getCategoryStorageArea(String categoryUuid) {
        String storageArea = getStorageArea(categoryUuid);
        if (StringUtil.isNullOrBlank(storageArea) == false)
            return storageArea;
        Category category = categoryService.get(categoryUuid);
        if (StringUtil.isNullOrBlank(category.getUpperCategory())
                || Category.DEFAULT_ROOTCATEGORY.equals(category.getUpperCategory()))
            return null;
        return getCategoryStorageArea(category.getUpperCategory());
    }

    private String getStorageArea(String categoryUuid) {
        CategoryStorageAreaConfig categoryStorageAreaConfig = dao
                .getStorageAreaByCategory(categoryUuid);
        if (categoryStorageAreaConfig == null)
            return null;
        return categoryStorageAreaConfig.getStorageArea();
    }

    @Override
    public CategoryStorageAreaConfig getCategoryStorageAreaConfig(String categoryUuid) {
        // TODO Auto-generated method stub
        return null;
    }

}
