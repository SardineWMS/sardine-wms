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

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.dao.basicInfo.seq.BillNumberDao;

/**
 * @author WUJING
 * 
 */
public class StockBatchUtils {
  public static final String DATEFORMAT = "yyMMdd";
  public static final int STOCKBATCH_FLOW_LEN = 6;
  /** 批次前缀 */
  public static final String STOCKBATCH_PREFIX = "WC";
  public static final String STOCKBATCH_SEQUENCENAME = "stockbatch";
  public static final int STOCKBATCH_FLOWCODE_LENGTH = 12;

  @Autowired
  private BillNumberDao billNumberDao;

  public String genStockBatch() {
    String currentDateStr = StringUtil.dateToString(new Date(), DATEFORMAT);
    String companyUuid = ApplicationContextUtil.getCompanyUuid();
    String maxStockBatch = billNumberDao.getMaxSequenceValueForUpdate(STOCKBATCH_SEQUENCENAME,
        companyUuid);

    String nextStockBatch = null;
    if (StringUtil.isNullOrBlank(maxStockBatch)) {
      billNumberDao.getMaxSequenceValueForUpdate(BillNumberGenerator.DEFAULT_SEQENCENAME,
          BillNumberGenerator.DEFAULT_SEQENCENAME);
      maxStockBatch = billNumberDao.getMaxSequenceValueForUpdate(STOCKBATCH_SEQUENCENAME,
          companyUuid);
    }

    if (StringUtil.isNullOrBlank(maxStockBatch)) {
      nextStockBatch = STOCKBATCH_PREFIX + currentDateStr + BillNumberGenerator.FIRST_NUMBER;
      billNumberDao.insertSequence(STOCKBATCH_SEQUENCENAME, nextStockBatch, companyUuid);
    } else {
      if (maxStockBatch.indexOf(STOCKBATCH_PREFIX + currentDateStr) >= 0) {
        nextStockBatch = STOCKBATCH_PREFIX + String.format("%0" + STOCKBATCH_FLOWCODE_LENGTH + "d",
            Long.valueOf(maxStockBatch.replaceAll(STOCKBATCH_PREFIX, "")) + 1);
        billNumberDao.updateSequenceValue(STOCKBATCH_SEQUENCENAME, nextStockBatch, companyUuid);
      } else {
        nextStockBatch = STOCKBATCH_PREFIX + currentDateStr + BillNumberGenerator.FIRST_NUMBER;
        billNumberDao.updateSequenceValue(STOCKBATCH_SEQUENCENAME, nextStockBatch, companyUuid);
      }
    }
    return nextStockBatch;
  }

  public String genProductionBatch(Date date) {
    Assert.assertArgumentNotNull(date, "date");
    return StringUtil.dateToString(date, "yyyyMMdd");
  }
}
