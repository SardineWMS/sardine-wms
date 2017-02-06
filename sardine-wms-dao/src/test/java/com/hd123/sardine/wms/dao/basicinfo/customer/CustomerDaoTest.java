/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	CustomerDaoTest.java
 * 模块说明：	
 * 修改历史：
 * 2017年1月13日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.dao.basicinfo.customer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.hd123.sardine.wms.api.basicInfo.customer.Customer;
import com.hd123.sardine.wms.api.basicInfo.customer.CustomerState;
import com.hd123.sardine.wms.api.basicInfo.customer.CustomerType;
import com.hd123.sardine.wms.common.entity.OperateInfo;
import com.hd123.sardine.wms.common.entity.Operator;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.utils.DateHelper;
import com.hd123.sardine.wms.dao.AbstractDataAccessTests;
import com.hd123.sardine.wms.dao.basicInfo.customer.CustomerDao;

/**
 * @author yangwenzhu
 *
 */
@DatabaseSetup({
        "InitData.xml" })
public class CustomerDaoTest extends AbstractDataAccessTests {

    @Autowired
    private CustomerDao customerDao;

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "Result_Insert.xml")
    public void insert() throws ParseException {
        Customer customer = new Customer();
        customer.setUuid("UUID03");
        customer.setCode("CODE03");
        customer.setName("NAME03");
        customer.setType(CustomerType.shop);
        customer.setPhone("phone001");
        customer.setAddress("SHANGHAI");
        customer.setState(CustomerState.normal);
        customer.setCompanyUuid("C001");
        customer.setRemark("INTRODUCTION");
        customer.setVersion(0);
        customer.setCreateInfo(new OperateInfo(DateHelper.strToDateTime("2017-01-13 13:17:26"),
                new Operator("YY", "Y001", "YZ")));
        customer.setLastModifyInfo(new OperateInfo(DateHelper.strToDateTime("2017-01-13 13:17:26"),
                new Operator("YY", "Y001", "YZ")));
        int i = customerDao.insert(customer);
        assertEquals(1, i);
    }

    @Test
    public void get() throws ParseException {
        Customer customer = customerDao.get("UUID01");

        assertNotNull(customer);
        assertEquals("UUID01", customer.getUuid());
        assertEquals("CODE01", customer.getCode());
        assertEquals("NAME01", customer.getName());
        assertEquals("store", customer.getType().toString());
        assertEquals("12345678901", customer.getPhone());
        assertEquals("HANGZHOU", customer.getAddress());
        assertEquals("C001", customer.getCompanyUuid());
        assertEquals("INTRODUCTION", customer.getRemark());
        assertEquals(1, customer.getVersion());
    }

    @Test
    public void getByCode() throws ParseException {
        Customer customer = customerDao.getByCode("CODE01");

        assertNotNull(customer);
        assertEquals("UUID01", customer.getUuid());
        assertEquals("CODE01", customer.getCode());
        assertEquals("NAME01", customer.getName());
        assertEquals("store", customer.getType().toString());
        assertEquals("12345678901", customer.getPhone());
        assertEquals("HANGZHOU", customer.getAddress());
        assertEquals("C001", customer.getCompanyUuid());
        assertEquals("INTRODUCTION", customer.getRemark());
        assertEquals(1, customer.getVersion());
    }

    @Test
    public void query() throws ParseException {
        PageQueryDefinition definition = new PageQueryDefinition();
        definition.setPageSize(0);
        List<Customer> list = customerDao.query(definition);
        assertNotNull(list);
        assertEquals(2, list.size());
    }

    @Test
    // @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT,
    // value = "Result_UpdateState.xml")
    public void updateState() throws ParseException {
        Customer customer = customerDao.get("UUID01");
        customer.setState(CustomerState.deleted);
        customer.setLastModifyInfo(new OperateInfo(DateHelper.strToDateTime("2017-01-12 16:35:12"),
                new Operator("YY", "Y001", "YZ")));
        int i = customerDao.updateState(customer);
        assertEquals(1, i);
        Customer customer2 = customerDao.get("UUID01");
        assertEquals("deleted", customer2.getState().toString());
        assertEquals(2, customer2.getVersion());
    }

    @Test
    public void update() throws ParseException {
        Customer customer = customerDao.get("UUID01");
        customer.setType(CustomerType.shop);
        customer.setLastModifyInfo(new OperateInfo(DateHelper.strToDateTime("2017-01-12 16:35:12"),
                new Operator("YY", "Y001", "YZ")));
        int i = customerDao.update(customer);
        assertEquals(1, i);
        Customer customer2 = customerDao.get("UUID01");
        assertEquals("shop", customer2.getType().toString());
        assertEquals(2, customer2.getVersion());
    }
}
