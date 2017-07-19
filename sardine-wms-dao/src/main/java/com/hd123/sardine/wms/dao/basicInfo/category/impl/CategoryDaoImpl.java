/**
 * 版权所有(C)，LHWMS项目组，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	CategoryDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年1月12日 - Jing - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.category.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.basicInfo.category.Category;
import com.hd123.sardine.wms.common.dao.NameSpaceSupport;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.dao.basicInfo.category.CategoryDao;

/**
 * @author Jing
 *
 */
public class CategoryDaoImpl extends NameSpaceSupport implements CategoryDao {
  public static final String GETBYCODE = "getByCode";
  public static final String GETROOTCATEGORYS = "getRootCategorys";
  public static final String GETLOWERCATEGORYS = "getLowerCategorys";
  public static final String REMOVECATEGORY = "removeCategory";
  public static final String QUERYLASTLOWER = "queryLastLower";
  public static final String GETPARENTUUID = "getParentUuid";

  @Override
  public Category getByCode(String code) {
    Assert.assertArgumentNotNull(code, "code");

    Map<String, Object> map = ApplicationContextUtil.mapWithParentCompanyUuid();
    map.put("code", code);
    return getSqlSession().selectOne(generateStatement(GETBYCODE), map);
  }

  @Override
  public Category get(String uuid) {
    Assert.assertArgumentNotNull(uuid, "uuid");
    return getSqlSession().selectOne(generateStatement(MAPPER_GET), uuid);
  }

  @Override
  public List<Category> getRootCategorys() {
    String companyUuid = ApplicationContextUtil.getParentCompanyUuid();
    return getSqlSession().selectList(generateStatement(GETROOTCATEGORYS), companyUuid);
  }

  @Override
  public List<Category> getLowerCategorys(String categoryUuid) {
    Assert.assertArgumentNotNull(categoryUuid, "categoryUuid");
    return getSqlSession().selectList(generateStatement(GETLOWERCATEGORYS), categoryUuid);
  }

  @Override
  public void insert(Category category) {
    getSqlSession().insert(generateStatement(MAPPER_INSERT), category);
  }

  @Override
  public void update(Category category) {
    getSqlSession().update(generateStatement(MAPPER_UPDATE), category);
  }

  @Override
  public void remove(String categoryUuid, long version) {
    Assert.assertArgumentNotNull(version, "version");
    Assert.assertArgumentNotNull(categoryUuid, "categoryUuid");

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("uuid", categoryUuid);
    map.put("version", version);
    getSqlSession().delete(REMOVECATEGORY, map);
  }

  @Override
  public List<Category> queryLastLower(PageQueryDefinition definition) {
    Assert.assertArgumentNotNull(definition, "definition");

    return getSqlSession().selectList(generateStatement(QUERYLASTLOWER), definition);
  }

  @Override
  public String getParentUuid(String uuid) {
    return selectOne(GETPARENTUUID, uuid);
  }
}
