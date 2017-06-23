/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	ReasonConfigDao.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月21日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.config.reasonconfig;

import java.util.List;

import com.hd123.sardine.wms.api.basicInfo.config.reasonconfig.ReasonType;

/**
 * @author fanqingqing
 *
 */
public interface ReasonConfigDao {
    void insert(ReasonType reasonType, String reason);

    void remove(ReasonType reasonType);

    List<String> query(ReasonType reasonType);

}
