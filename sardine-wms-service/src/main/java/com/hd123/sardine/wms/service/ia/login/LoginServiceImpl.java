/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	LoginServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月13日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.ia.login;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.sardine.wms.api.ia.login.LoginService;
import com.hd123.sardine.wms.api.ia.login.UserInfo;
import com.hd123.sardine.wms.api.ia.user.User;
import com.hd123.sardine.wms.api.ia.user.UserState;
import com.hd123.sardine.wms.common.entity.OperateContext;
import com.hd123.sardine.wms.common.entity.OperateInfo;
import com.hd123.sardine.wms.common.entity.Operator;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.utils.FlowCodeGenerator;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.common.validator.ValidateHandler;
import com.hd123.sardine.wms.common.validator.ValidateResult;
import com.hd123.sardine.wms.dao.ia.user.UserDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.ia.login.validator.RegisterValidateHandler;
import com.hd123.sardine.wms.service.ia.login.validator.UpdatePasswdValidateHandler;

/**
 * 登录服务实现
 * 
 * @author zhangsai
 *
 */
public class LoginServiceImpl extends BaseWMSService implements LoginService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ValidateHandler<String> loginValidateHandler;

    @Autowired
    private ValidateHandler<String> updatePasswdValidateHandler;

    @Autowired
    private ValidateHandler<OperateContext> operateContextValidateHandler;

    @Autowired
    private ValidateHandler<User> registerValidateHandler;

    @Override
    public UserInfo register(User user) throws IllegalArgumentException, WMSException {
        User existsUser = userDao.getByCode(user != null ? user.getCode() : null);
        ValidateResult result = registerValidateHandler
                .putAttribute(RegisterValidateHandler.KEY_CODEEXISTS_USER, existsUser)
                .validate(user);
        checkValidateResult(result);

        user.setCompanyUuid(FlowCodeGenerator.getInstance().allocate(User.COMPANYUUID_FLOWTYPE));
        user.setCompanyCode(User.COMPANYCODE_PREFIX
                + user.getCompanyUuid().substring(1, user.getCompanyUuid().length() - 1));
        user.setUserState(UserState.online);
        user.setUuid(UUIDGenerator.genUUID());
        user.setCreateInfo(new OperateInfo(new Operator("sardine", "sardine", "注册用户")));
        user.setLastModifyInfo(new OperateInfo(new Operator("sardine", "sardine", "注册用户")));
        userDao.insert(user);

        return user.fetchUserInfo();
    }

    @Override
    public UserInfo updatePasswd(String userUuid, String oldPasswd, String newPasswd,
            OperateContext operCtx) throws IllegalArgumentException, WMSException {
        User user = userDao.get(userUuid);
        ValidateResult result = updatePasswdValidateHandler
                .putAttribute(UpdatePasswdValidateHandler.KEY_UPDATEPASSWD_USER, user)
                .validate(userUuid);
        checkValidateResult(result);

        ValidateResult operCtxResult = operateContextValidateHandler.validate(operCtx);
        checkValidateResult(operCtxResult);

        userDao.updatePasswd(userUuid, oldPasswd, newPasswd);
        return user.fetchUserInfo();
    }

    @Override
    public UserInfo login(String userCode, String passwd)
            throws IllegalArgumentException, WMSException {
        ValidateResult result = loginValidateHandler.validate(userCode);
        checkValidateResult(result);

        User user = userDao.login(userCode, passwd);
        if (user == null)
            throw new WMSException("登录失败，用户名或密码错误。");

        if (user.getUserState().equals(UserState.online) == false)
            throw new WMSException("登录失败，当前用户未被启用。");

        return user.fetchUserInfo();
    }
}
