/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	SupplierReturnBillDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月21日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.dao.rtn.returnsupplier.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.rtn.returnsupplier.ReturnSupplierBill;
import com.hd123.sardine.wms.api.rtn.returnsupplier.ReturnSupplierBillItem;
import com.hd123.sardine.wms.common.dao.impl.BaseDaoImpl;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.dao.rtn.returnsupplier.SupplierReturnBillDao;

/**
 * @author fanqingqing
 *
 */
public class ReturnSupplierBillDaoImpl extends BaseDaoImpl<ReturnSupplierBill>
        implements SupplierReturnBillDao {
    public static final String MAPPER_GETBYBILLNUMBER = "getByBillNumber";
    public static final String MAPPER_QUERYITEMS = "queryItems";
    public static final String MAPPER_INSERTITEMS = "insertItems";

    @Override
    public ReturnSupplierBill getByBillNumber(String billNumber) {
        if (StringUtil.isNullOrBlank(billNumber))
            return null;
        Map<String, Object> map = ApplicationContextUtil.map();
        map.put("billNumber", billNumber);
        return getSqlSession().selectOne(generateStatement(MAPPER_GETBYBILLNUMBER), map);
    }

    @Override
    public List<ReturnSupplierBillItem> queryItems(String uuid) {
        if (StringUtil.isNullOrBlank(uuid))
            return new ArrayList<>();
        return getSqlSession().selectList(generateStatement(MAPPER_QUERYITEMS), uuid);
    }

    @Override
    public void insertItems(List<ReturnSupplierBillItem> items) {
        Assert.assertArgumentNotNull(items, "items");
        for (ReturnSupplierBillItem item : items) {
            getSqlSession().insert(generateStatement(MAPPER_INSERTITEMS), item);
        }
    }

}
