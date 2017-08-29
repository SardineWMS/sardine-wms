/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ShipBillService.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月15日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.tms.shipbill;

import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * @author fanqingqing
 *
 */
public interface ShipBillService {
    /** 查询条件 */
    public static final String QUERY_BILLNUMBER_LIKE = "billNumber";
    public static final String QUERY_STATE_EQUALS = "state";
    public static final String QUERY_VEHICLENUM_LIKE = "vehicleNum";
    public static final String QUERY_CUSTOMERCODE_CONTAINS = "customerCode";
    public static final String QUERY_CUSTOMERNAME_CONTAINS = "customerName";
    public static final String QUERY_DELIVERYTYPE_EQUALS = "deliveryType";
    public static final String QUERY_OPERATEMETHOD_EQUALS = "operateMethod";
    public static final String QUERY_DRIVERCODE_LIKE = "driverCode";
    public static final String QUERY_DRIVERNAME_LIKE = "driverName";
    public static final String QUERY_ARTICLECODE_CONTAINS = "articleCode";
    public static final String QUERY_SHIPERCODE_LIKE = "shiperCode";
    public static final String QUERY_SHIPERNAME_LIKE = "shiperName";
    public static final String QUERY_CONTAINERBARCODE_CONTAINS = "containerBarcode";

    /** 排序字段 */
    public static final String FIELD_ORDER_BILLNUMBER = "billNumber";
    public static final String FIELD_ORDER_STATE = "state";
    public static final String FIELD_ORDER_VEHICLENUM = "vehicleNum";

    /**
     * 根据UUID查询装车单
     * 
     * @param UUID
     *            装车单UUID，not null。
     * @return 装车单
     */
    ShipBill getByUuid(String uuid);

    /**
     * 根据单号查询装车单
     * 
     * @param billNumber
     *            单号，not null。
     * @return 装车单
     */
    ShipBill getByBillNumber(String billNumber);

    /**
     * 分页查询装车单集合
     * 
     * @param definition
     *            分页条件，not null。
     * @return 装车单集合
     * @throws IllegalArgumentException
     *             参数异常时抛出
     */
    PageQueryResult<ShipBill> query(PageQueryDefinition definition) throws IllegalArgumentException;

    /**
     * 完成
     * 
     * @param uuid
     *            装车单UUID，not null。
     * @param version
     *            版本号，not null。
     * @throws IllegalArgumentException
     *             参数异常时抛出
     * @throws VersionConflictException
     *             版本异常时抛出
     * @throws WMSException
     */
    void finish(String uuid, long version)
            throws IllegalArgumentException, VersionConflictException, WMSException;

    
}
