/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	OrderVerify.java
 * 模块说明：	
 * 修改历史：
 * 2017年7月19日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.in.order;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.basicInfo.bin.BinService;
import com.hd123.sardine.wms.api.basicInfo.bin.Wrh;
import com.hd123.sardine.wms.api.basicInfo.supplier.Supplier;
import com.hd123.sardine.wms.api.basicInfo.supplier.SupplierService;
import com.hd123.sardine.wms.api.in.order.OrderBill;
import com.hd123.sardine.wms.common.exception.WMSException;

/**
 * @author zhangsai
 *
 */
public class OrderVerifier {

  @Autowired
  private SupplierService supplierService;

  @Autowired
  private BinService binService;

  public void verifyOrderBill(OrderBill orderBill) throws WMSException {
    Assert.assertArgumentNotNull(orderBill, "orderBill");

    orderBill.validate();

    Supplier supplier = supplierService.get(orderBill.getSupplier().getUuid());
    if (supplier == null)
      throw new WMSException("供应商" + orderBill.getSupplier().getCode() + "不存在。");

    Wrh wrh = binService.getWrh(orderBill.getWrh().getUuid());
    if (wrh == null)
      throw new WMSException("仓位" + orderBill.getWrh().getCode() + "不存在。");
  }
}
