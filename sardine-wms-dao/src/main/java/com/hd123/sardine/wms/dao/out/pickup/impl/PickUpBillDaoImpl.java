/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	PickUpBillDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月17日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.out.pickup.impl;

import java.util.List;

import com.hd123.sardine.wms.api.out.pickup.PickUpBill;
import com.hd123.sardine.wms.api.out.pickup.PickUpBillFilter;
import com.hd123.sardine.wms.common.dao.NameSpaceSupport;
import com.hd123.sardine.wms.dao.out.pickup.PickUpBillDao;

/**
 * @author zhangsai
 *
 */
public class PickUpBillDaoImpl extends NameSpaceSupport implements PickUpBillDao{

  @Override
  public void saveNew(PickUpBill pickUpBill) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void saveModify(PickUpBill pickUpBill) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public PickUpBill get(String uuid) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public PickUpBill getByBillNumber(String billNumber) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<PickUpBill> query(PickUpBillFilter filter) {
    // TODO Auto-generated method stub
    return null;
  }

}
