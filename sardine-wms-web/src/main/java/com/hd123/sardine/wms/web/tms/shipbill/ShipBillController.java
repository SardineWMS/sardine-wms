/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	ShipBillController.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月17日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.web.tms.shipbill;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.tms.shipbill.DeliveryType;
import com.hd123.sardine.wms.api.tms.shipbill.OperateMethod;
import com.hd123.sardine.wms.api.tms.shipbill.ShipBill;
import com.hd123.sardine.wms.api.tms.shipbill.ShipBillCustomerItem;
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
 * 装车单管理控制层
 * 
 * @author fanqingqing
 *
 */
@RestController
@RequestMapping("/tms/shipbill")
public class ShipBillController extends BaseController {
    @Autowired
    private ShipBillService service;
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
            @RequestParam(value = "vehicleNum", required = false) String vehicleNum,
            @RequestParam(value = "customerCode", required = false) String customerCode,
            @RequestParam(value = "customerName", required = false) String customerName,
            @RequestParam(value = "deliveryType", required = false) String deliveryType,
            @RequestParam(value = "operateMethod", required = false) String operateMethod,
            @RequestParam(value = "driverCode", required = false) String driverCode,
            @RequestParam(value = "driverName", required = false) String driverName,
            @RequestParam(value = "articleCode", required = false) String articleCode,
            @RequestParam(value = "shiperCode", required = false) String shiperCode,
            @RequestParam(value = "shiperName", required = false) String shiperName,
            @RequestParam(value = "containerBarcode", required = false) String containerBarcode) {
        RespObject resp = new RespObject();
        try {
            PageQueryDefinition definition = new PageQueryDefinition();
            definition.setPage(page);
            definition.setPageSize(pageSize);
            definition.setSortField(
                    StringUtil.isNullOrBlank(sort) ? ShipBillService.QUERY_BILLNUMBER_LIKE : sort);
            definition.setOrderDir(OrderDir.valueOf(sortDirection));
            definition.put(ShipBillService.QUERY_BILLNUMBER_LIKE, billNumber);
            definition.put(ShipBillService.QUERY_STATE_EQUALS,
                    (StringUtil.isNullOrBlank(state) || DEFAULTOPTION.equals(state)) ? null
                            : ShipBillState.valueOf(state));
            definition.put(ShipBillService.QUERY_VEHICLENUM_LIKE, vehicleNum);
            definition.put(ShipBillService.QUERY_CUSTOMERCODE_CONTAINS, customerCode);
            definition.put(ShipBillService.QUERY_CUSTOMERNAME_CONTAINS, customerName);
            definition
                    .put(ShipBillService.QUERY_DELIVERYTYPE_EQUALS,
                            (StringUtil.isNullOrBlank(deliveryType)
                                    || DEFAULTOPTION.equals(deliveryType)) ? null
                                            : DeliveryType.valueOf(deliveryType));
            definition
                    .put(ShipBillService.QUERY_OPERATEMETHOD_EQUALS,
                            (StringUtil.isNullOrBlank(operateMethod)
                                    || DEFAULTOPTION.equals(operateMethod)) ? null
                                            : OperateMethod.valueOf(operateMethod));
            definition.put(ShipBillService.QUERY_DRIVERCODE_LIKE, driverCode);
            definition.put(ShipBillService.QUERY_DRIVERNAME_LIKE, driverName);
            definition.put(ShipBillService.QUERY_ARTICLECODE_CONTAINS, articleCode);
            definition.put(ShipBillService.QUERY_SHIPERCODE_LIKE, shiperCode);
            definition.put(ShipBillService.QUERY_SHIPERNAME_LIKE, shiperName);
            definition.put(ShipBillService.QUERY_CONTAINERBARCODE_CONTAINS, containerBarcode);

            PageQueryResult<ShipBill> result = service.query(definition);
            List<BShipBill> bShipBills = new ArrayList<>();
            for (ShipBill shipBill : result.getRecords())
                bShipBills.add(converShipBill(shipBill));
            PageQueryResult<BShipBill> pqr = new PageQueryResult<>();
            pqr.setPage(result.getPage());
            pqr.setPageCount(result.getPageCount());
            pqr.setPageSize(result.getPageSize());
            pqr.setRecordCount(result.getRecordCount());
            pqr.setRecords(bShipBills);

            resp.setObj(pqr);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (NotLoginInfoException e) {
            return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
        } catch (Exception e) {
            return new ErrorRespObject("分页获取装车单失败：" + e.getMessage());
        }
        return resp;
    }

    private BShipBill converShipBill(ShipBill source) {
        if (source == null)
            return null;
        BShipBill target = new BShipBill();
        target.setUuid(source.getUuid());
        target.setBillNumber(source.getBillNumber());
        target.setCarrier(source.getCarrier());
        target.setCompanyUuid(source.getCompanyUuid());
        target.setContainerStocks(source.getContainerStocks());
        target.setCreateInfo(source.getCreateInfo());
        target.setCustomerItems(source.getCustomerItems());
        target.setDeliveryType(source.getDeliveryType());
        target.setDriver(source.getDriver());
        target.setLastModifyInfo(source.getLastModifyInfo());
        target.setMethod(source.getMethod());
        target.setState(source.getState());
        target.setUuid(source.getUuid());
        target.setVehicleNum(source.getVehicleNum());
        target.setVersion(source.getVersion());
        target.setTotalCaseQty(source.getTotalCaseQty());
        target.setCustomerCount(source.getCustomerItems().size());
        int containerCount = 0;
        for (ShipBillCustomerItem item : source.getCustomerItems())
            containerCount += item.getContainerCount();
        target.setContainerCount(containerCount);
        return target;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public @ResponseBody RespObject get(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "uuid", required = true) String uuid) {
        RespObject resp = new RespObject();
        try {
            ShipBill bill = service.getByUuid(uuid);
            resp.setObj(bill);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (NotLoginInfoException e) {
            return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
        } catch (Exception e) {
            return new ErrorRespObject("获取装车单失败：" + e.getMessage());
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
            return new ErrorRespObject("完成装车单失败：" + e.getMessage());
        }
        return resp;
    }

}
