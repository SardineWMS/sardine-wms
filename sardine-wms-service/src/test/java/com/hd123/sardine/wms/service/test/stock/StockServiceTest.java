/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	StockServiceTest.java
 * 模块说明：	
 * 修改历史：
 * 2017年9月4日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.service.test.stock;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.sardine.wms.api.stock.StockFilter;
import com.hd123.sardine.wms.api.stock.StockService;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.service.test.BaseTest;

/**
 * @author yangwenzhu
 *
 */
public class StockServiceTest extends BaseTest {

  @Autowired
  private StockService service;

  @Test
  public void testQuery() throws WMSException {
    StockFilter filter = new StockFilter();
    filter.setWrhUuid("c0a20011d84a414daa7dba8f3a1995f0");
    service.query(filter);
  }
}
