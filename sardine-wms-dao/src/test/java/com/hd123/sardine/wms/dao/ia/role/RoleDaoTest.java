/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	RoleDaoTest.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月27日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.dao.ia.role;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.hd123.sardine.wms.api.ia.role.Role;
import com.hd123.sardine.wms.api.ia.role.RoleState;
import com.hd123.sardine.wms.common.entity.OperateInfo;
import com.hd123.sardine.wms.common.entity.Operator;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.utils.DateHelper;
import com.hd123.sardine.wms.dao.AbstractDataAccessTests;

/**
 * @author fanqingqing
 *
 */
@DatabaseSetup({
        "InitData.xml" })
public class RoleDaoTest extends AbstractDataAccessTests {
    @Autowired
    private RoleDao dao;

    @Test
    public void getByUuid() throws ParseException {
        Role role = dao.get("u1");

        assertNotNull(role);
        assertEquals("u1", role.getUuid());
        assertEquals("c1", role.getCode());
        assertEquals("n1", role.getName());
        assertEquals("online", role.getState().name());
        assertEquals(0, role.getVersion());
        assertEquals("o1", role.getCompanyUuid());
    }

    @Test
    public void query() {
        PageQueryDefinition definition = new PageQueryDefinition();
        definition.setPageSize(0);
        List<Role> roles = dao.query(definition);

        assertNotNull(roles);
        assertEquals(2, roles.size());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "Result_Insert.xml")
    public void insert() throws ParseException {
        Role role = new Role();
        role.setUuid("u3");
        role.setCode("c3");
        role.setCompanyUuid("o3");
        role.setName("n3");
        role.setCreateInfo(new OperateInfo(DateHelper.strToDateTime("2016-01-01 12:12:12"),
                new Operator("yy", "yy", "yz")));
        role.setLastModifyInfo(new OperateInfo(DateHelper.strToDateTime("2016-01-01 12:12:12"),
                new Operator("yy", "yy", "yz")));

        int count = dao.insert(role);

        assertEquals(1, count);
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "Result_Update.xml")
    public void update() {
        Role role = dao.get("u2");
        role.setState(RoleState.offline);

        int i = dao.update(role);
        assertEquals(1, i);
    }
    
    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "Result_Delete.xml")
    public void delete() {
        int count = dao.remove("u1", 0);
        assertEquals(1, count);
    }

}
