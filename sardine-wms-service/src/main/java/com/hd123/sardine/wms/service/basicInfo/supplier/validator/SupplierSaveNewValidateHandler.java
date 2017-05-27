/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	SupplierSaveNewValidateHandler.java
 * 模块说明：	
 * 修改历史：
 * 2017年1月10日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.service.basicInfo.supplier.validator;

import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.unbiz.fluentvalidator.Validator;
import com.hd123.sardine.wms.api.basicInfo.supplier.Supplier;
import com.hd123.sardine.wms.common.validator.GenericValidateHandler;
import com.hd123.sardine.wms.common.validator.ValidateBean;

/**
 * @author fanqingqing
 *
 */
public class SupplierSaveNewValidateHandler extends GenericValidateHandler<Supplier> {

    @Autowired
    private Validator<ValidateBean<Object>> notNullValidator;

    @Autowired
    private Validator<ValidateBean<String>> length30Validator;

    @Autowired
    private Validator<ValidateBean<String>> length100Validator;

    @Override
    protected void prepareValidators(Supplier bean) {
        on("供应商", bean, notNullValidator);
        on("供应商状态", bean.getState(), notNullValidator);
        on("供应商代码", bean.getCode(), notNullValidator);
        on("供应商代码", bean.getCode(), length30Validator);
        on("供应商名称", bean.getName(), notNullValidator);
        on("供应商名称", bean.getName(), length100Validator);
        on("供应商电话", bean.getPhone(), length30Validator);
        on("供应商地址", bean.getAddress(), length100Validator);
    }
}
