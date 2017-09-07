/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	ShipBillVerifier.java
 * 模块说明：	
 * 修改历史：
 * 2017年9月6日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.tms.shipbill;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.stock.Stock;
import com.hd123.sardine.wms.api.stock.StockFilter;
import com.hd123.sardine.wms.api.stock.StockService;
import com.hd123.sardine.wms.api.stock.StockState;
import com.hd123.sardine.wms.api.tms.shipbill.ShipBill;
import com.hd123.sardine.wms.api.tms.shipbill.ShipBillContainerStock;
import com.hd123.sardine.wms.api.tms.vehicle.Vehicle;
import com.hd123.sardine.wms.api.tms.vehicle.VehicleService;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.QpcHelper;
import com.hd123.sardine.wms.service.util.StockBatchUtils;

/**
 * @author zhangsai
 *
 */
public class ShipBillVerifier {

  @Autowired
  private VehicleService vehicleService;

  @Autowired
  private StockService stockService;

  @Autowired
  private StockBatchUtils stockBatchUtils;

  /**
   * 校验装车单是否合法
   * 
   * @param shipBill
   *          装车单， not null
   * @throws WMSException
   *           校验失败
   */
  public void verifyShipBill(ShipBill shipBill) throws WMSException {
    Assert.assertArgumentNotNull(shipBill, "shipBill");

    shipBill.validate();

    Vehicle vehicle = vehicleService.getByVehicleNo(shipBill.getVehicleNum());
    if (vehicle == null)
      throw new WMSException("车辆" + shipBill.getVehicleNum() + "不存在！");
    shipBill.setCarrier(vehicle.getCarrier());
    StockFilter stockFilter = new StockFilter();
    stockFilter.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    stockFilter.setPageSize(0);
    for (ShipBillContainerStock containerStock : shipBill.getContainerStocks()) {
      stockFilter.setArticleUuid(containerStock.getArticle().getUuid());
      stockFilter.setBinCode(containerStock.getBinCode());
      stockFilter.setContainerBarcode(containerStock.getContainerBarcode());
      stockFilter.setOwner(containerStock.getCustomer().getUuid());
      stockFilter.setProductionBatch(
          stockBatchUtils.genProductionBatch(containerStock.getProductionDate()));
      stockFilter.setQpcStr(containerStock.getQpcStr());
      stockFilter.setState(StockState.normal);
      stockFilter.setStockBatch(containerStock.getStockBatch());
      stockFilter.setSupplierUuid(containerStock.getSupplier().getUuid());
      List<Stock> stocks = stockService.query(stockFilter);
      BigDecimal totalQty = caculateTotalQty(stocks);
      if (totalQty.compareTo(containerStock.getQty()) < 0)
        throw new WMSException("货位" + containerStock.getBinCode() + "，容器"
            + containerStock.getContainerBarcode() + "库存不足，请重试！");
      containerStock.setPrice(stocks.get(0).getStockComponent().getPrice());
      containerStock.setCaseQtyStr(
          QpcHelper.qtyToCaseQtyStr(containerStock.getQty(), containerStock.getQpcStr()));
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
