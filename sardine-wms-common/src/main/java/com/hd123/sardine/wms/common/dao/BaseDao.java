/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * <p/>
 * 文件名：	BaseDao.java
 * 模块说明：
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.dao;

import java.util.List;

import com.hd123.sardine.wms.common.query.PageQueryDefinition;

/**
 * DAO层对象基础操作接口类
 *
 * @author zhangsai
 */
public interface BaseDao<T> {

    /**
     * 获取对象
     *
     * @param uuid
     *            对象uuid
     * @return 对象
     */
    T get(String uuid);

    /**
     * 根据条件分页查询对象列表
     *
     * @param param
     *            查询条件
     * @return 分页的对象列表
     */
    List<T> query(PageQueryDefinition param);

    /**
     * 新增对象
     *
     * @param model
     *            模型对象
     * @return 影响行数
     */
    int insert(T model);

    /**
     * 更新对象
     *
     * @param model
     *            模型对象
     * @return 影响行数
     */
    int update(T model);

    /**
     * 标记删除对象 <br>
     * 与 {@link #resume(String) resume} 相对应
     *
     * @param uuid
     *            模型对象UUID
     * @param version
     * @return 影响行数
     */
    int remove(String uuid, long version);
}
