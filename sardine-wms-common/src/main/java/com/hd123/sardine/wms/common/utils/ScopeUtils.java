/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	wms-wms-core
 * 文件名：	ScopeUtils.java
 * 模块说明：	
 * 修改历史：
 * 2013-11-14 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.utils;

import org.apache.commons.lang3.StringUtils;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * 货位范围解释器
 * 
 * @author zhangsai
 * 
 */
public class ScopeUtils {
  /**
   * 将存储范围解释为SQL语句，用于查询符合条件的货位 <br>
   * 支持格式：023100,0200-0211,020310(1/2/3),0208-0210(4/5) <br>
   * 样例：alias = 't.code' scopeExp = '0208-0210' <br>
   * 生成sql语句：(t.code between '0208%' and '0210%')
   * 
   * @param alias
   *          查询前缀
   * @param scopeExp
   *          货位范围表达式，多个条件使用“,”分割，装换为SQL语句为“or”，范围使用“-”，解释为SQL语句为between...and
   *          ...语句
   * @return 返回SQL语句
   */
  public static String scopeExpToSQLExp(String alias, String scopeExp) {
    Assert.assertArgumentNotNull(scopeExp, "scopeExp");
    Assert.assertArgumentNotNull(alias, "alias");
    StringBuffer result = new StringBuffer();
    alias.replace(" ", "");
    scopeExp.replace(" ", "");
    String[] scopes = scopeExp.split(",");

    for (String scope : scopes) {
      String sql = scopeExpToSQLExp2(alias, scope);
      result.append("(");
      result.append(sql);
      result.append(") or ");
    }

    if (scopes.length == 1) {
      return result.toString().substring(1, result.toString().length() - 5);
    }

    return result.toString().substring(0, result.toString().length() - 4);
  }

  public static boolean contains(String binScope, String binCode) {
    Assert.assertArgumentNotNull(binScope, "binScope");
    Assert.assertArgumentNotNull(binCode, "binCode");

    binScope.replace(" ", "");
    binCode.replace(" ", "");

    String[] scopes = binScope.split(",");
    for (String scope : scopes) {
      if (scope.indexOf("-") == -1 && scope.indexOf(")") == -1) {
        if (binCode.startsWith(scope))
          return true;
      }

      if (scope.indexOf("-") == -1 && scope.indexOf(")") > -1) {
        String[] items = scope.split("\\(");
        if (items.length > 1) {
          String[] children = items[1].split("/");
          for (String child : children) {
            if (child.length() < 2)
              child = "0" + child;
            if (binCode.startsWith(items[0] + child))
              return true;
          }
        }
      }

      if (scope.indexOf("-") > -1) {
        String[] items = scope.split("-");
        String startCode = items[0].length() < binCode.length() ? items[0]
            + StringUtils.repeat("0", binCode.length() - items[0].length()) : items[0];
        String endCode = items[1].length() < binCode.length() ? items[1]
            + StringUtils.repeat("9", binCode.length() - items[1].length()) : items[1];
        if (startCode.compareTo(binCode) <= 0 && endCode.compareTo(binCode) >= 0)
          return true;
      }
    }

    return false;
  }

  private static String scopeExpToSQLExp2(String alias, String scope) {
    assert StringUtil.isNullOrBlank(scope) == false;
    assert StringUtil.isNullOrBlank(alias) == false;
    String result = "";

    String[] splits = scope.split("\\(");
    String[] items = splits[0].split("-");
    String[] child = null;
    if (splits.length > 1) {
      child = splits[1].split("/");
    }

    if (items.length > 1) {
      String start = items[0] + StringUtils.repeat("0", 8 - items[0].length());
      String end = items[1] + StringUtils.repeat("0", 8 - items[1].length());
      if (start.compareTo(end) > 0)
        result += "(" + alias + " between '" + items[0] + "%' and '" + items[1] + "%')";
      else
        result += "(" + alias + " between '" + items[0] + "%' and '" + items[1] + "%' or " + alias
            + " like '" + items[1] + "%' or " + alias + " like '" + items[0] + "%')";
    } else {
      result += alias + " like '" + items[0] + "%'";
    }

    if (child != null && child.length > 0) {
      String str = "";
      for (int i = 0; i < items[0].length(); i++) {
        str += "_";
      }
      result += " and (";
      for (String ch : child) {
        if (ch.indexOf(")") != -1)
          ch = ch.substring(0, ch.length() - 1);
        if (str.length() == 6)
          ch = "_" + ch;
        if (ch.length() == 1 && str.length() < 6)
          ch = "0" + ch;
        result += alias + " like '" + str + ch + "%' or ";
      }
      result = result.substring(0, result.length() - 3) + ")";
    }

    return result;
  }
}
