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

import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBill;
import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBillItem;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.dao.out.acceptance.AcceptanceBillDao;

/**
 * @author fanqingqing
 *
 */
public class AcceptanceBillDaoImpl extends SqlSessionDaoSupport implements AcceptanceBillDao {
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
    public AcceptanceBill get(String uuid) {
        if (StringUtil.isNullOrBlank(uuid))
            return null;

        Map<String, Object> map = ApplicationContextUtil.map();
        map.put("uuid", uuid);
        return getSqlSession().selectOne(generateStatement(MAPPER_GET), map);
    }

    @Override
    public AcceptanceBill getByBillNumber(String billNumber) {
        if (StringUtil.isNullOrBlank(billNumber))
            return null;

        Map<String, Object> map = ApplicationContextUtil.map();
        map.put("uuid", billNumber);
        return getSqlSession().selectOne(generateStatement(MAPPER_GETBYBILLNUMBER), map);
    }

    @Override
    public List<AcceptanceBill> query(PageQueryDefinition definition) {
        Assert.assertArgumentNotNull(definition, "definition");

        definition.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
        return getSqlSession().selectList(generateStatement(MAPPER_QUERY), definition);
    }

    @Override
    public void insert(AcceptanceBill acceptanceBill) {

        Assert.assertArgumentNotNull(acceptanceBill, "acceptanceBill");

        getSqlSession().insert(generateStatement(MAPPER_INSERT), acceptanceBill);
    }

    @Override
    public void update(AcceptanceBill acceptanceBill) {
        Assert.assertArgumentNotNull(acceptanceBill, "acceptanceBill");

        int i = getSqlSession().update(generateStatement(MAPPER_UPDATE), acceptanceBill);
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
    public void insertItems(List<AcceptanceBillItem> items) {
        Assert.assertArgumentNotNull(items, "items");

        for (AcceptanceBillItem item : items) {
            Map<String, Object> map = ApplicationContextUtil.map();
            map.put("item", item);
            getSqlSession().insert(generateStatement(MAPPER_INSERTITEMS), map);
        }
    }

    @Override
    public void removeItems(String acceptanceBillUuid) {
        Map<String, Object> map = ApplicationContextUtil.map();
        map.put("acceptanceBillUuid", acceptanceBillUuid);
        getSqlSession().delete(generateStatement(MAPPER_REMOVEITEMS), map);
    }

    @Override
    public List<AcceptanceBillItem> queryItems(String acceptanceBillUuid) {
        Map<String, Object> map = ApplicationContextUtil.map();
        map.put("acceptanceBillUuid", acceptanceBillUuid);
        return getSqlSession().selectList(generateStatement(MAPPER_QUERYITEMS), map);
    }

}
