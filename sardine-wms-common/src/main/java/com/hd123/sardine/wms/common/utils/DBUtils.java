/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	DBUtils.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月2日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.utils;

import com.hd123.rumba.commons.lang.StringUtil;

/**
 * @author zhangsai
 *
 */
public class DBUtils {

  private static final int DB_COUNT = 2;

  public static String fetchDbName(String companyUuid) {
    if (StringUtil.isNullOrBlank(companyUuid))
      throw new IllegalArgumentException("组织ID为空，无法确定数据库名称！");

    String containDBName = null;
    if (companyUuid.indexOf(Constants.DC_PREFIX) >= 0) {
      containDBName = companyUuid.substring(0, companyUuid.indexOf(Constants.DC_PREFIX));
    } else if (companyUuid.indexOf(Constants.SUPP_PREFIX) >= 0) {
      containDBName = companyUuid.substring(0, companyUuid.indexOf(Constants.SUPP_PREFIX));
    } else if (companyUuid.indexOf(Constants.CARR_PREFIX) >= 0) {
      containDBName = companyUuid.substring(0, companyUuid.indexOf(Constants.CARR_PREFIX));
    } else {
      containDBName = companyUuid;
    }
    String seqNumber = containDBName.substring(containDBName.length() - 4, containDBName.length());
    int number = Integer.valueOf(seqNumber) % DB_COUNT;
//    return Constants.DB_PREFIX.concat(String.valueOf(number + 1));
    return "sardine1";
  }
}
