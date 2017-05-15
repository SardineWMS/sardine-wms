/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	BaseController.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月19日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.web;

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

import redis.clients.jedis.Jedis;

/**
 * @author zhangsai
 *
 */
public abstract class BaseController {

  private Jedis jedis;

  private void setUp() {
    if (jedis == null) {
      jedis = new Jedis("172.17.1.54", 6379);
      jedis.auth("sardine");
    }

    if (jedis.isConnected() == false)
      jedis.connect();
  }

  public void checkUserInfo(String token) {
    getLoginUser(token);
    ApplicationContextUtil.setCompany(getLoginCompany(token));
    ApplicationContextUtil.setOperateContext(getOperateContext(token));
  }

  public String setLoginInfoCache(UserInfo userInfo) {
    setUp();
    String token = UUIDGenerator.genUUID();
    jedis.set(token, SerializationUtils.serialize(userInfo));
    return token;
  }

  public void resetToken(String token) {
    setUp();
    jedis.del(token);
  }

  public UCN getLoginUser(String token) throws NotLoginInfoException {
    if (StringUtil.isNullOrBlank(token))
      throw new NotLoginInfoException("登录信息为空，请重新登录");
    setUp();
    String loginCache = jedis.get(token);
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
    setUp();
    String loginCache = jedis.get(token);
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
