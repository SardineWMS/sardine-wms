/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	CustomerType.java
 * 模块说明：	
 * 修改历史：
 * 2017年1月12日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.customer;

/**
 * 客户类型|枚举类
 * 
 * @author yangwenzhu
 *
 */
public enum CustomerType {
    /** 百货 */
    store("百货"),
    /** 精品店 */
    shop("精品店");
    private String caption;

    private CustomerType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}
