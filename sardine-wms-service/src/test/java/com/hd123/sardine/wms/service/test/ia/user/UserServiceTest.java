/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	UserServiceTest.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月16日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.test.ia.user;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hd123.sardine.wms.api.ia.resource.ResourceService;
import com.hd123.sardine.wms.api.ia.user.User;
import com.hd123.sardine.wms.api.ia.user.UserState;
import com.hd123.sardine.wms.dao.ia.user.UserDao;
import com.hd123.sardine.wms.service.ia.user.UserServiceImpl;
import com.hd123.sardine.wms.service.test.BaseServiceTest;

/**
 * @author zhangsai
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest extends BaseServiceTest {

  private static final String INSERT_COMPANYUUID = "insert_companyuuid";
  private static final String INSERT_COMPANYCODE = "insert_companycode";
  private static final String INSERT_COMPANYNAME = "insert_companyname";

  private static final String UPDATE_USERUUID = "update_uuid";
  private static final long UPDATE_USERVERSION = 0;

  private static final String DELETE_USERUUID = "delete_uuid";
  private static final long DELETE_USERVERSION = 0;

  private static final String LINE_USERUUID = "lineuuid";
  private static final long LINE_USERVERSION = 0;

  @InjectMocks
  public UserServiceImpl service;
  @Mock
  private UserDao dao;
  @Mock
  private ResourceService resourceService;

  @Captor
  private ArgumentCaptor<User> userCaptor;

  @Test
  public void insert() throws Exception {
    User user = UserBuilder.user().withUserState(UserState.online).build();
    when(dao.getByCode(anyString())).thenReturn(null);
    when(dao.get(anyString())).thenReturn(UserBuilder.user().withCompanyUuid(INSERT_COMPANYUUID)
        .withCompanyCode(INSERT_COMPANYCODE).withCompanyName(INSERT_COMPANYNAME).build());

    service.insert(user);

    verify(dao).insert(userCaptor.capture());

    assertThat(userCaptor.getValue().getUuid()).isNotNull();
    assertThat(userCaptor.getValue().getPasswd()).isEqualTo(User.DEFAULT_PASSWD);
    assertThat(userCaptor.getValue().getCompanyUuid()).isEqualTo(INSERT_COMPANYUUID);
    assertThat(userCaptor.getValue().getCompanyCode()).isEqualTo(INSERT_COMPANYCODE);
    assertThat(userCaptor.getValue().getCompanyName()).isEqualTo(INSERT_COMPANYNAME);
  }

  @Test
  public void update() throws Exception {
    User user = UserBuilder.user().withUuid(UPDATE_USERUUID).withVersion(UPDATE_USERVERSION)
        .withUserState(UserState.online).build();
    when(dao.getByCode(anyString()))
        .thenReturn(UserBuilder.user().withUuid(UPDATE_USERUUID).build());
    when(dao.get(anyString()))
        .thenReturn(UserBuilder.user().withVersion(UPDATE_USERVERSION).build());

    service.update(user);

    verify(dao).update(userCaptor.capture());
  }

  @Test
  public void remove() throws Exception {
    User user = UserBuilder.user().withUuid(DELETE_USERUUID).withVersion(DELETE_USERVERSION)
        .withUserState(UserState.online).build();
    when(dao.get(anyString())).thenReturn(user);

    service.remove(DELETE_USERUUID, DELETE_USERVERSION);

    verify(dao).remove(DELETE_USERUUID, DELETE_USERVERSION);
    verify(dao).removeRolesByUser(DELETE_USERUUID);
    verify(resourceService).removeResourceByUser(DELETE_USERUUID);
  }

  @Test
  public void online() throws Exception {
    User user = UserBuilder.user().withUuid(LINE_USERUUID).withVersion(LINE_USERVERSION)
        .withUserState(UserState.offline).build();
    when(dao.get(anyString())).thenReturn(user);

    service.online(LINE_USERUUID, LINE_USERVERSION);

    verify(dao).update(userCaptor.capture());
    assertThat(userCaptor.getValue().getUserState()).isEqualTo(UserState.online);
  }

  @Test
  public void offline() throws Exception {
    User user = UserBuilder.user().withUuid(LINE_USERUUID).withVersion(LINE_USERVERSION)
        .withUserState(UserState.online).build();
    when(dao.get(anyString())).thenReturn(user);

    service.offline(LINE_USERUUID, LINE_USERVERSION);

    verify(dao).update(userCaptor.capture());
    assertThat(userCaptor.getValue().getUserState()).isEqualTo(UserState.offline);
  }
}
