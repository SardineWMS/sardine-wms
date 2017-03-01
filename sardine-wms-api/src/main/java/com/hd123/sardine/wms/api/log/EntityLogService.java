/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	LogService.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月15日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.log;

import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * 实体日志接口
 * 
 * @author fanqingqing
 */
public interface EntityLogService {
    public static final String QUERY_ENTITYUUID_FIELD = "entityUuid";

    /**
     * 根据实体uuid查询日志
     * 
     * @param filter
     *            查询条件，not null。
     * @return 返回日志列表
     * @throws DBOperServiceException
     */
    PageQueryResult<EntityLog> query(PageQueryDefinition filter);
}
