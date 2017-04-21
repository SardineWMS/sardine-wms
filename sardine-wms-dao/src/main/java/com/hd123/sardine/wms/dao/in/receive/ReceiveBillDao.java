/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	sardine-wms-core
 * 文件名：	ReceiveBillDao.java
 * 模块说明：	
 * 修改历史：
 * 2014-3-20 - WUJING - 创建。
 */
package com.hd123.sardine.wms.dao.in.receive;

import java.util.List;

import com.hd123.sardine.wms.api.in.receive.ReceiveBill;
import com.hd123.sardine.wms.api.in.receive.ReceiveBillItem;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;

/**
 * @author WUJING
 * 
 */
public interface ReceiveBillDao {

  void insert(ReceiveBill bill);

  void update(ReceiveBill bill);

  ReceiveBill get(String uuid);

  ReceiveBill getByBillNumber(String billNumber);

  void remove(String uuid, long version);

  List<ReceiveBill> query(PageQueryDefinition definition);

  void insertItems(List<ReceiveBillItem> items);

  List<ReceiveBillItem> queryItems(String receiveBillUuid);

  void removeItems(String receiveBillUuid);
}
