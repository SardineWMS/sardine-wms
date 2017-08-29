/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	AlcNtcBillDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月1日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.dao.out.alcntc.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.out.alcntc.AlcNtcBill;
import com.hd123.sardine.wms.api.out.alcntc.AlcNtcBillItem;
import com.hd123.sardine.wms.common.dao.impl.BaseDaoImpl;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.dao.out.alcntc.AlcNtcBillDao;

/**
 * @author yangwenzhu
 *
 */
public class AlcNtcBillDaoImpl extends BaseDaoImpl<AlcNtcBill> implements AlcNtcBillDao {
  public static final String MAPPER_GETBYBILLNUMBER = "getByBillNumber";
  public static final String MAPPER_INSERTITEMS = "insertItems";
  public static final String MAPPER_REMOVEITEMSBYALCNTCUUID = "removeItemsByAlcNtcUuid";
  public static final String MAPPER_QUERYITEMSBYALCNTCUUID = "queryItemsByAlcNtcUuid";
  public static final String MAPPER_GETITEMBYUUID = "getItemByUuid";
  public static final String MAPPER_UPDATEITEM = "updateItem";
  public static final String MAPPER_GETBYITEMUUID = "getByItemUuid";
  public static final String MAPPER_GETBYTASKBILLNUMBER = "getByTaskBillNumber";
  private static final String REFRESHALCNTCBILLITEMPLANQTY = "refreshAlcNtcBillItemPlanQty";
  private static final String REFRESHALCNTCBILLITEMPLANCASEQTYSTR = "refreshAlcNtcBillItemPlanCaseQtyStr";

  @Override
  public AlcNtcBill getByBillNumber(String billNumber) {
    if (StringUtil.isNullOrBlank(billNumber))
      return null;
    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("billNumber", billNumber);
    return selectOne(MAPPER_GETBYBILLNUMBER, map);
  }

  @Override
  public void insertItems(List<AlcNtcBillItem> items) {
    Assert.assertArgumentNotNull(items, "items");
    for (AlcNtcBillItem item : items) {
      insert(MAPPER_INSERTITEMS, item);
    }
  }

  @Override
  public void removeItems(String alcNtcBillUuid) {
    Assert.assertArgumentNotNull(alcNtcBillUuid, "alcNtcBillUuid");

    getSqlSession().delete(generateStatement(MAPPER_REMOVEITEMSBYALCNTCUUID), alcNtcBillUuid);

  }

  @Override
  public List<AlcNtcBillItem> queryItems(String alcNtcBillUuid) {
    if (StringUtil.isNullOrBlank(alcNtcBillUuid))
      return new ArrayList<AlcNtcBillItem>();
    return getSqlSession().selectList(generateStatement(MAPPER_QUERYITEMSBYALCNTCUUID),
        alcNtcBillUuid);
  }

  @Override
  public AlcNtcBillItem getItemByUuid(String itemUuid) {
    if (StringUtil.isNullOrBlank(itemUuid))
      return null;
    return getSqlSession().selectOne(generateStatement(MAPPER_GETITEMBYUUID), itemUuid);
  }

  @Override
  public void updateItem(AlcNtcBillItem item) {
    Assert.assertArgumentNotNull(item, "item");

    getSqlSession().update(generateStatement(MAPPER_UPDATEITEM), item);

  }

  @Override
  public AlcNtcBill getByItemUuid(String itemUuid) {
    if (StringUtil.isNullOrBlank(itemUuid))
      return null;
    return getSqlSession().selectOne(generateStatement(MAPPER_GETBYITEMUUID), itemUuid);
  }

  @Override
  public List<AlcNtcBill> getByWaveBillNumber(String waveBillNumber) {
    if (StringUtil.isNullOrBlank(waveBillNumber))
      return new ArrayList<>();
    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("waveBillNumber", waveBillNumber);
    return getSqlSession().selectList(generateStatement(MAPPER_GETBYTASKBILLNUMBER), map);
  }

  @Override
  public void refreshAlcNtcBillItemPlanCaseQtyStr(String waveBillNumber) {
    Assert.assertArgumentNotNull(waveBillNumber, "waveBillNumber");

    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("waveBillNumber", waveBillNumber);
    update(REFRESHALCNTCBILLITEMPLANCASEQTYSTR, map);

  }

  @Override
  public void refreshAlcNtcBillItemPlanQty(String waveBillNumber) {
    Assert.assertArgumentNotNull(waveBillNumber, "waveBillNumber");

    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("waveBillNumber", waveBillNumber);
    update(REFRESHALCNTCBILLITEMPLANQTY, map);

  }
}
