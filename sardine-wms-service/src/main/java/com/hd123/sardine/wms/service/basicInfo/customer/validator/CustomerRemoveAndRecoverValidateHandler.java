/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	CustomerRemoveAndRecoverValidateHandler.java
 * 模块说明：	
 * 修改历史：
 * 2017年1月13日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.service.basicInfo.customer.validator;

import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.unbiz.fluentvalidator.Validator;
import com.hd123.sardine.wms.api.basicInfo.customer.Customer;
import com.hd123.sardine.wms.common.entity.HasVersion;
import com.hd123.sardine.wms.common.validator.GenericValidateHandler;
import com.hd123.sardine.wms.common.validator.ValidateBean;

/**
 * @author yangwenzhu
 *
 */
public class CustomerRemoveAndRecoverValidateHandler extends GenericValidateHandler<Customer> {
    public static final String KEY_OPERATOR_CUSTOMER = "operator_customer";
    @Autowired
    private Validator<ValidateBean<Object>> notNullValidator;

    @Autowired
    private Validator<ValidateBean<Object>> entityNotNullValidator;

    @Autowired
    private Validator<ValidateBean<HasVersion>> versionValidator;

    @Override
    protected void prepareValidators(Customer bean) {
        on("客户", bean, notNullValidator);

        Customer customer = (Customer) getAttribute(KEY_OPERATOR_CUSTOMER);
        on("操作的客户", customer, entityNotNullValidator);

        on(customer, versionValidator);
    }

}
