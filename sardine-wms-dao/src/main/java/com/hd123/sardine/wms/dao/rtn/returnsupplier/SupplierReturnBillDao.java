/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	SupplierReturnBillDao.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月21日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.dao.rtn.returnsupplier;

import java.util.List;

import com.hd123.sardine.wms.api.rtn.returnsupplier.ReturnSupplierBill;
import com.hd123.sardine.wms.api.rtn.returnsupplier.ReturnSupplierBillItem;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;

/**
 * @author fanqingqing
 *
 */
public interface SupplierReturnBillDao {
    ReturnSupplierBill get(String uuid);

    ReturnSupplierBill getByBillNumber(String billNumber);

    List<ReturnSupplierBill> query(PageQueryDefinition definition);

    int insert(ReturnSupplierBill shipBill);

    int update(ReturnSupplierBill shipBill);

    List<ReturnSupplierBillItem> queryItems(String uuid);

    void insertItems(List<ReturnSupplierBillItem> items);

}
