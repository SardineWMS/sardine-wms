/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	SyetemConfigService.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月6日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.config;

/**
 * 系统配置接口
 * 
 * @author zhangsai
 *
 */
public interface SystemConfigService {

  /**
   * 商品配置
   * 
   * @param articleConfig
   */
  void saveArticleConfig(ArticleConfig articleConfig);

  /**
   * 根据商品ID，获取商品配置
   * 
   * @param articleUuid
   * @return 商品配置，不存在返回null
   */
  ArticleConfig getArticleConfig(String articleUuid);
}
