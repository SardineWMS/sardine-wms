/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2012，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	Entity.java
 * 模块说明：	
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.entity;

import java.io.Serializable;

/**
 * 提供所有数据实体的基类，这是一个POJO类，符合JavaBean规范。
 * 
 * @author zhangsai
 * 
 */
public abstract class Entity implements Serializable, IsEntity, Injectable {

    private static final long serialVersionUID = 997723760796411734L;

    private String uuid;
    
    /**
     * 全局唯一标识。
     */
    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(String uuid) throws UnsupportedOperationException {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return uuid;
    }

    @Override
    public void inject(Object source) throws UnsupportedOperationException {
        if (source instanceof HasUuid) {
            uuid = ((HasUuid) source).getUuid();
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Entity other = (Entity) obj;
        if (uuid == null) {
            if (other.uuid != null)
                return false;
        } else if (!uuid.equals(other.uuid))
            return false;
        return true;
    }
}
