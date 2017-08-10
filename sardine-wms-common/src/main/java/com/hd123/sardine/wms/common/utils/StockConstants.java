/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	StockConstants.java
 * 模块说明：	
 * 修改历史：
 * 2017年7月21日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.utils;

/**
 * 库存属性为空时，使用该常量代替
 * 
 * @author zhangsai
 *
 */
public class StockConstants {
  /** 虚拟生产日期，用于不管理保质期的商品和未知生产日期的库存 */
  public static final String VISUAL_MAXDATE = "8888-12-31";
  /** 用于未知货位的库存 */
  public static final String VISUAL_STOCK_BINCODE = "-";
  /** 用于不管理容器的货位和未知容器的库存 */
  public static final String VISUAL_STOCK_CONTAINERBARCODE = "-";
  /** 用于未知供应商的库存 */
  public static final String VISUAL_STOCK_SUPPLIERUUID = "-";
  /** 用于未知批次的库存 */
  public static final String VISUAL_STOCK_STOCKBATCH = "-";
}
