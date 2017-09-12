/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	ReturnBillServiceTest.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月29日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.service.test.out.customerreturn;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.sardine.wms.api.rtn.customerreturn.ReturnBillService;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.service.test.BaseTest;

/**
 * @author yangwenzhu
 *
 */
public class ReturnBillServiceTest extends BaseTest {

  @Autowired
  private ReturnBillService service;

  @Test
  public void testAudit() throws WMSException {
    service.audit("210697a9ffcb4e63a10a09667d68e218", 0);
  }
}
