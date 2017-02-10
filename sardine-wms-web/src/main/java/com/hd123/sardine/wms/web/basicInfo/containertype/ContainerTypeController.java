/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	ContainerTypeController.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月7日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.web.basicInfo.containertype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.sardine.wms.api.basicInfo.containertype.ContainerType;
import com.hd123.sardine.wms.api.basicInfo.containertype.ContainerTypeService;
import com.hd123.sardine.wms.common.http.ErrorRespObject;
import com.hd123.sardine.wms.common.http.RespObject;
import com.hd123.sardine.wms.common.http.RespStatus;
import com.hd123.sardine.wms.common.query.OrderDir;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.web.BaseController;

/**
 * @author fanqingqing
 *
 */
@RestController
@RequestMapping("/basicinfo/containertype")
public class ContainerTypeController extends BaseController {

    @Autowired
    private ContainerTypeService containerTypeService;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public @ResponseBody RespObject get(@RequestParam(value = "uuid") String uuid) {
        RespObject resp = new RespObject();
        try {
            ContainerType containerType = containerTypeService.get(uuid);
            resp.setObj(containerType);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("查询失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/querybypage", method = RequestMethod.GET)
    public @ResponseBody RespObject query(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false,
                    defaultValue = "asc") String sortDirection,
            @RequestParam(value = "token", required = false) String token,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "name", required = false) String name) {
        RespObject resp = new RespObject();
        try {
            PageQueryDefinition definition = new PageQueryDefinition();
            definition.setPage(page);
            definition.setPageSize(pageSize);
            definition.setSortField(sort);
            definition.setOrderDir(OrderDir.valueOf(sortDirection));
            definition.put(ContainerTypeService.QUERY_CODE_FIELD, code);
            definition.put(ContainerTypeService.QUERY_NAME_FIELD, name);
            PageQueryResult<ContainerType> result = containerTypeService.query(definition);

            resp.setObj(result);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("分页查询失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/savenew", method = RequestMethod.POST)
    public @ResponseBody RespObject saveNew(@RequestBody ContainerType containerType) {
        RespObject resp = new RespObject();
        try {
            containerType.setCompanyUuid(getLoginCompany(containerType.getToken()).getUuid());
            String containerTypeUuid = containerTypeService.saveNew(containerType,
                    getOperateContext(containerType.getToken()));
            resp.setObj(containerTypeUuid);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("新增容器类型失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/savemodify", method = RequestMethod.PUT)
    public @ResponseBody RespObject saveModify(@RequestBody ContainerType containerType) {
        RespObject resp = new RespObject();
        try {
            containerTypeService.saveModify(containerType,
                    getOperateContext(containerType.getToken()));
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("编辑容器类型失败", e.getMessage());
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
            containerTypeService.remove(uuid, version, getOperateContext(token));
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("删除容器类型失败", e.getMessage());
        }
        return resp;
    }

}
