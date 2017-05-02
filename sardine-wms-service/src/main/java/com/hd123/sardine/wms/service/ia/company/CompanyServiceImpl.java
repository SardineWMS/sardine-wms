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

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.ia.user.Company;
import com.hd123.sardine.wms.api.ia.user.CompanyService;
import com.hd123.sardine.wms.common.entity.OperateContext;
import com.hd123.sardine.wms.common.entity.OperateInfo;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.utils.DBUtils;
import com.hd123.sardine.wms.common.validator.ValidateHandler;
import com.hd123.sardine.wms.common.validator.ValidateResult;
import com.hd123.sardine.wms.dao.ia.user.CompanyDao;
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
  private ValidateHandler<Company> companyInsertValidateHandler;

  @Autowired
  private ValidateHandler<OperateContext> operateContextValidateHandler;

  @Override
  public String insert(Company company, OperateContext operCtx)
      throws IllegalArgumentException, WMSException {
    Company dbCompany = getByName(company == null ? null : company.getName());

    ValidateResult validateResult = companyInsertValidateHandler
        .putAttribute(CompanyInsertValidateHandler.KEY_NAMEEXISTS_COMPANY, dbCompany)
        .validate(company);
    checkValidateResult(validateResult);
    ValidateResult operCtxResult = operateContextValidateHandler.validate(operCtx);
    checkValidateResult(operCtxResult);
    company.setCreateInfo(OperateInfo.newInstance(operCtx));
    company.setLastModifyInfo(OperateInfo.newInstance(operCtx));
    companyDao.insert(company);

    String dbName = DBUtils.fetchDbName(company.getUuid());
    companyDao.insertDBMap(company.getUuid(), dbName);
    return company.getUuid();
  }

  @Override
  public Company getByName(String name) {
    if (StringUtil.isNullOrBlank(name))
      return null;
    return companyDao.getByName(name);
  }

}
