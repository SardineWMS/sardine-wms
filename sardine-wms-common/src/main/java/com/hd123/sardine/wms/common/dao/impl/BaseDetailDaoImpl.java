/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	wechange-commons
 * 文件名：	BaseDetailDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2016年10月28日 - zhanglin - 创建。
 */
package com.hd123.sardine.wms.common.dao.impl;

import java.util.List;

import com.hd123.sardine.wms.common.dao.BaseDetailDao;
import com.hd123.sardine.wms.common.dao.NameSpaceSupport;

/**
 * @author zhanglin
 *
 */
public class BaseDetailDaoImpl<T> extends NameSpaceSupport implements BaseDetailDao<T> {

    @Override
    public List<T> queryByList(String headerUuid) {
        return getSqlSession().selectList(generateStatement(MAPPER_QUERY_BYLIST), headerUuid);
    }

    @Override
    public void deleteByHeader(String headerUuid) {
        getSqlSession().delete(generateStatement(MAPPER_DELETE_BYHEADER), headerUuid);
    }

    @Override
    public int insert(T model) {
        return getSqlSession().insert(generateStatement(MAPPER_INSERT), model);
    }

    @Override
    public void insertBatch(List<T> models) {
        for (T t : models) {
            getSqlSession().insert(generateStatement(MAPPER_INSERT), t);
        }
    }
}
