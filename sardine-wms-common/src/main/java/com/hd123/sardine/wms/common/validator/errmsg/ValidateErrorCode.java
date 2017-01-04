/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * <p/>
 * 文件名：	ValidateErrorCode.java
 * 模块说明：
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.validator.errmsg;

/**
 * 验证结果错误码
 *
 * @author zhangsai
 */
public class ValidateErrorCode {

    /** 无效参数错误 */
    public static final int ILLEGALARGUMENT = 100;

    /** 版本错误 */
    public static final int VERSIONCONFLICT = 200;

    /** 实体不存在错误 */
    public static final int ENTITYNOTFOUND = 300;

    /** 默认或未归纳的错误 */
    public static final int DEFAULT = 999;
}
