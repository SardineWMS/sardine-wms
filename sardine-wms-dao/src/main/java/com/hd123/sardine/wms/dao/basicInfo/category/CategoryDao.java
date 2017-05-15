/**
 * 版权所有(C)，LHWMS项目组，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	CategoryDao.java
 * 模块说明：	
 * 修改历史：
 * 2017年1月12日 - Jing - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.category;

import java.util.List;

import com.hd123.sardine.wms.api.basicInfo.category.Category;

/**
 * @author Jing
 *
 */
public interface CategoryDao {
    Category getByCode(String code);

    Category get(String uuid);

    List<Category> getRootCategorys();

    List<Category> getLowerCategorys(String categoryUuid);

    void insert(Category category);

    void update(Category category);

    void remove(String categoryUuid, long version);
}
