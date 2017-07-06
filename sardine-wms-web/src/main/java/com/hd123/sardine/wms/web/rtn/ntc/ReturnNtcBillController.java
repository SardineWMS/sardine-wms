/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	ReturnNtcBillController.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月29日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.web.rtn.ntc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.rtn.ntc.ReturnNtcBill;
import com.hd123.sardine.wms.api.rtn.ntc.ReturnNtcBillService;
import com.hd123.sardine.wms.api.rtn.ntc.ReturnNtcBillState;
import com.hd123.sardine.wms.common.exception.NotLoginInfoException;
import com.hd123.sardine.wms.common.http.ErrorRespObject;
import com.hd123.sardine.wms.common.http.RespObject;
import com.hd123.sardine.wms.common.http.RespStatus;
import com.hd123.sardine.wms.common.query.OrderDir;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.web.base.BaseController;

/**
 * @author yangwenzhu
 *
 */
@RestController
@RequestMapping("/rtn/ntc")
public class ReturnNtcBillController extends BaseController {
    @Autowired
    private ReturnNtcBillService service;

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public @ResponseBody RespObject insert(
            @RequestParam(value = "token", required = true) String token,
            @RequestBody ReturnNtcBill bill) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            ApplicationContextUtil.setOperateContext(getOperateContext(token));
            bill.setCompanyUuid(getLoginCompany(token).getUuid());
            String uuid = service.saveNew(bill);
            resp.setObj(uuid);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (NotLoginInfoException e) {
            return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
        } catch (Exception e) {
            return new ErrorRespObject("新增退仓通知单失败：" + e.getMessage());
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
            @RequestParam(value = "billNumber", required = false) String billNumber,
            @RequestParam(value = "customerCode", required = false) String customerCode,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "customerName", required = false) String customerName,
            @RequestParam(value = "sourceBillNumber", required = false) String sourceBillNumber,
            @RequestParam(value = "wrhCode", required = false) String wrhCode,
            @RequestParam(value = "dateLessThan", required = false) String dateLessThan,
            @RequestParam(value = "dateMoreThan", required = false) String dateMoreThan,
            @RequestParam(value = "articleName", required = false) String articleName,
            @RequestParam(value = "articleCode", required = false) String articleCode,
            @RequestParam(value = "supplierName", required = false) String supplierName,
            @RequestParam(value = "supplierCode", required = false) String supplierCode) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            ApplicationContextUtil.setOperateContext(getOperateContext(token));
            PageQueryDefinition definition = new PageQueryDefinition();
            definition.setPage(page);
            definition.setPageSize(pageSize);
            definition.setSortField(StringUtil.isNullOrBlank(sort)
                    ? ReturnNtcBillService.QUERY_BILLNUMBER_LIKE : sort);
            definition.setOrderDir(OrderDir.valueOf(sortDirection));
            definition.put(ReturnNtcBillService.QUERY_ARTICLECODE_EQUALS, articleCode);
            definition.put(ReturnNtcBillService.QUERY_ARTICLENAME_EQUALS, articleName);
            definition.put(ReturnNtcBillService.QUERY_STATE_EQUALS,
                    StringUtil.isNullOrBlank(state) ? null : ReturnNtcBillState.valueOf(state));
            definition.put(ReturnNtcBillService.QUERY_BILLNUMBER_LIKE, billNumber);
            definition.put(ReturnNtcBillService.QUERY_CUSTOMERCODE_EQUALS, customerCode);
            definition.put(ReturnNtcBillService.QUERY_CUSTOMERNAME_EQUALS, customerName);
            definition.put(ReturnNtcBillService.QUERY_RETURNDATE_LESSTHAN,
                    StringUtil.isNullOrBlank(dateLessThan) ? null
                            : StringUtil.toDate(dateLessThan, "yyyy-MM-dd"));
            definition.put(ReturnNtcBillService.QUERY_RETURNDATE_MORETHAN,
                    StringUtil.isNullOrBlank(dateMoreThan) ? null
                            : StringUtil.toDate(dateMoreThan, "yyyy-MM-dd"));
            definition.put(ReturnNtcBillService.QUERY_SOURCEBILLNUMBER_LIKE, sourceBillNumber);
            definition.put(ReturnNtcBillService.QUERY_SUPPLIERCODE_EQUALS, supplierCode);
            definition.put(ReturnNtcBillService.QUERY_SUPPLIERNAME_EQUALS, supplierName);
            definition.put(ReturnNtcBillService.QUERY_WRH_EQUALS, wrhCode);
            PageQueryResult<ReturnNtcBill> result = service.query(definition);
            resp.setObj(result);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (NotLoginInfoException e) {
            return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
        } catch (Exception e) {
            return new ErrorRespObject("分页获取退仓通知单失败：" + e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/getRtnNtcBill", method = RequestMethod.GET)
    public @ResponseBody RespObject getRtnNtcBill(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "uuid", required = true) String uuid) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            ApplicationContextUtil.setOperateContext(getOperateContext(token));
            ReturnNtcBill bill = service.get(uuid);
            resp.setObj(bill);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (NotLoginInfoException e) {
            return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
        } catch (Exception e) {
            return new ErrorRespObject("获取退仓通知单失败：" + e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public @ResponseBody RespObject update(
            @RequestParam(value = "token", required = true) String token,
            @RequestBody ReturnNtcBill bill) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            ApplicationContextUtil.setOperateContext(getOperateContext(token));
            service.saveModify(bill);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (NotLoginInfoException e) {
            return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
        } catch (Exception e) {
            return new ErrorRespObject("修改退仓通知单失败：" + e.getMessage());
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
            ApplicationContextUtil.setOperateContext(getOperateContext(token));
            service.remove(uuid, version);
            resp.setObj(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (NotLoginInfoException e) {
            return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
        } catch (Exception e) {
            return new ErrorRespObject("删除退仓通知单失败：" + e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/finish", method = RequestMethod.PUT)
    public @ResponseBody RespObject finish(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "uuid", required = true) String uuid,
            @RequestParam(value = "version", required = true) long version) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            ApplicationContextUtil.setOperateContext(getOperateContext(token));
            service.finish(uuid, version);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (NotLoginInfoException e) {
            return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
        } catch (Exception e) {
            return new ErrorRespObject("完成退仓通知单失败：" + e.getMessage());
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
        } catch (NotLoginInfoException e) {
            return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
        } catch (Exception e) {
            return new ErrorRespObject("作废退仓通知单失败：" + e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/genRtnBill", method = RequestMethod.PUT)
    public @ResponseBody RespObject genRtnBill(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "uuid", required = true) String uuid,
            @RequestParam(value = "version", required = true) long version) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            ApplicationContextUtil.setOperateContext(getOperateContext(token));
            service.genRtnBill(uuid, version);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (NotLoginInfoException e) {
            return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
        } catch (Exception e) {
            return new ErrorRespObject("生成退仓单失败：" + e.getMessage());
        }
        return resp;
    }

}
