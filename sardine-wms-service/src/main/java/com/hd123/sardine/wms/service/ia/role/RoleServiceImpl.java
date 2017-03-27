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
import com.hd123.sardine.wms.api.ia.role.Role;
import com.hd123.sardine.wms.api.ia.role.RoleService;
import com.hd123.sardine.wms.api.ia.role.RoleState;
import com.hd123.sardine.wms.common.entity.OperateContext;
import com.hd123.sardine.wms.common.entity.OperateInfo;
import com.hd123.sardine.wms.common.exception.EntityNotFoundException;
import com.hd123.sardine.wms.common.exception.IAException;
import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.dao.ia.role.RoleDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;

/**
 * 角色服务：实现
 * 
 * @author zhangsai
 *
 */
public class RoleServiceImpl extends BaseWMSService implements RoleService {

  @Autowired
  private RoleDao roleDao;

  @Override
  public PageQueryResult<Role> query(PageQueryDefinition definition)
      throws IllegalArgumentException {
    Assert.assertArgumentNotNull(definition, "definition");

    PageQueryResult<Role> pgr = new PageQueryResult<Role>();
    List<Role> list = roleDao.query(definition);
    PageQueryUtil.assignPageInfo(pgr, definition);
    pgr.setRecords(list);
    return pgr;
  }

  @Override
  public String insert(Role role, OperateContext operCtx)
      throws IllegalArgumentException, IAException {
    Assert.assertArgumentNotNull(role, "role");
    Assert.assertArgumentNotNull(operCtx, "operCtx");
    Assert.assertArgumentNotNull(role.getCode(), "role.code");
    Assert.assertArgumentNotNull(role.getName(), "role.name");
    Assert.assertArgumentNotNull(role.getCompanyUuid(), "role.companyUuid");

    Role existsRole = roleDao.getByCode(role.getCode(), role.getCompanyUuid());
    if (existsRole != null)
      throw new IAException("角色代码" + role.getCode() + "已经存在，请尝试其他代码");

    role.setUuid(UUIDGenerator.genUUID());
    role.setState(RoleState.online);
    role.setCreateInfo(OperateInfo.newInstance(operCtx));
    role.setLastModifyInfo(OperateInfo.newInstance(operCtx));
    roleDao.insert(role);
    return role.getUuid();
  }

  @Override
  public void update(Role role, OperateContext operCtx) throws IllegalArgumentException,
      EntityNotFoundException, VersionConflictException, IAException {
    Assert.assertArgumentNotNull(role, "role");
    Assert.assertArgumentNotNull(operCtx, "operCtx");
    Assert.assertArgumentNotNull(role.getUuid(), "role.uuid");
    Assert.assertArgumentNotNull(role.getCode(), "role.code");
    Assert.assertArgumentNotNull(role.getName(), "role.name");
    Assert.assertArgumentNotNull(role.getCompanyUuid(), "role.companyUuid");

    Role oldRole = roleDao.get(role.getUuid());
    if (oldRole == null)
      throw new EntityNotFoundException("编辑的角色不存在，请重试");

    Role existsRole = roleDao.getByCode(role.getCode(), role.getCompanyUuid());
    if (existsRole != null && existsRole.getUuid().equals(role.getUuid()) == false)
      throw new IAException("角色代码" + role.getCode() + "已经存在，请尝试其他代码");

    PersistenceUtils.checkVersion(role.getVersion(), oldRole, "角色", role.getCode());

    role.setLastModifyInfo(OperateInfo.newInstance(operCtx));
    roleDao.update(role);
  }

  @Override
  public void online(String uuid, long version, OperateContext operCtx)
      throws IllegalArgumentException, EntityNotFoundException, VersionConflictException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(operCtx, "operCtx");

    Role oldRole = roleDao.get(uuid);
    if (oldRole == null)
      throw new EntityNotFoundException("编辑的角色不存在，请重试");

    if (oldRole.getState().equals(RoleState.online))
      return;

    PersistenceUtils.checkVersion(version, oldRole, "角色", oldRole.getCode());

    oldRole.setState(RoleState.online);
    oldRole.setLastModifyInfo(OperateInfo.newInstance(operCtx));
    roleDao.update(oldRole);
  }

  @Override
  public void offline(String uuid, long version, OperateContext operCtx)
      throws IllegalArgumentException, EntityNotFoundException, VersionConflictException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(operCtx, "operCtx");

    Role oldRole = roleDao.get(uuid);
    if (oldRole == null)
      throw new EntityNotFoundException("编辑的角色不存在，请重试");

    if (oldRole.getState().equals(RoleState.offline))
      return;

    PersistenceUtils.checkVersion(version, oldRole, "角色", oldRole.getCode());

    oldRole.setState(RoleState.offline);
    oldRole.setLastModifyInfo(OperateInfo.newInstance(operCtx));
    roleDao.update(oldRole);
  }

  @Override
  public void remove(String uuid, long version, OperateContext operCtx)
      throws IllegalArgumentException, EntityNotFoundException, VersionConflictException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(operCtx, "operCtx");

    Role oldRole = roleDao.get(uuid);
    if (oldRole == null)
      return;

    PersistenceUtils.checkVersion(version, oldRole, "角色", oldRole.getCode());
    // TODO 更新 角色-用户 角色-资源  用户-资源

    roleDao.remove(uuid, version);
  }
}
