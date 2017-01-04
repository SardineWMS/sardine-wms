/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * <p/>
 * 文件名：	GenericValidator.java
 * 模块说明：
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.validator;

import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;

/**
 * 通用验证器基类
 *
 * @author zhangsai
 */
public abstract class GenericValidator<T> implements Validator<T> {

    public static final String DEFAULT_FIELD = "field";

    @Override
    public boolean accept(ValidatorContext context, T t) {
        return true;
    }

    @Override
    public void onException(Exception e, ValidatorContext context, T t) {
        // do nothing
    }

}
