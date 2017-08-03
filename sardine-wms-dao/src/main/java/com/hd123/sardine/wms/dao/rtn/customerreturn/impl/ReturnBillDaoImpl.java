/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	ReturnBillDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年7月6日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.dao.rtn.customerreturn.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.rtn.customerreturn.ReturnBill;
import com.hd123.sardine.wms.api.rtn.customerreturn.ReturnBillItem;
import com.hd123.sardine.wms.common.dao.impl.BaseDaoImpl;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.dao.rtn.customerreturn.ReturnBillDao;

/**
 * @author yangwenzhu
 *
 */
public class ReturnBillDaoImpl extends BaseDaoImpl<ReturnBill> implements ReturnBillDao {
    public static final String MAPPER_INSERTITEMS = "insertItems";
    public static final String MAPPER_REMOVEITEMS = "removeItems";
    public static final String MAPPER_GETBYBILLNUMBER = "getByBillNumber";
    public static final String MAPPER_GETITEMS = "getItems";

    @Override
    public void insertItems(List<ReturnBillItem> items) {
        Assert.assertArgumentNotNull(items, "items");
        for (ReturnBillItem item : items) {
            getSqlSession().insert(generateStatement(MAPPER_INSERTITEMS), item);
        }

    }

    @Override
    public void removeItems(String uuid) {
        Assert.assertArgumentNotNull(uuid, "uuid");
        getSqlSession().delete(generateStatement(MAPPER_REMOVEITEMS), uuid);
    }

    @Override
    public ReturnBill getByBillNumber(String billNumber) {
        if (StringUtil.isNullOrBlank(billNumber))
            return null;
        Map<String, Object> map = ApplicationContextUtil.map();
        map.put("billNumber", billNumber);
        return getSqlSession().selectOne(generateStatement(MAPPER_GETBYBILLNUMBER), map);
    }

    @Override
    public List<ReturnBillItem> getItems(String uuid) {
        if (StringUtil.isNullOrBlank(uuid))
            return new ArrayList<>();
        return getSqlSession().selectList(generateStatement(MAPPER_GETITEMS), uuid);
    }

}
