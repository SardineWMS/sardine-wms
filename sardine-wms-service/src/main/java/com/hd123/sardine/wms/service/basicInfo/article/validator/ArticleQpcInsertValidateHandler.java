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
import com.hd123.sardine.wms.api.basicInfo.article.ArticleQpc;
import com.hd123.sardine.wms.common.validator.GenericValidateHandler;
import com.hd123.sardine.wms.common.validator.ValidateBean;

/**
 * 新增商品规格校验器
 * <p>
 * <ul>
 * <li>商品规格基本信息不能为空</li>
 * </ul>
 * 
 * @author zhangsai
 *
 */
public class ArticleQpcInsertValidateHandler extends GenericValidateHandler<ArticleQpc> {

  @Autowired
  private Validator<ValidateBean<Object>> notNullValidator;

  @Override
  protected void prepareValidators(ArticleQpc bean) {
    on("商品规格", bean, notNullValidator);
    on("uuid", bean.getUuid(), notNullValidator);
    on("qpcStr", bean.getQpcStr(), notNullValidator);
    on("length", bean.getLength(), notNullValidator);
    on("width", bean.getWidth(), notNullValidator);
    on("height", bean.getHeight(), notNullValidator);
    on("weight", bean.getWeight(), notNullValidator);
  }
}
