/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	NameSpaceSupport.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月12日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.dao;

import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;

/**
 * 提供基本的myBatis查询语句定义
 * 
 * @author zhangsai
 *
 */
public class NameSpaceSupport extends SqlSessionDaoSupport {
  public static final String MAPPER_GET = "get";
  public static final String MAPPER_QUERY_BYPAGE = "queryByPage";
  public static final String MAPPER_INSERT = "insert";
  public static final String MAPPER_UPDATE = "update";
  public static final String MAPPER_DELETE_BYHEADER = "deleteByHeader";
  public static final String MAPPER_REMOVE = "remove";
  public static final String MAPPER_QUERY_BYLIST = "queryByList";
  public static final String MAPPER_INSERT_ITEM = "insertItem";

  public String generateStatement(String mapperId) {
    return this.getClass().getName() + "." + mapperId;
  }

  public int insert(String mapperId, Object paramter) {
    return getSqlSession().insert(generateStatement(mapperId), paramter);
  }

  public int update(String mapperId, Object paramter) {
    return getSqlSession().update(generateStatement(mapperId), paramter);
  }

  public <E> List<E> selectList(String mapperId, Object paramter) {
    return getSqlSession().selectList(generateStatement(mapperId), paramter);
  }

  public <T> T selectOne(String mapperId, Object paramter) {
    return getSqlSession().selectOne(generateStatement(mapperId), paramter);
  }

  public int delete(String mapperId, Object paramter) {
    return getSqlSession().delete(generateStatement(mapperId), paramter);
  }
}
