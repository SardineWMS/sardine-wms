/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	TaskDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月20日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.task.impl;

import java.util.List;
import java.util.Map;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.task.Task;
import com.hd123.sardine.wms.api.task.TaskStockItem;
import com.hd123.sardine.wms.api.task.TaskView;
import com.hd123.sardine.wms.common.dao.NameSpaceSupport;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.dao.task.TaskDao;

/**
 * @author zhangsai
 *
 */
public class TaskDaoImpl extends NameSpaceSupport implements TaskDao {

    @Override
    public void insert(Task task) {
        Assert.assertArgumentNotNull(task, "task");

        insert(MAPPER_INSERT, task);
    }

    @Override
    public void update(Task task) {
        Assert.assertArgumentNotNull(task, "task");

        int updateRows = update(MAPPER_UPDATE, task);
        PersistenceUtils.optimisticVerify(updateRows);
    }

    @Override
    public Task get(String uuid) {
        Map<String, Object> map = ApplicationContextUtil.map();
        map.put("uuid", uuid);
        return selectOne(MAPPER_GET, map);
    }

    @Override
    public List<TaskView> query(PageQueryDefinition definition) {
        Assert.assertArgumentNotNull(definition, "definition");
        return selectList(MAPPER_QUERY_BYLIST, definition);
    }

    @Override
    public void insertItem(TaskStockItem stockItem) {
        Assert.assertArgumentNotNull(stockItem, "stockItem");

        insert(MAPPER_INSERT_ITEM, stockItem);
    }
}
