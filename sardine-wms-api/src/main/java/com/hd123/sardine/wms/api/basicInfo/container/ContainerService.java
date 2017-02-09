/**
 * 版权所有(C)，LHWMS项目组，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ContainerService.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月7日 - Jing - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.container;

import com.hd123.sardine.wms.common.entity.OperateContext;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * @author Jing
 *
 */
public interface ContainerService {
    /***
     * 新增容器
     * 
     * @param containerTypeUuid
     *            not null
     * @param operateContext
     */
    void saveNew(String containerTypeUuid, OperateContext operateContext);

    /***
     * 根据容器条码和组织查询容器
     * 
     * @param barcode
     *            容器条码
     * @param orgId
     *            组织
     * @return 容器
     */
    Container getByBarcode(String barcode, String orgId);

    /***
     * 分页查询容器列表
     * 
     * @param definition
     * @return 容器列表
     */
    PageQueryResult<Container> query(PageQueryDefinition definition);
}
