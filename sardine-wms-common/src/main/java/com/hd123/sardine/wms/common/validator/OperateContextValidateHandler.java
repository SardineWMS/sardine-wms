/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	OperateContextValidateHandler.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月12日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.validator;

import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.unbiz.fluentvalidator.Validator;
import com.hd123.sardine.wms.common.entity.OperateContext;

/**
 * 操作人信息校验器
 * 
 * @author zhangsai
 *
 */
public class OperateContextValidateHandler extends GenericValidateHandler<OperateContext> {

    @Autowired
    private Validator<ValidateBean<OperateContext>> operateContextValidator;

    @Override
    protected void prepareValidators(OperateContext bean) {
        on(bean, operateContextValidator);
    }
}
