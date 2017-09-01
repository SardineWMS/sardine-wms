/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	TaskDao.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月12日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.task;

import java.util.List;

import com.hd123.sardine.wms.api.task.Task;
import com.hd123.sardine.wms.api.task.TaskStockItem;
import com.hd123.sardine.wms.api.task.TaskView;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;

/**
 * @author zhangsai
 *
 */
public interface TaskDao {

  void insert(Task task);

  void update(Task task);

  Task get(String uuid);

  List<TaskView> query(PageQueryDefinition definition);
  
  void insertItem(TaskStockItem stockItem);
}
