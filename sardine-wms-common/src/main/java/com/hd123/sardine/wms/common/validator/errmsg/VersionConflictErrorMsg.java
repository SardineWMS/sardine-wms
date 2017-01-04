/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * <p/>
 * 文件名：	VersionConflictErrorMsg.java
 * 模块说明：
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.validator.errmsg;

import com.hd123.sardine.wms.common.validator.GenericErrorMsg;

/**
 * 版本错误信息
 *
 * @author zhangsai
 */
public class VersionConflictErrorMsg extends GenericErrorMsg {

    public static VersionConflictErrorMsg create(String errorMsg) {
        return new VersionConflictErrorMsg(errorMsg);
    }

    public VersionConflictErrorMsg(String errorMsg) {
        setErrorCode(ValidateErrorCode.VERSIONCONFLICT);
        setErrorMsg(errorMsg);
    }
}
