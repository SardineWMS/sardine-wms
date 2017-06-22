/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ArticleConfig.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月6日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.config.articleconfig;

import java.io.Serializable;

import com.hd123.sardine.wms.api.basicInfo.article.ArticlePutawayBin;
import com.hd123.sardine.wms.common.entity.HasVersion;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * 商品配置
 * <p>
 * <ul>
 * <li>商品上架货位</li>
 * <li>商品固定拣货位</li>
 * <li>商品存储区域</li>
 * </ul>
 * 
 * @author zhangsai
 *
 */
public class ArticleConfig implements Serializable, HasVersion {
    private static final long serialVersionUID = 1333088523989204954L;

    private UCN article;
    private String fixedPickBin;
    private ArticlePutawayBin putawayBin;
    private String storageArea;
    private String companyUuid;
    private PickBinStockLimit pickBinStockLimit;
    private long version = HasVersion.START_VERSION;

    public UCN getArticle() {
        return article;
    }

    public void setArticle(UCN article) {
        this.article = article;
    }

    public String getFixedPickBin() {
        return fixedPickBin;
    }

    public void setFixedPickBin(String fixedPickBin) {
        this.fixedPickBin = fixedPickBin;
    }

    public ArticlePutawayBin getPutawayBin() {
        return putawayBin;
    }

    public void setPutawayBin(ArticlePutawayBin putawayBin) {
        this.putawayBin = putawayBin;
    }

    public String getStorageArea() {
        return storageArea;
    }

    public void setStorageArea(String storageArea) {
        this.storageArea = storageArea;
    }

    public String getCompanyUuid() {
        return companyUuid;
    }

    public void setCompanyUuid(String companyUuid) {
        this.companyUuid = companyUuid;
    }

    public PickBinStockLimit getPickBinStockLimit() {
        return pickBinStockLimit;
    }

    public void setPickBinStockLimit(PickBinStockLimit pickBinStockLimit) {
        this.pickBinStockLimit = pickBinStockLimit;
    }

    @Override
    public long getVersion() {
        return version;
    }

    @Override
    public void setVersion(long version) throws UnsupportedOperationException {
        this.version = version;
    }
}
