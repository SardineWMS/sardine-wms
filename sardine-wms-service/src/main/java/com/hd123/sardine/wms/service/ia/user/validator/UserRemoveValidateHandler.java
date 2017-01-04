/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	UserRemoveValidateHandler.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月12日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.ia.user.validator;

import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.unbiz.fluentvalidator.Validator;
import com.hd123.sardine.wms.api.ia.user.User;
import com.hd123.sardine.wms.common.entity.HasVersion;
import com.hd123.sardine.wms.common.validator.GenericValidateHandler;
import com.hd123.sardine.wms.common.validator.ValidateBean;

/**
 * 删除、启用、禁用用户校验器
 * <p>
 * <ul>
 * <li>操作的uuid不能为空</li>
 * <li>且用户实体存在</li>
 * <li>版本号一致</li>
 * </ul>
 * 
 * @author zhangsai
 *
 */
public class UserRemoveValidateHandler extends GenericValidateHandler<String> {

    /** 调用该校验器的service需传入当前待删除、启用、禁用的user对象 */
    public static final String KEY_OPERATOR_USER = "operator_user";

    @Autowired
    private Validator<ValidateBean<Object>> notNullValidator;
    
    @Autowired
    private Validator<ValidateBean<Object>> entityNotNullValidator;

    @Autowired
    private Validator<ValidateBean<HasVersion>> versionValidator;

    @Override
    protected void prepareValidators(String bean) {
        on("用户", bean, notNullValidator);

        User user = (User) getAttribute(KEY_OPERATOR_USER);
        on("操作的用户", user, entityNotNullValidator);

        on(user, versionValidator);
    }
}
