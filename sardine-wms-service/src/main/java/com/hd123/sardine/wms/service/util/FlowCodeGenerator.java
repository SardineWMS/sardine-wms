/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	FlowCodeGenerator.java
 * 模块说明：	
 * 修改历史：
 * 2016-12-13 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.dao.basicInfo.seq.Sequence;
import com.hd123.sardine.wms.dao.basicInfo.seq.SequenceDao;

/**
 * @author zhangsai
 * 
 */
public class FlowCodeGenerator {

  @Autowired
  private SequenceDao sequenceDao;

  private static FlowCodeGenerator instance = new FlowCodeGenerator();

  public static FlowCodeGenerator getInstance() {
    return instance;
  }

  public String allocate(String sequenceType, String companyUuid) {
    Assert.assertArgumentNotNull(sequenceType, "sequenceType");

    if (StringUtil.isNullOrBlank(companyUuid) == false)
      sequenceType = sequenceType + companyUuid;

    Sequence seq = new Sequence();
    seq.setCompanyUuid(companyUuid);
    seq.setCurrentValue(0);
    seq.setIncrement(1);
    seq.setSeqName(sequenceType);

    int currentValue = 0;
    try {
      currentValue = sequenceDao.getCurrentValue(sequenceType, companyUuid);
    } catch (Exception e) {
      sequenceDao.saveSequence(seq);
    }

    if (currentValue == 0)
      sequenceDao.saveSequence(seq);

    int seqValue = sequenceDao.getNextValue(sequenceType, companyUuid);
    return StringUtils.repeat("0", 6 - String.valueOf(seqValue).length()) + seqValue;
  }
}
