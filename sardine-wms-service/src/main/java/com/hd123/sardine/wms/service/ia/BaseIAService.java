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

import com.baidu.unbiz.fluentvalidator.ValidationError;
import com.hd123.sardine.wms.common.exception.EntityNotFoundException;
import com.hd123.sardine.wms.common.exception.IAException;
import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.validator.ValidateResult;
import com.hd123.sardine.wms.common.validator.errmsg.ValidateErrorCode;

/**
 * @author zhangsai
 *
 */
public abstract class BaseIAService {
    /**
     * 检查
     * 
     * @param result
     * @throws IllegalAccessException
     * @throws VersionConflictException
     * @throws WMSException
     */
    protected void checkValidateResult(ValidateResult result)
            throws IllegalArgumentException, VersionConflictException, IAException {
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
            throw new IAException(result.toString());
    }
}
