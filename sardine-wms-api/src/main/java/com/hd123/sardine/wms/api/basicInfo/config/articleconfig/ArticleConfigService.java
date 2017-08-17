/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	SyetemConfigService.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月6日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.config.articleconfig;

import java.util.List;

import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * 系统配置接口
 * 
 * @author zhangsai
 *
 */
public interface ArticleConfigService {
    /** 代码 类似于 */
    public static final String QUERY_ARTICLECODE_FIELD = "articleCode";
    /** 名称 类似于 */
    public static final String QUERY_FIXEDPICKBIN_FIELD = "fixedPickBin";

    /** 排序字段 代码 */
    public static final String ORDER_ARTICLECODEFIELD = "articleCode";

    /**
     * 设置商品配置
     * 
     * @param articleConfig
     *            商品配置实体，not null。
     */
    void saveArticleConfig(ArticleConfig articleConfig);

    /**
     * 根据商品UUID，获取商品配置
     * 
     * @param articleUuid
     *            商品UUID，not null。
     * @return 商品配置，不存在返回null
     */
    ArticleConfig getArticleConfig(String articleUuid);

    /**
     * 设置商品固定拣货位
     * 
     * @param articleUuid
     *            商品UUID，not null。
     * @param fixedPickBin
     *            拣货存储位，为空时清除商品固定拣货位。
     * @param version
     *            版本号，可以为 null。
     * @throws IllegalArgumentException
     *             参数异常时抛出。
     * @throws VersionConflictException
     *             版本异常时抛出。
     * @throws WMSException
     */
    void setArticleFixedPickBin(String articleUuid, String fixedPickBin, long version)
            throws IllegalArgumentException, VersionConflictException, WMSException;

    /**
     * 设置商品存储区域
     * 
     * @param articleUuid
     *            商品UUID，not null。
     * @param storageArea
     *            商品存储区域，为空时清除商品存储区域。
     * @param version
     *            版本号，可以为 null。
     * @throws IllegalArgumentException
     *             参数异常时抛出。
     * @throws VersionConflictException
     *             版本异常时抛出。
     * @throws WMSException
     */
    void setArticleStorageArea(String articleUuid, String storageArea, long version)
            throws IllegalArgumentException, VersionConflictException, WMSException;

    /**
     * 设置商品最高最低库存
     * 
     * @param articleUuid
     *            商品UUID，not null。
     * @param pickBinStockLimit
     *            最高最低库存配置，为空或对象内字段为空时，清除对应设置
     * @param version
     *            版本号，可以为 null。
     * @throws IllegalArgumentException
     *             参数异常时抛出。
     * @throws VersionConflictException
     *             版本异常时抛出。
     * @throws WMSException
     */
    void setPickBinStockLimit(String articleUuid, PickBinStockLimit pickBinStockLimit, long version)
            throws IllegalArgumentException, VersionConflictException, WMSException;

    /**
     * 分页查询商品配置
     * 
     * @param definition
     *            查询条件，not null。
     * @return 商品配置集合
     * @throws IllegalArgumentException
     *             参数异常时抛出。
     */
    PageQueryResult<ArticleConfig> queryArticleConfigquery(PageQueryDefinition definition)
            throws IllegalArgumentException;
    
    List<ArticleConfig> queryArticleConfigByArticleUuids(List<String> articleUuids);

}
