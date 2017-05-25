/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	DecIncInvBillDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月17日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.dao.inner.decincinv.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.inner.decincinv.DecIncInvBill;
import com.hd123.sardine.wms.api.inner.decincinv.DecIncInvBillItem;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.dao.inner.decincinv.DecIncInvBillDao;

/**
 * @author fanqingqing
 *
 */
public class DecIncInvBillDaoImpl extends SqlSessionDaoSupport implements DecIncInvBillDao {
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
    public DecIncInvBill get(String uuid) {
        if (StringUtil.isNullOrBlank(uuid))
            return null;

        DecIncInvBill result = getSqlSession().selectOne(generateStatement(MAPPER_GET), uuid);
        return result;
    }

    @Override
    public DecIncInvBill getByBillNumber(String billNumber) {
        if (StringUtil.isNullOrBlank(billNumber))
            return null;
        Map<String, Object> map = ApplicationContextUtil.map();
        map.put("billNumber", billNumber);

        DecIncInvBill result = getSqlSession().selectOne(generateStatement(MAPPER_GETBYBILLNUMBER),
                map);
        return result;
    }

    @Override
    public List<DecIncInvBill> query(PageQueryDefinition definition) {
        Assert.assertArgumentNotNull(definition, "definition");

        return getSqlSession().selectList(generateStatement(MAPPER_QUERY), definition);
    }

    @Override
    public void insert(DecIncInvBill billNumber) {
        Assert.assertArgumentNotNull(billNumber, "billNumber");

        getSqlSession().insert(generateStatement(MAPPER_INSERT), billNumber);
    }

    @Override
    public void update(DecIncInvBill billNumber) {
        Assert.assertArgumentNotNull(billNumber, "billNumber");

        int i = getSqlSession().update(generateStatement(MAPPER_UPDATE), billNumber);
        PersistenceUtils.optimisticVerify(i);
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
    public void removeItems(String billUuid) {
        Assert.assertArgumentNotNull(billUuid, "billUuid");

        getSqlSession().delete(generateStatement(MAPPER_REMOVEITEMS), billUuid);
    }

    @Override
    public void insertItems(List<DecIncInvBillItem> items) {
        Assert.assertArgumentNotNull(items, "items");

        for (DecIncInvBillItem item : items) {
            Map<String, Object> map = ApplicationContextUtil.map();
            map.put("item", item);
            getSqlSession().insert(generateStatement(MAPPER_INSERTITEMS), map);
        }
    }

    @Override
    public List<DecIncInvBillItem> queryItems(String billUuid) {
        if (StringUtil.isNullOrBlank(billUuid))
            return null;
        Map<String, Object> map= new HashMap<String, Object>();
        map.put("decIncInvBillUuid", billUuid);
        return getSqlSession().selectList(generateStatement(MAPPER_QUERYITEMS), map);
    }

}
