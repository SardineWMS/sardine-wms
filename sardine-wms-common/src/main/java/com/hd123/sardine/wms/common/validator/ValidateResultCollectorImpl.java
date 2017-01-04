/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * <p/>
 * 文件名：	ValidateResultCollectorImpl.java
 * 模块说明：
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.validator;

import com.baidu.unbiz.fluentvalidator.ResultCollector;
import com.baidu.unbiz.fluentvalidator.ValidationResult;

/**
 * 验证结果收集器
 *
 * @author zhangsai
 */
public class ValidateResultCollectorImpl implements ResultCollector<ValidateResult> {

    @Override
    public ValidateResult toResult(ValidationResult result) {
        ValidateResult ret = new ValidateResult();
        if (result.isSuccess()) {
            ret.setIsSuccess(true);
        } else {
            ret.setIsSuccess(false);
            ret.setErrors(result.getErrors());
        }

        ret.setTimeElapsed(result.getTimeElapsed());
        return ret;
    }

}
