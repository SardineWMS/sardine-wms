/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	Bin.java
 * 模块说明：	
 * 修改历史：
 * 2017-1-17 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.bin;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.entity.VersionedEntity;

/**
 * 货位
 * 
 * @author zhangsai
 * 
 */
public class Bin extends VersionedEntity {

  private static final long serialVersionUID = -5349677209950897452L;
  public static final int LENGTH_CODE = 8;
  public static final int LENGTH_LEVEL = 1;
  public static final String VIRTUALITY_BIN = "-";


  private String code;
  private UCN binType;
  private UCN wrh;
  private String shelfUuid;
  private BinUsage usage;
  private String binLevel;
  private String binColumn;
  private BinState state = BinState.free;

  private String companyUuid;

  /** 货位代码 */
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    Assert.assertStringNotTooLong(code, LENGTH_CODE, "code");

    this.code = code;
  }

  /** 货位类型uuid */
  public UCN getBinType() {
    return binType;
  }

  public void setBinType(UCN binType) {
    this.binType = binType;
  }

  /** 所属货架uuid */
  public String getShelfUuid() {
    return shelfUuid;
  }

  public void setShelfUuid(String shelfUuid) {
    Assert.assertArgumentNotNull(shelfUuid, "shelfUuid");

    this.shelfUuid = shelfUuid;
  }

  /** 货位用途 */
  public BinUsage getUsage() {
    return usage;
  }

  public void setUsage(BinUsage usage) {
    Assert.assertArgumentNotNull(usage, "usage");

    this.usage = usage;
  }
  
  public UCN getWrh() {
    return wrh;
  }

  public void setWrh(UCN wrh) {
    this.wrh = wrh;
  }

  /** 货位所在层 */
  public String getBinLevel() {
    return binLevel;
  }

  public void setBinLevel(String level) {
    Assert.assertArgumentNotNull(level, "level");
    Assert.assertStringNotTooLong(level, LENGTH_LEVEL, "level");

    this.binLevel = level;
  }

  /** 货位所在列 */
  public String getBinColumn() {
    return binColumn;
  }

  public void setBinColumn(String column) {
    Assert.assertArgumentNotNull(column, "column");
    Assert.assertStringNotTooLong(column, LENGTH_LEVEL, "column");
    this.binColumn = column;
  }

  public BinState getState() {
    return state;
  }

  public void setState(BinState state) {
    this.state = state;
  }

  public String getCompanyUuid() {
    return companyUuid;
  }

  public void setCompanyUuid(String companyUuid) {
    this.companyUuid = companyUuid;
  }
}
