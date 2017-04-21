/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	ReceiveBillDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月14日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.in.receive.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.in.receive.ReceiveBill;
import com.hd123.sardine.wms.api.in.receive.ReceiveBillItem;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.dao.in.receive.ReceiveBillDao;

/**
 * @author zhangsai
 *
 */
public class ReceiveBillDaoImpl extends SqlSessionDaoSupport implements ReceiveBillDao {
  private static final String MAPPER_GET = "get";
  private static final String MAPPER_GETBYBILLNUMBER = "getByBillNumber";
  private static final String MAPPER_INSERT = "insert";
  private static final String MAPPER_UPDATE = "update";
  private static final String MAPPER_REMOVE = "remove";
  private static final String MAPPER_INSERTITEMS = "insertItems";
  private static final String MAPPER_REMOVEITEMS = "removeItems";
  private static final String MAPPER_QUERYITEMS = "queryItems";
  private static final String MAPPER_QUERY = "query";

  private String generateStatement(String mapperId) {
    return this.getClass().getName() + "." + mapperId;
  }

  @Override
  public void insert(ReceiveBill bill) {
    Assert.assertArgumentNotNull(bill, "bill");

    getSqlSession().insert(generateStatement(MAPPER_INSERT), bill);
  }

  @Override
  public void update(ReceiveBill bill) {
    Assert.assertArgumentNotNull(bill, "bill");

    int i = getSqlSession().update(generateStatement(MAPPER_UPDATE), bill);
    PersistenceUtils.optimisticVerify(i);
  }

  @Override
  public ReceiveBill get(String uuid) {
    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("uuid", uuid);

    List<ReceiveBill> result = getSqlSession().selectList(generateStatement(MAPPER_GET), map);
    return CollectionUtils.isEmpty(result) ? null : result.get(0);
  }

  @Override
  public ReceiveBill getByBillNumber(String billNumber) {
    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("billNumber", billNumber);

    List<ReceiveBill> result = getSqlSession().selectList(generateStatement(MAPPER_GETBYBILLNUMBER),
        map);
    return CollectionUtils.isEmpty(result) ? null : result.get(0);
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
  public List<ReceiveBill> query(PageQueryDefinition definition) {
    Assert.assertArgumentNotNull(definition, "definition");

    return getSqlSession().selectList(generateStatement(MAPPER_QUERY), definition);
  }

  @Override
  public void insertItems(List<ReceiveBillItem> items) {
    Assert.assertArgumentNotNull(items, "items");

    for (ReceiveBillItem item : items) {
      Map<String, Object> map = ApplicationContextUtil.map();
      map.put("item", item);
      getSqlSession().insert(generateStatement(MAPPER_INSERTITEMS), map);
    }
  }

  @Override
  public List<ReceiveBillItem> queryItems(String receiveBillUuid) {
    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("receiveBillUuid", receiveBillUuid);
    return getSqlSession().selectList(generateStatement(MAPPER_QUERYITEMS), map);
  }

  @Override
  public void removeItems(String receiveBillUuid) {
    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("receiveBillUuid", receiveBillUuid);
    getSqlSession().delete(generateStatement(MAPPER_REMOVEITEMS), map);
  }
}
