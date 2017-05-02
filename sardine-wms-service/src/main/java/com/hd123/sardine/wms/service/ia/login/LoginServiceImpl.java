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

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.sardine.wms.api.ia.login.LoginService;
import com.hd123.sardine.wms.api.ia.login.UserInfo;
import com.hd123.sardine.wms.api.ia.user.Company;
import com.hd123.sardine.wms.api.ia.user.CompanyService;
import com.hd123.sardine.wms.api.ia.user.CompanyType;
import com.hd123.sardine.wms.api.ia.user.RegisterUser;
import com.hd123.sardine.wms.api.ia.user.User;
import com.hd123.sardine.wms.api.ia.user.UserState;
import com.hd123.sardine.wms.common.entity.OperateContext;
import com.hd123.sardine.wms.common.entity.OperateInfo;
import com.hd123.sardine.wms.common.entity.Operator;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.utils.Constants;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.common.validator.ValidateHandler;
import com.hd123.sardine.wms.common.validator.ValidateResult;
import com.hd123.sardine.wms.dao.ia.user.UserDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.ia.login.validator.RegisterValidateHandler;
import com.hd123.sardine.wms.service.ia.login.validator.UpdatePasswdValidateHandler;
import com.hd123.sardine.wms.service.util.FlowCodeGenerator;

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
  private FlowCodeGenerator flowCodeGenerator;

  @Autowired
  private CompanyService companyService;

  @Autowired
  private ValidateHandler<String> updatePasswdValidateHandler;

  @Autowired
  private ValidateHandler<OperateContext> operateContextValidateHandler;

  @Autowired
  private ValidateHandler<RegisterUser> registerValidateHandler;

  @Override
  public UserInfo register(RegisterUser registerUser) throws WMSException {
    User existsUser = userDao.getByCode(registerUser != null ? registerUser.getCode() : null);
    ValidateResult result = registerValidateHandler
        .putAttribute(RegisterValidateHandler.KEY_CODEEXISTS_USER, existsUser)
        .validate(registerUser);
    checkValidateResult(result);

    Company dbCompany = companyService.getByName(registerUser.getCompanyName());
    if (dbCompany != null)
      throw new WMSException("企业已存在，不能重复注册");

    String prefix = Constants.RESOURCE_PREFIX;
    String codePrefix;
    if (registerUser.getCompanyType().equals(CompanyType.carrier.name())) {
      prefix = prefix + Constants.CARR_PREFIX;
      codePrefix = Constants.CARR_CODE_PREFIX;
    } else if (registerUser.getCompanyType().equals(CompanyType.deliveryCenter.name())) {
      prefix = prefix + Constants.DC_PREFIX;
      codePrefix = Constants.DC_CODE_PREFIX;
    } else {
      prefix = prefix + Constants.SUPP_PREFIX;
      codePrefix = Constants.SUPP_CODE_PREFIX;
    }

    String flowCode = flowCodeGenerator.allocate(registerUser.getCompanyType(),
        Constants.VIRTUAL_COMPANYUUID, Constants.COMPANY_FLOW_LENGTH);

    Company company = new Company();
    company.setUuid(prefix + flowCode);
    company.setAddress(registerUser.getAddress());
    company.setCode(codePrefix + flowCode);
    company.setCompanyType(CompanyType.valueOf(registerUser.getCompanyType()));
    company.setHomePage(registerUser.getHomePage());
    company.setName(registerUser.getCompanyName());

    User user = new User();
    user.setCompanyUuid(company.getUuid());
    user.setCompanyCode(company.getCode());
    user.setCompanyName(company.getName());
    user.setName(registerUser.getName());
    user.setUserState(UserState.online);
    user.setPasswd(registerUser.getPasswd());
    user.setCode(registerUser.getCode());
    user.setPhone(registerUser.getPhone());
    user.setUuid(UUIDGenerator.genUUID());
    user.setCreateInfo(new OperateInfo(new Operator("sardine", "sardine", "注册用户")));
    user.setLastModifyInfo(new OperateInfo(new Operator("sardine", "sardine", "注册用户")));
    userDao.insert(user);
    OperateContext operCtx = new OperateContext(
        new Operator(user.getUuid(), user.getCode(), user.getName()), null);
    companyService.insert(company, operCtx);
    return user.fetchUserInfo();
  }

  @Override
  public UserInfo updatePasswd(String userUuid, String oldPasswd, String newPasswd,
      OperateContext operCtx) throws IllegalArgumentException, WMSException {
    User user = userDao.get(userUuid);
    ValidateResult result = updatePasswdValidateHandler
        .putAttribute(UpdatePasswdValidateHandler.KEY_UPDATEPASSWD_USER, user).validate(userUuid);
    checkValidateResult(result);

    ValidateResult operCtxResult = operateContextValidateHandler.validate(operCtx);
    checkValidateResult(operCtxResult);

    if (ObjectUtils.notEqual(user.getPasswd(), oldPasswd))
      throw new WMSException("原始密码错误");

    userDao.updatePasswd(userUuid, oldPasswd, newPasswd, operCtx);
    return user.fetchUserInfo();
  }

  @Override
  public UserInfo login(String userCode, String passwd)
      throws IllegalArgumentException, WMSException {
    User user = userDao.login(userCode, passwd);
    if (user == null)
      throw new WMSException("登录失败，用户名或密码错误。");

    if (user.getUserState().equals(UserState.online) == false)
      throw new WMSException("登录失败，当前用户未被启用。");

    return user.fetchUserInfo();
  }
}
