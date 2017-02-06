/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	CustomerDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年1月13日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.customer.impl;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.customer.Customer;
import com.hd123.sardine.wms.common.dao.impl.BaseDaoImpl;
import com.hd123.sardine.wms.dao.basicInfo.customer.CustomerDao;

/**
 * @author yangwenzhu
 *
 */
public class CustomerDaoImpl extends BaseDaoImpl<Customer> implements CustomerDao {
    public static final String MAPPER_GETBYCODE = "getByCode";
    public static final String MAPPER_UPDATESTATE = "updateState";

    @Override
    public Customer getByCode(String code) {
        if (StringUtil.isNullOrBlank(code))
            return null;
        return getSqlSession().selectOne(generateStatement(MAPPER_GETBYCODE), code);
    }

    @Override
    public int updateState(Customer customer) {
        return getSqlSession().update(generateStatement(MAPPER_UPDATESTATE), customer);
    }

}
