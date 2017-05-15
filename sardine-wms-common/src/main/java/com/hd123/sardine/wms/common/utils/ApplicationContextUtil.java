/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名： sardine-wms-common
 * 文件名： ApplicationContextUtil.java
 * 模块说明：  
 * 修改历史：
 * 2017年4月10日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.utils;

import java.util.HashMap;
import java.util.Map;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.common.entity.OperateContext;
import com.hd123.sardine.wms.common.entity.OperateInfo;
import com.hd123.sardine.wms.common.entity.Operator;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * 取系统环境变量
 * 
 * @author zhangsai
 *
 */
public class ApplicationContextUtil {

  private static ThreadLocal<String> COMPANYUUID_LOCAL = new ThreadLocal<String>();

  private static ThreadLocal<String> COMPANYCODE_LOCAL = new ThreadLocal<String>();

  private static ThreadLocal<String> DBNAME_LOCAL = new ThreadLocal<String>();

  private static ThreadLocal<OperateContext> OPERATECONTEXT_LOCAL = new ThreadLocal<OperateContext>();

  /**
   * 获取公司ID
   * 
   * @return
   */
  public static String getCompanyUuid() {
    if (StringUtil.isNullOrBlank(COMPANYUUID_LOCAL.get()))
      throw new IllegalArgumentException("登录信息为空，要重新登录咯");
    return COMPANYUUID_LOCAL.get();
  }

  public static String getCompanyCode() {
    if (StringUtil.isNullOrBlank(COMPANYCODE_LOCAL.get()))
      throw new IllegalArgumentException("登录信息为空，要重新登录咯");
    return COMPANYCODE_LOCAL.get();
  }

  public static String getParentCompanyUuid() {
    String companyUuid = COMPANYUUID_LOCAL.get();
    if (StringUtil.isNullOrBlank(companyUuid))
      throw new IllegalArgumentException("登录信息为空，要重新登录咯");

    String parentCompanyUuid = null;
    if (companyUuid.contains(Constants.DC_PREFIX)) {
      parentCompanyUuid = companyUuid.substring(0, companyUuid.indexOf(Constants.DC_PREFIX));
    } else if (companyUuid.contains(Constants.SUPP_PREFIX)) {
      parentCompanyUuid = companyUuid.substring(0, companyUuid.indexOf(Constants.SUPP_PREFIX));
    } else if (companyUuid.contains(Constants.CARR_PREFIX)) {
      parentCompanyUuid = companyUuid.substring(0, companyUuid.indexOf(Constants.CARR_PREFIX));
    } else {
      parentCompanyUuid = companyUuid;
    }

    return parentCompanyUuid;
  }

  public static String getParentCompanyCode() {
    String parentCompanyUuid = getParentCompanyUuid();
    String parentCompanyCode = parentCompanyUuid.substring(
        parentCompanyUuid.indexOf(Constants.RESOURCE_PREFIX) + Constants.RESOURCE_PREFIX.length(),
        parentCompanyUuid.length());
    return parentCompanyCode;
  }

  public static String getDBName() {
    if (StringUtil.isNullOrBlank(DBNAME_LOCAL.get()))
      throw new IllegalArgumentException("登录信息为空，要重新登录咯");
    return DBNAME_LOCAL.get();
  }

  public static void setOperateContext(OperateContext operCtx) {
    if (operCtx == null)
      throw new IllegalArgumentException("操作上下文为空！");
    OPERATECONTEXT_LOCAL.set(operCtx);
  }

  /**
   * 获取登录用户
   * 
   * @return
   */
  public static OperateContext getOperateContext() {
    if (OPERATECONTEXT_LOCAL.get() == null)
      throw new IllegalArgumentException("操作上下文为空！");
    return OPERATECONTEXT_LOCAL.get();
  }

  /**
   * 获取操作信息
   * 
   * @return
   */
  public static OperateInfo getOperateInfo() {
    OperateContext operCtx = OPERATECONTEXT_LOCAL.get();
    if (operCtx == null)
      throw new IllegalArgumentException("操作上下文为空！");

    OperateInfo operateInfo = OperateInfo.newInstance(operCtx);
    return operateInfo;
  }

  /** 获取登录用户 */
  public static UCN getLoginUser() {
    OperateContext operCtx = OPERATECONTEXT_LOCAL.get();
    if (operCtx == null)
      throw new IllegalArgumentException("操作上下文为空！");
    Operator operator = operCtx.getOperator();
    if (operator == null)
      throw new IllegalArgumentException("登录用户为空！");
    UCN loginUser = new UCN(operator.getId(), operator.getCode(), operator.getFullName());
    return loginUser;
  }

  /**
   * 设置公司ID，在线程开始时set
   * 
   * @param companyUuid
   */
  public static void setCompany(UCN company) {
    if (company == null)
      throw new IllegalArgumentException("登录信息为空，无法货区组织信息！");
    COMPANYUUID_LOCAL.set(company.getUuid());
    COMPANYCODE_LOCAL.set(company.getCode());
    DBNAME_LOCAL.set(DBUtils.fetchDbName(company.getUuid()));
  }

  public static Map<String, Object> map() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("companyUuid", getCompanyUuid());
    return map;
  }

  public static Map<String, Object> mapWithParentCompanyUuid() {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("companyUuid", getParentCompanyUuid());
    return map;
  }
}
