/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	AlcNtcBillState.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月31日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.out.alcntc;

/**
 * 配单状态|枚举
 * 
 * @author yangwenzhu
 *
 */
public enum AlcNtcBillState {
    /** 初始 */
    initial("初始"),
    /** 已作废 */
    aborted("已作废"),
    /** 待配送 */
    inAlc("待配送"),
    /** 分拣中 */
    inSorting("分拣中"),
    /** 已完成 */
    finished("已完成"),
    /** 配送中 */
    inProgress("配送中"),
    /** 已交接 */
    handover("已交接");

    private String caption;

    public String getCaption() {
        return caption;
    }

    private AlcNtcBillState(String caption) {
        this.caption = caption;
    }
}
