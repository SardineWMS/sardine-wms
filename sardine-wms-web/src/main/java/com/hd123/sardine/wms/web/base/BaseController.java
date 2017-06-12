/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	BaseController.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月19日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.web.base;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.ia.login.UserInfo;
import com.hd123.sardine.wms.common.entity.OperateContext;
import com.hd123.sardine.wms.common.entity.Operator;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.exception.NotLoginInfoException;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.SerializationUtils;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.web.base.jedis.RedisUtil;

/**
 * @author zhangsai
 *
 */
public abstract class BaseController {

  @Autowired
  private RedisUtil redisUtil;

  public void checkUserInfo(String token) {
    if (StringUtil.isNullOrBlank(token))
      throw new NotLoginInfoException("登录信息为空，请重新登录");
    String loginCache = redisUtil.getString(token);
    if (StringUtil.isNullOrBlank(loginCache)) {
      throw new NotLoginInfoException("登录信息为空，请重新登录");
    }
    try {
      UserInfo info = SerializationUtils.deserialize(loginCache, UserInfo.class);
      UCN user = new UCN(info.getUuid(), info.getCode(), info.getName());
      UCN company = new UCN(info.getCompanyUuid(), info.getCompanyCode(), info.getCompanyName());
      ApplicationContextUtil.setCompany(company);
      OperateContext operCtx = new OperateContext();
      operCtx.setOperator(new Operator(user.getUuid(), user.getCode(), user.getName()));
      ApplicationContextUtil.setOperateContext(operCtx);
    } catch (Exception e) {
      throw new NotLoginInfoException("登录信息为空，请重新登录");
    }
  }

  public String setLoginInfoCache(UserInfo userInfo) {
    String token = UUIDGenerator.genUUID();
    redisUtil.setString(token, SerializationUtils.serialize(userInfo));
    return token;
  }

  public void resetToken(String token) {
    redisUtil.deleteString(token);
  }

  public UCN getLoginUser(String token) throws NotLoginInfoException {
    if (StringUtil.isNullOrBlank(token))
      throw new NotLoginInfoException("登录信息为空，请重新登录");
    String loginCache = redisUtil.getString(token);
    if (StringUtil.isNullOrBlank(loginCache)) {
      throw new NotLoginInfoException("登录信息为空，请重新登录");
    }
    try {
      UserInfo info = SerializationUtils.deserialize(loginCache, UserInfo.class);
      UCN user = new UCN(info.getUuid(), info.getCode(), info.getName());
      return user;
    } catch (Exception e) {
      throw new NotLoginInfoException("登录信息为空，请重新登录");
    }
  }

  public UCN getLoginCompany(String token) throws NotLoginInfoException {
    String loginCache = redisUtil.getString(token);
    if (StringUtil.isNullOrBlank(loginCache)) {
      throw new NotLoginInfoException("登录信息为空，请重新登录");
    }

    try {
      UserInfo info = SerializationUtils.deserialize(loginCache, UserInfo.class);
      UCN company = new UCN(info.getCompanyUuid(), info.getCompanyCode(), info.getCompanyName());
      return company;
    } catch (Exception e) {
      throw new NotLoginInfoException("登录信息为空，请重新登录");
    }
  }

  public OperateContext getOperateContext(String token) {
    Assert.assertArgumentNotNull(token, "token");

    UCN user = getLoginUser(token);
    OperateContext operCtx = new OperateContext();
    operCtx.setOperator(new Operator(user.getUuid(), user.getCode(), user.getName()));
    return operCtx;
  }
}
