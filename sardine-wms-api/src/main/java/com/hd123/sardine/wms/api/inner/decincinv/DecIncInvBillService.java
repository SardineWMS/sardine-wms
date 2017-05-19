/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	DecIncInvBillService.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月17日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.inner.decincinv;

import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * 损益单接口
 * 
 * @author fanqingqing
 *
 */
public interface DecIncInvBillService {
    /** 查询条件 */
    public static final String QUERY_BILLNUMBER_LIKE = "billNumber";
    public static final String QUERY_STATE_EQUALS = "state";
    public static final String QUERY_TYPE_EQUALS = "type";
    public static final String QUERY_WRHCODE_LIKE = "wrhCode";

    /** 排序字段 */
    public static final String FIELD_ORDER_BILLNO = "billNumber";

    /**
     * 根据uuid获取损溢单
     * 
     * @param billUuid
     *            损益单uuid，not null。
     * @return 损溢单
     */
    DecIncInvBill get(String billUuid);

    /**
     * 根据单号获取损溢单
     * 
     * @param billNumber
     *            损溢单单号
     * @return 损溢单
     */
    DecIncInvBill getByBillNumber(String billNumber);

    /**
     * 分页查询损溢单
     * 
     * @param definition
     *            搜索条件，not null。
     * @return 损溢单集合
     */
    PageQueryResult<DecIncInvBill> query(PageQueryDefinition definition);

    /**
     * 新增损溢单
     * 
     * @param bill
     *            损溢单，not null
     * @return 损溢单uuid
     * @throws WMSException
     */
    String saveNew(DecIncInvBill bill) throws WMSException;

    /**
     * 修改损溢单
     * 
     * @param bill
     *            损溢单，not null。
     * @throws WMSException
     */
    void saveModify(DecIncInvBill bill) throws WMSException;

    /**
     * 删除
     * 
     * @param billUuid
     *            损溢单uuid，not null。
     * @param version
     *            损溢单版本号，not null。
     * @throws WMSException
     */
    void remove(String billUuid, long version) throws WMSException;

    /**
     * 审核
     * 
     * @param billUuid
     *            损溢单uuid，not null。
     * @param version
     *            损溢单版本号，not null。
     * @throws WMSException
     */
    void audit(String billUuid, long version) throws WMSException;

}
