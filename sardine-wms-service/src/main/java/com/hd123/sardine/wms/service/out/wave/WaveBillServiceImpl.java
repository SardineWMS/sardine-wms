/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	WaveBillServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月21日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.service.out.wave;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.article.Article;
import com.hd123.sardine.wms.api.basicInfo.config.articleconfig.ArticleConfig;
import com.hd123.sardine.wms.api.basicInfo.pickarea.PickArea;
import com.hd123.sardine.wms.api.out.alcntc.AlcNtcBillService;
import com.hd123.sardine.wms.api.out.alcntc.WaveAlcNtcItem;
import com.hd123.sardine.wms.api.out.pickup.PickUpBill;
import com.hd123.sardine.wms.api.out.pickup.PickUpBillService;
import com.hd123.sardine.wms.api.out.rpl.RplBill;
import com.hd123.sardine.wms.api.out.rpl.RplBillService;
import com.hd123.sardine.wms.api.out.wave.WaveBill;
import com.hd123.sardine.wms.api.out.wave.WaveBillService;
import com.hd123.sardine.wms.api.out.wave.WaveBillState;
import com.hd123.sardine.wms.api.out.wave.WavePickUpItem;
import com.hd123.sardine.wms.api.stock.StockMajorInfo;
import com.hd123.sardine.wms.api.tms.serialarch.SerialArch;
import com.hd123.sardine.wms.api.tms.serialarch.SerialArchService;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.dao.out.wave.WaveBillDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.log.EntityLogger;

/**
 * @author yangwenzhu
 *
 */
public class WaveBillServiceImpl extends BaseWMSService implements WaveBillService {
  @Autowired
  private WaveBillDao dao;
  @Autowired
  private EntityLogger logger;
  @Autowired
  private SerialArchService serialArchService;
  @Autowired
  private PickUpBillService pickUpBillService;
  @Autowired
  private RplBillService rplBillService;
  @Autowired
  private WaveVerifier waveVerifier;
  @Autowired
  private WaveHandler waveHandler;
  @Autowired
  private WaveQueryHandler waveQueryHandler;
  @Autowired
  private AlcNtcBillService alcNtcBillService;

  @Override
  public String saveNew(WaveBill bill) throws IllegalArgumentException, WMSException {
    Assert.assertArgumentNotNull(bill, "bill");

    waveVerifier.verifyWaveBill(bill);

    bill.setBillNumber(billNumberGenerator.allocateNextBillNumber(WaveBill.class.getSimpleName()));
    bill.setState(WaveBillState.initial);
    bill.setUuid(UUIDGenerator.genUUID());
    bill.setCreateInfo(ApplicationContextUtil.getOperateInfo());
    bill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());

    dao.insert(bill);
    waveHandler.joinWave(bill, null);
    System.out.println(bill.getNtcItems().toString());
    dao.saveWaveAlcNtcItems(bill.getUuid(), bill.getBillNumber());
    dao.updateWaveAlcNtcItemPickOrder(bill.getSerialArch().getUuid(), bill.getUuid());

    logger.injectContext(this, bill.getUuid(), WaveBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_ADDNEW, "新增波次单");
    return bill.getUuid();
  }

  @Override
  public void saveModify(WaveBill bill)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(bill, "bill");
    waveVerifier.verifyWaveBill(bill);

    WaveBill waveBill = dao.get(bill.getUuid());
    if (Objects.isNull(waveBill))
      throw new IllegalArgumentException("要修改的波次单" + waveBill.getUuid() + "不存在");
    PersistenceUtils.checkVersion(bill.getVersion(), waveBill, WaveBill.CAPTION, bill.getUuid());
    if (WaveBillState.initial.equals(bill.getState()) == false)
      throw new WMSException(
          MessageFormat.format("该波次单的状态是{0}，不能编辑，只有状态的补货单才能拿编辑", waveBill.getState().getCaption()));
    SerialArch serialArch = serialArchService.get(bill.getSerialArch().getUuid());
    bill.setSerialArch(new UCN(serialArch.getUuid(), serialArch.getCode(), serialArch.getName()));
    bill.setState(WaveBillState.initial);
    bill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());

    dao.update(bill);
    waveHandler.joinWave(bill, waveBill);

    dao.removeWaveAlcNtcItems(bill.getBillNumber());
    dao.saveWaveAlcNtcItems(bill.getUuid(), bill.getBillNumber());
    dao.updateWaveAlcNtcItemPickOrder(bill.getSerialArch().getUuid(), bill.getUuid());

    logger.injectContext(this, waveBill.getUuid(), WaveBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "修改波次单");
  }

  @Override
  public void remove(String uuid, long version) throws WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(version, "version");

    WaveBill bill = get(uuid);
    if (Objects.isNull(bill))
      throw new IllegalArgumentException(MessageFormat.format("要删除的波次单{0}不存在", uuid));
    if (WaveBillState.initial.equals(bill.getState()) == false)
      throw new IllegalArgumentException(
          MessageFormat.format("当前波次单的状态是{0},不是初始，不能删除", bill.getState()));

    dao.remove(uuid, version);

    waveHandler.leaveWave(bill);
    dao.removeWaveAlcNtcItems(bill.getBillNumber());

    logger.injectContext(this, uuid, WaveBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_REMOVE, "删除波次单");
  }

  @Override
  public WaveBill get(String uuid) {
    if (StringUtil.isNullOrBlank(uuid))
      return null;
    WaveBill bill = dao.get(uuid);
    if (bill == null)
      return bill;
    bill.setNtcItems(waveHandler.queryWaveNtcItems(bill.getBillNumber()));
    bill.setPickItems(pickUpBillService.queryPickTaskView(bill.getBillNumber()));
    bill.setRplItems(rplBillService.queryByWaveBillNumber(bill.getBillNumber()));
    return bill;
  }

  @Override
  public WaveBill getByBillNumber(String billNumber) {
    if (StringUtil.isNullOrBlank(billNumber))
      return null;
    WaveBill bill = dao.getByBillNumber(billNumber);
    if (bill == null)
      return bill;
    bill.setNtcItems(waveHandler.queryWaveNtcItems(bill.getBillNumber()));
    bill.setPickItems(pickUpBillService.queryPickTaskView(bill.getBillNumber()));
    bill.setRplItems(rplBillService.queryByWaveBillNumber(bill.getBillNumber()));
    return bill;
  }

  @Override
  public PageQueryResult<WaveBill> query(PageQueryDefinition definition) {
    Assert.assertArgumentNotNull(definition, "definition");
    List<WaveBill> list = dao.query(definition);
    PageQueryResult<WaveBill> pqr = new PageQueryResult<>();
    PageQueryUtil.assignPageInfo(pqr, definition);
    pqr.setRecords(list);
    return pqr;
  }

  @Override
  public List<String> start(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");

    WaveBill waveBill = dao.get(uuid);
    if (waveBill == null)
      throw new WMSException("波次不存在！");
    PersistenceUtils.checkVersion(version, waveBill, WaveBill.CAPTION, waveBill.getBillNumber());

    if (waveBill.getState().equals(WaveBillState.initial) == false)
      throw new WMSException("波次状态不是初始，无法启动！");

    waveBill.setState(WaveBillState.inProgress);
    waveBill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    dao.update(waveBill);

    List<String> articleUuids = dao.queryWaveArticleUuids(waveBill.getBillNumber());
    return articleUuids;
  }

  @Override
  public void executeArticle(String uuid, String billNumber, List<String> articleUuids)
      throws WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(billNumber, "billNumber");

    if (CollectionUtils.isEmpty(articleUuids))
      return;

    waveQueryHandler.buildCache(billNumber, articleUuids);
    List<WavePickUpItem> pickResult = new ArrayList<WavePickUpItem>();
    List<RplBill> rplBills = new ArrayList<RplBill>();
    for (String articleUuid : articleUuids) {
      Article article = waveQueryHandler.findArticleByArticleUuid(articleUuid);
      List<StockMajorInfo> stocks = waveQueryHandler.findStockByArticleUuid(articleUuid);
      ArticleConfig articleConfig = waveQueryHandler.findArticleConfigByArticleUuid(articleUuid);
      List<PickArea> pickareas = waveQueryHandler.findPickAreas();
      List<WaveAlcNtcItem> inAlcItems = waveQueryHandler.findWaveAlcNtcItems(articleUuid);
      PickUpGenerator pickUpGenerator = new PickUpGenerator(article, billNumber,
          ApplicationContextUtil.getCompanyUuid());
      pickUpGenerator.setArticleConfig(articleConfig);
      pickUpGenerator.setPickareas(pickareas);
      pickUpGenerator.setStocks(stocks);
      pickUpGenerator.setWaveAlcNtcItems(inAlcItems);
      pickUpGenerator.build();

      List<WavePickUpItem> result = pickUpGenerator.getPickResult();
      if (CollectionUtils.isEmpty(result))
        continue;
      pickResult.addAll(result);

      if (articleConfig == null || StringUtil.isNullOrBlank(articleConfig.getFixedPickBin()))
        continue;

      RplGenerator rplGenerator = new RplGenerator(pickUpGenerator.getRplCollector(),
          articleConfig);
      rplGenerator.build();
      rplBills.addAll(rplGenerator.getRplResult());
    }

    waveHandler.savePickResultAndStock(uuid, billNumber, pickResult);
    waveHandler.saveRplResultAndStock(rplBills, uuid, billNumber);
  }

  @Override
  public void generatePickUpBill(String uuid, String billNumber) throws WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(billNumber, "billNumber");

    List<PickUpBill> bills = waveHandler.generatePickUpBill(uuid, billNumber);
    for (PickUpBill bill : bills) {
      pickUpBillService.saveNew(bill);
    }
  }

  @Override
  public void startComplete(String billNumber) throws WMSException {
    Assert.assertArgumentNotNull(billNumber, "billNumber");

    WaveBill waveBill = dao.getByBillNumber(billNumber);
    if (waveBill == null)
      throw new WMSException("波次单" + billNumber + "不存在！");
    waveBill.setState(WaveBillState.started);
    waveBill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    dao.update(waveBill);
  }

  @Override
  public void startException(String billNumber, String errorMessage) throws WMSException {
    Assert.assertArgumentNotNull(billNumber, "billNumber");

    WaveBill waveBill = dao.getByBillNumber(billNumber);
    if (waveBill == null)
      throw new WMSException("波次单" + billNumber + "不存在！");
    waveBill.setState(WaveBillState.exception);
    waveBill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    dao.update(waveBill);
  }

  @Override
  public void rollBack(String uuid, long version) throws WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");

    WaveBill waveBill = dao.get(uuid);
    if (waveBill == null)
      throw new WMSException("波次单" + uuid + "不存在！");
    PersistenceUtils.checkVersion(version, waveBill, WaveBill.CAPTION, waveBill.getBillNumber());

    if (waveBill.getState().equals(WaveBillState.exception) == false
        && waveBill.getState().equals(WaveBillState.started) == false)
      throw new WMSException("只有启动异常或者启动完成的波次可以回滚！");

    waveHandler.rollbackStock(uuid, waveBill.getBillNumber());

    pickUpBillService.removeByWaveUuid(uuid);
    rplBillService.removeByWaveBillNumber(waveBill.getBillNumber());
    dao.removeWavePickUpItems(uuid);
    dao.rollbackWaveAlcNtcItemState(waveBill.getBillNumber());

    waveBill.setState(WaveBillState.initial);
    waveBill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    dao.update(waveBill);
  }

  @Override
  public void confirm(String uuid, long version) throws WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");

    WaveBill waveBill = dao.get(uuid);
    if (waveBill == null)
      throw new WMSException("波次单" + uuid + "不存在！");
    PersistenceUtils.checkVersion(version, waveBill, WaveBill.CAPTION, waveBill.getBillNumber());

    if (waveBill.getState().equals(WaveBillState.started) == false)
      throw new WMSException("只有启动启动完成的波次可以确认！");

    pickUpBillService.approveByWaveBillNumber(waveBill.getBillNumber());
    rplBillService.approveByWaveBillNumber(waveBill.getBillNumber());
    dao.removeWaveAlcNtcItems(waveBill.getBillNumber());
    dao.removeWavePickUpItems(uuid);
    alcNtcBillService.refreshAlcNtcBillItemPlanCaseQtyStr(waveBill.getBillNumber());

    waveBill.setState(WaveBillState.inAlc);
    waveBill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    dao.update(waveBill);
  }
}
