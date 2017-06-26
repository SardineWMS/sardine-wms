/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	WaveType.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月20日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.out.wave;

/**
 * 波次单类型|枚举
 * 
 * @author yangwenzhu
 *
 */
public enum WaveType {
    /** 正常波次 */
    normal("正常波次"),
    /** 电商波次 */
    eCommerce("电商波次");
    private String caption;

    public String getCaption() {
        return caption;
    };

    private WaveType(String caption) {
        this.caption = caption;
    }
}
