/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	ResourceDaoTest.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月29日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.dao.ia.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.hd123.sardine.wms.api.ia.resource.Resource;
import com.hd123.sardine.wms.dao.AbstractDataAccessTests;

/**
 * @author fanqingqing
 *
 */
@DatabaseSetup({
        "InitData.xml" })
public class ResourceDaoTest extends AbstractDataAccessTests {
    @Autowired
    private ResourceDao dao;

    @Test
    public void queryAllTopMenuResource() {
        List<Resource> resources = dao.queryAllTopMenuResource();

        assertNotNull(resources);
        assertEquals(1, resources.size());
    }

    @Test
    public void queryOwnedTopMenuResourceByRole() {
        List<Resource> resources = dao.queryOwnedTopMenuResourceByRole("r1");

        assertNotNull(resources);
        assertEquals(1, resources.size());
    }

    @Test
    public void queryOwnedTopMenuResourceByUser() {
        List<Resource> resources = dao.queryOwnedTopMenuResourceByUser("s1");

        assertNotNull(resources);
        assertEquals(1, resources.size());
    }
    
    @Test
    public void queryAllChildResource() {
        List<Resource> resources = dao.queryAllChildResource("u1");

        assertNotNull(resources);
        assertEquals(1, resources.size());
    }
    
    @Test
    public void queryOwnedChildResourceByRole() {
        List<Resource> resources = dao.queryOwnedChildResourceByRole("r1","u1");

        assertNotNull(resources);
        assertEquals(1, resources.size());
    }
    
    @Test
    public void queryOwnedChildResourceByUser() {
        List<Resource> resources = dao.queryOwnedChildResourceByUser("s1","u1");

        assertNotNull(resources);
        assertEquals(1, resources.size());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT,
            value = "Result_Insert_RoleResource.xml")
    public void insert_roleResource() throws ParseException {
        dao.saveRoleResource("r2", "u1");
    }
    
    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT,
            value = "Result_Insert_UserResource.xml")
    public void insert_userResource() throws ParseException {
        dao.saveUserResource("s2", "u1");
    }
    
    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "Result_Delete_RoleResource.xml")
    public void removeResourceByRole() {
        dao.removeResourceByRole("r1");
    }
    
    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "Result_Delete_UserResource.xml")
    public void removeResourceByUser() {
        dao.removeResourceByUser("s1");
    }

}
