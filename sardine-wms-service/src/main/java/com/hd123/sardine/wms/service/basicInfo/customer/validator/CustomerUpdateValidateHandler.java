/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	CustomerUpdateValidateHandler.java
 * 模块说明：	
 * 修改历史：
 * 2017年1月13日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.service.basicInfo.customer.validator;

import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.unbiz.fluentvalidator.Validator;
import com.hd123.sardine.wms.api.basicInfo.customer.Customer;
import com.hd123.sardine.wms.common.entity.IsEntity;
import com.hd123.sardine.wms.common.validator.GenericValidateHandler;
import com.hd123.sardine.wms.common.validator.ValidateBean;

/**
 * @author yangwenzhu
 *
 */
public class CustomerUpdateValidateHandler extends GenericValidateHandler<Customer> {
    public static final String KEY_CODEEXISTS_CUSTOMER = "codeExists_customer";

    @Autowired
    private Validator<ValidateBean<Object>> notNullValidator;

    @Autowired
    private Validator<ValidateBean<IsEntity>> nullValidator;

    @Autowired
    private Validator<ValidateBean<String>> length30Validator;

    @Autowired
    private Validator<ValidateBean<String>> length100Validator;

    @Override
    protected void prepareValidators(Customer bean) {
        on("客户", bean, notNullValidator);
        on("客户代码", bean.getCode(), notNullValidator);
        on("客户代码", bean.getCode(), length30Validator);
        on("客户名称", bean.getName(), notNullValidator);
        on("客户名称", bean.getName(), length100Validator);
        on("联系方式", bean.getPhone(), notNullValidator);
        on("联系方式", bean.getPhone(), length100Validator);
        on("地址", bean.getAddress(), notNullValidator);
        on("地址", bean.getAddress(), length100Validator);
        on("状态", bean.getState(), notNullValidator);
        on("组织ID", bean.getCompanyUuid(), notNullValidator);
        on("组织ID", bean.getCompanyUuid(), length30Validator);

        Customer customer = (Customer) getAttribute(KEY_CODEEXISTS_CUSTOMER);
        on("客户代码" + bean.getCode(), customer, nullValidator);

    }

}
