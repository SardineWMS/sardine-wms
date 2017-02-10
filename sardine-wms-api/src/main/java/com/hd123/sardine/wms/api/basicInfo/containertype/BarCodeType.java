/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	BarCodeType.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月6日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.containertype;

/**
 * @author fanqingqing
 *
 */
public enum BarCodeType {

    ONCE("一次性"), FOREVER("永久");

    private String caption;

    private BarCodeType(String caption) {
      this.caption = caption;
    }

    public String getCaption() {
      return caption;
    }

  }

