/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	PickAreaConfigService.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月13日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.config.pickareastorageareaconfig;

import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * @author fanqingqing
 *
 */
public interface PickAreaStorageAreaConfigService {
    /** 代码 等于 */
    public static final String QUERY_PICKAREACODE_FIELD = "code";
    /** 名称 等于 */
    public static final String QUERY_PICKAREANAME_FIELD = "name";

    /**
     * 设置拣货分区存储位配置
     * 
     * @param pickAreaUuid
     *            拣货分区UUID，not null。
     * @param storageArea
     *            存储区域，为空时清除存储区域配置
     * @param version
     *            版本号，可以为null。
     * @throws IllegalArgumentException
     *             参数异常时抛出。
     * @throws VersionConflictException
     *             版本异常时抛出。
     * @throws WMSException
     */
    void setPickAreaStorageAreaConfig(String pickAreaUuid, String storageArea, long version)
            throws IllegalArgumentException, VersionConflictException, WMSException;

    /**
     * 分页查询拣货分区存储区域配置
     * 
     * @param definition
     *            查询条件，not null。
     * @return 存储区域配置集合
     * @throws IllegalArgumentException
     */
    PageQueryResult<PickAreaStorageAreaConfig> query(PageQueryDefinition definition)
            throws IllegalArgumentException;

    PickAreaStorageAreaConfig getPickAreaStorageAreaConfig(String pickAreaUuid);

    /**
     * 根据拣货分区UUID查询配置的存储区域
     * 
     * @param pickAreaUuid
     *            拣货分区UUID，not null。
     * @return 存储区域，不存在时返回null。
     */
    String getStorageAreaByPickArea(String pickAreaUuid);

    /**
     * 根据商品固定拣货位查询所属拣货分区配置的存储区域
     * 
     * @param fixedPickBin
     *            固定拣货位，not null。
     * @return 存储区域，不存在时返回null。
     */
    String getStorageAreaByFixedPickBin(String fixedPickBin);

}
