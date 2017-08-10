/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	AlcNtcVerifier.java
 * 模块说明：	
 * 修改历史：
 * 2017年7月21日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.out.alcntc;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.basicInfo.article.Article;
import com.hd123.sardine.wms.api.basicInfo.article.ArticleService;
import com.hd123.sardine.wms.api.basicInfo.bin.BinService;
import com.hd123.sardine.wms.api.basicInfo.bin.Wrh;
import com.hd123.sardine.wms.api.basicInfo.customer.Customer;
import com.hd123.sardine.wms.api.basicInfo.customer.CustomerService;
import com.hd123.sardine.wms.api.out.alcntc.AlcNtcBill;
import com.hd123.sardine.wms.api.out.alcntc.AlcNtcBillItem;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.exception.WMSException;

/**
 * @author zhangsai
 *
 */
public class AlcNtcVerifier {

  @Autowired
  private BinService binService;

  @Autowired
  private ArticleService articleService;

  @Autowired
  private CustomerService customerService;

  public void verifyAlcNtcBill(AlcNtcBill alcNtcBill) throws WMSException {
    Assert.assertArgumentNotNull(alcNtcBill, "alcNtcBill");

    alcNtcBill.validate();

    Wrh wrh = binService.getWrh(alcNtcBill.getWrh().getUuid());
    if (wrh == null)
      throw new WMSException("仓位" + alcNtcBill.getWrh().getCode() + "不存在");
    alcNtcBill.setWrh(new UCN(wrh.getUuid(), wrh.getCode(), wrh.getName()));

    Customer customer = customerService.get(alcNtcBill.getCustomer().getUuid());
    if (customer == null)
      throw new WMSException("客户" + alcNtcBill.getCustomer().getCode() + "不存在！");
    alcNtcBill.setCustomer(new UCN(customer.getUuid(), customer.getCode(), customer.getName()));

    for (AlcNtcBillItem item : alcNtcBill.getItems()) {
      Article article = articleService.get(item.getArticle().getUuid());
      if (article == null)
        throw new WMSException("商品" + item.getArticle().getUuid() + "不存在");
    }
  }
}
