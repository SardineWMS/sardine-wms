/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	VehicleController.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月8日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.web.tms.vehicle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.tms.vehicle.Vehicle;
import com.hd123.sardine.wms.api.tms.vehicle.VehicleService;
import com.hd123.sardine.wms.api.tms.vehicle.VehicleState;
import com.hd123.sardine.wms.common.exception.NotLoginInfoException;
import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.http.ErrorRespObject;
import com.hd123.sardine.wms.common.http.RespObject;
import com.hd123.sardine.wms.common.http.RespStatus;
import com.hd123.sardine.wms.common.query.OrderDir;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.web.BaseController;

/**
 * @author yangwenzhu
 *
 */
@RestController
@RequestMapping(value = "/tms/vehicle")
public class VehicleController extends BaseController {
    @Autowired
    private VehicleService service;

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public @ResponseBody RespObject query(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false,
                    defaultValue = "asc") String sortDirection,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "vehicleNo", required = false) String vehicleNo,
            @RequestParam(value = "vehicleType", required = false) String vehicleType) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            ApplicationContextUtil.setOperateContext(getOperateContext(token));
            PageQueryDefinition definition = new PageQueryDefinition();
            definition.setPage(page);
            definition.setPageSize(pageSize);
            definition.setSortField(sort);
            definition.setOrderDir(OrderDir.valueOf(sortDirection));
            definition.setCompanyUuid(getLoginCompany(token).getUuid());
            definition.put(VehicleService.QUERY_CODE_LIKE, code);
            definition.put(VehicleService.QUERY_STATE_EQUALS,
                    StringUtil.isNullOrBlank(state) ? null : VehicleState.valueOf(state));
            definition.put(VehicleService.QUERY_VEHICLENO_LIKE, vehicleNo);
            definition.put(VehicleService.QUERY_VEHICLETYPECODE, vehicleType);
            PageQueryResult<Vehicle> result = service.query(definition);
            resp.setObj(result);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (NotLoginInfoException e) {
            return new ErrorRespObject("登录信息为空，请重新登录", e.getMessage());
        } catch (Exception e) {
            return new ErrorRespObject("分页查询车辆失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public @ResponseBody RespObject insert(
            @RequestParam(value = "token", required = true) String token,
            @RequestBody Vehicle vehicle) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            ApplicationContextUtil.setOperateContext(getOperateContext(token));
            vehicle.setState(VehicleState.free);
            vehicle.setCompanyUuid(getLoginCompany(token).getUuid());
            String uuid = service.saveNew(vehicle);
            resp.setObj(uuid);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("新增车辆失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public @ResponseBody RespObject get(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "uuid", required = true) String uuid) {
        RespObject resp = new RespObject();
        try {
            Vehicle vehicle = service.get(uuid);
            resp.setObj(vehicle);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("查询车辆失败", e.getMessage());
        }
        return resp;

    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public @ResponseBody RespObject update(
            @RequestParam(value = "token", required = true) String token,
            @RequestBody Vehicle vehicle) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            ApplicationContextUtil.setOperateContext(getOperateContext(token));
            vehicle.setCompanyUuid(getLoginCompany(token).getUuid());
            service.saveModify(vehicle);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (NotLoginInfoException e) {
            return new ErrorRespObject("登录信息为空，请重新登录", e.getMessage());
        } catch (Exception e) {
            return new ErrorRespObject("修改车辆失败", e.getMessage());
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
            return new ErrorRespObject("当前车辆已被其他用户修改", e.getMessage());
        } catch (Exception e) {
            return new ErrorRespObject("启用车辆失败", e.getMessage());
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
            return new ErrorRespObject("当前车辆已被其他用户修改", e.getMessage());
        } catch (Exception e) {
            return new ErrorRespObject("停用车辆失败", e.getMessage());
        }
        return resp;
    }
}
