/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	AcceptanceBillServiceTest.java
 * 模块说明：	
 * 修改历史：
 * 2017年9月8日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.service.test.out.acceptance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;

import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBill;
import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBillItem;
import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBillService;
import com.hd123.sardine.wms.api.out.alcntc.DeliverySystem;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.DateHelper;
import com.hd123.sardine.wms.service.test.BaseTest;

/**
 * @author yangwenzhu
 *
 */
@Commit
public class AcceptanceBillServiceTest extends BaseTest {
  @Autowired
  private AcceptanceBillService service;

  @Test
  public void testSaveNew() throws WMSException {
    AcceptanceBill bill = new AcceptanceBill();
    bill.setAcceptanceReason("测试");
    bill.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    bill.setCustomer(new UCN("5201457184a84179b2c9282fc16831d3", "c001", "好又多"));
    bill.setDeliverySystem(DeliverySystem.tradition);
    bill.setDeliveryType("仓库送");
    bill.setSourceBillNumber("1211111");
    bill.setSourceBillType("测试");
    bill.setTotalAmount(new BigDecimal(15));
    bill.setTotalCaseQtyStr("5");
    bill.setWrh(new UCN("c0a20011d84a414daa7dba8f3a1995f0", "01", "正常仓"));
    AcceptanceBillItem item = new AcceptanceBillItem();
    item.setArticle(new UCN("xxxx00290001001", "0001001", "可乐"));
    item.setArticleSpec("罐");
    item.setBinCode("01010114");
    item.setCaseQtyStr("5");
    item.setContainerBarCode("P000007");
    item.setLine(1);
    item.setMunit("-");
    item.setPrice(new BigDecimal(3));
    item.setProductionDate(new Date());
    item.setQpcStr("1*1*1");
    item.setQty(new BigDecimal(5));
    item.setSupplier(new UCN("33ae2122692e493fb9af106ba02927b3", "c001", "太古"));
    item.setValidDate(DateHelper.addDays(item.getProductionDate(), 180));
    item.setAmount(new BigDecimal(15));
    item.setPlanCaseQtyStr("0");
    List<AcceptanceBillItem> items = new ArrayList<>();
    items.add(item);
    bill.setItems(items);
    service.insert(bill);
  }

  @Test
  public void testApprove() throws WMSException {
    service.approve("e3507063351248f294535106f79cc25a", 0);
  }

  @Test
  public void testBeginAlc() throws WMSException {
    service.beginAlc("e3507063351248f294535106f79cc25a", 1);
  }

  @Test
  public void testPickup() throws WMSException {
    service.pickUp("52348500175e4e5e83df631e82903d30", new BigDecimal(5));
  }
}
