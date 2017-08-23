/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ShipBillState.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月15日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.tms.shipbill;

/**
 * @author fanqingqing
 *
 */
public enum ShipBillState {
    /** 初始 */
    Initial("初始"),

    /** 装车中 */
    InProgress("装车中"),

    /** 已完成 */
    Finished("已完成");

    private ShipBillState(String caption) {
      this.caption = caption;
    }

    private String caption;

    public String getCaption() {
      return caption;
    }
}
