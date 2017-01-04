/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * <p/>
 * 文件名：	GenericValidateHandler.java
 * 模块说明：
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.validator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.Validator;

/**
 * 通用验证器基类
 *
 * @author zhangsai
 */
public abstract class GenericValidateHandler<S> implements ValidateHandler<S> {

    /**
     * 验证器均可以共享使用的属性键值对
     */
    private Map<String, Object> attributes;

    /**
     * 调用发起点注入的闭包
     */
    @SuppressWarnings("rawtypes")
    private Map<String, ValidateClosure> closures;

    /**
     * 是否一旦发生验证错误就退出，默认为true
     *
     * @see #failFast()
     * @see #failOver()
     */
    private boolean isFailFast = true;

    /**
     * 基于FluentValidator验证框架进行验证
     */
    private FluentValidator fluentValidator;

    @Override
    public ValidateResult validate(S bean) {
        fluentValidator = FluentValidator.checkAll();
        if (isFailFast) {
            fluentValidator.failFast();
        } else {
            fluentValidator.failOver();
        }

        if (attributes != null && attributes.size() > 0) {
            for (String key : attributes.keySet()) {
                fluentValidator.putAttribute2Context(key, attributes.get(key));
            }
        }

        if (closures != null && closures.size() > 0) {
            for (String key : closures.keySet()) {
                fluentValidator.putClosure2Context(key, closures.get(key));
            }
        }

        prepareValidators(bean);

        return fluentValidator.doValidate().result(new ValidateResultCollectorImpl());
    }

    /**
     * 准备验证所需的验证器
     * <p>
     * 可调用 {@link #on(Object, Validator)} 、
     * {@link #onEach(Collection, Validator)} 和
     * {@link #onEach(Object[], Validator)} 进行添加
     * </p>
     */
    protected abstract void prepareValidators(S bean);

    /**
     * 为待验证对象添加验证器
     *
     * @param bean
     *            待验证对象, not null
     * @param validator
     *            验证器, not null
     * @return
     */
    protected <B> GenericValidateHandler<S> on(B bean, Validator<ValidateBean<B>> validator) {
        return on(GenericValidator.DEFAULT_FIELD, bean, validator);
    }

    @SuppressWarnings({
            "unchecked", "rawtypes" })
    protected <B> GenericValidateHandler<S> on(String field, B bean,
            Validator<ValidateBean<B>> validator) {
        if (fluentValidator != null)
            fluentValidator.on(new ValidateBean(field, bean), validator);
        return this;
    }

    /**
     * 为待验证对象数组添加验证器
     *
     * @param list
     *            待验证对象列表, not null
     * @param validator
     *            验证器, not null
     * @return
     */
    protected <B> GenericValidateHandler<S> onEach(B[] list, Validator<B> validator) {
        if (fluentValidator != null)
            fluentValidator.onEach(list, validator);
        return this;
    }

    /**
     * 为待验证对象列表添加验证器
     *
     * @param list
     *            待验证对象列表, not null
     * @param validator
     *            验证器, not null
     * @return
     */
    protected <B> GenericValidateHandler<S> onEach(Collection<B> list, Validator<B> validator) {
        if (fluentValidator != null)
            fluentValidator.onEach(list, validator);
        return this;
    }

    @Override
    public ValidateHandler<S> putAttribute(String key, Object value) {
        if (attributes == null)
            attributes = new HashMap<String, Object>();

        attributes.put(key, value);

        return this;
    }

    @Override
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public ValidateHandler<S> putClosure(String key, ValidateClosure value) {
        if (closures == null)
            closures = new HashMap<String, ValidateClosure>();

        closures.put(key, value);

        return this;
    }

    @Override
    public ValidateHandler<S> failFast() {
        this.isFailFast = true;
        return this;
    }

    @Override
    public ValidateHandler<S> failOver() {
        this.isFailFast = false;
        return this;
    }

}
