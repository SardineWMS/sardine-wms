/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	NotLoginInfoException.java
 * 模块说明：	
 * 修改历史：
 * 2016-12-19 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.exception;

import java.text.MessageFormat;

/**
 * 登录信息为空异常。
 * <p>
 * 用于web层。
 * 
 * @author zhangsai
 * 
 */
public class NotLoginInfoException extends RuntimeException {
  private static final long serialVersionUID = -8328953239140184166L;

  public NotLoginInfoException() {
    // do nothing
  }

  public NotLoginInfoException(String message) {
    super(message);
  }

  public NotLoginInfoException(Throwable caught) {
    super(caught);
  }

  public NotLoginInfoException(Throwable caught, String message) {
    super(message, caught);
  }

  public NotLoginInfoException(String pattern, Object... arguments) {
    super(MessageFormat.format(pattern, arguments));
  }

  public NotLoginInfoException(Throwable caught, String pattern, Object... arguments) {
    super(MessageFormat.format(pattern, arguments), caught);
  }
}
