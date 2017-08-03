/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	OperateMethod.java
 * 模块说明：	
 * 修改历史：
 * 2017年7月10日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.rtn.customerreturn;

/**
 * @author yangwenzhu
 *
 */
public enum OperateMethod {
    /** 手工单据 */
    ManualBill("手工单据"),
    /** APP */
    APP("APP");
    private String caption;

    public String getCaption() {
        return caption;
    }

    private OperateMethod(String caption) {
        this.caption = caption;
    }
}
