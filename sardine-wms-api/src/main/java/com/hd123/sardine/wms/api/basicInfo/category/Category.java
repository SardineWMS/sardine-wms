/**
 * 版权所有(C)，LHWMS项目组，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	Category.java
 * 模块说明：	
 * 修改历史：
 * 2017年1月12日 - Jing - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.category;

import java.util.ArrayList;
import java.util.List;

import com.hd123.sardine.wms.common.entity.StandardEntity;

/**
 * @author Jing
 *
 */
public class Category extends StandardEntity {
    private static final long serialVersionUID = 2587593374439103983L;
    public static final String DEFAULT_ROOTCATEGORY = "-";

    private String code;
    private String name;
    private String upperCategory = DEFAULT_ROOTCATEGORY;
    private String companyUuid;
    private String remark;

    private List<Category> children = new ArrayList<>();

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

    public String getUpperCategory() {
        return upperCategory;
    }

    public void setUpperCategory(String upperCategory) {
        this.upperCategory = upperCategory;
    }

    public String getCompanyUuid() {
        return companyUuid;
    }

    public void setCompanyUuid(String companyUuid) {
        this.companyUuid = companyUuid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<Category> getChildren() {
        return children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }
}
