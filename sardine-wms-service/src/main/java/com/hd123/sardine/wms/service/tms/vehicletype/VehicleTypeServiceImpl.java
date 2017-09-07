/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	VehicleTypeServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月8日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.service.tms.vehicletype;

import java.text.MessageFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.tms.vehicle.Vehicle;
import com.hd123.sardine.wms.api.tms.vehicle.VehicleService;
import com.hd123.sardine.wms.api.tms.vehicletype.VehicleType;
import com.hd123.sardine.wms.api.tms.vehicletype.VehicleTypeService;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.dao.tms.vehicletype.VehicleTypeDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.log.EntityLogger;

/**
 * @author yangwenzhu
 *
 */
public class VehicleTypeServiceImpl extends BaseWMSService implements VehicleTypeService {

  @Autowired
  private VehicleTypeDao dao;
  @Autowired
  private EntityLogger logger;
  @Autowired
  private VehicleService vehicleService;

  @Override
  public String saveNew(VehicleType type) throws IllegalArgumentException, WMSException {
    Assert.assertArgumentNotNull(type, "type");

    type.validate();

    VehicleType v = dao.getByCode(type.getCode());
    if (v != null)
      throw new WMSException(MessageFormat.format("已存在代码为{0}的车型", type.getCode()));
    type.setUuid(UUIDGenerator.genUUID());
    type.setCreateInfo(ApplicationContextUtil.getOperateInfo());
    type.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());

    dao.insert(type);

    logger.injectContext(this, type.getUuid(), VehicleType.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_ADDNEW, "新增车型");

    return type.getUuid();
  }

  @Override
  public void saveModify(VehicleType type) throws IllegalArgumentException, WMSException {
    Assert.assertArgumentNotNull(type, "type");

    type.validate();

    VehicleType existV = dao.get(type.getUuid());
    if (existV == null)
      throw new WMSException(MessageFormat.format("车型{0}不存在", type.getUuid()));
    VehicleType v = dao.getByCode(type.getCode());
    if (v != null && v.getUuid().equals(type.getUuid()) == false)
      throw new WMSException(MessageFormat.format("已经存在代码为{0}的车型", v.getCode()));
    PersistenceUtils.checkVersion(type.getVersion(), v, VehicleType.CAPTION, v.getCode());

    type.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    dao.update(type);

    logger.injectContext(this, type.getUuid(), VehicleType.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "修改车型");
  }

  @Override
  public void remove(String uuid, long version) throws IllegalArgumentException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(version, "version");

    VehicleType vehicleType = dao.get(uuid);
    if (vehicleType == null)
      throw new WMSException("要删除的车型" + uuid + "不存在");

    List<Vehicle> list = vehicleService.queryByType(uuid);
    if (list.isEmpty() == false)
      throw new WMSException("存在该车型的车辆，不能删除");

    PersistenceUtils.checkVersion(version, vehicleType, VehicleType.CAPTION, uuid);

    dao.remove(uuid, version);

    logger.injectContext(this, uuid, VehicleType.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_REMOVE, "删除车型");

  }

  @Override
  public PageQueryResult<VehicleType> query(PageQueryDefinition definition)
      throws IllegalArgumentException, WMSException {
    Assert.assertArgumentNotNull(definition, "definition");

    definition.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    PageQueryResult<VehicleType> pqr = new PageQueryResult<>();
    List<VehicleType> list = dao.query(definition);
    PageQueryUtil.assignPageInfo(pqr, definition);
    pqr.setRecords(list);
    return pqr;
  }

  @Override
  public VehicleType get(String uuid) throws IllegalArgumentException, WMSException {
    if (StringUtil.isNullOrBlank(uuid))
      return null;
    return dao.get(uuid);
  }

}
