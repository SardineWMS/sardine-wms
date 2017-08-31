/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	PickAreaDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月12日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.pickarea.impl;

import java.util.Map;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.pickarea.PickArea;
import com.hd123.sardine.wms.common.dao.impl.BaseDaoImpl;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.dao.basicInfo.pickarea.PickAreaDao;

/**
 * @author yangwenzhu
 *
 */
public class PickAreaDaoImpl extends BaseDaoImpl<PickArea> implements PickAreaDao {
  public static final String MAPPER_GETBYCODE = "getByCode";
  public static final String MAPPER_GET_BY_STORAGEAREA = "getByStorageArea";

  @Override
  public PickArea getByCode(String code) {
    if (StringUtil.isNullOrBlank(code))
      return null;
    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("code", code);
    return getSqlSession().selectOne(generateStatement(MAPPER_GETBYCODE), map);

  }

  @Override
  public PickArea getByStorageArea(String storageArea) {
    if (StringUtil.isNullOrBlank(storageArea))
      return null;
    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("storageArea", storageArea);
    return getSqlSession().selectOne(generateStatement(MAPPER_GET_BY_STORAGEAREA), map);
  }

}
