/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	SupplierReturnBillController.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月21日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.web.rtn.returnsupplier;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.rtn.returnsupplier.ReturnSupplierBill;
import com.hd123.sardine.wms.api.rtn.returnsupplier.ReturnSupplierBillService;
import com.hd123.sardine.wms.api.tms.shipbill.OperateMethod;
import com.hd123.sardine.wms.api.tms.shipbill.ShipBillService;
import com.hd123.sardine.wms.api.tms.shipbill.ShipBillState;
import com.hd123.sardine.wms.common.exception.NotLoginInfoException;
import com.hd123.sardine.wms.common.http.ErrorRespObject;
import com.hd123.sardine.wms.common.http.RespObject;
import com.hd123.sardine.wms.common.http.RespStatus;
import com.hd123.sardine.wms.common.query.OrderDir;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.web.base.BaseController;

/**
 * 供应商退货管理控制层
 * 
 * @author fanqingqing
 *
 */
@RestController
@RequestMapping("/rtn/returnsupplier")
public class ReturnSupplierBillController extends BaseController {
    @Autowired
    private ReturnSupplierBillService service;

    private final String DEFAULTOPTION = "all";

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public @ResponseBody RespObject query(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false,
                    defaultValue = "asc") String sortDirection,
            @RequestParam(value = "token", required = false) String token,
            @RequestParam(value = "billNumber", required = false) String billNumber,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "rtnSupplierNtcBillNumber",
                    required = false) String rtnSupplierNtcBillNumber,
            @RequestParam(value = "supplierCode", required = false) String supplierCode,
            @RequestParam(value = "supplierName", required = false) String supplierName,
            @RequestParam(value = "wrhUuid", required = false) String wrhUuid,
            @RequestParam(value = "operateMethod", required = false) String operateMethod,
            @RequestParam(value = "returnerCode", required = false) String returnerCode,
            @RequestParam(value = "returnerName", required = false) String returnerName,
            @RequestParam(value = "articleCode", required = false) String articleCode,
            @RequestParam(value = "binCode", required = false) String binCode,
            @RequestParam(value = "returnSupplierDateLessThanOrEqualStr",
                    required = false) String returnSupplierDateLessThanOrEqualStr,
            @RequestParam(value = "returnSupplierDateGreaterThanOrEqualStr",
                    required = false) String returnSupplierDateGreaterThanOrEqualStr,
            @RequestParam(value = "containerBarcode", required = false) String containerBarcode) {
        RespObject resp = new RespObject();
        try {
            PageQueryDefinition definition = new PageQueryDefinition();
            definition.setPage(page);
            definition.setPageSize(pageSize);
            definition.setSortField(StringUtil.isNullOrBlank(sort)
                    ? ReturnSupplierBillService.QUERY_BILLNUMBER_LIKE : sort);
            definition.setOrderDir(OrderDir.valueOf(sortDirection));
            definition.put(ReturnSupplierBillService.QUERY_BILLNUMBER_LIKE, billNumber);
            definition.put(ReturnSupplierBillService.QUERY_STATE_EQUALS,
                    (StringUtil.isNullOrBlank(state) || DEFAULTOPTION.equals(state)) ? null
                            : ShipBillState.valueOf(state));
            definition.put(ReturnSupplierBillService.QUERY_RTNSUPPLIERNTCBILLNUMBER_LIKE,
                    rtnSupplierNtcBillNumber);
            definition.put(ReturnSupplierBillService.QUERY_SUPPLIERCODE_LIKE, supplierCode);
            definition.put(ReturnSupplierBillService.QUERY_SUPPLIERNAME_LIKE, supplierName);
            definition.put(ReturnSupplierBillService.QUERY_WRHUUID_EQUALS,
                    DEFAULTOPTION.equals(wrhUuid) ? null : wrhUuid);
            definition
                    .put(ShipBillService.QUERY_OPERATEMETHOD_EQUALS,
                            (StringUtil.isNullOrBlank(operateMethod)
                                    || DEFAULTOPTION.equals(operateMethod)) ? null
                                            : OperateMethod.valueOf(operateMethod));
            definition.put(ReturnSupplierBillService.QUERY_RETURNERCODE_LIKE, returnerCode);
            definition.put(ReturnSupplierBillService.QUERY_RETURNERNAME_LIKE, returnerName);
            definition.put(ReturnSupplierBillService.QUERY_ARTICLECODE_CONTAINS, articleCode);
            definition.put(ReturnSupplierBillService.QUERY_BINCODE_CONTAINS, binCode);
            definition.put(ReturnSupplierBillService.QUERY_CONTAINERBARCODE_CONTAINS,
                    containerBarcode);
            if (StringUtil.isNullOrBlank(returnSupplierDateLessThanOrEqualStr) == false) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date returnSupplierDateLessThanOrEqual = formatter
                        .parse(returnSupplierDateLessThanOrEqualStr);
                definition.put(ReturnSupplierBillService.QUERY_RETURNSUPPLIERDATE_LESSTHANOREQUAL,
                        returnSupplierDateLessThanOrEqual);
            }
            if (StringUtil.isNullOrBlank(returnSupplierDateGreaterThanOrEqualStr) == false) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date returnSupplierDateGreaterThanOrEqual = formatter
                        .parse(returnSupplierDateGreaterThanOrEqualStr);
                definition.put(
                        ReturnSupplierBillService.QUERY_RETURNSUPPLIERDATE_GREATERTHANOREQUAL,
                        returnSupplierDateGreaterThanOrEqual);
            }

            PageQueryResult<ReturnSupplierBill> result = service.query(definition);
            resp.setObj(result);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (NotLoginInfoException e) {
            return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
        } catch (Exception e) {
            return new ErrorRespObject("分页获取供应商退货单失败：" + e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public @ResponseBody RespObject get(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "uuid", required = true) String uuid) {
        RespObject resp = new RespObject();
        try {
            ReturnSupplierBill bill = service.getByUuid(uuid);
            resp.setObj(bill);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (NotLoginInfoException e) {
            return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
        } catch (Exception e) {
            return new ErrorRespObject("获取供应商退货单失败：" + e.getMessage());
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
            service.finish(uuid, version);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (NotLoginInfoException e) {
            return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
        } catch (Exception e) {
            return new ErrorRespObject("完成供应商退货单失败：" + e.getMessage());
        }
        return resp;
    }

}
