/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	SerialArchLineCustomer.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月14日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.tms.serialarch;

import java.io.Serializable;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * 客户顺序|实体
 * 
 * @author yangwenzhu
 *
 */
public class SerialArchLineCustomer implements Serializable {
    private static final long serialVersionUID = -7219575648582022167L;

    private UCN customer;
    private int order;
    private String serialArchLineUuid;

    public UCN getCustomer() {
        return customer;
    }

    /** 客户 */
    public void setCustomer(UCN customer) {
        Assert.assertArgumentNotNull(customer, "customer");
        this.customer = customer;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getSerialArchLineUuid() {
        return serialArchLineUuid;
    }

    public void setSerialArchLineUuid(String serialArchLineUuid) {
        Assert.assertArgumentNotNull(serialArchLineUuid, "serialArchLineUuid");
        this.serialArchLineUuid = serialArchLineUuid;
    }

    public void validate() {
        Assert.assertArgumentNotNull(serialArchLineUuid, "serialArchLineUuid");
        Assert.assertArgumentNotNull(customer, "customer");
        Assert.assertArgumentNotNull(customer.getUuid(), "customer.uuid");

    }

}
