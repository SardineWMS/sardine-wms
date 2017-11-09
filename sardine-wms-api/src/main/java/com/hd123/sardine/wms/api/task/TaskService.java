/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	TaskService.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月12日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.task;

import java.math.BigDecimal;
import java.util.List;

import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * 指令服务：接口
 * 
 * @author zhangsai
 *
 */
public interface TaskService {

    /** 查询条件 指令类型 */
    public static final String QUERY_FIELD_TASKTYPE = "taskType";
    /** 查询条件 指令状态 */
    public static final String QUERY_FIELD_STATE = "state";
    /** 查询条件 商品代码 */
    public static final String QUERY_FIELD_ARTICLECODE = "articleCode";
    /** 查询条件  来源容器 */
    public static final String QUERY_FIELD_FROMCONTAINERBARCODE = "fromContainerBarcode";
    /** 查询条件 目标容器*/
    public static final String QUERY_FIELD_TOCONTAINERBARCODE = "toContainerBarcode";
    /** 查询条件  操作人 */
    public static final String QUERY_FIELD_OPERATOR = "operator";
    /** 查询条件  操作方式 */
    public static final String QUERY_FIELD_TYPE = "type";
    /** 查询条件  来源单号 */
    public static final String QUERY_FIELD_SOURCEBILL = "sourceBillNumber";
    /** 查询条件  来源货位 */
    public static final String QUERY_FIELD_FROMBINCODE = "fromBinCode";
    /** 查询条件  客户 */
    public static final String QUERY_FIELD_CUSTOMER = "customer";
    /** 查询条件  配送方式 */
    public static final String QUERY_FIELD_DELIVERYTYPE = "deliveryType";
    /** 查询条件  拣货区域 */
    public static final String QUERY_FIELD_PICKAREA = "pickArea";
    /** 查询条件  拣货类型 */
    public static final String QUERY_FIELD_PICKMETHOD = "pickMethod";
    /** 查询条件  单号 */
    public static final String QUERY_FIELD_BILLNUMBER = "billNumber";
    /** 查询条件  供应商 */
    public static final String QUERY_FIELD_OWNER= "owner";
    /**
     * 批量插入指令
     * <p>
     * 指令必须是同一指令类型
     * 
     * @param tasks
     *            指令集合，not null
     * @throws IllegalArgumentException
     * @throws VersionConflictException
     * @throws WMSException
     */
    void insert(List<Task> tasks)
            throws IllegalArgumentException, VersionConflictException, WMSException;

    /**
     * 作废指令
     * 
     * @param uuid
     *            指令UUID，not null
     * @param version
     *            版本号
     * @throws IllegalArgumentException
     * @throws VersionConflictException
     * @throws WMSException
     */
    void abort(String uuid, long version)
            throws IllegalArgumentException, VersionConflictException, WMSException;

    /**
     * 执行上架指令
     * 
     * @param uuid
     *            指令UUID not null
     * @param version
     *            版本号
     * @param toBinCode
     *            目标货位，如果为空则使用指令原目标货位
     * @param toContainerBarcode
     *            上架的目标容器，如果为空，表示上架后没有容器
     * @throws IllegalArgumentException
     * @throws VersionConflictException
     * @throws WMSException
     */
    void putaway(String uuid, long version, String toBinCode, String toContainerBarcode)
            throws IllegalArgumentException, VersionConflictException, WMSException;

    /**
     * 执行移库指令
     * 
     * @param uuid
     *            指令UUID，not null
     * @param version
     *            版本号
     * @param qty
     *            数量，为空时取指令原数量
     */
    void move(String uuid, long version, BigDecimal qty);

    /**
     * 执行补货指令
     * 
     * @param uuid
     *            指令UUID，not null
     * @param version
     *            版本号
     * @throws WMSException
     */
    void rpl(String uuid, long version) throws WMSException;

    /**
     * 执行退货下架指令
     * 
     * @param uuid
     *            指令UUID， not null
     * @param version
     *            版本号
     * @param binCode
     *            下架目标货位，为空取指令原目标货位
     * @param containerBarcode
     *            目标容器，not null
     * @param qty
     *            下架数量，为空时取指令原数量
     */
    void rtnShelf(String uuid, long version, String binCode, String containerBarcode,
            BigDecimal qty) throws WMSException;

    /**
     * 分页查询
     * 
     * @param definition
     *            查询条件，not null。
     * @return 指令集合
     */
    PageQueryResult<TaskView> query(PageQueryDefinition definition);

    /**
     * 通过UUID获取指令
     * 
     * @param uuid
     *            uuid,if null return null
     * @return
     */
    Task get(String uuid);
}
