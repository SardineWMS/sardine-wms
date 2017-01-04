/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	IAException.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月12日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.exception;

import java.text.MessageFormat;

/**
 * IA业务异常
 * 
 * @author zhangsai
 *
 */
public class IAException extends Exception {
    private static final long serialVersionUID = 8596402022797074626L;

    public IAException() {
        // do nothing
    }

    public IAException(String message) {
        super(message);
    }

    public IAException(Throwable caught) {
        super(caught);
    }

    public IAException(Throwable caught, String message) {
        super(message, caught);
    }

    public IAException(String pattern, Object... arguments) {
        super(MessageFormat.format(pattern, arguments));
    }

    public IAException(Throwable caught, String pattern, Object... arguments) {
        super(MessageFormat.format(pattern, arguments), caught);
    }
}
