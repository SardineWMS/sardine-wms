/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * <p/>
 * 文件名：	EntityNotNullValidator.java
 * 模块说明：
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.validator.routines;

import java.text.MessageFormat;

import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.hd123.sardine.wms.common.validator.GenericBeanValidator;
import com.hd123.sardine.wms.common.validator.errmsg.EntityNotFoundErrorMsg;
import com.hd123.sardine.wms.common.validator.errmsg.IllegalArgumentErrorMsg;

/**
 * 对象不能为空检验，且构造出{@link EntityNotFoundErrorMsg}
 * <p>
 * 检验内容有：
 * <li>对象不能空</li>
 * </p>
 *
 * @author zhangsai
 */
public class EntityNotNullValidator extends GenericBeanValidator<Object> {

    @Override
    public boolean doValidate(ValidatorContext context, Object obj) {
        if (obj == null) {
            context.addError(IllegalArgumentErrorMsg
                    .create(MessageFormat.format("{0}不能为空", getFieldCaption())));
            return false;
        }

        return true;
    }

    @Override
    protected String getDefaultCaption() {
        return DEFAULT_FIELD;
    }
}
