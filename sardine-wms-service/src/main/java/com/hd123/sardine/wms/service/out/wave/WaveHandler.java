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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.container.Container;
import com.hd123.sardine.wms.api.out.alcntc.AlcNtcBill;
import com.hd123.sardine.wms.api.out.alcntc.AlcNtcBillService;
import com.hd123.sardine.wms.api.out.alcntc.AlcNtcBillState;
import com.hd123.sardine.wms.api.out.pickup.PickUpBill;
import com.hd123.sardine.wms.api.out.pickup.PickUpBillItem;
import com.hd123.sardine.wms.api.out.pickup.PickUpBillState;
import com.hd123.sardine.wms.api.out.rpl.RplBill;
import com.hd123.sardine.wms.api.out.rpl.RplBillItem;
import com.hd123.sardine.wms.api.out.rpl.RplBillService;
import com.hd123.sardine.wms.api.out.wave.PickRule;
import com.hd123.sardine.wms.api.out.wave.WaveBill;
import com.hd123.sardine.wms.api.out.wave.WaveBinUsage;
import com.hd123.sardine.wms.api.out.wave.WaveNtcItem;
import com.hd123.sardine.wms.api.out.wave.WavePickUpItem;
import com.hd123.sardine.wms.api.stock.StockChangement;
import com.hd123.sardine.wms.api.stock.StockComponent;
import com.hd123.sardine.wms.api.stock.StockService;
import com.hd123.sardine.wms.api.stock.StockShiftIn;
import com.hd123.sardine.wms.api.stock.StockShiftRule;
import com.hd123.sardine.wms.api.stock.StockState;
import com.hd123.sardine.wms.common.entity.SourceBill;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.DateHelper;
import com.hd123.sardine.wms.common.utils.StockConstants;
import com.hd123.sardine.wms.dao.out.wave.WaveBillDao;
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
  private RplBillService rplBillService;

  @Autowired
  private StockBatchUtils stockBatchUtils;

  @Autowired
  private WaveBillDao waveBillDao;

  /**
   * 获取波次单时查询波次单下的配单明细
   * 
   * @param waveBillNumber
   *          波次单号
   * @return 配单明细集合
   */
  public List<WaveNtcItem> queryWaveNtcItems(String waveBillNumber) {
    if (StringUtil.isNullOrBlank(waveBillNumber))
      return new ArrayList<WaveNtcItem>();

    PageQueryDefinition definition = new PageQueryDefinition();
    definition.setPageSize(0);
    definition.put(AlcNtcBillService.QUERY_WAVEBILLNUMBER_LIKE, waveBillNumber);
    definition.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    PageQueryResult<AlcNtcBill> ntcBills = alcNtcService.query(definition);
    List<WaveNtcItem> ntcItems = new ArrayList<WaveNtcItem>();
    for (AlcNtcBill ntcBill : ntcBills.getRecords()) {
      WaveNtcItem ntcItem = new WaveNtcItem();
      ntcItem.setCustomer(ntcBill.getCustomer());
      ntcItem.setDeliveryType(ntcBill.getDeliveryMode());
      ntcItem.setNtcBillNumber(ntcBill.getBillNumber());
      ntcItem.setNtcBillState(ntcBill.getState());
      ntcItem.setNtcBillUuid(ntcBill.getUuid());
      ntcItem.setWaveBillNumber(waveBillNumber);
      ntcItems.add(ntcItem);
    }
    return ntcItems;
  }

  /**
   * 新加入的配单加入波次，原先存在波次现在删除的踢出波次
   * 
   * @param waveBill
   *          当前保存的波次 not null
   * @param oldBill
   *          原波次
   * @throws WMSException
   */
  public void joinWave(WaveBill waveBill, WaveBill oldBill) throws WMSException {
    Assert.assertArgumentNotNull(waveBill, "waveBill");

    for (WaveNtcItem waveItem : waveBill.getNtcItems()) {
      AlcNtcBill alcNtcBill = alcNtcService.getByBillNumber(waveItem.getNtcBillNumber());
      if (alcNtcBill == null)
        throw new WMSException("配货通知单" + waveItem.getNtcBillNumber() + "不存在！");
      if (alcNtcBill.getState().equals(AlcNtcBillState.used)
          && alcNtcBill.getWaveBillNumber().equals(waveBill.getBillNumber()))
        continue;
      alcNtcService.joinWave(alcNtcBill.getBillNumber(), alcNtcBill.getVersion(),
          waveBill.getBillNumber());
    }

    if (oldBill == null)
      return;

    for (WaveNtcItem waveItem : oldBill.getNtcItems()) {
      if (contain(waveBill.getNtcItems(), waveItem))
        continue;

      AlcNtcBill alcNtcBill = alcNtcService.getByBillNumber(waveItem.getNtcBillNumber());
      if (alcNtcBill == null)
        throw new WMSException("配货通知单" + waveItem.getNtcBillNumber() + "不存在！");
      alcNtcService.leaveWave(alcNtcBill.getBillNumber(), alcNtcBill.getVersion());
    }
  }

  private boolean contain(List<WaveNtcItem> nowItems, WaveNtcItem item) {
    assert nowItems != null;
    assert item != null;

    for (WaveNtcItem waveItem : nowItems) {
      if (waveItem.getNtcBillNumber().equals(item.getNtcBillNumber()))
        return true;
    }
    return false;
  }

  /**
   * 将波次下的所有配单全部踢出波次
   * 
   * @param bill
   *          波次单，not null
   * @throws WMSException
   */
  public void leaveWave(WaveBill bill) throws WMSException {
    assert bill != null;
    for (WaveNtcItem item : bill.getNtcItems()) {
      AlcNtcBill alcNtcBill = alcNtcService.getByBillNumber(item.getNtcBillNumber());
      if (alcNtcBill == null)
        throw new IllegalArgumentException(
            MessageFormat.format("通知单{0}不存在 ", item.getNtcBillNumber()));

      alcNtcService.leaveWave(alcNtcBill.getBillNumber(), alcNtcBill.getVersion());
    }
  }

  /**
   * 保存生成的拣货明细，并处理相应的库存
   * 
   * @param waveBillUuid
   *          波次UUID， not null
   * @param waveBillNumber
   *          波次单号，not null
   * @param pickResult
   *          拣货结果集，not null
   * @throws WMSException
   */
  public void savePickResultAndStock(String waveBillUuid, String waveBillNumber,
      List<WavePickUpItem> pickResult) throws WMSException {
    Assert.assertArgumentNotNull(waveBillUuid, "waveBillUuid");
    Assert.assertArgumentNotNull(waveBillNumber, "waveBillNumber");
    if (CollectionUtils.isEmpty(pickResult))
      return;

    for (WavePickUpItem item : pickResult)
      item.setWaveUuid(waveBillUuid);

    waveBillDao.saveWavePickUpItems(pickResult);

    List<StockShiftRule> lockRules = new ArrayList<StockShiftRule>();
    List<StockShiftIn> shiftIns = new ArrayList<StockShiftIn>();

    SourceBill sourceBill = new SourceBill(WaveBill.CAPTION, waveBillUuid, waveBillNumber);
    for (WavePickUpItem pickItem : pickResult) {
      if (pickItem.getBinUsage().equals(WaveBinUsage.StorageBin)
          || pickItem.getBinUsage().equals(WaveBinUsage.DynamicPickBin)) {
        StockShiftRule shiftRule = new StockShiftRule();
        shiftRule.setArticleUuid(pickItem.getArticle().getUuid());
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
        sc.setArticle(pickItem.getArticle());
        sc.setArticleSpec(pickItem.getArticleSpec());
        sc.setBinCode(pickItem.getBinCode());
        sc.setCompanyUuid(pickItem.getCompanyUuid());
        sc.setContainerBarcode(pickItem.getContainerBarcode());
        sc.setQpcStr(pickItem.getQpcStr());
        sc.setMunit(pickItem.getMunit());
        sc.setQty(pickItem.getQty());
        sc.setOwner(ApplicationContextUtil.getCompanyUuid());
        sc.setPrice(BigDecimal.ZERO);
        sc.setProductionDate(DateHelper.strToDate(StockConstants.VISUAL_MAXDATE));
        sc.setProductionBatch(stockBatchUtils.genProductionBatch(sc.getProductionDate()));
        sc.setSourceBill(sourceBill);
        sc.setState(StockState.forPick);
        sc.setStockBatch(StockConstants.VISUAL_STOCK_STOCKBATCH);
        sc.setSupplier(StockConstants.VISUAL_STOCK_SUPPLIER);
        sc.setValidDate(sc.getProductionDate());
        shiftIn.setStockComponent(sc);
        shiftIns.add(shiftIn);
      }
    }
    if (lockRules.isEmpty() == false)
      stockService.changeState(sourceBill, lockRules, StockState.normal, StockState.locked);
    if (shiftIns.isEmpty() == false)
      stockService.shiftIn(sourceBill, shiftIns);
  }

  /**
   * 波次回滚时，将已处理的库存还原
   * 
   * @param waveBillUuid
   *          波次UUID， not null
   * @param waveBillNumber
   *          波次单号，not null
   * @throws WMSException
   */
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
      shiftRule.setArticleUuid(item.getArticle().getUuid());
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

    List<RplBillItem> rplItems = rplBillService.queryRplItems(waveBillNumber);
    List<StockShiftRule> changeRules = new ArrayList<StockShiftRule>();
    List<StockShiftRule> shiftRules = new ArrayList<StockShiftRule>();
    for (RplBillItem item : rplItems) {
      StockShiftRule changeRule = new StockShiftRule();
      changeRule.setArticleUuid(item.getArticle().getUuid());
      changeRule.setBinCode(item.getFromBinCode());
      changeRule.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      changeRule.setContainerBarcode(item.getContainerBarcode());
      changeRule.setOperateBillUuid(waveBillUuid);
      changeRule.setOwner(item.getOwner());
      changeRule.setProductionBatch(item.getProductionBatch());
      changeRule.setQpcStr(item.getQpcStr());
      changeRule.setQty(item.getQty());
      changeRule.setState(StockState.forMoveOut);
      changeRule.setStockBatch(item.getStockBatch());
      changeRule.setSupplierUuid(item.getSupplier().getUuid());
      changeRules.add(changeRule);

      StockShiftRule shiftRule = new StockShiftRule();
      shiftRule.setArticleUuid(item.getArticle().getUuid());
      shiftRule.setBinCode(item.getToBinCode());
      shiftRule.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      shiftRule.setContainerBarcode(Container.VIRTUALITY_CONTAINER);
      shiftRule.setOperateBillUuid(waveBillUuid);
      shiftRule.setOwner(item.getOwner());
      shiftRule.setProductionBatch(item.getProductionBatch());
      shiftRule.setQpcStr(item.getQpcStr());
      shiftRule.setQty(item.getQty());
      shiftRule.setState(StockState.forMoveIn);
      shiftRule.setStockBatch(item.getStockBatch());
      shiftRule.setSupplierUuid(item.getSupplier().getUuid());
      shiftRules.add(shiftRule);
    }

    stockService.changeState(sourceBill, changeRules, StockState.forMoveOut, StockState.normal);
    stockService.shiftOut(sourceBill, shiftRules);
  }

  /**
   * 根据波次的生成的拣货明细对生成拣货单并分单
   * 
   * @param uuid
   *          波次UUID。not null
   * @param billNumber
   *          波次单号，not null
   * @return 拣货单集合
   */
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
        billItem.setArticle(pickItem.getArticle());
        billItem.setCaseQtyStr(pickItem.getCaseQtyStr());
        billItem.setMunit(pickItem.getMunit());
        billItem.setQpcStr(pickItem.getQpcStr());
        billItem.setSourceBinCode(pickItem.getBinCode());
        billItem.setSourceContainerBarcode(pickItem.getContainerBarcode());
        billItem.setQty(pickItem.getQty());
        billItem.setCaseQtyStr(pickItem.getCaseQtyStr());
        billItem.setArticleSpec(pickItem.getArticleSpec());

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

  /**
   * 保存补货结果并处理库存
   * 
   * @param rplBills
   *          补货单集合，not null
   * @param waveUuid
   *          波次UUID， not null
   * @param waveBillNumber
   *          波次单号，not null
   * @throws WMSException
   */
  public void saveRplResultAndStock(List<RplBill> rplBills, String waveUuid, String waveBillNumber)
      throws WMSException {
    Assert.assertArgumentNotNull(rplBills, "rplBills");
    Assert.assertArgumentNotNull(waveUuid, "waveUuid");
    Assert.assertArgumentNotNull(waveBillNumber, "waveBillNumber");

    if (rplBills.isEmpty())
      return;

    SourceBill sourceBill = new SourceBill(WaveBill.CAPTION, waveUuid, waveBillNumber);
    List<StockShiftRule> moveOutRules = new ArrayList<StockShiftRule>();
    List<StockShiftIn> shiftIns = new ArrayList<StockShiftIn>();
    Map<String, String> map = new HashMap<String, String>();
    for (RplBill rplBill : rplBills) {
      for (RplBillItem item : rplBill.getItems()) {
        StockShiftRule shiftRule = new StockShiftRule();
        shiftRule.setArticleUuid(item.getArticle().getUuid());
        shiftRule.setBinCode(item.getFromBinCode());
        shiftRule.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
        shiftRule.setContainerBarcode(item.getContainerBarcode());
        shiftRule.setOwner(item.getOwner());
        shiftRule.setProductionBatch(item.getProductionBatch());
        shiftRule.setQpcStr(item.getQpcStr());
        shiftRule.setQty(item.getQty());
        shiftRule.setState(StockState.normal);
        shiftRule.setStockBatch(item.getStockBatch());
        shiftRule.setSupplierUuid(item.getSupplier().getUuid());
        moveOutRules.add(shiftRule);

        map.put(item.getArticle().getUuid() + item.getFromBinCode() + item.getStockBatch(),
            item.getToBinCode());
      }
    }

    List<StockChangement> changements = stockService.changeState(sourceBill, moveOutRules,
        StockState.normal, StockState.forMoveOut);
    for (StockChangement changement : changements) {
      StockShiftIn shiftIn = new StockShiftIn();
      shiftIn.setStockComponent(changement.getStockComponent());
      shiftIn.getStockComponent()
          .setBinCode(map.get(shiftIn.getStockComponent().getArticle().getUuid()
              + shiftIn.getStockComponent().getBinCode()
              + shiftIn.getStockComponent().getStockBatch()));
      shiftIn.getStockComponent().setContainerBarcode(Container.VIRTUALITY_CONTAINER);
      shiftIn.getStockComponent().setState(StockState.forMoveIn);
      shiftIns.add(shiftIn);
    }
    stockService.shiftIn(sourceBill, shiftIns);

    for (RplBill rplBill : rplBills) {
      rplBill.setWaveBillNumber(waveBillNumber);
      rplBill.setWaveBillUuid(waveUuid);
      rplBillService.saveNew(rplBill);
    }
  }
}
