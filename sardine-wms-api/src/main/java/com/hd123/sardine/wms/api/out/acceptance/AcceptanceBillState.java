/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	AcceptanceBillState.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月5日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.out.acceptance;

/**
 * @author fanqingqing
 *
 */
public enum AcceptanceBillState {

    Initial("初始"),

    Approved("已批准"),
    
    InAlc("配货中"),

    Finished("已完成"),

    Aborted("已作废");

    private String caption;

    private AcceptanceBillState(String caption) {
      this.caption = caption;
    }

    public String getCaption() {
      return caption;
    }
}
