/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	CustomerService.java
 * 模块说明：	
 * 修改历史：
 * 2017年1月12日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.customer;

import com.hd123.sardine.wms.common.entity.OperateContext;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * @author yangwenzhu
 *
 */
public interface CustomerService {
    public static final String QUERY_CODE_FIELD = "code";
    public static final String QUERY_NAME_FIELD = "name";
    public static final String QUERY_STATE_FIELD = "state";

    public static final String ORDER_CODE_FIELD = "code";
    public static final String ORDER_NAME_FIELD = "name";

    Customer get(String uuid);

    /**
     * 新建客户
     * <li>状态默认正常，不可修改。只能通过删除接口将其删除</li>
     * 
     * @param customer
     *            客户，not null
     * @param operCtx
     *            操作人，not null
     * @return 新建客户uuid
     * @throws IllegalArgumentException
     *             参数异常
     * @throws WMSException
     */
    String insert(Customer customer, OperateContext operCtx)
            throws IllegalArgumentException, WMSException;

    /**
     * 删除客户
     * <li>只是标记删除，删除后状态为“已删除”</li>
     * 
     * 
     * @param uuid
     *            要删除客户的uuid，not null
     * @param version
     *            版本号，not null
     * @param operCtx
     *            操作人，not null
     * @throws IllegalArgumentException
     *             参数异常时抛出
     * @throws WMSException
     */
    void removeState(String uuid, long version, OperateContext operCtx)
            throws IllegalArgumentException, WMSException;

    /**
     * 编辑客户
     * <li>修改时，不能修改其状态。只能通过删除接口将其删除</li>
     * 
     * @param customer
     *            修改的客户，not null
     * @param operCtx
     *            操作人，not null
     * @throws IllegalArgumentException
     *             参数异常
     * @throws WMSException
     */
    void update(Customer customer, OperateContext operCtx)
            throws IllegalArgumentException, WMSException;

    /**
     * 通过代码获取客户
     * 
     * @param code
     *            客户代码，not null
     * @return 客户
     */
    Customer getByCode(String code);

    /**
     * 分页查询客户
     * 
     * @param definition
     *            分页条件
     * @return 客户集合
     */
    PageQueryResult<Customer> query(PageQueryDefinition definition);

    void recover(String uuid, long version, OperateContext operCtx)
            throws IllegalArgumentException, WMSException;
}
