/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	BShipBill.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月18日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.web.tms.shipbill;

import com.hd123.sardine.wms.api.tms.shipbill.ShipBill;

/**
 * @author fanqingqing
 *
 */
public class BShipBill extends ShipBill {
    private static final long serialVersionUID = 1290109541589351922L;

    private int customerCount = 0;
    private int containerCount = 0;

    public int getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(int customerCount) {
        this.customerCount = customerCount;
    }

    public int getContainerCount() {
        return containerCount;
    }

    public void setContainerCount(int containerCount) {
        this.containerCount = containerCount;
    }

}
