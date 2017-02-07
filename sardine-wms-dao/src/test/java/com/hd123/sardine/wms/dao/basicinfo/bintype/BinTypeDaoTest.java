/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	BinTypeDaoTest.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月6日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.dao.basicinfo.bintype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.hd123.sardine.wms.api.basicInfo.bintype.BinType;
import com.hd123.sardine.wms.common.entity.OperateInfo;
import com.hd123.sardine.wms.common.entity.Operator;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.utils.DateHelper;
import com.hd123.sardine.wms.dao.AbstractDataAccessTests;
import com.hd123.sardine.wms.dao.basicInfo.bintype.BinTypeDao;

/**
 * @author yangwenzhu
 *
 */
@DatabaseSetup({
        "InitData.xml" })
public class BinTypeDaoTest extends AbstractDataAccessTests {
    @Autowired
    private BinTypeDao binTypeDao;

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "Result_Insert.xml")
    public void insert() throws ParseException {
        BinType binType = new BinType();
        binType.setUuid("uuid03");
        binType.setCode("code03");
        binType.setName("name03");
        binType.setLength(new BigDecimal(60));
        binType.setWidth(new BigDecimal(60));
        binType.setHeight(new BigDecimal(60));
        binType.setPlotRatio(new BigDecimal(80));
        binType.setBearing(new BigDecimal(600));
        binType.setVersion(0);
        binType.setCreateInfo(new OperateInfo(DateHelper.strToDateTime("2017-02-06 16:17:10"),
                new Operator("yy", "y001", "yz")));
        binType.setLastModifyInfo(new OperateInfo(DateHelper.strToDateTime("2017-02-06 16:17:10"),
                new Operator("yy", "y001", "yz")));
        int i = binTypeDao.insert(binType);
        assertEquals(1, i);
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "Result_Delete.xml")
    public void delete() throws ParseException {
        int i = binTypeDao.remove("uuid02", 0);
        assertEquals(1, i);
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "Result_Update.xml")
    public void update() throws ParseException {
        BinType binType = binTypeDao.get("uuid02");
        binType.setLastModifyInfo(new OperateInfo(DateHelper.strToDateTime("2017-02-06 17:28:18"),
                new Operator("yy", "y001", "yz")));
        int i = binTypeDao.update(binType);
        assertEquals(1, i);
    }

    @Test
    public void get() throws ParseException {
        BinType binType = binTypeDao.get("uuid01");
        assertNotNull(binType);
        assertEquals("uuid01", binType.getUuid());
        assertEquals("code01", binType.getCode());
        assertEquals("name01", binType.getName());
        assertEquals(new BigDecimal(100), binType.getLength());
        assertEquals(new BigDecimal(100), binType.getWidth());
        assertEquals(new BigDecimal(100), binType.getHeight());
        assertEquals(new BigDecimal(50), binType.getPlotRatio());
        assertEquals(new BigDecimal(1000), binType.getBearing());
        assertEquals(0, binType.getVersion());
        assertEquals("yy", binType.getCreateInfo().getOperator().getId());
        assertEquals("y001", binType.getCreateInfo().getOperator().getCode());
        assertEquals("yz", binType.getCreateInfo().getOperator().getFullName());
    }

    @Test
    public void getByCode() throws ParseException {
        BinType binType = binTypeDao.getByCode("code01");
        assertNotNull(binType);
        assertEquals("uuid01", binType.getUuid());
        assertEquals("code01", binType.getCode());
        assertEquals("name01", binType.getName());
        assertEquals(new BigDecimal(100), binType.getLength());
        assertEquals(new BigDecimal(100), binType.getWidth());
        assertEquals(new BigDecimal(100), binType.getHeight());
        assertEquals(new BigDecimal(50), binType.getPlotRatio());
        assertEquals(new BigDecimal(1000), binType.getBearing());
        assertEquals(0, binType.getVersion());
        assertEquals("yy", binType.getCreateInfo().getOperator().getId());
        assertEquals("y001", binType.getCreateInfo().getOperator().getCode());
        assertEquals("yz", binType.getCreateInfo().getOperator().getFullName());
    }

    @Test
    public void query() throws ParseException {
        PageQueryDefinition definition = new PageQueryDefinition();
        definition.setPageSize(0);
        List<BinType> list = binTypeDao.query(definition);
        assertNotNull(list);
        assertEquals(2, list.size());
    }
}
