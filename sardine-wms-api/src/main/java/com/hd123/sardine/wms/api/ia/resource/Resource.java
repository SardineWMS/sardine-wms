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

    private String uuid;
    private String code;
    private String name;
    private String upperUuid;
    private String type;
    private boolean owned;
    private List<Resource> children = new ArrayList<>();

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

}
