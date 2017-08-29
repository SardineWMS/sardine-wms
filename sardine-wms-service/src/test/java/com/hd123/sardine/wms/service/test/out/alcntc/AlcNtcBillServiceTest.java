/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	AlcNtcBillServiceTest.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月29日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.service.test.out.alcntc;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.sardine.wms.api.out.alcntc.AlcNtcBillService;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.service.test.BaseTest;

/**
 * @author yangwenzhu
 *
 */
public class AlcNtcBillServiceTest extends BaseTest {

  @Autowired
  private AlcNtcBillService alcNtcBillService;

  @Test
  public void testRefreshAlcNtcBillItemPlanCaseQtyStr() throws WMSException {
    alcNtcBillService.refreshAlcNtcBillItemPlanCaseQtyStr("8000201170824000001");
  }
}
