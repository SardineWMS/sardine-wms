/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	ShipBillDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月16日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.dao.tms.shipbill.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.tms.shipbill.ShipBill;
import com.hd123.sardine.wms.api.tms.shipbill.ShipBillContainerStock;
import com.hd123.sardine.wms.api.tms.shipbill.ShipBillCustomerItem;
import com.hd123.sardine.wms.common.dao.NameSpaceSupport;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.dao.tms.shipbill.ShipBillDao;

/**
 * @author fanqingqing
 *
 */
public class ShipBillDaoImpl extends NameSpaceSupport implements ShipBillDao {
  private static final String MAPPER_GETBYBILLNUMBER = "getByBillNumber";
  private static final String MAPPER_QUERYCUSTOMERITEMS = "queryCustomerItems";
  private static final String MAPPER_INSERTCUSTOMERITEMS = "insertCustomerItems";
  private static final String MAPPER_QUERYCONTAINERSTOCKITEMS = "queryContainerStockItems";
  private static final String MAPPER_INSERTCONTAINERSTOCKITEMS = "insertContainerStockItems";
  private static final String REMOVECUSTOMERITEMS = "removeCustomerItems";
  private static final String REMOVECONTAINERSTOCKITEMS = "removeContainerStockItems";
  private static final String QUERYWAITSHIPSTOCK = "queryWaitShipStock";
  private static final String UPDATESHIPORDER = "updateShipOrder";

  @Override
  public ShipBill getByBillNumber(String billNumber) {
    if (StringUtil.isNullOrBlank(billNumber))
      return null;
    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("billNumber", billNumber);
    return getSqlSession().selectOne(generateStatement(MAPPER_GETBYBILLNUMBER), map);
  }

  @Override
  public List<ShipBillCustomerItem> queryCustomerItems(String uuid) {
    if (StringUtil.isNullOrBlank(uuid))
      return new ArrayList<>();
    return getSqlSession().selectList(generateStatement(MAPPER_QUERYCUSTOMERITEMS), uuid);
  }

  @Override
  public void insertCustomerItems(List<ShipBillCustomerItem> items) {
    Assert.assertArgumentNotNull(items, "items");
    for (ShipBillCustomerItem item : items) {
      getSqlSession().insert(generateStatement(MAPPER_INSERTCUSTOMERITEMS), item);
    }
  }

  @Override
  public List<ShipBillContainerStock> queryContainerStockItems(String uuid) {
    if (StringUtil.isNullOrBlank(uuid))
      return new ArrayList<>();
    return getSqlSession().selectList(generateStatement(MAPPER_QUERYCONTAINERSTOCKITEMS), uuid);
  }

  @Override
  public void insertContainerStockItems(List<ShipBillContainerStock> items) {
    Assert.assertArgumentNotNull(items, "items");
    for (ShipBillContainerStock item : items) {
      getSqlSession().insert(generateStatement(MAPPER_INSERTCONTAINERSTOCKITEMS), item);
    }
  }

  @Override
  public void removeCustomerItems(String shipBillUuid) {
    if (StringUtil.isNullOrBlank(shipBillUuid))
      return;

    delete(REMOVECUSTOMERITEMS, shipBillUuid);
  }

  @Override
  public void removeContainerStockItems(String shipBillUuid) {
    if (StringUtil.isNullOrBlank(shipBillUuid))
      return;

    delete(REMOVECONTAINERSTOCKITEMS, shipBillUuid);
  }

  @Override
  public ShipBill get(String uuid) {
    return selectOne(MAPPER_GET, uuid);
  }

  @Override
  public List<ShipBill> query(PageQueryDefinition param) {
    return selectList(MAPPER_QUERY_BYPAGE, param);
  }

  @Override
  public int insert(ShipBill model) {
    return insert(MAPPER_INSERT, model);
  }

  @Override
  public int update(ShipBill model) {
    int count = update(MAPPER_UPDATE, model);
    PersistenceUtils.optimisticVerify(count);
    return count;
  }

  @Override
  public void remove(String uuid, long version) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("uuid", uuid);
    map.put("version", version);
    int i = delete(MAPPER_REMOVE, map);
    PersistenceUtils.optimisticVerify(i);
  }

  @Override
  public List<ShipBillContainerStock> queryWaitShipStocks() {
    return selectList(QUERYWAITSHIPSTOCK, ApplicationContextUtil.map());
  }

  @Override
  public void updateShipOrder(String shipBillUuid) {
    if (StringUtil.isNullOrBlank(shipBillUuid))
      return;

    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("shipBillUuid", shipBillUuid);
    update(UPDATESHIPORDER, map);
  }
}
