/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	BillNumberGenerator.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月2日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.Constants;
import com.hd123.sardine.wms.dao.basicInfo.seq.BillNumberDao;

/**
 * 单号生成器
 * 
 * @author zhangsai
 *
 */
public class BillNumberGenerator {

  public static final String DATEFORMAT = "yyMMdd";

  public static final String FIRST_NUMBER = "000001";

  public static final String FIRST_PATH = "01";

  public static final int PATHCODE_LENGTH = 4;

  public static final String FIRST_SHELF = "01";

  public static final int SHELFCODE_LENGTH = 6;

  public static final int COMPANYUUID_FLOW_LENGTH = 4;

  public static final String FIRST_COMPANYUUID_FLOWCODE = "0001";

  public static final String FIRST_WAREHOUSE_FLOWCODE = "01";

  public static final int WAREHOUSE_FLOWCODE_LENGTH = 2;

  public static final String START_CONTAINERBARCODE = "1";

  public static final String DEFAULT_SEQENCENAME = "default";

  public static final String DEFAULT_COMPANYUUID = "sardine";

  public static final String DEFAULT_COMPANY_SEQUENCENAME = "company";

  @Autowired
  private BillNumberDao billNumberDao;

  public String allocateNextShelfCode(String pathCode) {
    Assert.assertArgumentNotNull(pathCode, "pathCode");

    String nextShelfCode = null;
    String companyUuid = ApplicationContextUtil.getCompanyUuid();
    String maxShelfCode = billNumberDao.getMaxSequenceValueForUpdate(pathCode, companyUuid);
    if (StringUtil.isNullOrBlank(maxShelfCode)) {
      billNumberDao.getMaxSequenceValueForUpdate(DEFAULT_SEQENCENAME, DEFAULT_SEQENCENAME);
      maxShelfCode = billNumberDao.getMaxSequenceValueForUpdate(pathCode, companyUuid);
    }

    if (StringUtil.isNullOrBlank(maxShelfCode)) {
      nextShelfCode = pathCode + FIRST_SHELF;
      billNumberDao.insertSequence(pathCode, nextShelfCode, companyUuid);
    } else {
      nextShelfCode = String.format("%0" + SHELFCODE_LENGTH + "d",
          Long.valueOf(maxShelfCode) + 1);
      billNumberDao.updateSequenceValue(pathCode, nextShelfCode, companyUuid);
    }
    return nextShelfCode;
  }

  public String allocateNextPathCode(String zoneCode) {
    Assert.assertArgumentNotNull(zoneCode, "zoneCode");

    String nextPathCode = null;
    String companyUuid = ApplicationContextUtil.getCompanyUuid();
    String maxPathCode = billNumberDao.getMaxSequenceValueForUpdate(zoneCode, companyUuid);
    if (StringUtil.isNullOrBlank(maxPathCode)) {
      billNumberDao.getMaxSequenceValueForUpdate(DEFAULT_SEQENCENAME, DEFAULT_SEQENCENAME);
      maxPathCode = billNumberDao.getMaxSequenceValueForUpdate(zoneCode, companyUuid);
    }

    if (StringUtil.isNullOrBlank(maxPathCode)) {
      nextPathCode = zoneCode + FIRST_PATH;
      billNumberDao.insertSequence(zoneCode, nextPathCode, companyUuid);
    } else {
      nextPathCode = String.format("%0" + PATHCODE_LENGTH + "d", Long.valueOf(maxPathCode) + 1);
      billNumberDao.updateSequenceValue(zoneCode, nextPathCode, companyUuid);
    }
    return nextPathCode;
  }

  public String allocateCompanyUuid() {
    String maxCompanyUuid = billNumberDao.getMaxSequenceValueForUpdate(DEFAULT_COMPANY_SEQUENCENAME,
        DEFAULT_COMPANYUUID);
    String nextCompanyUuid = null;
    if (StringUtil.isNullOrBlank(maxCompanyUuid)) {
      billNumberDao.getMaxSequenceValueForUpdate(DEFAULT_SEQENCENAME, DEFAULT_SEQENCENAME);
      maxCompanyUuid = billNumberDao.getMaxSequenceValueForUpdate(DEFAULT_COMPANY_SEQUENCENAME,
          DEFAULT_COMPANYUUID);
    }

    if (StringUtil.isNullOrBlank(maxCompanyUuid)) {
      nextCompanyUuid = Constants.RESOURCE_PREFIX + FIRST_COMPANYUUID_FLOWCODE;
      billNumberDao.insertSequence(DEFAULT_COMPANY_SEQUENCENAME, nextCompanyUuid,
          DEFAULT_COMPANYUUID);
    } else {
      String flowCode = maxCompanyUuid.replaceAll(Constants.RESOURCE_PREFIX, "");
      String newFlowCode = String.format("%0" + COMPANYUUID_FLOW_LENGTH + "d",
          Long.valueOf(flowCode) + 1);
      nextCompanyUuid = Constants.RESOURCE_PREFIX + newFlowCode;
      billNumberDao.updateSequenceValue("company", nextCompanyUuid, DEFAULT_COMPANYUUID);
    }

    return nextCompanyUuid;
  }

  public String allocateNextWareHouseUuid() {
    String companyUuid = ApplicationContextUtil.getParentCompanyUuid();
    String maxWarehouseUuid = billNumberDao.getMaxSequenceValueForUpdate(companyUuid, companyUuid);
    String nextWarehouseUuid = null;
    if (StringUtil.isNullOrBlank(maxWarehouseUuid)) {
      billNumberDao.getMaxSequenceValueForUpdate(DEFAULT_SEQENCENAME, DEFAULT_SEQENCENAME);
      maxWarehouseUuid = billNumberDao.getMaxSequenceValueForUpdate(companyUuid, companyUuid);
    }

    if (StringUtil.isNullOrBlank(maxWarehouseUuid)) {
      nextWarehouseUuid = ApplicationContextUtil.getParentCompanyUuid() + FIRST_WAREHOUSE_FLOWCODE;
      billNumberDao.insertSequence(companyUuid, nextWarehouseUuid, companyUuid);
    } else {
      String flowCode = maxWarehouseUuid.replaceAll(companyUuid, "");
      String newFlowCode = String.format("%0" + WAREHOUSE_FLOWCODE_LENGTH + "d",
          Long.valueOf(flowCode) + 1);
      nextWarehouseUuid = companyUuid + newFlowCode;
      billNumberDao.updateSequenceValue(companyUuid, nextWarehouseUuid, companyUuid);
    }
    return nextWarehouseUuid;
  }

  public String allocateNextContainerBarcode(String prefix, int length) {
    Assert.assertArgumentNotNull(prefix, "prefix");

    String nextContainerBarcode = null;
    String companyUuid = ApplicationContextUtil.getCompanyUuid();
    String maxContainerBarcode = billNumberDao.getMaxSequenceValueForUpdate(prefix, companyUuid);
    if (StringUtil.isNullOrBlank(maxContainerBarcode)) {
      billNumberDao.getMaxSequenceValueForUpdate(DEFAULT_SEQENCENAME, DEFAULT_SEQENCENAME);
      maxContainerBarcode = billNumberDao.getMaxSequenceValueForUpdate(prefix, companyUuid);
    }
    if (StringUtil.isNullOrBlank(maxContainerBarcode)) {
      nextContainerBarcode = prefix + String.format("%0" + length + "d", Long.valueOf(START_CONTAINERBARCODE));
      billNumberDao.insertSequence(prefix, nextContainerBarcode, companyUuid);
    } else {
      nextContainerBarcode = prefix + String.format("%0" + length + "d",
          Long.valueOf(maxContainerBarcode.replaceAll(prefix, "")) + 1);
      billNumberDao.updateSequenceValue(prefix, nextContainerBarcode, companyUuid);
    }

    return nextContainerBarcode;
  }

  public String allocateNextBillNumber(String sequenceName) {
    Assert.assertArgumentNotNull(sequenceName, "sequenceName");

    String companyUuid = ApplicationContextUtil.getCompanyUuid();
    String maxSequenceValue = billNumberDao.getMaxSequenceValueForUpdate(sequenceName, companyUuid);
    if (StringUtil.isNullOrBlank(maxSequenceValue)) {
      billNumberDao.getMaxSequenceValueForUpdate(DEFAULT_SEQENCENAME, DEFAULT_SEQENCENAME);
      maxSequenceValue = billNumberDao.getMaxSequenceValueForUpdate(sequenceName, companyUuid);
    }

    String nextSequenceValue = null;
    String dateString = StringUtil.dateToString(new Date(), DATEFORMAT);
    if (StringUtil.isNullOrBlank(maxSequenceValue)) {
      nextSequenceValue = ApplicationContextUtil.getCompanyCode() + dateString + FIRST_NUMBER;
      billNumberDao.insertSequence(sequenceName, nextSequenceValue, companyUuid);
    } else {
      if (maxSequenceValue.indexOf(ApplicationContextUtil.getCompanyCode() + dateString) >= 0) {
        nextSequenceValue = String.valueOf(Long.valueOf(maxSequenceValue) + 1);
        billNumberDao.updateSequenceValue(sequenceName, nextSequenceValue, companyUuid);
      } else {
        nextSequenceValue = ApplicationContextUtil.getCompanyCode() + dateString + FIRST_NUMBER;
        billNumberDao.updateSequenceValue(sequenceName, nextSequenceValue, companyUuid);
      }
    }
    return nextSequenceValue;
  }
}
