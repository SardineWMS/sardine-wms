/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	SupplierServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年1月9日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.service.basicInfo.supplier;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.supplier.Supplier;
import com.hd123.sardine.wms.api.basicInfo.supplier.SupplierService;
import com.hd123.sardine.wms.api.basicInfo.supplier.SupplierState;
import com.hd123.sardine.wms.common.exception.EntityNotFoundException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.common.validator.ValidateHandler;
import com.hd123.sardine.wms.common.validator.ValidateResult;
import com.hd123.sardine.wms.dao.basicInfo.supplier.SupplierDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.log.EntityLogger;

/**
 * 供应商服务实现
 * 
 * @author fanqingqing
 *
 */
public class SupplierServiceImpl extends BaseWMSService implements SupplierService {

  @Autowired
  private SupplierDao dao;
  @Autowired
  private EntityLogger logger;

  @Autowired
  private ValidateHandler<Supplier> supplierSaveNewValidateHandler;

  @Autowired
  private ValidateHandler<Supplier> supplierSaveModifyValidateHandler;

  @Override
  public Supplier get(String uuid) {
    if (StringUtil.isNullOrBlank(uuid))
      return null;
    return dao.get(uuid);
  }

  @Override
  public Supplier getByCode(String code) {
    if (StringUtil.isNullOrBlank(code))
      return null;
    return dao.getByCode(code);
  }

  @Override
  public PageQueryResult<Supplier> query(PageQueryDefinition definition)
      throws IllegalArgumentException {
    Assert.assertArgumentNotNull(definition, "definition");
    definition.setCompanyUuid(ApplicationContextUtil.getParentCompanyUuid());
    PageQueryResult<Supplier> pgr = new PageQueryResult<Supplier>();
    List<Supplier> list = dao.query(definition);
    PageQueryUtil.assignPageInfo(pgr, definition);
    pgr.setRecords(list);
    return pgr;
  }

  @Override
  public String saveNew(Supplier supplier) throws IllegalArgumentException, WMSException {
    ValidateResult saveNewResult = supplierSaveNewValidateHandler.validate(supplier);
    checkValidateResult(saveNewResult);

    Supplier dbSupplier = dao.getByCode(supplier.getCode());
    if (dbSupplier != null)
      throw new WMSException("已存在代码为" + supplier.getCode() + "的供应商");

    supplier.setUuid(UUIDGenerator.genUUID());
    supplier.setCompanyUuid(ApplicationContextUtil.getParentCompanyUuid());
    supplier.setCreateInfo(ApplicationContextUtil.getOperateInfo());
    supplier.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    dao.insert(supplier);

    logger.injectContext(this, supplier.getUuid(), Supplier.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_ADDNEW, "新增供应商");
    return supplier.getUuid();
  }

  @Override
  public void saveModify(Supplier supplier) throws IllegalArgumentException, WMSException {
    ValidateResult saveModifyResult = supplierSaveModifyValidateHandler.validate(supplier);
    checkValidateResult(saveModifyResult);

    Supplier oldSupplier = dao.get(supplier.getUuid());
    if (oldSupplier == null)
      throw EntityNotFoundException.create(Supplier.class.getName(), "uuid", supplier.getUuid());
    PersistenceUtils.checkVersion(supplier.getVersion(), oldSupplier, "供应商", supplier.getUuid());

    Supplier dbSupplier = dao.getByCode(supplier.getCode());
    if (dbSupplier != null && ObjectUtils.notEqual(supplier.getCode(), dbSupplier.getCode()))
      throw new WMSException("已存在代码为" + supplier.getCode() + "的供应商");

    supplier.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    dao.update(supplier);
    if (StringUtil.isNullOrBlank(supplier.getStorageArea()) == false) {
      dao.removeStorageArea(supplier.getUuid());
      dao.saveStorageArea(supplier.getUuid(), supplier.getStorageArea());
    }

    logger.injectContext(this, supplier.getUuid(), Supplier.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "修改供应商");
  }

  @Override
  public void remove(String uuid, long version) throws IllegalArgumentException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(version, "version");

    Supplier supplier = dao.get(uuid);
    if (supplier == null)
      throw EntityNotFoundException.create(Supplier.class.getName(), "uuid", uuid);
    PersistenceUtils.checkVersion(version, supplier, "供应商", uuid);

    supplier.setState(SupplierState.offline);
    supplier.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    dao.update(supplier);

    logger.injectContext(this, supplier.getUuid(), Supplier.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_REMOVE, "删除供应商");
  }

  @Override
  public void recover(String uuid, long version) throws IllegalArgumentException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(version, "version");

    Supplier supplier = dao.get(uuid);
    if (supplier == null)
      throw EntityNotFoundException.create(Supplier.class.getName(), "uuid", uuid);
    PersistenceUtils.checkVersion(version, supplier, "供应商", uuid);

    supplier.setState(SupplierState.online);
    supplier.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    dao.update(supplier);

    logger.injectContext(this, supplier.getUuid(), Supplier.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "恢复供应商");
  }

}
