/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	Role.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月23日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.ia.role;

import com.hd123.sardine.wms.common.entity.StandardEntity;

/**
 * 认证授权：角色 实体
 * 
 * @author zhangsai
 *
 */
public class Role extends StandardEntity {
  private static final long serialVersionUID = 7747961149895850151L;

  private String code;
  private String name;
  private String orgId;
  private RoleState state = RoleState.online;

  public String getOrgId() {
    return orgId;
  }

  public void setOrgId(String orgId) {
    this.orgId = orgId;
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

  public RoleState getState() {
    return state;
  }

  public void setState(RoleState state) {
    this.state = state;
  }
}
