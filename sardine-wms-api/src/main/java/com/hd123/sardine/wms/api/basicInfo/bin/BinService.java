/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	BinService.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月8日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.bin;

import java.util.List;

import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * 仓位、货区、货道、货架、货位 服务接口
 * <p>
 * 新增仓位<br>
 * 新增货区<br>
 * 新增货道<br>
 * 新增货架<br>
 * 新增货位<br>
 * 删除<br>
 * 分页查询<br>
 * 
 * @author zhangsai
 *
 */
public interface BinService {

  /** 查询条件：仓位uuid */
  public static final String QUERY_WRH_FIELD = "wrhUuid";
  /** 查询条件：货区uuid */
  public static final String QUERY_ZONE_FIELD = "zoneUuid";
  /** 查询条件：货道uuid */
  public static final String QUERY_PATH_FIELD = "pathUuid";
  /** 查询条件：货架uuid */
  public static final String QUERY_SHELF_FIELD = "shelfUuid";
  /** 查询条件：货位状态 */
  public static final String QUERY_STATE_FIELD = "state";
  /** 查询条件：货位用途 */
  public static final String QUERY_USAGE_FIELD = "usage";
  /** 查询条件：货位类型 */
  public static final String QUERY_BINTYPE_FIELD = "bintype";
  /** 查询条件：货位代码 */
  public static final String QUERY_CODE_FIELD = "code";

  public static final int PATH_CODE_LENGTH = 2;

  public static final int SHELF_CODE_LENGTH = 2;

  /**
   * 新增仓位
   * <p>
   * 同一组织下仓位代码不允许重复
   * 
   * @param wrh
   *          仓位，not null
   * @throws IllegalArgumentException
   *           参数为空
   * @throws WMSException
   *           代码重复
   */
  void insertWrh(Wrh wrh) throws IllegalArgumentException, WMSException;

  /**
   * 新增货区
   * <p>
   * 同一组织下货区代码不允许重复<br>
   * 且{@link Zone#getCompanyUuid()}与{@link Zone#getWrh()}所属组织一致
   * 
   * @param zone
   *          货区，not null
   * @throws IllegalArgumentException
   *           参数为空
   * @throws WMSException
   */
  void insertZone(Zone zone) throws IllegalArgumentException, WMSException;

  /**
   * 新增货道
   * <p>
   * 代码长度4位，后两位由序列生成，前两位为对应的货区代码<br>
   * 且{@link Path#getCompanyUuid()}与{@link Path#getZoneUuid()}所属组织一致
   * 
   * @param path
   *          货道，not null
   * @throws IllegalArgumentException
   *           参数为空
   * @throws WMSException
   */
  void insertPath(Path path) throws IllegalArgumentException, WMSException;

  /**
   * 新增货架
   * <p>
   * 代码长度6位，后两位由序列生成，前四位为对应的货道代码<br>
   * 且{@link Shelf#getCompanyUuid()}与{@link Shelf#getPathUuid()}所属组织一致
   * 
   * @param pathCode
   *          货道代码，not null
   * @throws IllegalArgumentException
   *           参数为空
   * @throws WMSException
   */
  void insertShelf(String pathCode) throws IllegalArgumentException, WMSException;

  /**
   * 新增货位
   * <p>
   * 代码不能为空<br>
   * 且{@link Bin#getCompanyUuid()}与{@link Bin#getShelfUuid()}所属组织一致
   * 
   * @param bin
   *          货位，not null
   * @param operCtx
   *          操作信息，not null
   * @throws IllegalArgumentException
   *           参数为空
   * @throws WMSException
   */
  void insertBin(Bin bin) throws IllegalArgumentException, WMSException;

  /**
   * 分页查询货位信息
   * 
   * @param definition
   *          查询条件，not null
   * @return 结果集
   * @throws IllegalArgumentException
   *           参数为空
   */
  PageQueryResult<Bin> queryBin(PageQueryDefinition definition) throws IllegalArgumentException;

  /**
   * 查询仓位、货区、货道、货架的树形关系
   * 
   * @return 组织为空返回空集合
   */
  List<BinInfo> queryTreeData();

  /**
   * 删除货位、货架、货道、货区、仓位
   * <p>
   * 删除上级时会连同下级一起删除<br>
   * 下级删除失败，整个删除则失败<br>
   * 货位状态不是{@link BinState#free}时不允许删除
   * 
   * @param uuid
   *          uuid，not null
   * @param version
   *          版本号
   * @throws IllegalArgumentException
   *           参数为空
   * @throws VersionConflictException
   *           货位版本号冲突
   * @throws WMSException
   */
  void remove(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException;

  /**
   * 查询当前组织下的仓位
   * 
   * @return 组织为空时返回空集合
   */
  List<Wrh> queryWrhs();

  /**
   * 查询当前组织下的货区
   * 
   * @return 组织为空时返回空集合
   */
  List<Zone> queryZones();

  /**
   * 查询仓位
   * 
   * @param wrhUuid
   * @return 仓位
   */
  Wrh getWrh(String wrhUuid);

  /**
   * 根据货位代码查询货位
   * 
   * @param binCode
   *          货位代码，not null
   * @return 货位
   */
  Bin getBinByCode(String binCode);

  /**
   * 获取仓位下指定货位用途的一个货位
   * 
   * @param wrhUuid
   *          仓位UUId
   * @param usage
   *          货位用途，
   * @return 参数为空时返回null
   */
  Bin getBinByWrhAndUsage(String wrhUuid, BinUsage usage);

  /**
   * 修改货位状态
   * 
   * @param uuid
   *          货位UUID，not null
   * @param version
   *          版本号
   * @param state
   *          状态
   * @throws IllegalArgumentException
   * @throws VersionConflictException
   * @throws WMSException
   */
  void changeState(String uuid, long version, BinState state)
      throws IllegalArgumentException, VersionConflictException, WMSException;

  /**
   * 锁定货位，临时状态，防止货位使用并发
   * 
   * @param uuid
   *          货位UUID
   * @param version
   *          货位版本号
   * @throws WMSException
   */
  void lock(String uuid, long version) throws WMSException;

  /**
   * 根据货位范围查询货位集合
   * <p>
   * 从小到大排序
   * 
   * @param binScope
   *          货位范围
   * @param usage
   *          货位用途
   * @param state
   *          货位状态
   * @return 符合条件的货位集合
   */
  List<String> queryBinByScopeAndUsage(String binScope, BinUsage usage, BinState state);

  /**
   * 根据货位范围查询货位集合，支持查询多个状态的货位
   * 
   * @param binScope
   *          货位范围
   * @param usage
   *          货位用途
   * @param states
   *          货位状态
   * @return
   */
  List<String> queryBinByScopeAndUsageAndStates(String binScope, BinUsage usage, List<BinState> states);
}
