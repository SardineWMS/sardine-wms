/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	EntityLogger.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月22日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.service.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.log.EntityLog;
import com.hd123.sardine.wms.common.entity.Entity;
import com.hd123.sardine.wms.common.entity.HasUuid;
import com.hd123.sardine.wms.common.entity.OperateContext;
import com.hd123.sardine.wms.dao.log.impl.EntityLogDaoImpl;

/**
 * 日志操作类
 * 
 * @author fanqingqing
 *
 */
@Repository
public class EntityLogger {
  /* 公共事件常量 */
  public static final String EVENT_ADDNEW = "新增";
  public static final String EVENT_BATCHADDNEW = "批量新增";
  public static final String EVENT_MODIFY = "修改";
  public static final String EVENT_REMOVE = "删除";
  public static final String EVENT_DELETE = "删除（标记）";
  public static final String EVENT_UNDELETE = "恢复删除";
  public static final String EVENT_REPLICATE = "复制";

  public static final String EVENT_SUBMIT = "提交";
  public static final String EVENT_ABORT = "作废";
  public static final String EVENT_REJECT = "驳回";
  public static final String EVENT_AGREE = "审批中";
  public static final String EVENT_APPROVE = "审批通过";
  public static final String EVENT_CANCEL = "撤销";
  public static final String EVENT_CLOSE = "关闭";

  public static final String EVENT_TERMIANTE = "中止";
  public static final String EVENT_EFFECTIVE = "生效";

  private Object service;
  // 实体
  private HasUuid entity;
  // 操作上下文
  private OperateContext operCtx;

  private String entityUuid;

  private String entityCaption;

  @Autowired
  private EntityLogDaoImpl daoService;

  /**
   * 注入日常上下文。该方法不能用于ESB方法组件上
   * 
   * @param service
   *          服务类，not null。
   * @param entity
   *          实体，not null。
   * @param operCtx
   *          操作上下文，not null。
   * @throws IllegalArgumentException
   */
  public void injectContext(Object service, HasUuid entity, OperateContext operCtx) {
    Assert.assertArgumentNotNull(service, "service");
    Assert.assertArgumentNotNull(entity, "entity");
    Assert.assertArgumentNotNull(operCtx, "operCtx");

    this.service = service;
    this.operCtx = operCtx;
    this.entity = entity;
  }

  /**
   * 注入日常上下文。
   * 
   * @param service
   *          服务类，not null。
   * @param entity
   *          实体，not null。
   * @param operCtx
   *          操作上下文，not null。
   * @throws IllegalArgumentException
   */
  public void injectContext(Object service, String entity, String entityCaption,
      OperateContext operCtx) {
    Assert.assertArgumentNotNull(service, "service");
    Assert.assertArgumentNotNull(entity, "entity");
    Assert.assertArgumentNotNull(entity, "entityCaption");
    Assert.assertArgumentNotNull(operCtx, "operCtx");

    this.service = service;
    this.operCtx = operCtx;
    this.entityUuid = entity;
    this.entityCaption = entityCaption;
  }

  public void setEntity(Entity entity) {
    this.entity = entity;
  }

  public EntityLogger() {
    super();
  }

  private static EntityLogger logger;

  public static EntityLogger getInstance() {
    if (logger == null)
      return new EntityLogger();
    return logger;
  }

  /**
   * 构造实体对象
   * 
   * @param event
   *          事件
   * @param message
   *          描述信息
   * @return 返回日志实体
   * @throws IllegalArgumentException
   * @throws IllegalStateException
   *           当日志实体未设置时抛出。
   */
  public EntityLog log(String event, String message) {
    Assert.assertArgumentNotNull(event, "event");

    EntityLog log = new EntityLog();
    log.setOperateInfo(operCtx.getOperator().toString());
    log.setTime(operCtx.getTime());
    log.setEvent(event);
    log.setMessage(message);
    log.setServiceClass(service.getClass().getName());
    log.setServiceCaption(service.getClass().getSimpleName());
    if (entity != null) {
      log.setEntityUuid(entity.getUuid());
      log.setEntityCaption(entity.getClass().getName());
    } else {
      log.setEntityUuid(entityUuid);
      log.setEntityCaption(entityCaption);
    }

    daoService.save(log);
    return log;
  }
}
