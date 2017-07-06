/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ReturnType.java
 * 模块说明：	
 * 修改历史：
 * 2017年7月4日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.rtn.customerreturn;

/**
 * @author yangwenzhu
 *
 */
public enum ReturnType {
    /** 好退 */
    goodReturn("好退"),
    /** 退供应商 */
    returnToSupplier("退供应商");
    private String caption;

    public String getCaption() {
        return caption;
    }

    private ReturnType(String caption) {
        this.caption = caption;
    }
}
