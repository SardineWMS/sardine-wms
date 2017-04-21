/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-common
 * 文件名：	QpcHelper.java
 * 模块说明：	
 * 修改历史：
 * 2014-5-23 - Gao JingYu - 创建。
 */
package com.hd123.sardine.wms.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.hd123.rumba.commons.lang.Assert;

/**
 * 包装规格工具类。
 * 
 * @author Gao JingYu
 */
public class QpcHelper {
  public static final int CASE_SCALE = 3;

  /**
   * 数量转换为件数。
   * 
   * @param qty
   *          单品数量，not null。
   * @param qpcStr
   *          包装规格，not null，格式类式于 "1*2*3"。
   * @return 件数。
   * @throws IllegalArgumentException
   */
  public static String qtyToCaseQtyStr(BigDecimal qty, String qpcStr) {
    BigDecimal dr[] = qty.abs().divideAndRemainder(qpcStrToQpc(qpcStr));
    BigDecimal i = dr[0].setScale(0);
    BigDecimal j = dr[1].setScale(3, RoundingMode.HALF_UP);

    BigDecimal middlerQpcStr = qpcStrToMiddleQpc(qpcStr);
    BigDecimal mdr[] = j.divideAndRemainder(middlerQpcStr);
    BigDecimal mdr1 = mdr[0].setScale(0);
    BigDecimal mdr2 = mdr[1].setScale(3, RoundingMode.HALF_UP);

    return concatQpcStr(i, mdr1, mdr2);
  }

  private static String concatQpcStr(BigDecimal whole, BigDecimal middle, BigDecimal part) {
    String middleStr = "";
    String wholeStr = "";
    String partStr = "";

    if (whole != null) {
      wholeStr = whole.toString();
    } else {
      wholeStr = "0";
    }

    if (middle != null) {
      middleStr = middle.toString();
    } else {
      middleStr = "0";
    }

    if (part != null) {
      partStr = part.toString();
    } else {
      partStr = "0";
    }

    int k = partStr.indexOf(".");
    if (k > 0) {
      int zeroIndex = partStr.length() - 1;
      while (partStr.charAt(zeroIndex) == '0')
        zeroIndex--;
      if (zeroIndex == k)
        zeroIndex--;
      partStr = partStr.substring(0, zeroIndex + 1);
    }

    if (middleStr.equals("0") && partStr.equals("0"))
      return wholeStr;
    else
      return wholeStr + "+" + middleStr + "+" + partStr;
  }

  /**
   * 数量转换为件数。
   * 
   * @param qty
   *          单品数量，not null。
   * @param qpc
   *          包装单品数量，not null，大于零。
   * @return 件数。
   * @throws IllegalArgumentException
   */
  /*
   * public static String qtyToCaseQtyStr(BigDecimal qty, BigDecimal qpc) {
   * Assert.assertArgumentNotNull(qty, "qty"); Assert.assertArgumentNotNull(qpc,
   * "qpc"); if (qpc.compareTo(BigDecimal.ZERO) <= 0) { throw new
   * IllegalArgumentException("qpc must be positive"); }
   * 
   * BigDecimal dr[] = qty.abs().divideAndRemainder(qpc); BigDecimal whole =
   * dr[0].setScale(0); BigDecimal part = dr[1].setScale(3);
   * 
   * return concatQpcStr(whole, BigDecimal.ZERO, part); }
   */
  /**
   * 包装规格转换为包装单品数量。
   * 
   * @param qpcStr
   *          包装规格，not null，格式类式于 "1*2*3"。
   * @return 包装单品数量
   * @throws IllegalArgumentException
   */
  public static BigDecimal qpcStrToQpc(String qpcStr) {
    Assert.assertArgumentNotNull(qpcStr, "qpcStr");
    if (qpcStr.matches("^\\d+\\*\\d+(\\.\\d+)?\\*\\d+(\\.\\d+)?$") == false) {
      throw new IllegalArgumentException("illegal format");
    }

    String[] parts = qpcStr.split("\\*");
    BigDecimal part0 = new BigDecimal(parts[0]);
    BigDecimal part1 = new BigDecimal(parts[1]);
    BigDecimal part2 = new BigDecimal(parts[2]);

    return part0.multiply(part1).multiply(part2);
  }

  /**
   * 包装规格转换为中包装单品数量。
   * 
   * @param qpcStr
   *          包装规格，not null，格式类式于 "1*2*3"。
   * @return 中包装单品数量
   * @throws IllegalArgumentException
   */
  public static BigDecimal qpcStrToMiddleQpc(String qpcStr) {
    Assert.assertArgumentNotNull(qpcStr, "qpcStr");
    if (qpcStr.matches("^\\d+\\*\\d+(\\.\\d+)?\\*\\d+(\\.\\d+)?$") == false) {
      throw new IllegalArgumentException("illegal format");
    }

    String[] parts = qpcStr.split("\\*");
    BigDecimal part2 = new BigDecimal(parts[2]);

    return part2;
  }

  /**
   * 件数相加。
   * 
   * @param addend
   *          not null。
   * @param augend
   *          not null。
   * @return 件数和
   * @throws IllegalArgumentException
   */
  public static String caseQtyStrAdd(String addend, String augend) {
    Assert.assertArgumentNotNull(addend, "addend");
    Assert.assertArgumentNotNull(augend, "augend");

    String[] addends = addend.split("\\+");
    BigDecimal addend0 = new BigDecimal(addends[0]);
    BigDecimal addend1 = BigDecimal.ZERO;
    BigDecimal addend2 = BigDecimal.ZERO;
    if (addends.length > 1)
      addend1 = new BigDecimal(addends[1]);
    if (addends.length > 2)
      addend2 = new BigDecimal(addends[2]);

    String[] augends = augend.split("\\+");
    BigDecimal augend0 = new BigDecimal(augends[0]);
    BigDecimal augend1 = BigDecimal.ZERO;
    BigDecimal augend2 = BigDecimal.ZERO;
    if (augends.length > 1)
      augend1 = new BigDecimal(augends[1]);
    if (augends.length > 2)
      augend2 = new BigDecimal(augends[2]);

    if (addend1.add(augend1).compareTo(BigDecimal.ZERO) == 0
        && addend2.add(augend2).compareTo(BigDecimal.ZERO) == 0)
      return addend0.add(augend0).toString();

    return addend0.add(augend0) + "+" + addend1.add(augend1) + "+" + addend2.add(augend2);
  }

  /**
   * 件数相减
   * <p>
   * <ul>
   * <li>(1) - (2+1+2) = 0</li>
   * <li>(5) - (2+1+2) = 3</li>
   * <li>(5+2+3) - (2+1+2) = 3+1+1</li>
   * <li>(5+2+3) - (2) = 3+2+3</li>
   * </ul>
   * 
   * @param subStr
   *          件数，not null
   * @param subedStr
   *          被减数，not null
   * @return 运算结果
   */
  public static String subtract(String subStr, String subedStr) {
    Assert.assertArgumentNotNull(subStr, "subStr");
    Assert.assertArgumentNotNull(subedStr, "subedStr");

    String result = null;
    if (compareTo(subStr, subedStr) == 0)
      return "0";

    String[] values1 = subStr.split("\\+");
    String[] values2 = subedStr.split("\\+");

    result = Integer.valueOf(values1[0]) - Integer.valueOf(values2[0]) + "";

    if (values1.length < 2)
      return result;

    if (values2.length < 2)
      return result + "+" + values1[1] + "+" + values1[2];

    int packageCount = Integer.valueOf(values1[1]) - Integer.valueOf(values2[1]);

    int qty = Integer.valueOf(values1[2]) - Integer.valueOf(values2[2]);

    if (packageCount <= 0 && qty <= 0)
      return result;

    result = result + "+" + (packageCount < 0 ? 0 : packageCount) + "+" + (qty < 0 ? 0 : qty);

    return result;
  }

  /**
   * 两个件数相比较
   * <p>
   * <ol>
   * <li>caseQtyStr > caseQtyStr2 : 1 true</li>
   * <li>caseQtyStr < caseQtyStr2 : -1 false</li>
   * <li>caseQtyStr = caseQtyStr2 : 0 false</li>
   * </ol>
   * 
   * @param caseQtyStr
   *          比较数，not null
   * @param caseQtyStr2
   *          被比较数，not null
   * @return 比较结果
   * @throws IllegalArgumentException
   */
  public static int compareTo(String caseQtyStr, String caseQtyStr2) {
    Assert.assertArgumentNotNull(caseQtyStr, "caseQtyStr");
    Assert.assertArgumentNotNull(caseQtyStr2, "caseQtyStr2");

    String[] values1 = caseQtyStr.split("\\+");
    String[] values2 = caseQtyStr2.split("\\+");

    if (Double.valueOf(values1[0]) > Double.valueOf(values2[0]))
      return 1;
    else if (Double.valueOf(values1[0]) < Double.valueOf(values2[0]))
      return -1;

    double pack1 = values1.length > 1 ? Double.valueOf(values1[1]) : 0;
    double pack2 = values2.length > 1 ? Double.valueOf(values2[1]) : 0;

    if (pack1 > pack2)
      return 1;
    else if (pack1 < pack2)
      return -1;

    double qty1 = values1.length > 2 ? Double.valueOf(values1[2]) : 0;
    double qty2 = values2.length > 2 ? Double.valueOf(values2[2]) : 0;

    if (qty1 > qty2)
      return 1;
    else if (qty1 < qty2)
      return -1;

    return 0;
  }

  public static void checkQpcStr(String qpcStr) {
    Assert.assertArgumentNotNull(qpcStr, "qpcStr");
    String[] qpcs = qpcStr.split("/*");
    if (qpcs.length != 3)
      throw new IllegalArgumentException("规格必须是1*m*n格式的。");

    try {
      Integer.valueOf(qpcs[0]);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("规格必须是1*m*n格式的。");
    }

    try {
      Integer.valueOf(qpcs[1]);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("规格必须是1*m*n格式的。");
    }

    try {
      Integer.valueOf(qpcs[2]);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("规格必须是1*m*n格式的。");
    }
  }
}
