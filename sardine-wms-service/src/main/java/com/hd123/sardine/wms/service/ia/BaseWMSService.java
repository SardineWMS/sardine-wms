/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	BaseIAService.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月12日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.ia;

import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.unbiz.fluentvalidator.ValidationError;
import com.hd123.sardine.wms.common.exception.EntityNotFoundException;
import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.utils.BillNumberGenerator;
import com.hd123.sardine.wms.common.validator.ValidateResult;
import com.hd123.sardine.wms.common.validator.errmsg.ValidateErrorCode;
import com.hd123.sardine.wms.service.util.FlowCodeGenerator;
import com.hd123.sardine.wms.service.util.StockBatchUtils;

/**
 * @author zhangsai
 *
 */
public abstract class BaseWMSService {

  @Autowired
  protected FlowCodeGenerator flowCodeGenerator;
  
  @Autowired
  protected StockBatchUtils stockBatchUtils;
  
  @Autowired
  protected BillNumberGenerator billNumberGenerator;

  /**
   * 检查
   * 
   * @param result
   * @throws IllegalAccessException
   * @throws VersionConflictException
   * @throws WMSException
   */
  protected void checkValidateResult(ValidateResult result)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    if (result == null || result.isSuccess() || result.getErrorNumber() <= 0)
      return;

    ValidationError error = result.getErrors().get(0);

    if (error.getErrorCode() == ValidateErrorCode.VERSIONCONFLICT)
      throw new VersionConflictException(result.toString());
    else if (error.getErrorCode() == ValidateErrorCode.ILLEGALARGUMENT)
      throw new IllegalArgumentException(result.toString());
    else if (error.getErrorCode() == ValidateErrorCode.ENTITYNOTFOUND)
      throw new EntityNotFoundException(result.toString());
    else
      throw new WMSException(result.toString());
  }
}
