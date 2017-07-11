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

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.ia.resource.Resource;
import com.hd123.sardine.wms.api.ia.resource.ResourceConstants;
import com.hd123.sardine.wms.api.ia.resource.ResourceService;
import com.hd123.sardine.wms.api.ia.user.Company;
import com.hd123.sardine.wms.api.ia.user.CompanyService;
import com.hd123.sardine.wms.api.ia.user.User;
import com.hd123.sardine.wms.api.ia.user.UserService;
import com.hd123.sardine.wms.api.ia.user.UserState;
import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.Constants;
import com.hd123.sardine.wms.common.utils.DBUtils;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.dao.ia.user.CompanyDao;
import com.hd123.sardine.wms.dao.ia.user.UserDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.log.EntityLogger;

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
  private EntityLogger logger;

  @Override
  public String insert(Company company) throws IllegalArgumentException, WMSException {
    Assert.assertArgumentNotNull(company, "company");
    Assert.assertArgumentNotNull(company.getName(), "company.name");
    Assert.assertArgumentNotNull(company.getAddress(), "company.address");

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
    user.setPasswd(company.getPassword());
    user.setAdministrator(true);
    user.setPhone(company.getAdminPhone());
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
    List<Resource> forwardResources = resourceService
        .queryByUpperResource(ResourceConstants.FORWARD_UUID);
    resourceUuids.add(ResourceConstants.FORWARD_UUID);
    for (Resource resource : forwardResources) {
      resourceUuids.add(resource.getUuid());
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

    logger.injectContext(this, company.getUuid(), Company.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_ADDNEW, "新建公司");
    return company.getUuid();
  }

  @Override
  public Company getByName(String name) {
    if (StringUtil.isNullOrBlank(name))
      return null;
    return companyDao.getByName(name);
  }

  @Override
  public Company get(String uuid) throws IllegalArgumentException {
    if (StringUtil.isNullOrBlank(uuid))
      return null;
    Company company = companyDao.get(uuid);
    return company;
  }

  @Override
  public void update(Company company)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(company, "company");

    Company oldCompany = companyDao.get(company.getUuid());
    if (oldCompany == null)
      throw new IllegalArgumentException("修改的仓库不存在！");
    PersistenceUtils.checkVersion(company.getVersion(), oldCompany, "苍龙", company.getName());

    if (company.getAdminCode().equals(oldCompany.getAdminCode()) == false) {
      User newAdmin = userDao.getByCode(company.getAdminCode());
      if (newAdmin == null) {
        User user = new User();
        user.setUuid(UUIDGenerator.genUUID());
        user.setCode(company.getAdminCode());
        user.setCompanyUuid(company.getUuid());
        user.setCompanyCode(company.getCode());
        user.setCompanyName(company.getName());
        user.setName(company.getAdminName());
        user.setPasswd(company.getPassword());
        user.setAdministrator(true);
        user.setPhone(company.getAdminPhone());
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
        List<Resource> forwardResources = resourceService
            .queryByUpperResource(ResourceConstants.FORWARD_UUID);
        resourceUuids.add(ResourceConstants.FORWARD_UUID);
        for (Resource resource : forwardResources) {
          resourceUuids.add(resource.getUuid());
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
      } else {
        newAdmin.setCompanyName(company.getName());
        newAdmin.setName(company.getAdminName());
        newAdmin.setPasswd(company.getPassword());
        newAdmin.setPhone(company.getAdminPhone());
        newAdmin.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
        userDao.update(newAdmin);
      }
    }
    company.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    companyDao.update(company);

    logger.injectContext(this, company.getUuid(), Company.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "编辑仓库");
  }

  @Override
  public List<Company> queryCompanys() {
    List<Company> companys = companyDao
        .queryCompanys(ApplicationContextUtil.getParentCompanyUuid());
    for (Company company : companys) {
      PageQueryDefinition definition = new PageQueryDefinition();
      definition.setCompanyUuid(company.getUuid());
      definition.put(UserService.QUERY_ADMINISTRATOR_FIELD, true);
      definition.setPageSize(0);
      List<User> users = userDao.query(definition);
      for (User user : users) {
        if (user.getCode().equals(company.getAdminCode())) {
          company.setAdminName(user.getName());
          company.setAdminPhone(user.getPhone());
          company.setPassword(user.getPasswd());
          break;
        }
      }
      company.setUsers(users);
    }
    return companys;
  }
}
