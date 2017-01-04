/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2012，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	StandardEntity.java
 * 模块说明：	
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.entity;

/**
 * 提供所有符合标准实体规范的实体的基类，这是一个POJO类，符合JavaBean规范。
 * <p>
 * 
 * @author zhangsai
 * 
 */
public abstract class StandardEntity extends VersionedEntity {

    private static final long serialVersionUID = 4676170909750743441L;

    private OperateInfo createInfo;
    private OperateInfo lastModifyInfo;

    /**
     * 创建信息。
     */
    public OperateInfo getCreateInfo() {
        return createInfo;
    }

    public void setCreateInfo(OperateInfo created) {
        this.createInfo = created;
    }

    /**
     * 最后修改信息。
     */
    public OperateInfo getLastModifyInfo() {
        return lastModifyInfo;
    }

    public void setLastModifyInfo(OperateInfo lastModifyInfo) {
        this.lastModifyInfo = lastModifyInfo;
    }
}
