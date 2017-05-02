/**
 * 版权所有(C)，LHWMS项目组，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	SequenceDao.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月7日 - Jing - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.seq;

/**
 * @author Jing
 *
 */
public interface SequenceDao {

    int getCurrentValue(String seqName, String companyUuid);

    int getNextValue(String seqName, String companyUuid);
    
    int getCurrentValueWithDate(String seqName, String companyUuid);

    int getNextValueWithDate(String seqName, String companyUuid);
}
