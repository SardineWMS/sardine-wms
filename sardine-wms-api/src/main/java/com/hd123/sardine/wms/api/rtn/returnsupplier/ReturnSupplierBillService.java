/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	SupplierReturnBillService.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月21日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.rtn.returnsupplier;

import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * @author fanqingqing
 *
 */
public interface ReturnSupplierBillService {
    /** 查询条件 */
    public static final String QUERY_BILLNUMBER_LIKE = "billNumber";
    public static final String QUERY_STATE_EQUALS = "state";
    public static final String QUERY_RTNSUPPLIERNTCBILLNUMBER_LIKE = "rtnSupplierNtcBillNumber";
    public static final String QUERY_SUPPLIERCODE_LIKE = "supplierCode";
    public static final String QUERY_SUPPLIERNAME_LIKE = "supplierName";
    public static final String QUERY_WRHUUID_EQUALS = "wrhUuid";
    public static final String QUERY_OPERATEMETHOD_EQUALS = "operateMethod";
    public static final String QUERY_RETURNERCODE_LIKE = "returnerCode";
    public static final String QUERY_RETURNERNAME_LIKE = "returnerName";
    public static final String QUERY_ARTICLECODE_CONTAINS = "articleCode";
    public static final String QUERY_BINCODE_CONTAINS = "binCode";
    public static final String QUERY_CONTAINERBARCODE_CONTAINS = "containerBarcode";
    public static final String QUERY_RETURNSUPPLIERDATE_LESSTHANOREQUAL = "returnSupplierDateLessThanOrEqual";
    public static final String QUERY_RETURNSUPPLIERDATE_GREATERTHANOREQUAL = "returnSupplierDateGreaterThanOrEqual";
    
    /** 排序字段 */
    public static final String FIELD_ORDER_BILLNUMBER = "billNumber";
    public static final String FIELD_ORDER_STATE = "state";
    public static final String FIELD_ORDER_RTNSUPPLIERNTCBILLNUMBER = "rtnSupplierNtcBillNumber";
    public static final String FIELD_ORDER_SUPPLIER = "supplier";

    /**
     * 根据UUID查询退货单
     * 
     * @param UUID
     *            退货单UUID，not null。
     * @return 退货单
     */
    ReturnSupplierBill getByUuid(String uuid);

    /**
     * 根据单号查询退货单
     * 
     * @param billNumber
     *            单号，not null。
     * @return 退货单
     */
    ReturnSupplierBill getByBillNumber(String billNumber);

    /**
     * 分页查询退货单集合
     * 
     * @param definition
     *            分页条件，not null。
     * @return 退货单集合
     * @throws IllegalArgumentException
     *             参数异常时抛出
     */
    PageQueryResult<ReturnSupplierBill> query(PageQueryDefinition definition)
            throws IllegalArgumentException;

    /**
     * 完成
     * 
     * @param uuid
     *            退货单UUID，not null。
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
