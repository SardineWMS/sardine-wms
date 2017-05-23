/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	RoleServiceTest.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月27日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.service.test.ia.role;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.fest.assertions.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hd123.sardine.wms.api.ia.resource.ResourceService;
import com.hd123.sardine.wms.api.ia.role.Role;
import com.hd123.sardine.wms.api.ia.role.RoleState;
import com.hd123.sardine.wms.dao.ia.role.RoleDao;
import com.hd123.sardine.wms.service.ia.role.RoleServiceImpl;
import com.hd123.sardine.wms.service.test.BaseServiceTest;

/**
 * @author fanqingqing
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class RoleServiceTest extends BaseServiceTest {
  private static final String UUID = "89757";
  private static final long VERSION = 0;

  @InjectMocks
  public RoleServiceImpl service;
  @Mock
  private RoleDao dao;
  @Mock
  private ResourceService resourceService;

  @Test
  public void insert() throws Exception {
    Role role = RoleBuilder.role().withUuid(null).build();

    service.insert(role);

    verify(dao).insert(role);
    Assertions.assertThat(role.getUuid()).isNotEmpty();
    Assertions.assertThat(role.getCreateInfo().getOperator())
        .isEqualTo(defaultOperCtx().getOperator());
    Assertions.assertThat(role.getLastModifyInfo().getOperator())
        .isEqualTo(defaultOperCtx().getOperator());
  }

  @Test
  public void update() throws Exception {
    Role role = RoleBuilder.role().withUuid(UUID).withVersion(0).build();
    when(dao.get(UUID)).thenReturn(role);

    service.update(role);

    verify(dao).update(role);
    Assertions.assertThat(role.getLastModifyInfo().getOperator())
        .isEqualTo(defaultOperCtx().getOperator());
  }

  @Test
  public void remove() throws Exception {
    Role role = RoleBuilder.role().withUuid(UUID).withState(RoleState.online).withVersion(VERSION)
        .build();
    when(dao.get(UUID)).thenReturn(role);

    service.remove(UUID, VERSION);

    verify(resourceService).removeResourceByRole(UUID);
    verify(dao).remove(UUID, VERSION);
  }

  @Test
  public void offline() throws Exception {
    Role role = RoleBuilder.role().withUuid(UUID).withState(RoleState.online).withVersion(VERSION)
        .build();
    when(dao.get(UUID)).thenReturn(role);

    service.offline(UUID, VERSION);

    verify(dao).update(role);

    Assertions.assertThat(RoleState.offline).isEqualTo(role.getState());
    Assertions.assertThat(role.getLastModifyInfo().getOperator())
        .isEqualTo(defaultOperCtx().getOperator());
  }

  @Test
  public void online() throws Exception {
    Role role = RoleBuilder.role().withUuid(UUID).withState(RoleState.offline).withVersion(VERSION)
        .build();
    when(dao.get(UUID)).thenReturn(role);

    service.online(UUID, VERSION);

    verify(dao).update(role);

    Assertions.assertThat(RoleState.online).isEqualTo(role.getState());
    Assertions.assertThat(role.getLastModifyInfo().getOperator())
        .isEqualTo(defaultOperCtx().getOperator());
  }

}
