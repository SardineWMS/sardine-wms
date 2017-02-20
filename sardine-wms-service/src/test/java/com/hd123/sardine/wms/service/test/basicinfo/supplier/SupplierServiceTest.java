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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.fest.assertions.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hd123.sardine.wms.api.basicInfo.supplier.Supplier;
import com.hd123.sardine.wms.api.basicInfo.supplier.SupplierState;
import com.hd123.sardine.wms.dao.basicInfo.supplier.SupplierDao;
import com.hd123.sardine.wms.service.basicInfo.supplier.SupplierServiceImpl;
import com.hd123.sardine.wms.service.test.BaseServiceTest;

/**
 * @author fanqingqing
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class SupplierServiceTest extends BaseServiceTest {
    private static final String UUID = "89757";
    private static final long VERSION = 0;

    @InjectMocks
    public SupplierServiceImpl service;
    @Mock
    private SupplierDao dao;

    @Test
    public void insert() throws Exception {
        Supplier supplier = SupplierBuilder.supplier().withUuid(null).build();

        service.saveNew(supplier, defaultOperCtx());

        verify(dao).insert(supplier);
        Assertions.assertThat(supplier.getUuid()).isNotEmpty();
    }

    @Test
    public void update() throws Exception {
        Supplier supplier = SupplierBuilder.supplier().withUuid(UUID).withVersion(0).build();
        when(dao.get(UUID)).thenReturn(supplier);

        service.saveModify(supplier, defaultOperCtx());

        verify(dao).update(supplier);
        Assertions.assertThat(supplier.getLastModifyInfo().getOperator())
                .isEqualTo(defaultOperCtx().getOperator());
    }

    @Test
    public void remove() throws Exception {
        Supplier supplier = SupplierBuilder.supplier().withUuid(UUID)
                .withState(SupplierState.normal).withVersion(VERSION).build();
        when(dao.get(UUID)).thenReturn(supplier);

        service.remove(UUID, VERSION, defaultOperCtx());

        verify(dao).update(supplier);
        Assertions.assertThat(SupplierState.deleted).isEqualTo(supplier.getState());
        Assertions.assertThat(supplier.getLastModifyInfo().getOperator())
                .isEqualTo(defaultOperCtx().getOperator());
    }

    @Test
    public void recover() throws Exception {
        Supplier supplier = SupplierBuilder.supplier().withUuid(UUID)
                .withState(SupplierState.deleted).withVersion(VERSION).build();
        when(dao.get(UUID)).thenReturn(supplier);

        service.recover(UUID, VERSION, defaultOperCtx());

        verify(dao).update(supplier);
        Assertions.assertThat(SupplierState.normal).isEqualTo(supplier.getState());
        Assertions.assertThat(supplier.getLastModifyInfo().getOperator())
                .isEqualTo(defaultOperCtx().getOperator());
    }

}
