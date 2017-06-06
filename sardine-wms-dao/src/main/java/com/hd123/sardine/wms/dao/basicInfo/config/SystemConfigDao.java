/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	SystemConfigDao.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月6日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.config;

import com.hd123.sardine.wms.api.basicInfo.config.ArticleConfig;

/**
 * @author zhangsai
 *
 */
public interface SystemConfigDao {

  void insertArticleConfig(ArticleConfig articleConfig);

  void updateArticleConfig(ArticleConfig articleConfig);

  ArticleConfig getArticleConfig(String articleUuid);
}
