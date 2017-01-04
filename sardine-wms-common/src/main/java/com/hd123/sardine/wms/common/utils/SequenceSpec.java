package com.hd123.sardine.wms.common.utils;

import java.io.Serializable;

/**
 * 序列定义信息
 * 
 * @author zhangsai
 * 
 */
public class SequenceSpec implements Serializable {
  private static final long serialVersionUID = 300200L;

  /** 在数据库中的唯一标识，不能超过18位（ORACLE是30位） */
  private String dbId;

  /** 起始值 */
  private long startValue;

  /** 最大值 */
  private long maxValue;

  /** 增量 */
  private long increment = 1L;

  /** 数据库序列标识(不包括前缀和后缀部分)，不超过18位 */
  public String getDbId() {
    return dbId;
  }

  public void setDbId(String dbId) {
    this.dbId = dbId;
  }

  /** 起始值 */
  public long getStartValue() {
    return startValue;
  }

  public void setStartValue(long startValue) {
    this.startValue = startValue;
  }

  /** 最大值 */
  public long getMaxValue() {
    return maxValue;
  }

  public void setMaxValue(long maxValue) {
    this.maxValue = maxValue;
  }

  /** 增量 */
  public long getIncrement() {
    return increment;
  }

  public void setIncrement(long increment) {
    this.increment = increment;
  }

}
