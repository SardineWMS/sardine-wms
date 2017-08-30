/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	PickUpBillItemDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月17日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.out.pickup.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.out.pickup.PickUpBillItem;
import com.hd123.sardine.wms.common.dao.NameSpaceSupport;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.dao.out.pickup.PickUpBillItemDao;

/**
 * @author zhangsai
 *
 */
public class PickUpBillItemDaoImpl extends NameSpaceSupport implements PickUpBillItemDao {

  private static final String SAVENEW = "saveNew";
  private static final String QUERYBYPICKUPBILL = "queryByPickUpBill";
  private static final String UPDATEREALQTY = "updateRealQty";
  private static final String REMOVEBYPICKUPBILL = "removeByPickUpBill";
  private static final String REMOVEBYWAVEBILLNUMBER = "removeByWaveBillNumber";
  private static final String QUERYBYUUIDS = "queryByUuids";
  private static final String QUERYBYSOURCECONTAINERBARCODE = "queryBySourceContainerBarcode";

  @Override
  public void saveNew(List<PickUpBillItem> items) {
    if (CollectionUtils.isEmpty(items))
      return;

    for (PickUpBillItem item : items) {
      insert(SAVENEW, item);
    }
  }

  @Override
  public List<PickUpBillItem> queryByPickUpBill(String pickUpBillUuid) {
    if (StringUtil.isNullOrBlank(pickUpBillUuid))
      return new ArrayList<PickUpBillItem>();

    return selectList(QUERYBYPICKUPBILL, pickUpBillUuid);
  }

  @Override
  public void updateRealQty(PickUpBillItem item) {
    Assert.assertArgumentNotNull(item, "item");

    update(UPDATEREALQTY, item);
  }

  @Override
  public void removeByPickUpBill(String pickUpBillUuid) {
    if (StringUtil.isNullOrBlank(pickUpBillUuid))
      return;

    delete(REMOVEBYPICKUPBILL, pickUpBillUuid);
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
  public List<PickUpBillItem> queryByUuids(List<String> uuids) {
    if (CollectionUtils.isEmpty(uuids))
      return new ArrayList<PickUpBillItem>();
    Map<String, List<String>> map = new HashMap<String, List<String>>();
    map.put("uuids", uuids);
    return selectList(QUERYBYUUIDS, map);
  }

  @Override
  public List<PickUpBillItem> queryBySourceContainerBarcode(String sourceContainerBarcode) {
    if (StringUtil.isNullOrBlank(sourceContainerBarcode))
      return new ArrayList<PickUpBillItem>();

    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("sourceContainerBarcode", sourceContainerBarcode);
    return selectList(QUERYBYSOURCECONTAINERBARCODE, map);
  }
}
