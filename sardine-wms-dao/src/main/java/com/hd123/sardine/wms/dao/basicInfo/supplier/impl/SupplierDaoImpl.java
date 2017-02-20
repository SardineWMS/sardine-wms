/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	SupplierDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年1月9日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.supplier.impl;

import java.util.HashMap;
import java.util.Map;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.supplier.Supplier;
import com.hd123.sardine.wms.common.dao.impl.BaseDaoImpl;
import com.hd123.sardine.wms.dao.basicInfo.supplier.SupplierDao;

/**
 * @author fanqingqing
 *
 */
public class SupplierDaoImpl extends BaseDaoImpl<Supplier> implements SupplierDao {
    public static final String MAPPER_GETBYCODE = "getByCode";

    @Override
    public Supplier getByCode(String code, String companyUuid) {
        if (StringUtil.isNullOrBlank(code) || StringUtil.isNullOrBlank(companyUuid))
            return null;
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        map.put("companyUuid", companyUuid);
        return getSqlSession().selectOne(generateStatement(MAPPER_GETBYCODE), map);
    }
}
