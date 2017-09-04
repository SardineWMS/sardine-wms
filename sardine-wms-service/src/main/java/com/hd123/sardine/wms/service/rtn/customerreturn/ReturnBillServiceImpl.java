/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	ReturnBillServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年7月6日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.service.rtn.customerreturn;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.article.Article;
import com.hd123.sardine.wms.api.basicInfo.article.ArticleService;
import com.hd123.sardine.wms.api.basicInfo.bin.Bin;
import com.hd123.sardine.wms.api.basicInfo.bin.BinService;
import com.hd123.sardine.wms.api.basicInfo.bin.BinUsage;
import com.hd123.sardine.wms.api.basicInfo.container.Container;
import com.hd123.sardine.wms.api.basicInfo.container.ContainerService;
import com.hd123.sardine.wms.api.basicInfo.container.ContainerState;
import com.hd123.sardine.wms.api.in.putaway.PutawayService;
import com.hd123.sardine.wms.api.rtn.customerreturn.ReturnBill;
import com.hd123.sardine.wms.api.rtn.customerreturn.ReturnBillItem;
import com.hd123.sardine.wms.api.rtn.customerreturn.ReturnBillService;
import com.hd123.sardine.wms.api.rtn.customerreturn.ReturnBillState;
import com.hd123.sardine.wms.api.rtn.customerreturn.ReturnType;
import com.hd123.sardine.wms.api.rtn.ntc.ReturnNtcBill;
import com.hd123.sardine.wms.api.rtn.ntc.ReturnNtcBillItem;
import com.hd123.sardine.wms.api.rtn.ntc.ReturnNtcBillService;
import com.hd123.sardine.wms.api.rtn.ntc.ReturnNtcBillState;
import com.hd123.sardine.wms.api.stock.StockComponent;
import com.hd123.sardine.wms.api.stock.StockService;
import com.hd123.sardine.wms.api.stock.StockShiftIn;
import com.hd123.sardine.wms.api.stock.StockState;
import com.hd123.sardine.wms.api.task.Task;
import com.hd123.sardine.wms.api.task.TaskService;
import com.hd123.sardine.wms.api.task.TaskType;
import com.hd123.sardine.wms.common.entity.OperateMode;
import com.hd123.sardine.wms.common.entity.SourceBill;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.dao.rtn.customerreturn.ReturnBillDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.log.EntityLogger;

/**
 * @author yangwenzhu
 *
 */
public class ReturnBillServiceImpl extends BaseWMSService implements ReturnBillService {

  @Autowired
  private ReturnBillDao dao;
  @Autowired
  private ReturnNtcBillService rtnNtcBillService;
  @Autowired
  private BinService binService;
  @Autowired
  private ContainerService containerService;
  @Autowired
  private ArticleService articleService;
  @Autowired
  private StockService stockService;
  @Autowired
  private TaskService taskService;
  @Autowired
  private PutawayService putawayService;
  @Autowired
  private EntityLogger logger;

  @Override
  public String saveNew(ReturnBill bill) throws WMSException, ParseException {
    Assert.assertArgumentNotNull(bill, "bill");

    verifyAndRefreshBill(bill);

    bill.setBillNumber(
        billNumberGenerator.allocateNextBillNumber(ReturnBill.class.getSimpleName()));
    bill.setCreateInfo(ApplicationContextUtil.getOperateInfo());
    bill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    bill.setState(ReturnBillState.initial);
    bill.setUuid(UUIDGenerator.genUUID());
    dao.insert(bill);

    for (ReturnBillItem item : bill.getItems()) {
      item.setUuid(UUIDGenerator.genUUID());
      item.setReturnBillUuid(bill.getUuid());
    }
    dao.insertItems(bill.getItems());
    logger.injectContext(this, bill.getUuid(), ReturnBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_ADDNEW, "新增退仓单");
    return bill.getUuid();
  }

  private void verifyAndRefreshBill(ReturnBill bill) throws WMSException, ParseException {
    assert bill != null;
    assert bill.getReturnNtcBillNumber() != null;
    ReturnNtcBill returnNtcBill = rtnNtcBillService.getByBillNumber(bill.getReturnNtcBillNumber());
    if (Objects.isNull(returnNtcBill))
      throw new WMSException(MessageFormat.format("退仓通知单“{0}”不存在。", bill.getReturnNtcBillNumber()));
    if ((ReturnNtcBillState.initial.equals(returnNtcBill.getState())
        || ReturnNtcBillState.inProgress.equals(returnNtcBill.getState())) == false)
      throw new WMSException(MessageFormat.format("退仓通知单状态是{0}，不是初始或进行中的，不能操作",
          returnNtcBill.getState().getCaption()));

    assert returnNtcBill.getItems() != null;
    assert bill.getItems() != null;
    for (ReturnNtcBillItem ntcItem : returnNtcBill.getItems()) {
      for (ReturnBillItem item : bill.getItems()) {
        if (ntcItem.getArticle().getUuid().equals(item.getArticle().getUuid())
            && ntcItem.getQpcStr().equals(item.getQpcStr())
            && ntcItem.getSupplier().getUuid().equals(item.getSupplier().getUuid())) {
          item.setReturnNtcBillItemUuid(ntcItem.getUuid());
        }
      }
    }

    bill.validate();

    StringBuffer errorMsg = new StringBuffer();
    errorMsg.append(verifyBinAndRefreshItems(bill, returnNtcBill));
    verifyAndRefreshArticles(errorMsg, bill.getItems());
    verifyContainers(errorMsg, bill);
    if (errorMsg.length() > 0)
      throw new WMSException(errorMsg.toString());
  }

  private void verifyContainers(StringBuffer errorMsg, ReturnBill bill) {
    assert errorMsg != null;
    assert bill != null;
    assert bill.getItems() != null;

    List<ReturnBillItem> items = bill.getItems();
    if (items.isEmpty())
      return;

    for (int i = 0; i < items.size(); i++) {
      ReturnBillItem item = items.get(i);
      if (Container.VIRTUALITY_CONTAINER.equals(item.getContainerBarcode()))
        continue;
      Container container = containerService.getByBarcode(item.getContainerBarcode());
      if (Objects.isNull(container)) {
        errorMsg.append(MessageFormat.format("第{0}行中，容器{1}不存在", i, item.getContainerBarcode()));
        continue;
      }
      if (ContainerState.STACONTAINERIDLE.equals(container.getState()) == false) {
        errorMsg.append(
            MessageFormat.format("第{0}行，容器状态是{1}，不是空闲状态", i, container.getState().getCaption()));
        continue;
      }
      for (int j = i + 1; j < items.size(); j++) {
        ReturnBillItem jItem = items.get(j);
        if (Container.VIRTUALITY_CONTAINER.equals(jItem.getContainerBarcode()))
          continue;
        Container jContainer = containerService.getByBarcode(jItem.getContainerBarcode());
        if (Objects.isNull(jContainer)) {
          errorMsg.append(MessageFormat.format("第{0}行中，容器{1}不存在", j, item.getContainerBarcode()));
          continue;
        }
        if (ContainerState.STACONTAINERIDLE.equals(container.getState()) == false) {
          errorMsg.append(
              MessageFormat.format("第{0}行，容器状态是{1}，不是空闲状态", i, container.getState().getCaption()));
          continue;
        }
        if (item.getReturnType().equals(jItem.getReturnType())
            && ReturnType.returnToSupplier.equals(item.getReturnType())) {
          if (item.getArticle().getUuid().equals(jItem.getArticle().getUuid())
              && item.getSupplier().getUuid().equals(jItem.getSupplier().getUuid()) == false) {
            errorMsg.append(MessageFormat.format("第{0}行和第{1}行，退货类型为退供应商时，不同供应商的商品不允许混载", i, j));
            continue;
          }
        }
        if (item.getReturnType().equals(jItem.getReturnType()) == false) {
          errorMsg.append(MessageFormat.format("第{0}行和第{1}行，好退和退供应商的商品不允许混载", i, j));
          continue;
        }
      }
    }

  }

  private void verifyAndRefreshArticles(StringBuffer errorMsg, List<ReturnBillItem> items)
      throws ParseException {
    assert errorMsg != null;
    assert items != null;

    for (ReturnBillItem item : items) {
      Article article = articleService.get(item.getArticle().getUuid());
      if (Objects.isNull(article))
        errorMsg.append(
            MessageFormat.format("第{0}行中，商品{1}不存在", item.getLine(), item.getArticle().getCode()));
      item.setProductionDate(
          article.calProductionDate(item.getProductionDate(), item.getValidDate()));
      item.setValidDate(article.calValidDate(item.getProductionDate(), item.getValidDate()));
      item.setStockBatch(stockBatchUtils.genProductionBatch(item.getProductionDate()));
      // 暂不校验到效期
    }

  }

  private StringBuffer verifyBinAndRefreshItems(ReturnBill bill, ReturnNtcBill returnNtcBill)
      throws WMSException {
    assert bill != null;
    assert returnNtcBill != null;
    assert bill.getWrh() != null;
    Bin bin = binService.getBinByWrhAndUsage(bill.getWrh().getUuid(), BinUsage.RtnReceiveTempBin);
    if (Objects.isNull(bin))
      throw new WMSException(
          MessageFormat.format("当前仓位{0}下没有退仓收货暂存位，无法进行退仓 ", bill.getWrh().getCode()));
    StringBuffer errorMsg = new StringBuffer();
    for (ReturnBillItem item : bill.getItems()) {
      ReturnNtcBillItem ntcBillItem = rtnNtcBillService.getItem(item.getReturnNtcBillItemUuid());
      if (Objects.isNull(ntcBillItem)) {
        errorMsg.append(MessageFormat.format("第{0}行的通知单明细不存在", item.getLine()));
        continue;
      }
      Bin supplierStorageBin = binService.getBinByCode(item.getBinCode());
      if (Objects.isNull(supplierStorageBin))
        throw new WMSException(MessageFormat.format("货位{0}不存在", item.getBinCode()));
      if (ReturnType.returnToSupplier.equals(item.getReturnType())
          && BinUsage.RtnReceiveTempBin.equals(supplierStorageBin.getUsage()) == false)
        errorMsg.append(MessageFormat.format("货位{0}用途不是退仓收货暂存位", item.getBinCode()));
      if (ReturnType.goodReturn.equals(item.getReturnType())
          && BinUsage.ReceiveStorageBin.equals(supplierStorageBin.getUsage()) == false)
        errorMsg.append(MessageFormat.format("货位{0}用途不是收货暂存位", item.getBinCode()));
      if (item.getQty().compareTo(ntcBillItem.getQty().subtract(ntcBillItem.getRealQty())) > 0) {
        errorMsg.append(MessageFormat.format("第{0}行中，退仓数量，不能大于可退数量", item.getLine()));
        continue;
      }

    }
    return errorMsg;
  }

  @Override
  public void saveModify(ReturnBill bill) throws WMSException, ParseException {
    Assert.assertArgumentNotNull(bill, "bill");

    ReturnBill existBill = get(bill.getUuid());
    if (Objects.isNull(existBill))
      throw new WMSException(MessageFormat.format("要编辑的退仓单{0}不存在，", bill.getUuid()));
    PersistenceUtils.checkVersion(bill.getVersion(), existBill, ReturnBill.CAPTION, bill.getUuid());
    if (ReturnBillState.initial.equals(existBill.getState()) == false)
      throw new WMSException(
          MessageFormat.format("当前退仓单的状态是{0}，不是初始状态，不能修改", bill.getState().getCaption()));

    verifyAndRefreshBill(bill);

    bill.setState(ReturnBillState.initial);
    bill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());

    for (ReturnBillItem item : bill.getItems()) {
      item.setUuid(UUIDGenerator.genUUID());
      item.setReturnBillUuid(bill.getUuid());
    }
    dao.update(bill);
    dao.removeItems(bill.getUuid());
    dao.insertItems(bill.getItems());

    logger.injectContext(this, bill.getUuid(), ReturnBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "修改退仓单");

  }

  @Override
  public void remove(String uuid, long version) throws WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(version, "version");

    ReturnBill bill = get(uuid);
    if (Objects.isNull(bill))
      throw new WMSException(MessageFormat.format("要删除的退仓单{0}不存在", uuid));
    PersistenceUtils.checkVersion(version, bill, ReturnBill.CAPTION, uuid);
    if (ReturnBillState.initial.equals(bill.getState()) == false)
      throw new WMSException(
          MessageFormat.format("当前退仓单的状态是{0}，不是初始，不能删除", bill.getState().getCaption()));
    dao.remove(uuid, version);
    dao.removeItems(uuid);

    logger.injectContext(this, uuid, ReturnBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_REMOVE, "删除退仓单");
  }

  @Override
  public ReturnBill get(String uuid) {
    if (StringUtil.isNullOrBlank(uuid))
      return null;
    ReturnBill bill = dao.get(uuid);
    if (Objects.isNull(bill))
      return null;
    List<ReturnBillItem> items = dao.getItems(uuid);
    bill.setItems(items);
    return bill;
  }

  @Override
  public ReturnBill getByBillNumber(String billNumber) {
    if (StringUtil.isNullOrBlank(billNumber))
      return null;
    return dao.getByBillNumber(billNumber);
  }

  @Override
  public PageQueryResult<ReturnBill> query(PageQueryDefinition definition) {
    Assert.assertArgumentNotNull(definition, "definition");
    PageQueryResult<ReturnBill> pqr = new PageQueryResult<>();
    definition.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    List<ReturnBill> list = dao.query(definition);
    PageQueryUtil.assignPageInfo(pqr, definition);
    pqr.setRecords(list);
    return pqr;
  }

  @Override
  public void audit(String uuid, long version) throws WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(version, "version");

    ReturnBill bill = get(uuid);
    if (Objects.isNull(bill))
      throw new IllegalArgumentException(MessageFormat.format("要审核的退仓单{0}不存在", uuid));
    if (ReturnBillState.finished.equals(bill.getState()))
      return;
    PersistenceUtils.checkVersion(version, bill, ReturnBill.CAPTION, uuid);

    if (ReturnBillState.initial.equals(bill.getState()) == false)
      throw new WMSException(
          MessageFormat.format("当前退仓单的状态是{0}，不是初始状态，不能审核", bill.getState().getCaption()));
    StringBuffer errorMsg = new StringBuffer();
    errorMsg.append(verifyContainer(bill));
    verifyItemQty(errorMsg, bill);
    if (errorMsg.length() > 0)
      throw new WMSException(errorMsg.toString());
    shiftIn(bill);
    manageContainer(bill);
    returnWrh(bill);

    bill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    bill.setState(ReturnBillState.finished);
    dao.update(bill);

    buildTaskWithContainer(bill);

    logger.injectContext(this, uuid, ReturnBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "审核退仓单");
  }

  private void buildTaskWithContainer(ReturnBill bill) throws WMSException {
    assert bill != null;

    List<Task> tasks = new ArrayList<>();
    for (ReturnBillItem item : bill.getItems()) {
      Task task = new Task();
      task.setArticle(item.getArticle());
      task.setArticleSpec(item.getArticleSpec());
      task.setCaseQtyStr(item.getCaseQtyStr());
      task.setCompanyUuid(bill.getCompanyUuid());
      task.setCreator(ApplicationContextUtil.getLoginUser());
      task.setFromBinCode(item.getBinCode());
      task.setFromContainerBarcode(item.getContainerBarcode());
      task.setMunit(item.getMunit());
      task.setOwner("");
      task.setProductionDate(item.getProductionDate());
      task.setProductionBatch(stockBatchUtils.genProductionBatch(item.getProductionDate()));
      task.setQpcStr(item.getQpcStr());
      task.setQty(item.getQty());
      task.setSourceBillNumber(bill.getBillNumber());
      task.setSourceBillType(ReturnBill.CAPTION);
      task.setSourceBillUuid(bill.getUuid());
      task.setStockBatch(item.getStockBatch());
      task.setSupplier(item.getSupplier());
      task.setTaskType(ReturnType.goodReturn.equals(item.getReturnType()) ? TaskType.Putaway
          : TaskType.RtnPutaway);
      task.setValidDate(item.getValidDate());
      task.setToContainerBarcode(Container.VIRTUALITY_CONTAINER);
      task.setToBinCode(ReturnType.goodReturn.equals(item.getReturnType())
          ? putawayService.fetchPutawayTargetBinByContainer(item.getContainerBarcode())
          : putawayService.fetchRtnPutawayTargetBinBySupplier(item.getSupplier().getUuid()));
      task.setType(OperateMode.ManualBill);
      task.setTaskGroupNumber(item.getContainerBarcode());
      tasks.add(task);
    }
    taskService.insert(tasks);
  }

  private void returnWrh(ReturnBill bill) throws WMSException {
    assert bill != null;
    assert bill.getItems() != null;

    ReturnNtcBill ntcBill = rtnNtcBillService.getByBillNumber(bill.getReturnNtcBillNumber());
    Map<String, BigDecimal> map = new HashMap<>();
    for (ReturnBillItem item : bill.getItems()) {
      map.put(item.getReturnNtcBillItemUuid(), item.getQty());
    }
    StringBuffer errorMsg = verifyReturnBill(ntcBill, map);
    if (errorMsg.length() > 0)
      throw new WMSException(errorMsg.toString());

    rtnNtcBillService.returnWrh(bill.getReturnNtcBillNumber(), map);
  }

  private StringBuffer verifyReturnBill(ReturnNtcBill ntcBill, Map<String, BigDecimal> map) {
    assert ntcBill != null;
    assert map != null;
    StringBuffer errorMsg = new StringBuffer();
    for (String key : map.keySet()) {
      ReturnNtcBillItem item = rtnNtcBillService.getItem(key);
      if (Objects.isNull(item)) {
        errorMsg.append(MessageFormat.format("通知单明细{0}不存在", key));
        continue;
      }
      if (item.getQty().subtract(item.getRealQty()).compareTo(map.get(key)) < 0) {
        errorMsg.append(MessageFormat.format("明细行{0}退仓数量不能大于可退数量", key));
        continue;
      }
    }
    return errorMsg;
  }

  private void manageContainer(ReturnBill bill) throws WMSException {
    assert bill != null;
    assert bill.getItems() != null;

    for (ReturnBillItem item : bill.getItems()) {
      Container container = containerService.getByBarcode(item.getContainerBarcode());
      containerService.change(container.getUuid(), container.getVersion(),
          ContainerState.STACONTAINERUSEING, item.getBinCode());
    }

  }

  private void shiftIn(ReturnBill bill) throws WMSException {
    assert bill != null;
    assert bill.getItems() != null;

    List<StockShiftIn> shiftIns = new ArrayList<>();
    for (ReturnBillItem item : bill.getItems()) {
      StockShiftIn shiftIn = new StockShiftIn();
      StockComponent component = new StockComponent();
      component.setArticle(item.getArticle());
      component.setBinCode(item.getBinCode());
      component.setCompanyUuid(bill.getCompanyUuid());
      component.setContainerBarcode(item.getContainerBarcode());
      component.setMunit(item.getMunit());
      component.setPrice(item.getPrice());
      component.setProductionDate(item.getProductionDate());
      component.setQpcStr(item.getQpcStr());
      component.setQty(item.getQty());
      component
          .setSourceBill(new SourceBill(ReturnBill.CAPTION, bill.getUuid(), bill.getBillNumber()));
      component.setState(StockState.normal);
      component.setStockBatch(item.getStockBatch());
      component.setSupplier(item.getSupplier());
      component.setValidDate(item.getValidDate());
      component
          .setProductionBatch(stockBatchUtils.genProductionBatch(component.getProductionDate()));
      component.setArticleSpec(item.getArticleSpec());
      shiftIn.setStockComponent(component);
      shiftIn.setSourceLineNumber(item.getLine());
      shiftIn.setSourceLineUuid(item.getUuid());
      shiftIns.add(shiftIn);
    }
    stockService.shiftIn(new SourceBill(ReturnBill.CAPTION, bill.getUuid(), bill.getBillNumber()),
        shiftIns);
  }

  private StringBuffer verifyContainer(ReturnBill bill) {
    assert bill != null;
    assert bill.getItems() != null;
    StringBuffer errorMsg = new StringBuffer();
    for (ReturnBillItem item : bill.getItems()) {
      Container container = containerService.getByBarcode(item.getContainerBarcode());
      if (Objects.isNull(container)) {
        errorMsg.append(
            MessageFormat.format("第{0}行中的容器{1}不存在", item.getLine(), item.getContainerBarcode()));
        continue;
      }
      if (Container.VIRTUALITY_CONTAINER.equals(item.getContainerBarcode())) {
        errorMsg.append(MessageFormat.format("第{0}行中，容器是虚拟容器，不能审核，请修改退仓容器", item.getLine()));
        continue;
      }
      if (ContainerState.STACONTAINERIDLE.equals(container.getState()) == false) {
        errorMsg.append(MessageFormat.format("第{0}行中，容器状态是{1}，不是空闲", item.getLine(),
            container.getState().getCaption()));
        continue;
      }
    }
    return errorMsg;
  }

  private void verifyItemQty(StringBuffer errorMsg, ReturnBill bill) throws WMSException {
    assert bill != null;

    ReturnNtcBill ntcBill = rtnNtcBillService.getByBillNumber(bill.getReturnNtcBillNumber());
    if (Objects.isNull(ntcBill))
      throw new WMSException(
          MessageFormat.format("退仓单对应的退仓通知单{0}不存在", bill.getReturnNtcBillNumber()));
    if ((ReturnNtcBillState.initial.equals(ntcBill.getState())
        || ReturnNtcBillState.inProgress.equals(ntcBill.getState())) == false)
      throw new WMSException(MessageFormat.format("退仓单对应的退仓通知单{0}的状态是{1}，不是初始或进行中的，不能审核该退仓单",
          bill.getReturnNtcBillNumber(), ntcBill.getState().getCaption()));
    for (ReturnBillItem item : bill.getItems()) {
      ReturnNtcBillItem ntcBillItem = rtnNtcBillService.getItem(item.getReturnNtcBillItemUuid());
      if (Objects.isNull(ntcBillItem)) {
        errorMsg.append(MessageFormat.format("第{0}行中，找不到对应的退仓通知单明细", item.getLine()));
        continue;
      }
      if (Objects.equals(item.getArticle().getUuid(),
          ntcBillItem.getArticle().getUuid()) == false) {
        errorMsg.append(MessageFormat.format("第{0}行与退仓通知单明细的商品不一致 ", item.getLine()));
        continue;
      }
      if (item.getQty().compareTo(ntcBillItem.getQty().subtract(ntcBillItem.getRealQty())) > 0) {
        errorMsg.append(MessageFormat.format("第{0}行中，退仓数量不能大于通知单中的可退数量", item.getLine()));
        continue;
      }
    }

  }

}
