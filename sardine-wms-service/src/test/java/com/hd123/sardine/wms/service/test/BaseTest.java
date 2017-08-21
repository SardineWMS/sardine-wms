package com.hd123.sardine.wms.service.test;
/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	BaseTest.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月21日 - zhangsai - 创建。
 */


import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hd123.sardine.wms.api.ia.login.LoginService;
import com.hd123.sardine.wms.api.ia.login.UserInfo;
import com.hd123.sardine.wms.common.entity.OperateContext;
import com.hd123.sardine.wms.common.entity.Operator;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;

/**
 * @author zhangsai
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "/applicationContext*.xml" })
public class BaseTest {

  @Autowired
  private LoginService loginService;

  @Before
  public void login() throws Exception {
    UserInfo userInfo = loginService.login("zhangsai", "admin8888");
    ApplicationContextUtil.setCompany(
        new UCN(userInfo.getCompanyUuid(), userInfo.getCompanyCode(), userInfo.getCompanyName()));
    ApplicationContextUtil.setOperateContext(new OperateContext(new Operator("zs", "zs", "zs")));
  }
}
