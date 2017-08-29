/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	ReturnNtcBillServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月28日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.service.rtn.ntc;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.article.Article;
import com.hd123.sardine.wms.api.basicInfo.article.ArticleService;
import com.hd123.sardine.wms.api.basicInfo.article.DateCheckStandard;
import com.hd123.sardine.wms.api.basicInfo.bin.BinService;
import com.hd123.sardine.wms.api.basicInfo.bin.BinUsage;
import com.hd123.sardine.wms.api.basicInfo.bin.Wrh;
import com.hd123.sardine.wms.api.basicInfo.container.Container;
import com.hd123.sardine.wms.api.rtn.customerreturn.ReturnBill;
import com.hd123.sardine.wms.api.rtn.customerreturn.ReturnBillItem;
import com.hd123.sardine.wms.api.rtn.customerreturn.ReturnBillService;
import com.hd123.sardine.wms.api.rtn.customerreturn.ReturnType;
import com.hd123.sardine.wms.api.rtn.ntc.ReturnNtcBill;
import com.hd123.sardine.wms.api.rtn.ntc.ReturnNtcBillItem;
import com.hd123.sardine.wms.api.rtn.ntc.ReturnNtcBillService;
import com.hd123.sardine.wms.api.rtn.ntc.ReturnNtcBillState;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.DateHelper;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.common.utils.QpcHelper;
import com.hd123.sardine.wms.common.utils.StockConstants;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.dao.rtn.ntc.ReturnNtcBillDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.log.EntityLogger;

/**
 * @author yangwenzhu
 *
 */
public class ReturnNtcBillServiceImpl extends BaseWMSService implements ReturnNtcBillService {

  @Autowired
  private EntityLogger logger;
  @Autowired
  private ReturnNtcBillDao dao;
  @Autowired
  private BinService binService;
  @Autowired
  private ArticleService articleService;
  @Autowired
  private ReturnBillService rtnBillService;

  @Override
  public String saveNew(ReturnNtcBill bill) throws WMSException {
    Assert.assertArgumentNotNull(bill, "bill");
    bill.validate();
    if (dao.getByBillNumber(bill.getBillNumber()) != null)
      return null;
    verifyArticlAndWrh(bill);

    bill.setUuid(UUIDGenerator.genUUID());
    bill.setBillNumber(
        billNumberGenerator.allocateNextBillNumber(ReturnNtcBill.class.getSimpleName()));
    bill.setState(ReturnNtcBillState.initial);
    bill.setCreateInfo(ApplicationContextUtil.getOperateInfo());
    bill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    bill.setTotalReturnedAmount(BigDecimal.ZERO);
    bill.setTotalReturnedCaseQtyStr("0");

    for (ReturnNtcBillItem item : bill.getItems()) {
      item.setUuid(UUIDGenerator.genUUID());
      item.setReturnNtcBillUuid(bill.getUuid());
      item.setRealCaseQtyStr("0");
      item.setRealQty(BigDecimal.ZERO);
    }
    dao.insert(bill);
    dao.insertItems(bill.getItems());

    logger.injectContext(this, bill.getUuid(), ReturnNtcBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_ADDNEW, "新建退仓通知单");
    return bill.getUuid();
  }

  private void verifyArticlAndWrh(ReturnNtcBill bill) {
    assert bill != null;
    assert bill.getItems() != null && bill.getItems().isEmpty() == false;

    Wrh wrh = binService.getWrh(bill.getWrh().getUuid());
    if (Objects.isNull(wrh))
      throw new IllegalArgumentException(MessageFormat.format("仓位{0}不存在", bill.getWrh().getUuid()));
    for (ReturnNtcBillItem item : bill.getItems()) {
      Article article = articleService.get(item.getArticle().getUuid());
      if (Objects.isNull(article))
        throw new IllegalArgumentException(
            MessageFormat.format("商品{0}不存在", item.getArticle().getUuid()));
    }
  }

  @Override
  public void saveModify(ReturnNtcBill bill) throws WMSException {
    Assert.assertArgumentNotNull(bill, "bill");

    bill.validate();

    verifyArticlAndWrh(bill);

    ReturnNtcBill existBill = dao.get(bill.getUuid());

    if (Objects.isNull(existBill))
      throw new IllegalArgumentException(MessageFormat.format("要修改的退仓通知单{0}不存在", bill.getUuid()));
    PersistenceUtils.checkVersion(bill.getVersion(), existBill, ReturnNtcBill.CATPION,
        bill.getUuid());
    if (ReturnNtcBillState.initial.equals(existBill.getState()) == false)
      throw new IllegalArgumentException(
          MessageFormat.format("当前退仓通知单的状态是{0}，不是初始状态，不能编辑 ", bill.getState().getCaption()));

    bill.setState(ReturnNtcBillState.initial);
    bill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());

    for (ReturnNtcBillItem item : bill.getItems()) {
      item.setUuid(UUIDGenerator.genUUID());
      item.setReturnNtcBillUuid(bill.getUuid());
    }
    dao.update(bill);
    dao.removeItems(bill.getUuid());
    dao.insertItems(bill.getItems());

    logger.injectContext(this, bill.getUuid(), ReturnNtcBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "修改退仓通知单");
  }

  @Override
  public void remove(String uuid, long version) throws WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(version, "version");

    ReturnNtcBill bill = dao.get(uuid);
    if (Objects.isNull(bill))
      throw new IllegalArgumentException(MessageFormat.format("要删除的退仓通知单{0}不存在", uuid));
    PersistenceUtils.checkVersion(version, bill, ReturnNtcBill.CATPION, uuid);
    if (((ReturnNtcBillState.initial.equals(bill.getState()))
        || ReturnNtcBillState.aborted.equals(bill.getState())) == false)
      throw new IllegalArgumentException(
          MessageFormat.format("当前退仓通知单状态是{0}，不是初始或作废，不能删除该单据", bill.getState().getCaption()));

    dao.removeItems(uuid);
    dao.remove(uuid, version);

    logger.injectContext(this, uuid, ReturnNtcBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_REMOVE, "删除退仓通知单");
  }

  @Override
  public void abort(String uuid, long version) throws WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(version, "version");

    ReturnNtcBill bill = dao.get(uuid);
    if (Objects.isNull(bill))
      throw new IllegalArgumentException(MessageFormat.format("要作废的退仓通知单{0}不存在", uuid));
    PersistenceUtils.checkVersion(version, bill, ReturnNtcBill.CATPION, uuid);
    if (ReturnNtcBillState.aborted.equals(bill.getState()))
      return;
    if (ReturnNtcBillState.initial.equals(bill.getState()) == false)
      throw new IllegalArgumentException(
          MessageFormat.format("当前退仓通知单状态是{0}，不是初始状态，不能作废", bill.getState().getCaption()));

    bill.setState(ReturnNtcBillState.aborted);
    bill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());

    dao.update(bill);
    logger.injectContext(this, uuid, ReturnNtcBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_ABORT, "作废退仓通知单");
  }

  @Override
  public ReturnNtcBill get(String uuid) {
    if (StringUtil.isNullOrBlank(uuid))
      return null;
    ReturnNtcBill bill = dao.get(uuid);
    if (Objects.isNull(bill))
      return null;
    List<ReturnNtcBillItem> items = dao.getItems(bill.getUuid());
    bill.setItems(items);
    return bill;
  }

  @Override
  public ReturnNtcBill getByBillNumber(String billNumber) {
    if (StringUtil.isNullOrBlank(billNumber))
      return null;
    ReturnNtcBill bill = dao.getByBillNumber(billNumber);
    if (Objects.isNull(bill))
      return null;
    List<ReturnNtcBillItem> items = dao.getItems(bill.getUuid());
    bill.setItems(items);
    return bill;
  }

  @Override
  public PageQueryResult<ReturnNtcBill> query(PageQueryDefinition definition) {
    Assert.assertArgumentNotNull(definition, "definition");
    definition.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    List<ReturnNtcBill> list = dao.query(definition);
    PageQueryResult<ReturnNtcBill> pqr = new PageQueryResult<>();
    PageQueryUtil.assignPageInfo(pqr, definition);
    pqr.setRecords(list);
    return pqr;
  }

  @Override
  public void finish(String uuid, long version) throws WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(version, "version");

    ReturnNtcBill bill = dao.get(uuid);
    if (Objects.isNull(bill))
      throw new IllegalArgumentException(MessageFormat.format("要完成的退仓通知单{0}不存在", uuid));
    PersistenceUtils.checkVersion(version, bill, ReturnNtcBill.CATPION, uuid);
    if (ReturnNtcBillState.finished.equals(bill.getState()))
      return;
    if ((ReturnNtcBillState.initial.equals(bill.getState())
        || ReturnNtcBillState.inProgress.equals(bill.getState())) == false)
      throw new IllegalArgumentException(
          MessageFormat.format("退仓通知单当前状态是{0}，不是初始或进行中的，不能完成", bill.getState().getCaption()));

    bill.setTotalReturnedAmount(bill.getTotalAmount());
    bill.setTotalReturnedCaseQtyStr(bill.getTotalCaseQtyStr());
    bill.setState(ReturnNtcBillState.finished);
    bill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());

    List<ReturnNtcBillItem> items = dao.getItems(uuid);
    if (Objects.isNull(items) || items.isEmpty())
      throw new IllegalArgumentException("退仓通知单明细不存在");

    for (ReturnNtcBillItem item : items) {
      item.setRealCaseQtyStr(item.getCaseQtyStr());
      item.setRealQty(item.getQty());
    }

    dao.update(bill);
    dao.removeItems(uuid);
    dao.insertItems(items);

    // TODO 审核退仓单，待退仓单服务代码

    logger.injectContext(this, uuid, ReturnNtcBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "完成退仓通知单");
  }

  @Override
  public void returnWrh(String billNumber, Map<String, BigDecimal> returnInfo) throws WMSException {
    Assert.assertArgumentNotNull(billNumber, "billNumber");
    Assert.assertArgumentNotNull(returnInfo, "returnInfo");

    ReturnNtcBill bill = verifyRtnNtcBill(billNumber);

    StringBuilder errorMsg = verifyRtnNtcBillItem(bill, returnInfo);
    if (errorMsg.length() > 0)
      throw new WMSException(errorMsg.toString());

    // String totalCaseQtyStr = BigDecimal.ZERO.toString();
    String realTotalCaseQrtStr = BigDecimal.ZERO.toString();
    for (ReturnNtcBillItem item : bill.getItems()) {
      // QpcHelper.caseQtyStrAdd(totalCaseQtyStr,
      // QpcHelper.qtyToCaseQtyStr(item.getQty(), item.getQpcStr()));
      realTotalCaseQrtStr = QpcHelper.caseQtyStrAdd(realTotalCaseQrtStr,
          QpcHelper.qtyToCaseQtyStr(item.getRealQty(), item.getQpcStr()));
    }
    bill.setTotalReturnedCaseQtyStr(realTotalCaseQrtStr);
    if (isReturned(bill) == false)
      bill.setState(ReturnNtcBillState.inProgress);
    else
      bill.setState(ReturnNtcBillState.finished);
    bill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());

    dao.update(bill);

    logger.injectContext(this, bill.getBillNumber(), ReturnNtcBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "退货");
  }

  private boolean isReturned(ReturnNtcBill bill) {
    assert bill != null;
    List<ReturnNtcBillItem> items = bill.getItems();
    assert items != null && items.isEmpty() == false;
    for (ReturnNtcBillItem item : bill.getItems()) {
      if (item.getQty().compareTo(item.getRealQty()) > 0)
        return false;
    }
    return true;
  }

  private StringBuilder verifyRtnNtcBillItem(ReturnNtcBill bill,
      Map<String, BigDecimal> returnInfo) {
    assert bill != null;
    assert returnInfo != null;

    StringBuilder errorMsg = new StringBuilder();
    for (String key : returnInfo.keySet()) {
      ReturnNtcBillItem item = findNtcBillItem(bill, key);
      if (Objects.isNull(item)) {
        errorMsg.append(MessageFormat.format("商品{0}不在通知单范围内", key));
        continue;
      }
      BigDecimal rtnQty = returnInfo.get(key);
      if (BigDecimal.ZERO.compareTo(rtnQty) >= 0)
        errorMsg.append(MessageFormat.format("商品{0}的退仓数量必须大于0", item.getArticle().getCode()));
      if (rtnQty.compareTo(item.getQty().subtract(item.getRealQty())) > 0)
        errorMsg.append(MessageFormat.format("商品{0}的退仓数量{1}不能大于未退仓的数量{2}。",
            item.getArticle().getCode(), rtnQty, item.getQty().subtract(item.getRealQty())));

      item.setRealQty(item.getRealQty().add(rtnQty));
      item.setRealCaseQtyStr(QpcHelper.qtyToCaseQtyStr(item.getRealQty(), item.getQpcStr()));
    }
    return errorMsg;
  }

  private ReturnNtcBillItem findNtcBillItem(ReturnNtcBill bill, String key) {
    assert bill != null;
    assert key != null;
    for (ReturnNtcBillItem item : bill.getItems()) {
      if (item.getUuid().equals(key))
        return item;
    }
    return null;
  }

  private ReturnNtcBill verifyRtnNtcBill(String billNumber) {
    assert billNumber != null;

    ReturnNtcBill bill = dao.getByBillNumber(billNumber);
    if (Objects.isNull(bill))
      throw new IllegalArgumentException(MessageFormat.format("退仓通知单{0}不存在", billNumber));

    if ((ReturnNtcBillState.initial.equals(bill.getState())
        || ReturnNtcBillState.inProgress.equals(bill.getState())) == false)
      throw new IllegalArgumentException(
          MessageFormat.format("当前退仓通知单状态是{0}，不是初始或进行中的，不能退货", bill.getState().getCaption()));
    List<ReturnNtcBillItem> items = dao.getItems(bill.getUuid());
    if (Objects.nonNull(items) && items.isEmpty() == false)
      bill.setItems(items);
    return bill;
  }

  @Override
  public void genRtnBill(String uuid, long version) throws WMSException, ParseException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(version, "version");

    ReturnNtcBill bill = get(uuid);
    if (Objects.isNull(bill))
      throw new IllegalArgumentException(MessageFormat.format("要生成退仓单的退仓通知单{0}不存在", uuid));
    if ((ReturnNtcBillState.initial.equals(bill.getState())
        || ReturnNtcBillState.inProgress.equals(bill.getState())) == false)
      throw new IllegalArgumentException(
          MessageFormat.format("当前退仓退仓单的状态是{0}，不是初始或进行中的，不能生成退仓单", bill.getState().getCaption()));
    PersistenceUtils.checkVersion(version, bill, ReturnNtcBill.CATPION, uuid);

    ReturnBill rtnBill = buildRtnBill(bill);
    if (Objects.isNull(rtnBill))
      return;

    bill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    dao.update(bill);

    logger.injectContext(this, uuid, ReturnNtcBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "生成退仓单");

  }

  private ReturnBill buildRtnBill(ReturnNtcBill bill) throws WMSException, ParseException {

    assert bill != null;
    assert bill.getItems() != null;
    if (bill.getItems().isEmpty())
      return null;
    ReturnBill rtnBill = new ReturnBill();
    rtnBill.setCompanyUuid(bill.getCompanyUuid());
    rtnBill.setCustomer(bill.getCustomer());
    rtnBill.setReturnNtcBillNumber(bill.getBillNumber());
    rtnBill.setReturnor(new UCN(ApplicationContextUtil.getOperateInfo().getOperator().getId(),
        ApplicationContextUtil.getOperateInfo().getOperator().getCode(),
        ApplicationContextUtil.getOperateInfo().getOperator().getFullName()));
    rtnBill.setWrh(bill.getWrh());
    List<ReturnNtcBillItem> items = bill.getItems();
    List<ReturnBillItem> rtnItems = new ArrayList<>();
    items.sort(new Comparator<ReturnNtcBillItem>() {

      @Override
      public int compare(ReturnNtcBillItem o1, ReturnNtcBillItem o2) {
        return o1.getLine() - o2.getLine();
      }
    });
    for (ReturnNtcBillItem item : items) {
      if (item.getRealQty().compareTo(item.getQty()) >= 0)
        continue;
      ReturnBillItem rtnItem = new ReturnBillItem();
      rtnItem.setArticle(item.getArticle());
      rtnItem.setCaseQtyStr(QpcHelper.subtract(item.getCaseQtyStr(), item.getRealCaseQtyStr()));
      rtnItem.setLine(item.getLine());
      rtnItem.setMunit(item.getMunit());
      rtnItem.setPrice(item.getPrice());
      rtnItem.setQpcStr(item.getQpcStr());
      rtnItem.setQty(item.getQty().subtract(item.getRealQty()));
      rtnItem.setAmount(rtnItem.getPrice().multiply(rtnItem.getQty()));
      rtnItem.setReturnNtcBillItemUuid(item.getUuid());
      rtnItem.setReturnType(ReturnType.returnToSupplier);
      rtnItem.setSupplier(item.getSupplier());
      rtnItem.setContainerBarcode(Container.VIRTUALITY_CONTAINER);
      rtnItem.setBinCode(binService
          .getBinByWrhAndUsage(bill.getWrh().getUuid(), BinUsage.RtnReceiveTempBin).getCode());
      Article article = articleService.get(item.getArticle().getUuid());
      boolean flag = DateCheckStandard.none.equals(article.getExpflag());
      rtnItem.setProductionDate(
          flag ? DateHelper.strToDate(StockConstants.VISUAL_MAXDATE) : new Date());
      rtnItem.setValidDate(flag ? DateHelper.strToDate(StockConstants.VISUAL_MAXDATE)
          : DateHelper.addDays(rtnItem.getProductionDate(), article.getExpDays()));
      rtnItems.add(rtnItem);
    }
    if (rtnItems.isEmpty())
      return null;
    rtnBill.setItems(rtnItems);
    BigDecimal totalAmount = BigDecimal.ZERO;
    String totalCaseQtyStr = "0";
    for (ReturnBillItem item : rtnItems) {
      totalAmount = totalAmount.add(item.getAmount());
      totalCaseQtyStr = QpcHelper.caseQtyStrAdd(totalCaseQtyStr, item.getCaseQtyStr());
    }
    rtnBill.setTotalAmount(totalAmount);
    rtnBill.setTotalCaseQtyStr(totalCaseQtyStr);
    rtnBillService.saveNew(rtnBill);
    return rtnBill;
  }

  @Override
  public ReturnNtcBillItem getItem(String uuid) {
    if (StringUtil.isNullOrBlank(uuid))
      return null;
    return dao.getItemByUuid(uuid);
  }

}
