/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	AlcNtcBillController.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月2日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.web.out.alcntc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.out.alcntc.AlcNtcBill;
import com.hd123.sardine.wms.api.out.alcntc.AlcNtcBillService;
import com.hd123.sardine.wms.api.out.alcntc.AlcNtcBillState;
import com.hd123.sardine.wms.common.exception.NotLoginInfoException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.http.ErrorRespObject;
import com.hd123.sardine.wms.common.http.RespObject;
import com.hd123.sardine.wms.common.http.RespStatus;
import com.hd123.sardine.wms.common.query.OrderDir;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.web.BaseController;

/**
 * @author yangwenzhu
 *
 */
@RestController
@RequestMapping("/out/alcntc")
public class AlcNtcBillController extends BaseController {

    @Autowired
    private AlcNtcBillService service;

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public @ResponseBody RespObject insert(
            @RequestParam(value = "token", required = true) String token,
            @RequestBody AlcNtcBill alcNtcBill) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            ApplicationContextUtil.setOperateContext(getOperateContext(token));
            alcNtcBill.setCompany(getLoginCompany(token));
            String uuid = service.insert(alcNtcBill);
            resp.setObj(uuid);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (IllegalArgumentException e) {
            return new ErrorRespObject("参数异常", e.getMessage());
        } catch (WMSException e) {
            return new ErrorRespObject("新建配单失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public @ResponseBody RespObject get(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "uuid") String uuid) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            AlcNtcBill bill = service.get(uuid);
            resp.setObj(bill);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("获取损溢单失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public @ResponseBody RespObject update(
            @RequestParam(value = "token", required = true) String token,
            @RequestBody AlcNtcBill alcNtcBill) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            service.update(alcNtcBill);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (NotLoginInfoException e) {
            return new ErrorRespObject("登录信息为空，请重新登录", e.getMessage());
        } catch (WMSException e) {
            return new ErrorRespObject("修改损溢单失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public @ResponseBody RespObject query(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false,
                    defaultValue = "asc") String sortDirection,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "billNumber", required = false) String billNumber,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "customer", required = false) String customer,
            @RequestParam(value = "taskBillNumber", required = false) String taskBillNumber,
            @RequestParam(value = "sourceBillNumber", required = false) String sourceBillNumber,
            @RequestParam(value = "wrh", required = false) String wrh,
            @RequestParam(value = "deliverMode", required = false) String deliveryMode) {
        RespObject resp = new RespObject();
        try {
            PageQueryDefinition definition = new PageQueryDefinition();
            definition.setPage(page);
            definition.setPageSize(pageSize);
            definition.setSortField(sort);
            definition.setOrderDir(OrderDir.valueOf(sortDirection));
            definition.put("companyUuid", getLoginCompany(token).getUuid());
            definition.put(AlcNtcBillService.QUERY_BILLNUMBER_LIKE, billNumber);
            definition.put(AlcNtcBillService.QUERY_STATE_EQUALS,
                    StringUtil.isNullOrBlank(state) ? null : AlcNtcBillState.valueOf(state));
            definition.put(AlcNtcBillService.QUERY_CUSTOMERCODE_LIKE, customer);
            definition.put(AlcNtcBillService.QUERY_TASKBILLNUMBER_LIKE, taskBillNumber);
            definition.put(AlcNtcBillService.QUEYR_SOURCEBILLNUMBER_LIKE, sourceBillNumber);
            definition.put(AlcNtcBillService.QUERY_WRH_EQUALS, wrh);
            definition.put(AlcNtcBillService.QUERY_DELIVERYMODE, deliveryMode);

            PageQueryResult<AlcNtcBill> result = service.query(definition);
            resp.setObj(result);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("分页查询配单失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/remove", method = RequestMethod.DELETE)
    public @ResponseBody RespObject remove(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "uuid", required = true) String uuid,
            @RequestParam(value = "version", required = true) long version) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            service.remove(uuid, version);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (WMSException e) {
            return new ErrorRespObject("删除配单失败！", e.getMessage());
        }
        return resp;

    }

    @RequestMapping(value = "/audit", method = RequestMethod.PUT)
    public @ResponseBody RespObject audit(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "uuid", required = true) String uuid,
            @RequestParam(value = "version", required = true) long version) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            ApplicationContextUtil.setOperateContext(getOperateContext(token));
            service.finish(uuid, version);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorRespObject("完成配单失败！", e.getMessage());
        }
        return resp;

    }

    @RequestMapping(value = "/abort", method = RequestMethod.PUT)
    public @ResponseBody RespObject abort(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "uuid", required = true) String uuid,
            @RequestParam(value = "version", required = true) long version) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            ApplicationContextUtil.setOperateContext(getOperateContext(token));
            service.abort(uuid, version);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorRespObject("作废配单失败！", e.getMessage());
        }
        return resp;

    }
}
