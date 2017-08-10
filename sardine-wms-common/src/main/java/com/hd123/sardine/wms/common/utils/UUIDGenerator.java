/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * <p/>
 * 文件名：	UUIDGenerator.java
 * 模块说明：
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.utils;

import java.util.UUID;

/**
 * UUID生成器
 *
 * @author zhangsai
 */
public class UUIDGenerator {

  /**
   * 生成一个UUID
   *
   * @return 新生成的UUID
   */
  public static String genUUID() {
    UUID uuid = UUID.randomUUID();
    String str = uuid.toString();
    // 去掉"-"符号
    String temp = str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18)
        + str.substring(19, 23) + str.substring(24);
    return temp;
  }

  /**
   * 获得指定数量的UUID
   *
   * @param number
   *          需要生成的数量
   * @return 返回生成的UUID数组
   */
  public static String[] genUUID(int number) {
    if (number < 1) {
      return null;
    }
    String[] ss = new String[number];
    for (int i = 0; i < number; i++) {
      ss[i] = genUUID();
    }
    return ss;
  }
  
  public static void main(String[] args) {
    String[] uuids = genUUID(11);
    for(String uuid : uuids) {
      System.out.println(uuid);
    }
  }

}
