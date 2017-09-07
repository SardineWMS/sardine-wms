/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	ShipBillDao.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月16日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.dao.tms.shipbill;

import java.util.List;

import com.hd123.sardine.wms.api.tms.shipbill.ShipBill;
import com.hd123.sardine.wms.api.tms.shipbill.ShipBillContainerStock;
import com.hd123.sardine.wms.api.tms.shipbill.ShipBillCustomerItem;
import com.hd123.sardine.wms.api.tms.shipbill.ShipTaskFilter;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;

/**
 * @author fanqingqing
 *
 */
public interface ShipBillDao {
  ShipBill get(String uuid);

  ShipBill getByBillNumber(String billNumber);

  List<ShipBill> query(PageQueryDefinition definition);

  int insert(ShipBill shipBill);

  int update(ShipBill shipBill);

  void remove(String uuid, long version);

  List<ShipBillCustomerItem> queryCustomerItems(String uuid);

  void insertCustomerItems(List<ShipBillCustomerItem> items);

  void removeCustomerItems(String shipBillUuid);

  List<ShipBillContainerStock> queryContainerStockItems(String uuid);

  void insertContainerStockItems(List<ShipBillContainerStock> items);

  void removeContainerStockItems(String shipBillUuid);

  List<ShipBillContainerStock> queryWaitShipStocks(ShipTaskFilter filter);

  void updateShipOrder(String shipBillUuid);
}
