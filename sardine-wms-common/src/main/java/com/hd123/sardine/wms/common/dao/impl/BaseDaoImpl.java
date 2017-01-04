/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	sardine-wms-common
 * 文件名：	BaseDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月12日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.common.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hd123.sardine.wms.common.dao.BaseDao;
import com.hd123.sardine.wms.common.dao.NameSpaceSupport;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;

/**
 * @author zhangsai
 *
 */
public class BaseDaoImpl<T> extends NameSpaceSupport implements BaseDao<T> {

    @Override
    public T get(String uuid) {
        return getSqlSession().selectOne(generateStatement(MAPPER_GET), uuid);
    }

    @Override
    public List<T> query(PageQueryDefinition param) {
        return getSqlSession().selectList(generateStatement(MAPPER_QUERY_BYPAGE), param);
    }

    @Override
    public int insert(T model) {
        return getSqlSession().insert(generateStatement(MAPPER_INSERT), model);
    }

    @Override
    public int update(T model) {
        int count = getSqlSession().update(generateStatement(MAPPER_UPDATE), model);
        PersistenceUtils.optimisticVerify(count);
        return count;
    }

    @Override
    public int remove(String uuid, long version) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uuid", uuid);
        map.put("version", version);
        int i = getSqlSession().delete(generateStatement(MAPPER_REMOVE), map);
        PersistenceUtils.optimisticVerify(i);
        return i;
    }
}
