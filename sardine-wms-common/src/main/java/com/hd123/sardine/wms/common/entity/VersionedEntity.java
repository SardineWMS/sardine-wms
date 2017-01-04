/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2012，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	VersionedEntity.java
 * 模块说明：	
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.entity;

/**
 * 提供所有具有版本控制信息的数据实体的基类，这是一个POJO类，符合JavaBean规范。
 * 
 * @author zhangsai
 * 
 */
public abstract class VersionedEntity extends Entity implements HasVersion {

    private static final long serialVersionUID = 5563356637795335129L;

    /** 默认以及起始版本号。 */
    public static final long DEFAULT_VERSION = HasVersion.START_VERSION;

    private long version = HasVersion.START_VERSION;

    /** 版本号。从0开始计数，默认为{@link #DEFAULT_VERSION}. */
    @Override
    public long getVersion() {
        return version;
    }

    @Override
    public void setVersion(long version) throws UnsupportedOperationException {
        this.version = version;
    }

    @Override
    public void inject(Object source) throws UnsupportedOperationException {
        super.inject(source);
        if (source instanceof HasVersion) {
            version = ((HasVersion) source).getVersion();
        }
    }

}
