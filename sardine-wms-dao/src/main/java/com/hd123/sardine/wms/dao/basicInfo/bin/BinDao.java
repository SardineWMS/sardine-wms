/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	BinDao.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月13日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.bin;

import java.util.List;

import com.hd123.sardine.wms.api.basicInfo.bin.Bin;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;

/**
 * 货位Dao接口
 * 
 * @author zhangsai
 *
 */
public interface BinDao {

  void remove(String uuid, long version);

  void insert(Bin bin);

  List<Bin> query(PageQueryDefinition definition);

  Bin getByCode(String companyUuid, String code);

  Bin get(String uuid, String companyUuid);
}
