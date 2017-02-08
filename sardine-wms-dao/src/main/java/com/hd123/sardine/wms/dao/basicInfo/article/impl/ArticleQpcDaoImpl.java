/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	ArticleQpcDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月7日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.article.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.article.ArticleQpc;
import com.hd123.sardine.wms.common.dao.impl.BaseDaoImpl;
import com.hd123.sardine.wms.dao.basicInfo.article.ArticleQpcDao;

/**
 * @author zhangsai
 *
 */
public class ArticleQpcDaoImpl extends BaseDaoImpl<ArticleQpc> implements ArticleQpcDao {

  private static final String MAPPER_QUERYBYLIST = "queryByList";
  private static final String MAPPER_SETDEFAULT = "setDefault";
  private static final String MAPPER_SETUNDEFAULT = "setUnDefault";
  private static final String MAPPER_REMOVEONE = "removeone";
  private static final String MAPPER_GETBYQPCSTR = "getByQpcStr";

  @Override
  public List<ArticleQpc> queryByList(String articleUuid) {
    if (StringUtil.isNullOrBlank(articleUuid))
      return new ArrayList<ArticleQpc>();

    return getSqlSession().selectList(generateStatement(MAPPER_QUERYBYLIST), articleUuid);
  }

  @Override
  public void setDefault(String uuid) {
    if (StringUtil.isNullOrBlank(uuid))
      return;

    getSqlSession().update(generateStatement(MAPPER_SETDEFAULT), uuid);
  }

  @Override
  public void remove(String uuid) {
    if (StringUtil.isNullOrBlank(uuid))
      return;

    getSqlSession().delete(generateStatement(MAPPER_REMOVEONE), uuid);
  }

  @Override
  public ArticleQpc getByQpcStr(String articleUuid, String qpcStr) {
    if (StringUtil.isNullOrBlank(articleUuid) || StringUtil.isNullOrBlank(qpcStr))
      return null;

    Map<String, String> map = new HashMap<String, String>();
    map.put("articleUuid", articleUuid);
    map.put("qpcStr", qpcStr);
    return getSqlSession().selectOne(generateStatement(MAPPER_GETBYQPCSTR), map);
  }

  @Override
  public void setUnDefault(String articleUuid) {
    if (StringUtil.isNullOrBlank(articleUuid))
      return;

    getSqlSession().update(generateStatement(MAPPER_SETUNDEFAULT), articleUuid);
  }
}
