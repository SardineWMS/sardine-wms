/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	ResourceDao.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月23日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.ia.resource;

import java.util.List;

import com.hd123.sardine.wms.api.ia.resource.Resource;

/**
 * @author zhangsai
 *
 */
public interface ResourceDao {

    void saveRoleResource(String roleUuid, String resourceUuid);

    void saveUserResource(String userUuid, String resourceUUuid);

    List<Resource> queryAllResource();

    List<Resource> queryOwnedResourceByRole(String roleUuid);

    List<Resource> queryOwnedResourceByUser(String userUuid);

    void removeResourceByUser(String userUuid);

    void removeResourceByRole(String roleUuid);

}
