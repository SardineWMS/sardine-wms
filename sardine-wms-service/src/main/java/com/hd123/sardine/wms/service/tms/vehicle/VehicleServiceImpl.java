/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	VehicleServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月8日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.service.tms.vehicle;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.tms.carrier.Carrier;
import com.hd123.sardine.wms.api.tms.carrier.CarrierService;
import com.hd123.sardine.wms.api.tms.carrier.CarrierState;
import com.hd123.sardine.wms.api.tms.vehicle.Vehicle;
import com.hd123.sardine.wms.api.tms.vehicle.VehicleService;
import com.hd123.sardine.wms.api.tms.vehicle.VehicleState;
import com.hd123.sardine.wms.api.tms.vehicletype.VehicleType;
import com.hd123.sardine.wms.api.tms.vehicletype.VehicleTypeService;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.dao.tms.vehicle.VehicleDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.log.EntityLogger;

/**
 * @author yangwenzhu
 *
 */
public class VehicleServiceImpl extends BaseWMSService implements VehicleService {

  @Autowired
  private VehicleDao dao;
  @Autowired
  private EntityLogger logger;
  @Autowired
  private CarrierService carrierService;
  @Autowired
  private VehicleTypeService vehicleTypeService;

  @Override
  public String saveNew(Vehicle vehicle) throws IllegalArgumentException, WMSException {
    Assert.assertArgumentNotNull(vehicle, "vehicle");

    vehicle.validate();
    Carrier carrier = carrierService.get(vehicle.getCarrier().getUuid());
    vehicle.setCarrier(new UCN(carrier.getUuid(), carrier.getCode(), carrier.getName()));
    VehicleType vehicleType = vehicleTypeService.get(vehicle.getVehicleType().getUuid());
    vehicle.setVehicleType(
        new UCN(vehicleType.getUuid(), vehicleType.getCode(), vehicleType.getName()));
    Vehicle v = getByCode(vehicle.getCode());
    if (v != null)
      throw new WMSException("已存在代码为" + vehicle.getCode() + "的车辆");
    v = getByVehicleNo(vehicle.getVehicleNo());
    if (v != null)
      throw new WMSException("已存在车牌号为" + vehicle.getVehicleNo() + "的车辆");

    vehicle.setUuid(UUIDGenerator.genUUID());
    vehicle.setState(VehicleState.free);
    vehicle.setCreateInfo(ApplicationContextUtil.getOperateInfo());
    vehicle.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    dao.insert(vehicle);

    logger.injectContext(this, vehicle.getUuid(), Vehicle.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_ADDNEW, "新增车辆");
    return vehicle.getUuid();
  }

  @Override
  public void saveModify(Vehicle vehicle) throws IllegalArgumentException, WMSException {
    Assert.assertArgumentNotNull(vehicle, "vehicle");

    vehicle.validate();
    Carrier carrier = carrierService.get(vehicle.getCarrier().getUuid());
    if (carrier == null)
      throw new WMSException("承运商" + vehicle.getCarrier().getUuid() + "不存在");
    vehicle.setCarrier(new UCN(carrier.getUuid(), carrier.getCode(), carrier.getName()));
    VehicleType vehicleType = vehicleTypeService.get(vehicle.getVehicleType().getUuid());
    if (vehicleType == null)
      throw new WMSException("车型" + vehicle.getVehicleType().getUuid() + "不存在");
    vehicle.setVehicleType(
        new UCN(vehicleType.getUuid(), vehicleType.getCode(), vehicleType.getName()));
    Vehicle v = dao.get(vehicle.getUuid());
    if (v == null)
      throw new WMSException("车辆" + vehicle.getUuid() + "不存在");
    if (VehicleState.free.equals(v.getState()) == false)
      throw new WMSException("非空闲车辆，不能修改");
    Vehicle exsitV = getByCode(vehicle.getCode());
    if (exsitV != null && v.getUuid().equals(exsitV.getUuid()) == false)
      throw new WMSException("已存在代码为" + exsitV.getCode() + "的车辆");
    exsitV = getByVehicleNo(vehicle.getVehicleNo());
    if (exsitV != null && v.getUuid().equals(exsitV.getUuid()) == false)
      throw new WMSException("已存在车牌号为" + exsitV.getVehicleNo() + "的车辆");

    PersistenceUtils.checkVersion(vehicle.getVersion(), v, Vehicle.CAPTION, v.getCode());
    vehicle.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    dao.update(vehicle);

    logger.injectContext(this, vehicle.getUuid(), Vehicle.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "修改车辆信息");
  }

  @Override
  public void online(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(version, "version");

    Vehicle v = dao.get(uuid);
    if (v == null)
      throw new WMSException("要启用的车辆" + uuid + "不存在");
    if (VehicleState.free.equals(v.getState()))
      return;
    if (VehicleState.offline.equals(v.getState()) == false)
      throw new WMSException("车辆当前状态为" + v.getState().getCaption() + "，只有“停用”状态的车辆才能启用");

    Carrier carrier = carrierService.get(v.getCarrier().getUuid());
    if (carrier == null)
      throw new WMSException("车辆对应的承运商不存在，车辆无法启用");
    if (CarrierState.offline.equals(carrier.getState()))
      throw new WMSException("车辆对应的承运商已停用，车辆无法启用");
    PersistenceUtils.checkVersion(version, v, Vehicle.CAPTION, uuid);

    v.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    v.setState(VehicleState.free);
    dao.update(v);
    logger.injectContext(this, uuid, Vehicle.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "启用车辆");
  }

  @Override
  public void offline(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(version, "version");

    Vehicle v = dao.get(uuid);
    if (v == null)
      throw new WMSException("要停用的车辆不存在");
    if (VehicleState.offline.equals(v.getState()))
      return;
    if (VehicleState.free.equals(v.getState()) == false)
      throw new WMSException("车辆当前状态为" + v.getState().getCaption() + "，只有“空闲”状态的车辆才能被停用");

    PersistenceUtils.checkVersion(version, v, Vehicle.CAPTION, uuid);
    v.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    v.setState(VehicleState.offline);
    dao.update(v);
    logger.injectContext(this, uuid, Vehicle.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "停用车辆");

  }

  @Override
  public PageQueryResult<Vehicle> query(PageQueryDefinition definition)
      throws IllegalArgumentException, WMSException {
    Assert.assertArgumentNotNull(definition, "definition");

    definition.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    PageQueryResult<Vehicle> qpr = new PageQueryResult<>();
    List<Vehicle> list = dao.query(definition);
    PageQueryUtil.assignPageInfo(qpr, definition);
    qpr.setRecords(list);
    return qpr;
  }

  @Override
  public Vehicle getByCode(String code) {
    if (StringUtil.isNullOrBlank(code))
      return null;
    return dao.getByCode(code);
  }

  @Override
  public List<Vehicle> queryByType(String typeUuid) throws IllegalArgumentException, WMSException {
    if (StringUtil.isNullOrBlank(typeUuid))
      return new ArrayList<Vehicle>();
    return dao.queryByType(typeUuid);
  }

  @Override
  public Vehicle getByVehicleNo(String vehicleNo) throws IllegalArgumentException, WMSException {
    if (StringUtil.isNullOrBlank(vehicleNo))
      return null;
    return dao.getByVehicleNo(vehicleNo);
  }

  @Override
  public Vehicle get(String uuid) throws IllegalArgumentException, WMSException {
    if (StringUtil.isNullOrBlank(uuid))
      return null;
    return dao.get(uuid);
  }

}
