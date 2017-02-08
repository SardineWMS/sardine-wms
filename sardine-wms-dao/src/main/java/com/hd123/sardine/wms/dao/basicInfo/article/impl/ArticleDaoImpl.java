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

import java.util.HashMap;
import java.util.Map;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.article.Article;
import com.hd123.sardine.wms.common.dao.impl.BaseDaoImpl;
import com.hd123.sardine.wms.dao.basicInfo.article.ArticleDao;

/**
 * @author zhangsai
 *
 */
public class ArticleDaoImpl extends BaseDaoImpl<Article> implements ArticleDao {

  public static final String MAPPER_GETBYCODE = "getByCode";
  public static final String MAPPER_GETBYBARCODE = "getByBarcode";

  @Override
  public Article getByCode(String code, String companyUuid) {
    if (StringUtil.isNullOrBlank(code) || StringUtil.isNullOrBlank(companyUuid))
      return null;

    Map<String, String> map = new HashMap<String, String>();
    map.put("code", code);
    map.put("companyUuid", companyUuid);
    return getSqlSession().selectOne(generateStatement(MAPPER_GETBYCODE), map);
  }

  @Override
  public Article getByBarcode(String barcode, String companyUuid) {
    if (StringUtil.isNullOrBlank(barcode) || StringUtil.isNullOrBlank(companyUuid))
      return null;

    Map<String, String> map = new HashMap<String, String>();
    map.put("barcode", barcode);
    map.put("companyUuid", companyUuid);
    return getSqlSession().selectOne(generateStatement(MAPPER_GETBYBARCODE), map);
  }
}
