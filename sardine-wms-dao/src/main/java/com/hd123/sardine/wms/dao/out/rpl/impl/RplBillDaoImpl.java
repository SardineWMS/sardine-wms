/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-core
 * 文件名：	RplBillDaoImpl.java
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
import com.hd123.sardine.wms.api.out.rpl.RplBill;
import com.hd123.sardine.wms.api.task.TaskView;
import com.hd123.sardine.wms.common.dao.NameSpaceSupport;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.dao.out.rpl.RplBillDao;

/**
 * @author WUJING
 * 
 */
public class RplBillDaoImpl extends NameSpaceSupport implements RplBillDao {
  private static String SAVENEW = "saveNew";
  private static String SAVEMODIFY = "saveModify";
  private static String GET = "get";
  private static String GETBYBILLNUMBER = "getByBillNumber";
  private static String REMOVEBYWAVEBILLNUMBER = "removeByWaveBillNumber";
  private static String APPROVE = "approve";
  private static String QUERYBYWAVEBILLNUMBER = "queryByWaveBillNumber";

  @Override
  public void saveNew(RplBill rplBill) {
    Assert.assertArgumentNotNull(rplBill, "rplBill");

    insert(SAVENEW, rplBill);
  }

  @Override
  public void saveModify(RplBill rplBill) {
    Assert.assertArgumentNotNull(rplBill, "rplBill");
    int i = update(SAVEMODIFY, rplBill);
    PersistenceUtils.optimisticVerify(i);
  }

  @Override
  public RplBill get(String uuid) {
    if (StringUtil.isNullOrBlank(uuid))
      return null;
    RplBill result = selectOne(GET, uuid);
    return result;
  }

  @Override
  public RplBill getByBillNumber(String billNumber) {
    if (StringUtil.isNullOrBlank(billNumber))
      return null;

    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("billNumber", billNumber);
    RplBill result = selectOne(GETBYBILLNUMBER, map);
    return result;
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
  public void approve(String waveBillNumber) {
    if (StringUtil.isNullOrBlank(waveBillNumber))
      return;
    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("waveBillNumber", waveBillNumber);
    update(APPROVE, map);
  }

  @Override
  public List<TaskView> queryByWaveBillNumber(String waveBillNumber) {
    if (StringUtil.isNullOrBlank(waveBillNumber))
      return new ArrayList<TaskView>();

    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("waveBillNumber", waveBillNumber);
    return selectList(QUERYBYWAVEBILLNUMBER, map);
  }
}
