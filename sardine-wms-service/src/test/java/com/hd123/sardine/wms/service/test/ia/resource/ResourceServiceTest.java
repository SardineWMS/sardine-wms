/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	ResourceServiceTest.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月29日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.service.test.ia.resource;

import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hd123.sardine.wms.dao.ia.resource.ResourceDao;
import com.hd123.sardine.wms.service.ia.resource.ResourceServiceImpl;
import com.hd123.sardine.wms.service.test.BaseServiceTest;

/**
 * @author fanqingqing
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ResourceServiceTest extends BaseServiceTest {
    private static final String ROLEUUID = "r111";
    private static final String USERUUID = "u111";
    private static final String RESOURCEUUIDS = "s111";

    @InjectMocks
    public ResourceServiceImpl service;
    @Mock
    private ResourceDao dao;

    @Test
    public void saveRoleResource() throws Exception {
        service.saveRoleResource(ROLEUUID, Arrays.asList(RESOURCEUUIDS));

        verify(dao).saveRoleResource(ROLEUUID, RESOURCEUUIDS);
    }

    @Test
    public void saveUserResource() throws Exception {
        service.saveUserResource(USERUUID, Arrays.asList(RESOURCEUUIDS));

        verify(dao).saveUserResource(USERUUID, RESOURCEUUIDS);
    }

    @Test
    public void removeResourceByUser() throws Exception {
        service.removeResourceByUser(USERUUID);
        verify(dao).removeResourceByUser(USERUUID);
    }

    @Test
    public void removeResourceByRole() throws Exception {
        service.removeResourceByRole(USERUUID);

        verify(dao).removeResourceByRole(USERUUID);
    }
}
