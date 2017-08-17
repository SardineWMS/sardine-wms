/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-core
 * 文件名：	PickUpDao.java
 * 模块说明：	
 * 修改历史：
 * 2014-8-5 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.out.pickup;

import java.util.List;

import com.hd123.sardine.wms.api.out.pickup.PickUpBill;
import com.hd123.sardine.wms.api.out.pickup.PickUpBillFilter;

/**
 * 拣货单DAO：接口
 * 
 * @author zhangsai
 * 
 */
public interface PickUpBillDao {

  void saveNew(PickUpBill pickUpBill);

  void saveModify(PickUpBill pickUpBill);

  PickUpBill get(String uuid);

  PickUpBill getByBillNumber(String billNumber);

  List<PickUpBill> query(PickUpBillFilter filter);
}
