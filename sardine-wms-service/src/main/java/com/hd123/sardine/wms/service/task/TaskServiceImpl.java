/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	TaskServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月12日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.task;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.bin.Bin;
import com.hd123.sardine.wms.api.basicInfo.bin.BinService;
import com.hd123.sardine.wms.api.basicInfo.bin.BinUsage;
import com.hd123.sardine.wms.api.basicInfo.container.Container;
import com.hd123.sardine.wms.api.basicInfo.container.ContainerService;
import com.hd123.sardine.wms.api.basicInfo.container.ContainerState;
import com.hd123.sardine.wms.api.basicInfo.pickarea.OperateMode;
import com.hd123.sardine.wms.api.stock.Stock;
import com.hd123.sardine.wms.api.stock.StockExtendInfo;
import com.hd123.sardine.wms.api.stock.StockFilter;
import com.hd123.sardine.wms.api.stock.StockService;
import com.hd123.sardine.wms.api.task.ArticleMoveRule;
import com.hd123.sardine.wms.api.task.ContainerMoveRule;
import com.hd123.sardine.wms.api.task.Task;
import com.hd123.sardine.wms.api.task.TaskService;
import com.hd123.sardine.wms.api.task.TaskState;
import com.hd123.sardine.wms.api.task.TaskType;
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
import com.hd123.sardine.wms.dao.task.TaskDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.log.EntityLogger;

/**
 * @author zhangsai
 *
 */
public class TaskServiceImpl extends BaseWMSService implements TaskService {

  @Autowired
  private TaskDao taskDao;

  @Autowired
  private StockService stockService;

  @Autowired
  private BinService binService;

  @Autowired
  private ContainerService containerService;

  @Autowired
  private TaskVerifier taskVerifier;

  @Autowired
  private TaskHandler taskHandler;

  @Autowired
  private EntityLogger logger;

  @Override
  public void insert(List<Task> tasks)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(tasks, "tasks");

    if (tasks.isEmpty())
      return;

    for (Task task : tasks) {
      taskVerifier.verifySourceStock(task);

      task.setUuid(UUIDGenerator.genUUID());
      task.setTaskNo(billNumberGenerator.allocateNextBillNumber(task.getTaskType().name()));
      task.setCreator(ApplicationContextUtil.getLoginUser());
      task.setCreateTime(new Date());
      task.setState(TaskState.Initial);
      taskDao.insert(task);

      taskHandler.lockStock(task);

      logger.injectContext(this, task.getUuid(), Task.class.getName(),
          ApplicationContextUtil.getOperateContext());
      logger.log(EntityLogger.EVENT_ADDNEW, "新增" + task.getTaskType().getCaption());
    }
  }

  @Override
  public void abort(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");

    Task task = taskDao.get(uuid);
    if (task == null)
      throw new WMSException("作废的指令不存在！");
    PersistenceUtils.checkVersion(version, task, "指令", task.getTaskNo());

    if (task.getTaskType().equals(TaskType.Putaway))
      throw new WMSException("上架指令不能作废！");

    task.setState(TaskState.Aborted);
    task.setOperator(ApplicationContextUtil.getLoginUser());
    task.setBeginOperateTime(new Date());
    task.setEndOperateTime(new Date());
    taskDao.update(task);

    taskHandler.releaseStock(task);

    logger.injectContext(this, uuid, Task.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_ABORT, "作废" + task.getTaskType().getCaption());
  }

  @Override
  public void putaway(String uuid, long version, String toBinCode)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");

    Task task = taskDao.get(uuid);
    if (task == null)
      throw new WMSException("指定的指令不存在！");
    PersistenceUtils.checkVersion(version, task, "指令", task.getTaskNo());

    if (task.getTaskType().equals(TaskType.Putaway) == false)
      throw new WMSException("当前指令不是上架指令！");

    if (StringUtil.isNullOrBlank(toBinCode) == false)
      task.setToBinCode(toBinCode);
    task.setOperator(ApplicationContextUtil.getLoginUser());
    task.setBeginOperateTime(new Date());
    task.setEndOperateTime(new Date());
    taskDao.update(task);

    logger.injectContext(this, uuid, Task.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_ADDNEW, "执行上架指令");
  }

  @Override
  public PageQueryResult<Task> query(PageQueryDefinition definition) {
    Assert.assertArgumentNotNull(definition, "definition");

    PageQueryResult<Task> pgr = new PageQueryResult<Task>();
    List<Task> list = taskDao.query(definition);
    PageQueryUtil.assignPageInfo(pgr, definition);
    pgr.setRecords(list);
    return pgr;
  }

  @Override
  public List<Task> saveArticleMoveTask(List<ArticleMoveRule> articleMoveRules)
      throws IllegalArgumentException, WMSException {
    Assert.assertArgumentNotNull(articleMoveRules, "articleMoveRules");

    if (articleMoveRules.isEmpty())
      return new ArrayList<Task>();

    StringBuffer errorMessage = verifyArticleMove(articleMoveRules);
    if (errorMessage.length() > 0)
      throw new WMSException(errorMessage.toString());

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    List<Task> tasks = new ArrayList<Task>();
    for (ArticleMoveRule rule : articleMoveRules) {
      Task task = new Task();
      task.setArticle(new UCN(rule.getArticleUuid(), rule.getArticleCode(), rule.getArticleName()));
      task.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      task.setCreateTime(new Date());
      task.setCreator(ApplicationContextUtil.getLoginUser());
      task.setFromBinCode(rule.getFromBinCode());
      task.setFromContainerBarcode(rule.getFromContainerBarcode());
      task.setOwner(ApplicationContextUtil.getCompanyUuid());
      try {
        task.setProductionDate(sdf.parse(rule.getProductDate()));
      } catch (ParseException e) {
        throw new IllegalArgumentException("生产日期格式不合法！");
      }
      task.setQpcStr(rule.getQpcStr());
      task.setQty(rule.getQty());
      task.setCaseQtyStr(QpcHelper.qtyToCaseQtyStr(task.getQty(), task.getQpcStr()));
      task.setState(TaskState.Initial);
      task.setSupplier(
          new UCN(rule.getSupplierUuid(), rule.getSupplierCode(), rule.getSupplierName()));
      task.setTaskNo(billNumberGenerator.allocateNextBillNumber(TaskType.Move.name()));
      task.setTaskType(TaskType.Move);
      task.setToBinCode(rule.getToBinCode());
      task.setToContainerBarcode(rule.getToContainerBarcode());
      task.setUuid(UUIDGenerator.genUUID());
      task.setVersion(0);
      tasks.add(task);
      taskDao.insert(task);
    }
    return tasks;
  }

  private StringBuffer verifyArticleMove(List<ArticleMoveRule> articleMoveRules)
      throws WMSException {
    assert articleMoveRules != null;

    StringBuffer errorMessage = new StringBuffer();
    Map<String, String> toContainerAndBinMap = new HashMap<String, String>();
    for (ArticleMoveRule articleMoveRule : articleMoveRules) {
      articleMoveRule.validate();
      StockFilter stockFilter = new StockFilter();
      stockFilter.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      stockFilter.setArticleUuid(articleMoveRule.getArticleUuid());
      stockFilter.setBinCode(articleMoveRule.getFromBinCode());
      stockFilter.setContainerBarcode(articleMoveRule.getFromContainerBarcode());
      stockFilter.setSupplierUuid(articleMoveRule.getSupplierUuid());
      stockFilter.setQpcStr(articleMoveRule.getQpcStr());
      List<StockExtendInfo> infos = stockService.queryStockExtendInfo(stockFilter);
      if (articleMoveRule.getQty().compareTo(calculateStockQty(infos)) > 0)
        errorMessage.append("商品" + articleMoveRule.getArticleCode() + "移库数量大于库存可移库数量！\n");

      Bin toBin = binService.getBinByCode(articleMoveRule.getToBinCode());
      if (toBin == null) {
        errorMessage.append("商品" + articleMoveRule.getArticleCode() + "移库的目标货位不存在！\n");
        continue;
      }
      if (toBin.getUsage().equals(BinUsage.PickUpStorageBin))
        articleMoveRule.setToContainerBarcode(Container.VIRTUALITY_CONTAINER);
      if (Container.VIRTUALITY_CONTAINER.equals(articleMoveRule.getToContainerBarcode()) == false) {
        Container container = containerService
            .getByBarcode(articleMoveRule.getToContainerBarcode());
        if (container == null) {
          errorMessage.append("商品" + articleMoveRule.getArticleCode() + "移库的目标容器不存在！\n");
          continue;
        }

        if (container.getState().equals(ContainerState.STACONTAINERIDLE) == false
            && container.getPosition().equals(articleMoveRule.getToBinCode()) == false) {
          errorMessage.append("容器" + articleMoveRule.getToContainerBarcode() + "的当前位置"
              + container.getPosition() + "与目标货位" + articleMoveRule.getToBinCode() + "不一致！\n");
          continue;
        }
        String toPosition = toContainerAndBinMap.get(articleMoveRule.getToContainerBarcode());
        if (StringUtil.isNullOrBlank(toPosition) == false
            && toPosition.equals(articleMoveRule.getToBinCode()) == false) {
          errorMessage.append("容器" + articleMoveRule.getToContainerBarcode() + "同时存在于货位"
              + toPosition + "和货位" + articleMoveRule.getToBinCode() + "两个位置上！\n");
        }
      }
    }

    return errorMessage;
  }

  private BigDecimal calculateStockQty(List<StockExtendInfo> infos) {
    if (CollectionUtils.isEmpty(infos))
      return BigDecimal.ZERO;

    BigDecimal totalQty = BigDecimal.ZERO;
    for (StockExtendInfo info : infos) {
      totalQty = totalQty.add(info.getQty());
    }
    return totalQty;
  }

  private StringBuffer verifyContainerMoveRule(List<ContainerMoveRule> rules) {
    assert rules != null;

    StringBuffer errorMessage = new StringBuffer();
    Map<String, String> toContainerAndBinMap = new HashMap<String, String>();
    for (ContainerMoveRule rule : rules) {
      rule.validate();
      StockFilter stockFilter = new StockFilter();
      stockFilter.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      stockFilter.setBinCode(rule.getFromBinCode());
      stockFilter.setContainerBarcode(rule.getFromContainerBarcode());
      List<Stock> stocks = stockService.query(stockFilter);
      if (CollectionUtils.isEmpty(stocks)) {
        errorMessage.append("容器" + rule.getFromContainerBarcode() + "无库存，无法移库！\n");
        continue;
      }
      // if (hasOnwayStock(stocks)) {
      // errorMessage.append("容器" + rule.getFromContainerBarcode() +
      // "存在在途库存，正在被其他业务使用，无法移库！\n");
      // continue;
      // }

      Container fromContainer = containerService.getByBarcode(rule.getFromContainerBarcode());
      if (fromContainer == null) {
        errorMessage.append("容器" + rule.getFromContainerBarcode() + "不存在！\n");
        continue;
      }

      if (fromContainer.getState().equals(ContainerState.STACONTAINERUSEING) == false) {
        errorMessage.append("容器" + rule.getFromContainerBarcode() + "状态"
            + fromContainer.getState().getCaption() + "，不是已使用，不能移库！\n");
        continue;
      }

      Bin toBin = binService.getBinByCode(rule.getToBinCode());
      if (toBin == null) {
        errorMessage.append("容器" + rule.getFromContainerBarcode() + "移库的目标货位不存在！\n");
        continue;
      }
      if (toBin.getUsage().equals(BinUsage.PickUpStorageBin))
        rule.setToContainerBarcode(Container.VIRTUALITY_CONTAINER);
      if (Container.VIRTUALITY_CONTAINER.equals(rule.getToContainerBarcode()) == false) {
        Container container = containerService.getByBarcode(rule.getToContainerBarcode());
        if (container == null) {
          errorMessage.append("容器" + rule.getFromContainerBarcode() + "移库的目标容器不存在！\n");
          continue;
        }

        if (container.getState().equals(ContainerState.STACONTAINERIDLE) == false
            && container.getPosition().equals(rule.getToBinCode()) == false) {
          errorMessage.append("容器" + rule.getToContainerBarcode() + "的当前位置"
              + container.getPosition() + "与目标货位" + rule.getToBinCode() + "不一致！\n");
          continue;
        }
        String toPosition = toContainerAndBinMap.get(rule.getToContainerBarcode());
        if (StringUtil.isNullOrBlank(toPosition) == false
            && toPosition.equals(rule.getToBinCode()) == false) {
          errorMessage.append("容器" + rule.getToContainerBarcode() + "同时存在于货位" + toPosition + "和货位"
              + rule.getToBinCode() + "两个位置上！\n");
        }
      }
    }
    return errorMessage;
  }

  @Override
  public void saveAndMoveArticleMoveTask(List<ArticleMoveRule> articleMoveRules)
      throws IllegalArgumentException, WMSException {
    List<Task> tasks = saveArticleMoveTask(articleMoveRules);
    for (Task task : tasks) {
      articleMove(task.getUuid(), task.getVersion(), task.getQty());
    }
  }

  @Override
  public List<Task> saveContainerMoveTask(List<ContainerMoveRule> containerMoveRules)
      throws IllegalArgumentException, WMSException {
    if (CollectionUtils.isEmpty(containerMoveRules))
      return new ArrayList<Task>();

    StringBuffer errorMessage = verifyContainerMoveRule(containerMoveRules);
    if (errorMessage.length() > 0)
      throw new WMSException(errorMessage.toString());

    List<Task> tasks = new ArrayList<Task>();
    for (ContainerMoveRule rule : containerMoveRules) {
      Task task = new Task();
      task.setArticle(null);
      task.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      task.setCreateTime(new Date());
      task.setCreator(ApplicationContextUtil.getLoginUser());
      task.setFromBinCode(rule.getFromBinCode());
      task.setFromContainerBarcode(rule.getFromContainerBarcode());
      task.setOwner(ApplicationContextUtil.getCompanyUuid());
      task.setState(TaskState.Initial);
      task.setTaskNo(billNumberGenerator.allocateNextBillNumber(TaskType.Move.name()));
      task.setTaskType(TaskType.Move);
      task.setToBinCode(rule.getToBinCode());
      task.setToContainerBarcode(rule.getToContainerBarcode());
      task.setUuid(UUIDGenerator.genUUID());
      task.setVersion(0);
      tasks.add(task);
      taskDao.insert(task);
    }
    return tasks;
  }

  @Override
  public void saveAndMoveContainerMoveTask(List<ContainerMoveRule> containerMoveRules)
      throws IllegalArgumentException, WMSException {
    List<Task> tasks = saveContainerMoveTask(containerMoveRules);
    for (Task task : tasks) {
      containerMove(task.getUuid(), task.getVersion());
    }
  }

  @Override
  public void articleMove(String uuid, long version, BigDecimal realQty)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");

    Task task = taskDao.get(uuid);
    if (task == null)
      throw new IllegalArgumentException("指定的指令不存在！");
    PersistenceUtils.checkVersion(version, task, "指令", task.getTaskNo());

    ArticleMoveRule moveRule = new ArticleMoveRule();
    moveRule.setArticleUuid(task.getArticle().getUuid());
    moveRule.setArticleCode(task.getArticle().getCode());
    moveRule.setArticleName(task.getArticle().getName());
    moveRule.setFromBinCode(task.getFromBinCode());
    moveRule.setFromContainerBarcode(task.getFromContainerBarcode());
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    moveRule.setProductDate(sdf.format(task.getProductionDate()));
    moveRule.setQpcStr(task.getQpcStr());
    moveRule.setQty(task.getQty());
    moveRule.setSupplierCode(task.getSupplier().getCode());
    moveRule.setSupplierName(task.getSupplier().getName());
    moveRule.setSupplierUuid(task.getSupplier().getUuid());
    moveRule.setToBinCode(task.getToBinCode());
    moveRule.setToContainerBarcode(task.getToContainerBarcode());
    verifyArticleMove(Arrays.asList(moveRule));

    task.setRealQty(realQty == null ? task.getQty() : realQty);
    task.setRealCaseQtyStr(QpcHelper.qtyToCaseQtyStr(task.getRealQty(), task.getQpcStr()));
    task.setBeginOperateTime(new Date());
    task.setEndOperateTime(new Date());
    task.setOperator(ApplicationContextUtil.getLoginUser());
    task.setState(TaskState.Finished);
    task.setType(OperateMode.ManualBill);

    taskDao.update(task);

//    List<ShiftOutRule> shiftOutRules = new ArrayList<ShiftOutRule>();
//    ShiftOutRule outRule = new ShiftOutRule();
//    outRule.setArticleUuid(task.getArticle().getUuid());
//    outRule.setBinCode(task.getFromBinCode());
//    outRule.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
//    outRule.setContainerBarcode(task.getFromContainerBarcode());
//    outRule.setQpcStr(task.getQpcStr());
//    outRule.setQty(task.getRealQty());
//    outRule.setSupplierUuid(task.getSupplier().getUuid());
//    shiftOutRules.add(outRule);
//    List<Stock> outStocks = stockService.shiftOut(task.getTaskType().getCaption(), task.getTaskNo(),
//        shiftOutRules);
//    for (Stock stock : outStocks) {
//      stock.setBinCode(task.getToBinCode());
//      stock.setContainerBarcode(task.getToContainerBarcode());
//      stock.setModifyTime(new Date());
//    }
//    List<Stock> shiftInStocks = stockService.shiftIn(task.getTaskType().getCaption(),
//        task.getTaskNo(), outStocks);
//    for (Stock stock : shiftInStocks) {
//      TaskStockItem item = new TaskStockItem();
//      item.setArticleUuid(stock.getArticleUuid());
//      item.setBinCode(stock.getBinCode());
//      item.setContainerBarcode(stock.getContainerBarcode());
//      item.setMeasureUnit(stock.getMeasureUnit());
//      item.setPrice(stock.getPrice());
//      item.setProductionDate(stock.getProductionDate());
//      item.setQpcStr(stock.getQpcStr());
//      item.setQty(stock.getQty());
//      item.setStockBatch(stock.getStockBatch());
//      item.setSupplierUuid(stock.getSupplierUuid());
//      item.setTaskUuid(task.getUuid());
//      item.setValidDate(task.getValidDate());
//      taskDao.insertItem(item);
//    }
  }

  @Override
  public void containerMove(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException {/*
    Assert.assertArgumentNotNull(uuid, "uuid");

    Task task = taskDao.get(uuid);
    if (task == null)
      throw new IllegalArgumentException("指定的指令不存在！");
    PersistenceUtils.checkVersion(version, task, "指令", task.getTaskNo());

    ContainerMoveRule moveRule = new ContainerMoveRule();
    moveRule.setFromBinCode(task.getFromBinCode());
    moveRule.setFromContainerBarcode(task.getFromContainerBarcode());
    moveRule.setToBinCode(task.getToBinCode());
    moveRule.setToContainerBarcode(task.getToContainerBarcode());
    verifyContainerMoveRule(Arrays.asList(moveRule));

    task.setBeginOperateTime(new Date());
    task.setEndOperateTime(new Date());
    task.setOperator(ApplicationContextUtil.getLoginUser());
    task.setState(TaskState.Finished);
    task.setType(OperateType.Web);

    taskDao.update(task);

    List<ShiftOutRule> shiftOutRules = new ArrayList<ShiftOutRule>();
    ShiftOutRule outRule = new ShiftOutRule();
    outRule.setBinCode(task.getFromBinCode());
    outRule.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    outRule.setContainerBarcode(task.getFromContainerBarcode());
    shiftOutRules.add(outRule);
    List<Stock> outStocks = stockService.shiftOut(task.getTaskType().getCaption(), task.getTaskNo(),
        shiftOutRules);
    for (Stock stock : outStocks) {
      stock.setBinCode(task.getToBinCode());
      stock.setContainerBarcode(task.getToContainerBarcode());
      stock.setModifyTime(new Date());
    }
    List<Stock> shiftInStocks = stockService.shiftIn(task.getTaskType().getCaption(),
        task.getTaskNo(), outStocks);
    for (Stock stock : shiftInStocks) {
      TaskStockItem item = new TaskStockItem();
      item.setArticleUuid(stock.getArticleUuid());
      item.setBinCode(stock.getBinCode());
      item.setContainerBarcode(stock.getContainerBarcode());
      item.setMeasureUnit(stock.getMeasureUnit());
      item.setPrice(stock.getPrice());
      item.setProductionDate(stock.getProductionDate());
      item.setQpcStr(stock.getQpcStr());
      item.setQty(stock.getQty());
      item.setStockBatch(stock.getStockBatch());
      item.setSupplierUuid(stock.getSupplierUuid());
      item.setTaskUuid(task.getUuid());
      item.setValidDate(task.getValidDate());
      taskDao.insertItem(item);
    }

    Bin fromBin = binService.getBinByCode(task.getFromBinCode());
    if (stockService.binHasStock(task.getFromBinCode()) == false) {
      binService.changeState(fromBin.getUuid(), fromBin.getVersion(), BinState.free);
    }

    Bin toBin = binService.getBinByCode(task.getFromBinCode());
    if (toBin.getState().equals(BinState.free)) {
      binService.changeState(fromBin.getUuid(), fromBin.getVersion(), BinState.using);
    }

    if (Container.VIRTUALITY_CONTAINER.equals(task.getFromContainerBarcode()) == false) {
      // Container fromContainer =
      // containerService.getByBarcode(task.getFromContainerBarcode());

    }
  */}
}
