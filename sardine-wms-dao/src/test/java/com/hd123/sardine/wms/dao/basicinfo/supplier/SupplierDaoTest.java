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

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
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
    public void insert() throws Exception{
        Supplier supplier = dao.get("u1");
        supplier.setUuid("u3");
        supplier.setCode("c3");
        supplier.setName("n3");
        supplier.setCompanyUuid("o3");
        supplier.setAddress("D3");
        
        supplier=null;
        System.out.println(supplier.getCreateInfo().getTime());

        int i = dao.insert(supplier);
        assertEquals(1, i);
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "Result_Update.xml")
    public void update() {
        Supplier supplier = dao.get("u2");
        supplier.setState(SupplierState.deleted);

        int i = dao.update(supplier);
        assertEquals(1, i);
    }
    
    static Logger log4j = Logger.getLogger(SupplierDaoTest.class.getClass());  
    
    @Test
    public void test(){
        BasicConfigurator.configure();  
      //  D:\WORKSPACE\SardineWMS\sardine-wms\sardine-wms-dao\src\main\resources
        PropertyConfigurator.configure("/src/main/resources/log4j.properties");  
          
        DOMConfigurator.configure("");  
        
        try{
            insert();
        }catch(Exception ex){
            log4j.error(ex);
        }
       

          
        log4j.debug("log4j debug");  
        log4j.info("log4j info");  
        log4j.warn("log4j warn");  
        log4j.error("log4j error");  
        log4j.fatal("log4j fatal");  
    }
}
