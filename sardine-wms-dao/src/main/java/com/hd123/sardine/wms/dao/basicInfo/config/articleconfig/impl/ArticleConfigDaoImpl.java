/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	SystemConfigDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月6日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.config.articleconfig.impl;

import java.util.List;
import java.util.Map;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.basicInfo.config.articleconfig.ArticleConfig;
import com.hd123.sardine.wms.common.dao.NameSpaceSupport;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.dao.basicInfo.config.articleconfig.ArticleConfigDao;

/**
 * @author zhangsai
 *
 */
public class ArticleConfigDaoImpl extends NameSpaceSupport implements ArticleConfigDao {

    @Override
    public void insertArticleConfig(ArticleConfig articleConfig) {
        Assert.assertArgumentNotNull(articleConfig, "articleConfig");

        insert(MAPPER_INSERT, articleConfig);
    }

    @Override
    public void updateArticleConfig(ArticleConfig articleConfig) {
        Assert.assertArgumentNotNull(articleConfig, "articleConfig");

        update(MAPPER_UPDATE, articleConfig);
    }

    @Override
    public ArticleConfig getArticleConfig(String articleUuid) {
        Map<String, Object> map = ApplicationContextUtil.map();
        map.put("articleUuid", articleUuid);
        return selectOne(MAPPER_GET, map);
    }

    @Override
    public List<ArticleConfig> query(PageQueryDefinition definition) {
        Assert.assertArgumentNotNull(definition, "definition");

        definition.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
        definition.put("parentCompanyUuid", ApplicationContextUtil.getParentCompanyUuid());
        return getSqlSession().selectList(generateStatement(MAPPER_QUERY_BYPAGE), definition);
    }
}
