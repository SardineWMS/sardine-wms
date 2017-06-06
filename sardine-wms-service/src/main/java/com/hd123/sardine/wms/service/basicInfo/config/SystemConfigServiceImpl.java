/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	SystemConfigServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月6日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.basicInfo.config;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.basicInfo.config.ArticleConfig;
import com.hd123.sardine.wms.api.basicInfo.config.SystemConfigService;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.dao.basicInfo.config.SystemConfigDao;

/**
 * @author zhangsai
 *
 */
public class SystemConfigServiceImpl implements SystemConfigService {

  @Autowired
  private SystemConfigDao systemConfigDao;

  @Override
  public void saveArticleConfig(ArticleConfig articleConfig) {
    Assert.assertArgumentNotNull(articleConfig, "articleConfig");

    articleConfig.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    ArticleConfig config = systemConfigDao.getArticleConfig(articleConfig.getArticleUuid());
    if (config != null)
      systemConfigDao.updateArticleConfig(articleConfig);
    else
      systemConfigDao.insertArticleConfig(articleConfig);
  }

  @Override
  public ArticleConfig getArticleConfig(String articleUuid) {
    return systemConfigDao.getArticleConfig(articleUuid);
  }
}
