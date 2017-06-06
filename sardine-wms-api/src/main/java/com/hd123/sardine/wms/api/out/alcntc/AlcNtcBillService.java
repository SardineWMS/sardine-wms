/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	AlcNtcBillService.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月31日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.out.alcntc;

import java.util.List;

import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * 配单服务|接口
 * 
 * @author yangwenzhu
 *
 */
public interface AlcNtcBillService {

    public static final String QUERY_BILLNUMBER_LIKE = "billNumber";
    public static final String QUERY_STATE_EQUALS = "state";
    public static final String QUERY_CUSTOMERCODE_LIKE = "customerCode";
    public static final String QUERY_TASKBILLNUMBER_LIKE = "taskBillNumber";
    public static final String QUEYR_SOURCEBILLNUMBER_LIKE = "sourceBillNumber";
    public static final String QUERY_WRH_EQUALS = "wrh";
    public static final String QUERY_DELIVERYMODE = "deliveryMode";

    /**
     * 新增配送通知单
     * 
     * @param alcNtcBill
     *            配单，not null
     * @throws IllegalArgumentException
     *             参数异常
     * @throws WMSException
     */
    String insert(AlcNtcBill alcNtcBill) throws IllegalArgumentException, WMSException;

    /**
     * 修改配单
     * 
     * @param alcNtcBill
     *            配单，not null
     * @throws IllegalArgumentException
     *             参数异常
     * @throws WMSException
     */
    void update(AlcNtcBill alcNtcBill) throws IllegalArgumentException, WMSException;

    /***
     * 作废配单 <br>
     * 只有初始状态可以作废
     * 
     * @param uuid
     *            要作废配单UUID，not null
     * @param version
     *            版本号，not null
     * @throws IllegalArgumentException
     *             参数异常
     * @throws VersionConflictException
     *             配单被其他用户修改
     * @throws WMSException
     */
    void abort(String uuid, long version)
            throws IllegalArgumentException, VersionConflictException, WMSException;

    /***
     * 完成配单
     * 
     * @param uuid
     *            要完成配单的UUID
     * @param version
     *            版本号，not null
     * @throws IllegalArgumentException
     *             参数异常
     * @throws VersionConflictException
     *             配单被其他用户修改
     * @throws WMSException
     */
    void finish(String uuid, long version)
            throws IllegalArgumentException, VersionConflictException, WMSException;

    /**
     * 分页查询配单
     * 
     * @param definition
     *            查询条件
     * @return 分页结果集
     * @throws IllegalArgumentException
     *             参数异常
     */
    PageQueryResult<AlcNtcBill> query(PageQueryDefinition definition)
            throws IllegalArgumentException;

    /**
     * 根据UUID获取配单
     * 
     * @param uuid
     *            配单UUID，not null
     * @return 配单
     * @throws IllegalArgumentException
     *             UUID为空时抛出
     */
    AlcNtcBill get(String uuid) throws IllegalArgumentException;

    /***
     * 根据单号获取配单
     * 
     * @param billNumber
     *            单号，not null
     * @return 配单
     * @throws IllegalArgumentException
     *             单号为空时抛出
     */
    AlcNtcBill getByBillNumber(String billNumber) throws IllegalArgumentException;

    /***
     * 加入波次 <br>
     * <br>
     * 配单加入波次后状态变为待配送，不能被其他波次使用
     * 
     * @param billNumber
     *            配单单号，not null
     * @param version
     *            版本号,not null
     * @param waveBillNumber
     *            波次单号，即作业号。not null
     * @throws IllegalArgumentException
     *             参数为空时抛出
     * @throws VersionConflictException
     *             配单被其他用户修改
     * @throws WMSException
     *             其他业务异常
     */
    void joinWave(String billNumber, long version, String waveBillNumber)
            throws IllegalArgumentException, VersionConflictException, WMSException;

    /**
     * 踢出波次 <br>
     * <br>
     * 将配单从波次作业中踢出，状态变为初始
     * 
     * @param billNumber
     *            配单单号，not null
     * @param version
     *            版本号，not null
     * @throws IllegalArgumentException
     *             参数为空时抛出
     * @throws VersionConflictException
     *             配单被其他用户修改
     * @throws WMSException
     *             其他业务异常
     */
    void leaveWave(String billNumber, long version)
            throws IllegalArgumentException, VersionConflictException, WMSException;

    /***
     * 删除配单
     * 
     * @param uuid
     *            配单UUID， not null
     * @param version
     *            版本号，not null
     * @throws IllegalArgumentException
     *             参数为空时抛出
     * @throws VersionConflictException
     *             配单被其他用户修改
     * @throws WMSException
     *             其他业务异常
     */
    void remove(String uuid, long version)
            throws IllegalArgumentException, VersionConflictException, WMSException;

    /***
     * 拣货
     * 
     * @param infos
     *            配送商品信息，not null
     * @throws IllegalArgumentException
     *             参数异常时抛出
     * @throws WMSException
     *             其他业务异常
     */
    void pickUp(List<DeliveryArticleInfo> infos) throws IllegalArgumentException, WMSException;

}
