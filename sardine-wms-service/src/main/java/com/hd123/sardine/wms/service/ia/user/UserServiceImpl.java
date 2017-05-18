/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	UserServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月19日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.ia.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.ia.resource.Resource;
import com.hd123.sardine.wms.api.ia.resource.ResourceService;
import com.hd123.sardine.wms.api.ia.user.User;
import com.hd123.sardine.wms.api.ia.user.UserService;
import com.hd123.sardine.wms.api.ia.user.UserState;
import com.hd123.sardine.wms.common.entity.OperateContext;
import com.hd123.sardine.wms.common.entity.OperateInfo;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.common.validator.ValidateHandler;
import com.hd123.sardine.wms.common.validator.ValidateResult;
import com.hd123.sardine.wms.common.validator.routines.NullValidator;
import com.hd123.sardine.wms.common.validator.routines.VersionValidator;
import com.hd123.sardine.wms.dao.ia.user.UserDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.ia.user.validator.UserInsertValidateHandler;
import com.hd123.sardine.wms.service.ia.user.validator.UserRemoveValidateHandler;
import com.hd123.sardine.wms.service.ia.user.validator.UserUpdateValidateHandler;
import com.hd123.sardine.wms.service.log.EntityLogger;

/**
 * 用户服务实现
 * 
 * @author zhangsai
 *
 */
public class UserServiceImpl extends BaseWMSService implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ValidateHandler<User> userInsertValidateHandler;

    @Autowired
    private ValidateHandler<User> userUpdateValidateHandler;

    @Autowired
    private ValidateHandler<String> userRemoveValidateHandler;

    @Autowired
    private ValidateHandler<OperateContext> operateContextValidateHandler;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private EntityLogger logger;

    @Override
    public User get(String uuid) {
        if (StringUtil.isNullOrBlank(uuid))
            return null;
        return userDao.get(uuid);
    }

    @Override
    public User getByCode(String code) {
        if (StringUtil.isNullOrBlank(code))
            return null;
        return userDao.getByCode(code);
    }

    @Override
    public PageQueryResult<User> query(PageQueryDefinition definition)
            throws IllegalArgumentException {
        Assert.assertArgumentNotNull(definition, "definition");

        PageQueryResult<User> pgr = new PageQueryResult<User>();
        List<User> list = userDao.query(definition);
        PageQueryUtil.assignPageInfo(pgr, definition);
        pgr.setRecords(list);
        return pgr;
    }

    @Override
    public String insert(User user, OperateContext operCtx)
            throws IllegalArgumentException, WMSException {
        User existsUser = userDao.getByCode(user == null ? null : user.getCode());
        User operatorUser = userDao.get((operCtx == null || operCtx.getOperator() == null) ? null
                : operCtx.getOperator().getId());

        ValidateResult insertResult = userInsertValidateHandler
                .putAttribute(UserInsertValidateHandler.KEY_CODEEXISTS_USER, existsUser)
                .putAttribute(UserInsertValidateHandler.KEY_OPERATOR_USER, operatorUser)
                .validate(user);
        checkValidateResult(insertResult);
        ValidateResult operCtxResult = operateContextValidateHandler.validate(operCtx);
        checkValidateResult(operCtxResult);

        user.setUuid(UUIDGenerator.genUUID());
        user.setPasswd(User.DEFAULT_PASSWD);
        user.setCompanyUuid(operatorUser.getCompanyUuid());
        user.setCompanyCode(operatorUser.getCompanyCode());
        user.setCompanyName(operatorUser.getCompanyName());
        user.setCreateInfo(OperateInfo.newInstance(operCtx));
        user.setLastModifyInfo(OperateInfo.newInstance(operCtx));
        userDao.insert(user);

        logger.injectContext(this, user.getUuid(), User.class.getName(), operCtx);
        logger.log(EntityLogger.EVENT_ADDNEW, "新建用户");
        return user.getUuid();
    }

    @Override
    public void update(User user, OperateContext operCtx)
            throws IllegalArgumentException, WMSException {
        User updateUser = userDao.get(user == null ? null : user.getUuid());
        User existsUser = userDao.getByCode(user == null ? null : user.getCode());
        ValidateResult updateResult = userUpdateValidateHandler
                .putAttribute(UserUpdateValidateHandler.KEY_CODEEXISTS_USER, existsUser)
                .putAttribute(UserUpdateValidateHandler.KEY_UPDATE_USER, updateUser)
                .putAttribute(NullValidator.KEY_CURRENTOPERATOR_UUID,
                        user == null ? null : user.getUuid())
                .putAttribute(VersionValidator.ATTR_KEY_VERSION, user.getVersion()).validate(user);
        checkValidateResult(updateResult);
        ValidateResult operCtxResult = operateContextValidateHandler.validate(operCtx);
        checkValidateResult(operCtxResult);

        user.setLastModifyInfo(OperateInfo.newInstance(operCtx));
        userDao.update(user);

        logger.injectContext(this, user.getUuid(), User.class.getName(), operCtx);
        logger.log(EntityLogger.EVENT_MODIFY, "修改用户");
    }

    @Override
    public void remove(String uuid, long version, OperateContext operCtx)
            throws IllegalArgumentException, WMSException {
        User deleteUser = userDao.get(uuid);
        ValidateResult removeResult = userRemoveValidateHandler
                .putAttribute(UserRemoveValidateHandler.KEY_OPERATOR_USER, deleteUser)
                .putAttribute(VersionValidator.ATTR_KEY_VERSION, version).validate(uuid);
        checkValidateResult(removeResult);
        ValidateResult operCtxResult = operateContextValidateHandler.validate(operCtx);
        checkValidateResult(operCtxResult);

        userDao.removeRolesByUser(uuid);
        resourceService.removeResourceByUser(uuid);
        userDao.remove(uuid, version);

        logger.injectContext(this, uuid, User.class.getName(), operCtx);
        logger.log(EntityLogger.EVENT_REMOVE, "删除用户");
    }

    @Override
    public void online(String uuid, long version, OperateContext operCtx)
            throws IllegalArgumentException, WMSException {
        User onlineUser = userDao.get(uuid);
        ValidateResult removeResult = userRemoveValidateHandler
                .putAttribute(UserRemoveValidateHandler.KEY_OPERATOR_USER, onlineUser)
                .putAttribute(VersionValidator.ATTR_KEY_VERSION, version).validate(uuid);
        checkValidateResult(removeResult);
        ValidateResult operCtxResult = operateContextValidateHandler.validate(operCtx);
        checkValidateResult(operCtxResult);

        if (onlineUser.getUserState().equals(UserState.online))
            return;

        onlineUser.setUserState(UserState.online);
        onlineUser.setLastModifyInfo(OperateInfo.newInstance(operCtx));
        userDao.update(onlineUser);

        logger.injectContext(this, uuid, User.class.getName(), operCtx);
        logger.log(EntityLogger.EVENT_MODIFY, "启用用户");
    }

    @Override
    public void offline(String uuid, long version, OperateContext operCtx)
            throws IllegalArgumentException, WMSException {
        User offlineUser = userDao.get(uuid);
        ValidateResult removeResult = userRemoveValidateHandler
                .putAttribute(UserRemoveValidateHandler.KEY_OPERATOR_USER, offlineUser)
                .putAttribute(VersionValidator.ATTR_KEY_VERSION, version).validate(uuid);
        checkValidateResult(removeResult);
        ValidateResult operCtxResult = operateContextValidateHandler.validate(operCtx);
        checkValidateResult(operCtxResult);

        if (offlineUser.getUserState().equals(UserState.offline))
            return;

        offlineUser.setUserState(UserState.offline);
        offlineUser.setLastModifyInfo(OperateInfo.newInstance(operCtx));
        userDao.update(offlineUser);

        logger.injectContext(this, uuid, User.class.getName(), operCtx);
        logger.log(EntityLogger.EVENT_MODIFY, "停用用户");
    }

    @Override
    public void saveUserRoles(String userUuid, List<String> roleUuids) {
        Assert.assertArgumentNotNull(userUuid, "userUuid");

        userDao.removeRolesByUser(userUuid);
        for (String roleUuid : roleUuids) {
            userDao.saveUserRole(userUuid, roleUuid);
        }

        Set<Resource> userResources = new HashSet<Resource>();
        userResources.addAll(resourceService.queryAllResourceByUser(userUuid));
        for (String roleUuid : roleUuids) {
            List<Resource> roleResources = resourceService.queryOwnedResourceByRole(roleUuid);
            userResources.addAll(roleResources);
        }

        List<String> userResourcesList = new ArrayList<String>();
        for (Resource resource : userResources)
            userResourcesList.add(resource.getUuid());
        resourceService.saveUserResource(userUuid, userResourcesList);

        logger.injectContext(this, userUuid, User.class.getName(),
                ApplicationContextUtil.getOperateContext());
        logger.log(EntityLogger.EVENT_ADDNEW, "保存用户资源");
    }
}
