/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	OrderBillDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月13日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.in.order.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.in.order.OrderBill;
import com.hd123.sardine.wms.api.in.order.OrderBillItem;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.dao.in.order.OrderBillDao;

/**
 * 订单DAO：实现
 * 
 * @author zhangsai
 *
 */
public class OrderBillDaoImpl extends SqlSessionDaoSupport implements OrderBillDao {
  private static final String MAPPER_GET = "get";
  private static final String MAPPER_GETBYBILLNUMBER = "getByBillNumber";
  private static final String MAPPER_INSERT = "insert";
  private static final String MAPPER_UPDATE = "update";
  private static final String MAPPER_REMOVE = "remove";
  private static final String MAPPER_INSERTITEMS = "insertItems";
  private static final String MAPPER_UPDATEITEM = "updateItem";
  private static final String MAPPER_REMOVEITEMS = "removeItems";
  private static final String MAPPER_QUERYITEMS = "queryItems";
  private static final String MAPPER_QUERY = "query";

  private String generateStatement(String mapperId) {
    return this.getClass().getName() + "." + mapperId;
  }

  @Override
  public void insert(OrderBill orderBill) {
    Assert.assertArgumentNotNull(orderBill, "orderBill");

    getSqlSession().insert(generateStatement(MAPPER_INSERT), orderBill);
  }

  @Override
  public void update(OrderBill orderBill) {
    Assert.assertArgumentNotNull(orderBill, "orderBill");

    int i = getSqlSession().update(generateStatement(MAPPER_UPDATE), orderBill);
    PersistenceUtils.optimisticVerify(i);
  }

  @Override
  public OrderBill get(String uuid) {
    if (StringUtil.isNullOrBlank(uuid))
      return null;

    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("uuid", uuid);

    List<OrderBill> result = getSqlSession().selectList(generateStatement(MAPPER_GET), map);
    if (CollectionUtils.isEmpty(result))
      return null;
    return result.get(0);
  }

  @Override
  public OrderBill getByBillNumber(String billNumber) {
    if (StringUtil.isNullOrBlank(billNumber))
      return null;

    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("billNumber", billNumber);

    List<OrderBill> result = getSqlSession().selectList(generateStatement(MAPPER_GETBYBILLNUMBER),
        map);
    if (CollectionUtils.isEmpty(result))
      return null;
    return result.get(0);
  }

  @Override
  public List<OrderBill> query(PageQueryDefinition definition) {
    Assert.assertArgumentNotNull(definition, "definition");

    return getSqlSession().selectList(generateStatement(MAPPER_QUERY), definition);
  }

  @Override
  public void remove(String uuid, long version) {
    Assert.assertArgumentNotNull(uuid, "uuid");

    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("uuid", uuid);
    map.put("version", version);
    int i = getSqlSession().delete(generateStatement(MAPPER_REMOVE), map);
    PersistenceUtils.optimisticVerify(i);
  }

  @Override
  public void insertItems(List<OrderBillItem> items) {
    Assert.assertArgumentNotNull(items, "items");

    for (OrderBillItem item : items) {
      getSqlSession().insert(generateStatement(MAPPER_INSERTITEMS), item);
    }
  }

  @Override
  public void updateItem(String uuid, BigDecimal qty, String caseQtyStr) {
    Assert.assertArgumentNotNull(uuid, "uuid");

    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("uuid", uuid);
    map.put("qty", qty);
    map.put("caseQtyStr", caseQtyStr);
    int i = getSqlSession().update(generateStatement(MAPPER_UPDATEITEM), map);
    PersistenceUtils.optimisticVerify(i);
  }

  @Override
  public void removeItems(String orderBillUuid) {
    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("orderBillUuid", orderBillUuid);
    getSqlSession().delete(generateStatement(MAPPER_REMOVEITEMS), map);
  }

  @Override
  public List<OrderBillItem> queryItems(String orderBillUuid) {
    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("orderBillUuid", orderBillUuid);
    return getSqlSession().selectList(generateStatement(MAPPER_QUERYITEMS), map);
  }
}
