/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	DatabaseContextHolder.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月17日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.dao.datasource;

/**
 * @author zhangsai
 *
 */
public class DatabaseContextHolder {

  private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();  
  
  public static void setCustomerType(String customerType) {  
      contextHolder.set(customerType);  
  }  

  public static String getCustomerType() {  
      return contextHolder.get();  
  }  

  public static void clearCustomerType() {  
      contextHolder.remove();  
  }
}
