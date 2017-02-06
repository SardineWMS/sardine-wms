/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	Customer.java
 * 模块说明：	
 * 修改历史：
 * 2017年1月12日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.customer;

import com.hd123.sardine.wms.common.entity.StandardEntity;

/**
 * 客户|实体类
 * 
 * @author yangwenzhu
 *
 */
public class Customer extends StandardEntity {
    private static final long serialVersionUID = 1L;

    private String code;
    private String name;
    private CustomerType type;
    private String phone;
    private String address;
    private CustomerState state;
    private String companyUuid;
    private String remark;

    /** 说明 */
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /** 客户代码 */
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /** 客户名称 */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** 客户类型 */
    public CustomerType getType() {
        return type;
    }

    public void setType(CustomerType type) {
        this.type = type;
    }

    /** 客户联系方式 */
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /** 客户地址 */
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /** 客户状态 */
    public CustomerState getState() {
        return state;
    }

    public void setState(CustomerState state) {
        this.state = state;
    }

    /** 所属组织UUID */
    public String getCompanyUuid() {
        return companyUuid;
    }

    public void setCompanyUuid(String companyUuid) {
        this.companyUuid = companyUuid;
    }
}
