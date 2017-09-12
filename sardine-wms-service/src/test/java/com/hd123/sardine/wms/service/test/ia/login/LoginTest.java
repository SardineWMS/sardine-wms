/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	LoginTest.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月31日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.service.test.ia.login;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.sardine.wms.api.ia.login.LoginService;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.service.test.BaseTest;

/**
 * @author yangwenzhu
 *
 */
public class LoginTest extends BaseTest {
  @Autowired
  private LoginService service;

  @Test
  public void testLogin() throws RuntimeException, WMSException {
    service.login("hn001", "admin8888");
  }

}
