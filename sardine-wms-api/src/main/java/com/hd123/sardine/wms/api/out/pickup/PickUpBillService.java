/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-api
 * 文件名：	PickUpBillService.java
 * 模块说明：	
 * 修改历史：
 * 2014-8-5 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.out.pickup;

import java.math.BigDecimal;
import java.util.List;

import com.hd123.sardine.wms.api.task.TaskView;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * 拣货单服务：接口
 * 
 * @author zhangsai
 * 
 */
public interface PickUpBillService {
    /** 排序字段 */
    public static final String ORDER_BILLNUMBER = "billNumber";
    public static final String FIELD_ORDER_LASTMODIFYTIME = "lastModifyTime";
    public static final String FIELD_ORDER_PICKORDER = "pickorder";
    public static final String FIELD_ORDER_PICKAREACODE = "pickAreaCode";
    public static final String FIELD_ORDER_DELIVERYTARGETCODE = "deliveryTargetCode";
    public static final String FIELD_ORDER_WAVEBILLNUMBER = "waveBillNumber";

    /** 查询字段     */
    public static final String FIELD_QUERY_BILLNUMBERLIKE = "billNumberLike";
    public static final String FIELD_QUERY_WAVEBILLNUMBERLIKE = "waveBillNumberLike";
    public static final String FIELD_QUERY_STATEEQUALS = "stateEquals";
    public static final String FIELD_QUERY_CUSTOMERUUIDEQUALS = "customerUuidEquals";
    public static final String FIELD_QUERY_ARTICLECODELIKE = "articleCodeLike";
    public static final String FIELD_QUERY_PICKTYPEEQUALS = "pickTypeEquals";
    public static final String FIELD_QUERY_PICKERUUIDEQUALS = "pickerUuidEquals";
    public static final String FIELD_QUERY_DELIVERYTYPEEQUALS = "deliveryTypeEquals";
    public static final String FIELD_QUERY_METHODEQUALS = "methodEquals";
    public static final String FIELD_QUERY_DELIVERYSYSTEMEQUALS = "deliverySystemEquals";
    public static final String FIELD_QUERY_FROMCONTAINERBARCODELIKE = "fromContainerBarcodeLike";
    public static final String FIELD_QUERY_TOCONTAINERBARCODELIKE = "toContainerBarcodeLike";
    public static final String FIELD_QUERY_FROMBINCODELIKE = "fromBinCodeLike";
    public static final String FIELD_QUERY_PICKAREAEQUALS = "pickAreaEquals";

    
    /**
     * 新增拣货单
     * <p>
     * 波次生成的拣货单，状态为{@link PickUpBillState#inConfirm}
     * 
     * @param pickUpBill
     *            拣货单，not null
     * @throws WMSException
     * @throws IllegalArgumentException
     * @throws DBOperServiceException
     */
    void saveNew(PickUpBill pickUpBill) throws WMSException;

    /**
     * 根据uuid查询拣货单
     * 
     * @param uuid
     *            拣货单uuid
     * @return 存在则返回，不存在返回null
     * @throws DBOperServiceException
     */
    PickUpBill get(String uuid);

    /**
     * 根据单号查询拣货单
     * 
     * @param billNumber
     *            拣货单单号
     * @return 存在则返回，不存在返回null
     * @throws DBOperServiceException
     */
    PickUpBill getByBillNumber(String billNumber);

    /**
     * 分页查询拣货
     * 
     * @param definition
     *            查询条件，not null
     * @return 分页结果集
     */
    PageQueryResult<PickUpBill> query(PickUpBillFilter filter);

    /**
     * 分页查询拣货单
     * 
     * @param filter
     *            查询条件，not null
     * @return 分页结果集
     * @throws IllegalArgumentException
     * @throws DBOperServiceException
     */
    List<TaskView> queryPickTaskView(String waveBillNumber);

    /**
     * 批准该波次下的所有拣货单
     * <p>
     * <ol>
     * <li>修改拣货单状态为“已批准”</li>
     * </ol>
     * 
     * @param waveBillNumber
     *            波次单号，not null
     * @throws WMSException
     * @throws IllegalArgumentException
     */
    void approveByWaveBillNumber(String waveBillNumber);

    /**
     * 审核
     * 
     * 仅改变拣货单及明细的状态
     * 
     * @param billNumber
     *            拣货单单号 not null
     * @param operCtx
     * @throws WMSException
     */
    void audit(String uuid, long version) throws WMSException;

    /**
     * 波次单回滚时删除拣货单和拣货单明细
     * 
     * @param waveUuid
     *            波次单UUID
     */
    void removeByWaveUuid(String waveUuid) throws WMSException;

    /**
     * 拣货
     * 
     * @param pickItemUuids
     *            拣货单明细UUID集合， not null
     * @param toBinCode
     *            拣货目标货位，集货位，not null
     * @param toContainerBarcode
     *            拣货容器，not null
     * @throws WMSException
     */
    void pick(List<String> pickItemUuids, String toBinCode, String toContainerBarcode, UCN picker)
            throws WMSException;

    /**
     * 单个拣货明细执行可修改拣货数量
     * 
     * @param itemUuid
     *            拣货单明细UUID。 not null
     * @param toBinCode
     *            目标货位，not null
     * @param containerBarcode
     *            目标容器， not null
     * @param picker
     *            拣货员，为空取当前登录人
     * @param qty
     *            拣货数量，为空取拣货单明细数量
     * @throws WMSException
     */
    void pick(String itemUuid, String toBinCode, String containerBarcode, UCN picker,
            BigDecimal qty) throws WMSException;

    /**
     * 整托拣
     * 
     * @param containerBarcode
     *            拣货来源容器， not null
     * @param toBinCode
     *            拣货目标货位， not null
     * @param toContainerBarcode
     *            拣货目标容器，为空时使用来源容器
     * @param picker
     *            拣货员
     * @throws WMSException
     */
    void pick(String containerBarcode, String toBinCode, String toContainerBarcode, UCN picker)
            throws WMSException;
}
