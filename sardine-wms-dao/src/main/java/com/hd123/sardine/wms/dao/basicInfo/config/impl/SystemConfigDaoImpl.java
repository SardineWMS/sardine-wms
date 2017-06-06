/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	SystemConfigDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月6日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.config.impl;

import java.util.Map;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.basicInfo.config.ArticleConfig;
import com.hd123.sardine.wms.common.dao.NameSpaceSupport;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.dao.basicInfo.config.SystemConfigDao;

/**
 * @author zhangsai
 *
 */
public class SystemConfigDaoImpl extends NameSpaceSupport implements SystemConfigDao {

  public static final String INSERTARTICLECONFIG = "insertArticleConfig";
  public static final String UPDATEARTICLECONFIG = "updateArticleConfig";
  public static final String GETARTICLECONFIG = "getArticleConfig";

  @Override
  public void insertArticleConfig(ArticleConfig articleConfig) {
    Assert.assertArgumentNotNull(articleConfig, "articleConfig");

    insert(INSERTARTICLECONFIG, articleConfig);
  }

  @Override
  public void updateArticleConfig(ArticleConfig articleConfig) {
    Assert.assertArgumentNotNull(articleConfig, "articleConfig");

    update(UPDATEARTICLECONFIG, articleConfig);
  }

  @Override
  public ArticleConfig getArticleConfig(String articleUuid) {
    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("articleUuid", articleUuid);
    return selectOne(GETARTICLECONFIG, map);
  }
}
