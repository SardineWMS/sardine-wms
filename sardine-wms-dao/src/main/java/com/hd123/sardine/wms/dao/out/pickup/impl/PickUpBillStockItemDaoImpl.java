/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	PickUpBillStockItemDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月17日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.out.pickup.impl;

import java.util.List;

import com.hd123.sardine.wms.api.out.pickup.PickUpBillStockItem;
import com.hd123.sardine.wms.common.dao.NameSpaceSupport;
import com.hd123.sardine.wms.dao.out.pickup.PickUpBillStockItemDao;

/**
 * @author zhangsai
 *
 */
public class PickUpBillStockItemDaoImpl extends NameSpaceSupport implements PickUpBillStockItemDao {

  @Override
  public void saveNew(List<PickUpBillStockItem> items) {
    // TODO Auto-generated method stub

  }

  @Override
  public List<PickUpBillStockItem> query(String itemUuid) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void removeByItem(List<String> itemUuids) {
    // TODO Auto-generated method stub

  }

}
