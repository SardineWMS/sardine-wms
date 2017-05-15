/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	RegisterInfo.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月4日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.ia.user;

import java.io.Serializable;

import com.hd123.rumba.commons.lang.Assert;

/**
 * 注册信息
 * 
 * @author zhangsai
 *
 */
public class RegisterInfo implements Serializable {
  private static final long serialVersionUID = -8827349899172358556L;

  private String loginId;
  private String companyName;
  private String passwd;

  public String getLoginId() {
    return loginId;
  }

  public void setLoginId(String loginId) {
    this.loginId = loginId;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getPasswd() {
    return passwd;
  }

  public void setPasswd(String passwd) {
    this.passwd = passwd;
  }

  public void validate() {
    Assert.assertArgumentNotNull(loginId, "loginId");
    Assert.assertArgumentNotNull(passwd, "passwd");
    Assert.assertArgumentNotNull(companyName, "companyName");
  }
}
