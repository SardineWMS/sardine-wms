/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	RoleDao.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月23日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.ia.role;

import java.util.List;

import com.hd123.sardine.wms.api.ia.role.Role;
import com.hd123.sardine.wms.common.dao.BaseDao;

/**
 * @author zhangsai
 *
 */
public interface RoleDao extends BaseDao<Role> {
    Role getByCode(String code, String orgId);

    List<Role> queryRolesByUser(String userUuid);

    void removeRelationRoleAndUserByRole(String roleUuid);

    List<Role> queryAllRoleByCompany(String companyUuid);
}
