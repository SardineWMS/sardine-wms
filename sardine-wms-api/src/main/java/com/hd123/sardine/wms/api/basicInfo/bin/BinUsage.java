/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	BinUsage.java
 * 模块说明：	
 * 修改历史：
 * 2017-1-17 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.bin;

/**
 * 货位用途
 * 
 * @author zhangsai
 * 
 */
public enum BinUsage {
    /** 存储位 */
    StorageBin("存储位"),
    /** 拣货存储位 */
    PickUpStorageBin("拣货存储位"),
    /** 收货暂存位 */
    ReceiveStorageBin("收货暂存位"),
    /** 集货位 */
    CollectBin("集货位"),
    /** 供应商集货位 */
    SupplierCollectBin("供应商集货位"),
    /** 退仓收货暂存位 */
    RtnReceiveTempBin("退仓收货暂存位");

    private String caption;

    private BinUsage(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}
