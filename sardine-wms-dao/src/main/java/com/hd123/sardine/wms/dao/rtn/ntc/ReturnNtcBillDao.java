/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	ReturnNtcBillDao.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月28日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.dao.rtn.ntc;

import java.util.List;

import com.hd123.sardine.wms.api.rtn.ntc.ReturnNtcBill;
import com.hd123.sardine.wms.api.rtn.ntc.ReturnNtcBillItem;
import com.hd123.sardine.wms.common.dao.BaseDao;

/**
 * @author yangwenzhu
 *
 */
public interface ReturnNtcBillDao extends BaseDao<ReturnNtcBill> {

    void insertItems(List<ReturnNtcBillItem> items);

    ReturnNtcBill getByBillNumber(String billNumber);

    void removeItems(String uuid);

    List<ReturnNtcBillItem> getItems(String uuid);

    ReturnNtcBillItem getItemByUuid(String itemUuid);

}
