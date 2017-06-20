/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	LineCustomer.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月17日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.web.tms.serialarch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 构造对象，用于接受前端传入参数
 * 
 * @author yangwenzhu
 *
 */
public class LineCustomer implements Serializable {
    private static final long serialVersionUID = 5406667550691977826L;
    private String lineUuid;
    private List<String> customers = new ArrayList<>();

    public String getLineUuid() {
        return lineUuid;
    }

    public void setLineUuid(String lineUuid) {
        this.lineUuid = lineUuid;
    }

    public List<String> getCustomers() {
        return customers;
    }

    public void setCustomers(List<String> customers) {
        this.customers = customers;
    }

}
