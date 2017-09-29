/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	User.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月13日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.ia.user;

import com.hd123.sardine.wms.api.ia.login.UserInfo;
import com.hd123.sardine.wms.common.entity.StandardEntity;

/**
 * 用户实体
 * <p>
 * 新用户注册、管理员新增用户<br>
 * 管理员用户可对该公司下用户进行管理，包括新增、删除、编辑、启用、禁用，普通用户无法对其他用户进行修改<br>
 * 新增用户的初始密码为888888，用户第一次登陆时系统提示修改密码
 * 
 * @author zhangsai
 *
 */
public class User extends StandardEntity {
  public static final String DEFAULT_PASSWD = "888888";
  public static final String DEFAULT_ADMIN_PASSWD = "admin8888";
  public static final String COMPANYUUID_FLOWTYPE = "companyid";
  public static final String COMPANYCODE_PREFIX = "8";

  private static final long serialVersionUID = -691450513005794988L;

  private String code;
  private String name;
  private String phone;
  private String passwd;
  private boolean administrator;

  private String companyUuid;
  private String companyCode;
  private String companyName;

  private String id;
  private String email;
  private String remark;

  private UserState userState = UserState.online;

  public UserState getUserState() {
    return userState;
  }

  public void setUserState(UserState userState) {
    this.userState = userState;
  }

  /** 指示当前用户是否是管理员用户，新注册的用户默认为管理员用户，管理员在新建时可指定新建用户是否是管理员 */
  public boolean isAdministrator() {
    return administrator;
  }

  public void setAdministrator(boolean administrator) {
    this.administrator = administrator;
  }

  /** 用户代码，由用户注册或者新增时指定，全局唯一，由字母和数字组成，长度不超过30位 */
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  /** 用户名称，长度不超过100 */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /** 用户电话 */
  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  /** 用户密码，新增用户初始密码为888888，注册用户可指定自己的密码 */
  public String getPasswd() {
    return passwd;
  }

  public void setPasswd(String passwd) {
    this.passwd = passwd;
  }

  /** 用户所属公司id，注册用户由系统生成，新增用户则根据当前管理员所属公司进行填充 */
  public String getCompanyUuid() {
    return companyUuid;
  }

  public void setCompanyUuid(String companyUuid) {
    this.companyUuid = companyUuid;
  }

  /** 用户所属公司代码，注册用户由系统生成，新增用户则根据当前管理员所属公司进行填充 */
  public String getCompanyCode() {
    return companyCode;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public void setCompanyCode(String companyCode) {
    this.companyCode = companyCode;
  }

  /** 用户所属公司id，注册用户由用户指定，新增用户则根据当前管理员所属公司进行填充 */
  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  /**
   * 获取用户登录信息
   */
  public UserInfo fetchUserInfo() {
    UserInfo info = new UserInfo();
    info.setCode(code);
    info.setName(name);
    info.setUuid(getUuid());
    info.setCompanyCode(companyCode);
    info.setCompanyUuid(companyUuid);
    info.setCompanyName(companyName);
    info.setAdministrator(administrator);
    return info;
  }
}
