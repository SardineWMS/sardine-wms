/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	CarrierController.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月6日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.web.tms.carrier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.sardine.wms.api.tms.carrier.Carrier;
import com.hd123.sardine.wms.api.tms.carrier.CarrierService;
import com.hd123.sardine.wms.common.exception.NotLoginInfoException;
import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.http.ErrorRespObject;
import com.hd123.sardine.wms.common.http.RespObject;
import com.hd123.sardine.wms.common.http.RespStatus;
import com.hd123.sardine.wms.common.query.OrderDir;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.web.base.BaseController;

/**
 * @author yangwenzhu
 *
 */
@RestController
@RequestMapping(value = "/tms/carrier")
public class CarrierController extends BaseController {

    @Autowired
    private CarrierService service;

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public @ResponseBody RespObject insert(
            @RequestParam(value = "token", required = true) String token,
            @RequestBody Carrier carrier) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            ApplicationContextUtil.setOperateContext(getOperateContext(token));
            carrier.setCompanyUuid(getLoginCompany(token).getUuid());
            String uuid = service.saveNew(carrier);
            resp.setObj(uuid);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (IllegalArgumentException e) {
            return new ErrorRespObject("参数异常", e.getMessage());
        } catch (Exception e) {
            return new ErrorRespObject("新增承运商失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public @ResponseBody RespObject update(
            @RequestParam(value = "token", required = true) String token,
            @RequestBody Carrier carrier) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            ApplicationContextUtil.setOperateContext(getOperateContext(token));
            service.saveModify(carrier);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (IllegalArgumentException e) {
            return new ErrorRespObject("参数异常", e.getMessage());
        } catch (Exception e) {
            return new ErrorRespObject("修改承运商失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/remove", method = RequestMethod.DELETE)
    public @ResponseBody RespObject remove(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "uuid", required = true) String uuid,
            @RequestParam(value = "version", required = true) long version) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            ApplicationContextUtil.setOperateContext(getOperateContext(token));
            service.remove(uuid, version);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (VersionConflictException e) {
            return new ErrorRespObject("该承运商已被其他用户修改", e.getMessage());
        } catch (NotLoginInfoException e) {
            return new ErrorRespObject("登录信息为空，请重新登录", e.getMessage());
        } catch (IllegalArgumentException e) {
            return new ErrorRespObject("参数异常", e.getMessage());
        } catch (Exception e) {
            return new ErrorRespObject("删除承运商失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/online", method = RequestMethod.PUT)
    public @ResponseBody RespObject online(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "uuid", required = true) String uuid,
            @RequestParam(value = "version", required = true) long version) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            ApplicationContextUtil.setOperateContext(getOperateContext(token));
            service.online(uuid, version);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (VersionConflictException e) {
            return new ErrorRespObject("该承运商已被其他用户修改", e.getMessage());
        } catch (NotLoginInfoException e) {
            return new ErrorRespObject("登录信息为空，请重新登录", e.getMessage());
        } catch (IllegalArgumentException e) {
            return new ErrorRespObject("参数异常", e.getMessage());
        } catch (Exception e) {
            return new ErrorRespObject("启用承运商失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/offline", method = RequestMethod.PUT)
    public @ResponseBody RespObject offline(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "uuid", required = true) String uuid,
            @RequestParam(value = "version", required = true) long version) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            ApplicationContextUtil.setOperateContext(getOperateContext(token));
            service.offline(uuid, version);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (VersionConflictException e) {
            return new ErrorRespObject("该承运商已被其他用户修改", e.getMessage());
        } catch (NotLoginInfoException e) {
            return new ErrorRespObject("登录信息为空，请重新登录", e.getMessage());
        } catch (IllegalArgumentException e) {
            return new ErrorRespObject("参数异常", e.getMessage());
        } catch (Exception e) {
            return new ErrorRespObject("禁用承运商失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public @ResponseBody RespObject query(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false,
                    defaultValue = "asc") String sortDirection,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "name", required = false) String name) {
        RespObject resp = new RespObject();
        try {
            PageQueryDefinition definition = new PageQueryDefinition();
            definition.setPage(page);
            definition.setPageSize(pageSize);
            definition.setSortField(sort);
            definition.setOrderDir(OrderDir.valueOf(sortDirection));
            definition.setCompanyUuid(getLoginCompany(token).getUuid());
            definition.put(CarrierService.QUERY_CODE_LIKE, code);
            definition.put(CarrierService.QUERY_NAME_EQUALS, name);

            PageQueryResult<Carrier> result = service.query(definition);
            resp.setObj(result);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("分页查询承运商失败", e.getMessage());
        }
        return resp;

    }

}
