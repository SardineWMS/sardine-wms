/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * <p/>
 * 文件名：	NullValidator.java
 * 模块说明：
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.validator.routines;

import java.text.MessageFormat;

import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.hd123.sardine.wms.common.entity.IsEntity;
import com.hd123.sardine.wms.common.validator.GenericBeanValidator;
import com.hd123.sardine.wms.common.validator.errmsg.IllegalArgumentErrorMsg;

/**
 * 对象为空检验
 * <p>
 * 检验内容有：
 * <li>对象必须空或者对象不为空且uuid与被校验uuid一致</li>
 * </p>
 *
 * @author zhangsai
 */
public class NullValidator extends GenericBeanValidator<IsEntity> {

    public static final String KEY_CURRENTOPERATOR_UUID = "key_currentoperator_uuid";

    @Override
    public boolean doValidate(ValidatorContext context, IsEntity obj) {
        if (obj != null
                && obj.getUuid().equals(context.getAttribute(KEY_CURRENTOPERATOR_UUID)) == false) {
            context.addError(IllegalArgumentErrorMsg
                    .create(MessageFormat.format("{0}已存在", getFieldCaption())));
            return false;
        }

        return true;
    }

    @Override
    protected String getDefaultCaption() {
        return DEFAULT_FIELD;
    }
}
