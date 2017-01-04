/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * <p/>
 * 文件名：	GenericBeanValidator.java
 * 模块说明：
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.validator;

import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.hd123.rumba.commons.lang.StringUtil;

/**
 * 使用通用验证中间对象的验证器
 *
 * @author zhangsai
 */
public abstract class GenericBeanValidator<V> extends GenericValidator<ValidateBean<V>> {

    /**
     * 验证对象标题
     */
    private String fieldCaption;

    @Override
    public boolean validate(ValidatorContext context, ValidateBean<V> vValidateBean) {
        // 设置验证标题
        setFieldCaption(vValidateBean.getField());

        return doValidate(context, vValidateBean.getBean());
    }

    /**
     * 执行检验
     *
     * @param context
     *            上下文
     * @param bean
     *            待验证对象
     * @return
     */
    protected abstract boolean doValidate(ValidatorContext context, V bean);

    /**
     * 取得验证对象的显示标题
     *
     * @return
     */
    protected String getFieldCaption() {
        return StringUtil.isNullOrBlank(fieldCaption) == false ? fieldCaption : getDefaultCaption();
    }

    /**
     * 设置验证对象的显示标题
     *
     * @param fieldCaption
     */
    protected void setFieldCaption(String fieldCaption) {
        if (StringUtil.isNullOrBlank(fieldCaption) == false) {
            this.fieldCaption = fieldCaption;
        } else {
            this.fieldCaption = getDefaultCaption();
        }
    }

    /**
     * 取得默认验证对象的显示标题
     *
     * @return
     */
    protected String getDefaultCaption() {
        return DEFAULT_FIELD;
    }
}
