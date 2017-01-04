/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2012，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	HasUuid.java
 * 模块说明：	
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.entity;

/**
 * 指示类拥有全局唯一标识属性，设计被用于实体数据对象。
 * <p>
 * 
 * 所谓全局唯一标识（Universally Unique Identifier,
 * UUID）在表现形式上就是一个字符串，通常被应用于标识某个数据实体。首先其可以作为数据实体的标识
 * ，这与现实生活中的“身份证”类似。当某个数据实体在其“诞生”
 * 之时即被分配，标识一旦被分配就不会再被分配给其它实体，以确保其唯一性；同时对于某个特定的数据实体一旦被分配了UUID
 * ，这个值就不会再被修改，伴随其终生。逻辑上的
 * “唯一”都是在某个特定范围的，而这里所说的“全局”可以被理解为是一个包含整个数字世界的范围，这一点是由uuid分配机制所保证的
 * ，其可以确保被分配的UUID不会被世界上另一个分配器再次分配。
 * <p>
 * 
 * @author zhangsai
 * 
 */
public interface HasUuid {

    /**
     * 全局唯一标识。
     */
    String getUuid();

    /**
     * @see #getUuid()
     * @throws UnsupportedOperationException
     *             当实现类不支持修改全局唯一标识时抛出。
     */
    void setUuid(String uuid) throws UnsupportedOperationException;
}
