/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	wechange-wms-dao
 * 文件名：	AbstractDataAccessTests.java
 * 模块说明：
 * 修改历史：
 * 2016-9-26 - zhanglin - 创建。
 */
package com.hd123.sardine.wms.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.hd123.sardine.wms.api.ia.user.User;
import com.hd123.sardine.wms.common.entity.OperateContext;
import com.hd123.sardine.wms.common.entity.Operator;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.dao.ia.user.UserDao;

/**
 * @author zhanglin
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "classpath:dao.xml", "classpath:applicationContext-ds.xml" })
@Rollback
@Transactional
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
    TransactionDbUnitTestExecutionListener.class })
public class AbstractDataAccessTests {
  @Autowired
  private UserDao userDao;

  @Before
  public void login() throws Exception {
    User user = userDao.login("zhangsai", "admin8888");
    ApplicationContextUtil.setCompany(
        new UCN(user.getCompanyUuid(), user.getCompanyCode(), user.getCompanyName()));
    ApplicationContextUtil.setOperateContext(new OperateContext(new Operator("zs", "zs", "zs")));
  }
  
  @Test
  public void test() {
    userDao.get("24234");
  }
}
