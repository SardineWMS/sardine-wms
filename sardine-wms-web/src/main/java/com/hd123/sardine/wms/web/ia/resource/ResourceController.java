/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	ResourceController.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月29日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.web.ia.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.sardine.wms.api.ia.resource.Resource;
import com.hd123.sardine.wms.api.ia.resource.ResourceService;
import com.hd123.sardine.wms.common.http.ErrorRespObject;
import com.hd123.sardine.wms.common.http.RespObject;
import com.hd123.sardine.wms.common.http.RespStatus;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.web.base.BaseController;

/**
 * @author fanqingqing
 *
 */
@RestController
@RequestMapping("/ia/resource")
public class ResourceController extends BaseController {
    @Autowired
    private ResourceService resourceService;

    @RequestMapping(value = "/queryOwnedMenuResourceByUser", method = RequestMethod.GET)
    public @ResponseBody RespObject queryOwnedAllResourceByUser(
            @RequestParam(value = "userUuid") String userUuid,
            @RequestParam(value = "token") String token) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            List<Resource> resources = resourceService.queryOwnedMenuResourceByUser(userUuid);
            resp.setObj(resources);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("查询失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/queryAllResourceByUser", method = RequestMethod.GET)
    public @ResponseBody RespObject queryAllResourceByUser(
            @RequestParam(value = "userUuid") String userUuid,
            @RequestParam(value = "token") String token) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            List<Resource> resources = resourceService.queryAllResourceByUser(userUuid);
            resp.setObj(resources);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("查询失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/queryOwnedResourceByRole", method = RequestMethod.GET)
    public @ResponseBody RespObject queryOwnedResourceByRole(
            @RequestParam(value = "roleUuid") String roleUuid,
            @RequestParam(value = "token") String token) {
        RespObject resp = new RespObject();
        try {
            List<Resource> resources = resourceService.queryOwnedResourceByRole(roleUuid);
            resp.setObj(resources);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("查询失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/queryAllResourceByRole", method = RequestMethod.GET)
    public @ResponseBody RespObject queryAllResourceByRole(
            @RequestParam(value = "roleUuid") String roleUuid,
            @RequestParam(value = "token") String token) {
        RespObject resp = new RespObject();
        try {
            List<Resource> resources = resourceService.queryAllResourceByRole(roleUuid);
            resp.setObj(resources);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("查询失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/saveUserResource", method = RequestMethod.POST)
    public @ResponseBody RespObject saveUserResource(
            @RequestParam(value = "userUuid") String userUuid,
            @RequestParam(value = "token", required = true) String token,
            @RequestBody List<String> resourceUuids) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            resourceService.saveUserResource(userUuid, resourceUuids);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("新增用户资源权限", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/saveRoleResource", method = RequestMethod.POST)
    public @ResponseBody RespObject saveRoleResource(
            @RequestParam(value = "roleUuid") String roleUuid,
            @RequestParam(value = "token", required = true) String token,
            @RequestBody List<String> resourceUuids) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            resourceService.saveRoleResource(roleUuid, resourceUuids);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("新增角色资源权限", e.getMessage());
        }
        return resp;
    }

}
