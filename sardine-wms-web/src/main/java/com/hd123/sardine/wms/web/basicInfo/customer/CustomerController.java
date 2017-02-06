/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	CustomerController.java
 * 模块说明：	
 * 修改历史：
 * 2017年1月13日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.web.basicInfo.customer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.customer.Customer;
import com.hd123.sardine.wms.api.basicInfo.customer.CustomerService;
import com.hd123.sardine.wms.api.basicInfo.customer.CustomerState;
import com.hd123.sardine.wms.api.basicInfo.customer.CustomerType;
import com.hd123.sardine.wms.common.entity.OperateInfo;
import com.hd123.sardine.wms.common.entity.Operator;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.http.ErrorRespObject;
import com.hd123.sardine.wms.common.http.RespObject;
import com.hd123.sardine.wms.common.http.RespStatus;
import com.hd123.sardine.wms.common.query.OrderDir;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.utils.DateHelper;
import com.hd123.sardine.wms.web.BaseController;

/**
 * @author yangwenzhu
 *
 */
@RestController
@RequestMapping("/basicinfo/customer")
public class CustomerController extends BaseController {
    @Autowired
    private CustomerService customerService;
    List<Customer> list = new ArrayList<Customer>();
    {
        Customer customer = new Customer();
        customer.setUuid("001");
        customer.setAddress("杭州");
        customer.setCode("code01");
        customer.setCompanyUuid("com01");
        customer.setCreateInfo(new OperateInfo(DateHelper.strToDateTime("2017-01-21 10:12:12"),
                new Operator("YY", "yy", "yangze")));
        customer.setLastModifyInfo(new OperateInfo(DateHelper.strToDateTime("2017-01-21 10:12:12"),
                new Operator("YY", "yy", "yangze")));
        customer.setName("商店");
        customer.setPhone("1111");
        customer.setRemark("标记");
        customer.setState(CustomerState.normal);
        customer.setToken("001");
        customer.setType(CustomerType.shop);
        customer.setVersion(0);

        Customer customer1 = new Customer();
        customer1.setUuid("002");
        customer1.setAddress("杭州");
        customer1.setCode("code02");
        customer1.setCompanyUuid("com02");
        customer1.setCreateInfo(new OperateInfo(DateHelper.strToDateTime("2017-01-21 10:12:12"),
                new Operator("YY", "yy", "yangze")));
        customer1.setLastModifyInfo(new OperateInfo(DateHelper.strToDateTime("2017-01-21 10:12:12"),
                new Operator("YY", "yy", "yangze")));
        customer1.setName("商店");
        customer1.setPhone("1111");
        customer1.setRemark("标记");
        customer1.setState(CustomerState.normal);
        customer1.setToken("002");
        customer1.setType(CustomerType.store);
        customer1.setVersion(0);
        list.add(customer1);
        list.add(customer);
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public @ResponseBody RespObject get(@RequestParam(value = "customerUuid") String customerUuid) {
        RespObject resp = new RespObject();
        try {
            Customer customer = customerService.get(customerUuid);
            // Customer customer = new Customer();
            // customer.setUuid("001");
            // customer.setAddress("杭州");
            // customer.setCode("code01");
            // customer.setCompanyUuid("com01");
            // customer.setCreateInfo(new
            // OperateInfo(DateHelper.strToDateTime("2017-01-21 10:12:12"),
            // new Operator("YY", "yy", "yangze")));
            // customer.setLastModifyInfo(
            // new OperateInfo(DateHelper.strToDateTime("2017-01-21 10:12:12"),
            // new Operator("YY", "yy", "yangze")));
            // customer.setName("商店");
            // customer.setPhone("1111");
            // customer.setRemark("标记");
            // customer.setState(CustomerState.normal);
            // customer.setToken("001");
            // customer.setType(CustomerType.shop);
            // customer.setVersion(0);
            resp.setObj(customer);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("查询客户失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/getbycode", method = RequestMethod.GET)
    public @ResponseBody RespObject getByCode(
            @RequestParam(value = "customerCode") String customerCode) {
        RespObject resp = new RespObject();
        try {
            Customer customer = customerService.getByCode(customerCode);
            resp.setObj(customer);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("查询客户失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public @ResponseBody RespObject query(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false,
                    defaultValue = "asc") String sortDirection,
            @RequestParam(value = "token", required = false) String token,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "state", required = false) String state) {
        RespObject resp = new RespObject();
        try {
            PageQueryDefinition definition = new PageQueryDefinition();
            definition.setPage(page);
            definition.setPageSize(pageSize);
            definition.setSortField(sort);
            definition.setOrderDir(OrderDir.valueOf(sortDirection));
            definition.put(CustomerService.QUERY_CODE_FIELD, code);
            definition.put(CustomerService.QUERY_NAME_FIELD, name);
            definition.put(CustomerService.QUERY_STATE_FIELD,
                    StringUtil.isNullOrBlank(state) ? null : CustomerState.valueOf(state));
            // definition.setCompanyUuid(getLoginCompany(token).getUuid());
            definition.setCompanyUuid("com01");
            PageQueryResult<Customer> result = customerService.query(definition);
            // PageQueryResult<Customer> result = new
            // PageQueryResult<Customer>();
            // PageQueryUtil.assignPageInfo(result, definition);
            // result.setRecords(list);
            // result.setRecordCount(list.size());
            resp.setObj(result);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("分页查询客户失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public @ResponseBody RespObject insert(@RequestBody Customer customer) {
        RespObject resp = new RespObject();
        try {
            String uuid = customerService.insert(customer, getOperateContext(customer.getToken()));
            resp.setObj(uuid);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (IllegalArgumentException e) {
            return new ErrorRespObject("参数异常", e.getMessage());
        } catch (WMSException e) {
            return new ErrorRespObject("新增客户失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public @ResponseBody RespObject update(@RequestBody Customer customer) {
        RespObject resp = new RespObject();
        try {
            customerService.update(customer, getOperateContext(customer.getToken()));
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (IllegalArgumentException e) {
            return new ErrorRespObject("参数异常", e.getMessage());
        } catch (WMSException e) {
            return new ErrorRespObject("修改客户失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/remove", method = RequestMethod.PUT)
    public @ResponseBody RespObject remove(
            @RequestParam(value = "uuid", required = true) String uuid,
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "version", required = true) long version) {
        RespObject resp = new RespObject();
        try {
            customerService.removeState(uuid, version, getOperateContext(token));
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (IllegalArgumentException e) {
            return new ErrorRespObject("参数异常", e.getMessage());
        } catch (WMSException e) {
            return new ErrorRespObject("删除客户失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/recover", method = RequestMethod.PUT)
    public @ResponseBody RespObject recover(
            @RequestParam(value = "uuid", required = true) String uuid,
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "version", required = true) long version) {
        RespObject resp = new RespObject();
        try {
            customerService.recover(uuid, version, getOperateContext(token));
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (IllegalArgumentException e) {
            return new ErrorRespObject("参数异常", e.getMessage());
        } catch (WMSException e) {
            return new ErrorRespObject("恢复客户失败", e.getMessage());
        }
        return resp;
    }

}
