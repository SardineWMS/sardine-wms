/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2012，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	HasCode.java
 * 模块说明：	
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.entity;

/**
 * 指示类拥有代码属性，代码可以作为业务层面数据实体标识。
 * <p>
 * 
 * 代码的表现形式是一个字符串，可以用来在某个特定范围，通常是某一类数据实体范围内，唯一确定一个数据实体。其编码的规则通常依赖与具体业务规则，采用一些便于“
 * 人类”阅读与理解的形式。另外对于一个特定的数据实体，其代码也允许被修改，同时某个代码即便已经被分配给某个数据实体，仍然可以再次被分配给另一个数据实体，
 * 只要确保之前使用该代码的数据实体不再使用即可。换言之在唯一性上，只需要确保在某个时刻通过代码可以唯一确定一个数据实体即可。
 * 
 * @author zhangsai
 * 
 */
public interface HasCode {

    /**
     * 代码。
     */
    String getCode();

    /**
     * @see #getCode()
     * @throws UnsupportedOperationException
     *             当实现类不支持修改代码时抛出。
     */
    void setCode(String code) throws UnsupportedOperationException;
}
