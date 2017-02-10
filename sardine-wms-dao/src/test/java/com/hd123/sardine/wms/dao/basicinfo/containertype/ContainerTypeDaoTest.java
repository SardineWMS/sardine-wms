/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	ContainerTypeDaoTest.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月7日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.dao.basicinfo.containertype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.hd123.sardine.wms.api.basicInfo.containertype.BarCodeType;
import com.hd123.sardine.wms.api.basicInfo.containertype.ContainerType;
import com.hd123.sardine.wms.common.entity.OperateInfo;
import com.hd123.sardine.wms.common.entity.Operator;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.utils.DateHelper;
import com.hd123.sardine.wms.dao.AbstractDataAccessTests;
import com.hd123.sardine.wms.dao.basicInfo.containertype.ContainerTypeDao;

/**
 * @author fanqingqing
 *
 */
@DatabaseSetup({
        "InitData.xml" })
public class ContainerTypeDaoTest extends AbstractDataAccessTests {
    @Autowired
    private ContainerTypeDao dao;

    @Test
    public void query() {
        PageQueryDefinition definition = new PageQueryDefinition();
        definition.setPageSize(0);

        List<ContainerType> containertypes = dao.query(definition);
        assertNotNull(containertypes);
        assertEquals(2, containertypes.size());
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "Result_Insert.xml")
    public void insert() {
        ContainerType containerType = new ContainerType();
        containerType.setUuid("u3");
        containerType.setCode("c3");
        containerType.setName("n3");
        containerType.setCompanyUuid("o3");
        containerType.setBarCodePrefix("Q");
        containerType.setBarCodeLength(9);
        containerType.setShip(false);
        containerType.setBarCodeType(BarCodeType.FOREVER);
        containerType.setCreateInfo(new OperateInfo(DateHelper.strToDateTime("2016-01-01 12:12:12"),
                new Operator("qq", "qq", "qq")));
        containerType.setLastModifyInfo(new OperateInfo(
                DateHelper.strToDateTime("2016-01-01 12:12:12"), new Operator("qq", "qq", "qq")));

        int i = dao.insert(containerType);
        assertEquals(1, i);
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "Result_Update.xml")
    public void update() {
        ContainerType containerType = dao.get("u2");
        containerType.setShip(false);

        int i = dao.update(containerType);
        assertEquals(1, i);
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "Result_Delete.xml")
    public void remove() {
        int i = dao.remove("u2", 0);
        assertEquals(1, i);
    }

}
