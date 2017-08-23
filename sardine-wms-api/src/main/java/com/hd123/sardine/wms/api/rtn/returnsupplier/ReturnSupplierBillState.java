/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	SupplierReturnBillState.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月21日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.rtn.returnsupplier;

/**
 * @author fanqingqing
 *
 */
public enum ReturnSupplierBillState {
    /** 初始 */
    Initial("初始"),

    /** 进行中 */
    InProgress("进行中"),

    /** 已完成 */
    Finished("已完成");

    private ReturnSupplierBillState(String caption) {
        this.caption = caption;
    }

    private String caption;

    public String getCaption() {
        return caption;
    }
}
