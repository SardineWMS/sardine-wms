/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	RplQtyMode.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月12日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.pickarea;

/**
 * 补货量模式|枚举
 * <ul>
 * <li>最高库存</li>
 * <li>够出货量</li>
 * </ul>
 * 
 * @author yangwenzhu
 *
 */
public enum RplQtyMode {
    /** 够出货量 */
    enoughShipments("够出货量"),
    /** 最高库存 */
    highestStock("最高库存");

    private String caption;

    private RplQtyMode(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}
