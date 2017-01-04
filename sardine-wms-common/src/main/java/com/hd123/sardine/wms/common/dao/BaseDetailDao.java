/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	BaseDetailDao.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月12日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.dao;

import java.util.List;

/**
 * 明细对象持久化基类
 * 
 * @author zhangsai
 *
 */
public interface BaseDetailDao<T> {

    /**
     * 根据单头查询对象列表
     *
     * @param params
     *            查询条件
     * @return 对象列表
     */
    List<T> queryByList(String headerUuid);

    /**
     * 新增对象
     *
     * @param model
     *            模型对象
     * @return 影响行数
     */
    int insert(T model);

    /**
     * 批量新增
     *
     * @param models
     *            模型对象列表
     */
    void insertBatch(List<T> models);

    /**
     * 根据单头批量删除明细
     * 
     * @param param
     *            单头标识
     */
    void deleteByHeader(String headerUuid);
}
