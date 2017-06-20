/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	SerialArchLine.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月14日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.tms.serialarch;

import java.util.ArrayList;
import java.util.List;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.StandardEntity;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * 运输线路|实体
 * 
 * @author yangwenzhu
 *
 */
public class SerialArchLine extends StandardEntity {
    private static final long serialVersionUID = 2705376298925093732L;
    public static final String CAPTION = "运输线路";
    private static final int LENGTH_CODE = 30;
    private static final int LENGTH_NAME = 100;

    private String code;
    private String name;
    private UCN serialArch;
    private String companyUuid;

    private List<SerialArchLineCustomer> customers = new ArrayList<>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        Assert.assertArgumentNotNull(code, "code");
        Assert.assertStringNotTooLong(code, LENGTH_CODE, "code");
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Assert.assertArgumentNotNull(name, "name");
        Assert.assertStringNotTooLong(name, LENGTH_NAME, "name");
        this.name = name;
    }

    public UCN getSerialArch() {
        return serialArch;
    }

    public void setSerialArch(UCN serialArch) {
        Assert.assertArgumentNotNull(serialArch, "serialArch");
        this.serialArch = serialArch;
    }

    public List<SerialArchLineCustomer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<SerialArchLineCustomer> customers) {
        this.customers = customers;
    }

    public String getCompanyUuid() {
        return companyUuid;
    }

    public void setCompanyUuid(String companyUuid) {
        Assert.assertArgumentNotNull(companyUuid, "companyUuid");
        this.companyUuid = companyUuid;
    }

    public void validate() {
        Assert.assertArgumentNotNull(code, "code");
        Assert.assertArgumentNotNull(name, "name");
        Assert.assertArgumentNotNull(serialArch, "serialArch");
        Assert.assertArgumentNotNull(companyUuid, "companyUuid");
    }

    public String toFriendString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        sb.append(code);
        sb.append("]");
        sb.append(name);
        return sb.toString();
    }
}
