/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * <p/>
 * 文件名：	ValidateHandler.java
 * 模块说明：
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.validator;

/**
 * 业务验证器接口
 *
 * @author zhangsai
 */
public interface ValidateHandler<S> {

    /**
     * 执行验证
     *
     * @param bean
     *            待验证对象
     * @return 返回验证结果
     */
    ValidateResult validate(S bean);

    /**
     * 添加属性值
     *
     * @param key
     * @param value
     * @return
     */
    ValidateHandler<S> putAttribute(String key, Object value);

    /**
     * 获取属性值
     *
     * @param key
     * @return
     */
    Object getAttribute(String key);

    /**
     * 添加一个闭包
     *
     * @param key
     * @param value
     * @return
     */
    @SuppressWarnings("rawtypes")
    ValidateHandler<S> putClosure(String key, ValidateClosure value);

    /**
     * 出错即退出
     *
     * @return ValidateHandler<S>
     */
    public ValidateHandler<S> failFast();

    /**
     * 出错不退出而继续
     *
     * @return ValidateHandler<S>
     */
    public ValidateHandler<S> failOver();

}
