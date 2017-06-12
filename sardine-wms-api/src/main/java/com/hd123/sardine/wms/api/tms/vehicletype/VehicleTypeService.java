/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	VehicleTypeService.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月6日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.tms.vehicletype;

import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * @author yangwenzhu
 *
 */
public interface VehicleTypeService {
    /**
     * 新建车型
     * 
     * @param type
     *            车型实体，not null
     * @return 新建车型的UUID
     * @throws IllegalArgumentException
     *             参数异常抛出
     * @throws WMSException
     */
    String saveNew(VehicleType type) throws IllegalArgumentException, WMSException;

    /**
     * 修改车型
     * 
     * @param type
     *            车型实体，not null
     * @throws IllegalArgumentException
     * @throws WMSException
     */
    void saveModify(VehicleType type) throws IllegalArgumentException, WMSException;

    /**
     * 删除车型
     * 
     * @param uuid
     *            uuid,not null
     * @param version
     *            版本号，not not null
     * @throws IllegalArgumentException
     *             UUID为空时抛出
     * @throws VersionConflictException
     *             版本冲突
     * @throws WMSException
     */
    void remove(String uuid, long version)
            throws IllegalArgumentException, VersionConflictException, WMSException;

    /**
     * 分页查询车型
     * 
     * @param definition
     *            查询条件，not null
     * @return 分页结果集
     * @throws IllegalArgumentException
     *             条件为空时抛出
     * @throws WMSException
     */
    PageQueryResult<VehicleType> query(PageQueryDefinition definition)
            throws IllegalArgumentException, WMSException;

    /**
     * 根据UUID获取车型
     * 
     * @param uuid
     *            uuid ,为空则返回空
     * @return 车型信息
     * @throws IllegalArgumentException
     * @throws WMSException
     */
    VehicleType get(String uuid) throws IllegalArgumentException, WMSException;

}
