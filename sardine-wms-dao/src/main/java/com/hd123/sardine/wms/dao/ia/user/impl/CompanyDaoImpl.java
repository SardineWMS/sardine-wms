/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	CustomerDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月14日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.dao.ia.user.impl;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.ia.user.Company;
import com.hd123.sardine.wms.common.dao.impl.BaseDaoImpl;
import com.hd123.sardine.wms.dao.ia.user.CompanyDao;

/**
 * @author yangwenzhu
 *
 */
public class CompanyDaoImpl extends BaseDaoImpl<Company> implements CompanyDao {
    private static final String GETBYNAME = "getByName";

    @Override
    public Company getByName(String name) {
        if (StringUtil.isNullOrBlank(name))
            return null;
        return getSqlSession().selectOne(generateStatement(GETBYNAME), name);
    }

}
