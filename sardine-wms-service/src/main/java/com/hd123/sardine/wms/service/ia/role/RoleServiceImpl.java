/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	RoleServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月23日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.ia.role;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.ia.resource.ResourceService;
import com.hd123.sardine.wms.api.ia.role.Role;
import com.hd123.sardine.wms.api.ia.role.RoleService;
import com.hd123.sardine.wms.api.ia.role.RoleState;
import com.hd123.sardine.wms.common.entity.OperateInfo;
import com.hd123.sardine.wms.common.exception.EntityNotFoundException;
import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.common.validator.ValidateHandler;
import com.hd123.sardine.wms.common.validator.ValidateResult;
import com.hd123.sardine.wms.dao.ia.role.RoleDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.ia.role.validator.RoleInsertValidateHandler;
import com.hd123.sardine.wms.service.ia.role.validator.RoleUpdateValidateHandler;
import com.hd123.sardine.wms.service.log.EntityLogger;

/**
 * 角色服务：实现
 * 
 * @author zhangsai
 *
 */
public class RoleServiceImpl extends BaseWMSService implements RoleService {

  @Autowired
  private RoleDao roleDao;
  @Autowired
  private ResourceService resourceService;
  @Autowired
  private ValidateHandler<Role> roleInsertValidateHandler;
  @Autowired
  private ValidateHandler<Role> roleUpdateValidateHandler;
  @Autowired
  private EntityLogger logger;

  @Override
  public PageQueryResult<Role> query(PageQueryDefinition definition)
      throws IllegalArgumentException {
    Assert.assertArgumentNotNull(definition, "definition");

    definition.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    PageQueryResult<Role> pgr = new PageQueryResult<Role>();
    List<Role> list = roleDao.query(definition);
    PageQueryUtil.assignPageInfo(pgr, definition);
    pgr.setRecords(list);
    return pgr;
  }

  @Override
  public String insert(Role role)
      throws IllegalArgumentException, WMSException {
    Assert.assertArgumentNotNull(role, "role");
    
    role.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    Role dbRole = roleDao.getByCode(role.getCode(),
        ApplicationContextUtil.getCompanyUuid());
    ValidateResult insertResult = roleInsertValidateHandler
        .putAttribute(RoleInsertValidateHandler.KEY_CODEEXISTS_ROLE, dbRole).validate(role);
    checkValidateResult(insertResult);

    Role existsRole = roleDao.getByCode(role.getCode(), role.getCompanyUuid());
    if (existsRole != null)
      throw new WMSException("角色代码" + role.getCode() + "已经存在，请尝试其他代码");

    role.setUuid(UUIDGenerator.genUUID());
    role.setState(RoleState.online);
    role.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    role.setCreateInfo(ApplicationContextUtil.getOperateInfo());
    role.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    roleDao.insert(role);

    logger.injectContext(this, role.getUuid(), Role.class.getName(), ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_ADDNEW, "新建角色");
    return role.getUuid();
  }

  @Override
  public void update(Role role) throws IllegalArgumentException,
      EntityNotFoundException, VersionConflictException, WMSException {
    Role oldRole = roleDao.get(role == null ? null : role.getUuid());
    ValidateResult updateResult = roleUpdateValidateHandler
        .putAttribute(RoleUpdateValidateHandler.KEY_OPERATOR_ROLE, oldRole).validate(role);
    checkValidateResult(updateResult);

    Role existsRole = roleDao.getByCode(role.getCode(), role.getCompanyUuid());
    if (existsRole != null && existsRole.getUuid().equals(role.getUuid()) == false)
      throw new WMSException("角色代码" + role.getCode() + "已经存在，请尝试其他代码");

    PersistenceUtils.checkVersion(role.getVersion(), oldRole, "角色", role.getCode());

    role.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    roleDao.update(role);

    logger.injectContext(this, role.getUuid(), Role.class.getName(), ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "修改角色");
  }

  @Override
  public void online(String uuid, long version)
      throws IllegalArgumentException, EntityNotFoundException, VersionConflictException,
      WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");

    Role oldRole = roleDao.get(uuid);
    if (oldRole == null)
      throw new EntityNotFoundException("编辑的角色不存在，请重试");

    if (oldRole.getState().equals(RoleState.online))
      return;

    PersistenceUtils.checkVersion(version, oldRole, "角色", oldRole.getCode());

    oldRole.setState(RoleState.online);
    oldRole.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    roleDao.update(oldRole);

    logger.injectContext(this, uuid, Role.class.getName(), ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "启用角色");
  }

  @Override
  public void offline(String uuid, long version)
      throws IllegalArgumentException, EntityNotFoundException, VersionConflictException,
      WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Role oldRole = roleDao.get(uuid);
    if (oldRole == null)
      throw new EntityNotFoundException("编辑的角色不存在，请重试");

    if (oldRole.getState().equals(RoleState.offline))
      return;

    PersistenceUtils.checkVersion(version, oldRole, "角色", oldRole.getCode());

    oldRole.setState(RoleState.offline);
    oldRole.setLastModifyInfo(OperateInfo.newInstance(ApplicationContextUtil.getOperateContext()));
    roleDao.update(oldRole);

    logger.injectContext(this, uuid, Role.class.getName(), ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "停用角色");
  }

  @Override
  public void remove(String uuid, long version)
      throws IllegalArgumentException, EntityNotFoundException, VersionConflictException,
      WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");

    Role oldRole = roleDao.get(uuid);
    if (oldRole == null)
      return;

    PersistenceUtils.checkVersion(version, oldRole, "角色", oldRole.getCode());
    resourceService.removeResourceByRole(uuid);
    roleDao.removeRelationRoleAndUserByRole(uuid);
    roleDao.remove(uuid, version);

    logger.injectContext(this, uuid, Role.class.getName(), ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_REMOVE, "删除角色");
  }

  @Override
  public List<Role> queryRolesByUser(String userUuid) throws IllegalArgumentException {
    Assert.assertArgumentNotNull(userUuid, "userUuid");
    List<Role> list = roleDao.queryRolesByUser(userUuid);
    for (Role role : list) {
      role.setOwned(true);
    }
    return list;
  }

  @Override
  public List<Role> queryAllRoleByUser(String userUuid)
      throws IllegalArgumentException {
    Assert.assertArgumentNotNull(userUuid, "userUuid");
    List<Role> allRoles = roleDao.queryAllRoleByCompany(ApplicationContextUtil.getCompanyUuid());
    List<Role> ownRoles = queryRolesByUser(userUuid);
    for (Role allRole : allRoles) {
      for (Role ownRole : ownRoles) {
        if (ownRole.equals(allRole)) {
          allRole.setOwned(ownRole.isOwned());
        }
      }
    }
    return allRoles;
  }
}
