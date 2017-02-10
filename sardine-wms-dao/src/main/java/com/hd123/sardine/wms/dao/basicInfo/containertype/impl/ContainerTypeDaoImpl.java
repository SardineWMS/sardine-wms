/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	ContainerTypeDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月7日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.containertype.impl;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.containertype.ContainerType;
import com.hd123.sardine.wms.common.dao.impl.BaseDaoImpl;
import com.hd123.sardine.wms.dao.basicInfo.containertype.ContainerTypeDao;

/**
 * @author fanqingqing
 *
 */
public class ContainerTypeDaoImpl extends BaseDaoImpl<ContainerType> implements ContainerTypeDao {
    public static final String MAPPER_GETBYCODE = "getByCode";

    @Override
    public ContainerType getByCode(String code) {
        if (StringUtil.isNullOrBlank(code))
            return null;
        return getSqlSession().selectOne(generateStatement(MAPPER_GETBYCODE));
    }

}