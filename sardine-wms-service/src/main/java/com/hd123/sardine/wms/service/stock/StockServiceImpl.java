/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	StockServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月11日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.stock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.stock.OnWayStock;
import com.hd123.sardine.wms.api.stock.OnWayStockOutRule;
import com.hd123.sardine.wms.api.stock.ShiftOutRule;
import com.hd123.sardine.wms.api.stock.Stock;
import com.hd123.sardine.wms.api.stock.StockExtendInfo;
import com.hd123.sardine.wms.api.stock.StockFilter;
import com.hd123.sardine.wms.api.stock.StockLog;
import com.hd123.sardine.wms.api.stock.StockService;
import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.dao.stock.StockDao;

/**
 * 库存服务实现
 * 
 * @author zhangsai
 *
 */
public class StockServiceImpl implements StockService {

  @Autowired
  private StockDao stockDao;

  @Override
  public List<Stock> shiftIn(String sourceBillType, String sourceBillNumber,
      List<Stock> willInStocks)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(willInStocks, "willInStocks");
    Assert.assertArgumentNotNull(sourceBillNumber, "sourceBillNumber");
    Assert.assertArgumentNotNull(sourceBillType, "sourceBillType");

    if (willInStocks.isEmpty())
      return null;

    List<Stock> result = new ArrayList<Stock>();
    for (Stock stock : willInStocks) {
      stock.validate();
      Stock existsStock = stockDao.getByPrimaryKey(stock.getBinCode(), stock.getContainerBarcode(),
          stock.getStockBatch(), stock.getArticleUuid());
      if (existsStock != null) {
        if (stock.getQty().compareTo(BigDecimal.ZERO) > 0) {
          existsStock.setQty(existsStock.getQty().add(stock.getQty()));
          log(existsStock, stock.getQty(), StockLog.NORMAL_LOG, sourceBillType, sourceBillNumber);
        } else {
          for (OnWayStock onWayStock : stock.getOnWayStocks()) {
            existsStock.setOnWayQty(existsStock.getOnWayQty().add(onWayStock.getQty()));
            onWayStock.setStockUuid(existsStock.getUuid());
            onWayStock.setUuid(UUIDGenerator.genUUID());
            log(existsStock, onWayStock.getQty(), StockLog.ONWAY_LOG, sourceBillType,
                sourceBillNumber);
            stockDao.insertOnWayStock(onWayStock);
          }
        }
        existsStock.setModifyTime(new Date());
        stockDao.update(existsStock);
        result.add(existsStock);
      } else {
        stock.setUuid(UUIDGenerator.genUUID());
        stock.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
        stock.setInstockTime(new Date());
        stock.setModifyTime(new Date());
        stock.setOnWayQty(BigDecimal.ZERO);
        log(stock, stock.getQty(), StockLog.NORMAL_LOG, sourceBillType, sourceBillNumber);
        for (OnWayStock onWayStock : stock.getOnWayStocks()) {
          onWayStock.setUuid(UUIDGenerator.genUUID());
          onWayStock.setStockUuid(stock.getUuid());
          stock.setOnWayQty(stock.getOnWayQty().add(onWayStock.getQty()));
          log(stock, onWayStock.getQty(), StockLog.ONWAY_LOG, sourceBillType, sourceBillNumber);
          stockDao.insertOnWayStock(onWayStock);
        }
        stockDao.insert(stock);
        result.add(stock);
      }
    }

    return result;
  }

  private void log(Stock stock, BigDecimal qty, String logType, String sourceBillType,
      String sourceBillNumber) {
    assert stock != null;
    assert qty != null;
    assert logType != null;
    assert sourceBillType != null;
    assert sourceBillNumber != null;

    StockLog log = new StockLog();
    log.setArticleUuid(stock.getArticleUuid());
    if (logType.equals(StockLog.NORMAL_LOG))
      log.setBeforeQty(stock.getQty());
    else
      log.setBeforeQty(stock.getOnWayQty());
    log.setQty(qty);
    log.setAfterQty(log.getBeforeQty().add(qty));
    log.setBinCode(stock.getBinCode());
    log.setContainerBarcode(stock.getContainerBarcode());
    log.setLogType(logType);
    log.setOperateDate(new Date());
    log.setOperateNumber(sourceBillNumber);
    log.setOperateType(sourceBillType);
    log.setQpcStr(stock.getQpcStr());
    log.setQty(qty);
    log.setStockBatch(stock.getStockBatch());
    log.setStockUuid(stock.getUuid());
    log.setSupplierUuid(stock.getSupplierUuid());
    log.setCompanyUuid(stock.getCompanyUuid());
    stockDao.insertLog(log);
  }

  @Override
  public List<Stock> shiftInOnWayStock(List<OnWayStock> onWayStocks)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(onWayStocks, "onWayStocks");

    List<Stock> result = new ArrayList<Stock>();
    if (onWayStocks.isEmpty())
      return result;

    Set<String> stockUuids = new HashSet<String>();
    for (OnWayStock onWayStock : onWayStocks) {
      onWayStock.validate();
      Stock stock = null;
      if (StringUtil.isNullOrBlank(onWayStock.getStockUuid()) == false) {
        stock = stockDao.get(onWayStock.getStockUuid());
        if (stock == null)
          throw new WMSException("根据指定库存ID找不到对应库存！");
      } else {
        stock = stockDao.getByPrimaryKey(onWayStock.getBinCode(), onWayStock.getContainerBarcode(),
            onWayStock.getStockBatch(), onWayStock.getArticleUuid());
      }
      if (stock == null) {
        stock = new Stock();
        stock.setUuid(UUIDGenerator.genUUID());
        stock.setArticleUuid(onWayStock.getArticleUuid());
        stock.setBinCode(onWayStock.getBinCode());
        stock.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
        stock.setContainerBarcode(onWayStock.getContainerBarcode());
        stock.setInstockTime(new Date());
        stock.setMeasureUnit(null);
        stock.setModifyTime(new Date());
        stock.setOnWayQty(onWayStock.getQty());
        stock.setProductionDate(null);
        stock.setQty(BigDecimal.ZERO);
        stock.setStockBatch(onWayStock.getStockBatch());
        stockDao.insert(stock);
      } else {
        stock.setOnWayQty(stock.getOnWayQty().add(onWayStock.getQty()));
        stock.setModifyTime(new Date());
        stockDao.update(stock);
      }
      onWayStock.setStockUuid(stock.getUuid());
      onWayStock.setUuid(UUIDGenerator.genUUID());
      stockDao.insertOnWayStock(onWayStock);
      stockUuids.add(stock.getUuid());
      log(stock, onWayStock.getQty(), StockLog.ONWAY_LOG, onWayStock.getTaskType().getCaption(),
          onWayStock.getTaskNo());
    }

    for (String stockUuid : stockUuids) {
      result.add(stockDao.get(stockUuid));
    }
    return result;
  }

  @Override
  public List<StockExtendInfo> queryStocks(StockFilter stockFilter)
      throws IllegalArgumentException {
    Assert.assertArgumentNotNull(stockFilter, "stockFilter");

    return stockDao.queryStockExtendInfo(stockFilter);
  }

  @Override
  public List<Stock> shiftOut(String sourceBillType, String sourceBillNumber,
      List<ShiftOutRule> shiftOutRules)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(shiftOutRules, "shiftOutRules");
    Assert.assertArgumentNotNull(sourceBillNumber, "sourceBillNumber");
    Assert.assertArgumentNotNull(sourceBillType, "sourceBillType");

    StockFilter stockFilter = new StockFilter();
    List<Stock> result = new ArrayList<Stock>();
    for (ShiftOutRule shiftOutRule : shiftOutRules) {
      stockFilter.setArticleUuid(shiftOutRule.getArticleUuid());
      stockFilter.setBinCode(shiftOutRule.getBinCode());
      stockFilter.setCompanyUuid(shiftOutRule.getCompanyUuid());
      stockFilter.setContainerBarcode(shiftOutRule.getContainerBarcode());
      stockFilter.setQpcStr(shiftOutRule.getQpcStr());
      stockFilter.setSourceBillUuid(shiftOutRule.getSourceBillUuid());
      stockFilter.setStockBatch(shiftOutRule.getStockBatch());
      stockFilter.setStockUuid(shiftOutRule.getStockUuid());
      stockFilter.setSupplierUuid(shiftOutRule.getSupplierUuid());
      List<Stock> stocks = stockDao.query(stockFilter);
      BigDecimal shiftOutQty = shiftOutRule.getQty();
      for (Stock stock : stocks) {
        if (shiftOutQty.compareTo(BigDecimal.ZERO) <= 0)
          break;
        BigDecimal thisShiftOutQty = BigDecimal.ZERO;
        if (stock.fetchAavailableQty().compareTo(shiftOutQty) >= 0) {
          thisShiftOutQty = shiftOutQty;
          stock.setQty(stock.getQty().subtract(shiftOutQty));
          shiftOutQty = BigDecimal.ZERO;
        } else {
          thisShiftOutQty = stock.getQty();
          shiftOutQty = shiftOutQty.subtract(stock.getQty());
          stock.setQty(BigDecimal.ZERO);
        }

        if (stock.getQty().compareTo(BigDecimal.ZERO) == 0
            && stock.getOnWayQty().compareTo(BigDecimal.ZERO) == 0) {
          stockDao.remove(stock.getUuid(), stock.getVersion());
        } else {
          stock.setModifyTime(new Date());
          stockDao.update(stock);
        }
        log(stock, thisShiftOutQty, StockLog.NORMAL_LOG, sourceBillType, sourceBillNumber);
        stock.setQty(thisShiftOutQty);
        result.add(stock);
      }
      if (shiftOutQty.compareTo(BigDecimal.ZERO) > 0)
        throw new WMSException("库存不足，出库失败！");
    }
    return result;
  }

  @Override
  public List<Stock> shiftOutOnWayStock(List<OnWayStockOutRule> shiftRules)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(shiftRules, "shiftRules");

    List<Stock> result = new ArrayList<Stock>();
    if (shiftRules.isEmpty())
      return result;

    Set<String> stockUuids = new HashSet<String>();
    for (OnWayStockOutRule outRule : shiftRules) {
      Stock stock = stockDao.get(outRule.getStockUuid());
      if (stock == null)
        stock = stockDao.getByPrimaryKey(outRule.getBinCode(), outRule.getContainerBarcode(),
            outRule.getStockBatch(), outRule.getArticleUuid());
      if (stock == null)
        throw new WMSException("指令" + outRule.getTaskNo() + "找不到对应的库存！");

      OnWayStock onWayStock = stockDao.getByTaskNo(stock.getUuid(), outRule.getTaskNo());
      if (onWayStock == null)
        throw new WMSException("指定的在途库存不存在，无法出库，");
      if (onWayStock.getQty().compareTo(outRule.getQty()) <= 0)
        throw new WMSException("指定库存数量不足，出库失败！");
      onWayStock.setQty(onWayStock.getQty().subtract(outRule.getQty()));
      if (onWayStock.getQty().compareTo(BigDecimal.ZERO) == 0)
        stockDao.removeOnWayStock(onWayStock.getUuid());
      else
        stockDao.updateOnWayStock(onWayStock);
      stock.setOnWayQty(stock.getOnWayQty().subtract(outRule.getQty()));
      if (stock.getQty().compareTo(BigDecimal.ZERO) == 0
          && stock.getOnWayQty().compareTo(BigDecimal.ZERO) == 0)
        stockDao.remove(stock.getUuid(), stock.getVersion());
      else {
        stock.setModifyTime(new Date());
        stockDao.update(stock);
      }
      log(stock, outRule.getQty(), StockLog.ONWAY_LOG, outRule.getTaskType().getCaption(),
          outRule.getTaskNo());
      stockUuids.add(stock.getUuid());
    }

    for (String stockUuid : stockUuids)
      result.add(stockDao.get(stockUuid));
    return result;
  }

  @Override
  public boolean binHasStock(String binCode) {
    if (StringUtil.isNullOrBlank(binCode))
      return false;
    return stockDao.binHasStock(binCode);
  }

  @Override
  public boolean containerHasStock(String containerBarcode) {
    if (StringUtil.isNullOrBlank(containerBarcode))
      return false;
    return stockDao.containerHasStock(containerBarcode);
  }
}
