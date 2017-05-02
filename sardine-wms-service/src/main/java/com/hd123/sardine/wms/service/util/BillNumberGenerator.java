/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	BillNumberGenerator.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月2日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.util;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.dao.basicInfo.seq.SequenceDao;

/**
 * @author zhangsai
 *
 */
public class BillNumberGenerator {

  @Autowired
  private FlowCodeGenerator flowCodeGenerator;

  @Autowired
  private SequenceDao sequenceDao;

  public String allocate(String billType) {
    Assert.assertArgumentNotNull(billType, "billType");
    String flowCode = flowCodeGenerator.allocateWithDate(billType,
        ApplicationContextUtil.getCompanyUuid(), 6);
    String datePart = sequenceDao.getCurrDatePart(billType,
        ApplicationContextUtil.getCompanyUuid());
    return ApplicationContextUtil.getCompanyCode() + billType + datePart + flowCode;
  }
}
