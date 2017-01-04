/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2012，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	HasVersion.java
 * 模块说明：	
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.entity;

/**
 * 指示类拥有描述数据实体版本信息的属性。
 * <p>
 * 
 * 通过版本号属性，可以区别实体数据对象从诞生经过不断修改操作所产生不同历史时期的状态。取值为long，通常来说当某个数据对象被创建后其初始版本号为0，
 * 之后每次修改都会导致其在原来基础上加1。
 * <p>
 * 
 * 作为典型的应用，将该属性用作乐观控制锁（Optimistic Control
 * Lock），用于解决在共享情况下多人同时修改同一数据对象从而导致的访问冲突问题。
 * 
 * @author zhangsai
 * 
 */
public interface HasVersion {

    /** 约定的起始版本号。 */
    public static final long START_VERSION = 0L;

    /**
     * 版本号。
     */
    long getVersion();

    /**
     * @see #getVersion()
     * @throws UnsupportedOperationException
     *             当实现类不提供修改版本号功能时抛出。
     */
    void setVersion(long version) throws UnsupportedOperationException;
}
