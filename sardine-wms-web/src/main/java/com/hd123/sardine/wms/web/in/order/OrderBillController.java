/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	OrderBillController.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月21日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.web.in.order;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.bin.BinService;
import com.hd123.sardine.wms.api.basicInfo.bin.Wrh;
import com.hd123.sardine.wms.api.basicInfo.supplier.Supplier;
import com.hd123.sardine.wms.api.basicInfo.supplier.SupplierService;
import com.hd123.sardine.wms.api.in.order.OrderBill;
import com.hd123.sardine.wms.api.in.order.OrderBillItem;
import com.hd123.sardine.wms.api.in.order.OrderBillService;
import com.hd123.sardine.wms.api.in.order.OrderBillState;
import com.hd123.sardine.wms.api.in.receive.ReceiveBillService;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.http.ErrorRespObject;
import com.hd123.sardine.wms.common.http.RespObject;
import com.hd123.sardine.wms.common.http.RespStatus;
import com.hd123.sardine.wms.common.query.OrderDir;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.DateHelper;
import com.hd123.sardine.wms.common.utils.QpcHelper;
import com.hd123.sardine.wms.web.base.BaseController;

/**
 * 入库订单控制管理层
 * 
 * @author fanqingqing
 */

@RestController
@RequestMapping("/in/order")
public class OrderBillController extends BaseController {

  @Autowired
  private OrderBillService orderBillService;
  @Autowired
  private SupplierService supplierService;
  @Autowired
  private BinService binService;

  @RequestMapping(value = "/querybypage", method = RequestMethod.GET)
  public @ResponseBody RespObject query(
      @RequestParam(value = "page", required = false, defaultValue = "1") int page,
      @RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize,
      @RequestParam(value = "sort", required = false) String sort,
      @RequestParam(value = "order", required = false, defaultValue = "asc") String sortDirection,
      @RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "billNumber", required = false) String billNumber,
      @RequestParam(value = "supplierCode", required = false) String supplierCode,
      @RequestParam(value = "whrCode", required = false) String whrCode,
      @RequestParam(value = "state", required = false) String state,
      @RequestParam(value = "articleCode", required = false) String articleCode,
      @RequestParam(value = "sourceBillNumber", required = false) String sourceBill) {
    RespObject resp = new RespObject();
    try {
      PageQueryDefinition definition = new PageQueryDefinition();
      definition.setPage(page);
      definition.setPageSize(pageSize);
      definition.setSortField(sort);
      definition.setOrderDir(OrderDir.valueOf(sortDirection));
      definition.put(OrderBillService.QUERY_BILLNUMBER_LIKE, billNumber);
      definition.put(OrderBillService.QUERY_SUPPLIERCODE_EQUALS, supplierCode);
      definition.put(OrderBillService.QUERY_WRHCODE_EQUALS, whrCode);
      definition.put(OrderBillService.QUERY_ARTICLE_CONTAINS, articleCode);
      definition.put(OrderBillService.QUERY_SOURCEBILL_LIKE, sourceBill);
      if (StringUtil.isNullOrBlank(state) == false && "all".equals(state) == false)
        definition.put(OrderBillService.QUERY_STATE_EQUALS, OrderBillState.valueOf(state));
      PageQueryResult<OrderBill> result = orderBillService.query(definition);
      resp.setObj(result);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("分页查询失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/get", method = RequestMethod.GET)
  public @ResponseBody RespObject get(@RequestParam(value = "uuid") String uuid,
      @RequestParam(value = "token", required = true) String token) {
    RespObject resp = new RespObject();
    try {
      OrderBill orderBill = orderBillService.get(uuid);
      resp.setObj(orderBill);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("查询失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/savenew", method = RequestMethod.POST)
  public @ResponseBody RespObject saveNew(
      @RequestParam(value = "token", required = true) String token,
      @RequestBody OrderBill orderBill) {
    RespObject resp = new RespObject();
    try {
      refreshBill(orderBill);
      String orderBillUuid = orderBillService.insert(orderBill);
      resp.setObj(orderBillUuid);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("新增入库订单失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/savemodify", method = RequestMethod.PUT)
  public @ResponseBody RespObject saveModify(
      @RequestParam(value = "token", required = true) String token,
      @RequestBody OrderBill orderBill) {
    RespObject resp = new RespObject();
    try {
      refreshBill(orderBill);
      orderBillService.update(orderBill);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("编辑供应商失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/remove", method = RequestMethod.DELETE)
  public @ResponseBody RespObject remove(
      @RequestParam(value = "uuid", required = false) String uuid,
      @RequestParam(value = "token", required = false) String token,
      @RequestParam(value = "version", required = false) long version) {
    RespObject resp = new RespObject();
    try {
      orderBillService.remove(uuid, version);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("删除供应商失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/bookreg", method = RequestMethod.PUT)
  public @ResponseBody RespObject bookReg(
      @RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "uuid", required = true) String uuid,
      @RequestParam(value = "version", required = true) long version,
      @RequestParam(value = "bookedDate", required = true) String bookedDate) {
    RespObject resp = new RespObject();
    Date date = DateHelper.strToDate(bookedDate);
    try {
      orderBillService.preBookReg(uuid, version, date);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("订单预约失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/check", method = RequestMethod.PUT)
  public @ResponseBody RespObject check(
      @RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "uuid", required = true) String uuid,
      @RequestParam(value = "version", required = true) long version) {
    RespObject resp = new RespObject();
    try {
      orderBillService.preCheck(uuid, version);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("订单预检失败：" + e.getMessage());
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
      orderBillService.finish(uuid, version);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("订单完成失败：" + e.getMessage());
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
      orderBillService.abort(uuid, version);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("订单作废失败：" + e.getMessage());
    }
    return resp;
  }

  private void refreshBill(OrderBill orderBill) throws WMSException {
    if (orderBill.getSupplier() == null
        || StringUtil.isNullOrBlank(orderBill.getSupplier().getUuid()))
      throw new WMSException("供应商不能为空");
    if (orderBill.getWrh() == null || StringUtil.isNullOrBlank(orderBill.getWrh().getUuid()))
      throw new WMSException("仓位不能为空");
    Supplier supplier = supplierService.get(orderBill.getSupplier().getUuid());
    if (supplier == null)
      throw new WMSException("供应商不存在");
    Wrh wrh = binService.getWrh(orderBill.getWrh().getUuid());
    if (wrh == null)
      throw new WMSException("仓位不存在");
    orderBill.setSupplier(new UCN(supplier.getUuid(), supplier.getCode(), supplier.getName()));
    orderBill.setWrh(new UCN(wrh.getUuid(), wrh.getCode(), wrh.getName()));
    if (CollectionUtils.isEmpty(orderBill.getItems()))
      return;
  }

  @RequestMapping(value = "/getByBillNo", method = RequestMethod.GET)
  public @ResponseBody RespObject getByBillNo(@RequestParam(value = "billNumber") String billNumber,
      @RequestParam(value = "token", required = true) String token) {
    RespObject resp = new RespObject();
    try {
      OrderBill orderBill = orderBillService.getByBillNumber(billNumber);
      resp.setObj(orderBill);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("查询失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/refreshcaseqtyandamount", method = RequestMethod.PUT)
  public @ResponseBody RespObject refreshCaseQtyAndAmount(
      @RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "line", required = true) int line, @RequestBody OrderBill orderBill) {
    RespObject resp = new RespObject();
    try {
      if (CollectionUtils.isEmpty(orderBill.getItems())) {
        orderBill.setTotalAmount(BigDecimal.ZERO);
        orderBill.setTotalCaseQtyStr("0");
      } else {
        for (OrderBillItem item : orderBill.getItems()) {
          if (line == item.getLine()) {
            if (StringUtil.isNullOrBlank(item.getQpcStr()))
              throw new WMSException("规格不能为空");
            item.setCaseQtyStr(QpcHelper.qtyToCaseQtyStr(item.getQty(), item.getQpcStr()));
            orderBill.setTotalCaseQtyStr(QpcHelper.caseQtyStrAdd(
                orderBill.getTotalCaseQtyStr() == null ? "0" : orderBill.getTotalCaseQtyStr(),
                item.getCaseQtyStr()));
            if (orderBill.getTotalAmount() == null)
              orderBill.setTotalAmount(BigDecimal.ZERO);
            orderBill.setTotalAmount(
                orderBill.getTotalAmount().add(item.getQty().multiply(item.getPrice())));
            break;
          }
        }
      }

      resp.setObj(orderBill);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("刷新订单件数失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/querycanreceiveorderbills", method = RequestMethod.GET)
  public RespObject queryCanReceiveOrderBills(
      @RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "orderBillNumber", required = false) String orderBillNumber,
      @RequestParam(value = "page", required = false, defaultValue = "1") int page,
      @RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize,
      @RequestParam(value = "sort", required = false) String sort, @RequestParam(value = "order",
          required = false, defaultValue = "desc") String sortDirection) {
    RespObject resp = new RespObject();
    try {
      PageQueryDefinition definition = new PageQueryDefinition();
      definition.setPage(page);
      definition.setPageSize(pageSize);
      definition.setSortField(sort);
      definition.setOrderDir(OrderDir.valueOf(sortDirection));
      definition.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      definition.put(ReceiveBillService.FIELD_ORDER_BILLNO, orderBillNumber);
      PageQueryResult<OrderBill> pqr = orderBillService.queryCanReceiveOrderBills(definition);
      resp.setObj(pqr);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("查询可收货订单失败：", e.getMessage());
    }
    return resp;

  }

}
