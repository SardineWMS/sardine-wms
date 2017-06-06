/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	AlCNtcBillDao.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月1日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.dao.out.alcntc;

import java.util.List;

import com.hd123.sardine.wms.api.out.alcntc.AlcNtcBill;
import com.hd123.sardine.wms.api.out.alcntc.AlcNtcBillItem;
import com.hd123.sardine.wms.common.dao.BaseDao;

/**
 * @author yangwenzhu
 *
 */
public interface AlcNtcBillDao extends BaseDao<AlcNtcBill> {
    AlcNtcBill getByBillNumber(String billNumber);

    void insertItems(List<AlcNtcBillItem> items);

    void removeItems(String alcNtcBillUuid);

    List<AlcNtcBillItem> queryItems(String alcNtcBillUuid);

    AlcNtcBillItem getItemByUuid(String itemUuid);

    void updateItem(AlcNtcBillItem item);

    AlcNtcBill getByItemUuid(String itemUuid);
}
