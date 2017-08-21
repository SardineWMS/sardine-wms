/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	WaveHandler.java
 * 模块说明：	
 * 修改历史：
 * 2017年7月21日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.out.wave;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.out.alcntc.AlcNtcBill;
import com.hd123.sardine.wms.api.out.alcntc.AlcNtcBillService;
import com.hd123.sardine.wms.api.out.alcntc.AlcNtcBillState;
import com.hd123.sardine.wms.api.out.pickup.PickUpBill;
import com.hd123.sardine.wms.api.out.pickup.PickUpBillItem;
import com.hd123.sardine.wms.api.out.pickup.PickUpBillState;
import com.hd123.sardine.wms.api.out.wave.PickRule;
import com.hd123.sardine.wms.api.out.wave.WaveBill;
import com.hd123.sardine.wms.api.out.wave.WaveBillItem;
import com.hd123.sardine.wms.api.out.wave.WaveBinUsage;
import com.hd123.sardine.wms.api.out.wave.WavePickUpItem;
import com.hd123.sardine.wms.api.stock.StockComponent;
import com.hd123.sardine.wms.api.stock.StockService;
import com.hd123.sardine.wms.api.stock.StockShiftIn;
import com.hd123.sardine.wms.api.stock.StockShiftRule;
import com.hd123.sardine.wms.api.stock.StockState;
import com.hd123.sardine.wms.api.task.Task;
import com.hd123.sardine.wms.common.entity.SourceBill;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.DateHelper;
import com.hd123.sardine.wms.common.utils.StockConstants;
import com.hd123.sardine.wms.dao.out.wave.WaveBillDao;
import com.hd123.sardine.wms.service.task.TaskHandler;
import com.hd123.sardine.wms.service.util.StockBatchUtils;

/**
 * @author zhangsai
 *
 */
public class WaveHandler {

  @Autowired
  private AlcNtcBillService alcNtcService;

  @Autowired
  private StockService stockService;

  @Autowired
  private StockBatchUtils stockBatchUtils;

  @Autowired
  private WaveBillDao waveBillDao;

  @Autowired
  private TaskHandler taskHandler;

  public void joinWave(WaveBill waveBill, WaveBill oldBill) throws WMSException {
    Assert.assertArgumentNotNull(waveBill, "waveBill");

    for (WaveBillItem waveItem : waveBill.getItems()) {
      AlcNtcBill alcNtcBill = alcNtcService.getByBillNumber(waveItem.getAlcNtcBillNumber());
      if (alcNtcBill == null)
        throw new WMSException("配货通知单" + waveItem.getAlcNtcBillNumber() + "不存在！");
      if (alcNtcBill.getState().equals(AlcNtcBillState.used)
          && alcNtcBill.getWaveBillNumber().equals(waveBill.getBillNumber()))
        continue;
      alcNtcService.joinWave(alcNtcBill.getBillNumber(), alcNtcBill.getVersion(),
          waveBill.getBillNumber());
    }

    if (oldBill == null)
      return;

    for (WaveBillItem waveItem : oldBill.getItems()) {
      if (contain(waveBill.getItems(), waveItem))
        continue;

      AlcNtcBill alcNtcBill = alcNtcService.getByBillNumber(waveItem.getAlcNtcBillNumber());
      if (alcNtcBill == null)
        throw new WMSException("配货通知单" + waveItem.getAlcNtcBillNumber() + "不存在！");
      alcNtcService.leaveWave(alcNtcBill.getBillNumber(), alcNtcBill.getVersion());
    }
  }

  private boolean contain(List<WaveBillItem> nowItems, WaveBillItem item) {
    assert nowItems != null;
    assert item != null;

    for (WaveBillItem waveItem : nowItems) {
      if (waveItem.getAlcNtcBillNumber().equals(item.getAlcNtcBillNumber()))
        return true;
    }
    return false;
  }

  public void leaveWave(WaveBill bill) throws WMSException {
    assert bill != null;
    for (WaveBillItem item : bill.getItems()) {
      AlcNtcBill alcNtcBill = alcNtcService.getByBillNumber(item.getAlcNtcBillNumber());
      if (alcNtcBill == null)
        throw new IllegalArgumentException(
            MessageFormat.format("通知单{0}不存在 ", item.getAlcNtcBillNumber()));

      alcNtcService.leaveWave(alcNtcBill.getBillNumber(), alcNtcBill.getVersion());
    }
  }

  public void saveRplResult(List<Task> rplTasks) throws WMSException {
    if (CollectionUtils.isEmpty(rplTasks))
      return;

    waveBillDao.insertRplTasks(rplTasks);
    for (Task task : rplTasks)
      taskHandler.lockStock(task);
  }

  public void savePickResultAndStock(String waveBillUuid, String waveBillNumber,
      List<WavePickUpItem> pickResult) throws WMSException {
    Assert.assertArgumentNotNull(waveBillUuid, "waveBillUuid");
    Assert.assertArgumentNotNull(waveBillNumber, "waveBillNumber");
    if (CollectionUtils.isEmpty(pickResult))
      return;

    waveBillDao.saveWavePickUpItems(pickResult);

    List<StockShiftRule> lockRules = new ArrayList<StockShiftRule>();
    List<StockShiftIn> shiftIns = new ArrayList<StockShiftIn>();

    SourceBill sourceBill = new SourceBill(WaveBill.CAPTION, waveBillUuid, waveBillNumber);
    for (WavePickUpItem pickItem : pickResult) {
      if (pickItem.getBinUsage().equals(WaveBinUsage.StorageBin)
          || pickItem.getBinUsage().equals(WaveBinUsage.DynamicPickBin)) {
        StockShiftRule shiftRule = new StockShiftRule();
        shiftRule.setArticleUuid(pickItem.getArticleUuid());
        shiftRule.setBinCode(pickItem.getBinCode());
        shiftRule.setCompanyUuid(pickItem.getCompanyUuid());
        shiftRule.setContainerBarcode(pickItem.getContainerBarcode());
        shiftRule.setOwner(ApplicationContextUtil.getCompanyUuid());
        shiftRule.setQpcStr(pickItem.getQpcStr());
        shiftRule.setQty(pickItem.getQty());
        shiftRule.setState(StockState.normal);
        lockRules.add(shiftRule);
      } else {
        StockShiftIn shiftIn = new StockShiftIn();
        StockComponent sc = new StockComponent();
        sc.setArticleUuid(pickItem.getArticleUuid());
        sc.setBinCode(pickItem.getBinCode());
        sc.setCompanyUuid(pickItem.getCompanyUuid());
        sc.setContainerBarcode(pickItem.getContainerBarcode());
        sc.setMeasureUnit(pickItem.getMunit());
        sc.setOwner(ApplicationContextUtil.getCompanyUuid());
        sc.setPrice(BigDecimal.ZERO);
        sc.setProductionDate(DateHelper.strToDate(StockConstants.VISUAL_MAXDATE));
        sc.setProductionBatch(stockBatchUtils.genProductionBatch(sc.getProductionDate()));
        sc.setSourceBill(sourceBill);
        sc.setState(StockState.forPick);
        sc.setStockBatch(StockConstants.VISUAL_STOCK_STOCKBATCH);
        sc.setSupplierUuid(StockConstants.VISUAL_STOCK_SUPPLIERUUID);
        sc.setValidDate(sc.getProductionDate());
        shiftIn.setStockComponent(sc);
        shiftIns.add(shiftIn);
      }
    }
    if (lockRules.isEmpty() == false)
      stockService.changeState(sourceBill, lockRules, StockState.normal, StockState.locked);
    if (shiftIns.isEmpty() == false)
      stockService.shiftIn(sourceBill, shiftIns);

    List<Task> rplTasks = waveBillDao.queryRplTasks(waveBillUuid);
    for (Task task : rplTasks)
      taskHandler.releaseStock(task);
  }

  public void rollbackStock(String waveBillUuid, String waveBillNumber) throws WMSException {
    Assert.assertArgumentNotNull(waveBillUuid, "waveBillUuid");
    Assert.assertArgumentNotNull(waveBillNumber, "waveBillNumber");

    List<WavePickUpItem> pickItems = waveBillDao.queryPickItem(waveBillUuid);
    if (CollectionUtils.isEmpty(pickItems))
      return;

    SourceBill sourceBill = new SourceBill(WaveBill.CAPTION, waveBillUuid, waveBillNumber);
    List<StockShiftRule> unlockRules = new ArrayList<StockShiftRule>();
    List<StockShiftRule> outRules = new ArrayList<StockShiftRule>();
    for (WavePickUpItem item : pickItems) {
      StockShiftRule shiftRule = new StockShiftRule();
      shiftRule.setArticleUuid(item.getArticleUuid());
      shiftRule.setBinCode(item.getBinCode());
      shiftRule.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      shiftRule.setContainerBarcode(item.getContainerBarcode());
      shiftRule.setOperateBillUuid(waveBillUuid);
      shiftRule.setOwner(ApplicationContextUtil.getCompanyUuid());
      shiftRule.setQpcStr(item.getQpcStr());
      shiftRule.setQty(item.getQty());
      if (item.getBinUsage().equals(WaveBinUsage.FixBin)) {
        shiftRule.setState(StockState.forPick);
        outRules.add(shiftRule);
      } else {
        shiftRule.setState(StockState.locked);
        unlockRules.add(shiftRule);
      }
    }
    stockService.changeState(sourceBill, unlockRules, StockState.locked, StockState.normal);
    stockService.shiftOut(sourceBill, outRules);
  }

  public List<PickUpBill> generatePickUpBill(String uuid, String billNumber) {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(billNumber, "billNumber");

    List<PickUpBill> result = new ArrayList<PickUpBill>();
    List<PickRule> pickRules = waveBillDao.queryPickRules(uuid);
    for (PickRule rule : pickRules) {
      List<WavePickUpItem> pickItems = waveBillDao.queryPickItemByPickRule(rule);
      List<PickUpBill> bills = new ArrayList<PickUpBill>();
      for (WavePickUpItem pickItem : pickItems) {
        PickUpBillItem billItem = new PickUpBillItem();
        billItem.setAlcNtcBillItemUuid(pickItem.getAlcNtcBillItemUuid());
        billItem.setArticle(new UCN(pickItem.getArticleUuid(), null, null));
        billItem.setCaseQtyStr(pickItem.getCaseQtyStr());
        billItem.setMunit(pickItem.getMunit());
        billItem.setQpcStr(pickItem.getQpcStr());
        billItem.setSourceBinCode(pickItem.getBinCode());
        billItem.setSourceContainerBarcode(pickItem.getContainerBarcode());

        PickUpBill bill = findPickUpBill(bills, pickItem.getVolume(), pickItem.getItemVolume());
        if (bill == null) {
          bill = new PickUpBill();
          bill.setCustomer(pickItem.getCustomer());
          bill.setDeliveryType(pickItem.getDeliveryType());
          bill.setMethod(rule.getOperateMethod());
          bill.setPickArea(rule.getPickArea());
          bill.setPickOrder(pickItem.getPickOrder());
          bill.setType(pickItem.getType());
          bill.setSourceBill(new SourceBill(WaveBill.CAPTION, uuid, billNumber));
          bill.setVolume(BigDecimal.ZERO);
          bill.setState(PickUpBillState.inConfirm);
          bills.add(bill);
        }

        bill.getItems().add(billItem);
        bill.setVolume(bill.getVolume().add(pickItem.getItemVolume()));
      }
      if (bills.isEmpty() == false)
        result.addAll(bills);
    }
    return result;
  }

  private PickUpBill findPickUpBill(List<PickUpBill> bills, BigDecimal volume,
      BigDecimal itemVolume) {
    assert bills != null;
    assert volume != null;
    assert itemVolume != null;

    if (volume.compareTo(BigDecimal.ZERO) <= 0)
      return null;

    if (itemVolume.compareTo(volume) >= 0)
      return null;

    for (PickUpBill bill : bills) {
      if (bill.getVolume().add(itemVolume).compareTo(volume) <= 0)
        return bill;
    }
    return null;
  }
}
