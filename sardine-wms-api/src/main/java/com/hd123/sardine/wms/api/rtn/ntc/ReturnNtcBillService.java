/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ReturnNtcBillService.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月28日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.rtn.ntc;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Map;

import com.hd123.sardine.wms.common.exception.EntityNotFoundException;
import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * @author yangwenzhu
 *
 */
public interface ReturnNtcBillService {

    /** 查询字段 */
    public static final String QUERY_BILLNUMBER_LIKE = "billNumber";
    public static final String QUERY_STATE_EQUALS = "state";
    public static final String QUERY_CUSTOMERCODE_EQUALS = "customerCode";
    public static final String QUERY_CUSTOMERNAME_EQUALS = "customerName";
    public static final String QUERY_SOURCEBILLNUMBER_LIKE = "sourceBillNumber";
    public static final String QUERY_WRH_EQUALS = "wrhCode";
    public static final String QUERY_RETURNDATE_LESSTHAN = "returnDateLessThan";
    public static final String QUERY_RETURNDATE_MORETHAN = "returnDateMoreThan";
    public static final String QUERY_ARTICLENAME_EQUALS = "articleName";
    public static final String QUERY_ARTICLECODE_EQUALS = "articleCode";
    public static final String QUERY_SUPPLIERCODE_EQUALS = "supplierCode";
    public static final String QUERY_SUPPLIERNAME_EQUALS = "supplierName";

    /**
     * 新建退仓通知单
     * 
     * @param bill
     *            退仓通知单实体，not null
     * @return 退仓通知单UUID
     * @throws IllegalArgumentException
     *             bill为空抛出
     * @throws WMSException
     * 
     */
    String saveNew(ReturnNtcBill bill) throws WMSException;

    /**
     * 修改退仓通知单
     * 
     * @param bill
     *            退仓通知单实体，not null
     * @throws IllegalArgumentException
     *             bill为空，
     * @throws VersionConflictException
     *             当前单据已被其他用户修改
     * @throws EntityNotFoundException
     * @throws WMSException
     */
    void saveModify(ReturnNtcBill bill) throws WMSException;

    /**
     * 删除退仓通知单
     * <p>
     * 只有初始状态才可删除
     * 
     * @param uuid
     *            uuid, not null
     * @param version
     *            版本号，not null
     * @throws IllegalArgumentException
     *             UUID，version为空
     * @throws VersionConflictException
     *             当前单据已被其他用户修改
     * @throws EntityNotFoundException
     * @throws WMSException
     */
    void remove(String uuid, long version) throws WMSException;

    /**
     * 作废退仓通知单
     * <p>
     * 只有初始状态才可作废
     * 
     * @param uuid
     *            uuid,not null
     * @param version
     *            version,not null
     * @throws IllegalArgumentException
     * @throws VersionConflictException
     * @throws EntityNotFoundException
     * @throws WMSException
     */
    void abort(String uuid, long version) throws WMSException;

    /**
     * 根据UUID获取退仓通知单
     * 
     * @param uuid
     *            uuid,为空则返回null
     * @return 退仓通知单实体
     */
    ReturnNtcBill get(String uuid);

    /**
     * 根据单号获取退仓通知单
     * 
     * @param billNumber
     *            billNumber，为空则返回null
     * @return 退仓通知单实体
     */
    ReturnNtcBill getByBillNumber(String billNumber);

    /**
     * 分页查询退仓通知单
     * 
     * @param definition
     *            definition,not null
     * @throws IllegalArgumentException
     * @return 分页结果集
     */
    PageQueryResult<ReturnNtcBill> query(PageQueryDefinition definition);

    /**
     * 完成退仓通知单，初始和进行中的才可完成
     * <p>
     * <li>初始状态的退仓通知单可以通过手工单据直接完成
     * <li>完成进行中的退仓单
     * 
     * 
     * @param uuid
     *            uuid,not null
     * @param version
     *            版本号，not null
     * @throws IllegalArgumentException
     * @throws VersionConflictException
     * @throws EntityNotFoundException
     * @throws WMSException
     */
    void finish(String uuid, long version) throws WMSException;

    /**
     * 退货
     * 
     * @param billNumber
     *            单号，not null
     * @param returnInfo
     *            KEY：明细行UUID， VALUE：商品数量
     * @throws WMSException
     */
    void returnWrh(String billNumber, Map<String, BigDecimal> returnInfo) throws WMSException;

    /**
     * 生成退仓单
     * 
     * @param uuid
     *            通知单UUID，not null
     * @param version
     *            版本号，not null
     * @throws IllegalArgumentException
     * @throws VersionConflictException
     * @throws WMSException
     * @throws ParseException
     */
    void genRtnBill(String uuid, long version) throws WMSException, ParseException;

    /**
     * 根据UUID查询退仓通知单明细
     * 
     * @param uuid
     *            通知单明细UUID，如为空，则返回null
     * @return 通知单明细
     */
    ReturnNtcBillItem getItem(String uuid);
}
