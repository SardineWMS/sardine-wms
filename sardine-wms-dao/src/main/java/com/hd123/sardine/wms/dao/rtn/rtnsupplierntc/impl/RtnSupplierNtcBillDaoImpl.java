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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.rtn.rtnsupplierntc.RtnSupplierNtcBill;
import com.hd123.sardine.wms.api.rtn.rtnsupplierntc.RtnSupplierNtcBillItem;
import com.hd123.sardine.wms.common.dao.impl.BaseDaoImpl;
import com.hd123.sardine.wms.dao.rtn.rtnsupplierntc.RtnSupplierNtcBillDao;

/**
 * @author yangwenzhu
 *
 */
public class RtnSupplierNtcBillDaoImpl extends BaseDaoImpl<RtnSupplierNtcBill>
    implements RtnSupplierNtcBillDao {

  public static final String MAPPER_INSERT_ITEMS = "insert_items";
  public static final String MAPPER_REMOVE_ITEMS = "remove_items";
  public static final String MAPPER_QUERY_ITEMS = "query_items";
  public static final String MAPPER_GET_ITEM = "get_item";
  public static final String MAPPER_REFRESH_ITEM_UNSHELVED_QTY_AND_CASEQTYSTR = "refreshItemUnshelvedQtyAndCaseQtyStr";

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
  public void refreshItemUnshelvedQtyAndCaseQtyStr(String itemUuid, BigDecimal unshelvedQty,
      String caseQtyStr) {
    Assert.assertArgumentNotNull(itemUuid, "itemUuid");
    Assert.assertArgumentNotNull(unshelvedQty, "unshelvedQty");
    Assert.assertArgumentNotNull(caseQtyStr, "caseQtyStr");
    Map<String, Object> map = new HashMap<>();
    map.put("uuid", itemUuid);
    map.put("unshelvedQty", unshelvedQty);
    map.put("caseQtyStr", caseQtyStr);

    getSqlSession().update(generateStatement(MAPPER_REFRESH_ITEM_UNSHELVED_QTY_AND_CASEQTYSTR),
        map);

  }

}
