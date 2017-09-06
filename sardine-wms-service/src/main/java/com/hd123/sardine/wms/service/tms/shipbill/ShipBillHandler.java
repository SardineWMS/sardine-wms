/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	ShipBillHandler.java
 * 模块说明：	
 * 修改历史：
 * 2017年9月6日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.tms.shipbill;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.basicInfo.article.ArticleQpc;
import com.hd123.sardine.wms.api.basicInfo.article.ArticleService;
import com.hd123.sardine.wms.api.basicInfo.container.Container;
import com.hd123.sardine.wms.api.tms.shipbill.ShipBill;
import com.hd123.sardine.wms.api.tms.shipbill.ShipBillContainerStock;
import com.hd123.sardine.wms.api.tms.shipbill.ShipBillCustomerItem;
import com.hd123.sardine.wms.common.utils.QpcHelper;

/**
 * @author zhangsai
 *
 */
public class ShipBillHandler {

  @Autowired
  private ArticleService articleService;

  public List<ShipBillCustomerItem> fetchContainerItems(ShipBill shipBill) {
    Assert.assertArgumentNotNull(shipBill, "shipBill");

    List<ShipBillCustomerItem> containerItems = new ArrayList<ShipBillCustomerItem>();
    for (ShipBillContainerStock cs : shipBill.getContainerStocks()) {
      ShipBillCustomerItem customerItem = findContainerItem(containerItems,
          cs.getCustomer().getUuid());
      if (customerItem == null) {
        customerItem = new ShipBillCustomerItem();
        customerItem.setCustomer(cs.getCustomer());
        customerItem.setLine(containerItems.size() + 1);
        customerItem.setTotalCaseQty(cs.getCaseQtyStr());
        customerItem
            .setTotalVolume(caculateVolume(cs.getArticle().getUuid(), cs.getQpcStr(), cs.getQty()));
        customerItem.setContainerCount(
            caculateContainerCount(shipBill.getContainerStocks(), cs.getCustomer().getUuid()));
        containerItems.add(customerItem);
      } else {
        customerItem.setTotalCaseQty(
            QpcHelper.caseQtyStrAdd(customerItem.getTotalCaseQty(), cs.getCaseQtyStr()));
        customerItem.setTotalVolume(customerItem.getTotalVolume()
            .add(caculateVolume(cs.getArticle().getUuid(), cs.getQpcStr(), cs.getQty())));
      }
    }
    return containerItems;
  }

  private int caculateContainerCount(List<ShipBillContainerStock> css, String customerUuid) {
    assert css != null;
    assert customerUuid != null;

    Set<String> containerSet = new HashSet<String>();
    for (ShipBillContainerStock cs : css) {
      if (Objects.equals(cs.getContainerBarcode(), Container.VIRTUALITY_CONTAINER))
        continue;
      containerSet.add(cs.getContainerBarcode());
    }
    return containerSet.size();
  }

  private BigDecimal caculateVolume(String articleUuid, String qpcStr, BigDecimal qty) {
    assert articleUuid != null;
    assert qpcStr != null;
    assert qty != null;

    List<ArticleQpc> qpcs = articleService.queryArticleQpcs(articleUuid);
    for (ArticleQpc qpc : qpcs) {
      if (qpcStr.equals(qpc.getQpcStr())) {
        return qpc.getWidth().multiply(qpc.getLength()).multiply(qpc.getHeight()).multiply(qty)
            .divide(QpcHelper.qpcStrToQpc(qpcStr));
      }
    }
    return BigDecimal.ZERO;
  }

  private ShipBillCustomerItem findContainerItem(List<ShipBillCustomerItem> containerItems,
      String customerUuid) {
    if (CollectionUtils.isEmpty(containerItems))
      return null;

    for (ShipBillCustomerItem containerItem : containerItems) {
      if (containerItem.getCustomer().getUuid().equals(customerUuid))
        return containerItem;
    }
    return null;
  }
}
