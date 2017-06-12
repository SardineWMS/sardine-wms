/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	AuthenticationController.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月12日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.web.ia.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.sardine.wms.api.ia.login.LoginService;
import com.hd123.sardine.wms.api.ia.login.UserInfo;
import com.hd123.sardine.wms.api.ia.user.RegisterInfo;
import com.hd123.sardine.wms.common.http.ErrorRespObject;
import com.hd123.sardine.wms.common.http.RespObject;
import com.hd123.sardine.wms.common.http.RespStatus;
import com.hd123.sardine.wms.web.base.BaseController;

/**
 * 认证管理控制层
 * 
 * @author zhangsai
 *
 */
@RestController
@RequestMapping("/ia/authen")
public class AuthenticationController extends BaseController {
  @Autowired
  private LoginService loginService;

  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public @ResponseBody RespObject login(
      @RequestParam(value = "loginid", required = true) String loginId,
      @RequestParam(value = "password", required = true) String password) {
    RespObject resp = new RespObject();
    try {
//      UsernamePasswordToken usernameToken = new UsernamePasswordToken(loginId,
//          "888888");
//      Subject subject = SecurityUtils.getSubject();
//      subject.hasRole("ere");
//      subject.login(usernameToken);
      UserInfo userInfo = loginService.login(loginId, password);
      String token = setLoginInfoCache(userInfo);
      resp.setObj(userInfo);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
      resp.setToken(token);
    } catch (Exception e) {
      e.printStackTrace();
      return new ErrorRespObject("登录失败", e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/loginOut", method = RequestMethod.GET)
  public @ResponseBody RespObject loginOut(
      @RequestParam(value = "token", required = true) String token) {
    RespObject resp = new RespObject();
    try {
      resetToken(token);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      e.printStackTrace();
      return new ErrorRespObject("退出登录失败", e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/update_passwd", method = RequestMethod.PUT)
  public @ResponseBody RespObject updatePasswd(
      @RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "oldPasswd", required = true) String oldPasswd,
      @RequestParam(value = "newPasswd", required = true) String newPasswd) {
    RespObject resp = new RespObject();
    try {
      UserInfo userInfo = loginService.updatePasswd(getLoginUser(token).getUuid(), oldPasswd,
          newPasswd);
      resp.setObj(userInfo);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
      resp.setToken(token);
    } catch (Exception e) {
      return new ErrorRespObject("密码修改失败", e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/register", method = RequestMethod.POST)
  public @ResponseBody RespObject register(@RequestBody RegisterInfo registerInfo) {
    RespObject resp = new RespObject();
    try {
      UserInfo userInfo = loginService.register(registerInfo);
      String token = setLoginInfoCache(userInfo);
      resp.setObj(userInfo);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
      resp.setToken(token);
    } catch (Exception e) {
      return new ErrorRespObject("用户注册失败", e.getMessage());
    }
    return resp;
  }
}
