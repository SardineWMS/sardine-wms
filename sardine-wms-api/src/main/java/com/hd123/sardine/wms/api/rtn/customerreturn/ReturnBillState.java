/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ReturnBillState.java
 * 模块说明：	
 * 修改历史：
 * 2017年7月4日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.rtn.customerreturn;

/**
 * @author yangwenzhu
 *
 */
public enum ReturnBillState {
    /** 初始 */
    initial("初始"),
    /** 进行中 */
    inProgress("进行中"),
    /** 已完成 */
    finished("已完成");
    private String caption;

    public String getCaption() {
        return caption;
    }

    private ReturnBillState(String caption) {
        this.caption = caption;
    }
}
