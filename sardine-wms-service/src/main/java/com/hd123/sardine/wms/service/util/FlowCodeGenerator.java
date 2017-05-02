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
import com.hd123.sardine.wms.dao.basicInfo.seq.SequenceDao;

/**
 * @author zhangsai
 * 
 */
public class FlowCodeGenerator {

  @Autowired
  private SequenceDao sequenceDao;

  public String allocate(String sequenceType, String companyUuid, int length) {
    Assert.assertArgumentNotNull(sequenceType, "sequenceType");
    int seqValue = sequenceDao.getNextValue(sequenceType, companyUuid);
    return StringUtils.repeat("0", length - String.valueOf(seqValue).length()) + seqValue;
  }

  public String allocateWithDate(String sequenceType, String companyUuid, int length) {
    Assert.assertArgumentNotNull(sequenceType, "sequenceType");
    Assert.assertArgumentNotNull(companyUuid, "companyUuid");

    int seqValue = sequenceDao.getNextValueWithDate(sequenceType, companyUuid);
    return StringUtils.repeat("0", length - String.valueOf(seqValue).length()) + seqValue;
  }
}
