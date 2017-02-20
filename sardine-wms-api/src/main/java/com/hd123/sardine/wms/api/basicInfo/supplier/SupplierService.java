/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	SupplierService.java
 * 模块说明：	
 * 修改历史：
 * 2017年1月9日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.supplier;

import com.hd123.sardine.wms.common.entity.OperateContext;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * @author fanqingqing
 *
 */
public interface SupplierService {
    /** 代码 类似于 */
    public static final String QUERY_CODE_FIELD = "code";
    /** 名称 类似于 */
    public static final String QUERY_NAME_FIELD = "name";
    /** 状态 等于 */
    public static final String QUERY_STATE_FIELD = "state";

    /** 排序字段 代码 */
    public static final String ORDER_CODE_FIELD = "code";
    /** 排序字段 名称 */
    public static final String ORDER_NAME_FIELD = "name";

    /**
     * 根据uuid查询供应商。
     * 
     * @param uuid
     *            唯一标示，not null。
     * @return 供应商
     */
    Supplier get(String uuid);

    /**
     * 根据代码查询供应商。
     * 
     * @param code
     *            供应商代码，not null。
     * @return 供应商
     */
    Supplier getByCode(String code);

    /**
     * 分页查询供应商集合
     * 
     * @param definition
     *            查询条件，not null。
     * @return 供应商集合信息
     * @throws IllegalArgumentException
     *             definition为空时抛出
     */
    PageQueryResult<Supplier> query(PageQueryDefinition definition) throws IllegalArgumentException;

    /**
     * 新增供应商
     * 
     * @param supplier
     *            供应商,not null。
     * @param operCtx
     *            操作信息，not null。
     * @return 供应商uuid
     * @throws IllegalArgumentException
     *             参数为空时抛出
     * @throws WMSException
     */
    String saveNew(Supplier supplier, OperateContext operCtx)
            throws IllegalArgumentException, WMSException;

    /**
     * 更新供应商信息
     * 
     * @param supplier
     *            供应商，not null。
     * @param operCtx
     *            操作信息,not null。
     * @throws IllegalArgumentException
     *             参数为空
     * @throws WMSException
     */
    void saveModify(Supplier supplier, OperateContext operCtx)
            throws IllegalArgumentException, WMSException;

    /**
     * 删除供应商，只标记状态未“已删除”
     * 
     * @param uuid
     *            供应商uuid，not null。
     * @param version
     *            版本号，not null。
     * @param operCtx
     *            操作信息，not null。
     * @throws IllegalArgumentException
     *             参数为空
     * @throws WMSException
     */
    void remove(String uuid, long version, OperateContext operCtx)
            throws IllegalArgumentException, WMSException;

    /**
     * 恢复供应商状态未“正常”
     * 
     * @param uuid
     *            供应商uuid，not null。
     * @param version
     *            版本号，not null。
     * @param operCtx
     *            操作信息，not null。
     * @throws IllegalArgumentException
     *             参数为空。
     * @throws WMSException
     */
    void recover(String uuid, long version, OperateContext operCtx)
            throws IllegalArgumentException, WMSException;

}
