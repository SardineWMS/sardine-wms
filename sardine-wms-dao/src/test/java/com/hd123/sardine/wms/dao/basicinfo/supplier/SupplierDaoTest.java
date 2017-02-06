/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	SupplierDaoTest.java
 * 模块说明：	
 * 修改历史：
 * 2017年1月12日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.supplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.hd123.sardine.wms.api.basicInfo.supplier.Supplier;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.dao.AbstractDataAccessTests;

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
        assertEquals(1, suppliers.size());
    }

}
