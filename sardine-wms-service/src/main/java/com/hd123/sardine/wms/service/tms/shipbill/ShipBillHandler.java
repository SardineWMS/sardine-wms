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
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.article.ArticleQpc;
import com.hd123.sardine.wms.api.basicInfo.article.ArticleService;
import com.hd123.sardine.wms.api.basicInfo.bin.Bin;
import com.hd123.sardine.wms.api.basicInfo.bin.BinService;
import com.hd123.sardine.wms.api.basicInfo.bin.BinState;
import com.hd123.sardine.wms.api.basicInfo.container.Container;
import com.hd123.sardine.wms.api.basicInfo.container.ContainerService;
import com.hd123.sardine.wms.api.stock.StockService;
import com.hd123.sardine.wms.api.stock.StockShiftRule;
import com.hd123.sardine.wms.api.stock.StockState;
import com.hd123.sardine.wms.api.tms.shipbill.ShipBill;
import com.hd123.sardine.wms.api.tms.shipbill.ShipBillContainerStock;
import com.hd123.sardine.wms.api.tms.shipbill.ShipBillCustomerItem;
import com.hd123.sardine.wms.common.entity.SourceBill;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.QpcHelper;
import com.hd123.sardine.wms.service.util.StockBatchUtils;

/**
 * @author zhangsai
 *
 */
public class ShipBillHandler {

  @Autowired
  private ArticleService articleService;

  @Autowired
  private StockService stockService;

  @Autowired
  private StockBatchUtils stockBatchUtils;

  @Autowired
  private BinService binService;

  @Autowired
  private ContainerService containerService;

  public void stockOut(ShipBill shipBill, List<ShipBillContainerStock> items) throws WMSException {
    Assert.assertArgumentNotNull(shipBill, "shipBill");
    Assert.assertArgumentNotNull(items, "items");

    if (items.isEmpty())
      return;

    List<StockShiftRule> rules = new ArrayList<StockShiftRule>();
    for (ShipBillContainerStock cs : items) {
      StockShiftRule rule = new StockShiftRule();
      rule.setArticleUuid(cs.getArticle().getUuid());
      rule.setBinCode(cs.getBinCode());
      rule.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      rule.setContainerBarcode(cs.getContainerBarcode());
      rule.setOwner(cs.getCustomer().getUuid());
      rule.setProductionBatch(stockBatchUtils.genProductionBatch(cs.getProductionDate()));
      rule.setQpcStr(cs.getQpcStr());
      rule.setQty(cs.getQty());
      rule.setState(StockState.normal);
      rule.setSupplierUuid(cs.getSupplier().getUuid());
      rules.add(rule);
    }

    SourceBill sourceBill = new SourceBill(ShipBill.CAPTION, shipBill.getUuid(),
        shipBill.getBillNumber());
    stockService.shiftOut(sourceBill, rules);
  }

  public void releaseBinAndContainer(ShipBill shipBill, List<ShipBillContainerStock> items)
      throws WMSException {
    Assert.assertArgumentNotNull(shipBill, "shipBill");
    Assert.assertArgumentNotNull(items, "items");

    if (items.isEmpty())
      return;

    Set<String> binCodes = new HashSet<String>();
    Set<String> containerBarcodes = new HashSet<String>();
    for (ShipBillContainerStock cs : items) {
      binCodes.add(cs.getBinCode());
      if (StringUtil.isNullOrBlank(cs.getContainerBarcode()) == false
          && Container.VIRTUALITY_CONTAINER.equals(cs.getContainerBarcode()) == false)
        containerBarcodes.add(cs.getContainerBarcode());
    }

    for (String binCode : binCodes) {
      Bin bin = binService.getBinByCode(binCode);
      if (stockService.hasBinStock(binCode))
        continue;
      binService.changeState(bin.getUuid(), bin.getVersion(), BinState.free);
    }

    for (String containerBarcode : containerBarcodes) {
      Container container = containerService.getByBarcode(containerBarcode);
      containerService.recycle(container.getUuid(), container.getVersion());
    }
  }

  public List<ShipBillCustomerItem> fetchContainerItems(ShipBill shipBill) {
    Assert.assertArgumentNotNull(shipBill, "shipBill");

    List<ShipBillCustomerItem> customerItems = new ArrayList<ShipBillCustomerItem>();
    for (ShipBillContainerStock cs : shipBill.getContainerStocks()) {
      ShipBillCustomerItem customerItem = findContainerItem(customerItems,
          cs.getCustomer().getUuid());
      if (customerItem == null) {
        customerItem = new ShipBillCustomerItem();
        customerItem.setCustomer(cs.getCustomer());
        customerItem.setLine(customerItems.size() + 1);
        customerItem.setTotalCaseQty(cs.getCaseQtyStr());
        customerItem
            .setTotalVolume(caculateVolume(cs.getArticle().getUuid(), cs.getQpcStr(), cs.getQty()));
        customerItem.setContainerCount(
            caculateContainerCount(shipBill.getContainerStocks(), cs.getCustomer().getUuid()));
        customerItem
            .setTotalWeight(caculateWeight(cs.getArticle().getUuid(), cs.getQpcStr(), cs.getQty()));
        customerItem.setTotalAmount(cs.getPrice().multiply(cs.getQty()));
        customerItems.add(customerItem);
      } else {
        customerItem.setTotalCaseQty(
            QpcHelper.caseQtyStrAdd(customerItem.getTotalCaseQty(), cs.getCaseQtyStr()));
        customerItem.setTotalVolume(customerItem.getTotalVolume()
            .add(caculateVolume(cs.getArticle().getUuid(), cs.getQpcStr(), cs.getQty())));
        customerItem.setTotalWeight(customerItem.getTotalWeight()
            .add(caculateWeight(cs.getArticle().getUuid(), cs.getQpcStr(), cs.getQty())));
        customerItem
            .setTotalAmount(customerItem.getTotalAmount().add(cs.getPrice().multiply(cs.getQty())));
      }
    }
    return customerItems;
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
        if (qpc.getLength() == null || qpc.getWidth() == null || qpc.getHeight() == null) {
          qpc.setLength(BigDecimal.ZERO);
          qpc.setWidth(BigDecimal.ZERO);
          qpc.setHeight(BigDecimal.ZERO);
        }
        return qpc.getWidth().multiply(qpc.getLength()).multiply(qpc.getHeight()).multiply(qty)
            .divide(QpcHelper.qpcStrToQpc(qpcStr));
      }
    }
    return BigDecimal.ZERO;
  }

  private BigDecimal caculateWeight(String articleUuid, String qpcStr, BigDecimal qty) {
    assert articleUuid != null;
    assert qpcStr != null;
    assert qty != null;

    List<ArticleQpc> qpcs = articleService.queryArticleQpcs(articleUuid);
    for (ArticleQpc qpc : qpcs) {
      if (qpcStr.equals(qpc.getQpcStr())) {
        if (qpc.getWeight() == null)
          qpc.setWeight(BigDecimal.ZERO);
        return qpc.getWeight().multiply(qty).divide(QpcHelper.qpcStrToQpc(qpcStr));
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
