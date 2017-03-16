/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	Customer.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月14日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.ia.user;

import com.hd123.sardine.wms.common.entity.StandardEntity;

/**
 * 企业|实体类 <br>
 * <br>
 * 包含以下基本信息
 * <ul>
 * <li>企业代码</li>
 * <li>企业名称</li>
 * <li>企业地址</li>
 * <li>企业主页</li>
 * <li>企业类型</li>
 * </ul>
 * 
 * @author yangwenzhu
 *
 */
public class Company extends StandardEntity {
    private static final long serialVersionUID = -6948568494308425486L;

    private String code;
    private String name;
    private String address;
    private String homePage;
    private CompanyType companyType;

    /** 企业代码 */
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /** 企业名称 */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** 企业地址 */
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /** 企业主页 */
    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    /** 企业类型 */
    public CompanyType getCompanyType() {
        return companyType;
    }

    public void setCompanyType(CompanyType companyType) {
        this.companyType = companyType;
    }

}
