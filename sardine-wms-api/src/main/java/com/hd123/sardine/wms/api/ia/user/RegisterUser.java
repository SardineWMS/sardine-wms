/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	RegisterUser.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月14日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.ia.user;

import java.io.Serializable;

/**
 * 注册使用对象|实体 <br>
 * <br>
 * 只是为了接收前端传入数据时使用，其他情况请使用{@link User}。
 * 
 * @author yangwenzhu
 *
 */
public class RegisterUser implements Serializable {
  private static final long serialVersionUID = 1L;

  private String uuid;
  private String code;
  private String name;
  private String phone;
  private String passwd;

  private String companyUuid;
  private String companyCode;
  private String companyName;
  private String address;
  private String homePage;
  private String companyType;
  private UserState userState = UserState.online;

  private String id;
  private String email;
  private String remark;

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getPasswd() {
    return passwd;
  }

  public void setPasswd(String passwd) {
    this.passwd = passwd;
  }

  public UserState getUserState() {
    return userState;
  }

  public void setUserState(UserState userState) {
    this.userState = userState;
  }

  public String getCompanyUuid() {
    return companyUuid;
  }

  public void setCompanyUuid(String companyUuid) {
    this.companyUuid = companyUuid;
  }

  public String getCompanyCode() {
    return companyCode;
  }

  public void setCompanyCode(String companyCode) {
    this.companyCode = companyCode;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getHomePage() {
    return homePage;
  }

  public void setHomePage(String homePage) {
    this.homePage = homePage;
  }

  public String getCompanyType() {
    return companyType;
  }

  public void setCompanyType(String companyType) {
    this.companyType = companyType;
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

}
