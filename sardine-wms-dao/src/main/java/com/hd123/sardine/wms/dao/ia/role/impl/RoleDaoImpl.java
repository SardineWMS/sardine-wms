/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	RoleDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月24日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.ia.role.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.ia.role.Role;
import com.hd123.sardine.wms.common.dao.impl.BaseDaoImpl;
import com.hd123.sardine.wms.dao.ia.role.RoleDao;

/**
 * @author zhangsai
 *
 */
public class RoleDaoImpl extends BaseDaoImpl<Role> implements RoleDao {
    private static final String MAPPER_GETBYCODE = "getByCode";
    private static final String MAPPER_QUERYROLESBYUSER = "queryRolesByUser";
    public static final String MAPPER_REMOVERELATIONROLEANDUSERBYROLE = "removeRelationRoleAndUserByRole";

    @Override
    public Role getByCode(String code, String companyUuid) {
        if (StringUtil.isNullOrBlank(code) || StringUtil.isNullOrBlank(companyUuid))
            return null;
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        map.put("companyUuid", companyUuid);
        return getSqlSession().selectOne(generateStatement(MAPPER_GETBYCODE), map);
    }

    @Override
    public List<Role> queryRolesByUser(String userUuid) {
        Assert.assertArgumentNotNull(userUuid, "userUuid");
        return getSqlSession().selectList(generateStatement(MAPPER_QUERYROLESBYUSER), userUuid);
    }

    @Override
    public void removeRelationRoleAndUserByRole(String roleUuid) {
        Assert.assertArgumentNotNull(roleUuid, "roleUuid");

        getSqlSession().delete(generateStatement(MAPPER_REMOVERELATIONROLEANDUSERBYROLE), roleUuid);
    }

}
