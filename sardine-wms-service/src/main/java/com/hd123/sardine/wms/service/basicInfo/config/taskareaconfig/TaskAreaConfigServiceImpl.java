/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	TaskAreaConfigServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月15日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.service.basicInfo.config.taskareaconfig;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.config.taskareaconfig.TaskAreaConfig;
import com.hd123.sardine.wms.api.basicInfo.config.taskareaconfig.TaskAreaConfigService;
import com.hd123.sardine.wms.api.ia.user.User;
import com.hd123.sardine.wms.api.ia.user.UserService;
import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.dao.basicInfo.config.taskareaconfig.TaskAreaConfigDao;

/**
 * @author fanqingqing
 *
 */
public class TaskAreaConfigServiceImpl implements TaskAreaConfigService {

    @Autowired
    private TaskAreaConfigDao dao;
    @Autowired
    private UserService userService;

    @Override
    public String saveNew(TaskAreaConfig taskAreaConfig)
            throws IllegalArgumentException, WMSException {
        Assert.assertArgumentNotNull(taskAreaConfig, "taskAreaConfig");
        taskAreaConfig.validate();

        User user = userService.get(taskAreaConfig.getOperator().getUuid());
        if (user == null)
            throw new WMSException("操作人不存在");
        TaskAreaConfig oldTaskAreaConfig = dao.getTaskAreaConfigByOperator(taskAreaConfig.getOperator().getUuid());
        if (oldTaskAreaConfig != null)
            throw new WMSException("操作人已设置了作业范围" + oldTaskAreaConfig.getTaskArea() + "，不能重复设置");
        taskAreaConfig.setUuid(UUIDGenerator.genUUID());
        taskAreaConfig.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
        dao.insert(taskAreaConfig);
        return taskAreaConfig.getUuid();
    }

    @Override
    public void saveModify(TaskAreaConfig taskAreaConfig)
            throws IllegalArgumentException, VersionConflictException, WMSException {
        Assert.assertArgumentNotNull(taskAreaConfig, "taskAreaConfig");
        taskAreaConfig.validate();

        User user = userService.get(taskAreaConfig.getOperator().getUuid());
        if (user == null)
            throw new WMSException("操作人不存在");
        TaskAreaConfig oldTaskAreaConfig = dao.getTaskAreaConfigByOperator(taskAreaConfig.getOperator().getUuid());

        PersistenceUtils.checkVersion(taskAreaConfig.getVersion(), oldTaskAreaConfig, "作业区域配置",
                taskAreaConfig.getOperator().getCode());

        if (oldTaskAreaConfig != null
                && ObjectUtils.notEqual(taskAreaConfig.getUuid(), oldTaskAreaConfig.getUuid()))
            throw new WMSException("操作人已设置了作业范围" + oldTaskAreaConfig.getTaskArea() + "，不能重复设置");
        dao.update(taskAreaConfig);
    }

    @Override
    public void remove(String taskAreaConfigUuid, long version)
            throws IllegalArgumentException, VersionConflictException, WMSException {
        Assert.assertArgumentNotNull(taskAreaConfigUuid, "taskAreaConfigUuid");
        Assert.assertArgumentNotNull(version, "version");
        dao.removeTaskAreaConfig(taskAreaConfigUuid, version);
    }

    @Override
    public PageQueryResult<TaskAreaConfig> query(PageQueryDefinition definition)
            throws IllegalArgumentException {
        Assert.assertArgumentNotNull(definition, "definition");

        PageQueryResult<TaskAreaConfig> pgr = new PageQueryResult<TaskAreaConfig>();
        List<TaskAreaConfig> list = dao.query(definition);
        PageQueryUtil.assignPageInfo(pgr, definition);
        pgr.setRecords(list);
        return pgr;
    }

    @Override
    public String getTaskAreaByOperator(String operatorUuid) {
        if (StringUtil.isNullOrBlank(operatorUuid))
            return null;
        TaskAreaConfig taskAreaConfig = dao.getTaskAreaConfigByOperator(operatorUuid);
        if (taskAreaConfig == null)
            return null;
        return taskAreaConfig.getTaskArea();
    }

    @Override
    public TaskAreaConfig getTaskAreaConfig(String uuid) {
        if (StringUtil.isNullOrBlank(uuid))
            return null;
        TaskAreaConfig taskAreaConfig = dao.getTaskAreaConfiByUuidg(uuid);
        return taskAreaConfig;
    }

}
