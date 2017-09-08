/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	SupplierReturnBillVerifier.java
 * 模块说明：	
 * 修改历史：
 * 2017年9月8日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.rtn.supplierreturn;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.basicInfo.supplier.Supplier;
import com.hd123.sardine.wms.api.basicInfo.supplier.SupplierService;
import com.hd123.sardine.wms.api.rtn.returnsupplier.ReturnSupplierBillItem;
import com.hd123.sardine.wms.api.stock.Stock;
import com.hd123.sardine.wms.api.stock.StockFilter;
import com.hd123.sardine.wms.api.stock.StockService;
import com.hd123.sardine.wms.api.stock.StockState;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.service.util.StockBatchUtils;

/**
 * @author zhangsai
 *
 */
public class SupplierReturnBillVerifier {

  @Autowired
  private SupplierService supplierService;

  @Autowired
  private StockService stockService;

  @Autowired
  private StockBatchUtils stockBatchUtils;

  public void verifyHandOverItems(List<ReturnSupplierBillItem> items) throws WMSException {
    Assert.assertArgumentNotNull(items, "items");

    StockFilter stockFilter = new StockFilter();
    stockFilter.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    stockFilter.setPageSize(0);
    for (ReturnSupplierBillItem item : items) {
      item.validate();
      Supplier supplier = supplierService.get(item.getSupplier().getUuid());
      if (supplier == null)
        throw new WMSException("供应商" + item.getSupplier().getCode() + "不存在！");

      stockFilter.setArticleUuid(item.getArticle().getUuid());
      stockFilter.setStockBatch(item.getStockBatch());
      stockFilter.setBinCode(item.getBinCode());
      stockFilter.setContainerBarcode(item.getContainerBarcode());
      stockFilter.setState(StockState.normal);
      stockFilter.setQpcStr(item.getQpcStr());
      stockFilter.setProductionBatch(stockBatchUtils.genProductionBatch(item.getProductionDate()));
      List<Stock> stocks = stockService.query(stockFilter);

      BigDecimal totalQty = caculateTotalQty(stocks);
      if (totalQty.compareTo(item.getQty()) < 0)
        throw new WMSException("商品" + item.getArticle().getCode() + "退货交接的数量大于库存数量！");
      item.setArticle(stocks.get(0).getStockComponent().getArticle());
      item.setSpec(stocks.get(0).getStockComponent().getArticleSpec());
      item.setMunit(stocks.get(0).getStockComponent().getMunit());
      item.setReturnSupplierDate(new Date());
    }
  }

  private BigDecimal caculateTotalQty(List<Stock> stocks) {
    if (CollectionUtils.isEmpty(stocks))
      return BigDecimal.ZERO;

    BigDecimal totalQty = BigDecimal.ZERO;
    for (Stock stock : stocks) {
      totalQty = totalQty.add(stock.getStockComponent().getQty());
    }
    return totalQty;
  }
}
