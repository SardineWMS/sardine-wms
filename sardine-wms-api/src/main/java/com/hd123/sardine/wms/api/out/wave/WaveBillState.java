/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	WaveBillState.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月20日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.out.wave;

/**
 * 波次单状态|枚举
 * 
 * @author yangwenzhu
 *
 */
public enum WaveBillState {
    /** 初始 */
    initial("初始"),
    /** 启动中 */
    inProgress("启动中"),
    /** 启动异常 */
    exception("启动异常"),
    /** 启动完成 */
    started("启动完成"),
    /** 配货中 */
    inAlc("配货中"),
    /** 已完成 */
    finished("已完成");
    private String caption;

    public String getCaption() {
        return caption;
    };

    private WaveBillState(String caption) {
        this.caption = caption;
    }
}
