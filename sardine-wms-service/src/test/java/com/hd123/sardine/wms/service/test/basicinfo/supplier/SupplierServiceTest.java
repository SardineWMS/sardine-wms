/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	SupplierServiceTest.java
 * 模块说明：	
 * 修改历史：
 * 2017年1月12日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.service.test.basicinfo.supplier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.dao.basicInfo.supplier.SupplierDao;
import com.hd123.sardine.wms.service.basicInfo.supplier.SupplierServiceImpl;
import com.hd123.sardine.wms.service.test.BaseServiceTest;

/**
 * @author fanqingqing
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class SupplierServiceTest extends BaseServiceTest {

    @InjectMocks
    public SupplierServiceImpl service;
    @Mock
    private SupplierDao dao;

    @Test
    public void query() {
        PageQueryDefinition definition = new PageQueryDefinition();
        definition.setPageSize(0);
        service.query(definition);
    }
}
