/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	SupplierDaoTest.java
 * 模块说明：	
 * 修改历史：
 * 2017年1月12日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.dao.basicinfo.supplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.hd123.sardine.wms.api.basicInfo.containertype.ContainerType;
import com.hd123.sardine.wms.api.basicInfo.supplier.Supplier;
import com.hd123.sardine.wms.api.basicInfo.supplier.SupplierState;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.dao.AbstractDataAccessTests;
import com.hd123.sardine.wms.dao.basicInfo.supplier.SupplierDao;

/**
 * @author fanqingqing
 *
 */
@DatabaseSetup({
        "InitData.xml" })
public class SupplierDaoTest extends AbstractDataAccessTests {

    @Autowired
    private SupplierDao dao;

    @Test
    public void query() {
        PageQueryDefinition definition = new PageQueryDefinition();
        definition.setPageSize(0);

        List<Supplier> suppliers = dao.query(definition);
        assertNotNull(suppliers);
        assertEquals(2, suppliers.size());
    }
    
    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "Result_Insert.xml")
    public void insert(){
        Supplier supplier=dao.get("u1");
        supplier.setUuid("u3");
        supplier.setCode("c3");
        supplier.setName("n3");
        supplier.setCompanyUuid("o3");
        supplier.setAddress("D3");
        
        int i = dao.insert(supplier);
        assertEquals(1, i);
    }
    
    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "Result_Update.xml")
    public void update() {
        Supplier supplier=dao.get("u2");
        supplier.setState(SupplierState.deleted);

        int i = dao.update(supplier);
        assertEquals(1, i);
    }

}
