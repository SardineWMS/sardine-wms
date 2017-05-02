/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	TaskController.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月25日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.web.inner.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.task.Task;
import com.hd123.sardine.wms.api.task.TaskService;
import com.hd123.sardine.wms.api.task.TaskState;
import com.hd123.sardine.wms.api.task.TaskType;
import com.hd123.sardine.wms.common.http.ErrorRespObject;
import com.hd123.sardine.wms.common.http.RespObject;
import com.hd123.sardine.wms.common.http.RespStatus;
import com.hd123.sardine.wms.common.query.OrderDir;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.web.BaseController;

/**
 * @author zhangsai
 *
 */
@RestController
@RequestMapping("/inner/task")
public class TaskController extends BaseController {

  @Autowired
  private TaskService taskService;

  @RequestMapping(value = "/query", method = RequestMethod.GET)
  public @ResponseBody RespObject query(
      @RequestParam(value = "page", required = false, defaultValue = "1") int page,
      @RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize,
      @RequestParam(value = "sort", required = false) String sort,
      @RequestParam(value = "order", required = false, defaultValue = "asc") String sortDirection,
      @RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "taskNo", required = false) String taskNo,
      @RequestParam(value = "taskType", required = false) String taskType,
      @RequestParam(value = "state", required = false) String state) {
    RespObject resp = new RespObject();
    try {
      ApplicationContextUtil.setCompany(getLoginCompany(token));
      PageQueryDefinition definition = new PageQueryDefinition();
      definition.setPage(page);
      definition.setPageSize(pageSize);
      definition.setSortField(sort);
      definition.setOrderDir(OrderDir.valueOf(sortDirection));
      definition.put(TaskService.QUERY_FIELD_TASKNO, taskNo);
      definition.put(TaskService.QUERY_FIELD_TASKTYPE,
          StringUtil.isNullOrBlank(taskType) ? null : TaskType.valueOf(taskType));
      definition.put(TaskService.QUERY_FIELD_STATE,
          StringUtil.isNullOrBlank(state) ? null : TaskState.valueOf(state));
      definition.setCompanyUuid(getLoginCompany(token).getUuid());
      PageQueryResult<Task> result = taskService.query(definition);
      resp.setObj(result);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("分页查询失败", e.getMessage());
    }
    return resp;
  }

}
