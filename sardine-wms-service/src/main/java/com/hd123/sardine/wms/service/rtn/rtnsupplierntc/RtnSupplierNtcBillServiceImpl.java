/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	RtnSupplierNtcBillServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月15日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.service.rtn.rtnsupplierntc;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.article.Article;
import com.hd123.sardine.wms.api.basicInfo.article.ArticleService;
import com.hd123.sardine.wms.api.basicInfo.bin.BinService;
import com.hd123.sardine.wms.api.basicInfo.bin.Wrh;
import com.hd123.sardine.wms.api.basicInfo.container.Container;
import com.hd123.sardine.wms.api.basicInfo.supplier.Supplier;
import com.hd123.sardine.wms.api.basicInfo.supplier.SupplierService;
import com.hd123.sardine.wms.api.basicInfo.supplier.SupplierState;
import com.hd123.sardine.wms.api.rtn.returnsupplier.ReturnSupplierBill;
import com.hd123.sardine.wms.api.rtn.returnsupplier.ReturnSupplierBillService;
import com.hd123.sardine.wms.api.rtn.returnsupplier.ReturnSupplierBillState;
import com.hd123.sardine.wms.api.rtn.rtnsupplierntc.RtnSupplierNtcBill;
import com.hd123.sardine.wms.api.rtn.rtnsupplierntc.RtnSupplierNtcBillItem;
import com.hd123.sardine.wms.api.rtn.rtnsupplierntc.RtnSupplierNtcBillService;
import com.hd123.sardine.wms.api.rtn.rtnsupplierntc.RtnSupplierNtcBillState;
import com.hd123.sardine.wms.api.stock.Stock;
import com.hd123.sardine.wms.api.task.Task;
import com.hd123.sardine.wms.api.task.TaskService;
import com.hd123.sardine.wms.api.task.TaskType;
import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.DateHelper;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.common.utils.QpcHelper;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.dao.rtn.rtnsupplierntc.RtnSupplierNtcBillDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.log.EntityLogger;

/**
 * @author yangwenzhu
 *
 */
public class RtnSupplierNtcBillServiceImpl extends BaseWMSService
    implements RtnSupplierNtcBillService {

  @Autowired
  private RtnSupplierNtcBillDao dao;

  @Autowired
  private ArticleService articleService;

  @Autowired
  private BinService binService;

  @Autowired
  private SupplierService supplierService;

  @Autowired
  private TaskService taskService;

  @Autowired
  private ReturnSupplierBillService returnSupplierBillService;

  @Autowired
  private EntityLogger logger;

  @Override
  public PageQueryResult<RtnSupplierNtcBill> query(PageQueryDefinition definition) {
    Assert.assertArgumentNotNull(definition, "definition");
    List<RtnSupplierNtcBill> list = dao.query(definition);
    PageQueryResult<RtnSupplierNtcBill> pqr = new PageQueryResult<>();
    PageQueryUtil.assignPageInfo(pqr, definition);
    pqr.setRecords(list);
    return pqr;
  }

  @Override
  public RtnSupplierNtcBill get(String uuid) {
    if (StringUtil.isNullOrBlank(uuid))
      return null;
    RtnSupplierNtcBill bill = dao.get(uuid);
    if (Objects.isNull(bill))
      return null;
    List<RtnSupplierNtcBillItem> items = dao.queryItems(bill.getUuid());
    bill.setItems(items);
    return dao.get(uuid);
  }

  @Override
  public String saveNew(RtnSupplierNtcBill bill) throws WMSException {
    Assert.assertArgumentNotNull(bill, "bill");

    bill.validate();
    if (new Date().after(DateHelper.addDays(bill.getRtnDate(), 1)))
      throw new WMSException("退货日期不能早于当前日期");
    validateArticle(bill);
    validateSupplier(bill.getSupplier().getUuid());
    bill.setUuid(UUIDGenerator.genUUID());
    bill.setBillNumber(
        billNumberGenerator.allocateNextBillNumber(RtnSupplierNtcBill.class.getSimpleName()));
    bill.setState(RtnSupplierNtcBillState.Initial);
    bill.setCreateInfo(ApplicationContextUtil.getOperateInfo());
    bill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());

    List<RtnSupplierNtcBillItem> items = bill.getItems();
    for (int i = 0; i < items.size(); i++) {
      items.get(i).setUuid(UUIDGenerator.genUUID());
      items.get(i).setRtnSupplierNtcBillUuid(bill.getUuid());
      items.get(i).setLine(i + 1);
    }
    dao.insert(bill);
    dao.insertItems(bill.getItems());

    logger.injectContext(this, bill.getUuid(), RtnSupplierNtcBill.CAPTION,
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_ADDNEW, "新增供应商退货通知单");
    return bill.getUuid();
  }

  private void validateSupplier(String uuid) throws WMSException {
    assert uuid != null;
    Supplier supplier = supplierService.get(uuid);
    if (Objects.isNull(supplier))
      throw new WMSException(MessageFormat.format("供应商{0}不存在", uuid));
    if (SupplierState.offline.equals(supplier.getState()))
      throw new WMSException(MessageFormat.format("当前供应商的状态是{0}，不是正常状态，不能新建供应商退货通知单",
          supplier.getState().getCaption()));
  }

  private void validateArticle(RtnSupplierNtcBill bill) throws WMSException {
    assert bill != null;
    assert bill.getItems() != null;

    Wrh wrh = binService.getWrh(bill.getWrh().getUuid());
    if (Objects.isNull(wrh))
      throw new WMSException(MessageFormat.format("仓位{0}不存在", bill.getWrh().getUuid()));

    List<RtnSupplierNtcBillItem> items = bill.getItems();
    if (CollectionUtils.isEmpty(items))
      return;
    for (RtnSupplierNtcBillItem item : items) {
      Article article = articleService.get(item.getArticle().getUuid());
      if (Objects.isNull(article))
        throw new WMSException(MessageFormat.format("明细中商品{0}不存在", item.getArticle().getUuid()));
    }

  }

  @Override
  public void saveModify(RtnSupplierNtcBill bill) throws WMSException {
    Assert.assertArgumentNotNull(bill, "bill");
    bill.validate();
    RtnSupplierNtcBill existBill = get(bill.getUuid());
    if (Objects.isNull(existBill))
      throw new WMSException("要修改的供应商退货通知单不存在");
    PersistenceUtils.checkVersion(bill.getVersion(), existBill, RtnSupplierNtcBill.CAPTION,
        bill.getUuid());
    if (RtnSupplierNtcBillState.Initial.equals(bill.getState()) == false)
      throw new WMSException(
          MessageFormat.format("当前供应商退货通知单状态是{0}，不是初始状态，不允许修改", bill.getState().getCaption()));
    validateSupplier(bill.getSupplier().getUuid());
    validateArticle(bill);

    List<RtnSupplierNtcBillItem> items = bill.getItems();
    for (int i = 0; i < items.size(); i++) {
      items.get(i).setLine(i + 1);
    }

    bill.setState(RtnSupplierNtcBillState.Initial);
    bill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());

    for (RtnSupplierNtcBillItem item : bill.getItems()) {
      item.setUuid(UUIDGenerator.genUUID());
      item.setRtnSupplierNtcBillUuid(bill.getUuid());
    }

    dao.update(bill);
    dao.removeItmes(bill.getUuid());
    dao.insertItems(bill.getItems());

    logger.injectContext(this, bill.getUuid(), RtnSupplierNtcBill.CAPTION,
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "修改供应商退货通知单");
  }

  @Override
  public void abort(String uuid, long version) throws IllegalArgumentException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(version, "version");

    RtnSupplierNtcBill bill = get(uuid);
    if (Objects.isNull(bill))
      throw new WMSException(MessageFormat.format("要删除的供应商退货通知单{0}不存在", bill.getUuid()));
    PersistenceUtils.checkVersion(version, bill, RtnSupplierNtcBill.CAPTION, uuid);
    if (RtnSupplierNtcBillState.Aborted.equals(bill.getState()))
      return;
    if (RtnSupplierNtcBillState.Initial.equals(bill.getState()) == false)
      throw new WMSException(
          MessageFormat.format("当前供应商退货通知单的状态是{0}，不是初始状态，不能作废", bill.getState().getCaption()));

    bill.setState(RtnSupplierNtcBillState.Aborted);
    bill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());

    dao.update(bill);

    logger.injectContext(this, uuid, RtnSupplierNtcBill.CAPTION,
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_ABORT, "作废供应商退货通知单");
  }

  @Override
  public void finish(String uuid, long version) throws IllegalArgumentException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(version, "version");

    RtnSupplierNtcBill bill = get(uuid);
    if (Objects.isNull(bill))
      throw new WMSException("要完成的供应商退货通知单不存在！");
    if (RtnSupplierNtcBillState.Finished.equals(bill.getState()))
      return;
    if (RtnSupplierNtcBillState.Aborted.equals(bill.getState()))
      throw new WMSException(MessageFormat.format("当前供应商退货通知单的状态是{0}，不是初始或进行中的状态，不能完成。",
          bill.getState().getCaption()));

    PersistenceUtils.checkVersion(version, bill, RtnSupplierNtcBill.CAPTION, uuid);

    bill.setState(RtnSupplierNtcBillState.Finished);
    bill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    dao.update(bill);

    PageQueryDefinition definition = new PageQueryDefinition();
    definition.setPageSize(0);
    definition.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    definition.put(ReturnSupplierBillService.QUERY_RTNSUPPLIERNTCBILLNUMBER_LIKE,
        bill.getBillNumber());
    PageQueryResult<ReturnSupplierBill> pqr = returnSupplierBillService.query(definition);
    List<ReturnSupplierBill> returnSupplierBills = pqr.getRecords();
    if (CollectionUtils.isEmpty(returnSupplierBills) == false) {
      for (ReturnSupplierBill returnSupplierBill : returnSupplierBills) {
        if (ReturnSupplierBillState.InProgress.equals(returnSupplierBill.getState()) == false)
          continue;
        returnSupplierBillService.finish(returnSupplierBill.getUuid(),
            returnSupplierBill.getVersion());
      }
    }

    logger.injectContext(this, uuid, RtnSupplierNtcBill.CAPTION,
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "完成退货通知单");
  }

  @Override
  public void unshelve(String itemUuid, BigDecimal unshelvedQty)
      throws IllegalArgumentException, WMSException {
    Assert.assertArgumentNotNull(itemUuid, "itemUuid");
    Assert.assertArgumentNotNull(unshelvedQty, "unshelvedQty");

    RtnSupplierNtcBillItem item = dao.getItem(itemUuid);
    if (Objects.isNull(item))
      throw new WMSException("该下架指令对应的供应商退货通知单明细不存在");
    RtnSupplierNtcBill bill = get(item.getRtnSupplierNtcBillUuid());
    if (Objects.isNull(bill))
      throw new WMSException("该下架指令对应的供应商退货通知单不存在");

    if (bill.getState().equals(RtnSupplierNtcBillState.Finished))
      throw new WMSException("对应的退货通知单已结束，请作废指令！");

    if (bill.getUnshelvedAmount() == null)
      bill.setUnshelvedAmount(item.getPrice().multiply(unshelvedQty));
    else
      bill.setUnshelvedAmount(
          bill.getUnshelvedAmount().add(item.getPrice().multiply(unshelvedQty)));
    if (StringUtil.isNullOrBlank(bill.getUnshelvedCaseQtyStr()))
      bill.setUnshelvedCaseQtyStr(QpcHelper.qtyToCaseQtyStr(unshelvedQty, item.getQpcStr()));
    else
      bill.setUnshelvedCaseQtyStr(QpcHelper.caseQtyStrAdd(bill.getUnshelvedCaseQtyStr(),
          QpcHelper.qtyToCaseQtyStr(unshelvedQty, item.getQpcStr())));
    bill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    dao.update(bill);

    if (item.getUnshelvedQty() == null)
      item.setUnshelvedQty(unshelvedQty);
    else
      item.setUnshelvedQty(item.getUnshelvedQty().add(unshelvedQty));
    if (StringUtil.isNullOrBlank(item.getUnshelvedCaseQtyStr()))
      item.setUnshelvedCaseQtyStr(QpcHelper.qtyToCaseQtyStr(unshelvedQty, item.getQpcStr()));
    else
      item.setUnshelvedCaseQtyStr(QpcHelper.caseQtyStrAdd(item.getUnshelvedCaseQtyStr(),
          QpcHelper.qtyToCaseQtyStr(unshelvedQty, item.getQpcStr())));
    dao.updateItem(item);

    logger.injectContext(this, bill.getBillNumber(), RtnSupplierNtcBill.CAPTION,
        ApplicationContextUtil.getOperateContext());
    logger.log("下架", "商品" + item.getArticle().getCode() + "下架数量" + unshelvedQty);
  }

  @Override
  public void genUnshelveTask(String uuid, long version)
      throws IllegalArgumentException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(version, "version");

    RtnSupplierNtcBill bill = get(uuid);
    if (Objects.isNull(bill))
      throw new WMSException(MessageFormat.format("要生成下架指令的供应商退货通知单{0}不存在", uuid));
    if (RtnSupplierNtcBillState.Initial.equals(bill.getState()) == false)
      throw new WMSException(
          MessageFormat.format("当前供应商退货通知单状态是{0},不是初始，不能生成下架指令", bill.getState().getCaption()));
    PersistenceUtils.checkVersion(version, bill, RtnSupplierNtcBill.CAPTION, uuid);

    buildUnshelveTask(bill);

    bill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    dao.update(bill);

    logger.injectContext(this, uuid, RtnSupplierNtcBill.CAPTION,
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "生成下架指令");
  }

  private void buildUnshelveTask(RtnSupplierNtcBill bill)
      throws VersionConflictException, IllegalArgumentException, WMSException {
    assert bill != null;

    List<Task> tasks = new ArrayList<Task>();
    for (RtnSupplierNtcBillItem item : bill.getItems()) {
      List<Stock> stocks = dao.queryWaitUnShelveStocks(bill.getWrh().getUuid(),
          item.getArticle().getUuid(), bill.getSupplier().getUuid());
      BigDecimal waitUnShelveQty = item.getQty();
      for (Stock stock : stocks) {
        if (waitUnShelveQty.compareTo(BigDecimal.ZERO) <= 0)
          break;
        Task task = new Task();
        task.setArticle(item.getArticle());
        task.setArticleSpec(stock.getStockComponent().getArticleSpec());
        task.setCompanyUuid(bill.getCompanyUuid());
        task.setCreator(ApplicationContextUtil.getLoginUser());
        task.setFromBinCode(stock.getStockComponent().getBinCode());
        task.setFromContainerBarcode(stock.getStockComponent().getContainerBarcode());
        task.setMunit(item.getMunit());
        task.setOperator(ApplicationContextUtil.getLoginUser());
        task.setOwner(bill.getSupplier().getUuid());
        task.setProductionDate(stock.getStockComponent().getProductionDate());
        task.setProductionBatch(stock.getStockComponent().getProductionBatch());
        task.setQpcStr(item.getQpcStr());
        if (waitUnShelveQty.compareTo(stock.getStockComponent().getQty()) <= 0)
          task.setQty(waitUnShelveQty);
        else
          task.setQty(stock.getStockComponent().getQty());
        task.setCaseQtyStr(QpcHelper.qtyToCaseQtyStr(task.getQty(), task.getQpcStr()));
        task.setSourceBillLineUuid(item.getUuid());
        task.setSourceBillNumber(bill.getBillNumber());
        task.setSourceBillType(ReturnSupplierBill.CAPTION);
        task.setSourceBillUuid(bill.getUuid());
        task.setStockBatch(stock.getStockComponent().getStockBatch());
        task.setSupplier(bill.getSupplier());
        task.setTaskGroupNumber(stock.getStockComponent().getContainerBarcode());
        task.setTaskType(TaskType.RtnShelf);
        task.setToContainerBarcode(Container.VIRTUALITY_CONTAINER);
        task.setValidDate(stock.getStockComponent().getValidDate());
        tasks.add(task);

        waitUnShelveQty = waitUnShelveQty.subtract(task.getQty());
      }
    }
    taskService.insert(tasks);
  }

  @Override
  public RtnSupplierNtcBillItem getItem(String uuid) throws WMSException {
    if (StringUtil.isNullOrBlank(uuid))
      return null;
    return dao.getItem(uuid);
  }

  @Override
  public void handover(String billNumber, String articleUuid, BigDecimal qty) throws WMSException {
    RtnSupplierNtcBill ntcBill = dao.getByBillNumber(billNumber);
    if (ntcBill == null)
      throw new WMSException("退货通知单" + billNumber + "不存在！");
    List<RtnSupplierNtcBillItem> items = dao.queryItems(ntcBill.getUuid());
    boolean finish = true;
    String articleCode = null;
    for (RtnSupplierNtcBillItem item : items) {
      if (item.getArticle().getUuid().equals(articleUuid)) {
        articleCode = item.getArticle().getCode();
        item.setRealQty(item.getRealQty().add(qty));
        item.setRealCaseQtyStr(QpcHelper.qtyToCaseQtyStr(item.getRealQty(), item.getQpcStr()));

        if (item.getUnshelvedQty().compareTo(item.getRealQty()) < 0)
          throw new WMSException("商品" + item.getArticle().getCode() + "退货数量大于下架数量！");

        dao.updateItem(item);

        if (StringUtil.isNullOrBlank(ntcBill.getRealCaseQtyStr()))
          ntcBill.setRealCaseQtyStr(item.getRealCaseQtyStr());
        else
          ntcBill.setRealCaseQtyStr(QpcHelper.caseQtyStrAdd(ntcBill.getRealCaseQtyStr(),
              QpcHelper.qtyToCaseQtyStr(qty, item.getQpcStr())));
        if (ntcBill.getRealAmount() == null)
          ntcBill.setRealAmount(item.getRealQty().multiply(item.getPrice()));
        else
          ntcBill.setRealAmount(
              ntcBill.getRealAmount().add(item.getRealQty().multiply(item.getPrice())));
      }
      if (item.getUnshelvedQty().compareTo(item.getRealQty()) > 0
          || item.getQty().compareTo(item.getUnshelvedQty()) > 0)
        finish = false;
    }

    if (finish)
      ntcBill.setState(RtnSupplierNtcBillState.Finished);
    else
      ntcBill.setState(RtnSupplierNtcBillState.InProgress);
    ntcBill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    dao.update(ntcBill);

    logger.injectContext(this, ntcBill.getUuid(), RtnSupplierNtcBill.CAPTION,
        ApplicationContextUtil.getOperateContext());
    logger.log("退货交接", "商品" + articleCode + "交接数量" + qty);
  }
}
