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

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.customer.Customer;
import com.hd123.sardine.wms.api.basicInfo.customer.CustomerService;
import com.hd123.sardine.wms.api.basicInfo.customer.CustomerState;
import com.hd123.sardine.wms.common.entity.OperateContext;
import com.hd123.sardine.wms.common.entity.OperateInfo;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.common.validator.ValidateHandler;
import com.hd123.sardine.wms.common.validator.ValidateResult;
import com.hd123.sardine.wms.dao.basicInfo.customer.CustomerDao;
import com.hd123.sardine.wms.service.basicInfo.customer.validator.CustomerInsertValidateHandler;
import com.hd123.sardine.wms.service.basicInfo.customer.validator.CustomerRemoveAndRecoverValidateHandler;
import com.hd123.sardine.wms.service.basicInfo.customer.validator.CustomerUpdateValidateHandler;
import com.hd123.sardine.wms.service.ia.BaseWMSService;

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
    private ValidateHandler<OperateContext> operateContextValidateHandler;

    @Override
    public String insert(Customer customer, OperateContext operCtx)
            throws IllegalArgumentException, WMSException {
        Customer dbCustomer = customerDao.getByCode(customer == null ? null : customer.getCode());

        ValidateResult insertResult = customerInsertValidateHandler
                .putAttribute(CustomerInsertValidateHandler.KEY_CODEEXISTS_CUSTOMER, dbCustomer)
                .validate(customer);
        checkValidateResult(insertResult);
        ValidateResult operCtxResult = operateContextValidateHandler.validate(operCtx);
        checkValidateResult(operCtxResult);

        customer.setUuid(UUIDGenerator.genUUID());
        customer.setCreateInfo(OperateInfo.newInstance(operCtx));
        customer.setLastModifyInfo(OperateInfo.newInstance(operCtx));
        customer.setState(CustomerState.normal);
        customer.setCompanyUuid("001");// TODO 先写假的
        customerDao.insert(customer);
        return customer.getUuid();
    }

    @Override
    public void removeState(String uuid, long version, OperateContext operCtx)
            throws IllegalArgumentException, WMSException {
        Customer dbCustomer = customerDao.get(uuid);
        ValidateResult updateResult = customerRemoveAndRecoverValidateHandler
                .putAttribute(CustomerRemoveAndRecoverValidateHandler.KEY_OPERATOR_CUSTOMER,
                        dbCustomer)
                .validate(dbCustomer);
        checkValidateResult(updateResult);
        PersistenceUtils.checkVersion(version, dbCustomer, "客户", uuid);
        ValidateResult operCtxResult = operateContextValidateHandler.validate(operCtx);
        checkValidateResult(operCtxResult);
        dbCustomer.setState(CustomerState.deleted);
        dbCustomer.setLastModifyInfo(OperateInfo.newInstance(operCtx));
        customerDao.update(dbCustomer);
    }

    @Override
    public void update(Customer customer, OperateContext operCtx)
            throws IllegalArgumentException, WMSException {
        Customer dbCustomer = customerDao.get(customer == null ? null : customer.getCode());
        ValidateResult updateResult = customerUpdateValidateHandler
                .putAttribute(CustomerUpdateValidateHandler.KEY_CODEEXISTS_CUSTOMER, dbCustomer)
                .validate(customer);
        checkValidateResult(updateResult);
        PersistenceUtils.checkVersion(customer.getVersion(), dbCustomer, "客户", customer.getUuid());
        ValidateResult operCtxResult = operateContextValidateHandler.validate(operCtx);
        checkValidateResult(operCtxResult);
        customer.setLastModifyInfo(OperateInfo.newInstance(operCtx));
        customerDao.update(customer);
    }

    @Override
    public Customer getByCode(String code) {
        if (StringUtil.isNullOrBlank(code))
            return null;
        return customerDao.getByCode(code);
    }

    @Override
    public PageQueryResult<Customer> query(PageQueryDefinition definition) {
        PageQueryResult<Customer> pgr = new PageQueryResult<Customer>();
        List<Customer> list = customerDao.query(definition);
        PageQueryUtil.assignPageInfo(pgr, definition);
        pgr.setRecords(list);
        return pgr;
    }

    @Override
    public void recover(String uuid, long version, OperateContext operCtx)
            throws IllegalArgumentException, WMSException {
        Customer dbCustomer = customerDao.get(uuid);
        ValidateResult updateResult = customerRemoveAndRecoverValidateHandler
                .putAttribute(CustomerRemoveAndRecoverValidateHandler.KEY_OPERATOR_CUSTOMER,
                        dbCustomer)
                .validate(dbCustomer);
        checkValidateResult(updateResult);
        PersistenceUtils.checkVersion(version, dbCustomer, "客户", uuid);
        ValidateResult operCtxResult = operateContextValidateHandler.validate(operCtx);
        checkValidateResult(operCtxResult);
        dbCustomer.setState(CustomerState.normal);
        dbCustomer.setLastModifyInfo(OperateInfo.newInstance(operCtx));
        customerDao.update(dbCustomer);
    }

    @Override
    public Customer get(String uuid) {
        if (StringUtil.isNullOrBlank(uuid))
            return null;
        return customerDao.get(uuid);
    }

}
