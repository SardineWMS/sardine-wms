/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	CarrierState.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月6日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.tms.carrier;

/**
 * @author yangwenzhu
 *
 */
public enum CarrierState {
    /** 已启用 */
    online("已启用"),
    /** 已停用 */
    offline("已停用");
    private String caption;

    private CarrierState(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }

}
