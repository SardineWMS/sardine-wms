/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	SerializeException.java
 * 模块说明：	
 * 修改历史：
 * 2016-12-12 -zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.exception;

import java.text.MessageFormat;

/**
 * 序例化异常。
 * 
 * @author zhangsai
 */
public class SerializeException extends RuntimeException {
    private static final long serialVersionUID = -2103457205643499634L;

    public SerializeException() {
        super();
    }

    public SerializeException(String s) {
        super(s);
    }

    public SerializeException(String pattern, Object... parameters) {
        super(MessageFormat.format(pattern, parameters));
    }

    public SerializeException(Exception e) {
        super(e);
    }

    public SerializeException(Exception e, String s) {
        super(s, e);
    }

    public SerializeException(Exception e, String pattern, Object... parameters) {
        this(e, MessageFormat.format(pattern, parameters));
    }
}
