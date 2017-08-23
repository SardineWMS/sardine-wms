/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	ShipBillDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月16日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.dao.tms.shipbill.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.tms.shipbill.ShipBill;
import com.hd123.sardine.wms.api.tms.shipbill.ShipBillContainerStock;
import com.hd123.sardine.wms.api.tms.shipbill.ShipBillCustomerItem;
import com.hd123.sardine.wms.common.dao.impl.BaseDaoImpl;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.dao.tms.shipbill.ShipBillDao;

/**
 * @author fanqingqing
 *
 */
public class ShipBillDaoImpl extends BaseDaoImpl<ShipBill> implements ShipBillDao {
    public static final String MAPPER_GETBYBILLNUMBER = "getByBillNumber";
    public static final String MAPPER_QUERYCUSTOMERITEMS = "queryCustomerItems";
    public static final String MAPPER_INSERTCUSTOMERITEMS = "insertCustomerItems";
    public static final String MAPPER_QUERYCONTAINERSTOCKITEMS = "queryContainerStockItems";
    public static final String MAPPER_INSERTCONTAINERSTOCKITEMS = "insertContainerStockItems";

    @Override
    public ShipBill getByBillNumber(String billNumber) {
        if (StringUtil.isNullOrBlank(billNumber))
            return null;
        Map<String, Object> map = ApplicationContextUtil.map();
        map.put("billNumber", billNumber);
        return getSqlSession().selectOne(generateStatement(MAPPER_GETBYBILLNUMBER), map);
    }

    @Override
    public List<ShipBillCustomerItem> queryCustomerItems(String uuid) {
        if (StringUtil.isNullOrBlank(uuid))
            return new ArrayList<>();
        return getSqlSession().selectList(generateStatement(MAPPER_QUERYCUSTOMERITEMS), uuid);
    }

    @Override
    public void insertCustomerItems(List<ShipBillCustomerItem> items) {
        Assert.assertArgumentNotNull(items, "items");
        for (ShipBillCustomerItem item : items) {
            getSqlSession().insert(generateStatement(MAPPER_INSERTCUSTOMERITEMS), item);
        }
    }

    @Override
    public List<ShipBillContainerStock> queryContainerStockItems(String uuid) {
        if (StringUtil.isNullOrBlank(uuid))
            return new ArrayList<>();
        return getSqlSession().selectList(generateStatement(MAPPER_QUERYCONTAINERSTOCKITEMS), uuid);
    }

    @Override
    public void insertContainerStockItems(List<ShipBillContainerStock> items) {
        Assert.assertArgumentNotNull(items, "items");
        for (ShipBillContainerStock item : items) {
            getSqlSession().insert(generateStatement(MAPPER_INSERTCONTAINERSTOCKITEMS), item);
        }
    }
}
