/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	Constants.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月10日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.utils;

/**
 * 系统常量
 * 
 * @author zhangsai
 *
 */
public class Constants {

  /** 公司ID起始序号 */
  public static final String COMPANY_ID_START = "100001";
  
  public static final String RESOURCE_PREFIX = "xxxx";

  /** 配送中心ID前缀 */
  public static final String DC_PREFIX = "dc";

  /** 供应商ID前缀 */
  public static final String SUPP_PREFIX = "supp";

  /** 承运商ID前缀 */
  public static final String CARR_PREFIX = "carr";

  /** 数据库前缀名 */
  public static final String DB_PREFIX = "sardine";
  
  /** 需要分库的表，在做增删改查时都以此为数据库名，后续由拦截器统一替换为真实的数据库名*/
  public static final String DB_PROXY = "db_proxy";
  
  public static final int COMPANY_FLOW_LENGTH = 4;
  
  public static final String DC_CODE_PREFIX = "8";
  public static final String SUPP_CODE_PREFIX = "9";
  public static final String CARR_CODE_PREFIX = "6";
}
