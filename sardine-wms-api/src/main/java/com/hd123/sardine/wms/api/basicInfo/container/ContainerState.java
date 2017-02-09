/**
 * 版权所有(C)，LHWMS项目组，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ContainerState.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月7日 - Jing - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.container;

/**
 * @author Jing
 *
 */
public enum ContainerState {

    /** 空闲 */
    STACONTAINERIDLE("空闲"),
    /** 收货中 */
    STACONTAINERSTKINING("收货中"),
    /** 已使用 */
    STACONTAINERUSEING("已使用"),
    /** 拣货中 */
    STACONTAINERPICKING("拣货中"),
    /** 平移中 */
    STACONTAINERMOVING("平移中"),
    /** 上架中 */
    STACONTAINERPUTAWAYING("上架中");

    private String caption;

    private ContainerState(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }

}
