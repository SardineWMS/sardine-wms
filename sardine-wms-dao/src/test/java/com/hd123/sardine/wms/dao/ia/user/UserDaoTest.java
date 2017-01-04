/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名： sardine-wms-dao
 * 文件名： UserDaoTest.java
 * 模块说明：
 * 修改历史：
 * 2016-12-12 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.dao.ia.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.hd123.sardine.wms.api.ia.user.User;
import com.hd123.sardine.wms.common.entity.OperateInfo;
import com.hd123.sardine.wms.common.entity.Operator;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.utils.DateHelper;
import com.hd123.sardine.wms.dao.AbstractDataAccessTests;

/**
 * @author zhangsai
 * 
 */
@DatabaseSetup({
        "InitData.xml" })
public class UserDaoTest extends AbstractDataAccessTests {

    @Autowired
    private UserDao userDao;

    @Test
    public void getByUuid() throws ParseException {
        User user = userDao.get("u1");

        assertNotNull(user);
        assertEquals("u1", user.getUuid());
        assertEquals("o1", user.getCompanyUuid());
        assertEquals("C1", user.getCompanyCode());
        assertEquals("N1", user.getCompanyName());
        assertEquals("n1", user.getName());
        assertEquals("c1", user.getCode());
        assertEquals("p1", user.getPasswd());
    }

    @Test
    public void query() {
        PageQueryDefinition definition = new PageQueryDefinition();
        definition.setPageSize(0);
        List<User> users = userDao.query(definition);

        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "Result_Insert.xml")
    public void insert() throws ParseException {
        User user = new User();
        user.setUuid("u3");
        user.setCode("c3");
        user.setName("n3");
        user.setCompanyUuid("o3");
        user.setCompanyCode("C3");
        user.setCompanyName("N3");
        user.setAdministrator(true);
        user.setPasswd("p3");
        user.setVersion(0);
        user.setPhone("phone");
        user.setCreateInfo(new OperateInfo(DateHelper.strToDateTime("2016-01-01 12:12:12"),
                new Operator("yy", "yy", "yz")));
        user.setLastModifyInfo(new OperateInfo(DateHelper.strToDateTime("2016-01-01 12:12:12"),
                new Operator("yy", "yy", "yz")));

        int count = userDao.insert(user);
        assertEquals(1, count);
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "Result_Delete.xml")
    public void delete() {
        long version = 0;
        int count = userDao.remove("u1", version);
        assertEquals(1, count);
    }
}
