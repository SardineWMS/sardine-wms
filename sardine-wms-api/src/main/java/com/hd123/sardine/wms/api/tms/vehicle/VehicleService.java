/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	VehicleService.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月7日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.tms.vehicle;

import java.util.List;

import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * @author yangwenzhu
 *
 */
public interface VehicleService {
  public static final String QUERY_CODE_LIKE = "code";
  public static final String QUERY_STATE_EQUALS = "state";
  public static final String QUERY_VEHICLENO_LIKE = "vehicleNo";
  public static final String QUERY_VEHICLETYPECODE = "vehicleTypeCode";
  public static final String QUERY_CARRIER_CODE = "carrierCode";

  /**
   * 新增车辆
   * 
   * @param vehicle
   *          车辆，not null
   * @return 新增车辆的UUID
   * @throws IllegalArgumentException
   *           vehicle为空时抛出，
   * @throws WMSException
   */
  String saveNew(Vehicle vehicle) throws IllegalArgumentException, WMSException;

  /**
   * 修改车辆
   * 
   * @param vehicle
   *          车辆，not null
   * @throws IllegalArgumentException
   *           vehicle为空时抛出
   * @throws WMSException
   */
  void saveModify(Vehicle vehicle) throws IllegalArgumentException, WMSException;

  /**
   * 启用车辆
   * 
   * @param uuid
   *          uuid, not null
   * @param version
   *          version,not null
   * @throws IllegalArgumentException
   *           uuid,version 为空时抛出
   * @throws VersionConflictException
   *           版本冲突
   * @throws WMSException
   */
  void online(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException;

  /**
   * 停用车辆
   * 
   * @param uuid
   *          uuid, not null
   * @param verison
   *          version,not null
   * @throws IllegalArgumentException
   *           uuid, version 为空时抛出
   * @throws VersionConflictException
   *           版本冲突
   * @throws WMSException
   */
  void offline(String uuid, long verison)
      throws IllegalArgumentException, VersionConflictException, WMSException;

  /**
   * 分页查询车辆
   * 
   * @param definition
   *          查询条件
   * @return 分页结果集
   * @throws IllegalArgumentException
   *           definition为空时抛出
   * @throws WMSException
   */
  PageQueryResult<Vehicle> query(PageQueryDefinition definition)
      throws IllegalArgumentException, WMSException;

  /**
   * 根据代码获取车辆
   * 
   * @param code
   *          code为空，则返回空
   * @return 车辆信息
   */
  Vehicle getByCode(String code);

  /**
   * 根据类型查询车辆
   * 
   * @param typeUuid
   *          类型UUID， 若为空，则返回空集合
   * @return 车辆集合
   * @throws IllegalArgumentException
   * @throws WMSException
   */
  List<Vehicle> queryByType(String typeUuid) throws IllegalArgumentException, WMSException;

  /**
   * 根据车牌号查询车龄
   * 
   * @param vehicleNo
   *          车牌号，若为空，则返回null
   * @return 车辆信息
   * @throws IllegalArgumentException
   * @throws WMSException
   */
  Vehicle getByVehicleNo(String vehicleNo) throws IllegalArgumentException, WMSException;

  /**
   * 根据UUID获取车辆
   * 
   * @param uuid
   *          uuid,若为空，则返回空
   * @return 车辆信息
   * @throws IllegalArgumentException
   * @throws WMSException
   */
  Vehicle get(String uuid) throws IllegalArgumentException, WMSException;
}
