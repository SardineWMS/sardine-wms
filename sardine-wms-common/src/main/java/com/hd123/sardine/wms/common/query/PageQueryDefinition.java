/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * <p/>
 * 文件名：	PageQueryDefinition.java
 * 模块说明：
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.query;

import java.util.Collection;
import java.util.HashMap;

/**
 * 分页查询的查询条件定义
 * <p>
 * 集成{@link HashMap} 存放键值对，作为查询条件
 *
 * @author zhangsai
 */
public class PageQueryDefinition extends HashMap<String, Object> {
    private static final long serialVersionUID = -4771265129949662836L;

    public static final int DEFAULT_PAGESIZE = 50;

    /** 每页记录数 */
    private int pageSize = DEFAULT_PAGESIZE;
    /** 当前页号,从0开始记数 */
    private int page = 0;
    /** 总页数 */
    private int pageCount;
    /** 总记录数 */
    private int recordCount;

    /** 排序字段 */
    private String sortField;
    /** 排序 */
    private OrderDir orderDir = OrderDir.asc;
    /** 组织 */
    private String companyUuid;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize < 0 ? DEFAULT_PAGESIZE : pageSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page < 0 ? 0 : page;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
        int pageCount = recordCount % pageSize == 0 ? recordCount / pageSize
                : recordCount / pageSize + 1;
        this.setPageCount(pageCount);
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
        put("sortField", sortField);
    }

    public OrderDir getOrderDir() {
        return orderDir;
    }

    public void setOrderDir(OrderDir orderDir) {
        this.orderDir = orderDir;
        put("orderDir", orderDir == null ? null : orderDir.name());
    }

    public String getCompanyUuid() {
        return companyUuid;
    }

    public void setCompanyUuid(String companyUuid) {
        this.companyUuid = companyUuid;
        put("companyUuid", companyUuid);
    }

    /**
     * 用于分页查询传入集合参数，解决mybatis分页插件不支持in查询问题
     * <p>
     * 不做分页查询时，请勿调用该方法<br>
     * mapper文件：u.name in ${names}，不要再用foreach
     * 
     * @param key
     * @param value
     */
    @SuppressWarnings("rawtypes")
    public void putCollection(String key, Collection value) {
        if (value == null)
            return;

        String valueStr = "(";
        for (Object v : value) {
            valueStr = valueStr + "'" + v.toString() + "',";
        }
        valueStr = valueStr.substring(0, valueStr.length() - 1) + ")";
        put(key, valueStr);
    }
}
