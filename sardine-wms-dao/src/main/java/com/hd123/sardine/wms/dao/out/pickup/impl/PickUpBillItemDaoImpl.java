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

import java.util.List;

import com.hd123.sardine.wms.api.out.pickup.PickUpBillItem;
import com.hd123.sardine.wms.common.dao.NameSpaceSupport;
import com.hd123.sardine.wms.dao.out.pickup.PickUpBillItemDao;

/**
 * @author zhangsai
 *
 */
public class PickUpBillItemDaoImpl extends NameSpaceSupport implements PickUpBillItemDao {

  @Override
  public void saveNew(List<PickUpBillItem> items) {
    // TODO Auto-generated method stub

  }

  @Override
  public List<PickUpBillItem> queryByPickUpBill(String pickUpBillUuid) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void updateRealQty(PickUpBillItem item) {
    // TODO Auto-generated method stub

  }

  @Override
  public void removeByPickUpBill(String pickUpBillUuid) {
    // TODO Auto-generated method stub

  }

  @Override
  public void removeItems(List<String> uuids) {
    // TODO Auto-generated method stub

  }

  @Override
  public void removeByWaveBillNumber(String waveBillNumber) {
    // TODO Auto-generated method stub

  }

}
