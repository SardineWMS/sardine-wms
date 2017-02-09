/**
 * 版权所有(C)，LHWMS项目组，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	ContainerDao.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月7日 - Jing - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.container;

import java.util.List;

import com.hd123.sardine.wms.api.basicInfo.container.Container;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;

/**
 * @author Jing
 *
 */
public interface ContainerDao {
    Container getByBarcode(String barcode, String companyUuid);

    void insert(Container container);

    List<Container> query(PageQueryDefinition param);
}
