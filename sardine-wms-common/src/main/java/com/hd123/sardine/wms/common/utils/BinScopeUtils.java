/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	wms-common
 * 文件名：	BinScopeUtils.java
 * 模块说明：	
 * 修改历史：
 * 2015-2-5 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.utils;

import org.apache.commons.lang3.StringUtils;

import com.hd123.rumba.commons.lang.Assert;

/**
 * 货位范围工具类
 * 
 * @author zhangsai
 * 
 */
public class BinScopeUtils {

  /**
   * 判断某个货位是否在指定的货位范围内
   * 
   * @param binScope
   *          货位范围，not null <br>
   *          支持格式：023100,0200-0211,020310(1/2/3)
   * @param binCode
   *          单个代码，可以是货区、货道、货架等，not null
   * @return true：在，false：不在
   * @throws IllegalArgumentException
   */
  public static boolean contains(String binScope, String binCode) {
    Assert.assertArgumentNotNull(binScope, "binScope");
    Assert.assertArgumentNotNull(binCode, "binCode");
    binScope.replace(" ", "");
    binCode.replace(" ", "");

    String[] scopes = binScope.split(",");
    for (String scope : scopes) {
      // 023100
      if (scope.indexOf("-") == -1 && scope.indexOf(")") == -1) {
        if (binCode.length() >= scope.length() && binCode.startsWith(scope))
          return true;
      }

      // 020310(1/2/3)
      else if (scope.indexOf("-") == -1 && scope.indexOf(")") > -1) {
        String[] items = scope.split("\\(");
        if (items.length > 1) {
          String[] children = items[1].split("/");
          for (String child : children) {
            if (child.length() < 2)
              child = "0" + child;
            String newBinCode = items[0] + child;
            if (binCode.startsWith(binCode.length() > newBinCode.length() ? newBinCode : newBinCode
                .substring(0, binCode.length())))
              return true;
          }
        }
      }

      // 0200-0211
      else if (scope.indexOf("-") > -1) {
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

  /**
   * 判断两个货位范围是否有重复货位
   * 
   * @param binScope
   *          货位范围，not null <br>
   *          支持格式：023100,0200-0211,020310(1/2/3)
   * @param binScope2
   *          货位范围2，not null <br>
   *          支持格式：023100,0200-0211,020310(1/2/3)
   * @return true:有，false:没有
   */
  public static boolean hasRepeat(String binScope, String binScope2) {
    Assert.assertArgumentNotNull(binScope, "binScope");
    Assert.assertArgumentNotNull(binScope2, "binScope2");
    binScope.replace(" ", "");
    binScope2.replace(" ", "");

    String[] scopes = binScope.split(",");
    for (String scope : scopes) {
      // 023100
      if (scope.indexOf("-") == -1 && scope.indexOf(")") == -1) {
        if (contains(binScope2, scope) == false)
          continue;
        else
          return true;
      }

      // 020310(1/2/3)
      else if (scope.indexOf("-") == -1 && scope.indexOf(")") > -1) {
        String[] items = scope.split("\\(");
        if (items.length > 1) {
          String[] children = items[1].split("/");
          for (String child : children) {
            child = "0" + child;
            if (child.endsWith(")"))
              child = child.substring(0, child.length() - 1);

            if (contains(binScope2, items[0] + child) == false)
              continue;
            else
              return true;
          }
        }
      }

      // 0200-0211
      else if (scope.indexOf("-") > -1) {
        String[] items = scope.split("-");
        if (items[0].length() > items[1].length())
          items[1] = String.format("%0" + items[0].length() + "d", Integer.valueOf(items[1]));
        else
          items[0] = String.format("%0" + items[1].length() + "d", Integer.valueOf(items[0]));
        for (int binCode = Integer.valueOf(items[0]); Integer.valueOf(items[1]).compareTo(binCode) >= 0; binCode++) {
          if (contains(binScope2, String.valueOf(binCode)) == false)
            continue;
          else
            return true;
        }
      }
    }

    return false;
  }

  /**
   * 判断子货位范围 是不是存在于父货位范围里
   * 
   * @param childScope
   *          子货位范围
   * @param parentScope
   *          父货位范围
   * @return
   */
  public static boolean hasOut(String childScope, String parentScope) {

    Assert.assertArgumentNotNull(childScope, "childScope");
    Assert.assertArgumentNotNull(parentScope, "parentScope");
    childScope.replace(" ", "");
    parentScope.replace(" ", "");

    String[] childSocpes = childScope.split(",");

    for (String cs : childSocpes) {

      // 023100
      if (cs.indexOf("-") == -1 && cs.indexOf(")") == -1) {
        if (contains(parentScope, cs))
          continue;
        else
          return false;
      }
      // 020310(1/2/3)
      else if (cs.indexOf("-") == -1 && cs.indexOf(")") > -1) {
        String[] items = cs.split("\\(");
        if (items.length > 1) {
          String[] children = items[1].split("/");
          for (String child : children) {
            child = "0" + child;
            if (child.endsWith(")"))
              child = child.substring(0, child.length() - 1);

            if (contains(parentScope, items[0] + child))
              continue;
            else
              return false;
          }
        }
      }
      // 0200-0211
      else if (cs.indexOf("-") > -1) {
        String[] items = cs.split("-");
        if (items[0].length() > items[1].length())
          items[1] = String.format("%0" + items[0].length() + "d", Integer.valueOf(items[1]));
        else
          items[0] = String.format("%0" + items[1].length() + "d", Integer.valueOf(items[0]));
        for (int binCode = Integer.valueOf(items[0]); Integer.valueOf(items[1]).compareTo(binCode) >= 0; binCode++) {
          if (contains(parentScope, String.valueOf(binCode)))
            continue;
          else
            return false;
        }
      }
    }

    return true;

  }
}
