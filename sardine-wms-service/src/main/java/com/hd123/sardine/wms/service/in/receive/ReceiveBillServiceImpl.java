/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	ReceiveBillServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月14日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.in.receive;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.article.Article;
import com.hd123.sardine.wms.api.basicInfo.article.ArticleService;
import com.hd123.sardine.wms.api.basicInfo.bin.Bin;
import com.hd123.sardine.wms.api.basicInfo.bin.BinService;
import com.hd123.sardine.wms.api.basicInfo.bin.BinState;
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
import com.hd123.sardine.wms.api.in.receive.ReceiveBillService;
import com.hd123.sardine.wms.api.in.receive.ReceiveBillState;
import com.hd123.sardine.wms.api.stock.Stock;
import com.hd123.sardine.wms.api.stock.StockService;
import com.hd123.sardine.wms.api.task.Task;
import com.hd123.sardine.wms.api.task.TaskService;
import com.hd123.sardine.wms.api.task.TaskType;
import com.hd123.sardine.wms.common.entity.OperateInfo;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.common.utils.QpcHelper;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.dao.in.receive.ReceiveBillDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;

/**
 * @author zhangsai
 *
 */
public class ReceiveBillServiceImpl extends BaseWMSService implements ReceiveBillService {

  @Autowired
  private ReceiveBillDao receiveBillDao;

  @Autowired
  private ArticleService articleService;

  @Autowired
  private BinService binService;

  @Autowired
  private TaskService taskService;

  @Autowired
  private StockService stockService;

  @Autowired
  private ContainerService containerService;

  @Autowired
  private OrderBillService orderBillService;
  
  @Override
  public String insert(ReceiveBill bill) throws WMSException {
    Assert.assertArgumentNotNull(bill, "bill");
    bill.validate();

    verifyAndRefresh(bill);

    bill.setUuid(UUIDGenerator.genUUID());
    bill.setBillNumber(null);
    bill.setState(ReceiveBillState.Initial);
    bill.setCreateInfo(OperateInfo.newInstance(ApplicationContextUtil.getOperateContext()));
    bill.setLastModifyInfo(OperateInfo.newInstance(ApplicationContextUtil.getOperateContext()));

    for (ReceiveBillItem item : bill.getItems()) {
      item.setUuid(UUIDGenerator.genUUID());
      item.setReceiveBillUuid(bill.getUuid());
    }
    receiveBillDao.insert(bill);
    receiveBillDao.insertItems(bill.getItems());
    return bill.getBillNumber();
  }

  @Override
  public void update(ReceiveBill bill) throws WMSException {
    Assert.assertArgumentNotNull(bill, "bill");
    Assert.assertArgumentNotNull(bill.getUuid(), "bill.uuid");
    Assert.assertArgumentNotNull(bill.getBillNumber(), "bill.billNumber");
    bill.validate();

    verifyAndRefresh(bill);

    ReceiveBill oldBill = receiveBillDao.get(bill.getUuid());
    if (oldBill == null)
      throw new WMSException("编辑的收货单" + bill.getBillNumber() + "不存在！");
    PersistenceUtils.checkVersion(bill.getVersion(), oldBill, "收货单", bill.getBillNumber());
    if (oldBill.getState().equals(ReceiveBillState.Initial) == false)
      throw new WMSException("收货单" + bill.getBillNumber() + "状态不是初始，无法编辑！");

    bill.setState(ReceiveBillState.Initial);
    bill.setLastModifyInfo(OperateInfo.newInstance(ApplicationContextUtil.getOperateContext()));

    for (ReceiveBillItem item : bill.getItems()) {
      item.setUuid(UUIDGenerator.genUUID());
      item.setReceiveBillUuid(bill.getUuid());
    }
    receiveBillDao.update(bill);
    receiveBillDao.removeItems(bill.getUuid());
    receiveBillDao.insertItems(bill.getItems());
  }

  private void verifyAndRefresh(ReceiveBill bill) throws WMSException {
    assert bill != null;

    OrderBill orderBill = orderBillService.getByBillNumber(bill.getOrderBillNumber());
    if (orderBill == null)
      throw new WMSException("订单" + bill.getOrderBillNumber() + "不存在，无法收货！");
    if (orderBill.getState().equals(OrderBillState.Aborted)
        || orderBill.getState().equals(OrderBillState.Finished))
      throw new WMSException("订单" + bill.getOrderBillNumber() + "已完成或者已作废，无法收货！");
    bill.setSupplier(orderBill.getSupplier());
    bill.setWrh(orderBill.getWrh());

    Bin bin = binService.getBinByWrhAndUsage(bill.getWrh().getUuid(), BinUsage.ReceiveStorageBin);
    if (bin == null)
      throw new WMSException("仓位" + bill.getWrh().getCode() + "下不存在收货暂存位！");

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
      if (new Date().before(item.getProduceDate()))
        throw new WMSException("商品" + item.getArticle().getCode() + "的生产日期大于当前日期！");

      Article article = articleService.get(item.getArticle().getUuid());
      if (article == null)
        throw new WMSException("商品" + item.getArticle().getCode() + "不存在！");
      Date validDate = article.calValidDate(item.getProduceDate());
      if (new Date().before(validDate))
        throw new WMSException("商品有效期早于当前日期，无法收货！");
      Container container = containerService.getByBarcode(item.getContainerBarcode(),
          ApplicationContextUtil.getCompanyUuid());
      if (container == null)
        throw new WMSException("容器" + item.getContainerBarcode() + "不存在，无法收货！");
      if (container.getState().equals(ContainerState.STACONTAINERUSEING) == false
          && container.getState().equals(ContainerState.STACONTAINERIDLE) == false)
        throw new WMSException("收货容器" + item.getContainerBarcode() + "状态必须是空闲或者已使用的！");
      if (container.getState().equals(ContainerState.STACONTAINERUSEING)) {
        Bin currentBin = binService.getBinByCode(container.getPosition());
        if (currentBin == null)
          throw new WMSException("收货容器状态为已使用，却不存在实际位置，无法收货！");
        if (currentBin.getUsage().equals(BinUsage.ReceiveStorageBin) == false
            || bin.getWrh().getUuid().equals(bill.getWrh().getUuid()) == false)
          throw new WMSException("收货容器" + item.getContainerBarcode() + "状态为已使用，却不在收货暂存位上，无法收货！");
      }
      item.setBinCode(bin.getCode());
      item.setValidDate(validDate);
      item.setOrderBillLineUuid(orderItem.getUuid());
      item.setStockBatch(stockBatchUtils.genStockBatch());
      item.setCaseQtyStr(QpcHelper.qtyToCaseQtyStr(item.getQty(), item.getQpcStr()));
      totalCaseQtyStr = QpcHelper.caseQtyStrAdd(totalCaseQtyStr, item.getCaseQtyStr());
    }
    bill.setCaseQtyStr(totalCaseQtyStr);
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

  @Override
  public ReceiveBill get(String uuid) {
    if (StringUtil.isNullOrBlank(uuid))
      return null;

    return receiveBillDao.get(uuid);
  }

  @Override
  public ReceiveBill getByBillNo(String billno) {
    if (StringUtil.isNullOrBlank(billno))
      return null;

    return receiveBillDao.getByBillNumber(billno);
  }

  @Override
  public PageQueryResult<ReceiveBill> query(PageQueryDefinition definition) {
    Assert.assertArgumentNotNull(definition, "definition");

    PageQueryResult<ReceiveBill> pgr = new PageQueryResult<ReceiveBill>();
    List<ReceiveBill> list = receiveBillDao.query(definition);
    PageQueryUtil.assignPageInfo(pgr, definition);
    pgr.setRecords(list);
    return pgr;
  }

  @Override
  public void remove(String uuid, long version) throws WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");

    ReceiveBill receiveBill = receiveBillDao.get(uuid);
    if (receiveBill == null)
      return;
    PersistenceUtils.checkVersion(version, receiveBill, "收货单", receiveBill.getBillNumber());

    if (receiveBill.getState().equals(ReceiveBillState.Initial) == false)
      throw new WMSException("收货单" + receiveBill.getBillNumber() + "状态不是初始，不能删除！");

    receiveBillDao.remove(uuid, version);
    receiveBillDao.removeItems(uuid);
  }

  @Override
  public void audit(String uuid, long version) throws WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");

    ReceiveBill bill = receiveBillDao.get(uuid);
    if (bill == null)
      throw new WMSException("审核的收货单不存在！");
    if (bill.getState().equals(ReceiveBillState.Audited))
      return;

    PersistenceUtils.checkVersion(version, bill, "收货单", bill.getBillNumber());
    List<ReceiveBillItem> items = receiveBillDao.queryItems(uuid);
    if (bill.getState().equals(ReceiveBillState.Initial)) {
      List<Stock> stocks = new ArrayList<Stock>();
      for (ReceiveBillItem item : items) {
        Stock stock = new Stock();
        stock.setArticleUuid(item.getArticle().getUuid());
        stock.setBinCode(item.getBinCode());
        stock.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
        stock.setContainerBarcode(item.getContainerBarcode());
        stock.setMeasureUnit(item.getMunit());
        stock.setProductionDate(item.getProduceDate());
        stock.setQpcStr(item.getQpcStr());
        stock.setQty(item.getQty());
        stock.setSourceBillNumber(bill.getBillNumber());
        stock.setSourceBillType("收货单");
        stock.setSourceBillUuid(bill.getUuid());
        stock.setSourceLineNumber(item.getLine());
        stock.setSourceLineUuid(item.getUuid());
        stock.setStockBatch(item.getStockBatch());
        stock.setSupplierUuid(bill.getSupplier().getUuid());
        stock.setValidDate(item.getValidDate());
        stocks.add(stock);
      }
      stockService.shiftIn("收货单", bill.getBillNumber(), stocks);
    }

    Set<String> binCodes = new HashSet<String>();
    Map<String, String> containerBarcodes = new HashMap<String, String>();
    List<Task> tasks = new ArrayList<Task>();
    for (ReceiveBillItem item : items) {
      binCodes.add(item.getBinCode());
      containerBarcodes.put(item.getContainerBarcode(), item.getBinCode());
    }

    for (String binCode : binCodes) {
      Bin bin = binService.getBinByCode(binCode);
      if (bin == null || bin.getState().equals(BinState.free) == false)
        continue;
      if (stockService.binHasStock(binCode))
        binService.changeState(bin.getUuid(), bin.getVersion(), BinState.using);
    }

    List<String> hasNotTaskContainer = new ArrayList<String>();
    for (String containerBarcode : containerBarcodes.keySet()) {
      Container container = containerService.getByBarcode(containerBarcode,
          ApplicationContextUtil.getCompanyUuid());
      if (container == null
          || (container.getState().equals(ContainerState.STACONTAINERIDLE) == false
              && container.getState().equals(ContainerState.STACONTAINERSTKINING) == false))
        continue;

      if (container.getState().equals(ContainerState.STACONTAINERIDLE)
          && bill.getState().equals(ReceiveBillState.Initial))
        continue;

      containerService.change(container.getUuid(), container.getVersion(),
          ContainerState.STACONTAINERUSEING, containerBarcodes.get(containerBarcode));
      hasNotTaskContainer.add(containerBarcode);
    }

    for (ReceiveBillItem item : items) {
      if (hasNotTaskContainer.contains(item.getContainerBarcode())) {
        Task task = new Task();
        task.setArticle(item.getArticle());
        task.setCaseQtyStr(item.getCaseQtyStr());
        task.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
        task.setCreator(bill.getReceiver());
        task.setFromBinCode(item.getBinCode());
        task.setFromContainerBarcode(item.getContainerBarcode());
        task.setOwner("");
        task.setProductionDate(item.getProduceDate());
        task.setQpcStr(item.getQpcStr());
        task.setQty(item.getQty());
        task.setSourceBillLine(item.getLine());
        task.setSourceBillNumber(bill.getBillNumber());
        task.setSourceBillType("收货单");
        task.setSourceBillUuid(bill.getUuid());
        task.setStockBatch(item.getStockBatch());
        task.setSupplier(bill.getSupplier());
        task.setTaskType(TaskType.Putaway);
        task.setValidDate(item.getValidDate());
        tasks.add(task);
      }
    }
    taskService.insert(tasks);

    bill.setState(ReceiveBillState.Audited);
    bill.setLastModifyInfo(OperateInfo.newInstance(ApplicationContextUtil.getOperateContext()));
    receiveBillDao.update(bill);
  }
}
