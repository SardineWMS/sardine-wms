/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-core
 * 文件名：	StockServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2014-4-10 - WUJING - 创建。
 */
package com.hd123.sardine.wms.service.stock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.stock.Stock;
import com.hd123.sardine.wms.api.stock.StockChangeDirection;
import com.hd123.sardine.wms.api.stock.StockChangement;
import com.hd123.sardine.wms.api.stock.StockExtendInfo;
import com.hd123.sardine.wms.api.stock.StockFilter;
import com.hd123.sardine.wms.api.stock.StockMajorFilter;
import com.hd123.sardine.wms.api.stock.StockMajorInfo;
import com.hd123.sardine.wms.api.stock.StockService;
import com.hd123.sardine.wms.api.stock.StockShiftIn;
import com.hd123.sardine.wms.api.stock.StockShiftRule;
import com.hd123.sardine.wms.api.stock.StockShiftTarget;
import com.hd123.sardine.wms.api.stock.StockState;
import com.hd123.sardine.wms.common.entity.SourceBill;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.dao.stock.StockDao;
import com.hd123.sardine.wms.dao.stock.StockOperLog;

/**
 * @author WUJING
 * 
 */
public class StockServiceImpl implements StockService {
  @Autowired
  private StockDao stockDao;

  @Override
  public void shiftIn(SourceBill sourceBill, List<StockShiftIn> stocks) throws WMSException {
    Assert.assertArgumentNotNull(stocks, "stocks");
    Assert.assertArgumentNotNull(sourceBill, "sourceBill");

    validateShiftIn(stocks);

    StockFilter stockFilter = new StockFilter();
    stockFilter.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    for (StockShiftIn shiftIn : stocks) {
      stockFilter.setPageSize(0);
      stockFilter.setArticleUuid(shiftIn.getStockComponent().getArticleUuid());
      stockFilter.setBinCode(shiftIn.getStockComponent().getBinCode());
      stockFilter.setContainerBarcode(shiftIn.getStockComponent().getContainerBarcode());
      stockFilter.setOperateBillUuid(shiftIn.getStockComponent().getOperateBill() == null ? null
          : shiftIn.getStockComponent().getOperateBill().getBillUuid());
      stockFilter.setOwner(shiftIn.getStockComponent().getOwner());
      stockFilter.setProductionBatch(shiftIn.getStockComponent().getProductionBatch());
      stockFilter.setQpcStr(shiftIn.getStockComponent().getQpcStr());
      stockFilter.setSourceBillUuid(shiftIn.getStockComponent().getSourceBill().getBillUuid());
      stockFilter.setState(shiftIn.getStockComponent().getState());
      stockFilter.setStockBatch(shiftIn.getStockComponent().getStockBatch());
      stockFilter.setSupplierUuid(shiftIn.getStockComponent().getSupplierUuid());
      List<Stock> existsStocks = stockDao.queryStocks(stockFilter);

      StockOperLog log = null;
      if (existsStocks.isEmpty()) {
        Stock stock = new Stock();
        stock.setUuid(UUIDGenerator.genUUID());
        stock.setStockComponent(shiftIn.getStockComponent());
        stock.setModified(new Date());
        stockDao.insertStock(stock);

        log = createLog(stock);
        log.setOperateBill(sourceBill);
        log.setBeforeQty(BigDecimal.ZERO);
        log.setAfterQty(log.getQty());
      } else {
        Stock existsStock = existsStocks.get(0);
        stockDao.updateStock(existsStock.getUuid(), existsStock.getVersion(),
            shiftIn.getStockComponent().getQty(), new Date());

        log = createLog(existsStock);
        log.setOperateBill(sourceBill);
        log.setBeforeQty(existsStock.getStockComponent().getQty());
        log.setAfterQty(existsStock.getStockComponent().getQty().add(log.getQty()));
      }
      stockDao.insertStockLog(log);
    }
  }

  private StockOperLog createLog(Stock stock) {
    assert stock != null;

    StockOperLog log = new StockOperLog();
    log.setUuid(UUIDGenerator.genUUID());
    log.setArticleUuid(stock.getStockComponent().getArticleUuid());
    log.setBinCode(stock.getStockComponent().getBinCode());
    log.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    log.setContainerBarcode(stock.getStockComponent().getContainerBarcode());
    log.setLogTime(new Date());
    log.setOperateBill(stock.getStockComponent().getOperateBill());
    log.setOwner(stock.getStockComponent().getOwner());
    log.setQpcStr(stock.getStockComponent().getOwner());
    log.setQty(stock.getStockComponent().getQty());
    log.setState(stock.getStockComponent().getState());
    log.setProductionBatch(stock.getStockComponent().getProductionBatch());
    log.setStockBatch(stock.getStockComponent().getStockBatch());
    log.setStockUuid(stock.getUuid());
    log.setSupplierUuid(stock.getStockComponent().getSupplierUuid());

    return log;
  }

  private void validateShiftIn(List<StockShiftIn> stocks) {
    for (StockShiftIn stock : stocks) {
      stock.validate();
    }
  }

  private void validateShiftOut(List<StockShiftRule> rules) throws WMSException {
    for (StockShiftRule rule : rules) {
      rule.validate();
    }
  }

  @Override
  public List<StockChangement> shiftOut(SourceBill sourceBill, List<StockShiftRule> rules)
      throws WMSException {
    Assert.assertArgumentNotNull(rules, "rules");
    Assert.assertArgumentNotNull(sourceBill, "sourceBill");

    validateShiftOut(rules);

    List<StockChangement> result = new ArrayList<StockChangement>();
    StockFilter stockFilter = new StockFilter();
    stockFilter.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    stockFilter.setPageSize(0);
    for (StockShiftRule shiftRule : rules) {
      stockFilter.setArticleUuid(shiftRule.getArticleUuid());
      stockFilter.setBinCode(shiftRule.getBinCode());
      stockFilter.setContainerBarcode(shiftRule.getContainerBarcode());
      stockFilter.setOperateBillUuid(shiftRule.getOperateBillUuid());
      stockFilter.setOwner(shiftRule.getOwner());
      stockFilter.setProductionBatch(shiftRule.getProductionBatch());
      stockFilter.setQpcStr(shiftRule.getQpcStr());
      stockFilter.setSourceBillUuid(shiftRule.getSourceBillUuid());
      stockFilter.setState(shiftRule.getState());
      stockFilter.setStockBatch(shiftRule.getStockBatch());
      stockFilter.setSupplierUuid(shiftRule.getSupplierUuid());
      stockFilter.setStockUuid(shiftRule.getStockUuid());
      List<Stock> existsStocks = stockDao.queryStocks(stockFilter);
      BigDecimal shiftOutQty = shiftRule.getQty();
      for (Stock stock : existsStocks) {
        StockChangement changement = new StockChangement();
        changement.setDirection(StockChangeDirection.shiftOut);
        changement.setStockComponent(stock.getStockComponent());
        changement.setStockUuid(stock.getUuid());
        result.add(changement);

        StockOperLog log = createLog(stock);
        log.setBeforeQty(stock.getStockComponent().getQty());
        log.setOperateBill(sourceBill);

        if (stock.getStockComponent().getQty().compareTo(shiftOutQty) > 0) {
          stockDao.updateStock(stock.getUuid(), stock.getVersion(), shiftOutQty.negate(),
              new Date());

          changement.getStockComponent().setQty(shiftOutQty);

          log.setQty(shiftOutQty.negate());
          log.setAfterQty(log.getBeforeQty().subtract(shiftOutQty));

          shiftOutQty = BigDecimal.ZERO;
          break;
        } else {
          stockDao.removeStock(stock.getUuid(), stock.getVersion());

          log.setQty(stock.getStockComponent().getQty().negate());
          log.setAfterQty(BigDecimal.ZERO);

          shiftOutQty = shiftOutQty.subtract(stock.getStockComponent().getQty());
        }
        stockDao.insertStockLog(log);
      }
      if (shiftOutQty.compareTo(BigDecimal.ZERO) > 0)
        throw new WMSException("商品" + shiftRule.getArticleUuid() + "库存不足，缺少" + shiftOutQty + "个！");
    }

    return result;
  }

  @Override
  public List<StockChangement> shift(SourceBill sourceBill, List<StockShiftRule> rules,
      StockShiftTarget target) throws WMSException {
    Assert.assertArgumentNotNull(sourceBill, "sourceBill");
    Assert.assertArgumentNotNull(rules, "rules");
    Assert.assertArgumentNotNull(target, "target");
    target.validate();

    validateShiftOut(rules);

    List<StockChangement> changements = shiftOut(sourceBill, rules);

    List<StockShiftIn> shiftIns = new ArrayList<StockShiftIn>();
    for (StockChangement changement : changements) {
      StockShiftIn shiftIn = new StockShiftIn();
      shiftIn.setStockComponent(changement.getStockComponent());
      shiftIn.getStockComponent().setState(target.getState());
      shiftIn.getStockComponent().setBinCode(target.getBinCode());
      shiftIn.getStockComponent().setContainerBarcode(target.getContainerBarCode());
      shiftIns.add(shiftIn);
    }
    shiftIn(sourceBill, shiftIns);

    return changements;
  }

  @Override
  public List<StockChangement> changeState(SourceBill sourceBill, List<StockShiftRule> rules,
      StockState fromState, StockState toState) throws WMSException {
    Assert.assertArgumentNotNull(rules, "rules");
    Assert.assertArgumentNotNull(fromState, "fromState");
    Assert.assertArgumentNotNull(toState, "toState");
    Assert.assertArgumentNotNull(sourceBill, "sourceBill");

    validateShiftOut(rules);

    for (StockShiftRule rule : rules)
      rule.setState(fromState);

    List<StockChangement> changements = shiftOut(sourceBill, rules);

    List<StockShiftIn> shiftIns = new ArrayList<StockShiftIn>();
    for (StockChangement changement : changements) {
      StockShiftIn shiftIn = new StockShiftIn();
      shiftIn.setStockComponent(changement.getStockComponent());
      shiftIn.getStockComponent().setState(toState);
      shiftIns.add(shiftIn);
    }
    shiftIn(sourceBill, shiftIns);

    return changements;
  }

  @Override
  public List<Stock> query(StockFilter filter) {
    Assert.assertArgumentNotNull(filter, "filter");

    List<Stock> result = new ArrayList<Stock>();
    List<Stock> stocks = stockDao.queryStocks(filter);
    if (stocks != null && stocks.isEmpty() == false)
      result = stocks;
    return result;
  }

  @Override
  public List<StockExtendInfo> queryStockExtendInfo(StockFilter filter) {
    Assert.assertArgumentNotNull(filter, "filter");
    return stockDao.queryStockExtendInfo(filter);
  }

  @Override
  public List<StockMajorInfo> queryStockMajorInfo(StockMajorFilter filter) {
    Assert.assertArgumentNotNull(filter, "filter");
    return stockDao.queryMajorInfo(filter);
  }
}
