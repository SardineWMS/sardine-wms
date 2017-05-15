/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	Resource.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月23日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.ia.resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 资源权限
 * <p>
 * 资源作为权限，由系统维护。<br>
 * 资源的下级也可以是资源也可以是操作，资源和操作都是权限。<br>
 * 举例：基本资料（资源）的下级为供应商（资源）。供应商的下级为供应商-新建（操作）、供应商-删除、供应商-查看等，
 * 当用户拥有供应商资源的权限时就相当于有了供应商的所有操作权限。总结：用户拥有某个操作的权限则只拥有该操作权限，如果拥有某个资源，
 * 则同时拥有该资源下的所有操作权限。
 * 
 * @author zhangsai
 *
 */
public class Resource implements Serializable {
  private static final long serialVersionUID = -9183507457817520821L;

  public static final String BASIC_RESOURCE_UUID = "0001";
  public static final String SYSTEM_RESOURCE_UUID = "0000";
  
  public static final List<String> SYSTEM_ADMIN_RESOURCES = new ArrayList<String>();
  static {
    SYSTEM_ADMIN_RESOURCES.add(SYSTEM_RESOURCE_UUID);
    SYSTEM_ADMIN_RESOURCES.add(BASIC_RESOURCE_UUID);
  }
  
  public static final List<String> DC_ADMIN_RESOURCES = new ArrayList<String>();
  static {
    DC_ADMIN_RESOURCES.add(BASIC_RESOURCE_UUID);
    DC_ADMIN_RESOURCES.add(SYSTEM_RESOURCE_UUID);
  }

  private String uuid;
  private String code;
  private String name;
  private String upperUuid;
  private String type;
  private boolean owned;
  private List<Resource> children = new ArrayList<Resource>();

  public boolean isOwned() {
    return owned;
  }

  public void setOwned(boolean owned) {
    this.owned = owned;
  }

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

  public String getUpperUuid() {
    return upperUuid;
  }

  public void setUpperUuid(String upperUuid) {
    this.upperUuid = upperUuid;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public List<Resource> getChildren() {
    return children;
  }

  public void setChildren(List<Resource> children) {
    this.children = children;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((children == null) ? 0 : children.hashCode());
    result = prime * result + ((code == null) ? 0 : code.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + (owned ? 1231 : 1237);
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    result = prime * result + ((upperUuid == null) ? 0 : upperUuid.hashCode());
    result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Resource other = (Resource) obj;
    if (children == null) {
      if (other.children != null)
        return false;
    } else if (!children.equals(other.children))
      return false;
    if (code == null) {
      if (other.code != null)
        return false;
    } else if (!code.equals(other.code))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (owned != other.owned)
      return false;
    if (type == null) {
      if (other.type != null)
        return false;
    } else if (!type.equals(other.type))
      return false;
    if (upperUuid == null) {
      if (other.upperUuid != null)
        return false;
    } else if (!upperUuid.equals(other.upperUuid))
      return false;
    if (uuid == null) {
      if (other.uuid != null)
        return false;
    } else if (!uuid.equals(other.uuid))
      return false;
    return true;
  }

}
