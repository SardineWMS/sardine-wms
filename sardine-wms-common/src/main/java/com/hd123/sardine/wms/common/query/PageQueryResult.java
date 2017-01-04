/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * <p/>
 * 文件名：	PageQueryResult.java
 * 模块说明：
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页查询结果对象
 *
 * @author zhangsai
 */
public class PageQueryResult<T> implements Serializable {
    private static final long serialVersionUID = 7685838390515141405L;

    /** 每页最大显示记录数 */
    private int pageSize = 0;
    /** 当前页号 ，从0开始记数 */
    private int page = 0;
    /** 查询结果集总页数 */
    private int pageCount = 0;
    /** 查询结查集总记数数 */
    private int recordCount = 0;
    /** 结查集 */
    private List<T> records = new ArrayList<T>();

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize < 0 ? 0 : pageSize;
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
        this.pageCount = pageCount < 0 ? 0 : pageCount;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount < 0 ? 0 : recordCount;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    /**
     * 根据分页结果集计算总页数 。
     */
    public void calculatePageCount() {
        int pageCount = recordCount / pageSize;
        this.pageCount = recordCount % pageSize == 0 ? pageCount : (pageCount + 1);
    }
}
