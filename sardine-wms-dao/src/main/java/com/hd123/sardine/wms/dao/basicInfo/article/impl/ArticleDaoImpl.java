/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	ArticleDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月7日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.article.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.article.Article;
import com.hd123.sardine.wms.common.dao.impl.BaseDaoImpl;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.dao.basicInfo.article.ArticleDao;

/**
 * @author zhangsai
 *
 */
public class ArticleDaoImpl extends BaseDaoImpl<Article> implements ArticleDao {

  public static final String MAPPER_GETBYCODE = "getByCode";
  public static final String MAPPER_GETBYBARCODE = "getByBarcode";
  public static final String QUERYINSTOCKS = "queryInStocks";
  public static final String QUERYARTICLES = "queryArticles";

  @Override
  public Article getByCode(String code) {
    if (StringUtil.isNullOrBlank(code))
      return null;

    Map<String, Object> map = ApplicationContextUtil.mapWithParentCompanyUuid();
    map.put("code", code);
    return getSqlSession().selectOne(generateStatement(MAPPER_GETBYCODE), map);
  }

  @Override
  public Article getByBarcode(String barcode) {
    if (StringUtil.isNullOrBlank(barcode))
      return null;

    Map<String, Object> map = ApplicationContextUtil.mapWithParentCompanyUuid();
    map.put("barcode", barcode);
    return getSqlSession().selectOne(generateStatement(MAPPER_GETBYBARCODE), map);
  }

  @Override
  public List<UCN> queryInStocks(PageQueryDefinition definition) {
    Assert.assertArgumentNotNull(definition, "definition");
    List<UCN> result = getSqlSession().selectList(generateStatement(QUERYINSTOCKS), definition);

    return result;
  }

  @Override
  public List<Article> queryArticles(List<String> articleUuids) {
    if (CollectionUtils.isEmpty(articleUuids))
      return new ArrayList<Article>();
    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("articleUuids", articleUuids);
    return selectList(QUERYARTICLES, map);
  }
}
