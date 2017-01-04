/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * <p/>
 * 文件名：	RespStatus.java
 * 模块说明：
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.http;

/**
 * HTTP请求返回状态常量
 *
 * @author zhangsai
 */
public class RespStatus {

    /** 成功(默认成功状态码) */
    public static final String HTTP_STATUS_SUCCESS = "200";

    /** 业务处理错误 */
    public static final String HTTP_STATUS_ERROR = "500";
    
    /** 数据持久化处理错误 */
    public static final String HTTP_STATUS_ERROR_DATAACCESS = "900";
}
