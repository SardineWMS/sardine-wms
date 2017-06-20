/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	CustomerDao.java
 * 模块说明：	
 * 修改历史：
 * 2017年1月13日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.customer;

import java.util.List;

import com.hd123.sardine.wms.api.basicInfo.customer.Customer;
import com.hd123.sardine.wms.common.dao.BaseDao;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * @author yangwenzhu
 *
 */
public interface CustomerDao extends BaseDao<Customer> {
    Customer getByCode(String code);

    int updateState(Customer customer);

    List<UCN> queryAllCustomer();

}
