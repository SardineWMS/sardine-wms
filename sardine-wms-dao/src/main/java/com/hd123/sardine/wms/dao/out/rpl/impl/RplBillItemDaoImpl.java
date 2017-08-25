/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-core
 * 文件名：	RplItemDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2014-8-12 - WUJING - 创建。
 */
package com.hd123.sardine.wms.dao.out.rpl.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.out.rpl.RplBillItem;
import com.hd123.sardine.wms.common.dao.NameSpaceSupport;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.dao.out.rpl.RplBillItemDao;

/**
 * @author WUJING
 * 
 */
public class RplBillItemDaoImpl extends NameSpaceSupport implements RplBillItemDao {
  private static String SAVENEW = "saveNew";
  private static String QUERYBYRPLUUID = "queryByRplUuid";
  private static String SAVEMODIFY = "saveModify";
  private static String REMOVEBYWAVEBILLNUMBER = "removeByWaveBillNumber";
  private static String QUERYRPLITEMS = "queryRplItems";

  @Override
  public void saveNew(List<RplBillItem> items) {
    Assert.assertArgumentNotNull(items, "items");
    for (RplBillItem item : items) {
      insert(SAVENEW, item);
    }
  }

  @Override
  public List<RplBillItem> queryByRplUuid(String rplBillUuid) {
    if (StringUtil.isNullOrBlank(rplBillUuid))
      return new ArrayList<RplBillItem>();

    List<RplBillItem> items = selectList(QUERYBYRPLUUID, rplBillUuid);
    return items;
  }

  @Override
  public void removeByWaveBillNumber(String waveBillNumber) {
    if (StringUtil.isNullOrBlank(waveBillNumber))
      return;

    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("waveBillNumber", waveBillNumber);
    delete(REMOVEBYWAVEBILLNUMBER, map);
  }

  @Override
  public void saveModify(RplBillItem item) {
    update(SAVEMODIFY, item);
  }

  @Override
  public List<RplBillItem> queryRplItems(String waveBillNumber) {
    if (StringUtil.isNullOrBlank(waveBillNumber))
      return new ArrayList<RplBillItem>();

    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("waveBillNumber", waveBillNumber);
    return selectList(QUERYRPLITEMS, map);
  }
}
