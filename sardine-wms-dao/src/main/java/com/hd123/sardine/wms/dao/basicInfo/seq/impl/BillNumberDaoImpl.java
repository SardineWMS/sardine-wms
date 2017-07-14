/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	FlowCodeDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年7月13日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.seq.impl;

import java.util.HashMap;
import java.util.Map;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.dao.NameSpaceSupport;
import com.hd123.sardine.wms.dao.basicInfo.seq.BillNumberDao;

/**
 * @author zhangsai
 *
 */
public class BillNumberDaoImpl extends NameSpaceSupport implements BillNumberDao {

  private static final String GETMAXSEQUENCEVALUEFORUPDATE = "getMaxSequenceValueForUpdate";
  private static final String INSERTSEQUENCE = "insertSequence";
  private static final String UPDATESEQUENCEVALUE = "updateSequenceValue";

  @Override
  public String getMaxSequenceValueForUpdate(String sequenceName, String companyUuid) {
    Assert.assertArgumentNotNull(sequenceName, "sequenceName");
    Assert.assertArgumentNotNull(companyUuid, "companyUuid");

    Map<String, String> map = new HashMap<String, String>();
    map.put("sequenceName", sequenceName);
    map.put("companyUuid", companyUuid);
    return selectOne(GETMAXSEQUENCEVALUEFORUPDATE, map);
  }

  @Override
  public void insertSequence(String sequenceName, String value, String companyUuid) {
    Assert.assertArgumentNotNull(sequenceName, "sequenceName");
    Assert.assertArgumentNotNull(companyUuid, "companyUuid");
    Assert.assertArgumentNotNull(value, "value");

    Map<String, String> map = new HashMap<String, String>();
    map.put("sequenceName", sequenceName);
    map.put("companyUuid", companyUuid);
    map.put("value", value);
    
    insert(INSERTSEQUENCE, map);
  }

  @Override
  public void updateSequenceValue(String sequenceName, String value, String companyUuid) {
    Assert.assertArgumentNotNull(sequenceName, "sequenceName");
    Assert.assertArgumentNotNull(companyUuid, "companyUuid");
    Assert.assertArgumentNotNull(value, "value");

    Map<String, String> map = new HashMap<String, String>();
    map.put("sequenceName", sequenceName);
    map.put("companyUuid", companyUuid);
    map.put("value", value);
    
    update(UPDATESEQUENCEVALUE, map);
  }

  // private static final String GETMAXBILLNUMBERFORUPDATE =
  // "getMaxBillNumberForUpdate";
  // private static final String GETMAXTASKNUMBERFORUPDATE =
  // "getMaxTaskNumberForUpdate";
  // private static final String GETMAXSTOCKBATCHFORUPDATE =
  // "getMaxStockBatchForUpdate";
  // private static final String GETMAXPATHUUID = "getMaxPathUuid";
  // private static final String GETMAXPATHFORUPDATE = "getMaxPathForUpdate";
  // private static final String GETMAXSHELFFORUPDATE = "getMaxShelfForUpdate";
  // private static final String GETMAXWRHUUIDFORUPDATE =
  // "getMaxWrhUuidForUpdate";
  // private static final String GETMAXCOMPANYUUIDFORUPDATE =
  // "getMaxCompanyUuidForUpdate";
  // private static final String GETMAXCONTAINERBARCODE =
  // "getMaxContainerBarcode";
  // private static final String FETCHRESOURCELOCK = "fetchResourceLock";

  // @Override
  // public String getMaxBillNumberForUpdate(String billType, String
  // companyUuid) {
  // if (StringUtil.isNullOrBlank(companyUuid) ||
  // StringUtil.isNullOrBlank(billType))
  // return null;
  //
  // Map<String, String> map = new HashMap<String, String>();
  // map.put("billType", billType);
  // map.put("companyUuid", companyUuid);
  // String maxBillNumber = selectOne(GETMAXBILLNUMBERFORUPDATE, map);
  // if (StringUtil.isNullOrBlank(maxBillNumber)) {
  // selectList(FETCHRESOURCELOCK, null);
  // maxBillNumber = selectOne(GETMAXBILLNUMBERFORUPDATE, map);
  // }
  // return maxBillNumber;
  // }
  //
  // @Override
  // public String getMaxTaskNumberForUpdate(TaskType taskType, String
  // companyUuid) {
  // if (taskType == null || StringUtil.isNullOrBlank(companyUuid))
  // return null;
  //
  // Map<String, String> map = new HashMap<String, String>();
  // map.put("taskType", taskType.name());
  // map.put("companyUuid", companyUuid);
  // String maxBillNumber = selectOne(GETMAXTASKNUMBERFORUPDATE, map);
  // if (StringUtil.isNullOrBlank(maxBillNumber)) {
  // selectList(FETCHRESOURCELOCK, null);
  // maxBillNumber = selectOne(GETMAXTASKNUMBERFORUPDATE, map);
  // }
  // return maxBillNumber;
  // }
  //
  // @Override
  // public String getMaxStockBatchForUpdate(String companyUuid) {
  // if (StringUtil.isNullOrBlank(companyUuid))
  // return null;
  //
  // String maxStockBatch = selectOne(GETMAXSTOCKBATCHFORUPDATE, companyUuid);
  // if (StringUtil.isNullOrBlank(maxStockBatch)) {
  // selectList(FETCHRESOURCELOCK, null);
  // maxStockBatch = selectOne(GETMAXSTOCKBATCHFORUPDATE, companyUuid);
  // }
  // return maxStockBatch;
  // }
  //
  // @Override
  // public String getMaxPathForUpdate(String zoneUuid, String companyUuid) {
  // if (StringUtil.isNullOrBlank(zoneUuid) ||
  // StringUtil.isNullOrBlank(companyUuid))
  // return null;
  //
  // Map<String, String> map = new HashMap<String, String>();
  // map.put("zoneUuid", zoneUuid);
  // map.put("companyUuid", companyUuid);
  // String maxPathCode = selectOne(GETMAXPATHUUID, map);
  // map.put("code", maxPathCode);
  // if (StringUtil.isNullOrBlank(maxPathCode)) {
  // selectList(FETCHRESOURCELOCK, null);
  // maxPathCode = selectOne(GETMAXPATHFORUPDATE, map);
  // } else {
  // maxPathCode = selectOne(GETMAXPATHFORUPDATE, map);
  // }
  // return maxPathCode;
  // }
  //
  // @Override
  // public String getMaxShelfForUpdate(String pathUuid, String companyUuid) {
  // if (StringUtil.isNullOrBlank(pathUuid) ||
  // StringUtil.isNullOrBlank(companyUuid))
  // return null;
  //
  // Map<String, String> map = new HashMap<String, String>();
  // map.put("pathUuid", pathUuid);
  // map.put("companyUuid", companyUuid);
  // String maxShelfCode = selectOne(GETMAXSHELFFORUPDATE, map);
  // if (StringUtil.isNullOrBlank(maxShelfCode)) {
  // selectList(FETCHRESOURCELOCK, null);
  // maxShelfCode = selectOne(GETMAXSHELFFORUPDATE, map);
  // }
  // return maxShelfCode;
  // }
  //
  // @Override
  // public String getMaxWrhUuidForUpdate(String companyUuid) {
  // if (StringUtil.isNullOrBlank(companyUuid))
  // return null;
  //
  // String maxWareHouseUuid = selectOne(GETMAXWRHUUIDFORUPDATE, companyUuid);
  // if (StringUtil.isNullOrBlank(maxWareHouseUuid)) {
  // selectList(FETCHRESOURCELOCK, null);
  // maxWareHouseUuid = selectOne(GETMAXWRHUUIDFORUPDATE, companyUuid);
  // }
  // return maxWareHouseUuid;
  // }
  //
  // @Override
  // public String getMaxCompanyUuidForUpdate() {
  // String maxCompanyUuid = selectOne(GETMAXCOMPANYUUIDFORUPDATE, null);
  // if (StringUtil.isNullOrBlank(maxCompanyUuid)) {
  // selectList(FETCHRESOURCELOCK, null);
  // maxCompanyUuid = selectOne(GETMAXCOMPANYUUIDFORUPDATE, null);
  // }
  // return maxCompanyUuid;
  // }
  //
  // @Override
  // public String getMaxContainerBarcode(String containerTypeUuid, String
  // companyUuid) {
  // if (StringUtil.isNullOrBlank(containerTypeUuid) ||
  // StringUtil.isNullOrBlank(companyUuid))
  // return null;
  //
  // Map<String, String> map = new HashMap<String, String>();
  // map.put("containerTypeUuid", containerTypeUuid);
  // map.put("companyUuid", companyUuid);
  // String maxContainerBarcode = selectOne(GETMAXCONTAINERBARCODE, map);
  // if (StringUtil.isNullOrBlank(maxContainerBarcode)) {
  // selectList(FETCHRESOURCELOCK, null);
  // maxContainerBarcode = selectOne(GETMAXCONTAINERBARCODE, map);
  // }
  // return maxContainerBarcode;
  // }
}
