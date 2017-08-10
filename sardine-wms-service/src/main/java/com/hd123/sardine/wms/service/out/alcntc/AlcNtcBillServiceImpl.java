/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	AlcNtcBillServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月1日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.service.out.alcntc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.article.Article;
import com.hd123.sardine.wms.api.basicInfo.article.ArticleQpc;
import com.hd123.sardine.wms.api.basicInfo.article.ArticleService;
import com.hd123.sardine.wms.api.basicInfo.bin.BinService;
import com.hd123.sardine.wms.api.basicInfo.bin.Wrh;
import com.hd123.sardine.wms.api.ia.user.CompanyService;
import com.hd123.sardine.wms.api.out.alcntc.AlcNtcBill;
import com.hd123.sardine.wms.api.out.alcntc.AlcNtcBillItem;
import com.hd123.sardine.wms.api.out.alcntc.AlcNtcBillService;
import com.hd123.sardine.wms.api.out.alcntc.AlcNtcBillState;
import com.hd123.sardine.wms.api.out.alcntc.DeliveryArticleInfo;
import com.hd123.sardine.wms.api.out.alcntc.DeliverySystem;
import com.hd123.sardine.wms.api.out.alcntc.WaveAlcNtcItem;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.common.utils.QpcHelper;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.dao.out.alcntc.AlcNtcBillDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.log.EntityLogger;

/**
 * @author yangwenzhu
 *
 */
public class AlcNtcBillServiceImpl extends BaseWMSService implements AlcNtcBillService {

  private static final String DEFAULT_CASEQTYSTR = "0";
  private static final String WARHOUSE_DELIVERY = "warehouseDelivery";

  @Autowired
  private EntityLogger logger;
  @Autowired
  private AlcNtcBillDao dao;
  @Autowired
  private ArticleService articleService;
  @Autowired
  private BinService binService;
  @Autowired
  private CompanyService companyService;

  @Override
  public String insert(AlcNtcBill alcNtcBill) throws IllegalArgumentException, WMSException {
    Assert.assertArgumentNotNull(alcNtcBill, "alcNtcBill");

    alcNtcBill.validate();

    if (dao.getByBillNumber(alcNtcBill.getBillNumber()) != null)
      return null;
    verifyArticleAndWrh(alcNtcBill);
    refreshInVaildQpcStr(alcNtcBill);

    alcNtcBill.setUuid(UUIDGenerator.genUUID());
    alcNtcBill.setBillNumber(
        billNumberGenerator.allocateNextBillNumber(AlcNtcBill.class.getSimpleName()));
    alcNtcBill.setState(AlcNtcBillState.initial);
    alcNtcBill.setCreateInfo(ApplicationContextUtil.getOperateInfo());
    alcNtcBill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    alcNtcBill.setDeliverySystem(WARHOUSE_DELIVERY.equals(alcNtcBill.getDeliveryMode())
        ? DeliverySystem.tradition : DeliverySystem.eCommerce);

    for (AlcNtcBillItem item : alcNtcBill.getItems()) {
      item.setUuid(UUIDGenerator.genUUID());
      item.setAlcNtcBillUuid(alcNtcBill.getUuid());
    }
    dao.insertItems(alcNtcBill.getItems());
    dao.insert(alcNtcBill);

    logger.injectContext(this, alcNtcBill.getUuid(), AlcNtcBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_ADDNEW, "新增配单");
    return alcNtcBill.getUuid();
  }

  private void verifyArticleAndWrh(AlcNtcBill alcNtcBill) throws WMSException {
    assert alcNtcBill != null;
    assert alcNtcBill.getItems() != null && alcNtcBill.getItems().isEmpty() == false;

    Wrh wrh = binService.getWrh(alcNtcBill.getWrh().getUuid());
    if (wrh == null)
      throw new WMSException("仓位" + alcNtcBill.getWrh().getUuid() + "不存在");

    for (AlcNtcBillItem item : alcNtcBill.getItems()) {
      Article article = articleService.get(item.getArticle().getUuid());
      if (article == null)
        throw new WMSException("商品" + item.getArticle().getUuid() + "不存在");
    }

  }

  private void refreshInVaildQpcStr(AlcNtcBill alcNtcBill) {
    assert alcNtcBill != null;
    for (AlcNtcBillItem item : alcNtcBill.getItems()) {
      if (StringUtil.isNullOrBlank(item.getQpcStr())
          || item.getQpcStr().matches("^1\\*\\d+(\\.\\d+)?\\*\\d+(\\.\\d+)?$") == false) {
        List<ArticleQpc> qpcs = articleService.queryArticleQpcs(item.getArticle().getUuid());
        if (qpcs.isEmpty())
          item.setQpcStr(ArticleQpc.DEFAULT_QPCSTR);
        else
          item.setQpcStr(qpcs.get(0).getQpcStr());
        item.setCaseQtyStr(QpcHelper.qtyToCaseQtyStr(item.getQty(), item.getQpcStr()));
      }
    }
  }

  @Override
  public void update(AlcNtcBill alcNtcBill)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(alcNtcBill, "alcNtcBill");

    alcNtcBill.validate();
    AlcNtcBill bill = getByBillNumber(alcNtcBill.getBillNumber());
    if (bill == null)
      throw new WMSException("配单" + alcNtcBill.getBillNumber() + "不存在");
    if (AlcNtcBillState.initial.equals(bill.getState()) == false)
      throw new WMSException("配单" + bill.getBillNumber() + "状态不是初始，不能修改");
    PersistenceUtils.checkVersion(alcNtcBill.getVersion(), bill, AlcNtcBill.CAPTION,
        alcNtcBill.getBillNumber());

    verifyArticleAndWrh(alcNtcBill);
    refreshInVaildQpcStr(alcNtcBill);
    alcNtcBill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    for (AlcNtcBillItem item : alcNtcBill.getItems()) {
      item.setAlcNtcBillUuid(alcNtcBill.getUuid());
      item.setUuid(UUIDGenerator.genUUID());
    }

    dao.update(alcNtcBill);
    dao.removeItems(alcNtcBill.getUuid());
    dao.insertItems(alcNtcBill.getItems());

    logger.injectContext(this, alcNtcBill.getUuid(), AlcNtcBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "修改配单");

  }

  @Override
  public void abort(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(version, "version");

    AlcNtcBill bill = get(uuid);
    if (bill == null)
      throw new WMSException("配单" + uuid + "不存在");
    if (AlcNtcBillState.aborted.equals(bill.getState()))
      return;
    if (AlcNtcBillState.initial.equals(bill.getState()) == false)
      throw new WMSException("配单" + bill.getBillNumber() + "状态不是初始，不能作废");
    PersistenceUtils.checkVersion(version, bill, AlcNtcBill.CAPTION, bill.getBillNumber());

    bill.setState(AlcNtcBillState.aborted);
    bill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    dao.update(bill);

    logger.injectContext(this, uuid, AlcNtcBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_ABORT, "作废配单");
  }

  @Override
  public void finish(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(version, "version");

    AlcNtcBill bill = get(uuid);
    if (bill == null)
      throw new WMSException("配单" + uuid + "不存在");
    if (AlcNtcBillState.finished.equals(bill.getState()))
      return;
    if ((AlcNtcBillState.initial.equals(bill.getState())
        || AlcNtcBillState.finished.equals(bill.getState())) == false)
      throw new WMSException("配单" + "状态不是初始或分拣中，不允许完成");
    PersistenceUtils.checkVersion(version, bill, AlcNtcBill.CAPTION, uuid);

    if (AlcNtcBillState.initial.equals(bill.getState()))
      finishInitialState(bill);

    dao.update(bill);
    dao.removeItems(uuid);
    dao.insertItems(bill.getItems());

    logger.injectContext(this, uuid, AlcNtcBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "完成配单");

  }

  private void finishInitialState(AlcNtcBill bill) {
    assert bill != null;
    bill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    for (AlcNtcBillItem item : bill.getItems()) {
      item.setPlanCaseQtyStr(DEFAULT_CASEQTYSTR);
      item.setPlanQty(BigDecimal.ZERO);
      item.setRealCaseQtyStr(DEFAULT_CASEQTYSTR);
      item.setRealQty(BigDecimal.ZERO);
    }
    bill.setState(AlcNtcBillState.finished);
    bill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
  }

  @Override
  public PageQueryResult<AlcNtcBill> query(PageQueryDefinition definition)
      throws IllegalArgumentException {
    Assert.assertArgumentNotNull(definition, "definition");

    PageQueryResult<AlcNtcBill> qpr = new PageQueryResult<>();
    List<AlcNtcBill> list = dao.query(definition);
    PageQueryUtil.assignPageInfo(qpr, definition);
    qpr.setRecords(list);
    return qpr;
  }

  @Override
  public AlcNtcBill get(String uuid) throws IllegalArgumentException {
    if (StringUtil.isNullOrBlank(uuid))
      return null;
    AlcNtcBill alcNtcBill = dao.get(uuid);
    if (alcNtcBill == null)
      return null;
    Wrh wrh = binService.getWrh(alcNtcBill.getWrh().getUuid());
    alcNtcBill.setWrh(new UCN(wrh.getUuid(), wrh.getCode(), wrh.getName()));
    alcNtcBill.setItems(dao.queryItems(uuid));
    for (AlcNtcBillItem item : alcNtcBill.getItems()) {
      Article article = articleService.get(item.getArticle().getUuid());
      item.setArticle(new UCN(article.getUuid(), article.getCode(), article.getName()));
    }
    return alcNtcBill;
  }

  @Override
  public AlcNtcBill getByBillNumber(String billNumber) throws IllegalArgumentException {
    if (StringUtil.isNullOrBlank(billNumber))
      return null;
    AlcNtcBill alcNtcBill = dao.getByBillNumber(billNumber);
    if (alcNtcBill == null)
      return null;
    Wrh wrh = binService.getWrh(alcNtcBill.getWrh().getUuid());
    alcNtcBill.setWrh(new UCN(wrh.getUuid(), wrh.getCode(), wrh.getName()));
    alcNtcBill.setItems(dao.queryItems(alcNtcBill.getUuid()));
    return alcNtcBill;
  }

  @Override
  public void joinWave(String billNumber, long version, String waveBillNumber)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(billNumber, "billNumber");
    Assert.assertArgumentNotNull(version, "version");
    Assert.assertArgumentNotNull(waveBillNumber, "waveBillNumber");

    AlcNtcBill bill = getByBillNumber(billNumber);
    if (bill == null)
      throw new WMSException("配单" + billNumber + "不存在");
    PersistenceUtils.checkVersion(version, bill, AlcNtcBill.CAPTION, billNumber);

//    if (AlcNtcBillState.initial.equals(bill.getState()) == false
//        && StringUtil.isNullOrBlank(bill.getTaskBillNumber()) == false)
//      throw new WMSException("配单状态不是初始，不能加入波次");
    if (AlcNtcBillState.initial.equals(bill.getState()))
      bill.setState(AlcNtcBillState.inAlc);
    bill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    dao.update(bill);

    logger.injectContext(this, bill.getUuid(), AlcNtcBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "加入波次");
  }

  @Override
  public void leaveWave(String billNumber, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(billNumber, "billNumber");
    Assert.assertArgumentNotNull(version, "version");

    AlcNtcBill bill = getByBillNumber(billNumber);
    if (bill == null)
      throw new WMSException("配单" + billNumber + "不存在");
    PersistenceUtils.checkVersion(version, bill, AlcNtcBill.class.getName(), billNumber);

    if (AlcNtcBillState.inAlc.equals(bill.getState()) == false)
      throw new WMSException("配单" + billNumber + "不是待配货状态，不能踢出波次");

    bill.setState(AlcNtcBillState.initial);
//    bill.setTaskBillNumber(null);
    bill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());

    dao.update(bill);

    logger.injectContext(this, billNumber, AlcNtcBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "踢出波次");

  }

  @Override
  public void remove(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(version, "version");

    AlcNtcBill bill = get(uuid);
    if (bill == null)
      throw new WMSException("配单" + uuid + "不存在");
    PersistenceUtils.checkVersion(version, bill, AlcNtcBill.class.getName(), uuid);

    if ((AlcNtcBillState.aborted.equals(bill.getState())
        || AlcNtcBillState.initial.equals(bill.getState())) == false)
      throw new WMSException("只有初始或已作废的配单才能删除");

    dao.remove(uuid, version);
    dao.removeItems(uuid);

    logger.injectContext(this, uuid, AlcNtcBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_REMOVE, "删除配单");
  }

//  @Override
//  public void pickUp(List<DeliveryArticleInfo> infos)
//      throws IllegalArgumentException, WMSException {
//    Assert.assertArgumentNotNull(infos, "infos");
//
//    updateAlcNtcItemRealQty(infos);
//    updateAlcNtc(infos);
//
//  }

  private void updateAlcNtc(List<DeliveryArticleInfo> infos) {
    List<AlcNtcBill> alcNtcs = new ArrayList<>();
    for (DeliveryArticleInfo info : infos) {
      AlcNtcBill bill = dao.getByItemUuid(info.getItemUuid());
      assert bill != null;
      alcNtcs.add(bill);

    }

    Collections.sort(alcNtcs, new Comparator<AlcNtcBill>() {

      @Override
      public int compare(AlcNtcBill o1, AlcNtcBill o2) {
        return o1.getBillNumber().compareTo(o2.getBillNumber());
      }
    });

    Set<String> billNumbers = new HashSet<>();
    for (AlcNtcBill alcNtcBill : alcNtcs) {
      if (billNumbers.contains(alcNtcBill.getBillNumber()))
        continue;
      // TODO 判断对应拣货单是否拣货完成，待拣货服务
    }

  }

  private void updateAlcNtcItemRealQty(List<DeliveryArticleInfo> infos) {
    List<DeliveryArticleInfo> list = merge(infos);

    Collections.sort(list, new Comparator<DeliveryArticleInfo>() {

      @Override
      public int compare(DeliveryArticleInfo o1, DeliveryArticleInfo o2) {
        return o1.getItemUuid().compareTo(o2.getItemUuid());
      }
    });

    for (DeliveryArticleInfo info : infos) {
      AlcNtcBillItem item = dao.getItemByUuid(info.getItemUuid());

      if (item.getRealQty() == null)
        item.setRealQty(info.getQty());
      else
        item.setRealQty(item.getRealQty().add(info.getQty()));

      if (item.getRealQty().compareTo(item.getPlanQty()) > 0)
        continue;
      item.setRealCaseQtyStr(QpcHelper.qtyToCaseQtyStr(item.getRealQty(), item.getQpcStr()));
      dao.updateItem(item);
    }

  }

  private List<DeliveryArticleInfo> merge(List<DeliveryArticleInfo> infos) {
    List<DeliveryArticleInfo> result = new ArrayList<>();
    for (DeliveryArticleInfo info : infos) {
      boolean find = false;
      for (DeliveryArticleInfo delivery : result) {
        if (Objects.equals(info.getItemUuid(), delivery.getItemUuid())) {
          delivery.setQty(delivery.getQty().add(info.getQty()));
          find = true;
        }
      }
      if (find == false)
        result.add(info);
    }
    return result;
  }

//  @Override
//  public List<AlcNtcBill> getByTaskBillNumber(String taskBillNumber) {
//    if (StringUtil.isNullOrBlank(taskBillNumber))
//      return new ArrayList<>();
//    return dao.getByTaskBillNumber(taskBillNumber);
//  }

  @Override
  public void pickUp(String itemUuid, BigDecimal qty)
      throws IllegalArgumentException, WMSException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public List<String> queryArticleByWaveBillNumber(String waveBillNumber) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<WaveAlcNtcItem> queryAlcNtcItems(String waveBillNumber, List<String> articleUuids) {
    // TODO Auto-generated method stub
    return null;
  }

}
