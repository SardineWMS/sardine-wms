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

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.stock.StockExtendInfo;
import com.hd123.sardine.wms.api.stock.StockFilter;
import com.hd123.sardine.wms.api.stock.StockService;
import com.hd123.sardine.wms.api.task.ArticleMoveRule;
import com.hd123.sardine.wms.api.task.ContainerMoveRule;
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
import com.hd123.sardine.wms.web.base.BaseController;

/**
 * @author zhangsai
 *
 */
@RestController
@RequestMapping("/inner/task")
public class TaskController extends BaseController {

  @Autowired
  private TaskService taskService;

  @Autowired
  private StockService stockService;

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

  @RequestMapping(value = "/queryStocks", method = RequestMethod.GET)
  public @ResponseBody RespObject queryStocks(
      @RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "articleCode", required = true) String articleCode) {
    RespObject resp = new RespObject();

    try {
      StockFilter stockFilter = new StockFilter();
      stockFilter.setPageSize(0);
      stockFilter.setArticleCode(articleCode);
      List<StockExtendInfo> infos = stockService.queryStocks(stockFilter);
      resp.setObj(infos);
    } catch (Exception e) {
      return new ErrorRespObject("分页查询失败", e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/saveArticleMoveRule", method = RequestMethod.POST)
  public @ResponseBody RespObject saveArticleMoveRule(
      @RequestParam(value = "token", required = true) String token,
      @RequestBody List<ArticleMoveRule> articleMoveRules) {
    RespObject resp = new RespObject();
    if (CollectionUtils.isEmpty(articleMoveRules))
      return resp;

    try {
      taskService.saveArticleMoveTask(articleMoveRules);
    } catch (Exception e) {
      return new ErrorRespObject("保存移库规则失败！", e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/saveAndMoveArticleMoveRule", method = RequestMethod.POST)
  public @ResponseBody RespObject saveAndMoveArticleMoveRule(
      @RequestParam(value = "token", required = true) String token,
      @RequestBody List<ArticleMoveRule> articleMoveRules) {
    RespObject resp = new RespObject();
    if (CollectionUtils.isEmpty(articleMoveRules))
      return resp;

    try {
      taskService.saveAndMoveArticleMoveTask(articleMoveRules);
    } catch (Exception e) {
      return new ErrorRespObject("保存并移库失败！", e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/saveContainerMoveRule", method = RequestMethod.POST)
  public @ResponseBody RespObject saveContainerMoveRule(
      @RequestParam(value = "token", required = true) String token,
      @RequestBody List<ContainerMoveRule> containereMoveRules) {
    RespObject resp = new RespObject();
    if (CollectionUtils.isEmpty(containereMoveRules))
      return resp;

    try {
      taskService.saveContainerMoveTask(containereMoveRules);
    } catch (Exception e) {
      return new ErrorRespObject("保存容器移库规则失败！", e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/saveAndMoveContainerMoveRule", method = RequestMethod.POST)
  public @ResponseBody RespObject saveAndMoveContainerMoveRule(
      @RequestParam(value = "token", required = true) String token,
      @RequestBody List<ContainerMoveRule> containereMoveRules) {
    RespObject resp = new RespObject();
    if (CollectionUtils.isEmpty(containereMoveRules))
      return resp;

    try {
      taskService.saveAndMoveContainerMoveTask(containereMoveRules);
    } catch (Exception e) {
      return new ErrorRespObject("保存并移库失败！", e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/articleMove", method = RequestMethod.PUT)
  public @ResponseBody RespObject articleMove(
      @RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "uuid", required = true) String uuid,
      @RequestParam(value = "version", required = true) int version,
      @RequestParam(value = "realQty", required = true) BigDecimal realQty) {
    RespObject resp = new RespObject();

    try {
      taskService.articleMove(uuid, version, realQty);
    } catch (Exception e) {
      return new ErrorRespObject("保存并移库失败！", e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/containerMove", method = RequestMethod.PUT)
  public @ResponseBody RespObject containerMove(
      @RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "uuid", required = true) String uuid,
      @RequestParam(value = "version", required = true) int version) {
    RespObject resp = new RespObject();

    try {
      taskService.containerMove(uuid, version);
    } catch (Exception e) {
      return new ErrorRespObject("保存并移库失败！", e.getMessage());
    }
    return resp;
  }
}
