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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.bin.Bin;
import com.hd123.sardine.wms.common.dao.NameSpaceSupport;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.dao.basicInfo.bin.BinDao;

/**
 * 货位Dao实现类
 * 
 * @author zhangsai
 *
 */
public class BinDaoImpl extends NameSpaceSupport implements BinDao {

  private static final String GETBYCODE = "getByCode";

  @Override
  public void insert(Bin bin) {
    getSqlSession().insert(generateStatement(MAPPER_INSERT), bin);
  }

  @Override
  public List<Bin> query(PageQueryDefinition definition) {
    return getSqlSession().selectList(generateStatement(MAPPER_QUERY_BYPAGE), definition);
  }

  @Override
  public void remove(String uuid, long version) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("uuid", uuid);
    map.put("version", version);
    int i = getSqlSession().delete(generateStatement(MAPPER_REMOVE), map);
    PersistenceUtils.optimisticVerify(i);
  }

  @Override
  public Bin getByCode(String companyUuid, String code) {
    if (StringUtil.isNullOrBlank(companyUuid) || StringUtil.isNullOrBlank(code))
      return null;

    Map<String, String> map = new HashMap<String, String>();
    map.put("companyUuid", companyUuid);
    map.put("code", code);
    return getSqlSession().selectOne(generateStatement(GETBYCODE), map);
  }
  
  @Override
  public Bin get(String uuid, String companyUuid) {
    if (StringUtil.isNullOrBlank(companyUuid) || StringUtil.isNullOrBlank(uuid))
      return null;

    Map<String, String> map = new HashMap<String, String>();
    map.put("companyUuid", companyUuid);
    map.put("uuid", uuid);
    return getSqlSession().selectOne(generateStatement(MAPPER_GET), map);
  }
}
