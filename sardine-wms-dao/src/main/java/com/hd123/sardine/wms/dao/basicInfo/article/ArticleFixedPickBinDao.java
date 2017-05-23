/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	ArticleFixedPickBinDao.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月23日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.article;

/**
 * @author fanqingqing
 *
 */
public interface ArticleFixedPickBinDao {
    
    void insert(String articleUuid, String fixedPickBin);

    void removeByArticle(String articleUuid);
    
    void removeByFixedPickBin(String fixedPickBin);
    
    void removeByArticleCompany(String articleUuid);
    
    String getFixedPickBin(String articleUuid);
}
