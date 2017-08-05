/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	ReceiveHandler.java
 * 模块说明：	
 * 修改历史：
 * 2017年7月19日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.in.receive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.basicInfo.bin.Bin;
import com.hd123.sardine.wms.api.basicInfo.bin.BinService;
import com.hd123.sardine.wms.api.basicInfo.bin.BinState;
import com.hd123.sardine.wms.api.basicInfo.container.Container;
import com.hd123.sardine.wms.api.basicInfo.container.ContainerService;
import com.hd123.sardine.wms.api.basicInfo.container.ContainerState;
import com.hd123.sardine.wms.api.in.order.OrderBillService;
import com.hd123.sardine.wms.api.in.order.OrderReceiveInfo;
import com.hd123.sardine.wms.api.in.putaway.PutawayService;
import com.hd123.sardine.wms.api.in.receive.ReceiveBill;
import com.hd123.sardine.wms.api.in.receive.ReceiveBillItem;
import com.hd123.sardine.wms.api.stock.Stock;
import com.hd123.sardine.wms.api.stock.StockService;
import com.hd123.sardine.wms.api.task.Task;
import com.hd123.sardine.wms.api.task.TaskService;
import com.hd123.sardine.wms.api.task.TaskType;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;

/**
 * 收货相关处理器
 * 
 * @author zhangsai
 *
 */
public class ReceiveHandler {

  @Autowired
  private StockService stockService;

  @Autowired
  private OrderBillService orderBillService;

  @Autowired
  private BinService binService;

  @Autowired
  private ContainerService containerService;

  @Autowired
  private PutawayService putawayService;

  @Autowired
  private TaskService taskService;

  /**
   * 收货库存入库
   * 
   * @param receiveBill
   *          收货单，not null
   * @param items
   *          收货明细，not null
   * @throws WMSException
   */
  public void shiftIn(ReceiveBill receiveBill, List<ReceiveBillItem> items) throws WMSException {
    assert receiveBill != null;
    assert items != null;

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
      stock.setSourceBillNumber(receiveBill.getBillNumber());
      stock.setSourceBillType("收货单");
      stock.setSourceBillUuid(receiveBill.getUuid());
      stock.setSourceLineNumber(item.getLine());
      stock.setSourceLineUuid(item.getUuid());
      stock.setStockBatch(item.getStockBatch());
      stock.setSupplierUuid(receiveBill.getSupplier().getUuid());
      stock.setValidDate(item.getValidDate());
      stocks.add(stock);
    }
    stockService.shiftIn("收货单", receiveBill.getBillNumber(), stocks);
  }

  /**
   * 回写订单明细收货数量，并适时修改订单状态
   * 
   * @param orderBillNumber
   *          订单单号， not null
   * @param items
   *          收货明细，not null
   * @throws WMSException
   */
  public void orderBillReceive(String orderBillNumber, List<ReceiveBillItem> items)
      throws WMSException {
    Assert.assertArgumentNotNull(orderBillNumber, "orderBillNumber");
    Assert.assertArgumentNotNull(items, "items");

    List<OrderReceiveInfo> receiveInfos = new ArrayList<OrderReceiveInfo>();
    for (ReceiveBillItem receiveBillItem : items) {
      OrderReceiveInfo receiveInfo = new OrderReceiveInfo();
      receiveInfo.setArticleUuid(receiveBillItem.getArticle().getUuid());
      receiveInfo.setQpcStr(receiveBillItem.getQpcStr());
      receiveInfo.setQty(receiveBillItem.getQty());
      receiveInfos.add(receiveInfo);
    }
    orderBillService.receive(orderBillNumber, receiveInfos);
  }

  /**
   * 收货单对应的货位状态是空闲时，修改成使用中<br>
   * 收货单对应的容器状态为空闲时，修改成已使用
   * 
   * @param items
   *          收货明细。not null
   * @return 返回状态被修改的容器条码集合
   * @throws WMSException
   */
  public List<String> useReceiveBinAndContainer(List<ReceiveBillItem> items) throws WMSException {
    assert items != null;

    Map<String, Bin> binMap = new HashMap<String, Bin>();
    Map<String, Container> containerMap = new HashMap<String, Container>();
    List<String> containerBarcodes = new ArrayList<String>();
    for (ReceiveBillItem item : items) {
      Bin receiveBin = binMap.get(item.getBinCode());
      if (receiveBin == null) {
        receiveBin = binService.getBinByCode(item.getBinCode());
        if (receiveBin == null)
          throw new WMSException("收货的目标货位不存在！");
        binMap.put(item.getBinCode(), receiveBin);

        if (receiveBin.getState().equals(BinState.free))
          binService.changeState(receiveBin.getUuid(), receiveBin.getVersion(), BinState.using);
      }

      Container receiveContainer = containerMap.get(item.getContainerBarcode());
      if (receiveContainer != null)
        continue;
      receiveContainer = containerService.getByBarcode(item.getContainerBarcode());
      if (receiveContainer == null)
        throw new WMSException("收货容器" + item.getContainerBarcode() + "不存在！");
      containerMap.put(item.getContainerBarcode(), receiveContainer);
      if (receiveContainer.getState().equals(ContainerState.STACONTAINERIDLE)
          || receiveContainer.getState().equals(ContainerState.STACONTAINERSTKINING)) {
        containerBarcodes.add(item.getContainerBarcode());
        containerService.change(receiveContainer.getUuid(), receiveContainer.getVersion(),
            ContainerState.STACONTAINERUSEING, item.getBinCode());
      }
    }
    return containerBarcodes;
  }

  /**
   * 生成上架指令
   * 
   * @param receiveBill
   *          收货单，not null
   * @param items
   *          收货明细， not null
   * @param containerBarcodes
   *          需要生成上架指令的容器，not null
   * @throws WMSException
   */
  public void generatorAndSavePutawayTask(ReceiveBill receiveBill, List<ReceiveBillItem> items,
      List<String> containerBarcodes) throws WMSException {
    Assert.assertArgumentNotNull(receiveBill, "receiveBill");
    Assert.assertArgumentNotNull(items, "items");
    Assert.assertArgumentNotNull(containerBarcodes, "containerBarcodes");

    List<Task> tasks = new ArrayList<Task>();
    for (ReceiveBillItem item : items) {
      if (containerBarcodes.contains(item.getContainerBarcode()) == false)
        continue;
      Task task = new Task();
      task.setArticle(item.getArticle());
      task.setCaseQtyStr(item.getCaseQtyStr());
      task.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      task.setCreator(receiveBill.getReceiver());
      task.setFromBinCode(item.getBinCode());
      task.setFromContainerBarcode(item.getContainerBarcode());
      task.setOwner(ApplicationContextUtil.getCompanyUuid());
      task.setProductionDate(item.getProduceDate());
      task.setQpcStr(item.getQpcStr());
      task.setQty(item.getQty());
      task.setSourceBillLine(item.getLine());
      task.setSourceBillNumber(receiveBill.getBillNumber());
      task.setSourceBillType("收货单");
      task.setSourceBillUuid(receiveBill.getUuid());
      task.setStockBatch(item.getStockBatch());
      task.setSupplier(receiveBill.getSupplier());
      task.setTaskType(TaskType.Putaway);
      task.setValidDate(item.getValidDate());
      task.setToBinCode(putawayService.fetchPutawayTargetBinByArticle(item.getArticle().getUuid(),
          item.getQty()));
      task.setToContainerBarcode(Container.VIRTUALITY_CONTAINER);
      tasks.add(task);
    }
    if (tasks.size() > 0)
      taskService.insert(tasks);
  }
}