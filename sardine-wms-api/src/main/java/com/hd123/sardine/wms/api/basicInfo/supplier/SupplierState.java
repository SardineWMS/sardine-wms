/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	SupplierState.java
 * 模块说明：	
 * 修改历史：
 * 2017年1月9日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.supplier;

/**
 * 供应商状态
 * 
 * @author fanqingqing
 *
 */
public enum SupplierState {
    /** 启用 */
    online("启用"),
    /** 停用 */
    offline("停用");

    private String caption;

    private SupplierState(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}
