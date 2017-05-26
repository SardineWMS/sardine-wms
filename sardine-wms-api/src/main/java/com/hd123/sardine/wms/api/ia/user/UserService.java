/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	UserService.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月13日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.ia.user;

import java.util.List;

import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;

/**
 * 用户服务接口
 * <p>
 * 包括：
 * <ul>
 * <li>根据uuid查询用户</li>
 * <li>根据代码查询用户</li>
 * <li>分页查询用户</li>
 * <li>新增用户</li>
 * <li>修改用户</li>
 * <li>删除用户</li>
 * <li>启用禁用用户</li>
 * </ul>
 * 
 * @author zhangsai
 *
 */
public interface UserService {

    /** 代码查询，支持模糊查询 */
    public static final String QUERY_CODE_FIELD = "code";
    /** 名称查询，支持模糊查询 */
    public static final String QUERY_NAME_FIELD = "name";
    /** 电话查询，支持模糊查询 */
    public static final String QUERY_PHONE_FIELD = "phone";
    /** 用户状态查询 */
    public static final String QUERY_USERSTATE_FIELD = "userState";
    /** 用户拥有角色查询 */
    public static final String QUERY_ROLE_FIELD = "roleUuid";

    /** 代码排序 */
    public static final String ORDER_CODE_FIELD = "code";
    /** 名称排序 */
    public static final String ORDER_NAME_FIELD = "name";
    /** 创建时间排序 */
    public static final String ORDER_CREATEDTIME_FIELD = "createdtime";
    /** 修改时间排序 */
    public static final String ORDER_LASTMODIFYTIME_FIELD = "lastModifytime";

    /**
     * 获取用户根据用户uuid
     * 
     * @param uuid
     * @return 用户实体，不存在则返回null
     */
    User get(String uuid);

    /**
     * 获取用户根据用户代码
     * <p>
     * 用户代码全局唯一
     * 
     * @param code
     * @return 用户实体，不存在则返回null
     */
    User getByCode(String code);

    /**
     * 分页查询用户列表
     * 
     * @param definition
     *            查询条件，not null
     * @return 用户列表信息
     * @throws IllegalArgumentException
     *             definition为空
     */
    PageQueryResult<User> query(PageQueryDefinition definition) throws IllegalArgumentException;

    /**
     * 新增用户
     * <p>
     * 由注册的用户给自己系统增加新的用户，新增的用户与创建者组织一致<br>
     * 默认密码统一为888888，由新用户第一次登陆时修改
     * 
     * @param user
     *            用户信息，not null
     * @return 新用户id
     * @throws IllegalArgumentException
     *             参数为空
     * @throws WMSException
     *             用户代码重复
     */
    String insert(User user) throws IllegalArgumentException, WMSException;

    /**
     * 更新用户信息，主要用户修改用户基本资料
     * 
     * @param user
     *            用户信息，not null
     * @throws IllegalArgumentException
     *             参数为空
     * @throws WMSException
     *             用户代码重复，版本冲突、实体不存在
     */
    void update(User user) throws IllegalArgumentException, WMSException;

    /**
     * 删除用户
     * <p>
     * 只有用户所属组织的管理员才能删除用户
     * 
     * @param uuid
     *            要删除的用户uuid not null
     * @param version
     *            版本号
     * @throws IllegalArgumentException
     *             参数为空
     * @throws WMSException
     *             版本冲突
     */
    void remove(String uuid, long version) throws IllegalArgumentException, WMSException;

    /**
     * 启用用户
     * <p>
     * 只有用户所属组织的管理员才能启用用户，启用后的用户才能登陆系统
     * 
     * @param uuid
     *            要启用的用户uuid not null
     * @param version
     *            版本号
     * @throws IllegalArgumentException
     *             参数为空
     * @throws WMSException
     *             版本冲突
     */
    void online(String uuid, long version) throws IllegalArgumentException, WMSException;

    /**
     * 禁用用户
     * <p>
     * 只有用户所属组织的管理员才能禁用用户，禁用后的用户不能登陆系统
     * 
     * @param uuid
     *            要禁用的用户uuid not null
     * @param version
     *            版本号
     * @throws IllegalArgumentException
     *             参数为空
     * @throws WMSException
     *             版本冲突
     */
    void offline(String uuid, long version) throws IllegalArgumentException, WMSException;

    /**
     * 给用户分配角色
     * 
     * @param userUuids
     *            用户UUID，not null
     * @param roleUuids
     *            角色UUID集合，可以为空，为空时表示用户无任何角色
     * @throws IllegalArgumentException
     */
    void saveUserRoles(String userUuid, List<String> roleUuids);
}
