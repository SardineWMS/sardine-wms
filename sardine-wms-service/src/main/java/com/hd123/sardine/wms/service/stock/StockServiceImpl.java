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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.stock.Stock;
import com.hd123.sardine.wms.api.stock.StockChangement;
import com.hd123.sardine.wms.api.stock.StockComponent;
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
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.dao.stock.PStockShiftIn;
import com.hd123.sardine.wms.dao.stock.PStockShiftRule;
import com.hd123.sardine.wms.dao.stock.StockCmd;
import com.hd123.sardine.wms.dao.stock.StockDao;
import com.hd123.sardine.wms.dao.stock.StockShiftMessage;

/**
 * @author WUJING
 * 
 */
public class StockServiceImpl implements StockService {
  private static final Logger logger = LoggerFactory.getLogger(StockService.class);

  @Autowired
  private StockDao stockDao;

  @Override
  public void shiftIn(SourceBill sourceBill, List<StockShiftIn> stocks) throws WMSException {
    Assert.assertArgumentNotNull(stocks, "stocks");
    Assert.assertArgumentNotNull(sourceBill, "sourceBill");

    validateShiftIn(stocks);

    String workId = UUIDGenerator.genUUID();

    insertCmdRecords(workId, sourceBill);

    insertRecords(stocks, sourceBill, workId);

    stockDao.executeShiftIn(workId);

    assertError(workId);

    clean(workId);
  }

  private void assertError(String workId) throws WMSException {
    StringBuilder errorMessage = new StringBuilder();

    List<StockShiftMessage> list = stockDao.getStockMessage(workId);
    if (list != null && list.isEmpty() == false) {
      for (StockShiftMessage stockMessage : list) {
        if (Integer.valueOf(stockMessage.getSuccess()) == 1) {
          errorMessage = errorMessage.append(
              "库存业务失败，存在" + stockMessage.getLineNumber() + "行商品" + stockMessage.getMessage() + "。");
        }
      }
    }

    if (errorMessage.length() > 0)
      throw new WMSException(errorMessage.toString());
  }

  private void insertRecords(List<StockShiftIn> stocks, SourceBill sourceBill, String workId) {
    for (int i = 0; i < stocks.size(); i++) {
      StockShiftIn sIn = stocks.get(i);

      PStockShiftIn stockIn = new PStockShiftIn();
      stockIn.setSourceLineUuid(sIn.getSourceLineUuid());
      stockIn.setSourceLineNumber(sIn.getSourceLineNumber());
      stockIn.setWorkId(workId);

      assert sIn.getStockComponent() != null;

      stockIn.setOwner(sIn.getStockComponent().getOwner());
      stockIn.setCompanyUuid(sIn.getStockComponent().getCompanyUuid());
      stockIn.setSupplierUuid(sIn.getStockComponent().getSupplierUuid());
      stockIn.setBinCode(sIn.getStockComponent().getBinCode());
      stockIn.setContainerBarCode(sIn.getStockComponent().getContainerBarcode());
      stockIn.setArticleUuid(sIn.getStockComponent().getArticleUuid());
      stockIn.setStockBatch(sIn.getStockComponent().getStockBatch());
      stockIn.setProductionDate(sIn.getStockComponent().getProductionDate());
      stockIn.setValidDate(sIn.getStockComponent().getValidDate());

      assert sIn.getStockComponent().getSourceBill() != null;

      stockIn.setSourceBill(sIn.getStockComponent().getSourceBill());
      stockIn.setQty(sIn.getStockComponent().getQty());
      stockIn.setState(sIn.getStockComponent().getState());
      stockIn.setQpcStr(sIn.getStockComponent().getQpcStr());

      if (sIn.getStockComponent().getState().equals(StockState.normal)) {
        stockIn.setOperateBill(new SourceBill(StockComponent.DEFAULT_OPERATE_BILL,
            StockComponent.DEFAULT_OPERATE_BILL, StockComponent.DEFAULT_OPERATE_BILL));
      } else {
        stockIn.setOperateBill(sourceBill);
      }

      stockIn.setMeasureUnit(sIn.getStockComponent().getMeasureUnit());
      stockIn.setInstockTime(sIn.getStockComponent().getInstockTime());

      stockDao.saveStockShiftIn(stockIn);
    }

  }

  private void insertRuleRecords(List<StockShiftRule> rules, String workid) {
    assert rules != null;
    assert workid != null;

    for (int i = 0; i < rules.size(); i++) {
      StockShiftRule rule = rules.get(i);
      PStockShiftRule pRule = new PStockShiftRule();
      pRule.setBinCode(rule.getBinCode());
      pRule.setContainerBarcode(rule.getContainerBarcode());
      pRule.setCompanyUuid(rule.getCompanyUuid());
      pRule.setOwner(rule.getOwner());
      pRule.setArticleUuid(rule.getArticleUuid());
      pRule.setQty(rule.getQty());
      pRule.setSourceLineNumber(rule.getSourceLineNumber());
      pRule.setSourceLineUuid(rule.getSourceLineUuid());
      pRule.setState(rule.getState());
      pRule.setStockBatch(rule.getStockBatch());
      pRule.setStockUuid(rule.getStockUuid());
      pRule.setSupplierUuid(rule.getSupplierUuid());
      pRule.setWorkId(workid);
      pRule.setQpcStr(rule.getQpcStr());
      if (StockState.normal.equals(rule.getState()) == false) {
        pRule.setOperateBillUuid(rule.getOperateBillUuid());
      }
      stockDao.saveStockShiftRule(pRule);
    }
  }

  private void insertCmdRecords(String workid, SourceBill sourceBill) {
    StockCmd stockCmd = new StockCmd();
    stockCmd.setWorkId(workid);
    stockCmd.setBillUuid(sourceBill.getBillUuid());
    stockCmd.setBillNumber(sourceBill.getBillNumber());
    stockCmd.setBillType(sourceBill.getBillType());
    stockDao.saveStockCmd(stockCmd);
  }

  private void clean(String workId) {
    stockDao.executeClearProcedure(workId);
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

    String workId = UUIDGenerator.genUUID();

    insertCmdRecords(workId, sourceBill);

    insertRuleRecords(rules, workId);

    stockDao.executeShiftOut(workId);

    assertError(workId);

    List<StockChangement> result = stockDao.queryStocksChangement(workId);

    clean(workId);

    return result;
  }

  @Override
  public List<StockChangement> shift(SourceBill sourceBill, List<StockShiftRule> rules,
      StockShiftTarget target) throws WMSException {
    Assert.assertArgumentNotNull(sourceBill, "sourceBill");
    Assert.assertArgumentNotNull(rules, "rules");
    Assert.assertArgumentNotNull(target, "target");
    target.validate();

    StopWatch watch = new StopWatch("库存服务shift");
    watch.start();
    validateShiftOut(rules);

    String workId = UUIDGenerator.genUUID();

    insertCmdRecords(workId, sourceBill);

    insertRuleRecords(rules, workId);

    stockDao.executeShift(workId, target.getBinCode(), target.getContainerBarCode(),
        target.getState());

    assertError(workId);

    List<StockChangement> result = stockDao.queryStocksChangement(workId);

    clean(workId);

    watch.stop();

    logger.info(watch.prettyPrint());
    return result;
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

    String workId = UUIDGenerator.genUUID();

    insertCmdRecords(workId, sourceBill);

    insertRuleRecords(rules, workId);

    stockDao.executeChange(workId, toState.name());

    assertError(workId);

    List<StockChangement> result = stockDao.queryStocksChangement(workId);

    clean(workId);

    return result;
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
