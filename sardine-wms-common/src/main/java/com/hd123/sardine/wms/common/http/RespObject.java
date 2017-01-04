/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * <p/>
 * 文件名：	RespObject.java
 * 模块说明：
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.http;

import java.io.Serializable;

/**
 * 返回前台界面的对象
 *
 * @author zhangsai
 */
public class RespObject implements Serializable {
    private static final long serialVersionUID = -4137747576992624575L;

    /** 执行是否成功 */
    private String status = RespStatus.HTTP_STATUS_SUCCESS;
    /** 返回信息 */
    private String message;
    /** 返回对象 */
    private Object obj = null;
    
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
