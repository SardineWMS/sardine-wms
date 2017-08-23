/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	DeliveryType.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月15日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.tms.shipbill;

/**
 * @author fanqingqing
 *
 */
public enum DeliveryType {
    /** 仓库送 */
    warehouse("仓库送"),
    /** 自提 */
    pickByOneSelf("自提");

    private DeliveryType(String caption) {
        this.caption = caption;
    }

    private String caption;

    public String getCaption() {
        return caption;
    }
}
