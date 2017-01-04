/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2012，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	HasUCN.java
 * 模块说明：	
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.entity;

/**
 * 指示类同时拥有全局唯一标识、代码和名称属性。
 * 
 * @author zhangsai
 * 
 */
public interface HasUCN extends HasUuid, HasCode, HasName {
    // Do Nothing
}
