/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * <p/>
 * 文件名：	StringLengthValidator.java
 * 模块说明：
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.validator.routines;

import java.text.MessageFormat;

import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.common.validator.GenericBeanValidator;
import com.hd123.sardine.wms.common.validator.errmsg.IllegalArgumentErrorMsg;

/**
 * 字符串长度校验
 *
 * @author zhangsai
 */
public class StringLengthValidator extends GenericBeanValidator<String> {

    // 最小长度
    private int minLength;
    // 最大长度
    private int maxLength;

    public StringLengthValidator() {
        super();
    }

    public StringLengthValidator(int minLength, int maxLength) {
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    @Override
    public boolean doValidate(ValidatorContext context, String value) {
        // 如果字符串为空并且最小长度小于等于0，则认为合法
        if (StringUtil.isNullOrBlank(value) && minLength <= 0)
            return true;

        if (minLength > 0 && value.length() < minLength) {
            context.addError(IllegalArgumentErrorMsg.create(MessageFormat.format("{0}长度不能小于{1}", getFieldCaption(), minLength)));
            return false;
        }

        if (maxLength > 0 && value.length() > maxLength) {
            context.addError(IllegalArgumentErrorMsg.create(MessageFormat.format("{0}长度不能大于{1}", getFieldCaption(), maxLength)));
            return false;
        }

        return true;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
}
