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

  /** 资源标示前缀，用于后续扩展 */
  public static final String RESOURCE_PREFIX = "xxxx";

  /** 配送中心ID前缀 */
  public static final String DC_PREFIX = "dc";

  /** 供应商ID前缀 */
  public static final String SUPP_PREFIX = "supp";

  /** 承运商ID前缀 */
  public static final String CARR_PREFIX = "carr";

  /** 数据库前缀名 */
  public static final String DB_PREFIX = "sardine";

  /** 需要分库的表，在做增删改查时都以此为数据库名，后续由拦截器统一替换为真实的数据库名 */
  public static final String DB_PROXY = "db_proxy";

  /** 组织ID流水长度 */
  public static final int COMPANY_FLOW_LENGTH = 4;

  /** 配送中心代码前缀，代码=前缀+id流水，用于单号和指令号的前缀 */
  public static final String DC_CODE_PREFIX = "8";

  /** 供应商代码前缀，代码=前缀+id流水，用于单号和指令号的前缀 */
  public static final String SUPP_CODE_PREFIX = "9";

  /** 承运商代码前缀，代码=前缀+id流水，用于单号和指令号的前缀 */
  public static final String CARR_CODE_PREFIX = "6";

  /** 虚拟组织ID，用于不需要区分组织的序列 */
  public static final String VIRTUAL_COMPANYUUID = "sardine";
}
