/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	WMSException.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月12日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.exception;

import java.text.MessageFormat;

/**
 * WMS业务异常
 * 
 * @author zhangsai
 *
 */
public class WMSException extends Exception {
    private static final long serialVersionUID = 8596402022797074626L;

    public WMSException() {
        // do nothing
    }

    public WMSException(String message) {
        super(message);
    }

    public WMSException(Throwable caught) {
        super(caught);
    }

    public WMSException(Throwable caught, String message) {
        super(message, caught);
    }

    public WMSException(String pattern, Object... arguments) {
        super(MessageFormat.format(pattern, arguments));
    }

    public WMSException(Throwable caught, String pattern, Object... arguments) {
        super(MessageFormat.format(pattern, arguments), caught);
    }
}
