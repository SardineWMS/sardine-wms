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
  /** 拣货位 */
  PickUpBin("拣货位"),

  /** 收货暂存位 */
  ReceiveStorageBin("收货暂存位"),
  /** 统配更正位 */
  UnifyAdjBin("统配更正位"),
  /** 越库更正位 */
  TranSitAndStraightAdjBin("越库更正位"),
  /** 直通收货暂存位 */
  StraightReceiveStorageBin("直通收货暂存位"),
  /** 中转收货暂存位 */
  TransitReceiveStorageBin("中转收货暂存位"),

  /** 集货位 */
  CollectBin("集货位"),

  /** 统配集货暂存位 */
  UnifyCollectTemporaryBin("统配集货暂存位"),
  /** 直通集货暂存位 */
  StraightCollectTemporaryBin("直通集货暂存位"),
  /** 中转集货暂存位 */
  TransitCollectTemporaryBin("中转集货暂存位"),
  /** 上架中转位 */
  PickTransitBin("上架中转位"),

  /** 供应商集货位 */
  SupplierCollectBin("供应商集货位"),
  /** 退货集货暂存位 */
  SupplierRtnCollectTempBin("退货集货暂存位"),
  /** 供应商退货中转位 */
  SupplierReturnTrasitBin("供应商退货中转位"),
  /** 供应商退货下架暂存位 */
  SupplierReturnGetDownBin("供应商退货下架暂存位"),

  /** 自动库存储位 */
  AutoStorageBin("自动库存储位"),
  /** 自动库拣货存储位 */
  AutoPickStorageBin("自动库拣货存储位"),
  /** 自动库提总暂存位 */
  AutoTotalPickTempBin("自动库提总暂存位"),
  /** 自动库分拨位 */
  AutoAllocateBin("自动库分拨位"),
  /** 自动库上架中转位 */
  AutoPutAwayTransitBin("自动库上架中转位"),

  /** 供应商分拨位 */
  SupplierAllocateBin("供应商分拨位"),
  /** 退货退仓位 */
  QueryRtnWrhBin("退货退仓位"),
  /** 供应商退货位 */
  SupplierReturnBin("供应商退货位"),
  /** 调剂退仓位 */
  TransfersRtnBin("调剂退仓位"),
  /** 拣货暂存位 */
  PickUpTemporaryBin("拣货暂存位"),
  /** 门店分拨位 */
  StoreAllocateBin("门店分拨位"),
  /** 补货暂存位 */
  RplTemporaryBin("补货暂存位"),
  /** 移库暂存位 */
  MoveTemporaryBin("移库暂存位"),
  /** 异常处理位 */
  ExceptionBin("异常处理位"),

  /** 所有货位 */
  AllBin("所有货位");

  private String caption;

  private BinUsage(String caption) {
    this.caption = caption;
  }

  public String getCaption() {
    return caption;
  }
}
