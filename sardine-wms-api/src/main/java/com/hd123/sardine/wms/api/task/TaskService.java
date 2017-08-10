/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	TaskService.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月12日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.task;

import java.math.BigDecimal;
import java.util.List;

import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * 指令服务：接口
 * 
 * @author zhangsai
 *
 */
public interface TaskService {

  /** 查询条件 指令号 */
  public static final String QUERY_FIELD_TASKNO = "taskNo";
  /** 查询条件 指令类型 */
  public static final String QUERY_FIELD_TASKTYPE = "taskType";
  /** 查询条件 来源单据UUID */
  public static final String QUERY_FIELD_SOURCEBILLUUID = "sourceBillUuid";
  /** 查询条件 指令状态 */
  public static final String QUERY_FIELD_STATE = "state";
  /** 查询条件 商品代码 */
  public static final String QUERY_FIELD_ARTICLECODE = "articleCode";
  /** 排序条件 指令号 */
  public static final String ORDER_FIELD_TASKNO = "taskNo";

  /**
   * 批量插入指令
   * <p>
   * 指令必须是同一指令类型
   * 
   * @param tasks
   *          指令集合，not null
   * @throws IllegalArgumentException
   * @throws VersionConflictException
   * @throws WMSException
   */
  void insert(List<Task> tasks)
      throws IllegalArgumentException, VersionConflictException, WMSException;

  /**
   * 作废指令
   * 
   * @param uuid
   *          指令UUID，not null
   * @param version
   *          版本号
   * @throws IllegalArgumentException
   * @throws VersionConflictException
   * @throws WMSException
   */
  void abort(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException;

  /**
   * 执行上架指令
   * 
   * @param uuid
   *          指令UUID not null
   * @param version
   *          版本号
   * @param toBinCode
   *          目标货位，如果为空则使用指令原目标货位
   * @throws IllegalArgumentException
   * @throws VersionConflictException
   * @throws WMSException
   */
  void putaway(String uuid, long version, String toBinCode)
      throws IllegalArgumentException, VersionConflictException, WMSException;

  /**
   * 分页查询指令
   * 
   * @param definition
   *          查询条件，not null
   * @return 分页集合数据
   */
  PageQueryResult<Task> query(PageQueryDefinition definition);

  /**
   * 按照商品移库规则批量保存商品移库指令
   * 
   * @param articleMoveRules
   *          移库条件
   * @throws IllegalArgumentException
   * @throws WMSException
   */
  List<Task> saveArticleMoveTask(List<ArticleMoveRule> articleMoveRules)
      throws IllegalArgumentException, WMSException;

  /**
   * 按照商品移库规则批量保存商品移库指令，并执行生成的移库指令
   * 
   * @param articleMoveRules
   *          移库条件
   * @throws IllegalArgumentException
   * @throws WMSException
   */
  void saveAndMoveArticleMoveTask(List<ArticleMoveRule> articleMoveRules)
      throws IllegalArgumentException, WMSException;

  /**
   * 按容器移库规则保存移库指令
   * 
   * @param containerMoveRules
   *          移库条件
   * @throws IllegalArgumentException
   * @throws WMSException
   */
  List<Task> saveContainerMoveTask(List<ContainerMoveRule> containerMoveRules)
      throws IllegalArgumentException, WMSException;

  /**
   * 按容器移库规则保存移库指令，并执行生成的移库指令
   * 
   * @param containerMoveRules
   *          移库条件
   * @throws IllegalArgumentException
   * @throws WMSException
   */
  void saveAndMoveContainerMoveTask(List<ContainerMoveRule> containerMoveRules)
      throws IllegalArgumentException, WMSException;

  /**
   * 商品移库
   * 
   * @param uuid
   *          商品移库指令UUID
   * @param version
   *          版本号
   * @param realQty
   *          移库实际数量
   * @throws IllegalArgumentException
   * @throws VersionConflictException
   * @throws WMSException
   */
  void articleMove(String uuid, long version, BigDecimal realQty)
      throws IllegalArgumentException, VersionConflictException, WMSException;

  /**
   * 容器移库
   * 
   * @param uuid
   *          容器移库指令UUID
   * @param version
   *          指令版本号
   * @throws IllegalArgumentException
   * @throws VersionConflictException
   * @throws WMSException
   */
  void containerMove(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException;
}
