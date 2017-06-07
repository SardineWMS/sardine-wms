/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	Carrier.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月6日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.tms.carrier;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.StandardEntity;

/**
 * 承运商|实体
 * <p>
 * 标识车辆的所属单位， 用于配送中心与第三方车辆进行结算
 * 
 * @author yangwenzhu
 *
 */
public class Carrier extends StandardEntity {
    private static final long serialVersionUID = -1948750099589451601L;
    public static final String CAPTION = "承运商";

    private static final int LENGTHI_CODE = 30;
    private static final int LENGTH_NAME = 100;
    private static final int LENGTH_ADDRESS = 255;

    private String code;
    private String name;
    private String address;
    private String contactPhone;
    private String contact;
    private String companyUuid;
    private CarrierState state = CarrierState.online;

    /** 代码 */
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        Assert.assertArgumentNotNull(code, "code");
        Assert.assertStringNotTooLong(code, LENGTHI_CODE, "code");
        this.code = code;
    }

    /** 名称 */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        Assert.assertArgumentNotNull(name, "name");
        Assert.assertStringNotTooLong(name, LENGTH_NAME, "name");
        this.name = name;
    }

    /** 地址 */
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address != null)
            Assert.assertStringNotTooLong(address, LENGTH_ADDRESS, "address");
        this.address = address;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    /** 联系方式 */
    public void setContactPhone(String contactPhone) {
        if (contactPhone != null)
            Assert.assertStringNotTooLong(contactPhone, LENGTHI_CODE, "contactPhone");
        this.contactPhone = contactPhone;
    }

    /** 联系人 */
    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        if (contact != null)
            Assert.assertStringNotTooLong(contact, LENGTH_NAME, "contact");
        this.contact = contact;
    }

    /** 组织 */
    public String getCompanyUuid() {
        return companyUuid;
    }

    public void setCompanyUuid(String companyUuid) {
        this.companyUuid = companyUuid;
    }

    public CarrierState getState() {
        return state;
    }

    public void setState(CarrierState state) {
        Assert.assertArgumentNotNull(state, "state");
        this.state = state;
    }

    public void validate() {
        Assert.assertArgumentNotNull(code, "code");
        Assert.assertArgumentNotNull(name, "name");
        Assert.assertArgumentNotNull(state, "state");
    }

}
