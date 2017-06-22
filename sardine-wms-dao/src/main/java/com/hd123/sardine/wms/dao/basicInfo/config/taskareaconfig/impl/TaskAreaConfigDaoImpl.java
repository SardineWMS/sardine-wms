/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	TaskAreaConfigDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月15日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.config.taskareaconfig.impl;

import java.util.List;
import java.util.Map;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.config.taskareaconfig.TaskAreaConfig;
import com.hd123.sardine.wms.common.dao.NameSpaceSupport;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.dao.basicInfo.config.taskareaconfig.TaskAreaConfigDao;

/**
 * @author fanqingqing
 *
 */
public class TaskAreaConfigDaoImpl extends NameSpaceSupport implements TaskAreaConfigDao {
    public static final String MAPPER_GETBYOPERATOR = "getByOperator";

    @Override
    public void insert(TaskAreaConfig taskAreaConfig) {
        Assert.assertArgumentNotNull(taskAreaConfig, "taskAreaConfig");

        insert(MAPPER_INSERT, taskAreaConfig);
    }

    @Override
    public void removeTaskAreaConfig(String uuid, long version) {
        Assert.assertArgumentNotNull(uuid, "uuid");
        Assert.assertArgumentNotNull(version, "version");

        Map<String, Object> map = ApplicationContextUtil.map();
        map.put("uuid", uuid);
        map.put("version", version);
        int i = getSqlSession().delete(generateStatement(MAPPER_REMOVE), map);
        PersistenceUtils.optimisticVerify(i);
    }

    @Override
    public void update(TaskAreaConfig taskAreaConfig) {
        Assert.assertArgumentNotNull(taskAreaConfig, "taskAreaConfig");

        int i = getSqlSession().update(generateStatement(MAPPER_UPDATE), taskAreaConfig);
        PersistenceUtils.optimisticVerify(i);
    }

    @Override
    public TaskAreaConfig getTaskAreaConfigByOperator(String operatorUuid) {
        if (StringUtil.isNullOrBlank(operatorUuid))
            return null;

        Map<String, Object> map = ApplicationContextUtil.map();
        map.put("operatorUuid", operatorUuid);
        return getSqlSession().selectOne(generateStatement(MAPPER_GETBYOPERATOR), map);
    }

    @Override
    public List<TaskAreaConfig> query(PageQueryDefinition definition) {
        Assert.assertArgumentNotNull(definition, "definition");

        definition.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
        return getSqlSession().selectList(generateStatement(MAPPER_QUERY_BYPAGE), definition);
    }

    @Override
    public TaskAreaConfig getTaskAreaConfiByUuidg(String uuid) {
        if (StringUtil.isNullOrBlank(uuid))
            return null;

        Map<String, Object> map = ApplicationContextUtil.map();
        map.put("uuid", uuid);
        return getSqlSession().selectOne(generateStatement(MAPPER_GET), map);
    }

}
