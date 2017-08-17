/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	OperateMode.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月12日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.common.entity;

/**
 * 拣货、补货操作方式|枚举类
 * <ul>
 * <li>手工单据</li>
 * <li>APP</li>
 * </ul>
 * 
 * @author yangwenzhu
 *
 */
public enum OperateMode {
    /** 手工单据 */
    ManualBill("手工单据"),
    /** APP */
    APP("APP");
    private String caption;

    private OperateMode(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}
