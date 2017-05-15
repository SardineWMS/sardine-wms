/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	CompanyInsertValidateHandler.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月14日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.service.ia.company.validator;

import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.unbiz.fluentvalidator.Validator;
import com.hd123.sardine.wms.api.ia.user.Company;
import com.hd123.sardine.wms.common.entity.IsEntity;
import com.hd123.sardine.wms.common.validator.GenericValidateHandler;
import com.hd123.sardine.wms.common.validator.ValidateBean;

/**
 * @author yangwenzhu
 *
 */
public class CompanyInsertValidateHandler extends GenericValidateHandler<Company> {

    public static final String KEY_NAMEEXISTS_COMPANY = "nameExists_company";

    @Autowired
    private Validator<ValidateBean<Object>> notNullValidator;

    @Autowired
    private Validator<ValidateBean<String>> length100Validator;

    @Autowired
    private Validator<ValidateBean<IsEntity>> nullValidator;

    @Override
    protected void prepareValidators(Company bean) {
        on("企业信息", bean, notNullValidator);
        on("企业名称", bean.getName(), notNullValidator);
        on("企业名称", bean.getName(), length100Validator);
        on("企业地址", bean.getAddress(), length100Validator);
        on("主页", bean.getHomePage(), length100Validator);

        Company company = (Company) getAttribute(KEY_NAMEEXISTS_COMPANY);
        on("企业名称" + bean.getName(), company, nullValidator);
    }

}
