/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	ArticleQpcDao.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月7日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.article;

import java.util.List;

import com.hd123.sardine.wms.api.basicInfo.article.ArticleQpc;
import com.hd123.sardine.wms.common.dao.BaseDao;

/**
 * @author zhangsai
 *
 */
public interface ArticleQpcDao extends BaseDao<ArticleQpc> {

  List<ArticleQpc> queryByList(String articleUuid);

  void setDefault(String uuid);
  
  void setUnDefault(String articleUuid);

  void remove(String uuid);

  ArticleQpc getByQpcStr(String articleUuid, String qpcStr);
}
