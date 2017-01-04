/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	DataAccessException.java
 * 模块说明：	
 * 修改历史：
 * 2016-12-19 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.exception;

import java.text.MessageFormat;

/**
 * 数据访问异常。
 * <p>
 * 数据访问层基类。
 * 
 * @author zhangsai
 * 
 */
public class DataAccessException extends RuntimeException {
  private static final long serialVersionUID = -8328953239140184166L;

  public DataAccessException() {
    // do nothing
  }

  public DataAccessException(String message) {
    super(message);
  }

  public DataAccessException(Throwable caught) {
    super(caught);
  }

  public DataAccessException(Throwable caught, String message) {
    super(message, caught);
  }

  public DataAccessException(String pattern, Object... arguments) {
    super(MessageFormat.format(pattern, arguments));
  }

  public DataAccessException(Throwable caught, String pattern, Object... arguments) {
    super(MessageFormat.format(pattern, arguments), caught);
  }
}
