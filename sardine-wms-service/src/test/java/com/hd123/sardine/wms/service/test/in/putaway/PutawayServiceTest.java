/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	PutawayServiceTest.java
 * 模块说明：	
 * 修改历史：
 * 2017年9月1日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.service.test.in.putaway;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.sardine.wms.api.in.putaway.PutawayService;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.service.test.BaseTest;

/**
 * @author yangwenzhu
 *
 */
public class PutawayServiceTest extends BaseTest {
  @Autowired
  private PutawayService service;

  @Test
  public void testFetchRtnSupplier() throws WMSException {
    String bin = service.fetchRtnPutawayTargetBinBySupplier("33ae2122692e493fb9af106ba02927b3");
    System.out.println(bin);
  }

}
