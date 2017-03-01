/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	EntityLogDao.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月21日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.dao.log;

import java.util.List;

import com.hd123.sardine.wms.api.log.EntityLog;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;

/**
 * @author fanqingqing
 *
 */
public interface EntityLogDao {

    /**
     * 保存日志
     * 
     * @param log
     *            日志对象，not null。
     * @throws DBOperServiceException
     */
    void save(EntityLog log);

    /**
     * 根据实体uuid查询日志
     * 
     * @param filter
     *            查询条件，not null。
     * @return 返回日志列表
     * @throws DBOperServiceException
     * @throws IllegalArgumentException
     */
    List<EntityLog> getEntityLog(PageQueryDefinition filter);
}
