/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	DecIncInvBillDao.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月17日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.dao.inner.decincinv;

import java.util.List;

import com.hd123.sardine.wms.api.inner.decincinv.DecIncInvBill;
import com.hd123.sardine.wms.api.inner.decincinv.DecIncInvBillItem;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;

/**
 * @author fanqingqing
 *
 */
public interface DecIncInvBillDao {

    DecIncInvBill get(String uuid);
    
    DecIncInvBill getByBillNumber(String billNumber);
    
    List<DecIncInvBill> query(PageQueryDefinition definition);
    
    void insert(DecIncInvBill billNumber);

    void update(DecIncInvBill billNumber);

    void remove(String uuid, long version);
    
    void removeItems(String billUuid);

    void insertItems(List<DecIncInvBillItem> items);

    List<DecIncInvBillItem> queryItems(String billUuid);
}
