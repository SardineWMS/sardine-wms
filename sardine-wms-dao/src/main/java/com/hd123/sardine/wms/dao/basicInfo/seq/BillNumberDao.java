/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	FlowCodeDao.java
 * 模块说明：	
 * 修改历史：
 * 2017年7月12日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.seq;

/**
 * @author zhangsai
 *
 */
public interface BillNumberDao {

/*  String getMaxBillNumberForUpdate(String billType, String companyUuid);

  String getMaxTaskNumberForUpdate(TaskType taskType, String companyUuid);

  String getMaxStockBatchForUpdate(String companyUuid);

  String getMaxPathForUpdate(String zoneUuid, String companyUuid);

  String getMaxShelfForUpdate(String pathUuid, String companyUuid);

  String getMaxWrhUuidForUpdate(String companyUuid);

  String getMaxCompanyUuidForUpdate();

  String getMaxContainerBarcode(String containerTypeUuid, String companyUuid);*/

  String getMaxSequenceValueForUpdate(String sequenceName, String companyUuid);
  
  void insertSequence(String sequenceName, String value, String companyUuid);
  
  void updateSequenceValue(String sequenceName, String value, String companyUuid);
}
