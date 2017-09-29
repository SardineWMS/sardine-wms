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

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.ia.login.LoginService;
import com.hd123.sardine.wms.api.ia.login.UserInfo;
import com.hd123.sardine.wms.api.ia.resource.Resource;
import com.hd123.sardine.wms.api.ia.resource.ResourceService;
import com.hd123.sardine.wms.api.ia.user.Company;
import com.hd123.sardine.wms.api.ia.user.CompanyService;
import com.hd123.sardine.wms.api.ia.user.CompanyType;
import com.hd123.sardine.wms.api.ia.user.RegisterInfo;
import com.hd123.sardine.wms.api.ia.user.RegisterUser;
import com.hd123.sardine.wms.api.ia.user.User;
import com.hd123.sardine.wms.api.ia.user.UserState;
import com.hd123.sardine.wms.common.entity.OperateContext;
import com.hd123.sardine.wms.common.entity.OperateInfo;
import com.hd123.sardine.wms.common.entity.Operator;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.Constants;
import com.hd123.sardine.wms.common.utils.SerializationUtils;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.common.validator.ValidateHandler;
import com.hd123.sardine.wms.common.validator.ValidateResult;
import com.hd123.sardine.wms.dao.ia.user.CompanyDao;
import com.hd123.sardine.wms.dao.ia.user.UserDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.ia.login.validator.RegisterValidateHandler;
import com.hd123.sardine.wms.service.ia.login.validator.UpdatePasswdValidateHandler;
import com.hd123.sardine.wms.service.log.EntityLogger;

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
  private CompanyDao companyDao;

  @Autowired
  private CompanyService companyService;

  @Autowired
  private ResourceService resourceService;

  @Autowired
  private ValidateHandler<String> updatePasswdValidateHandler;

  @Autowired
  private ValidateHandler<RegisterUser> registerValidateHandler;

  @Autowired
  private EntityLogger logger;

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

    // String prefix = Constants.RESOURCE_PREFIX;

    String companyUuid = billNumberGenerator.allocateCompanyUuid();
    String flowCode = companyUuid.replaceAll(Constants.RESOURCE_PREFIX, "");

    Company company = new Company();
    company.setUuid(companyUuid);
    company.setAddress(registerUser.getAddress());
    company.setCode(Constants.DC_CODE_PREFIX + flowCode);
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
    user.setId(registerUser.getId());
    user.setEmail(registerUser.getEmail());
    user.setRemark(registerUser.getRemark());
    user.setUuid(UUIDGenerator.genUUID());
    user.setCreateInfo(new OperateInfo(new Operator("sardine", "sardine", "注册用户")));
    user.setLastModifyInfo(new OperateInfo(new Operator("sardine", "sardine", "注册用户")));
    userDao.insert(user);
    companyService.insert(company);

    logger.injectContext(this, user.getUuid(), User.class.getName(), new OperateContext(
        new Operator(user.getUuid(), user.getCode(), user.getName()), new Date()));
    logger.log(EntityLogger.EVENT_ADDNEW, "注册用户");
    return user.fetchUserInfo();
  }

  @Override
  public UserInfo updatePasswd(String userUuid, String oldPasswd, String newPasswd)
      throws IllegalArgumentException, WMSException {
    User user = userDao.get(userUuid);
    ValidateResult result = updatePasswdValidateHandler
        .putAttribute(UpdatePasswdValidateHandler.KEY_UPDATEPASSWD_USER, user).validate(userUuid);
    checkValidateResult(result);

    if (ObjectUtils.notEqual(user.getPasswd(), oldPasswd))
      throw new WMSException("原始密码错误");

    userDao.updatePasswd(userUuid, oldPasswd, newPasswd,
        ApplicationContextUtil.getOperateContext());

    logger.injectContext(this, userUuid, User.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "用户修改密码");
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

    List<Resource> ownedMenus = null;
    List<Resource> ownedResources = null;
    ApplicationContextUtil
        .setCompany(new UCN(user.getCompanyUuid(), user.getCompanyCode(), user.getCompanyName()));
    if (user.isAdministrator()) {
      ownedMenus = resourceService.queryOwnedMenuByUserType(ApplicationContextUtil.getUserType());
      ownedResources = resourceService
          .queryOwnedOperateByUserType(ApplicationContextUtil.getUserType());
    } else {
      ownedMenus = resourceService.queryOwnedMenuResourceByUser(user.getUuid());
      ownedResources = resourceService.queryOwnedOperateByUser(user.getUuid());
    }

    UserInfo userInfo = user.fetchUserInfo();
    userInfo.setOwnedMenus(SerializationUtils.serialize(ownedMenus));
    userInfo.setOwnedResources(SerializationUtils.serialize(ownedResources));
    return userInfo;
  }

  @Override
  public UserInfo register(RegisterInfo registerInfo)
      throws IllegalArgumentException, WMSException {
    Assert.assertArgumentNotNull(registerInfo, "registerInfo");
    registerInfo.validate();

    User existsUser = userDao.getByCode(registerInfo.getLoginId());
    if (existsUser != null)
      throw new WMSException("用户名已存在！");

    User user = new User();
    user.setUuid(UUIDGenerator.genUUID());
    user.setCode(registerInfo.getLoginId());
    String companyUuid = billNumberGenerator.allocateCompanyUuid();
    String flowCode = companyUuid.replaceAll(Constants.RESOURCE_PREFIX, "");
    user.setCompanyUuid(Constants.RESOURCE_PREFIX.concat(flowCode));
    user.setCompanyCode(Constants.DC_CODE_PREFIX + flowCode);
    user.setCompanyName(registerInfo.getCompanyName());
    user.setName("企业管理员");
    user.setPasswd(registerInfo.getPasswd());
    user.setPhone(null);
    user.setAdministrator(true);
    user.setUserState(UserState.online);
    user.setCreateInfo(new OperateInfo(new Operator("sardine", "sardine", "注册用户")));
    user.setLastModifyInfo(new OperateInfo(new Operator("sardine", "sardine", "注册用户")));
    userDao.insert(user);

    ApplicationContextUtil
        .setCompany(new UCN(user.getCompanyUuid(), user.getCompanyCode(), user.getCompanyName()));
    String dbName = ApplicationContextUtil.getDBName();
    companyDao.insertDBMap(user.getCompanyUuid(), dbName);

    // List<String> resourceUuids = new ArrayList<String>();
    // List<Resource> basicResources = resourceService
    // .queryByUpperResource(Resource.BASIC_RESOURCE_UUID);
    // resourceUuids.add(Resource.BASIC_RESOURCE_UUID);
    // for (Resource resource : basicResources) {
    // resourceUuids.add(resource.getUuid());
    // }
    // List<Resource> systemResources = resourceService
    // .queryByUpperResource(Resource.SYSTEM_RESOURCE_UUID);
    // resourceUuids.add(Resource.SYSTEM_RESOURCE_UUID);
    // for (Resource resource : systemResources) {
    // resourceUuids.add(resource.getUuid());
    // }
    // resourceService.saveUserResource(user.getUuid(), resourceUuids);
    return user.fetchUserInfo();
  }
}
