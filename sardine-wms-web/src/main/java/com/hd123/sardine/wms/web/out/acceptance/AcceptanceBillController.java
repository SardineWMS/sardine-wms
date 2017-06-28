/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	AcceptanceBillController.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月7日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.web.out.acceptance;

import java.math.BigDecimal;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBill;
import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBillItem;
import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBillService;
import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBillState;
import com.hd123.sardine.wms.common.exception.NotLoginInfoException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.http.ErrorRespObject;
import com.hd123.sardine.wms.common.http.RespObject;
import com.hd123.sardine.wms.common.http.RespStatus;
import com.hd123.sardine.wms.common.query.OrderDir;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.QpcHelper;
import com.hd123.sardine.wms.web.base.BaseController;

/**
 * @author fanqingqing
 *
 */
@RestController
@RequestMapping("/out/acceptance")
public class AcceptanceBillController extends BaseController {

    @Autowired
    private AcceptanceBillService service;

    @RequestMapping(value = "/savenew", method = RequestMethod.POST)
    public @ResponseBody RespObject insert(
            @RequestParam(value = "token", required = true) String token,
            @RequestBody AcceptanceBill acceptanceBill) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            ApplicationContextUtil.setOperateContext(getOperateContext(token));
            acceptanceBill.setCompanyUuid(getLoginCompany(token).getUuid());
            String uuid = service.insert(acceptanceBill);
            resp.setObj(uuid);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (IllegalArgumentException e) {
            return new ErrorRespObject("参数异常：" + e.getMessage());
        } catch (Exception e) {
            return new ErrorRespObject("新建领用单失败：" + e.getMessage());
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
            AcceptanceBill bill = service.get(uuid);
            resp.setObj(bill);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("获取领用单单失败：" + e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/savemodify", method = RequestMethod.PUT)
    public @ResponseBody RespObject update(
            @RequestParam(value = "token", required = true) String token,
            @RequestBody AcceptanceBill acceptanceBill) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            service.update(acceptanceBill);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (NotLoginInfoException e) {
            return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
        } catch (Exception e) {
            return new ErrorRespObject("修改领用单单失败：" + e.getMessage());
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
            @RequestParam(value = "billNumber", required = false) String billNumber,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "customercode", required = false) String customercode,
            @RequestParam(value = "wrhcode", required = false) String wrhcode,
            @RequestParam(value = "deliverySystem", required = false) String deliverySystem,
            @RequestParam(value = "deliveryType", required = false) String deliveryType) {
        RespObject resp = new RespObject();
        try {
            PageQueryDefinition definition = new PageQueryDefinition();
            definition.setPage(page);
            definition.setPageSize(pageSize);
            definition.setSortField(sort);
            definition.setOrderDir(OrderDir.valueOf(sortDirection));
            definition.put(AcceptanceBillService.QUERY_BILLNUMBER_LIKE, billNumber);
            if (StringUtil.isNullOrBlank(state) == false && "all".equals(state) == false)
                definition.put(AcceptanceBillService.QUERY_STATE_EQUALS,
                        AcceptanceBillState.valueOf(state));
            definition.put(AcceptanceBillService.QUERY_CUSTOMERCODE_EQUALS, customercode);
            definition.put(AcceptanceBillService.QUERY_WRHCODE_EQUALS, wrhcode);
            definition.put(AcceptanceBillService.QUERY_DELIVERYSYSTEM_EQUALS, deliverySystem);
            definition.put(AcceptanceBillService.QUERY_DELIVERYTYPE_EQUALS, deliveryType);

            PageQueryResult<AcceptanceBill> result = service.query(definition);
            resp.setObj(result);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("分页查询领用单失败：" + e.getMessage());
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
        } catch (Exception e) {
            return new ErrorRespObject("删除领用单失败：" + e.getMessage());
        }
        return resp;

    }

    @RequestMapping(value = "/approve", method = RequestMethod.PUT)
    public @ResponseBody RespObject approve(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "uuid", required = true) String uuid,
            @RequestParam(value = "version", required = true) long version) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            ApplicationContextUtil.setOperateContext(getOperateContext(token));
            service.approve(uuid, version);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("批准领用单失败：" + e.getMessage());
        }
        return resp;

    }

    @RequestMapping(value = "/beginalc", method = RequestMethod.PUT)
    public @ResponseBody RespObject beginAlc(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "uuid", required = true) String uuid,
            @RequestParam(value = "version", required = true) long version) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            ApplicationContextUtil.setOperateContext(getOperateContext(token));
            service.beginAlc(uuid, version);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("完成领用单失败：" + e.getMessage());
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
        } catch (Exception e) {
            return new ErrorRespObject("完成领用单失败：" + e.getMessage());
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
            return new ErrorRespObject("作废领用单失败：" + e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/refreshcaseqtyandamount", method = RequestMethod.PUT)
    public @ResponseBody RespObject refreshCaseQtyAndAmount(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "line", required = true) int line,
            @RequestBody AcceptanceBill acceptanceBill) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            ApplicationContextUtil.setOperateContext(getOperateContext(token));
            if (CollectionUtils.isEmpty(acceptanceBill.getItems())) {
                acceptanceBill.setTotalAmount(BigDecimal.ZERO);
                acceptanceBill.setTotalCaseQtyStr("0");
            } else {
                for (AcceptanceBillItem item : acceptanceBill.getItems()) {
                    if (line == item.getLine()) {
                        if (StringUtil.isNullOrBlank(item.getQpcStr()))
                            throw new WMSException("规格不能为空");
                        item.setCaseQtyStr(
                                QpcHelper.qtyToCaseQtyStr(item.getQty(), item.getQpcStr()));
                        acceptanceBill.setTotalCaseQtyStr(QpcHelper
                                .caseQtyStrAdd(acceptanceBill.getTotalCaseQtyStr() == null ? "0"
                                        : acceptanceBill.getTotalCaseQtyStr(),
                                        item.getCaseQtyStr()));
                        item.setAmount(item.getQty().multiply(item.getPrice()));
                        if (acceptanceBill.getTotalAmount() == null)
                            acceptanceBill.setTotalAmount(BigDecimal.ZERO);
                        acceptanceBill.setTotalAmount(
                                acceptanceBill.getTotalAmount().add(item.getAmount()));
                        break;
                    }
                }
            }

            resp.setObj(acceptanceBill);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("刷新订单件数失败：" + e.getMessage());
        }
        return resp;
    }

}
