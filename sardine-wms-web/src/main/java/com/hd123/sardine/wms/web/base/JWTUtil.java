/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	JWTUtil.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月10日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.web.base;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.net.InternetDomainName;
import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.ia.login.UserInfo;
import com.hd123.sardine.wms.common.entity.OperateContext;
import com.hd123.sardine.wms.common.entity.Operator;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;

/**
 * @author zhangsai
 *
 */
public class JWTUtil {

  public static final String COOKIE_NAME = "auth_cookie";
  public static final String LOGIN_COOKIE_NAME = "login_auth_cookie";
  public static final String SECRET_KEY = "request";
  public static final String LOGIN_CLAIM_NAME = "login_Info";
  /** 用户ID */
  public static final String KEY_USR_ID = "userUuid";
  /** 用户代码 */
  public static final String KEY_USR_CODE = "userCode";
  /** 用户名称 */
  public static final String KEY_USR_NAME = "userName";
  /** 仓库 */
  public static final String KEY_LOGIN_COMPANY_ID = "companyUuid";
  /** 登录组织代码 */
  public static final String KEY_LOGIN_COMPANY_CODE = "companyCode";
  /** 登录组织名称 */
  public static final String KEY_LOGIN_COMPANY_NAME = "companyName";

  public static void addCookie(HttpServletRequest request, HttpServletResponse response,
      UserInfo userInfo) {
    Assert.assertArgumentNotNull(request, "request");
    Assert.assertArgumentNotNull(response, "response");

    response.setHeader("Access-Control-Allow-Origin", "*");
    response.addCookie(createCookie(request, generateJwt(userInfo)));
    response.addCookie(createLoginCookie(request));
  }

  public static void clearCookies(HttpServletRequest request, HttpServletResponse response) {
    Assert.assertArgumentNotNull(request, "request");
    Assert.assertArgumentNotNull(response, "response");
    
    Cookie cookie = new Cookie(LOGIN_COOKIE_NAME, null);
    cookie.setDomain(getCookieDomain(request));
    cookie.setPath("/");
    response.addCookie(cookie);
    
    Cookie loginContextcookie = new Cookie(COOKIE_NAME, null);
    loginContextcookie.setDomain(getCookieDomain(request));
    loginContextcookie.setPath("/");
    loginContextcookie.setHttpOnly(true);
    response.addCookie(loginContextcookie);
  }

  public static void verifyLoginContext(Cookie[] cookies) {
    if (cookies == null)
      throw new IllegalArgumentException("当前用户未登陆！");
    Cookie authCookie = null;
    for (Cookie cookie : cookies) {
      if (cookie.getName().equals(JWTUtil.COOKIE_NAME))
        authCookie = cookie;
    }
    if (authCookie == null)
      throw new IllegalArgumentException("当前用户未登陆！");

    try {
      Algorithm algorithm = Algorithm.HMAC256(JWTUtil.SECRET_KEY);
      DecodedJWT decodedJWT = JWT.require(algorithm).build().verify(authCookie.getValue());
      Map<String, Claim> claims = decodedJWT.getClaims();
      String companyUuid = claims.get(KEY_LOGIN_COMPANY_ID).asString();
      String companyCode = claims.get(KEY_LOGIN_COMPANY_CODE).asString();
      String companyName = claims.get(KEY_LOGIN_COMPANY_NAME).asString();
      String userUuid = claims.get(KEY_USR_ID).asString();
      String userCode = claims.get(KEY_USR_CODE).asString();
      String userName = claims.get(KEY_USR_NAME).asString();
      ApplicationContextUtil.setCompany(new UCN(companyUuid, companyCode, companyName));
      ApplicationContextUtil
          .setOperateContext(new OperateContext(new Operator(userUuid, userCode, userName)));
    } catch (Exception e) {
      throw new IllegalArgumentException("校验登陆信息失败，原因：" + e.getMessage());
    }
  }

  private static String generateJwt(UserInfo userInfo) {
    if (userInfo == null)
      throw new IllegalArgumentException("用户未登陆！");

    try {
      Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
      JWTCreator.Builder jwtBuilder = JWT.create()
          .withClaim(KEY_LOGIN_COMPANY_ID, userInfo.getCompanyUuid())
          .withClaim(KEY_LOGIN_COMPANY_CODE, userInfo.getCompanyCode())
          .withClaim(KEY_LOGIN_COMPANY_NAME, userInfo.getCompanyName())
          .withClaim(KEY_USR_ID, userInfo.getUuid()).withClaim(KEY_USR_CODE, userInfo.getCode())
          .withClaim(KEY_USR_NAME, userInfo.getName());
      String authToken = jwtBuilder.sign(algorithm);
      return authToken;
    } catch (Exception e) {
      throw new IllegalArgumentException("获取登陆信息失败！原因：" + e.getMessage());
    }
  }

  private static Cookie createCookie(HttpServletRequest request, String jwt) {
    Cookie cookie = new Cookie(COOKIE_NAME, jwt);
    cookie.setDomain(getCookieDomain(request));
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    return cookie;
  }

  private static Cookie createLoginCookie(HttpServletRequest request) {
    Cookie cookie = new Cookie(LOGIN_COOKIE_NAME, "true");
    cookie.setDomain(getCookieDomain(request));
    cookie.setPath("/");
    return cookie;
  }

  private static String getCookieDomain(HttpServletRequest request) {
    String result = request.getServerName();
    try {
      result = "." + InternetDomainName.from(request.getServerName()).topPrivateDomain().toString();
    } catch (Exception e) {
    }
    return result;
  }
}
