/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	ArticleInsertValidateHandler.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月12日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.basicInfo.article.validator;

import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.unbiz.fluentvalidator.Validator;
import com.hd123.sardine.wms.api.basicInfo.article.Article;
import com.hd123.sardine.wms.api.basicInfo.category.Category;
import com.hd123.sardine.wms.common.entity.HasVersion;
import com.hd123.sardine.wms.common.entity.IsEntity;
import com.hd123.sardine.wms.common.validator.GenericValidateHandler;
import com.hd123.sardine.wms.common.validator.ValidateBean;

/**
 * 新增商品校验器
 * <p>
 * <ul>
 * <li>商品基本信息不能为空</li>
 * <li>待编辑的商品uuid不能为空，且商品必须存在</li>
 * <li>版本号一致</li>
 * <li>商品代码不能重复，需由调用者将根据新商品代码查询出的商品以key
 * {@link ArticleUpdateValidateHandler#KEY_CODEEXISTS_ARTICLE}传入</li>
 * </ul>
 * 
 * @author zhangsai
 *
 */
public class ArticleUpdateValidateHandler extends GenericValidateHandler<Article> {

  /** 调用该校验器的service需传入user对象 与当前注册用户代码一致的已存在用户 */
  public static final String KEY_CODEEXISTS_ARTICLE = "codeExists_article";

  /** 调用该校验器的service需传入商品所属类别对象 */
  public static final String KEY_CATEGORY = "category";

  /** 调用该校验器的service需传入当前待编辑的商品对象 */
  public static final String KEY_UPDATE_ARTICLE = "update_article";

  @Autowired
  private Validator<ValidateBean<Object>> notNullValidator;

  @Autowired
  private Validator<ValidateBean<Object>> entityNotNullValidator;

  @Autowired
  private Validator<ValidateBean<String>> length30Validator;

  @Autowired
  private Validator<ValidateBean<String>> length100Validator;

  @Autowired
  private Validator<ValidateBean<IsEntity>> nullValidator;

  @Autowired
  private Validator<ValidateBean<HasVersion>> versionValidator;

  @Override
  protected void prepareValidators(Article bean) {
    on("商品", bean, notNullValidator);
    on("商品uuid", bean.getUuid(), notNullValidator);
    on("商品状态", bean.getState(), notNullValidator);
    on("商品代码", bean.getCode(), notNullValidator);
    on("商品代码", bean.getCode(), length30Validator);
    on("商品名称", bean.getName(), notNullValidator);
    on("商品名称", bean.getName(), length100Validator);
    on("商品规格", bean.getSpec(), notNullValidator);
    on("商品规格", bean.getSpec(), length30Validator);
    on("组织id", bean.getCompanyUuid(), notNullValidator);
    on("商品类别", bean.getCategory(), notNullValidator);

    Article article = (Article) getAttribute(KEY_CODEEXISTS_ARTICLE);
    on("用户代码" + bean.getCode(), article, nullValidator);

    Category category = (Category) getAttribute(KEY_CATEGORY);
    on("类别", category, entityNotNullValidator);

    Article updateArticle = (Article) getAttribute(KEY_UPDATE_ARTICLE);
    on("编辑的商品", updateArticle, entityNotNullValidator);
    on(updateArticle, versionValidator);
  }
}
