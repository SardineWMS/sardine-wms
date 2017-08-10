/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	AcceptanceBillDao.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月5日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.dao.out.acceptance;

import java.util.List;

import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBill;
import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBillItem;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;

/**
 * @author fanqingqing
 *
 */
public interface AcceptanceBillDao {

    AcceptanceBill get(String uuid);

    AcceptanceBill getByBillNumber(String billNumber);

    List<AcceptanceBill> query(PageQueryDefinition definition);

    void insert(AcceptanceBill acceptanceBill);

    void update(AcceptanceBill acceptanceBill);

    void remove(String uuid, long version);

    void insertItems(List<AcceptanceBillItem> items);

    void removeItems(String acceptanceBillUuid);

    List<AcceptanceBillItem> queryItems(String acceptanceBillUuid);
    
    void updateItem(AcceptanceBillItem item);
}
