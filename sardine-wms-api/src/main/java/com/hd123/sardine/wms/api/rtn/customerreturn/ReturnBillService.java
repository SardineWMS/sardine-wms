/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ReturnBillService.java
 * 模块说明：	
 * 修改历史：
 * 2017年7月5日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.rtn.customerreturn;

import java.text.ParseException;

import com.hd123.sardine.wms.common.exception.WMSException;

/**
 * @author yangwenzhu
 *
 */
public interface ReturnBillService {

    /**
     * 新增退仓单
     * 
     * @param bill
     *            退仓单,not null
     * @return 新增退仓单UUID
     * @throws IllegalArgumentException
     * @throws WMSException
     * @throws ParseException
     */
    String saveNew(ReturnBill bill) throws WMSException, ParseException;

    /**
     * 编辑退仓单
     * 
     * @param bill
     *            退仓单，not null
     * @throws IllegalArgumentException
     * @throws VersionConflictException
     * @throws WMSException
     */
    void saveModify(ReturnBill bill) throws WMSException;

    /**
     * 删除退仓单
     * 
     * @param uuid
     *            uuid,not null
     * @param version
     *            版本号，not null
     * @throws IllegalArgumentException
     * @throws VersionConflictException
     * @throws WMSException
     */
    void remove(String uuid, long version) throws WMSException;

    /**
     * 根据UUID获取退仓单
     * 
     * @param uuid
     *            uuid,如为空，则返回null
     * @return 退仓单实体
     */
    ReturnBill get(String uuid);
}
