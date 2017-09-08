/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	RtnSupplierNtcBillDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月16日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.dao.rtn.rtnsupplierntc.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.rtn.rtnsupplierntc.RtnSupplierNtcBill;
import com.hd123.sardine.wms.api.rtn.rtnsupplierntc.RtnSupplierNtcBillItem;
import com.hd123.sardine.wms.api.stock.Stock;
import com.hd123.sardine.wms.common.dao.impl.BaseDaoImpl;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.dao.rtn.rtnsupplierntc.RtnSupplierNtcBillDao;

/**
 * @author yangwenzhu
 *
 */
public class RtnSupplierNtcBillDaoImpl extends BaseDaoImpl<RtnSupplierNtcBill>
    implements RtnSupplierNtcBillDao {

  private static final String MAPPER_INSERT_ITEMS = "insert_items";
  private static final String MAPPER_REMOVE_ITEMS = "remove_items";
  private static final String MAPPER_QUERY_ITEMS = "query_items";
  private static final String MAPPER_GET_ITEM = "get_item";
  private static final String UPDATEITEM = "updateItem";
  private static final String GETBYBILLNUMBER = "getByBillNumber";
  private static final String QUERYWAITUNSHELVESTOCKS = "queryWaitUnShelveStocks";

  @Override
  public void insertItems(List<RtnSupplierNtcBillItem> items) {
    Assert.assertArgumentNotNull(items, "items");
    for (RtnSupplierNtcBillItem item : items) {
      getSqlSession().insert(generateStatement(MAPPER_INSERT_ITEMS), item);
    }
  }

  @Override
  public void removeItmes(String billUuid) {
    Assert.assertArgumentNotNull(billUuid, "billUuid");
    getSqlSession().delete(generateStatement(MAPPER_REMOVE_ITEMS), billUuid);
  }

  @Override
  public List<RtnSupplierNtcBillItem> queryItems(String uuid) {
    if (StringUtil.isNullOrBlank(uuid))
      return new ArrayList<>();
    return getSqlSession().selectList(generateStatement(MAPPER_QUERY_ITEMS), uuid);
  }

  @Override
  public RtnSupplierNtcBillItem getItem(String uuid) {
    if (StringUtil.isNullOrBlank(uuid))
      return null;
    return getSqlSession().selectOne(generateStatement(MAPPER_GET_ITEM), uuid);
  }

  @Override
  public RtnSupplierNtcBill getByBillNumber(String billNumber) {
    if (StringUtil.isNullOrBlank(billNumber))
      return null;

    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("billNumber", billNumber);
    return selectOne(GETBYBILLNUMBER, map);
  }

  @Override
  public void updateItem(RtnSupplierNtcBillItem item) {
    Assert.assertArgumentNotNull(item, "item");

    update(UPDATEITEM, item);
  }

  @Override
  public List<Stock> queryWaitUnShelveStocks(String wrhUuid, String articleUuid,
      String supplierUuid) {
    if (StringUtil.isNullOrBlank(supplierUuid) || StringUtil.isNullOrBlank(articleUuid)
        || StringUtil.isNullOrBlank(wrhUuid))
      return new ArrayList<Stock>();

    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("wrhUuid", wrhUuid);
    map.put("articleUuid", articleUuid);
    map.put("supplierUuid", supplierUuid);
    return selectList(QUERYWAITUNSHELVESTOCKS, map);
  }
}
