/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	DecIncInvBillServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月17日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.service.inner.decincinv;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.article.Article;
import com.hd123.sardine.wms.api.basicInfo.article.ArticleService;
import com.hd123.sardine.wms.api.basicInfo.bin.Bin;
import com.hd123.sardine.wms.api.basicInfo.bin.BinService;
import com.hd123.sardine.wms.api.basicInfo.bin.BinState;
import com.hd123.sardine.wms.api.basicInfo.bin.Wrh;
import com.hd123.sardine.wms.api.basicInfo.container.Container;
import com.hd123.sardine.wms.api.basicInfo.container.ContainerService;
import com.hd123.sardine.wms.api.basicInfo.container.ContainerState;
import com.hd123.sardine.wms.api.ia.user.User;
import com.hd123.sardine.wms.api.ia.user.UserService;
import com.hd123.sardine.wms.api.inner.decincinv.DecIncInvBill;
import com.hd123.sardine.wms.api.inner.decincinv.DecIncInvBillItem;
import com.hd123.sardine.wms.api.inner.decincinv.DecIncInvBillService;
import com.hd123.sardine.wms.api.inner.decincinv.DecIncInvBillState;
import com.hd123.sardine.wms.api.inner.decincinv.DecIncInvBillType;
import com.hd123.sardine.wms.api.stock.Stock;
import com.hd123.sardine.wms.api.stock.StockComponent;
import com.hd123.sardine.wms.api.stock.StockFilter;
import com.hd123.sardine.wms.api.stock.StockService;
import com.hd123.sardine.wms.api.stock.StockShiftIn;
import com.hd123.sardine.wms.api.stock.StockShiftRule;
import com.hd123.sardine.wms.common.entity.OperateInfo;
import com.hd123.sardine.wms.common.entity.SourceBill;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.dao.inner.decincinv.DecIncInvBillDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.log.EntityLogger;

/**
 * @author fanqingqing
 *
 */
public class DecIncInvBillServiceImpl extends BaseWMSService implements DecIncInvBillService {

  @Autowired
  private DecIncInvBillDao billDao;
  @Autowired
  private BinService binService;
  @Autowired
  private ArticleService articleService;
  @Autowired
  private ContainerService containerService;
  @Autowired
  private StockService stockService;
  @Autowired
  private UserService userService;
  @Autowired
  private EntityLogger logger;

  @Override
  public DecIncInvBill get(String billUuid) {
    if (StringUtil.isNullOrBlank(billUuid))
      return null;
    DecIncInvBill decIncInvBill = billDao.get(billUuid);
    if (decIncInvBill == null)
      return null;
    decIncInvBill.setItems(billDao.queryItems(billUuid));
    return decIncInvBill;
  }

  @Override
  public DecIncInvBill getByBillNumber(String billNumber) {
    if (StringUtil.isNullOrBlank(billNumber))
      return null;
    DecIncInvBill decIncInvBill = billDao.getByBillNumber(billNumber);
    if (decIncInvBill == null)
      return null;
    decIncInvBill.setItems(billDao.queryItems(decIncInvBill.getUuid()));
    return decIncInvBill;
  }

  @Override
  public PageQueryResult<DecIncInvBill> query(PageQueryDefinition definition) {
    Assert.assertArgumentNotNull(definition, "definition");

    PageQueryResult<DecIncInvBill> pgr = new PageQueryResult<DecIncInvBill>();
    List<DecIncInvBill> list = billDao.query(definition);
    PageQueryUtil.assignPageInfo(pgr, definition);
    pgr.setRecords(list);
    return pgr;
  }

  @Override
  public String saveNew(DecIncInvBill bill) throws WMSException {
    Assert.assertArgumentNotNull(bill, "bill");
    bill.setState(DecIncInvBillState.Initial);
    bill.validate();
    verifyDecIncInvBill(bill);

    bill.setUuid(UUIDGenerator.genUUID());
    bill.setBillNumber(
        billNumberGenerator.allocateNextBillNumber(DecIncInvBill.class.getSimpleName()));
    bill.setCreateInfo(OperateInfo.newInstance(ApplicationContextUtil.getOperateContext()));
    bill.setLastModifyInfo(OperateInfo.newInstance(ApplicationContextUtil.getOperateContext()));

    for (DecIncInvBillItem item : bill.getItems()) {
      item.setUuid(UUIDGenerator.genUUID());
      item.setDecIncInvBillUuid(bill.getUuid());
      item.setStockBatch(stockBatchUtils.genStockBatch());
    }
    billDao.insert(bill);
    billDao.insertItems(bill.getItems());

    logger.injectContext(this, bill.getUuid(), DecIncInvBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_ADDNEW, "新建损溢单");

    return bill.getUuid();
  }

  @Override
  public void saveModify(DecIncInvBill bill) throws WMSException {
    Assert.assertArgumentNotNull(bill, "bill");
    bill.validate();
    verifyDecIncInvBill(bill);

    bill.setLastModifyInfo(OperateInfo.newInstance(ApplicationContextUtil.getOperateContext()));
    for (DecIncInvBillItem item : bill.getItems()) {
      item.setUuid(UUIDGenerator.genUUID());
      item.setDecIncInvBillUuid(bill.getUuid());
      item.setStockBatch(stockBatchUtils.genStockBatch());
    }
    billDao.update(bill);
    billDao.removeItems(bill.getUuid());
    billDao.insertItems(bill.getItems());

    logger.injectContext(this, bill.getUuid(), DecIncInvBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "修改损溢单");
  }

  @Override
  public void remove(String billUuid, long version) throws WMSException {
    Assert.assertArgumentNotNull(billUuid, "billUuid");
    Assert.assertArgumentNotNull(version, "version");

    DecIncInvBill decIncInvBill = billDao.get(billUuid);
    if (decIncInvBill == null)
      throw new WMSException("损益单不存在");
    PersistenceUtils.checkVersion(version, decIncInvBill, "损溢单", decIncInvBill.getBillNumber());

    if (decIncInvBill.getState().equals(DecIncInvBillState.Initial) == false)
      throw new WMSException("损溢单" + decIncInvBill.getBillNumber() + "状态不是初始，不能删除！");

    billDao.remove(billUuid, version);
    billDao.removeItems(billUuid);
  }

  @Override
  public void audit(String billUuid, long version) throws WMSException {
    Assert.assertArgumentNotNull(billUuid, "billUuid");
    Assert.assertArgumentNotNull(version, "version");

    DecIncInvBill decIncInvBill = get(billUuid);
    if (decIncInvBill == null)
      throw new WMSException("损益单不存在");
    if (DecIncInvBillState.Audited.equals(decIncInvBill.getState()))
      return;
    PersistenceUtils.checkVersion(version, decIncInvBill, "损溢单", decIncInvBill.getBillNumber());
    verifyDecIncInvBill(decIncInvBill);

    if (DecIncInvBillType.Dec.equals(decIncInvBill.getType()))
      decInvStocks(decIncInvBill);
    else
      incInvStocks(decIncInvBill);

    decIncInvBill.setState(DecIncInvBillState.Audited);
    decIncInvBill
        .setLastModifyInfo(OperateInfo.newInstance(ApplicationContextUtil.getOperateContext()));
    billDao.update(decIncInvBill);

    logger.injectContext(this, billUuid, DecIncInvBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "审核损溢单");
  }

  private void verifyDecIncInvBill(DecIncInvBill bill) throws WMSException {
    bill.validate();
    Wrh wrh = binService.getWrh(bill.getWrh().getUuid());
    if (wrh == null)
      throw new WMSException("仓位" + bill.getWrh().getCode() + "不存在");

    User user = userService.getByCode(bill.getOperator().getCode());
    if (Objects.isNull(user))
      throw new WMSException("操作人" + bill.getOperator().getCode() + "不存在");
    bill.setOperator(new UCN(user.getUuid(), user.getCode(), user.getName()));

    StringBuffer errorMessage = new StringBuffer();
    Map<String, Set<String>> map = new HashMap<>();
    for (int i = 0; i < bill.getItems().size(); i++) {
      DecIncInvBillItem item = bill.getItems().get(i);
      item.setLine(i + 1);
      if (DecIncInvBillType.Dec.equals(bill.getType()))
        verifyDecItem(item, wrh.getUuid(), errorMessage);
      else
        verifyIncItem(item, wrh.getUuid(), errorMessage);
      if (StringUtil.isNullOrBlank(item.getContainerBarCode()) == false
          && Container.VIRTUALITY_CONTAINER.equals(item.getContainerBarCode()) == false) {
        if (map.containsKey(item.getContainerBarCode()))
          map.get(item.getContainerBarCode()).add(item.getBinCode());
        else {
          Set<String> bins = new HashSet<>();
          bins.add(item.getBinCode());
          map.put(item.getContainerBarCode(), bins);
        }
      }
    }

    for (String key : map.keySet()) {
      Set<String> bins = map.get(key);
      if (bins.size() > 1)
        errorMessage.append("容器" + key + "只能放在同一货位上");
    }

    if (errorMessage.length() > 0)
      throw new WMSException(errorMessage.toString());

  }

  private void verifyDecItem(DecIncInvBillItem item, String wrhUuid, StringBuffer errorMessage) {
    Bin bin = binService.getBinByCode(item.getBinCode());
    if (bin == null)
      errorMessage.append("第" + item.getLine() + "行明细，货位" + item.getBinCode() + "不存在");
    else if (ObjectUtils.notEqual(bin.getWrh().getUuid(), wrhUuid))
      errorMessage.append("第" + item.getLine() + "行明细，货位" + item.getBinCode() + "所属仓位与损耗单仓位不一致");
    List<Stock> stocks = getStocks(item);
    if (CollectionUtils.isEmpty(stocks))
      errorMessage.append("第" + item.getLine() + "行明细，没有对应的库存");

    BigDecimal stockQty = BigDecimal.ZERO;
    for (Stock stock : stocks)
      stockQty = stockQty.add(stock.getStockComponent().getQty());
    if (item.getQty().abs().compareTo(stockQty) > 0)
      errorMessage.append("第" + item.getLine() + "行明细，损耗数量不能大于库存数量" + stockQty);
  }

  private void verifyIncItem(DecIncInvBillItem item, String wrhUuid, StringBuffer errorMessage) {
    Bin bin = binService.getBinByCode(item.getBinCode());
    if (bin == null)
      errorMessage.append("第" + item.getLine() + "行明细，货位" + item.getBinCode() + "不存在");
    else if (ObjectUtils.notEqual(bin.getWrh().getUuid(), wrhUuid))
      errorMessage.append("第" + item.getLine() + "行明细，货位" + item.getBinCode() + "所属仓位与损耗单仓位不一致");
    if (StringUtil.isNullOrBlank(item.getContainerBarCode()) == false
        && Container.VIRTUALITY_CONTAINER.equals(item.getContainerBarCode()) == false) {
      Container container = containerService.getByBarcode(item.getContainerBarCode());
      if (container == null)
        errorMessage.append("第" + item.getLine() + "行明细，容器" + item.getContainerBarCode() + "不存在");
      else if (Container.VIRTUALITY_CONTAINER.equals(item.getContainerBarCode()) == false
          && ContainerState.STACONTAINERIDLE.equals(container.getState()) == false
          && ObjectUtils.notEqual(item.getBinCode(), container.getPosition()))
        errorMessage.append("第" + item.getLine() + "行明细，容器" + item.getContainerBarCode() + "已经在"
            + container.getPosition() + "位上");
    }

    Article article = articleService.get(item.getArticle().getUuid());
    if (article == null)
      errorMessage.append("第" + item.getLine() + "行明细，商品" + item.getArticle().getCode() + "不存在");

  }

  private List<Stock> getStocks(DecIncInvBillItem item) {
    StockFilter stockFilter = new StockFilter();
    stockFilter.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    stockFilter.setBinCode(item.getBinCode());
    stockFilter.setContainerBarcode(item.getContainerBarCode());
    stockFilter.setArticleUuid(item.getArticle().getUuid());
    stockFilter.setSupplierUuid(item.getSupplier().getUuid());
    stockFilter.setQpcStr(item.getQpcStr());
    stockFilter.setStockBatch(item.getStockBatch());
    stockFilter.setPage(1);
    List<Stock> stocks = stockService.query(stockFilter);
    return stocks;
  }

  private void decInvStocks(DecIncInvBill decIncInvBill) throws WMSException {
    List<StockShiftRule> shiftOutRules = new ArrayList<>();
    Set<String> containerBarCodes = new HashSet<>();
    Set<String> binCodes = new HashSet<>();
    for (DecIncInvBillItem item : decIncInvBill.getItems()) {
      StockShiftRule rule = new StockShiftRule();
      rule.setBinCode(item.getBinCode());
      rule.setContainerBarcode(item.getContainerBarCode());
      rule.setArticleUuid(item.getArticle().getUuid());
      rule.setSupplierUuid(item.getSupplier().getUuid());
      rule.setQty(item.getQty().abs());
      rule.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      rule.setStockBatch(item.getStockBatch());
      rule.setQpcStr(item.getQpcStr());
      rule.setOwner(ApplicationContextUtil.getCompanyUuid());
      shiftOutRules.add(rule);
      if (StringUtil.isNullOrBlank(item.getContainerBarCode()) == false
          && Container.VIRTUALITY_CONTAINER.equals(item.getContainerBarCode()) == false)
        containerBarCodes.add(item.getContainerBarCode());
      binCodes.add(item.getBinCode());
    }

    stockService.shiftOut(new SourceBill(DecIncInvBill.CAPTION, decIncInvBill.getUuid(),
        decIncInvBill.getBillNumber()), shiftOutRules);

    for (String containerBarCode : containerBarCodes) {
      StockFilter stockFilter = new StockFilter();
      stockFilter.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      stockFilter.setContainerBarcode(containerBarCode);
      stockFilter.setPageSize(0);
      List<Stock> stocks = stockService.query(stockFilter);
      if (CollectionUtils.isEmpty(stocks)) {
        Container container = containerService.getByBarcode(containerBarCode);
        containerService.change(container.getUuid(), container.getVersion(),
            ContainerState.STACONTAINERIDLE, Container.UNKNOWN_POSITION);
      }
    }

    for (String binCode : binCodes) {
      StockFilter stockFilter = new StockFilter();
      stockFilter.setPageSize(0);
      stockFilter.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      stockFilter.setBinCode(binCode);
      List<Stock> stocks = stockService.query(stockFilter);
      if (CollectionUtils.isEmpty(stocks)) {
        Bin bin = binService.getBinByCode(binCode);
        binService.changeState(bin.getUuid(), bin.getVersion(), BinState.free);
      }

    }

  }

  private void incInvStocks(DecIncInvBill decIncInvBill) throws WMSException {
    List<StockShiftIn> shiftIns = new ArrayList<>();
    Map<String, String> containerBinMap = new HashMap<>();
    Set<String> binCodes = new HashSet<>();

    for (DecIncInvBillItem item : decIncInvBill.getItems()) {
      StockShiftIn shiftIn = new StockShiftIn();
      StockComponent component = new StockComponent();
      component.setArticle(item.getArticle());
      component.setArticleSpec(item.getArticleSpec());
      component.setBinCode(item.getBinCode());
      component.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      component.setContainerBarcode(item.getContainerBarCode());
      component.setMunit(item.getMeasureUnit());
      component.setPrice(item.getPrice());
      component.setProductionBatch(stockBatchUtils.genProductionBatch(item.getProductionDate()));
      component.setProductionDate(item.getProductionDate());
      component.setQpcStr(item.getQpcStr());
      component.setQty(item.getQty());
      component.setSourceBill(new SourceBill(DecIncInvBill.CAPTION, decIncInvBill.getUuid(),
          decIncInvBill.getBillNumber()));
      component.setStockBatch(item.getStockBatch());
      component.setSupplier(item.getSupplier());
      component.setValidDate(item.getExpireDate());
      component.setProductionDate(item.getProductionDate());
      shiftIn.setStockComponent(component);
      shiftIn.setSourceLineNumber(item.getLine());
      shiftIn.setSourceLineUuid(item.getUuid());
      shiftIns.add(shiftIn);

      if (StringUtil.isNullOrBlank(item.getContainerBarCode()) == false
          && Container.VIRTUALITY_CONTAINER.equals(item.getContainerBarCode()) == false)
        containerBinMap.put(item.getContainerBarCode(), item.getBinCode());
      binCodes.add(item.getBinCode());
    }
    stockService.shiftIn(new SourceBill(DecIncInvBill.CAPTION, decIncInvBill.getUuid(),
        decIncInvBill.getBillNumber()), shiftIns);

    for (String containerBarCode : containerBinMap.keySet()) {
      Container container = containerService.getByBarcode(containerBarCode);
      if (ContainerState.STACONTAINERIDLE.equals(container.getState())) {
        containerService.change(container.getUuid(), container.getVersion(),
            ContainerState.STACONTAINERUSEING, containerBinMap.get(containerBarCode));
      }
    }

    for (String binCode : binCodes) {
      Bin bin = binService.getBinByCode(binCode);
      if (BinState.free.equals(bin.getState())) {
        binService.changeState(bin.getUuid(), bin.getVersion(), BinState.using);
      }
    }
  }

}
