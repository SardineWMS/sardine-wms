/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	RtnSupplierNtcBillServiceTest.java
 * 模块说明：	
 * 修改历史：
 * 2017年9月5日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.service.test.rtn.rtnsupplierntc;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.sardine.wms.api.rtn.rtnsupplierntc.RtnSupplierNtcBillService;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.service.test.BaseTest;

/**
 * @author yangwenzhu
 *
 */
public class RtnSupplierNtcBillServiceTest extends BaseTest {
  @Autowired
  private RtnSupplierNtcBillService service;

  @Test
  public void testGenUnshelveTask() throws WMSException {
    service.genUnshelveTask("63edbaf785f3427c8473f0968c83d7c0", 0);
  }

}
