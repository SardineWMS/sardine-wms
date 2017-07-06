/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	ReturnBillDao.java
 * 模块说明：	
 * 修改历史：
 * 2017年7月6日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.dao.rtn.customerreturn;

import java.util.List;

import com.hd123.sardine.wms.api.rtn.customerreturn.ReturnBill;
import com.hd123.sardine.wms.api.rtn.customerreturn.ReturnBillItem;
import com.hd123.sardine.wms.common.dao.BaseDao;

/**
 * @author yangwenzhu
 *
 */
public interface ReturnBillDao extends BaseDao<ReturnBill> {

    void insertItems(List<ReturnBillItem> items);
}
