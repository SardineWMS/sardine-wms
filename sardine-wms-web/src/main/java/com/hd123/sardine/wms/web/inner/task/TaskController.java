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
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.out.pickup.PickUpBillService;
import com.hd123.sardine.wms.api.out.rpl.RplBillService;
import com.hd123.sardine.wms.api.stock.StockService;
import com.hd123.sardine.wms.api.task.ArticleMoveRule;
import com.hd123.sardine.wms.api.task.ContainerMoveRule;
import com.hd123.sardine.wms.api.task.Task;
import com.hd123.sardine.wms.api.task.TaskService;
import com.hd123.sardine.wms.api.task.TaskType;
import com.hd123.sardine.wms.common.exception.NotLoginInfoException;
import com.hd123.sardine.wms.common.exception.WMSException;
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
    private RplBillService rplBillService;

    @Autowired
    private PickUpBillService pickUpBillService;

    @Autowired
    private StockService stockService;

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public @ResponseBody RespObject query(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false,
                    defaultValue = "asc") String sortDirection,
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "articleCode", required = false) String articleCode,
            @RequestParam(value = "taskType", required = true) String taskType,
            @RequestBody List<String> states) {
        RespObject resp = new RespObject();
        try {
            PageQueryDefinition definition = new PageQueryDefinition();
            definition.setPage(page);
            definition.setPageSize(pageSize);
            definition.setSortField(sort);
            definition.setOrderDir(OrderDir.valueOf(sortDirection));
            if (StringUtil.isNullOrBlank(taskType) == false)
                definition.put(TaskService.QUERY_FIELD_TASKTYPE, TaskType.valueOf(taskType));
            definition.put(TaskService.QUERY_FIELD_STATES, states);
            definition.put(TaskService.QUERY_FIELD_ARTICLECODE, articleCode);
            definition.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
            PageQueryResult<?> result = null;
          //  if (TaskType.Pick.name().equals(taskType))
                result = pickUpBillService.query(definition);
//            else
//                result = taskService.query(definition);
            resp.setObj(result);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("分页查询失败：" + e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/queryStocks", method = RequestMethod.GET)
    public @ResponseBody RespObject queryStocks(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "articleCode", required = true) String articleCode) {
        RespObject resp = new RespObject();

        try {
            // StockFilter stockFilter = new StockFilter();
            // stockFilter.setPageSize(0);
            // stockFilter.setArticleCode(articleCode);
            // List<StockExtendInfo> infos =
            // stockService.queryStockExtendInfo(stockFilter);
            // resp.setObj(infos);
        } catch (Exception e) {
            return new ErrorRespObject("分页查询失败：" + e.getMessage());
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
            // taskService.saveArticleMoveTask(articleMoveRules);
        } catch (Exception e) {
            return new ErrorRespObject("保存移库规则失败：" + e.getMessage());
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
            // taskService.saveAndMoveArticleMoveTask(articleMoveRules);
        } catch (Exception e) {
            return new ErrorRespObject("保存并移库失败：" + e.getMessage());
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
            // taskService.saveContainerMoveTask(containereMoveRules);
        } catch (Exception e) {
            return new ErrorRespObject("保存容器移库规则失败：" + e.getMessage());
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
            // taskService.saveAndMoveContainerMoveTask(containereMoveRules);
        } catch (Exception e) {
            return new ErrorRespObject("保存并移库失败：" + e.getMessage());
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
            // taskService.articleMove(uuid, version, realQty);
        } catch (Exception e) {
            return new ErrorRespObject("保存并移库失败：" + e.getMessage());
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
            // taskService.containerMove(uuid, version);
        } catch (Exception e) {
            return new ErrorRespObject("保存并移库失败：" + e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/abort", method = RequestMethod.PUT)
    public @ResponseBody RespObject abort(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "uuid", required = true) String uuid,
            @RequestParam(value = "version", required = true) long version) {
        RespObject resp = new RespObject();
        try {
            // taskService.abort(uuid, version);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (NotLoginInfoException e) {
            return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
        } catch (Exception e) {
            return new ErrorRespObject("作废指令失败：" + e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/putaway", method = RequestMethod.PUT)
    public @ResponseBody RespObject putaway(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "uuid", required = true) String uuid,
            @RequestParam(value = "toBinCode", required = true) String toBinCode,
            @RequestParam(value = "toContainerBarcode", required = false) String toContainerBarcode,
            @RequestParam(value = "version", required = true) long version) {
        RespObject resp = new RespObject();
        try {
            // taskService.putaway(uuid, versioabn, toBinCode,
            // toContainerBarcode);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (NotLoginInfoException e) {
            return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
        } catch (Exception e) {
            return new ErrorRespObject("收货上架失败：" + e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/rpl", method = RequestMethod.PUT)
    public @ResponseBody RespObject rpl(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "rplBillUuid", required = true) String rplBillUuid,
            @RequestParam(value = "version", required = true) long version) {
        RespObject resp = new RespObject();

        try {
            // rplBillService.rpl(rplBillUuid, version, rpler);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (NotLoginInfoException e) {
            return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
        } catch (Exception e) {
            return new ErrorRespObject("补货失败：" + e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/rtnshelf", method = RequestMethod.PUT)
    public @ResponseBody RespObject rtnShelf(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "uuid", required = true) String uuid,
            @RequestParam(value = "toBinCode", required = true) String toBinCode,
            @RequestParam(value = "toContainerBarcode", required = false) String toContainerBarcode,
            @RequestParam(value = "version", required = true) long version,
            @RequestParam(value = "qty", required = true) BigDecimal qty) {
        RespObject resp = new RespObject();
        try {
            // taskService.rtnShelf(uuid, version, toBinCode,
            // toContainerBarcode, qty);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (NotLoginInfoException e) {
            return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
        } catch (Exception e) {
            return new ErrorRespObject("收货上架失败：" + e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/execute", method = RequestMethod.POST)
    public @ResponseBody RespObject execute(
            @RequestParam(value = "uuid", required = true) String uuid,
            @RequestParam(value = "version", required = true) long version) {
        RespObject resp = new RespObject();
        try {
            Task task = taskService.get(uuid);
            if (Objects.isNull(task))
                throw new WMSException(MessageFormat.format("要执行的指令{0}不存在", uuid));
            if (TaskType.Putaway.equals(task.getType()))
                taskService.putaway(uuid, version, task.getToBinCode(),
                        task.getToContainerBarcode());
            if (TaskType.Move.equals(task.getType()))
                taskService.move(uuid, version, task.getQty());
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
            return resp;
        } catch (Exception e) {
            return new ErrorRespObject("指令执行失败：" + e.getMessage());
        }

    }

    @RequestMapping(value = "/batchpick", method = RequestMethod.POST)
    public @ResponseBody RespObject pick(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "toBinCode", required = true) String toBinCode,
            @RequestParam(value = "toContainerBarcode", required = false) String toContainerBarcode,
            @RequestBody List<String> pickItemUuids) {
        RespObject resp = new RespObject();
        try {
            pickUpBillService.pick(pickItemUuids, toBinCode, toContainerBarcode,
                    ApplicationContextUtil.getLoginUser());
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (NotLoginInfoException e) {
            return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
        } catch (Exception e) {
            return new ErrorRespObject("拣货失败：" + e.getMessage());
        }
        return resp;
    }

}
