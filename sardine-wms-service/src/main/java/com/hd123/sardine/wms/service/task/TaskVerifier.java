/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	TaskVerifier.java
 * 模块说明：	
 * 修改历史：
 * 2017年7月28日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.task;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.stock.Stock;
import com.hd123.sardine.wms.api.stock.StockFilter;
import com.hd123.sardine.wms.api.stock.StockService;
import com.hd123.sardine.wms.api.stock.StockState;
import com.hd123.sardine.wms.api.task.Task;
import com.hd123.sardine.wms.api.task.TaskType;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.service.util.StockBatchUtils;

/**
 * @author zhangsai
 *
 */
public class TaskVerifier {

  @Autowired
  private StockService stockService;

  @Autowired
  private StockBatchUtils stockBatchUtils;

  public List<Stock> verifySourceStock(Task task) throws WMSException {
    Assert.assertArgumentNotNull(task, "task");

    task.validate();
    StockFilter stockFilter = new StockFilter();
    stockFilter.setPageSize(0);
    stockFilter.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    stockFilter.setArticleUuid(task.getArticle().getUuid());
    stockFilter.setContainerBarcode(task.getFromContainerBarcode());
    stockFilter.setBinCode(task.getFromBinCode());
    stockFilter.setQpcStr(task.getQpcStr());
    stockFilter.setProductionBatch(stockBatchUtils.genProductionBatch(task.getProductionDate()));
    stockFilter.setStockBatch(task.getStockBatch());
    List<Stock> stocks = stockService.query(stockFilter);

    if (CollectionUtils.isEmpty(stocks))
      throw new WMSException("指令来源库存不足！");

    BigDecimal normalStockQty = calculateStockQty(stocks);
    if (normalStockQty.compareTo(task.getQty()) < 0)
      throw new WMSException("指令来源货位库存不足！");

    if ((TaskType.Putaway.equals(task.getTaskType())
        || TaskType.RtnHandover.equals(task.getTaskType())
        || TaskType.RtnPutaway.equals(task.getTaskType())
        || TaskType.RtnShelf.equals(task.getTaskType()) || TaskType.Ship.equals(task.getTaskType()))
        && hasOtherStock(stocks))
      throw new WMSException("当前指令不允许存在非正常库存！");
    return stocks;
  }

  private BigDecimal calculateStockQty(List<Stock> stocks) {
    if (CollectionUtils.isEmpty(stocks))
      return BigDecimal.ZERO;

    BigDecimal normalStockQty = BigDecimal.ZERO;
    for (Stock stock : stocks) {
      if (stock.getStockComponent().getState().equals(StockState.normal) == false)
        continue;
      normalStockQty = normalStockQty.add(stock.getStockComponent().getQty());
    }
    return normalStockQty;
  }

  private boolean hasOtherStock(List<Stock> stocks) {
    if (CollectionUtils.isNotEmpty(stocks))
      return false;

    for (Stock stock : stocks) {
      if (stock.getStockComponent().getState().equals(StockState.normal) == false)
        return true;
    }
    return false;
  }
}
