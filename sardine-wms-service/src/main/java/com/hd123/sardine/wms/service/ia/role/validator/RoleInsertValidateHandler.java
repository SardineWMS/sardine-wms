/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	UserInsertValidateHandler.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月12日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.ia.role.validator;

import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.unbiz.fluentvalidator.Validator;
import com.hd123.sardine.wms.api.ia.role.Role;
import com.hd123.sardine.wms.common.entity.IsEntity;
import com.hd123.sardine.wms.common.validator.GenericValidateHandler;
import com.hd123.sardine.wms.common.validator.ValidateBean;

/**
 * 新增角色校验器
 * <p>
 * <ul>
 * <li>新角色的基本信息必须满足规范</li>
 * <li>操作人角色必须存在，调用者通过{@link RoleInsertValidateHandler#KEY_OPERATOR_ROLE}传入</li>
 * <li>新角色代码不能重复，需由调用者将根据新角色代码查询出的角色以key
 * {@link RoleInsertValidateHandler#KEY_CODEEXISTS_USER}传入</li>
 * </ul>
 * 
 * @author zhangsai
 *
 */
public class RoleInsertValidateHandler extends GenericValidateHandler<Role> {
    public static final String KEY_CODEEXISTS_ROLE = "codeExists_role";

    @Autowired
    private Validator<ValidateBean<Object>> notNullValidator;

    @Autowired
    private Validator<ValidateBean<String>> length30Validator;

    @Autowired
    private Validator<ValidateBean<String>> length100Validator;

    @Autowired
    private Validator<ValidateBean<IsEntity>> nullValidator;

    @Override
    protected void prepareValidators(Role bean) {
        on("角色", bean, notNullValidator);
        on("角色状态", bean.getState(), notNullValidator);
        on("角色代码", bean.getCode(), notNullValidator);
        on("角色代码", bean.getCode(), length30Validator);
        on("角色名称", bean.getName(), notNullValidator);
        on("角色名称", bean.getName(), length100Validator);
        on("角色组织", bean.getCompanyUuid(), notNullValidator);
        on("角色组织", bean.getCompanyUuid(), length30Validator);

        Role role = (Role) getAttribute(KEY_CODEEXISTS_ROLE);
        on("角色代码" + bean.getCode(), role, nullValidator);
    }
}
