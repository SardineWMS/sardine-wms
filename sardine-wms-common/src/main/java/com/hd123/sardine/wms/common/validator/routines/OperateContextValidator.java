/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * <p/>
 * 文件名：	OperateContextValidator.java
 * 模块说明：
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.validator.routines;

import java.text.MessageFormat;

import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.common.entity.OperateContext;
import com.hd123.sardine.wms.common.validator.GenericBeanValidator;
import com.hd123.sardine.wms.common.validator.errmsg.IllegalArgumentErrorMsg;

/**
 * 操作信息检验
 * <p>
 * 检验内容：
 * <li>对象不能为空</li>
 * <li>对象操作人id不能为空</li>
 * <li>对象操作人code不能为空</li>
 * <li>对象操作人fullName不能为空</li>
 * <li>对象时间不能为空</li>
 * </p>
 *
 * @author zhangsai
 */
public class OperateContextValidator extends GenericBeanValidator<OperateContext> {

    @Override
    public boolean doValidate(ValidatorContext context, OperateContext operCtx) {
        if (operCtx == null || operCtx.getOperator() == null
                || StringUtil.isNullOrBlank(operCtx.getOperator().getId())) {
            context.addError(IllegalArgumentErrorMsg
                    .create(MessageFormat.format("{0}不能为空", getFieldCaption())));
            return false;
        }

        if (StringUtil.isNullOrBlank(operCtx.getOperator().getCode())
                || StringUtil.isNullOrBlank(operCtx.getOperator().getFullName())) {
            context.addError(
                    IllegalArgumentErrorMsg.create(MessageFormat.format("{0}不能为空", "操作人代码名称")));
            return false;
        }

        if (operCtx.getTime() == null) {
            context.addError(
                    IllegalArgumentErrorMsg.create(MessageFormat.format("{0}不能为空", "操作人时间")));
            return false;
        }

        return true;
    }

    @Override
    protected String getDefaultCaption() {
        return "操作人信息";
    }
}
