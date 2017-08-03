/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ReturnBillService.java
 * 模块说明：	
 * 修改历史：
 * 2017年7月5日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.rtn.customerreturn;

import java.text.ParseException;

import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * 退仓单服务|接口
 * 
 * @author yangwenzhu
 *
 */
public interface ReturnBillService {
    /** 查询字段 单号类似于 */
    public static final String QUERY_BILLNUMBER_LIKE = "billNumberLike";
    /** 查询字段 状态等于 */
    public static final String QUERY_STATE_EQUALS = "stateEquals";
    /** 查询字段 客户代码类似于 */
    public static final String QUERY_CUSTOMER_CODE_LIKE = "customerCodeLike";
    /** 查询字段 退仓通知单类似于 */
    public static final String QUERY_RETURNNTCBILL_LIKE = "rtnNtcBillLike";
    /** 查询字段 客户名称类似于 */
    public static final String QUERY_CUSTOMER_NAME_LIKE = "customerNameLike";
    /** 查询字段 仓位等于 */
    public static final String QUERY_WRH_EQUALS = "wrhEquals";
    /** 查询字段 退仓员代码类似于 */
    public static final String QUERY_RETURNOR_CODE_LIKE = "returnorCodeLike";
    /** 查询字段 退仓员名称类似于 */
    public static final String QUERY_RETURNOR_NAME_LIKE = "returnorNameLike";
    /** 查询字段 商品代码包含 */
    public static final String QUERY_ARTICLE_CODE_CONTAINS = "articleCodeContains";
    /** 查询字段 商品名称包含 */
    public static final String QUERY_ARTICLE_NAME_CONTAINS = "articleNameContains";
    /** 查询字段 供应商代码包含 */
    public static final String QUERY_SUPPLIER_CODE_CONTAINS = "supplierCodeContains";
    /** 查询字段 供应商名称包含 */
    public static final String QUERY_SUPPLIER_NAMW_CONTAINS = "supplierNameContains";

    /**
     * 新增退仓单
     * 
     * @param bill
     *            退仓单,not null
     * @return 新增退仓单UUID
     * @throws IllegalArgumentException
     * @throws WMSException
     * @throws ParseException
     */
    String saveNew(ReturnBill bill) throws WMSException, ParseException;

    /**
     * 编辑退仓单
     * 
     * @param bill
     *            退仓单，not null
     * @throws IllegalArgumentException
     * @throws VersionConflictException
     * @throws WMSException
     * @throws ParseException
     */
    void saveModify(ReturnBill bill) throws WMSException, ParseException;

    /**
     * 删除退仓单
     * 
     * @param uuid
     *            uuid,not null
     * @param version
     *            版本号，not null
     * @throws IllegalArgumentException
     * @throws VersionConflictException
     * @throws WMSException
     */
    void remove(String uuid, long version) throws WMSException;

    /**
     * 根据UUID获取退仓单
     * 
     * @param uuid
     *            uuid,如为空，则返回null
     * @return 退仓单实体
     */
    ReturnBill get(String uuid);

    /**
     * 根据单号获取退仓单
     * 
     * @param billNumber
     *            单号，如为空，则返回null
     * @return 退仓单实体
     */
    ReturnBill getByBillNumber(String billNumber);

    /**
     * 分页查询退仓单
     * 
     * @param definition
     *            过滤条件，not null
     * @return 分页结果集
     * @throws IllegalArgumentException
     */
    PageQueryResult<ReturnBill> query(PageQueryDefinition definition);

    /**
     * 审核退仓单
     * 
     * @param uuid
     *            UUID，not null
     * @param version
     *            版本号，not null
     * @throws IllegalArgumentException
     * @throws VersionConflictException
     * @throws WMSException
     */
    void audit(String uuid, long version) throws WMSException;
}
