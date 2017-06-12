/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	VehicleState.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月6日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.tms.vehicle;

/**
 * @author yangwenzhu
 *
 */
public enum VehicleState {

    free("空闲"),

    unUse("待排车"),

    used("已排车"),

    shiping("装车中"),

    shiped("已装车"),

    inAlc("配送中"),

    offline("已停用");

    private String caption;

    private VehicleState(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}
