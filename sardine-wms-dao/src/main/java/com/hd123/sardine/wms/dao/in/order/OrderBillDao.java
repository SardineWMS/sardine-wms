/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	OrderBillDao.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月13日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.in.order;

import java.math.BigDecimal;
import java.util.List;

import com.hd123.sardine.wms.api.in.order.OrderBill;
import com.hd123.sardine.wms.api.in.order.OrderBillItem;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;

/**
 * 订单DAO：接口
 * 
 * @author zhangsai
 *
 */
public interface OrderBillDao {

  void insert(OrderBill orderBill);

  void update(OrderBill orderBill);

  OrderBill get(String uuid);

  OrderBill getByBillNumber(String billNumber);

  List<OrderBill> query(PageQueryDefinition definition);

  void remove(String uuid, long version);

  void insertItems(List<OrderBillItem> items);

  void updateItem(String uuid, BigDecimal qty, String caseQtyStr);

  void removeItems(String orderBillUuid);

  List<OrderBillItem> queryItems(String orderBillUuid);
}
