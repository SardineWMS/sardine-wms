/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	WaveBillDao.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月21日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.dao.out.wave;

import java.util.List;

import com.hd123.sardine.wms.api.out.wave.WaveBill;
import com.hd123.sardine.wms.api.out.wave.WaveBillItem;
import com.hd123.sardine.wms.common.dao.BaseDao;

/**
 * @author yangwenzhu
 *
 */
public interface WaveBillDao extends BaseDao<WaveBill> {

    WaveBill getByBillNumber(String billNumber);

    void insertItems(List<WaveBillItem> items);

    void removeItems(String waveBillUuid);

    List<WaveBillItem> getItemsByWaveBillUuid(String waveBillUuid);
}
