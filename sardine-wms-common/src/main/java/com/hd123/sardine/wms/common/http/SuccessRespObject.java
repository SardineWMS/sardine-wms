/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * <p/>
 * 文件名：	SuccessRespObject.java
 * 模块说明：
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.http;

/**
 * 默认成功的响应对象
 *
 * @author zhangsai
 */
public class SuccessRespObject extends RespObject {
    private static final long serialVersionUID = -5701798642257837532L;

    public SuccessRespObject() {
        this.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    }

    public SuccessRespObject(String message) {
        this();

        this.setMessage(message);
    }

    public SuccessRespObject(String message, Object object) {
        this(message);

        this.setObj(object);
    }
}
