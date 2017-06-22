/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	TaskAreaConfig.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月19日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.web.basicInfo.config.taskareaconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.sardine.wms.api.basicInfo.config.taskareaconfig.TaskAreaConfig;
import com.hd123.sardine.wms.api.basicInfo.config.taskareaconfig.TaskAreaConfigService;
import com.hd123.sardine.wms.common.http.ErrorRespObject;
import com.hd123.sardine.wms.common.http.RespObject;
import com.hd123.sardine.wms.common.http.RespStatus;
import com.hd123.sardine.wms.common.query.OrderDir;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.web.base.BaseController;

/**
 * @author fanqingqing
 *
 */

@RestController
@RequestMapping("/basicinfo/taskareaconfig")
public class TaskAreaConfigControll extends BaseController {
    @Autowired
    private TaskAreaConfigService service;

    @RequestMapping(value = "/querybypage", method = RequestMethod.GET)
    public @ResponseBody RespObject query(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false,
                    defaultValue = "asc") String sortDirection,
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "operatorCode", required = false) String operatorCode,
            @RequestParam(value = "operatorName", required = false) String operatorName) {
        RespObject resp = new RespObject();
        try {
            PageQueryDefinition definition = new PageQueryDefinition();
            definition.setPage(page);
            definition.setPageSize(pageSize);
            definition.setSortField(sort);
            definition.setOrderDir(OrderDir.valueOf(sortDirection));
            definition.put(TaskAreaConfigService.QUERY_OPERATORCODE_FIELD, operatorCode);
            definition.put(TaskAreaConfigService.QUERY_OPERATORNAME_FIELD, operatorName);
            PageQueryResult<TaskAreaConfig> result = service.query(definition);

            resp.setObj(result);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("分页查询失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public @ResponseBody RespObject get(@RequestParam(value = "uuid") String uuid,
            @RequestParam(value = "token", required = true) String token) {
        RespObject resp = new RespObject();
        try {
            TaskAreaConfig taskAreaConfig = service.getTaskAreaConfig(uuid);
            resp.setObj(taskAreaConfig);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("查询失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/savenew", method = RequestMethod.POST)
    public @ResponseBody RespObject saveNew(
            @RequestParam(value = "token", required = true) String token,
            @RequestBody TaskAreaConfig taskAreaConfig) {
        RespObject resp = new RespObject();
        try {
            taskAreaConfig.setCompanyUuid(getLoginCompany(token).getUuid());
            service.saveNew(taskAreaConfig);
            // resp.setObj(supplierUuid);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("新增作业区域配置失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/savemodify", method = RequestMethod.PUT)
    public @ResponseBody RespObject saveModify(
            @RequestParam(value = "token", required = true) String token,
            @RequestBody TaskAreaConfig taskAreaConfig) {
        RespObject resp = new RespObject();
        try {
            service.saveModify(taskAreaConfig);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("编辑作业区域配置失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/remove", method = RequestMethod.DELETE)
    public @ResponseBody RespObject remove(
            @RequestParam(value = "uuid", required = false) String uuid,
            @RequestParam(value = "token", required = false) String token,
            @RequestParam(value = "version", required = false) long version) {
        RespObject resp = new RespObject();
        try {
            service.remove(uuid, version);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("删除作业区域配置失败", e.getMessage());
        }
        return resp;
    }
}
