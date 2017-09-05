/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	AcceptanceBillDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月6日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.dao.out.acceptance.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBill;
import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBillItem;
import com.hd123.sardine.wms.common.dao.NameSpaceSupport;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.dao.out.acceptance.AcceptanceBillDao;

/**
 * @author fanqingqing
 *
 */
public class AcceptanceBillDaoImpl extends NameSpaceSupport implements AcceptanceBillDao {
  private static final String MAPPER_GET = "get";
  private static final String MAPPER_GETBYBILLNUMBER = "getByBillNumber";
  private static final String MAPPER_INSERT = "insert";
  private static final String MAPPER_UPDATE = "update";
  private static final String MAPPER_REMOVE = "remove";
  private static final String MAPPER_INSERTITEMS = "insertItems";
  private static final String MAPPER_REMOVEITEMS = "removeItems";
  private static final String MAPPER_QUERYITEMS = "queryItems";
  private static final String MAPPER_QUERY = "query";
  private static final String UPDATEITEM = "updateItem";
  private static final String GETBYITEMUUID = "getByItemUuid";

  @Override
  public AcceptanceBill get(String uuid) {
    if (StringUtil.isNullOrBlank(uuid))
      return null;

    return selectOne(MAPPER_GET, uuid);
  }

  @Override
  public AcceptanceBill getByBillNumber(String billNumber) {
    if (StringUtil.isNullOrBlank(billNumber))
      return null;

    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("uuid", billNumber);
    return selectOne(MAPPER_GETBYBILLNUMBER, map);
  }

  @Override
  public List<AcceptanceBill> query(PageQueryDefinition definition) {
    Assert.assertArgumentNotNull(definition, "definition");

    definition.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    return selectList(MAPPER_QUERY, definition);
  }

  @Override
  public void insert(AcceptanceBill acceptanceBill) {
    Assert.assertArgumentNotNull(acceptanceBill, "acceptanceBill");

    insert(MAPPER_INSERT, acceptanceBill);
  }

  @Override
  public void update(AcceptanceBill acceptanceBill) {
    Assert.assertArgumentNotNull(acceptanceBill, "acceptanceBill");

    int i = update(MAPPER_UPDATE, acceptanceBill);
    PersistenceUtils.optimisticVerify(i);
  }

  @Override
  public void remove(String uuid, long version) {
    Assert.assertArgumentNotNull(uuid, "uuid");

    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("uuid", uuid);
    map.put("version", version);
    int i = delete(MAPPER_REMOVE, map);
    PersistenceUtils.optimisticVerify(i);
  }

  @Override
  public void insertItems(List<AcceptanceBillItem> items) {
    Assert.assertArgumentNotNull(items, "items");

    for (AcceptanceBillItem item : items) {
      insert(MAPPER_INSERTITEMS, item);
    }
  }

  @Override
  public void removeItems(String acceptanceBillUuid) {
    Assert.assertArgumentNotNull(acceptanceBillUuid, "acceptanceBillUuid");

    delete(MAPPER_REMOVEITEMS, acceptanceBillUuid);
  }

  @Override
  public List<AcceptanceBillItem> queryItems(String acceptanceBillUuid) {
    if (StringUtil.isNullOrBlank(acceptanceBillUuid))
      return new ArrayList<AcceptanceBillItem>();

    return selectList(MAPPER_QUERYITEMS, acceptanceBillUuid);
  }

  @Override
  public void updateItem(AcceptanceBillItem item) {
    Assert.assertArgumentNotNull(item, "item");

    update(UPDATEITEM, item);
  }

  @Override
  public AcceptanceBill getByItemUuid(String itemUuid) {
    if (StringUtil.isNullOrBlank(itemUuid))
      return null;

    return selectOne(GETBYITEMUUID, itemUuid);
  }
}
