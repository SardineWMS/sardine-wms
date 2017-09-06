/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	RtnSupplierNtcBillController.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月16日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.web.rtn.rtnsupplierntc;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.rtn.ntc.ReturnNtcBillState;
import com.hd123.sardine.wms.api.rtn.rtnsupplierntc.RtnSupplierNtcBill;
import com.hd123.sardine.wms.api.rtn.rtnsupplierntc.RtnSupplierNtcBillService;
import com.hd123.sardine.wms.common.exception.NotLoginInfoException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.http.ErrorRespObject;
import com.hd123.sardine.wms.common.http.RespObject;
import com.hd123.sardine.wms.common.http.RespStatus;
import com.hd123.sardine.wms.common.query.OrderDir;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;

/**
 * @author yangwenzhu
 *
 */
@RestController
@RequestMapping("/rtn/rtnsupplierntc")
public class RtnSupplierNtcBillController {
  @Autowired
  private RtnSupplierNtcBillService service;

  @RequestMapping(value = "/query", method = RequestMethod.GET)
  public @ResponseBody RespObject query(
      @RequestParam(value = "page", required = false, defaultValue = "1") int page,
      @RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize,
      @RequestParam(value = "sort", required = false) String sort,
      @RequestParam(value = "order", required = false, defaultValue = "asc") String sortDirection,
      @RequestParam(value = "token", required = false) String token,
      @RequestParam(value = "billNumber", required = false) String billNumber,
      @RequestParam(value = "customerCode", required = false) String customerCode,
      @RequestParam(value = "state", required = false) String state,
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
      PageQueryDefinition definition = new PageQueryDefinition();
      definition.setPage(page);
      definition.setPageSize(pageSize);
      definition.setSortField(
          StringUtil.isNullOrBlank(sort) ? RtnSupplierNtcBillService.QUERY_BILLNUMBER_LIKE : sort);
      definition.setOrderDir(OrderDir.valueOf(sortDirection));
      definition.put(RtnSupplierNtcBillService.QUERY_ARTICLECODE_CONTAIN, articleCode);
      definition.put(RtnSupplierNtcBillService.QUERY_ARTICLENAME_CONTAIN, articleName);
      definition.put(RtnSupplierNtcBillService.QUERY_STATE_EQUALS,
          StringUtil.isNullOrBlank(state) ? null : ReturnNtcBillState.valueOf(state));
      definition.put(RtnSupplierNtcBillService.QUERY_BILLNUMBER_LIKE, billNumber);
      definition.put(RtnSupplierNtcBillService.QUERY_RTNDATE_LESSTHAN,
          StringUtil.isNullOrBlank(dateLessThan) ? null
              : StringUtil.toDate(dateLessThan, "yyyy-MM-dd"));
      definition.put(RtnSupplierNtcBillService.QUERY_RTNDATE_MORETHAN,
          StringUtil.isNullOrBlank(dateMoreThan) ? null
              : StringUtil.toDate(dateMoreThan, "yyyy-MM-dd"));
      definition.put(RtnSupplierNtcBillService.QUERY_SOURCEBILLNUMBER_LIKE, sourceBillNumber);
      definition.put(RtnSupplierNtcBillService.QUERY_SUPPLIERCODE_LIKE, supplierCode);
      definition.put(RtnSupplierNtcBillService.QUERY_SUPPLIERNAME_LIKE, supplierName);
      definition.put(RtnSupplierNtcBillService.QUERY_WRH_EQUALS, wrhCode);
      definition.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      PageQueryResult<RtnSupplierNtcBill> result = service.query(definition);
      resp.setObj(result);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (NotLoginInfoException e) {
      return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
    } catch (Exception e) {
      return new ErrorRespObject("分页获取供应商退货通知单失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/insert", method = RequestMethod.POST)
  public @ResponseBody RespObject insert(
      @RequestParam(value = "token", required = true) String token,
      @RequestBody RtnSupplierNtcBill bill) {
    RespObject resp = new RespObject();
    try {
      bill.setUnshelvedAmount(BigDecimal.ZERO);
      bill.setUnshelvedCaseQtyStr("0");
      bill.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      String uuid = service.saveNew(bill);
      resp.setObj(uuid);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("新建供应商退货通知单失败：" + e.getMessage());
    }
    return resp;

  }

  @RequestMapping(value = "/getrtnsupplierntcbill", method = RequestMethod.GET)
  public @ResponseBody RespObject getRtnSupplierNtcBill(
      @RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "uuid", required = true) String uuid) {
    RespObject resp = new RespObject();
    try {
      RtnSupplierNtcBill bill = service.get(uuid);
      resp.setObj(bill);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("获取供应商退货通知单失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/updatebill", method = RequestMethod.PUT)
  public @ResponseBody RespObject updateBill(
      @RequestParam(value = "token", required = true) String token,
      @RequestBody RtnSupplierNtcBill bill) {
    RespObject resp = new RespObject();
    try {
      service.saveModify(bill);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("修改供应商退货通知单失败：" + e.getMessage());
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
      service.abort(uuid, version);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("作废供应商退货通知单失败：" + e.getMessage());
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
      service.remove(uuid, version);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("删除供应商退货通知单失败：" + e.getMessage());
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
    } catch (Exception e) {
      return new ErrorRespObject("完成供应商退货通知单失败：" + e.getMessage());
    }
    return resp;

  }

  @RequestMapping(value = "/gentask", method = RequestMethod.PUT)
  public @ResponseBody RespObject genTask(
      @RequestParam(value = "uuid", required = true) String uuid,
      @RequestParam(value = "version", required = true) long version) {
    RespObject resp = new RespObject();
    try {
      service.genUnshelveTask(uuid, version);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("生成下架指令失败：" + e.getMessage());
    }
    return resp;

  }

}
