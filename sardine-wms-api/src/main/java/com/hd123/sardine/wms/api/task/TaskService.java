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

  /** 查询条件 指令号*/
  public static final String QUERY_FIELD_TASKNO = "taskNo";
  /** 查询条件 指令类型*/
  public static final String QUERY_FIELD_TASKTYPE = "taskType";
  /** 查询条件 指令状态*/
  public static final String QUERY_FIELD_STATE = "state";
  /** 查询条件 商品代码*/
  public static final String QUERY_FIELD_ARTICLECODE = "articleCode";
  /** 排序条件 指令号*/
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
}
