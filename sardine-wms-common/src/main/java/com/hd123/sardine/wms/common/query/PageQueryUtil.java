/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * <p/>
 * 文件名：	PageQueryUtil.java
 * 模块说明：
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.query;

/**
 * 分页查询工具类
 *
 * @author zhangsai
 */
public class PageQueryUtil {

    /**
     * 将查询分页信息赋值给分面查询结果。
     *
     * @param pageResult
     *            分页查询结果集，not null。
     * @param def
     *            分页查询定义，not null。
     */
    public static void assignPageInfo(PageQueryResult<?> pageResult, PageQueryDefinition def) {
        if (pageResult == null) {
            throw new IllegalArgumentException("非法的参数，不允许为null。");
        }
        if (def == null) {
            throw new IllegalArgumentException("非法的参数，不允许为null。");
        }
        pageResult.setPage(def.getPage());
        pageResult.setPageSize(def.getPageSize());
        pageResult.setPageCount(def.getPageCount());
        pageResult.setRecordCount(def.getRecordCount());
    }
}
