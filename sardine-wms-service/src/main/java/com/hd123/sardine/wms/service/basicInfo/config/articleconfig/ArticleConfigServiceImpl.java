/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	SystemConfigServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月6日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.basicInfo.config.articleconfig;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.article.Article;
import com.hd123.sardine.wms.api.basicInfo.article.ArticleService;
import com.hd123.sardine.wms.api.basicInfo.bin.Bin;
import com.hd123.sardine.wms.api.basicInfo.bin.BinService;
import com.hd123.sardine.wms.api.basicInfo.bin.BinUsage;
import com.hd123.sardine.wms.api.basicInfo.config.articleconfig.ArticleConfig;
import com.hd123.sardine.wms.api.basicInfo.config.articleconfig.ArticleConfigService;
import com.hd123.sardine.wms.api.basicInfo.config.articleconfig.PickBinStockLimit;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.dao.basicInfo.config.articleconfig.ArticleConfigDao;

/**
 * @author zhangsai
 *
 */
public class ArticleConfigServiceImpl implements ArticleConfigService {

  @Autowired
  private ArticleConfigDao articleConfigDao;
  @Autowired
  private ArticleService articleService;
  @Autowired
  private BinService binService;

  @Override
  public void saveArticleConfig(ArticleConfig articleConfig) {
    Assert.assertArgumentNotNull(articleConfig, "articleConfig");

    articleConfig.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    ArticleConfig config = articleConfigDao.getArticleConfig(articleConfig.getArticle().getUuid());
    if (config != null)
      articleConfigDao.updateArticleConfig(articleConfig);
    else
      articleConfigDao.insertArticleConfig(articleConfig);
  }

  @Override
  public ArticleConfig getArticleConfig(String articleUuid) {
    if (StringUtil.isNullOrBlank(articleUuid))
      return null;
    return articleConfigDao.getArticleConfig(articleUuid);
  }

  @Override
  public void setArticleFixedPickBin(String articleUuid, String fixedPickBin, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(articleUuid, "articleUuid");

    if (StringUtil.isNullOrBlank(fixedPickBin) == false) {
      Bin bin = binService.getBinByCode(fixedPickBin);
      if (bin == null)
        throw new WMSException("固定拣货位" + fixedPickBin + "不存在");
      if (BinUsage.PickUpStorageBin.equals(bin.getUsage()) == false)
        throw new WMSException("固定拣货位只能是" + BinUsage.PickUpStorageBin.getCaption());
    }

    ArticleConfig articleConfig = articleConfigDao.getArticleConfig(articleUuid);
    if (articleConfig == null) {
      if (StringUtil.isNullOrBlank(fixedPickBin))
        return;
      Article article = articleService.get(articleUuid);
      if (article == null)
        throw new WMSException("商品不存在");
      ArticleConfig newArticleConfig = new ArticleConfig();
      newArticleConfig.setArticle(new UCN(article.getUuid(), article.getCode(), article.getName()));
      newArticleConfig.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      newArticleConfig.setFixedPickBin(fixedPickBin);
      articleConfigDao.insertArticleConfig(newArticleConfig);
      return;
    }

    PersistenceUtils.checkVersion(version, articleConfig, "商品配置",
        articleConfig.getArticle().getCode());
    articleConfig.setFixedPickBin(fixedPickBin);
    articleConfigDao.updateArticleConfig(articleConfig);
  }

  @Override
  public void setArticleStorageArea(String articleUuid, String storageArea, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(articleUuid, "articleUuid");

    ArticleConfig articleConfig = articleConfigDao.getArticleConfig(articleUuid);
    if (articleConfig == null) {
      if (StringUtil.isNullOrBlank(storageArea))
        return;
      Article article = articleService.get(articleUuid);
      if (article == null)
        throw new WMSException("商品不存在");
      ArticleConfig newArticleConfig = new ArticleConfig();
      newArticleConfig.setArticle(new UCN(article.getUuid(), article.getCode(), article.getName()));
      newArticleConfig.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      newArticleConfig.setStorageArea(storageArea);
      articleConfigDao.insertArticleConfig(newArticleConfig);
      return;
    }

    PersistenceUtils.checkVersion(version, articleConfig, "商品配置",
        articleConfig.getArticle().getCode());
    articleConfig.setStorageArea(storageArea);
    articleConfigDao.updateArticleConfig(articleConfig);
  }

  @Override
  public void setPickBinStockLimit(String articleUuid, PickBinStockLimit pickBinStockLimit,
      long version) throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(articleUuid, "articleUuid");

    verifyPickBinStockLimit(pickBinStockLimit);
    ArticleConfig articleConfig = articleConfigDao.getArticleConfig(articleUuid);
    if (articleConfig == null) {
      if (pickBinStockLimit == null)
        return;
      Article article = articleService.get(articleUuid);
      if (article == null)
        throw new WMSException("商品不存在");
      ArticleConfig newArticleConfig = new ArticleConfig();
      newArticleConfig.setArticle(new UCN(article.getUuid(), article.getCode(), article.getName()));
      newArticleConfig.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      newArticleConfig.setPickBinStockLimit(pickBinStockLimit);
      articleConfigDao.insertArticleConfig(newArticleConfig);
      return;
    }

    PersistenceUtils.checkVersion(version, articleConfig, "商品配置",
        articleConfig.getArticle().getCode());
    articleConfig.setPickBinStockLimit(pickBinStockLimit);
    articleConfigDao.updateArticleConfig(articleConfig);
  }

  private void verifyPickBinStockLimit(PickBinStockLimit pickBinStockLimit) throws WMSException {
    if (pickBinStockLimit == null)
      return;
    if (BigDecimal.ZERO.compareTo(pickBinStockLimit.getHighQty()) < 0
        && BigDecimal.ZERO.compareTo(pickBinStockLimit.getLowQty()) < 0
        && pickBinStockLimit.getHighQty().compareTo(pickBinStockLimit.getLowQty()) <= 0)
      throw new WMSException("最高库存必须大于最低库存");
    if (StringUtil.isNullOrBlank(pickBinStockLimit.getPickUpQpcStr()))
      return;

    if (pickBinStockLimit.getPickUpQpcStr()
        .matches("^1\\*\\d+(\\.\\d+)?\\*\\d+(\\.\\d+)?$") == false)
      throw new WMSException("拣货规格格式错误");
  }

  @Override
  public PageQueryResult<ArticleConfig> queryArticleConfigquery(PageQueryDefinition definition)
      throws IllegalArgumentException {
    Assert.assertArgumentNotNull(definition, "definition");

    PageQueryResult<ArticleConfig> pgr = new PageQueryResult<ArticleConfig>();
    List<ArticleConfig> list = articleConfigDao.query(definition);
    PageQueryUtil.assignPageInfo(pgr, definition);
    pgr.setRecords(list);
    return pgr;
  }

  @Override
  public List<ArticleConfig> queryArticleConfigByArticleUuids(List<String> articleUuids) {
    if (CollectionUtils.isEmpty(articleUuids))
      return new ArrayList<ArticleConfig>();
    return articleConfigDao.queryArticleConfigByArticleUuids(articleUuids);
  }
}
