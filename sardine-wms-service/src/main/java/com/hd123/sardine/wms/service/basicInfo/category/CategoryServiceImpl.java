/**
 * 版权所有(C)，LHWMS项目组，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	CategoryServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年1月12日 - Jing - 创建。
 */
package com.hd123.sardine.wms.service.basicInfo.category;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.category.Category;
import com.hd123.sardine.wms.api.basicInfo.category.CategoryService;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.dao.basicInfo.category.CategoryDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.log.EntityLogger;

/**
 * @author Jing
 *
 */
public class CategoryServiceImpl extends BaseWMSService implements CategoryService {
  @Autowired
  private CategoryDao dao;

  @Autowired
  private EntityLogger logger;

  @Override
  public List<Category> getRootCategorys() {

    List<Category> categorys = dao.getRootCategorys();
    for (Category o : categorys) {
      o.setChildren(queryLowers(o.getUuid()));
    }
    return categorys;
  }

  private List<Category> queryLowers(String categoryUuid) {
    List<Category> categorys = dao.getLowerCategorys(categoryUuid);
    for (Category o : categorys) {
      o.setChildren(queryLowers(o.getUuid()));
    }
    return categorys;
  }

  @Override
  public List<Category> getLowerCategorys(String categoryUuid) {
    if (StringUtil.isNullOrBlank(categoryUuid))
      return new ArrayList<>();

    return dao.getLowerCategorys(categoryUuid);
  }

  @Override
  public Category get(String categoryUuid) {
    if (StringUtil.isNullOrBlank(categoryUuid))
      return null;

    return dao.get(categoryUuid);
  }

  @Override
  public Category getByCode(String categoryCode) {
    if (StringUtil.isNullOrBlank(categoryCode))
      return null;

    return dao.getByCode(categoryCode);
  }

  @Override
  public void saveNew(Category category) throws WMSException {
    Assert.assertArgumentNotNull(category, "category");
    Assert.assertArgumentNotNull(category.getCode(), "category.code");
    Assert.assertArgumentNotNull(category.getName(), "category.name");

    if (Category.DEFAULT_ROOTCATEGORY.equals(category.getUpperCategory()) == false) {
      Category upperCategory = dao.get(category.getUpperCategory());
      if (upperCategory == null)
        throw new WMSException("上级类别不存在，无法保存。");
    }

    Category tCategory = dao.getByCode(category.getCode());
    if (tCategory != null)
      throw new WMSException("类别代码“" + category.getCode() + "”已经存在。");

    category.setUuid(UUIDGenerator.genUUID());
    category.setCompanyUuid(ApplicationContextUtil.getParentCompanyUuid());
    category.setCreateInfo(ApplicationContextUtil.getOperateInfo());
    category.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    dao.insert(category);

    logger.injectContext(this, category.getUuid(), Category.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_ADDNEW, "新增商品类别");
  }

  @Override
  public void saveModify(Category category) throws WMSException {
    Assert.assertArgumentNotNull(category, "category");
    Assert.assertArgumentNotNull(category.getCode(), "category.code");
    Assert.assertArgumentNotNull(category.getName(), "category.name");
    Assert.assertArgumentNotNull(category.getCompanyUuid(), "category.companyUuid");

    Category tCategory = dao.getByCode(category.getCode());
    if (tCategory != null && tCategory.getUuid().equals(category.getUuid()) == false)
      throw new WMSException("类别代码“" + category.getCode() + "”已经存在。");

    category.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    dao.update(category);

    logger.injectContext(this, category.getUuid(), Category.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "修改商品类别");
  }

  @Override
  public void remove(String uuid, long verison) {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(verison, "verison");

    dao.remove(uuid, verison);

    logger.injectContext(this, uuid, Category.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_REMOVE, "删除商品类别");
  }

  @Override
  public PageQueryResult<Category> queryLastLower(PageQueryDefinition definition) {
    Assert.assertArgumentNotNull(definition, "definition");

    definition.setCompanyUuid(ApplicationContextUtil.getParentCompanyUuid());
    PageQueryResult<Category> pgr = new PageQueryResult<Category>();
    List<Category> list = dao.queryLastLower(definition);
    PageQueryUtil.assignPageInfo(pgr, definition);
    pgr.setRecords(list);
    return pgr;
  }
}
