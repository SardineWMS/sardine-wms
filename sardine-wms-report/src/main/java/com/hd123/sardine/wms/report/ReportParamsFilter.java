/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-report
 * 文件名：	ReportParamsFilter.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月7日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.report;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.common.exception.NotLoginInfoException;
import com.hd123.sardine.wms.common.utils.DBUtils;

/**
 * @author zhangsai
 *
 */
public class ReportParamsFilter implements Filter {

  private static final String KEY_URL_REPORTLET = "reportlet";
  private static final String KEY_URL_REPORTLETS = "reportlets";
  private static final String KEY_URL_FORMLET = "formlet";
  private static final String KEY_URL_DBNAME = "dbname";
  private static final String KEY_URL_COMPANYUUID = "companyUuid";
  private static final String KEY_URL_USERUUID = "userUuid";

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    // do nothing
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    if ((!(request instanceof HttpServletRequest))
        || (!(response instanceof HttpServletResponse))) {
      chain.doFilter(request, response);
      return;
    }

    HttpServletResponse httpResponse = (HttpServletResponse) response;
    httpResponse.setHeader("Access-Control-Allow-Origin", "*");

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    if (StringUtil.isNullOrBlank(httpRequest.getParameter(KEY_URL_COMPANYUUID))
        && StringUtil.isNullOrBlank(httpRequest.getParameter(KEY_URL_USERUUID))
        && StringUtil.isNullOrBlank(httpRequest.getParameter(KEY_URL_REPORTLET))
        && StringUtil.isNullOrBlank(httpRequest.getParameter(KEY_URL_REPORTLETS))
        && StringUtil.isNullOrBlank(httpRequest.getParameter(KEY_URL_FORMLET))) {
      chain.doFilter(request, response);
      return;
    }
    try {
      String dbname = DBUtils.fetchDbName(httpRequest.getParameter(KEY_URL_COMPANYUUID));
      request.setAttribute(KEY_URL_DBNAME, dbname);
    } catch (Exception e) {
      throw new NotLoginInfoException("登录信息为空，请重新登录");
    }

    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {
    // do nothing
  }
}
