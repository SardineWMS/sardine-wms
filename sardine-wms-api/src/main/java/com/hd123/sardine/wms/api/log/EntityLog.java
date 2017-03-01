/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	Log.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月15日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.log;

import java.util.Date;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.Entity;

/**
 * 实体日志对象
 * 
 * @author fanqingqing
 */
public class EntityLog extends Entity {
    private static final long serialVersionUID = -1876843966266482745L;

    /** 属性{@link #getOperateInfo()} 的最大长度限制 */
    public static final int LENGTH_OPERATEINFO = 255;
    /** 属性{@link #getEvent()} 的最大长度限制 */
    public static final int LENGTH_EVENT = 100;
    /** 属性{@link #getMessage()} 的最大长度限制 */
    public static final int LENGTH_MESSAGE = 255;
    /** 属性{@link #getServiceClass()} 的最大长度限制 */
    public static final int LENGTH_SERVICECLASS = 128;
    /** 属性{@link #getServiceCaption()} 的最大长度限制 */
    public static final int LENGTH_SERVICECAPTION = 64;
    /** 属性{@link #getEntityUuid()} 的最大长度限制 */
    public static final int LENGTH_ENTITYUUID = 38;
    /** 属性{@link #getEntityCaption()} 的最大长度限制 */
    public static final int LENGTH_ENTITYCAPTION = 64;

    private String operateInfo;
    private Date time;
    private String event;
    private String message;
    private String ServiceClass;
    private String ServiceCaption;
    private String entityUuid = "unknown uuid";
    private String entityCaption = "未知";

    /** 操作人信息 */
    public String getOperateInfo() {
        return operateInfo;
    }

    public void setOperateInfo(String operateInfo) {
        Assert.assertArgumentNotNull(operateInfo, "operateInfo");
        Assert.assertStringNotTooLong(operateInfo, LENGTH_OPERATEINFO, "operateInfo");
        this.operateInfo = operateInfo;
    }

    /** 操作时间 */
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        Assert.assertArgumentNotNull(time, "time");
        this.time = time;
    }

    /** 日志事件 */
    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        Assert.assertArgumentNotNull(event, "event");
        Assert.assertStringNotTooLong(event, LENGTH_EVENT, "event");

        this.event = event;
    }

    /** 消息 */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        Assert.assertArgumentNotNull(message, "message");
        Assert.assertStringNotTooLong(message, LENGTH_MESSAGE, "message");

        this.message = message;
    }

    /** 服务类 */
    public String getServiceClass() {
        return ServiceClass;
    }

    public void setServiceClass(String serviceClass) {
        Assert.assertArgumentNotNull(serviceClass, "serviceClass");
        Assert.assertStringNotTooLong(serviceClass, LENGTH_SERVICECLASS, "serviceClass");

        ServiceClass = serviceClass;
    }

    /** 服务类标题 */
    public String getServiceCaption() {
        return ServiceCaption;
    }

    public void setServiceCaption(String serviceCaption) {
        Assert.assertArgumentNotNull(serviceCaption, "serviceCaption");
        Assert.assertStringNotTooLong(serviceCaption, LENGTH_SERVICECAPTION, "serviceCaption");

        ServiceCaption = serviceCaption;
    }

    /** 实体uuid */
    public String getEntityUuid() {
        return entityUuid;
    }

    public void setEntityUuid(String entityUuid) {
        Assert.assertArgumentNotNull(entityUuid, "entityUuid");
        Assert.assertStringNotTooLong(entityUuid, LENGTH_ENTITYUUID, "entityUuid");

        this.entityUuid = entityUuid;
    }

    /** 实体标题 */
    public String getEntityCaption() {
        return entityCaption;
    }

    public void setEntityCaption(String entityCaption) {
        Assert.assertArgumentNotNull(entityCaption, "entityCaption");
        Assert.assertStringNotTooLong(entityCaption, LENGTH_ENTITYCAPTION, "entityCaption");

        this.entityCaption = entityCaption;
    }

}
