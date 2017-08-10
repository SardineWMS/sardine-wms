/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	AcceptanceVerifier.java
 * 模块说明：	
 * 修改历史：
 * 2017年7月20日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.out.acceptance;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.basicInfo.article.Article;
import com.hd123.sardine.wms.api.basicInfo.article.ArticleService;
import com.hd123.sardine.wms.api.basicInfo.bin.BinService;
import com.hd123.sardine.wms.api.basicInfo.bin.Wrh;
import com.hd123.sardine.wms.api.basicInfo.customer.Customer;
import com.hd123.sardine.wms.api.basicInfo.customer.CustomerService;
import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBill;
import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBillItem;
import com.hd123.sardine.wms.common.exception.WMSException;

/**
 * @author zhangsai
 *
 */
public class AcceptanceVerifier {

  @Autowired
  private BinService binService;

  @Autowired
  private CustomerService customerService;

  @Autowired
  private ArticleService articleService;

  public void verifyAcceptanceBill(AcceptanceBill acceptanceBill) throws WMSException {
    Assert.assertArgumentNotNull(acceptanceBill, "acceptanceBill");

    acceptanceBill.validate();
    Wrh wrh = binService.getWrh(acceptanceBill.getWrh().getUuid());
    if (wrh == null)
      throw new WMSException("要货的仓位" + acceptanceBill.getWrh().getCode() + "不存在！");
    Customer customer = customerService.get(acceptanceBill.getCustomer().getUuid());
    if (customer == null)
      throw new WMSException("要货的客户" + acceptanceBill.getCustomer().getCode() + "不存在！");

    Map<String, Article> articleMap = new HashMap<String, Article>();
    for (AcceptanceBillItem acceptanceItem : acceptanceBill.getItems()) {
      Article article = articleMap.get(acceptanceItem.getArticle().getUuid());
      if (article == null) {
        article = articleService.get(acceptanceItem.getArticle().getUuid());
        if (article == null)
          throw new WMSException("要货的商品" + acceptanceItem.getArticle().getCode() + "不存在！");
        articleMap.put(acceptanceItem.getArticle().getUuid(), article);
      }
    }
  }
}
