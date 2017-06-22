/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	TaskAreaConfig.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月13日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.config.taskareaconfig;

import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * @author fanqingqing
 *
 */
public interface TaskAreaConfigService {
    /** 代码 等于 */
    public static final String QUERY_OPERATORCODE_FIELD = "operatorCode";
    /** 名称 等于 */
    public static final String QUERY_OPERATORNAME_FIELD = "operatorName";

    /**
     * 新增作业区域配置
     * 
     * @param taskAreaConfig
     *            作业区域配置，not null。
     * @throws IllegalArgumentException
     *             参数异常时抛出。
     * @throws WMSException
     */
    String saveNew(TaskAreaConfig taskAreaConfig) throws IllegalArgumentException, WMSException;

    /**
     * 修改作业区域配置
     * 
     * @param taskAreaConfig
     *            作业区域配置，not null。
     * @throws IllegalArgumentException
     *             参数异常时抛出。
     * @throws VersionConflictException
     *             版本异常时抛出。
     * @throws WMSException
     */
    void saveModify(TaskAreaConfig taskAreaConfig)
            throws IllegalArgumentException, VersionConflictException, WMSException;

    /**
     * 删除作业区域配置
     * 
     * @param taskAreaConfigUuid
     *            作业区域UUID，not null。
     * @param version
     *            版本号，not null。
     * @throws IllegalArgumentException
     *             参数异常时抛出。
     * @throws VersionConflictException
     *             版本异常时抛出。
     * @throws WMSException
     */
    void remove(String taskAreaConfigUuid, long version)
            throws IllegalArgumentException, VersionConflictException, WMSException;

    /**
     * 分页查询作业区域配置
     * 
     * @param definition
     *            搜索条件，not null。
     * @return 作业区域集合
     * @throws IllegalArgumentException
     *             参数异常时抛出。
     */
    PageQueryResult<TaskAreaConfig> query(PageQueryDefinition definition)
            throws IllegalArgumentException;

    /**
     * 根据操作人UUID查询作业区域
     * 
     * @param operatorUuid
     *            操作人UUID，not null。
     * @return 作业区域
     */
    String getTaskAreaByOperator(String operatorUuid);

    /**
     * 根据UUID查询作业区域
     * 
     * @param uuid
     *            作业区域配置UUID，not null。
     * @return 作业区域
     */
    TaskAreaConfig getTaskAreaConfig(String uuid);

}
