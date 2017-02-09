/**
 * 版权所有(C)，LHWMS项目组，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	ContainerDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月7日 - Jing - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.container.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.basicInfo.container.Container;
import com.hd123.sardine.wms.common.dao.NameSpaceSupport;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.dao.basicInfo.container.ContainerDao;

/**
 * @author Jing
 *
 */
public class ContainerDaoImpl extends NameSpaceSupport implements ContainerDao {
    public static final String GETBYBARCODE = "getByBarcode";
    public static final String INSERTCONTAINER = "insertContainer";
    public static final String QUERYCONTAINERS = "queryContainer";

    @Override
    public Container getByBarcode(String barcode, String companyUuid) {
        Assert.assertArgumentNotNull(barcode, "barcode");
        Assert.assertArgumentNotNull(companyUuid, "companyUuid");

        Map<String, String> map = new HashMap<>();
        map.put("barcode", barcode);
        map.put("companyUuid", companyUuid);

        return getSqlSession().selectOne(generateStatement(GETBYBARCODE), map);
    }

    @Override
    public void insert(Container container) {
        getSqlSession().insert(generateStatement(INSERTCONTAINER), container);
    }

    @Override
    public List<Container> query(PageQueryDefinition param) {
        return getSqlSession().selectList(generateStatement(QUERYCONTAINERS), param);
    }

}
