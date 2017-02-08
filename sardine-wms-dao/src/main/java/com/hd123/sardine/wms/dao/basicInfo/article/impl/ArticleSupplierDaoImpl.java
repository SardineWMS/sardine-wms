/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	ArticleSupplierDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月7日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.article.impl;

import java.util.ArrayList;
import java.util.List;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.article.ArticleSupplier;
import com.hd123.sardine.wms.common.dao.impl.BaseDaoImpl;
import com.hd123.sardine.wms.dao.basicInfo.article.ArticleSupplierDao;

/**
 * @author zhangsai
 *
 */
public class ArticleSupplierDaoImpl extends BaseDaoImpl<ArticleSupplier>
    implements ArticleSupplierDao {

  public static final String MAPPER_QUERYBYLIST = "queryByList";
  public static final String MAPPER_SETDEFAULT = "setDefault";
  private static final String MAPPER_SETUNDEFAULT = "setUnDefault";
  public static final String MAPPER_REMOVEONE = "removeone";

  @Override
  public List<ArticleSupplier> queryByList(String articleUuid) {
    if (StringUtil.isNullOrBlank(articleUuid))
      return new ArrayList<ArticleSupplier>();

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
  public void setUnDefault(String articleUuid) {
    if (StringUtil.isNullOrBlank(articleUuid))
      return;

    getSqlSession().update(generateStatement(MAPPER_SETUNDEFAULT), articleUuid);
  }
}
