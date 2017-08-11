/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	UserController.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月12日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.web.ia.user;

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
import com.hd123.sardine.wms.api.ia.user.User;
import com.hd123.sardine.wms.api.ia.user.UserService;
import com.hd123.sardine.wms.api.ia.user.UserState;
import com.hd123.sardine.wms.common.http.ErrorRespObject;
import com.hd123.sardine.wms.common.http.RespObject;
import com.hd123.sardine.wms.common.http.RespStatus;
import com.hd123.sardine.wms.common.query.OrderDir;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.web.base.BaseController;

/**
 * 用户管理控制层
 * 
 * @author zhangsai
 *
 */
@RestController
@RequestMapping("/ia/user")
public class UserController extends BaseController {
  @Autowired
  private UserService userService;
  @Autowired
  private RoleService roleService;

  @RequestMapping(value = "/get", method = RequestMethod.GET)
  public @ResponseBody RespObject get(@RequestParam(value = "userUuid") String userUuid,
      @RequestParam(value = "token") String token) {
    RespObject resp = new RespObject();
    try {
      User user = userService.get(userUuid);
      resp.setObj(user);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("查询失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/getbycode", method = RequestMethod.GET)
  public @ResponseBody RespObject getByCode(@RequestParam(value = "userCode") String userCode) {
    RespObject resp = new RespObject();
    try {
      User user = userService.getByCode(userCode);
      resp.setObj(user);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("查询失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/querybypage", method = RequestMethod.GET)
  public @ResponseBody RespObject query(
      @RequestParam(value = "page", required = false, defaultValue = "1") int page,
      @RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize,
      @RequestParam(value = "sort", required = false) String sort,
      @RequestParam(value = "order", required = false, defaultValue = "asc") String sortDirection,
      @RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "code", required = false) String code,
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "phone", required = false) String phone,
      @RequestParam(value = "userState", required = false) String userState,
      @RequestParam(value = "roleUuid", required = false) String roleUuid) {
    RespObject resp = new RespObject();
    try {
      PageQueryDefinition definition = new PageQueryDefinition();
      definition.setPage(page);
      definition.setPageSize(pageSize);
      definition.setSortField(sort);
      definition.setOrderDir(OrderDir.valueOf(sortDirection));
      definition.put(UserService.QUERY_CODE_FIELD, code);
      definition.put(UserService.QUERY_NAME_FIELD, name);
      definition.put(UserService.QUERY_PHONE_FIELD, phone);
      definition.put(UserService.QUERY_USERSTATE_FIELD,
          StringUtil.isNullOrBlank(userState) ? null : UserState.valueOf(userState));
      definition.put(UserService.QUERY_ROLE_FIELD, roleUuid);
      PageQueryResult<User> result = userService.query(definition);

      resp.setObj(result);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("分页查询失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/insert", method = RequestMethod.POST)
  public @ResponseBody RespObject insert(
      @RequestParam(value = "token", required = true) String token, @RequestBody User user) {
    RespObject resp = new RespObject();
    try {
      String userUuid = userService.insert(user);
      resp.setObj(userUuid);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("新增用户失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/update", method = RequestMethod.PUT)
  public @ResponseBody RespObject update(
      @RequestParam(value = "token", required = true) String token, @RequestBody User user) {
    RespObject resp = new RespObject();
    try {
      userService.update(user);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("编辑用户失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/remove", method = RequestMethod.DELETE)
  public @ResponseBody RespObject remove(@RequestParam(value = "uuid", required = true) String uuid,
      @RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "version", required = true) long version) {
    RespObject resp = new RespObject();
    try {
      userService.remove(uuid, version);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("删除用户失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/online", method = RequestMethod.PUT)
  public @ResponseBody RespObject online(@RequestParam(value = "uuid", required = true) String uuid,
      @RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "version", required = true) long version) {
    RespObject resp = new RespObject();
    try {
      userService.online(uuid, version);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("启用用户失败：" + e.getMessage());
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
      userService.offline(uuid, version);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("禁用用户失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/saveUserRoles", method = RequestMethod.POST)
  public @ResponseBody RespObject saveUserRoles(
      @RequestParam(value = "userUuid", required = true) String userUuid,
      @RequestParam(value = "token", required = true) String token,
      @RequestBody List<String> roleUuids) {
    RespObject resp = new RespObject();
    try {
      userService.saveUserRoles(userUuid, roleUuids);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("分配角色失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/queryRolesByUser", method = RequestMethod.GET)
  public @ResponseBody RespObject queryRolesByUser(
      @RequestParam(value = "userUuid") String userUuid,
      @RequestParam(value = "token") String token) {
    RespObject resp = new RespObject();
    try {
      List<Role> roles = roleService.queryRolesByUser(userUuid);
      resp.setObj(roles);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("查询用户拥有的角色失败：" + e.getMessage());
    }
    return resp;
  }

}
