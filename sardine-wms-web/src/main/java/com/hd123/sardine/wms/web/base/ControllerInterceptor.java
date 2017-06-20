/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	ControllerInterceptor.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月17日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.web.base;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.hd123.sardine.wms.common.dao.datasource.DatabaseContextHolder;
import com.hd123.sardine.wms.common.utils.SerializationUtils;

/**
 * Controller拦截器
 * <p>
 * <li>预处理：校验登录信息，记录日志</li>
 * <li>后处理，记录日志</li>
 * 
 * @author zhangsai
 *
 */
public class ControllerInterceptor implements HandlerInterceptor {

  private static final Logger LOGGER = LoggerFactory.getLogger(ControllerInterceptor.class);
  private static final String TOKEN_NAME = "token";

  private List<String> uncheckUrls;
  private String url_prefix = "/sardine-wms-web";

  public void setUrl_prefix(String url_prefix) {
    this.url_prefix = url_prefix;
  }

  public void setUncheckUrls(List<String> uncheckUrls) {
    this.uncheckUrls = uncheckUrls;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception arg3) throws Exception {

  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView arg3) throws Exception {
    if (handler instanceof HandlerMethod) {
      HandlerMethod method = (HandlerMethod) handler;
      LOGGER.info(method.getBeanType().getSimpleName() + "--->" + method.getMethod().getName()
          + "执行完成，参数：" + SerializationUtils.serialize(request.getParameterMap()));
    }
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    if (handler instanceof HandlerMethod) {
      HandlerMethod method = (HandlerMethod) handler;
      LOGGER.info(method.getBeanType().getSimpleName() + "--->" + method.getMethod().getName()
          + "开始执行，参数：" + SerializationUtils.serialize(request.getParameterMap()));

      // 后续将根据token对应公司uuid的前缀确定数据源，并将数据源key存储到jedis中
      DatabaseContextHolder.setCustomerType("dataSource");
      String requestUrl = request.getRequestURI();
      if (uncheckUrls.contains(requestUrl.substring(url_prefix.length())))
        return true;

      String token = request.getParameter(TOKEN_NAME);
      BaseController controller = (BaseController) method.getBean();
      controller.checkUserInfo(token);
      return true;
    }
    return false;
  }
}
