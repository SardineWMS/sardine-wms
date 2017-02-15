/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	WrhDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月13日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.bin.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.bin.Path;
import com.hd123.sardine.wms.common.dao.NameSpaceSupport;
import com.hd123.sardine.wms.dao.basicInfo.bin.PathDao;

/**
 * 货道Dao实现类
 * 
 * @author zhangsai
 *
 */
public class PathDaoImpl extends NameSpaceSupport implements PathDao {

  private static final String GETBYCODE = "getByCode";

  @Override
  public void insert(Path path) {
    getSqlSession().insert(generateStatement(MAPPER_INSERT), path);
  }

  @Override
  public List<Path> query(String companyUuid, String zoneUuid) {
    if (StringUtil.isNullOrBlank(companyUuid) || StringUtil.isNullOrBlank(zoneUuid))
      return new ArrayList<Path>();

    Map<String, String> map = new HashMap<String, String>();
    map.put("companyUuid", companyUuid);
    map.put("zoneUuid", zoneUuid);
    List<Path> paths = getSqlSession().selectList(generateStatement(MAPPER_QUERY_BYLIST), map);
    return paths;
  }

  @Override
  public Path getByCode(String companyUuid, String code) {
    if (StringUtil.isNullOrBlank(companyUuid) || StringUtil.isNullOrBlank(code))
      return null;

    Map<String, String> map = new HashMap<String, String>();
    map.put("companyUuid", companyUuid);
    map.put("code", code);
    return getSqlSession().selectOne(generateStatement(GETBYCODE), map);
  }

  @Override
  public Path get(String uuid, String companyUuid) {
    if (StringUtil.isNullOrBlank(companyUuid) || StringUtil.isNullOrBlank(uuid))
      return null;

    Map<String, String> map = new HashMap<String, String>();
    map.put("companyUuid", companyUuid);
    map.put("uuid", uuid);
    return getSqlSession().selectOne(generateStatement(MAPPER_GET), map);
  }
}
