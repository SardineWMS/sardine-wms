/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	ReceiveVerifier.java
 * 模块说明：	
 * 修改历史：
 * 2017年7月19日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.in.receive;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.basicInfo.article.Article;
import com.hd123.sardine.wms.api.basicInfo.article.ArticleService;
import com.hd123.sardine.wms.api.basicInfo.bin.Bin;
import com.hd123.sardine.wms.api.basicInfo.bin.BinService;
import com.hd123.sardine.wms.api.basicInfo.bin.BinUsage;
import com.hd123.sardine.wms.api.basicInfo.container.Container;
import com.hd123.sardine.wms.api.basicInfo.container.ContainerService;
import com.hd123.sardine.wms.api.basicInfo.container.ContainerState;
import com.hd123.sardine.wms.api.in.order.OrderBill;
import com.hd123.sardine.wms.api.in.order.OrderBillItem;
import com.hd123.sardine.wms.api.in.order.OrderBillService;
import com.hd123.sardine.wms.api.in.order.OrderBillState;
import com.hd123.sardine.wms.api.in.receive.ReceiveBill;
import com.hd123.sardine.wms.api.in.receive.ReceiveBillItem;
import com.hd123.sardine.wms.api.in.receive.ReceiveBillState;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.utils.QpcHelper;
import com.hd123.sardine.wms.service.util.StockBatchUtils;

/**
 * @author zhangsai
 *
 */
public class ReceiveVerifier {

  @Autowired
  private OrderBillService orderBillService;

  @Autowired
  private BinService binService;

  @Autowired
  private StockBatchUtils stockBatchUtils;

  @Autowired
  private ArticleService articleService;

  @Autowired
  private ContainerService containerService;

  /**
   * 收货时校验收货单对应的订单相关内容，并根据订单内容刷新收货单
   * <p>
   * <ul>
   * 收货单对应的订单必须存在，且状态不能是已作废或者已完成。
   * </ul>
   * <ul>
   * 收货的商品必须存在于订单明细中，且收货数量要小于等于待收货数量。
   * </ul>
   * 
   * @param bill
   *          收货单，not null
   * @throws WMSException
   *           校验失败时抛出异常
   */
  public void verifyOrderBillAndRefresh(ReceiveBill bill) throws WMSException {
    OrderBill orderBill = orderBillService.getByBillNumber(bill.getOrderBillNumber());
    if (orderBill == null)
      throw new WMSException("订单" + bill.getOrderBillNumber() + "不存在，无法收货！");
    if (orderBill.getState().equals(OrderBillState.Aborted)
        || orderBill.getState().equals(OrderBillState.Finished))
      throw new WMSException("订单" + bill.getOrderBillNumber() + "已完成或者已作废，无法收货！");
    bill.setSupplier(orderBill.getSupplier());
    bill.setWrh(orderBill.getWrh());
    String totalCaseQtyStr = "0";
    for (ReceiveBillItem item : bill.getItems()) {
      OrderBillItem orderItem = findOrderBillItemByArticleAndQpcStr(orderBill.getItems(),
          item.getArticle().getUuid(), item.getQpcStr());
      if (orderItem == null)
        throw new WMSException(
            "商品" + item.getArticle().getCode() + "不在订单" + bill.getOrderBillNumber() + "中，无法收货！");
      if (orderItem.getQty().subtract(orderItem.getReceivedQty()).compareTo(item.getQty()) < 0)
        throw new WMSException("商品" + item.getArticle().getCode() + "规格" + item.getQpcStr()
            + "可收货数量" + orderItem.getQty().subtract(orderItem.getReceivedQty()) + "小于本次收货数量"
            + item.getQty());

      item.setOrderBillLineUuid(orderItem.getUuid());
      item.setArticle(orderItem.getArticle());
      item.setArticleSpec(orderItem.getArticleSpec());
      item.setQpcStr(orderItem.getQpcStr());
      item.setMunit(orderItem.getMunit());
      item.setPrice(orderItem.getPrice());
      item.setCaseQtyStr(QpcHelper.qtyToCaseQtyStr(item.getQty(), item.getQpcStr()));
      totalCaseQtyStr = QpcHelper.caseQtyStrAdd(totalCaseQtyStr, item.getCaseQtyStr());
    }
    bill.setCaseQtyStr(totalCaseQtyStr);
  }

  /**
   * 校验收货商品的生产日期并刷新到效期和收货批次
   * <p>
   * <ul>
   * 管理保质期的商品生产日期不能晚于当前日期，到效期不能早于当前日期
   * </ul>
   * <ul>
   * 
   * </ul>
   * 
   * @param bill
   *          收货单，not null
   * @throws WMSException
   *           校验失败时抛出异常
   */
  public void verifyReceiveProductDate(ReceiveBill receiveBill) throws WMSException {
    Assert.assertArgumentNotNull(receiveBill, "receiveBill");

    for (ReceiveBillItem receiveBillItem : receiveBill.getItems()) {
      if (new Date().before(receiveBillItem.getProduceDate()))
        throw new WMSException("商品" + receiveBillItem.getArticle().getCode() + "的生产日期大于当前日期！");

      Article article = articleService.get(receiveBillItem.getArticle().getUuid());
      if (article == null)
        throw new WMSException("商品" + receiveBillItem.getArticle().getCode() + "不存在！");

      try {
        Date productDate = article.calProductionDate(receiveBillItem.getProduceDate(),
            receiveBillItem.getValidDate());
        Date validDate = article.calValidDate(receiveBillItem.getProduceDate(),
            receiveBillItem.getValidDate());
        receiveBillItem.setProduceDate(productDate);
        receiveBillItem.setValidDate(validDate);
      } catch (ParseException e) {
      }
      article.verifyProductDateAndValidDate(receiveBillItem.getProduceDate(),
          receiveBillItem.getValidDate());
      article.verifyStkLmtDays(receiveBillItem.getValidDate());
      // receiveBillItem.setStockBatch(stockBatchUtils.genStockBatch());
    }
  }

  /**
   * 校验收货单包含的容器状态是否为空闲
   * <p>
   * <ul>
   * 收货单对应的仓位下必须存在收货暂存位
   * </ul>
   * <ul>
   * 容器状态必须是空闲
   * </ul>
   * 
   * @param bill
   *          收货单，not null
   * @throws WMSException
   *           校验失败时抛出异常
   */
  public void verifyBinAndContainer(ReceiveBill receiveBill) throws WMSException {
    Assert.assertArgumentNotNull(receiveBill, "receiveBill");

    Bin bin = binService.getBinByWrhAndUsage(receiveBill.getWrh().getUuid(),
        BinUsage.ReceiveStorageBin);
    if (bin == null)
      throw new WMSException("仓位" + receiveBill.getWrh().getCode() + "下不存在收货暂存位！");

    for (ReceiveBillItem receiveItem : receiveBill.getItems()) {
      Container container = containerService.getByBarcode(receiveItem.getContainerBarcode());
      if (container == null)
        throw new WMSException("容器" + receiveItem.getContainerBarcode() + "不存在，无法收货！");
      if (container.getState().equals(ContainerState.STACONTAINERIDLE) == false)
        throw new WMSException("收货容器" + receiveItem.getContainerBarcode() + "状态必须是空闲的！");
      receiveItem.setBinCode(bin.getCode());
    }
  }

  private OrderBillItem findOrderBillItemByArticleAndQpcStr(List<OrderBillItem> orderItems,
      String articleUuid, String qpcStr) {
    assert orderItems != null;
    assert articleUuid != null;
    assert qpcStr != null;

    for (OrderBillItem orderItem : orderItems) {
      if (orderItem.getArticle().getUuid().equals(articleUuid)
          && orderItem.getQpcStr().equals(qpcStr))
        return orderItem;
    }
    return null;
  }
}
