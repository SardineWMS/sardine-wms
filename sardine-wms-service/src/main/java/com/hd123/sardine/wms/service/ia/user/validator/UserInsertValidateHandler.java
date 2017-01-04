/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	UserInsertValidateHandler.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月12日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.ia.user.validator;

import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.unbiz.fluentvalidator.Validator;
import com.hd123.sardine.wms.api.ia.user.User;
import com.hd123.sardine.wms.common.entity.IsEntity;
import com.hd123.sardine.wms.common.validator.GenericValidateHandler;
import com.hd123.sardine.wms.common.validator.ValidateBean;

/**
 * 新增用户校验器
 * <p>
 * <ul>
 * <li>新用户的基本信息必须满足规范</li>
 * <li>操作人用户必须存在，调用者通过{@link UserInsertValidateHandler#KEY_OPERATOR_USER}传入</li>
 * <li>新用户代码不能重复，需由调用者将根据新用户代码查询出的用户以key
 * {@link UserInsertValidateHandler#KEY_CODEEXISTS_USER}传入</li>
 * </ul>
 * 
 * @author zhangsai
 *
 */
public class UserInsertValidateHandler extends GenericValidateHandler<User> {

    /** 调用该校验器的service需传入user对象 与当前注册用户代码一致的已存在用户 */
    public static final String KEY_CODEEXISTS_USER = "codeExists_user";

    /** 调用该校验器的service需传入操作人用户，该用户必须存在 */
    public static final String KEY_OPERATOR_USER = "Operator_user";

    @Autowired
    private Validator<ValidateBean<Object>> notNullValidator;

    @Autowired
    private Validator<ValidateBean<Object>> entityNotNullValidator;

    @Autowired
    private Validator<ValidateBean<String>> length30Validator;

    @Autowired
    private Validator<ValidateBean<String>> length100Validator;

    @Autowired
    private Validator<ValidateBean<IsEntity>> nullValidator;

    @Override
    protected void prepareValidators(User bean) {
        on("用户", bean, notNullValidator);
        on("用户状态", bean.getUserState(), notNullValidator);
        on("用户代码", bean.getCode(), notNullValidator);
        on("用户代码", bean.getCode(), length30Validator);
        on("用户名称", bean.getName(), notNullValidator);
        on("用户名称", bean.getName(), length100Validator);
        on("用户电话", bean.getPhone(), notNullValidator);
        on("用户电话", bean.getPhone(), length30Validator);
        on("密码", bean.getPasswd(), length30Validator);

        User user = (User) getAttribute(KEY_CODEEXISTS_USER);
        on("用户代码" + bean.getCode(), user, nullValidator);

        User operatorUser = (User) getAttribute(KEY_OPERATOR_USER);
        on("用户", operatorUser, entityNotNullValidator);
    }
}
