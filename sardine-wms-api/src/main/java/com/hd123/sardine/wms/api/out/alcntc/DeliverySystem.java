/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	DeliverySystem.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月31日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.out.alcntc;

/**
 * 配货体系|枚举
 * 
 * @author yangwenzhu
 *
 */
public enum DeliverySystem {
    /** 传统体系 */
    tradition("传统体系"),
    /** 电商体系 */
    eCommerce("电商体系");

    private String caption;

    public String getCaption() {
        return caption;
    }

    private DeliverySystem(String caption) {
        this.caption = caption;
    }
}
