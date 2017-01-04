/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2012，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	HasName.java
 * 模块说明：	
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.entity;

/**
 * 指示类具有名称属性，名称可以被看作是业务实体对象摘要。
 * <p>
 * 
 * 为某个业务实体对象指定名称，目的是为了便于人类阅读和理解。就像人类的名字，取名时尽量避免与他人重名，但并不能彻底避免出现重名的情况，
 * 即便如此也没有给人类生活带来太大的不便。因此在规则上并不要求名称的唯一。
 * <p>
 * 
 * @author zhangsai
 * 
 */
public interface HasName {

  /**
   * 名称
   */
  String getName();

  /**
   * @see #getName()
   * @throws UnsupportedOperationException
   *           当实现类不支持修改名称时抛出。
   */
  void setName(String name) throws UnsupportedOperationException;
}
