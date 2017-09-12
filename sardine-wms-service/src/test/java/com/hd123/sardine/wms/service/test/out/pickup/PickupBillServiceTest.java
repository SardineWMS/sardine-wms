/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	PickupBillService.java
 * 模块说明：	
 * 修改历史：
 * 2017年9月8日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.service.test.out.pickup;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;

import com.hd123.sardine.wms.api.out.pickup.PickUpBillService;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.service.test.BaseTest;

/**
 * @author yangwenzhu
 *
 */
 @Commit
public class PickupBillServiceTest extends BaseTest {

  @Autowired
  private PickUpBillService service;

  @Test
  public void testPick() throws WMSException {
    service.pick("P000007", "01010311", "P000016", new UCN("yz", "yz", "yz"));
  }
}
