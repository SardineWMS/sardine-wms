/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	RoleUpdateValidateHandler.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月29日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.service.ia.role.validator;

import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.unbiz.fluentvalidator.Validator;
import com.hd123.sardine.wms.api.ia.role.Role;
import com.hd123.sardine.wms.common.validator.GenericValidateHandler;
import com.hd123.sardine.wms.common.validator.ValidateBean;

/**
 * @author fanqingqing
 *
 */
public class RoleUpdateValidateHandler extends GenericValidateHandler<Role> {
    public static final String KEY_OPERATOR_ROLE = "operator_role";

    @Autowired
    private Validator<ValidateBean<Object>> notNullValidator;

    @Autowired
    private Validator<ValidateBean<Object>> entityNotNullValidator;

    @Autowired
    private Validator<ValidateBean<String>> length30Validator;

    @Autowired
    private Validator<ValidateBean<String>> length100Validator;

    @Override
    protected void prepareValidators(Role bean) {
        on("角色", bean, notNullValidator);
        on("角色UUID", bean.getCode(), notNullValidator);
        on("角色UUID", bean.getCode(), length30Validator);
        on("角色状态", bean.getState(), notNullValidator);
        on("角色代码", bean.getCode(), notNullValidator);
        on("角色代码", bean.getCode(), length30Validator);
        on("角色名称", bean.getName(), notNullValidator);
        on("角色名称", bean.getName(), length100Validator);
        on("角色组织", bean.getCompanyUuid(), notNullValidator);
        on("角色组织", bean.getCompanyUuid(), length30Validator);

        Role role = (Role) getAttribute(KEY_OPERATOR_ROLE);
        on("角色" + bean, role, entityNotNullValidator);
    }
}
