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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.out.pickup.PickUpBillStockItem;
import com.hd123.sardine.wms.common.dao.NameSpaceSupport;
import com.hd123.sardine.wms.dao.out.pickup.PickUpBillStockItemDao;

/**
 * @author zhangsai
 *
 */
public class PickUpBillStockItemDaoImpl extends NameSpaceSupport implements PickUpBillStockItemDao {

  private static final String SAVENEW = "saveNew";
  private static final String QUERY = "query";

  @Override
  public void saveNew(List<PickUpBillStockItem> items) {
    if (CollectionUtils.isEmpty(items))
      return;

    for (PickUpBillStockItem sItem : items) {
      insert(SAVENEW, sItem);
    }
  }

  @Override
  public List<PickUpBillStockItem> query(String itemUuid) {
    if (StringUtil.isNullOrBlank(itemUuid))
      return new ArrayList<PickUpBillStockItem>();

    return selectList(QUERY, itemUuid);
  }
}
