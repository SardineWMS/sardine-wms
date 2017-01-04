/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	FlowCodeGenerator.java
 * 模块说明：	
 * 修改历史：
 * 2016-12-13 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.utils;

import com.hd123.rumba.commons.lang.Assert;

/**
 * @author zhangsai
 * 
 */
public class FlowCodeGenerator {
  /** 流水号起始值 */
  private static final long FLOW_START = 1L;
  /** 流水号最大值 */
  private static long FLOW_MAX = 99999L;
  /** 序列类型最大长度 */
  private static final int MAX_SEQUENCE_TYPE_LENGTH = 10;

  private static FlowCodeGenerator instance = new FlowCodeGenerator();

  public static FlowCodeGenerator getInstance() {
    return instance;
  }

  public String allocate(String sequenceType) {
    Assert.assertArgumentNotNull(sequenceType, "sequenceType");
    Assert.assertStringNotTooLong(sequenceType, MAX_SEQUENCE_TYPE_LENGTH, "sequenceType");

    SequenceSpec seq = new SequenceSpec();
    seq.setDbId(sequenceType);
    seq.setStartValue(FLOW_START);
    seq.setMaxValue(FLOW_MAX);
    seq.setIncrement(1L);
    return String.valueOf(new OracleSequenceGenerator().nextValue(seq));
  }

  public String getCurrentCode(String sequenceType) {
    Assert.assertArgumentNotNull(sequenceType, "sequenceType");
    Assert.assertStringNotTooLong(sequenceType, MAX_SEQUENCE_TYPE_LENGTH, "sequenceType");

    SequenceSpec seq = new SequenceSpec();
    seq.setDbId(sequenceType);
    seq.setStartValue(FLOW_START);
    seq.setMaxValue(FLOW_MAX);
    seq.setIncrement(1L);
    return String.valueOf(new OracleSequenceGenerator().currentValue(seq));
  }
}
