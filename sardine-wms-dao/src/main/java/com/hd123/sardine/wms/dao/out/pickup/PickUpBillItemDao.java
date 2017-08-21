/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-core
 * 文件名：	PickUpBillItemDao.java
 * 模块说明：	
 * 修改历史：
 * 2014-8-6 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.out.pickup;

import java.util.List;

import com.hd123.sardine.wms.api.out.pickup.PickUpBillItem;

/**
 * 拣货单明细DAO：接口
 * 
 * @author zhangsai
 * 
 */
public interface PickUpBillItemDao {
  void saveNew(List<PickUpBillItem> items);

  List<PickUpBillItem> queryByPickUpBill(String pickUpBillUuid);

  void updateRealQty(PickUpBillItem item);

  void removeByPickUpBill(String pickUpBillUuid);

  void removeByWaveBillNumber(String waveBillNumber);
}
