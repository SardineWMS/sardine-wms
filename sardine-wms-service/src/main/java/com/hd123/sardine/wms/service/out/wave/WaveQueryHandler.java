/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	WaveQueryHandler.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月17日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.out.wave;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.basicInfo.article.Article;
import com.hd123.sardine.wms.api.basicInfo.article.ArticleService;
import com.hd123.sardine.wms.api.basicInfo.config.articleconfig.ArticleConfig;
import com.hd123.sardine.wms.api.basicInfo.config.articleconfig.ArticleConfigService;
import com.hd123.sardine.wms.api.basicInfo.pickarea.PickArea;
import com.hd123.sardine.wms.api.basicInfo.pickarea.PickAreaService;
import com.hd123.sardine.wms.api.out.alcntc.WaveAlcNtcItem;
import com.hd123.sardine.wms.api.stock.StockMajorFilter;
import com.hd123.sardine.wms.api.stock.StockMajorInfo;
import com.hd123.sardine.wms.api.stock.StockService;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.ScopeUtils;
import com.hd123.sardine.wms.dao.out.wave.WaveBillDao;

/**
 * @author zhangsai
 *
 */
public class WaveQueryHandler {

  @Autowired
  private StockService stockService;

  @Autowired
  private ArticleService articleService;

  @Autowired
  private ArticleConfigService articleConfigService;

  @Autowired
  private PickAreaService pickAreaService;

  @Autowired
  private WaveBillDao waveBillDao;

  private List<StockMajorInfo> stockMajorInfos;

  private List<PickArea> areas;

  private List<ArticleConfig> articleConfigs;

  private List<WaveAlcNtcItem> alcNtcItems;

  private List<Article> articles;

  /**
   * 构造拣货所需的信息
   * <p>
   * 查询这些商品在存储位。拣货存储位上的库存<br>
   * 查询拣货分区<br>
   * 查询商品配置<br>
   * 查询商品<br>
   * 查询并锁定配单明细
   * 
   * @param waveBillNumber
   *          波次单号
   * @param articleUuids
   *          拣货商品ID集合
   * @throws WMSException
   */
  public void buildCache(String waveBillNumber, List<String> articleUuids) throws WMSException {
    stockMajorInfos = queryStocks(articleUuids);
    areas = queryPickAreas();
    articleConfigs = queryArticleConfigs(articleUuids);
    articles = queryArticles(articleUuids);
    alcNtcItems = waveBillDao.queryWaveAlcNtcItems(waveBillNumber, articleUuids);
    int rows = waveBillDao.updateWaveAlcNtcItemsState(waveBillNumber, articleUuids);
    if (alcNtcItems.size() != rows)
      throw new WMSException("商品发生变化，请重新启动！");
  }

  private List<StockMajorInfo> queryStocks(List<String> articleUuids) {
    if (CollectionUtils.isEmpty(articleUuids))
      return new ArrayList<StockMajorInfo>();

    StockMajorFilter filter = new StockMajorFilter();
    filter.setArticleUuids(articleUuids);
    return stockService.queryStockMajorInfo(filter);
  }

  /**
   * 查找当前上商品的存储位和拣货存储位库存
   * 
   * @param articleUuid
   *          商品id
   * @return 库存集合
   */
  public List<StockMajorInfo> findStockByArticleUuid(String articleUuid) {
    Assert.assertArgumentNotNull(articleUuid, "articleUuid");

    List<StockMajorInfo> result = new ArrayList<StockMajorInfo>();
    if (CollectionUtils.isEmpty(stockMajorInfos))
      return result;

    for (StockMajorInfo info : stockMajorInfos) {
      if (info.getArticleUuid().equals(articleUuid))
        result.add(info);
    }
    return result;
  }

  private List<Article> queryArticles(List<String> articleUuids) {
    if (CollectionUtils.isEmpty(articleUuids))
      return new ArrayList<Article>();

    return articleService.queryArticles(articleUuids);
  }

  public Article findArticleByArticleUuid(String articleUuid) {
    if (CollectionUtils.isEmpty(articles))
      return null;

    for (Article article : articles) {
      if (Objects.equals(article.getUuid(), articleUuid))
        return article;
    }
    return null;
  }

  private List<ArticleConfig> queryArticleConfigs(List<String> articleUuids) {
    if (CollectionUtils.isEmpty(articleUuids))
      return new ArrayList<ArticleConfig>();

    return articleConfigService.queryArticleConfigByArticleUuids(articleUuids);
  }

  public ArticleConfig findArticleConfigByArticleUuid(String articleUuid) {
    if (CollectionUtils.isEmpty(articleConfigs))
      return null;

    for (ArticleConfig articleConfig : articleConfigs) {
      if (Objects.equals(articleConfig.getArticle().getUuid(), articleUuid))
        return articleConfig;
    }
    return null;
  }

  private List<PickArea> queryPickAreas() {
    PageQueryDefinition definition = new PageQueryDefinition();
    definition.setPageSize(0);
    definition.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    return pickAreaService.query(definition).getRecords();
  }

  public PickArea findPickAreaByArticleUuid(String binCode) {
    if (CollectionUtils.isEmpty(areas))
      return null;

    for (PickArea area : areas) {
      if (ScopeUtils.contains(area.getBinScope(), binCode))
        return area;
    }
    return null;
  }

  public List<WaveAlcNtcItem> findWaveAlcNtcItems(String articleUuid) {
    if (CollectionUtils.isEmpty(alcNtcItems))
      return new ArrayList<WaveAlcNtcItem>();

    List<WaveAlcNtcItem> result = new ArrayList<WaveAlcNtcItem>();
    for (WaveAlcNtcItem item : alcNtcItems) {
      if (Objects.equals(item.getArticleUuid(), articleUuid))
        result.add(item);
    }
    return result;
  }
}
