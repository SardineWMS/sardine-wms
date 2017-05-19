/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	DecIncInvBillType.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月17日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.inner.decincinv;

/**
 * 损益单类型
 * 
 * @author fanqingqing
 *
 */
public enum DecIncInvBillType {

    Dec("损耗"),

    Inc("溢余");

    private String caption;

    private DecIncInvBillType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}
