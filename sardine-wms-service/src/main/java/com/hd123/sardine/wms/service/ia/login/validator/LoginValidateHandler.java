/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	LoginValidateHandler.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月24日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.ia.login.validator;

import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.unbiz.fluentvalidator.Validator;
import com.hd123.sardine.wms.common.validator.GenericValidateHandler;
import com.hd123.sardine.wms.common.validator.ValidateBean;

/**
 * 登录名不能为空
 * 
 * @author zhangsai
 *
 */
public class LoginValidateHandler extends GenericValidateHandler<String> {

    @Autowired
    private Validator<ValidateBean<Object>> notNullValidator;

    @Override
    protected void prepareValidators(String bean) {
        on("登录名", bean, notNullValidator);
    }
}
