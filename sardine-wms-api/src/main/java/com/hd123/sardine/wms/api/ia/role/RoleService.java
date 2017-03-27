/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	RoleService.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月23日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.ia.role;

import com.hd123.sardine.wms.common.entity.OperateContext;
import com.hd123.sardine.wms.common.exception.EntityNotFoundException;
import com.hd123.sardine.wms.common.exception.IAException;
import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * 角色服务：接口
 * 
 * @author zhangsai
 *
 */
public interface RoleService {
    /** 代码查询，支持模糊查询 */
    public static final String QUERY_CODE_FIELD = "code";
    /** 名称查询，支持模糊查询 */
    public static final String QUERY_NAME_FIELD = "name";
    /** 状态查询 */
    public static final String QUERY_STATE_FIELD = "state";

    /** 代码排序 */
    public static final String ORDER_CODE_FIELD = "code";
    /** 名称排序 */
    public static final String ORDER_NAME_FIELD = "name";


    /**
     * 分页查询角色信息
     * 
     * @param definition
     *            搜索条件，not null。
     * @return 角色分页集合
     * @throws IllegalArgumentException
     */
    PageQueryResult<Role> query(PageQueryDefinition definition) throws IllegalArgumentException;

    /**
     * 新建角色
     * 
     * @param role
     *            角色，not nul。
     * @param operCtx
     *            操作信息，not null。
     * @return 角色uuid
     * @throws IllegalArgumentException
     * @throws IAException
     */
    String insert(Role role, OperateContext operCtx) throws IllegalArgumentException, IAException;

    /**
     * 更新角色
     * 
     * @param role
     *            角色实体，not null。
     * @param operCtx
     *            操作信息，not null。
     * @throws IllegalArgumentException
     * @throws EntityNotFoundException
     * @throws VersionConflictException
     * @throws IAException
     */
    void update(Role role, OperateContext operCtx) throws IllegalArgumentException,
            EntityNotFoundException, VersionConflictException, IAException;

    /**
     * 启用角色
     * 
     * @param uuid
     *            角色uuid，not null。
     * @param version
     *            角色版本号，not null。
     * @param operCtx
     *            操作信息，not null。
     * @throws IllegalArgumentException
     * @throws EntityNotFoundException
     * @throws VersionConflictException
     */
    void online(String uuid, long version, OperateContext operCtx)
            throws IllegalArgumentException, EntityNotFoundException, VersionConflictException;

    /**
     * 停用角色
     * 
     * @param uuid
     *            角色uuid，not null。
     * @param version
     *            角色版本号，not null。
     * @param operCtx
     *            操作信息，not null。
     * @throws IllegalArgumentException
     * @throws EntityNotFoundException
     * @throws VersionConflictException
     */
    void offline(String uuid, long version, OperateContext operCtx)
            throws IllegalArgumentException, EntityNotFoundException, VersionConflictException;

    /**
     * 删除角色
     * 
     * @param uuid
     *            角色uuid，not null。
     * @param version
     *            角色版本号，not null。
     * @param operCtx
     *            操作信息，not null。
     * @throws IllegalArgumentException
     * @throws EntityNotFoundException
     * @throws VersionConflictException
     */
    void remove(String uuid, long version, OperateContext operCtx)
            throws IllegalArgumentException, EntityNotFoundException, VersionConflictException;
}
