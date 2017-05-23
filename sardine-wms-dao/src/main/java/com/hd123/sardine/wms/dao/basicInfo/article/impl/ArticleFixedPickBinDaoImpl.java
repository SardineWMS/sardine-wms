/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	ArticleFixedPickBinDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月23日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.article.impl;

import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.dao.basicInfo.article.ArticleFixedPickBinDao;

/**
 * @author fanqingqing
 *
 */
public class ArticleFixedPickBinDaoImpl extends SqlSessionDaoSupport
        implements ArticleFixedPickBinDao {
    private static final String MAPPER_INSERT = "insert";
    private static final String MAPPER_REMOVEBYARTICLE = "removeByarticle";
    private static final String MAPPER_REMOVEBYFIXEDPICKBIN = "removeByFixedPickBin";
    private static final String MAPPER_GETFIXEDPICKBIN = "getFixedPickBin";
    private static final String MAPPER_REMOVEBYARTICLECOMPANY = "removeByArticleCompany";

    public String generateStatement(String mapperId) {
        return this.getClass().getName() + "." + mapperId;
    }

    @Override
    public void insert(String articleUuid, String fixedPickBin) {
        Map<String, Object> map = ApplicationContextUtil.map();
        map.put("articleUuid", articleUuid);
        map.put("fixedPickBin", fixedPickBin);

        getSqlSession().insert(generateStatement(MAPPER_INSERT), map);
    }

    @Override
    public void removeByArticle(String articleUuid) {
        getSqlSession().delete(generateStatement(MAPPER_REMOVEBYARTICLE), articleUuid);
    }

    @Override
    public void removeByFixedPickBin(String fixedPickBin) {
        Map<String, Object> map = ApplicationContextUtil.map();
        map.put("fixedPickBin", fixedPickBin);
        getSqlSession().delete(generateStatement(MAPPER_REMOVEBYFIXEDPICKBIN), map);
    }

    @Override
    public String getFixedPickBin(String articleUuid) {
        Map<String, Object> map = ApplicationContextUtil.map();
        map.put("articleUuid", articleUuid);
        return getSqlSession().selectOne(generateStatement(MAPPER_GETFIXEDPICKBIN), map);
    }

    @Override
    public void removeByArticleCompany(String articleUuid) {
        Map<String, Object> map = ApplicationContextUtil.map();
        map.put("articleUuid", articleUuid);
        getSqlSession().delete(generateStatement(MAPPER_REMOVEBYARTICLECOMPANY), map);
    }

}
