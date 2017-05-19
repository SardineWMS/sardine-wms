/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	DecIncInvBillState.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月17日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.inner.decincinv;

/**
 * 损溢单状态
 * 
 * @author fanqingqing
 *
 */
public enum DecIncInvBillState {

    Initial("未审核"),

    Audited("已审核");

    private String caption;

    private DecIncInvBillState(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}
