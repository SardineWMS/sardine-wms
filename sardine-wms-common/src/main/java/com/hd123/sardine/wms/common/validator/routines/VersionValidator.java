/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * <p/>
 * 文件名：	VersionValidator.java
 * 模块说明：
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.validator.routines;

import java.text.MessageFormat;

import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.hd123.sardine.wms.common.entity.HasVersion;
import com.hd123.sardine.wms.common.validator.GenericBeanValidator;
import com.hd123.sardine.wms.common.validator.errmsg.VersionConflictErrorMsg;

/**
 * 版本检查校验器
 *
 * @author zhangsai
 */
public class VersionValidator extends GenericBeanValidator<HasVersion> {

    // 验证处理器中的属性key
    public static final String ATTR_KEY_VERSION = "version";

    @Override
    protected boolean doValidate(ValidatorContext context, HasVersion bean) {
        Object obj = context.getAttribute(ATTR_KEY_VERSION);
        if (obj == null)
            return true;

        if (!(obj instanceof Long))
            return true;

        Long version = (Long) obj;

        if (version != bean.getVersion()) {
            context.addError(VersionConflictErrorMsg.create(MessageFormat.format("目标版本[{0}]与当前版本[{1}]", version, bean.getVersion())));
            return false;
        }

        return true;
    }

    @Override
    protected String getDefaultCaption() {
        return "版本号";
    }
}
