/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	CustomerServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年1月13日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.service.basicInfo.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.customer.Customer;
import com.hd123.sardine.wms.api.basicInfo.customer.CustomerService;
import com.hd123.sardine.wms.api.basicInfo.customer.CustomerState;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.common.validator.ValidateHandler;
import com.hd123.sardine.wms.common.validator.ValidateResult;
import com.hd123.sardine.wms.dao.basicInfo.customer.CustomerDao;
import com.hd123.sardine.wms.service.basicInfo.customer.validator.CustomerInsertValidateHandler;
import com.hd123.sardine.wms.service.basicInfo.customer.validator.CustomerRemoveAndRecoverValidateHandler;
import com.hd123.sardine.wms.service.basicInfo.customer.validator.CustomerUpdateValidateHandler;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.log.EntityLogger;

/**
 * @author yangwenzhu
 *
 */
public class CustomerServiceImpl extends BaseWMSService implements CustomerService {
    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private ValidateHandler<Customer> customerInsertValidateHandler;

    @Autowired
    private ValidateHandler<Customer> customerUpdateValidateHandler;

    @Autowired
    private ValidateHandler<Customer> customerRemoveAndRecoverValidateHandler;

    @Autowired
    private EntityLogger logger;

    @Override
    public String insert(Customer customer) throws IllegalArgumentException, WMSException {
        Customer dbCustomer = customerDao.getByCode(customer == null ? null : customer.getCode());

        ValidateResult insertResult = customerInsertValidateHandler
                .putAttribute(CustomerInsertValidateHandler.KEY_CODEEXISTS_CUSTOMER, dbCustomer)
                .validate(customer);
        checkValidateResult(insertResult);

        customer.setUuid(UUIDGenerator.genUUID());
        customer.setCompanyUuid(ApplicationContextUtil.getParentCompanyUuid());
        customer.setCreateInfo(ApplicationContextUtil.getOperateInfo());
        customer.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
        customer.setState(CustomerState.online);
        customerDao.insert(customer);

        logger.injectContext(this, customer.getUuid(), Customer.class.getName(),
                ApplicationContextUtil.getOperateContext());
        logger.log(EntityLogger.EVENT_ADDNEW, "新增客户");
        return customer.getUuid();
    }

    @Override
    public void offline(String uuid, long version)
            throws IllegalArgumentException, WMSException {
        Customer dbCustomer = customerDao.get(uuid);
        ValidateResult updateResult = customerRemoveAndRecoverValidateHandler
                .putAttribute(CustomerRemoveAndRecoverValidateHandler.KEY_OPERATOR_CUSTOMER,
                        dbCustomer)
                .validate(dbCustomer);
        checkValidateResult(updateResult);
        PersistenceUtils.checkVersion(version, dbCustomer, "客户", uuid);
        dbCustomer.setState(CustomerState.offline);
        dbCustomer.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
        customerDao.update(dbCustomer);

        logger.injectContext(this, uuid, Customer.class.getName(),
                ApplicationContextUtil.getOperateContext());
        logger.log(EntityLogger.EVENT_DELETE, "禁用客户");
    }

    @Override
    public void update(Customer customer) throws IllegalArgumentException, WMSException {
        Customer dbCustomer = customerDao.getByCode(customer == null ? null : customer.getCode());
        ValidateResult updateResult = customerUpdateValidateHandler
                .putAttribute(CustomerUpdateValidateHandler.KEY_CODEEXISTS_CUSTOMER, dbCustomer)
                .validate(customer);
        checkValidateResult(updateResult);
        PersistenceUtils.checkVersion(customer.getVersion(), dbCustomer, "客户", customer.getUuid());
        customer.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
        customerDao.update(customer);

        logger.injectContext(this, customer.getUuid(), Customer.class.getName(),
                ApplicationContextUtil.getOperateContext());
        logger.log(EntityLogger.EVENT_MODIFY, "修改客户");
    }

    @Override
    public Customer getByCode(String code) {
        if (StringUtil.isNullOrBlank(code))
            return null;
        return customerDao.getByCode(code);
    }

    @Override
    public PageQueryResult<Customer> query(PageQueryDefinition definition) {
        Assert.assertArgumentNotNull(definition, "definition");
        definition.setCompanyUuid(ApplicationContextUtil.getParentCompanyUuid());
        PageQueryResult<Customer> pgr = new PageQueryResult<Customer>();
        List<Customer> list = customerDao.query(definition);
        PageQueryUtil.assignPageInfo(pgr, definition);
        pgr.setRecords(list);
        return pgr;
    }

    @Override
    public void online(String uuid, long version) throws IllegalArgumentException, WMSException {
        Customer dbCustomer = customerDao.get(uuid);
        ValidateResult updateResult = customerRemoveAndRecoverValidateHandler
                .putAttribute(CustomerRemoveAndRecoverValidateHandler.KEY_OPERATOR_CUSTOMER,
                        dbCustomer)
                .validate(dbCustomer);
        checkValidateResult(updateResult);
        PersistenceUtils.checkVersion(version, dbCustomer, "客户", uuid);
        dbCustomer.setState(CustomerState.online);
        dbCustomer.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
        customerDao.update(dbCustomer);

        logger.injectContext(this, uuid, Customer.class.getName(),
                ApplicationContextUtil.getOperateContext());
        logger.log(EntityLogger.EVENT_MODIFY, "启用客户");
    }

    @Override
    public Customer get(String uuid) {
        if (StringUtil.isNullOrBlank(uuid))
            return null;
        return customerDao.get(uuid);
    }

    @Override
    public List<UCN> queryAllCustomer() {

        return customerDao.queryAllCustomer();
    }

}
