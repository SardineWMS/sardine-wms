/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	ContainerTypeServiceTest.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月7日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.service.test.basicinfo.containertype;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.fest.assertions.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hd123.sardine.wms.api.basicInfo.containertype.ContainerType;
import com.hd123.sardine.wms.dao.basicInfo.containertype.ContainerTypeDao;
import com.hd123.sardine.wms.service.basicInfo.containertype.ContainerTypeServiceImpl;
import com.hd123.sardine.wms.service.test.BaseServiceTest;

/**
 * @author fanqingqing
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ContainerTypeServiceTest extends BaseServiceTest {
    private static final String UUID = "89757";
    private static final long VERSION = 0;
    @InjectMocks
    public ContainerTypeServiceImpl service;
    @Mock
    private ContainerTypeDao dao;

    @Test
    public void insert() throws Exception {
        ContainerType containerType = ContainerTypeBuilder.containerType().withUuid(null).build();

        service.saveNew(containerType, defaultOperCtx());

        verify(dao).insert(containerType);
        Assertions.assertThat(containerType.getUuid()).isNotEmpty();
    }

    @Test
    public void update() throws Exception {
        ContainerType containerType = ContainerTypeBuilder.containerType().withUuid(UUID)
                .withVersion(0).build();
        when(dao.get(UUID)).thenReturn(containerType);

        service.saveModify(containerType, defaultOperCtx());

        verify(dao).update(containerType);
        Assertions.assertThat(containerType.getLastModifyInfo().getOperator())
                .isEqualTo(defaultOperCtx().getOperator());
    }

    @Test
    public void remove() throws Exception {
        ContainerType containerType = ContainerTypeBuilder.containerType().withUuid(UUID)
                .withVersion(VERSION).build();
        when(dao.get(UUID)).thenReturn(containerType);

        service.remove(UUID, VERSION, defaultOperCtx());

        verify(dao).remove(UUID, VERSION);
    }

}
