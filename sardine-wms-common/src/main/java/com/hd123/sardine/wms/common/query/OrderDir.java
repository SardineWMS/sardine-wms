/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * <p/>
 * 文件名：	OrderDir.java
 * 模块说明：
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.query;

/**
 * 查询语句排序定义
 *
 * @author zhangsai
 */
public enum OrderDir {

    /** 升序 */
    asc,
    /** 降序 */
    desc;

    /**
     * 判断是否为升序
     *
     * @return
     */
    public boolean isAsc() {
        return this == asc;
    }

    /**
     * 返回是否降序
     *
     * @return
     */
    public boolean isDesc() {
        return this == desc;
    }

    /**
     * 相对valueOf()方法，提供大小写无关的能力。
     *
     */
    public static OrderDir valueOf2(String str) {
        return valueOf(str.toLowerCase());
    }
}
