/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	ReturnBillController.java
 * 模块说明：	
 * 修改历史：
 * 2017年7月7日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.web.rtn.customerreturn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.rtn.customerreturn.ReturnBill;
import com.hd123.sardine.wms.api.rtn.customerreturn.ReturnBillService;
import com.hd123.sardine.wms.api.rtn.customerreturn.ReturnBillState;
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
@RequestMapping("/rtn/customerrtn")
public class ReturnBillController extends BaseController {

  @Autowired
  private ReturnBillService service;

  @RequestMapping(value = "/query", method = RequestMethod.GET)
  public @ResponseBody RespObject query(
      @RequestParam(value = "page", required = false, defaultValue = "1") int page,
      @RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize,
      @RequestParam(value = "sort", required = false) String sort,
      @RequestParam(value = "order", required = false, defaultValue = "asc") String sortDirection,
      @RequestParam(value = "token", required = false) String token,
      @RequestParam(value = "billNumber", required = false) String billNumber,
      @RequestParam(value = "state", required = false) String state,
      @RequestParam(value = "customerCode", required = false) String customerCode,
      @RequestParam(value = "customerName", required = false) String customerName,
      @RequestParam(value = "returnNtcBillNumber", required = false) String returnNtcBillNumber,
      @RequestParam(value = "wrhCode", required = false) String wrh,
      @RequestParam(value = "returnorCode", required = false) String returnorCode,
      @RequestParam(value = "returnorName", required = false) String returnorName,
      @RequestParam(value = "articleName", required = false) String articleName,
      @RequestParam(value = "articleCode", required = false) String articleCode,
      @RequestParam(value = "supplierName", required = false) String supplierName,
      @RequestParam(value = "supplierCode", required = false) String supplierCode,
      @RequestParam(value = "containerBarcode", required = false) String containerBarcode) {
    RespObject resp = new RespObject();
    try {
      PageQueryDefinition definition = new PageQueryDefinition();
      definition.setPage(page);
      definition.setPageSize(pageSize);
      definition.setSortField(
          StringUtil.isNullOrBlank(sort) ? ReturnBillService.QUERY_BILLNUMBER_LIKE : sort);
      definition.setOrderDir(OrderDir.valueOf(sortDirection));
      definition.put(ReturnBillService.QUERY_BILLNUMBER_LIKE, billNumber);
      definition.put(ReturnBillService.QUERY_STATE_EQUALS,
          StringUtil.isNullOrBlank(state) ? null : ReturnBillState.valueOf(state));
      definition.put(ReturnBillService.QUERY_CUSTOMER_CODE_LIKE, customerCode);
      definition.put(ReturnBillService.QUERY_RETURNNTCBILL_LIKE, returnNtcBillNumber);
      definition.put(ReturnBillService.QUERY_CUSTOMER_NAME_LIKE, customerName);
      definition.put(ReturnBillService.QUERY_WRH_EQUALS, wrh);
      definition.put(ReturnBillService.QUERY_RETURNOR_CODE_LIKE, returnorCode);
      definition.put(ReturnBillService.QUERY_RETURNOR_NAME_LIKE, returnorName);
      definition.put(ReturnBillService.QUERY_ARTICLE_CODE_CONTAINS, articleCode);
      definition.put(ReturnBillService.QUERY_ARTICLE_NAME_CONTAINS, articleName);
      definition.put(ReturnBillService.QUERY_SUPPLIER_CODE_CONTAINS, supplierCode);
      definition.put(ReturnBillService.QUERY_SUPPLIER_NAMW_CONTAINS, supplierName);
      definition.put(ReturnBillService.QUERY_CONTAINERBARCODE_CONTAINS, containerBarcode);
      PageQueryResult<ReturnBill> result = service.query(definition);
      resp.setObj(result);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (NotLoginInfoException e) {
      return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
    } catch (Exception e) {
      return new ErrorRespObject("分页获取退仓单失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/insert", method = RequestMethod.POST)
  public @ResponseBody RespObject insert(
      @RequestParam(value = "token", required = true) String token, @RequestBody ReturnBill bill) {
    RespObject resp = new RespObject();
    try {
      bill.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      String uuid = service.saveNew(bill);
      resp.setObj(uuid);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (NotLoginInfoException e) {
      return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
    } catch (Exception e) {
      return new ErrorRespObject("新增退仓单失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/getRtnBill", method = RequestMethod.GET)
  public @ResponseBody RespObject getRtnNtcBill(
      @RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "uuid", required = true) String uuid) {
    RespObject resp = new RespObject();
    try {
      ReturnBill bill = service.get(uuid);
      resp.setObj(bill);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (NotLoginInfoException e) {
      return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
    } catch (Exception e) {
      return new ErrorRespObject("获取退仓单失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/update", method = RequestMethod.PUT)
  public @ResponseBody RespObject update(
      @RequestParam(value = "token", required = true) String token, @RequestBody ReturnBill bill) {
    RespObject resp = new RespObject();
    try {
      service.saveModify(bill);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (NotLoginInfoException e) {
      return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
    } catch (Exception e) {
      return new ErrorRespObject("修改退仓单失败：" + e.getMessage());
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
      resp.setObj(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (NotLoginInfoException e) {
      return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
    } catch (Exception e) {
      return new ErrorRespObject("删除退仓单失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/audit", method = RequestMethod.PUT)
  public @ResponseBody RespObject finish(
      @RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "uuid", required = true) String uuid,
      @RequestParam(value = "version", required = true) long version) {
    RespObject resp = new RespObject();
    try {
      service.audit(uuid, version);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (NotLoginInfoException e) {
      return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
    } catch (Exception e) {
      return new ErrorRespObject("审核退仓单失败：" + e.getMessage());
    }
    return resp;
  }
}
