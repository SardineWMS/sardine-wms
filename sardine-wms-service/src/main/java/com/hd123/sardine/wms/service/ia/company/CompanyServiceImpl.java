/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	CompanyServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月14日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.service.ia.company;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.ia.resource.Resource;
import com.hd123.sardine.wms.api.ia.resource.ResourceConstants;
import com.hd123.sardine.wms.api.ia.resource.ResourceService;
import com.hd123.sardine.wms.api.ia.user.Company;
import com.hd123.sardine.wms.api.ia.user.CompanyService;
import com.hd123.sardine.wms.api.ia.user.User;
import com.hd123.sardine.wms.api.ia.user.UserState;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.Constants;
import com.hd123.sardine.wms.common.utils.DBUtils;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.common.validator.ValidateHandler;
import com.hd123.sardine.wms.common.validator.ValidateResult;
import com.hd123.sardine.wms.dao.ia.user.CompanyDao;
import com.hd123.sardine.wms.dao.ia.user.UserDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.ia.company.validator.CompanyInsertValidateHandler;

/**
 * @author yangwenzhu
 *
 */
public class CompanyServiceImpl extends BaseWMSService implements CompanyService {

  @Autowired
  private CompanyDao companyDao;

  @Autowired
  private UserDao userDao;

  @Autowired
  private ResourceService resourceService;

  @Autowired
  private ValidateHandler<Company> companyInsertValidateHandler;

  @Override
  public String insert(Company company)
      throws IllegalArgumentException, WMSException {
    Company dbCompany = getByName(company == null ? null : company.getName());

    ValidateResult validateResult = companyInsertValidateHandler
        .putAttribute(CompanyInsertValidateHandler.KEY_NAMEEXISTS_COMPANY, dbCompany)
        .validate(company);
    checkValidateResult(validateResult);
    String flowCode = flowCodeGenerator.allocate(
        ApplicationContextUtil.getCompanyUuid().concat(Constants.DC_PREFIX),
        Constants.VIRTUAL_COMPANYUUID, 2);
    company.setUuid(
        ApplicationContextUtil.getCompanyUuid().concat(Constants.DC_PREFIX).concat(flowCode));
    company.setParentUuid(ApplicationContextUtil.getParentCompanyUuid());
    company.setCode(
        Constants.DC_CODE_PREFIX.concat(ApplicationContextUtil.getCompanyCode()).concat(flowCode));
    company.setCreateInfo(ApplicationContextUtil.getOperateInfo());
    company.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    companyDao.insert(company);

    String dbName = DBUtils.fetchDbName(company.getUuid());
    companyDao.insertDBMap(company.getUuid(), dbName);

    User user = new User();
    user.setUuid(UUIDGenerator.genUUID());
    user.setCode(company.getAdminCode());
    user.setCompanyUuid(company.getUuid());
    user.setCompanyCode(company.getCode());
    user.setCompanyName(company.getName());
    user.setName(company.getAdminName());
    user.setPasswd(User.DEFAULT_ADMIN_PASSWD);
    user.setAdministrator(true);
    user.setPhone(null);
    user.setUserState(UserState.online);
    user.setCreateInfo(ApplicationContextUtil.getOperateInfo());
    user.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    userDao.insert(user);

    List<String> resourceUuids = new ArrayList<String>();
    resourceUuids.add(ResourceConstants.DC_BASIC_UUID);
    List<Resource> basicResources = resourceService
        .queryByUpperResource(ResourceConstants.DC_BASIC_UUID);
    for (Resource r : basicResources) {
      resourceUuids.add(r.getUuid());
    }
    resourceUuids.remove(ResourceConstants.DC_BASIC_ARTICLE_DELETE_UUID);
    resourceUuids.remove(ResourceConstants.DC_BASIC_ARTICLE_EDIT_UUID);
    resourceUuids.remove(ResourceConstants.DC_BASIC_SUPPLIER_DELETE_UUID);
    resourceUuids.remove(ResourceConstants.DC_BASIC_SUPPLIER_EDIT_UUID);
    resourceUuids.remove(ResourceConstants.DC_BASIC_CUSTOMER_DELETE_UUID);
    resourceUuids.remove(ResourceConstants.DC_BASIC_CUSTOMER_EDIT_UUID);
    resourceUuids.remove(ResourceConstants.DC_BASIC_CATEGORY_DELETE_UUID);
    resourceUuids.remove(ResourceConstants.DC_BASIC_CATEGORY_EDIT_UUID);
    resourceService.saveUserResource(user.getUuid(), resourceUuids);
    return company.getUuid();
  }

  @Override
  public Company getByName(String name) {
    if (StringUtil.isNullOrBlank(name))
      return null;
    return companyDao.getByName(name);
  }

}
