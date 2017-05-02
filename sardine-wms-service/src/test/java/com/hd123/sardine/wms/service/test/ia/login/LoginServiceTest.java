/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	LoginServiceTest.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月16日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.test.ia.login;

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

import com.hd123.sardine.wms.api.ia.login.UserInfo;
import com.hd123.sardine.wms.api.ia.user.CompanyService;
import com.hd123.sardine.wms.api.ia.user.RegisterUser;
import com.hd123.sardine.wms.api.ia.user.User;
import com.hd123.sardine.wms.api.ia.user.UserState;
import com.hd123.sardine.wms.common.entity.OperateContext;
import com.hd123.sardine.wms.dao.ia.user.UserDao;
import com.hd123.sardine.wms.service.ia.login.LoginServiceImpl;
import com.hd123.sardine.wms.service.test.BaseServiceTest;
import com.hd123.sardine.wms.service.test.ia.user.RegisterUserBuilder;
import com.hd123.sardine.wms.service.test.ia.user.UserBuilder;
import com.hd123.sardine.wms.service.util.FlowCodeGenerator;

/**
 * @author zhangsai
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class LoginServiceTest extends BaseServiceTest {

    private static final String LOGIN_CODE = "001";
    private static final String LOGIN_PASSWD = "002";

    private static final String UPDATEPASSWD_USERID = "000001";
    private static final String UPDATEPASSWD_OLDPASSWD = "888888";
    private static final String UPDATEPASSWD_NEWPASSWD = "666666";

    private static final String REGISTER_USERCODE = "0021";
    private static final String REGISTER_USERNAME = "沙丁鱼";
    private static final String REGISTER_PASSWD = "666666";
    private static final String REGISTER_COMPANYNAME = "天堂伞";
    private static final String REGISTER_COMPANYCODE = "10001";
    private static final String ADDRESS = "九堡";
    private static final String HOMEPAGE = "www.hd123.com";
    private static final String COMPANYTYPE = "deliveryCenter";

    @InjectMocks
    public LoginServiceImpl service;
    @Mock
    private UserDao dao;
    @Mock
    private FlowCodeGenerator flowCodeGenerator;
    @Mock
    private CompanyService companyService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    public void login() throws Exception {
        User user = UserBuilder.user().withUserState(UserState.online).build();
        when(dao.login(anyString(), anyString())).thenReturn(user);

        UserInfo userInfo = service.login(LOGIN_CODE, LOGIN_PASSWD);

        assertThat(userInfo).isNotNull();
        assertThat(userInfo.getUuid()).isEqualTo(user.getUuid());
        assertThat(userInfo.getCode()).isEqualTo(user.getCode());
        assertThat(userInfo.getName()).isEqualTo(user.getName());
        assertThat(userInfo.getCompanyUuid()).isEqualTo(user.getCompanyUuid());
        assertThat(userInfo.getCompanyCode()).isEqualTo(user.getCompanyCode());
        assertThat(userInfo.getCompanyName()).isEqualTo(user.getCompanyName());
    }

    @Test
    public void updatePasswd() throws Exception {
        OperateContext operCtx = defaultOperCtx();
        User user = UserBuilder.user().withUserState(UserState.online).build();
        when(dao.get(anyString())).thenReturn(user);

        UserInfo userInfo = service.updatePasswd(UPDATEPASSWD_USERID, UPDATEPASSWD_OLDPASSWD,
                UPDATEPASSWD_NEWPASSWD, operCtx);

        verify(dao).updatePasswd(UPDATEPASSWD_USERID, UPDATEPASSWD_OLDPASSWD,
                UPDATEPASSWD_NEWPASSWD, operCtx);
        assertThat(userInfo).isNotNull();
        assertThat(userInfo.getUuid()).isEqualTo(user.getUuid());
        assertThat(userInfo.getCode()).isEqualTo(user.getCode());
        assertThat(userInfo.getName()).isEqualTo(user.getName());
        assertThat(userInfo.getCompanyUuid()).isEqualTo(user.getCompanyUuid());
        assertThat(userInfo.getCompanyCode()).isEqualTo(user.getCompanyCode());
        assertThat(userInfo.getCompanyName()).isEqualTo(user.getCompanyName());
    }

    @Test
    public void register() throws Exception {
        RegisterUser registerUser = RegisterUserBuilder.registerUser().withName(REGISTER_USERNAME)
                .withCompanyName(REGISTER_COMPANYNAME).withPasswd(REGISTER_PASSWD)
                .withCompanyCode(REGISTER_COMPANYCODE).withAddress(ADDRESS).withHomePage(HOMEPAGE)
                .withCompanyType(COMPANYTYPE).build();
        when(dao.getByCode(anyString())).thenReturn(null);
        when(flowCodeGenerator.allocate(anyString(), anyString(), 8)).thenReturn("800001");

        service.register(registerUser);

        verify(dao).insert(userCaptor.capture());
        assertThat(userCaptor.getValue().getUuid()).isNotNull();
        assertThat(userCaptor.getValue().getCompanyUuid()).isNotNull();
        assertThat(userCaptor.getValue().getCompanyCode()).isNotNull();
        assertThat(userCaptor.getValue().getCompanyName()).isEqualTo(REGISTER_COMPANYNAME);
        assertThat(userCaptor.getValue().getName()).isEqualTo(REGISTER_USERNAME);
        assertThat(userCaptor.getValue().getPasswd()).isEqualTo(REGISTER_PASSWD);
    }
}
