/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	ContainerTypeSaveModifyValidator.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月7日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.service.basicInfo.containertype.validator;

import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.unbiz.fluentvalidator.Validator;
import com.hd123.sardine.wms.api.basicInfo.containertype.ContainerType;
import com.hd123.sardine.wms.common.validator.GenericValidateHandler;
import com.hd123.sardine.wms.common.validator.ValidateBean;

/**
 * @author fanqingqing
 *
 */
public class ContainerTypeSaveModifyValidateHandler extends GenericValidateHandler<ContainerType> {

    @Autowired
    private Validator<ValidateBean<Object>> notNullValidator;

    @Autowired
    private Validator<ValidateBean<String>> length30Validator;

    @Autowired
    private Validator<ValidateBean<String>> length100Validator;

    @Override
    protected void prepareValidators(ContainerType bean) {
        on("容器类型", bean, notNullValidator);
        on("容器类型uuid", bean.getUuid(), notNullValidator);
        on("容器类型代码", bean.getCode(), notNullValidator);
        on("容器类型代码", bean.getCode(), length30Validator);
        on("容器类型名称", bean.getName(), notNullValidator);
        on("容器类型名称", bean.getName(), length100Validator);
        on("条码前缀", bean.getBarCodePrefix(), notNullValidator);
    }
}
