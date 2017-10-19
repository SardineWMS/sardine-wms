/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	CarrierService.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月6日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.tms.carrier;

import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * 承运商|接口
 * 
 * @author yangwenzhu
 *
 */
public interface CarrierService {
    /** 查询条件 */
    public static final String QUERY_CODE_LIKE = "code";
    public static final String QUERY_NAME_EQUALS = "name";
    public static final String QUERY_STATE_EQUALS = "state";
    /** 排序字段 */
    public static final String FIELD_ORDER_CODE = "code";

    /**
     * 新增承运商
     * 
     * @param carrier
     *            承运商实体，not null
     * @return 新增承运商UUID
     * @throws IllegalArgumentException
     *             参数异常
     * @throws WMSException
     *             其他业务异常
     */
    String saveNew(Carrier carrier) throws IllegalArgumentException, WMSException;

    /**
     * 修改承运商
     * 
     * @param carrier
     *            承运商实体，not null
     * @throws IllegalArgumentException
     *             参数异常
     * @throws WMSException
     *             其他业务异常
     */
    void saveModify(Carrier carrier) throws IllegalArgumentException, WMSException;

    /**
     * 启用承运商
     * 
     * @param uuid
     *            要启用承运商的UUID，not null
     * @param version
     *            版本号，not null
     * @throws IllegalArgumentException
     *             uuid,version为空时抛出
     * @throws VersionConflictException
     *             已被其他用户修改时抛出
     * @throws WMSException
     *             其他业务异常
     */
    void online(String uuid, long version)
            throws IllegalArgumentException, VersionConflictException, WMSException;

    /**
     * 停用承运商
     * 
     * @param uuid
     *            要停用承运商的UUID，not null
     * @param version
     *            版本号，not null
     * @throws IllegalArgumentException
     *             UUID，version为空时抛出
     * @throws VersionConflictException
     *             已被其他用户修改时抛出
     * @throws WMSException
     *             其他业务异常
     */
    void offline(String uuid, long version)
            throws IllegalArgumentException, VersionConflictException, WMSException;

    /**
     * 删除承运商
     * 
     * @param uuid
     *            要删除承运商的UUID，not null
     * @param version
     *            版本号，not null
     * @throws IllegalArgumentException
     *             UUID为空时抛出
     * @throws VersionConflictException
     *             被其他用户修改时抛出
     * @throws WMSException
     *             其他业务异常
     */
    void remove(String uuid, long version)
            throws VersionConflictException, IllegalArgumentException, WMSException;

    /**
     * 根据代码查询承运商
     * <p>
     * 代码为空，则返回空
     * 
     * @param code
     *            代码
     * @return 承运商实体
     * @throws WMSException
     */
    Carrier getByCode(String code) throws WMSException;

    /**
     * 分页查询承运商
     * 
     * @param definition
     *            查询条件
     * @return 分页结果集
     */
    PageQueryResult<Carrier> query(PageQueryDefinition definition);

    /**
     * 根据UUID获取承运商
     * 
     * @param uuid
     *            uuid,为空则返回空
     * @return 承运商
     * @throws IllegalArgumentException
     * @throws WMSException
     */
    Carrier get(String uuid) throws IllegalArgumentException, WMSException;

}
