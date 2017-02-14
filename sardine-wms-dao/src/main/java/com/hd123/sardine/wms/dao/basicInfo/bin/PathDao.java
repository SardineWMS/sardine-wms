/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	PathDao.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月13日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.bin;

import java.util.List;

import com.hd123.sardine.wms.api.basicInfo.bin.Path;

/**
 * 货道Dao 接口
 * 
 * @author zhangsai
 *
 */
public interface PathDao {

  void insert(Path path);

  List<Path> query(String companyUuid, String zoneUuid);
  
  Path getByCode(String companyUuid, String code);
  
  Path get(String uuid, String companyUuid);
}
