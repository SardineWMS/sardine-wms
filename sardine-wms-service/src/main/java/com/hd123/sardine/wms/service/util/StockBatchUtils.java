/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-common
 * 文件名：	StockBatchUtils.java
 * 模块说明：	
 * 修改历史：
 * 2014-6-6 - WUJING - 创建。
 */
package com.hd123.sardine.wms.service.util;

import java.util.Date;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;

/**
 * @author WUJING
 * 
 */
public class StockBatchUtils {
  public static final String DATEFORMAT = "yyyyMMdd";
  public static final int STOCKBATCH_FLOW_LEN = 6;
  /** 批次前缀 */
  public static final String WMS_STOCKBATCH_PREFIX = "WC";

  public static String genStockBatch() {
    String currentDateStr = StringUtil.dateToString(new Date(), DATEFORMAT);
    String sequenceType = currentDateStr + WMS_STOCKBATCH_PREFIX;
    String flowCode = FlowCodeGenerator.getInstance().allocate(sequenceType,
        ApplicationContextUtil.getCompanyUuid(), STOCKBATCH_FLOW_LEN);
    String stockBatch = WMS_STOCKBATCH_PREFIX + currentDateStr + flowCode;
    return stockBatch;
  }

  public static String genProductionBatch(Date date) {
    Assert.assertArgumentNotNull(date, "date");
    return StringUtil.dateToString(date, "yyyyMMdd");
  }
}
