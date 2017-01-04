/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	LoginValidateHandler.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月24日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.ia.login.validator;

import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.unbiz.fluentvalidator.Validator;
import com.hd123.sardine.wms.api.ia.user.User;
import com.hd123.sardine.wms.common.validator.GenericValidateHandler;
import com.hd123.sardine.wms.common.validator.ValidateBean;

/**
 * 修改密码校验器
 * <p>
 * <ul>
 * <li>要修改的用户id不为空</li>
 * <li>且存在对应用户</li>
 * </ul>
 * 
 * @author zhangsai
 *
 */
public class UpdatePasswdValidateHandler extends GenericValidateHandler<String> {

    /** 调用该校验器的service需传入user对象，否则校验不通过 */
    public static final String KEY_UPDATEPASSWD_USER = "updatepasswd_user_";

    @Autowired
    private Validator<ValidateBean<Object>> notNullValidator;

    @Autowired
    private Validator<ValidateBean<Object>> entityNotNullValidator;

    @Override
    protected void prepareValidators(String bean) {
        on("登录名", bean, notNullValidator);

        User user = (User) getAttribute(KEY_UPDATEPASSWD_USER);
        on("要修改的用戶", user, entityNotNullValidator);
    }
}
