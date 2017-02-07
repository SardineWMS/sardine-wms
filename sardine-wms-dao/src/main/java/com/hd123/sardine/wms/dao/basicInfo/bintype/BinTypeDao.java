/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	BinTypeDao.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月6日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.bintype;

import com.hd123.sardine.wms.api.basicInfo.bintype.BinType;
import com.hd123.sardine.wms.common.dao.BaseDao;

/**
 * @author yangwenzhu
 *
 */
public interface BinTypeDao extends BaseDao<BinType> {
    BinType getByCode(String code);
}
