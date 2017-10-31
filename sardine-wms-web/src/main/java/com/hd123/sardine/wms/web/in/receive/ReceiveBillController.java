/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	ReceiveBillController.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月4日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.web.in.receive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.in.receive.ReceiveBill;
import com.hd123.sardine.wms.api.in.receive.ReceiveBillService;
import com.hd123.sardine.wms.api.in.receive.ReceiveBillState;
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
@RequestMapping("/in/receive")
public class ReceiveBillController extends BaseController {

  @Autowired
  private ReceiveBillService service;

  @RequestMapping(value = "/query", method = RequestMethod.GET)
  public RespObject query(
      @RequestParam(value = "page", required = false, defaultValue = "1") int page,
      @RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize,
      @RequestParam(value = "sort", required = false) String sort,
      @RequestParam(value = "order", required = false, defaultValue = "desc") String sortDirection,
      @RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "billNumber", required = false) String billNumber,
      @RequestParam(value = "supplier", required = false) String supplier,
      @RequestParam(value = "wrh", required = false) String wrh,
      @RequestParam(value = "orderBill", required = false) String orderBill,
      @RequestParam(value = "state", required = false) String state,
      @RequestParam(value = "articleCode", required = false) String articleCode,
      @RequestParam(value = "receiverCode", required = false) String receiverCode) {
    RespObject resp = new RespObject();
    try {
      PageQueryDefinition definition = new PageQueryDefinition();
      definition.setPage(page);
      definition.setPageSize(pageSize);
      definition.setSortField(
          StringUtil.isNullOrBlank(sort) ? ReceiveBillService.FIELD_ORDER_BILLNO : sort);
      definition.setOrderDir(OrderDir.valueOf(sortDirection));
      definition.put(ReceiveBillService.QUERY_BILLNO_FIELD, billNumber);
      definition.put(ReceiveBillService.QUERY_ORDERBILL_FIELD, orderBill);
      definition.put(ReceiveBillService.QUERY_SUPPLIER_FIELD, supplier);
      definition.put(ReceiveBillService.QUERY_WRH_FIELD, wrh);
      definition.put(ReceiveBillService.QUERY_STATE_FIELD,
          StringUtil.isNullOrBlank(state) ? null : ReceiveBillState.valueOf(state));
      definition.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      definition.put(ReceiveBillService.QUERY_ARTICLE_CODE_FIELD, articleCode);
      definition.put(ReceiveBillService.QUERY_RECEIVER_CODE_FIELD, receiverCode);
      PageQueryResult<ReceiveBill> result = service.query(definition);
      resp.setObj(result);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("分页查询失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/insert", method = RequestMethod.POST)
  public RespObject insert(@RequestParam(value = "token", required = true) String token,
      @RequestBody ReceiveBill receiveBill) {
    RespObject resp = new RespObject();
    try {
      receiveBill.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      String uuid = service.insert(receiveBill);
      resp.setObj(uuid);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("新增收货单失败：" + e.getMessage());
    }
    return resp;

  }

  @RequestMapping(value = "/getByBillNo", method = RequestMethod.GET)
  public RespObject getByBillNo(
      @RequestParam(value = "billNumber", required = true) String billNumber,
      @RequestParam(value = "token", required = true) String token) {
    RespObject resp = new RespObject();
    try {
      ReceiveBill receiveBill = service.getByBillNo(billNumber);
      resp.setObj(receiveBill);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("获取收货单失败：" + e.getMessage());
    }
    return resp;

  }

  @RequestMapping(value = "/remove", method = RequestMethod.DELETE)
  public RespObject remove(@RequestParam(value = "uuid", required = true) String uuid,
      @RequestParam(value = "version", required = true) long version,
      @RequestParam(value = "token", required = true) String token) {
    RespObject resp = new RespObject();
    try {
      service.remove(uuid, version);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("删除收货单失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/audit", method = RequestMethod.PUT)
  public RespObject audit(@RequestParam(value = "uuid", required = true) String uuid,
      @RequestParam(value = "version", required = true) long version,
      @RequestParam(value = "token", required = true) String token) {
    RespObject resp = new RespObject();
    try {
      service.audit(uuid, version);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      e.printStackTrace();
      return new ErrorRespObject("审核收货单失败：" + e.getMessage());
    }
    return resp;

  }

  @RequestMapping(value = "/update", method = RequestMethod.PUT)
  public RespObject update(@RequestParam(value = "token", required = true) String token,
      @RequestBody ReceiveBill receiveBill) {
    RespObject resp = new RespObject();
    try {
      service.update(receiveBill);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("修改收货单失败：" + e.getMessage());
    }
    return resp;

  }
}
