/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	CategoryConfigService.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月13日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.config.categorystorageareaconfig;

import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * @author fanqingqing
 *
 */
public interface CategoryStorageAreaConfigService {
    /** 代码 等于 */
    public static final String QUERY_CATEGORYCODE_FIELD = "categoryCode";
    /** 名称 等于 */
    public static final String QUERY_CATEGORYNAME_FIELD = "categoryName";

    /** 上级代码 等于 */
    public static final String QUERY_CATEGORYUPPERCODE_FIELD = "categoryUpperCode";

    /**
     * 设置商品类别存储区域配置
     * 
     * @param categoryUuid
     *            商品类别UUID，not null。
     * @param storageArea
     *            存储区域，为空时清除存储区域设置
     * @param version
     *            版本号，可以为null。
     * @throws IllegalArgumentException
     *             参数异常时抛出。
     * @throws VersionConflictException
     *             版本异常时抛出
     * @throws WMSException
     */
    void setCategoryStorageAreaConfig(String categoryUuid, String storageArea, long version)
            throws IllegalArgumentException, VersionConflictException, WMSException;

    /**
     * 分页查询商品类别存储区域配置
     * 
     * @param definition
     *            查询条件，not null。
     * @return 商品类别存储区域配置集合
     * @throws IllegalArgumentException
     *             参数异常时抛出。
     */
    PageQueryResult<CategoryStorageAreaConfig> query(PageQueryDefinition definition)
            throws IllegalArgumentException;

    CategoryStorageAreaConfig getCategoryStorageAreaConfig(String categoryUuid);

    /**
     * 根据商品类别UUID查询存储区域
     * 
     * @param categoryUuid
     *            商品类别UUID，not null。
     * @return 存储区域
     */
    String getStorageAreaByCategory(String categoryUuid);

    /**
     * 根据商品查询商品类别配置额存储区域（递归查询直接父级商品类别直到顶级类别）
     * 
     * @param articleUuid
     *            商品UUID,not null。
     * @return 存储区域，不存在是返回null。
     */
    String getStorageAreaByArticle(String articleUuid);

}
