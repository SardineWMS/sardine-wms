/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ReturnNtcBillState.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月27日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.rtn.ntc;

/**
 * 退仓通知单状态|枚举
 * 
 * @author yangwenzhu
 *
 */
public enum ReturnNtcBillState {
    /** 初始 */
    initial("初始"),
    /** 已完成 */
    finished("已完成"),
    /** 已作废 */
    aborted("已作废"),
    /** 进行中 */
    inProgress("进行中");

    private String caption;

    public String getCaption() {
        return caption;
    }

    private ReturnNtcBillState(String caption) {
        this.caption = caption;
    }
}
