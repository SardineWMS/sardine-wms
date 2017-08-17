/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-core
 * 文件名：	PickUpBillItemItemDao.java
 * 模块说明：	
 * 修改历史：
 * 2014-8-6 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.out.pickup;

import java.util.List;

import com.hd123.sardine.wms.api.out.pickup.PickUpBillStockItem;

/**
 * 拣货单明细明细DAO：接口
 * 
 * @author zhangsai
 * 
 */
public interface PickUpBillStockItemDao {

  void saveNew(List<PickUpBillStockItem> items);

  List<PickUpBillStockItem> query(String itemUuid);
  
  void removeByItem(List<String> itemUuids);
}
