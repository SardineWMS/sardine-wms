/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	EntityLogDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月21日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.dao.log.impl;

import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.log.EntityLog;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.dao.log.EntityLogDao;

/**
 * @author fanqingqing
 *
 */
public class EntityLogDaoImpl extends SqlSessionDaoSupport implements EntityLogDao {
    private static String INSERT_ENTITYLOG = "insert_EntityLog";
    private static String SELECT_ENTITYLOG = "select_entityLog";

    @Override
    public void save(EntityLog log) {
        Assert.assertArgumentNotNull(log, "log");

        log.setUuid(UUIDGenerator.genUUID());
        getSqlSession().insert(this.getClass().getName() + "." + INSERT_ENTITYLOG, log);
    }

    @Override
    public List<EntityLog> getEntityLog(PageQueryDefinition filter) {
        List<EntityLog> list = getSqlSession()
                .selectList(this.getClass().getName() + "." + SELECT_ENTITYLOG, filter);
        return list;

    }

}
