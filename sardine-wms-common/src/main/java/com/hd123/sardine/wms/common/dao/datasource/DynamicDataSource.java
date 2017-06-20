/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	DynamicDataSource.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月17日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.dao.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

  @Override
  protected Object determineCurrentLookupKey() {
    return DatabaseContextHolder.getCustomerType();
  }
}
