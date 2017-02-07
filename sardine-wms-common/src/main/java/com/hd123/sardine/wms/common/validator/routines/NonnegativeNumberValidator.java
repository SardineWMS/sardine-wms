/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	PositiveIntegerValidator.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月7日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.common.validator.routines;

import java.text.MessageFormat;

import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.hd123.sardine.wms.common.validator.GenericBeanValidator;
import com.hd123.sardine.wms.common.validator.errmsg.IllegalArgumentErrorMsg;

/**
 * @author yangwenzhu
 *
 */
public class NonnegativeNumberValidator extends GenericBeanValidator<Object> {

    @Override
    protected boolean doValidate(ValidatorContext context, Object bean) {
        String regex = "^[0-9][0-9]?[0-9]*(\\.[0-9]+)?$";
        if (!bean.toString().matches(regex)) {
            context.addError(IllegalArgumentErrorMsg
                    .create(MessageFormat.format("{0}必须输入非负实数", getFieldCaption())));
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        String regex = "[0-9][0-9]?[0-9]*(\\.[0-9]+)?$";
        String test = "0.0";
        System.out.println(test.matches(regex));
    }

}
