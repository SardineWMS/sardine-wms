/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	SearchArchDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月14日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.dao.tms.serialarch.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.tms.serialarch.SerialArch;
import com.hd123.sardine.wms.api.tms.serialarch.SerialArchLine;
import com.hd123.sardine.wms.api.tms.serialarch.SerialArchLineCustomer;
import com.hd123.sardine.wms.common.dao.impl.BaseDaoImpl;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.dao.tms.serialarch.SerialArchDao;

/**
 * @author yangwenzhu
 *
 */
public class SerialArchDaoImpl extends BaseDaoImpl<SerialArch> implements SerialArchDao {
    public static final String MAPPER_GETBYCODE = "getByCode";
    public static final String MAPPER_INSERTSERIALARCHLINE = "insertSerialArchLine";
    public static final String MAPPER_GETLINEBYCODE = "getLineByCode";
    public static final String MAPPER_GETLINE = "getLine";
    public static final String MAPPER_GETLINEBYCUSTOMERUUID = "getLineByCustomerUuid";
    public static final String MAPPER_SAVELINECUSTOME = "saveLineCustomer";
    public static final String MAPPER_GETLINEBYARCHUUID = "getLineByArchUuid";
    public static final String MAPPER_GETCUSTOMERBYLINE = "getCustomerByLine";
    public static final String MAPPER_GETCUSTOMER = "getCustomer";
    public static final String MAPPER_REMOVECUSTOMER = "removeCustomer";
    public static final String MAPPER_UPDATELINECUSTOMER = "updateLineCustomer";
    public static final String MAPPER_GETCUSTOMERBYLINEANDORDER = "getCustomerByLineAndOrder";
    public static final String MAPPER_QUERYCUSTOMERORDERLESS = "queryCustomerOrderLess";
    public static final String MAPPER_QUERYCUSTOMERORDERMORE = "queryCustomerOrderMore";
    public static final String MAPPER_QUERYCUSTOMERBYLINE = "queryCustomerByLine";

    @Override
    public SerialArch getByCode(String code) {
        if (StringUtil.isNullOrBlank(code))
            return null;
        Map<String, Object> map = ApplicationContextUtil.map();
        map.put("code", code);
        return getSqlSession().selectOne(generateStatement(MAPPER_GETBYCODE), map);
    }

    @Override
    public int insertSerialArchLine(SerialArchLine line) {
        Assert.assertArgumentNotNull(line, "line");
        return getSqlSession().insert(generateStatement(MAPPER_INSERTSERIALARCHLINE), line);
    }

    @Override
    public SerialArchLine getLineByCode(String lineCode) {
        if (StringUtil.isNullOrBlank(lineCode))
            return null;
        Map<String, Object> map = ApplicationContextUtil.map();
        map.put("code", lineCode);
        return getSqlSession().selectOne(generateStatement(MAPPER_GETLINEBYCODE), map);
    }

    @Override
    public SerialArchLine getLine(String uuid) {
        if (StringUtil.isNullOrBlank(uuid))
            return null;
        return getSqlSession().selectOne(generateStatement(MAPPER_GETLINE), uuid);
    }

    @Override
    public List<SerialArchLine> getLineByCustomerUuid(String customerUuid) {
        if (StringUtil.isNullOrBlank(customerUuid))
            return new ArrayList<>();
        return getSqlSession().selectList(generateStatement(MAPPER_GETLINEBYCUSTOMERUUID),
                customerUuid);
    }

    @Override
    public int insertLineCustomer(SerialArchLineCustomer c) {
        Assert.assertArgumentNotNull(c, "c");
        return getSqlSession().insert(generateStatement(MAPPER_SAVELINECUSTOME), c);
    }

    @Override
    public List<SerialArchLine> getLineByArchUuid(String archUuid) {
        if (StringUtil.isNullOrBlank(archUuid))
            return new ArrayList<>();
        return getSqlSession().selectList(generateStatement(MAPPER_GETLINEBYARCHUUID), archUuid);
    }

    @Override
    public List<SerialArchLineCustomer> getCustomerByLine(String lineUuid) {
        if (StringUtil.isNullOrBlank(lineUuid))
            return new ArrayList<>();
        return getSqlSession().selectList(generateStatement(MAPPER_GETCUSTOMERBYLINE), lineUuid);
    }

    @Override
    public SerialArchLineCustomer getCustomer(String lineUuid, String customerUuid) {
        if (StringUtil.isNullOrBlank(lineUuid) || StringUtil.isNullOrBlank(customerUuid))
            return null;
        Map<String, String> map = new HashMap<>();
        map.put("lineUuid", lineUuid);
        map.put("customerUuid", customerUuid);
        return getSqlSession().selectOne(generateStatement(MAPPER_GETCUSTOMER), map);
    }

    @Override
    public int removeCustomer(String lineUuid, String customerUuid) {
        Assert.assertArgumentNotNull(lineUuid, "lineUuid");
        Assert.assertArgumentNotNull(customerUuid, "customerUuid");
        Map<String, String> map = new HashMap<>();
        map.put("lineUuid", lineUuid);
        map.put("customerUuid", customerUuid);
        int i = getSqlSession().delete(generateStatement(MAPPER_REMOVECUSTOMER), map);
        PersistenceUtils.optimisticVerify(i);
        return i;
    }

    @Override
    public void updateLineCustomer(SerialArchLineCustomer lineCustomer) {
        Assert.assertArgumentNotNull(lineCustomer, "lineCustomer");

        getSqlSession().update(generateStatement(MAPPER_UPDATELINECUSTOMER), lineCustomer);
    }

    @Override
    public SerialArchLineCustomer getCustomerByLineAndOrder(String lineUuid, int order) {
        if (StringUtil.isNullOrBlank(lineUuid))
            return null;
        Map<String, Object> map = new HashMap<>();
        map.put("lineUuid", lineUuid);
        map.put("order", order);
        return getSqlSession().selectOne(generateStatement(MAPPER_GETCUSTOMERBYLINEANDORDER), map);
    }

    @Override
    public List<SerialArchLineCustomer> queryCustomerOrderLess(String lineUuid, int order) {
        if (StringUtil.isNullOrBlank(lineUuid))
            return new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("lineUuid", lineUuid);
        map.put("order", order);
        return getSqlSession().selectList(generateStatement(MAPPER_QUERYCUSTOMERORDERLESS), map);
    }

    @Override
    public List<SerialArchLineCustomer> queryCustomerOrderMore(String lineUuid, int order) {
        if (StringUtil.isNullOrBlank(lineUuid))
            return new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("lineUuid", lineUuid);
        map.put("order", order);
        return getSqlSession().selectList(generateStatement(MAPPER_QUERYCUSTOMERORDERMORE), map);
    }

    @Override
    public List<SerialArchLineCustomer> queryCustomerByLine(PageQueryDefinition definition) {
        Assert.assertArgumentNotNull(definition, "definition");

        return getSqlSession().selectList(generateStatement(MAPPER_QUERYCUSTOMERBYLINE),
                definition);
    }

}
