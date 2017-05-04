/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	RoleController.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月27日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.web.ia.role;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.ia.role.Role;
import com.hd123.sardine.wms.api.ia.role.RoleService;
import com.hd123.sardine.wms.api.ia.user.UserState;
import com.hd123.sardine.wms.common.http.ErrorRespObject;
import com.hd123.sardine.wms.common.http.RespObject;
import com.hd123.sardine.wms.common.http.RespStatus;
import com.hd123.sardine.wms.common.query.OrderDir;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.web.BaseController;

/**
 * 角色管理控制层
 * 
 * @author fanqingqing
 *
 */
@RestController
@RequestMapping("/ia/role")
public class RoleController extends BaseController {
    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/querybypage", method = RequestMethod.GET)
    public @ResponseBody RespObject query(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false,
                    defaultValue = "asc") String sortDirection,
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "state", required = false) String state) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            PageQueryDefinition definition = new PageQueryDefinition();
            definition.setPage(page);
            definition.setPageSize(pageSize);
            definition.setSortField(sort);
            definition.setOrderDir(OrderDir.valueOf(sortDirection));
            definition.put(RoleService.QUERY_CODE_FIELD, code);
            definition.put(RoleService.QUERY_NAME_FIELD, name);
            definition.put(RoleService.QUERY_STATE_FIELD,
                    StringUtil.isNullOrBlank(state) ? null : UserState.valueOf(state));
            definition.setCompanyUuid(getLoginCompany(token).getUuid());
            PageQueryResult<Role> result = roleService.query(definition);

            resp.setObj(result);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("分页查询失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public @ResponseBody RespObject insert(
            @RequestParam(value = "token", required = true) String token, @RequestBody Role role) {
        RespObject resp = new RespObject();
        try {
            String roleUuid = roleService.insert(role, getOperateContext(token));
            resp.setObj(roleUuid);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("新增角色失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public @ResponseBody RespObject update(
            @RequestParam(value = "token", required = true) String token, @RequestBody Role role) {
        RespObject resp = new RespObject();
        try {
            roleService.update(role, getOperateContext(token));
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("编辑角色失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/remove", method = RequestMethod.DELETE)
    public @ResponseBody RespObject remove(
            @RequestParam(value = "uuid", required = true) String uuid,
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "version", required = true) long version) {
        RespObject resp = new RespObject();
        try {
            roleService.remove(uuid, version, getOperateContext(token));
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("删除角色失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/online", method = RequestMethod.PUT)
    public @ResponseBody RespObject online(
            @RequestParam(value = "uuid", required = true) String uuid,
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "version", required = true) long version) {
        RespObject resp = new RespObject();
        try {
            roleService.online(uuid, version, getOperateContext(token));
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("启用角色失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/offline", method = RequestMethod.PUT)
    public @ResponseBody RespObject offline(
            @RequestParam(value = "uuid", required = true) String uuid,
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "version", required = true) long version) {
        RespObject resp = new RespObject();
        try {
            roleService.offline(uuid, version, getOperateContext(token));
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("禁用角色失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/queryAllResource", method = RequestMethod.GET)
    public @ResponseBody RespObject queryAllResource(
            @RequestParam(value = "token", required = true) String token) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            List<Role> list = roleService.queryAllRoleByUser(getLoginUser(token).getUuid(),
                    getLoginCompany(token).getUuid());
            resp.setObj(list);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("查询角色失败", e.getMessage());
        }
        return resp;

    }
}
