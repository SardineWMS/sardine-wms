/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	CompanyType.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月14日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.ia.user;

/**
 * 企业类型 |枚举类
 * 
 * @author yangwenzhu
 *
 */
public enum CompanyType {
    /** 配送中心 */
    deliveryCenter("配送中心"),
    /** 供应商 */
    supplier("供应商"),
    /** 承运商 */
    carrier("承运商");

    private String caption;

    private CompanyType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}
