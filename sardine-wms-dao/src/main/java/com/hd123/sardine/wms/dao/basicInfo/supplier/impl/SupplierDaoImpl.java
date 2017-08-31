/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	SupplierDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年1月9日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.supplier.impl;

import java.util.Map;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.supplier.Supplier;
import com.hd123.sardine.wms.common.dao.impl.BaseDaoImpl;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.dao.basicInfo.supplier.SupplierDao;

/**
 * @author fanqingqing
 *
 */
public class SupplierDaoImpl extends BaseDaoImpl<Supplier> implements SupplierDao {
  public static final String MAPPER_GETBYCODE = "getByCode";
  public static final String MAPPER_INSERT_STORAGE_AREA = "insertStorageArea";
  public static final String MAPPER_REMOVE_STORAGE_AREA = "removeStorageArea";

  @Override
  public Supplier getByCode(String code) {
    if (StringUtil.isNullOrBlank(code))
      return null;
    Map<String, Object> map = ApplicationContextUtil.mapWithParentCompanyUuid();
    map.put("code", code);
    return getSqlSession().selectOne(generateStatement(MAPPER_GETBYCODE), map);
  }

  @Override
  public void saveStorageArea(String supplierUuid, String storageArea) {
    Assert.assertArgumentNotNull(supplierUuid, "supplierUuid");
    Assert.assertArgumentNotNull(storageArea, "storageArea");

    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("supplierUuid", supplierUuid);
    map.put("storageArea", storageArea);

    getSqlSession().insert(generateStatement(MAPPER_INSERT_STORAGE_AREA), map);

  }

  @Override
  public void removeStorageArea(String supplierUuid) {
    Assert.assertArgumentNotNull(supplierUuid, "supplierUuid");
    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("supplierUuid", supplierUuid);
    getSqlSession().delete(generateStatement(MAPPER_REMOVE_STORAGE_AREA), map);

  }
}
