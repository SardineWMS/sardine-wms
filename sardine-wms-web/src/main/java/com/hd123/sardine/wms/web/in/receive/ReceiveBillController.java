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

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.article.Article;
import com.hd123.sardine.wms.api.basicInfo.article.ArticleService;
import com.hd123.sardine.wms.api.in.receive.ReceiveBill;
import com.hd123.sardine.wms.api.in.receive.ReceiveBillItem;
import com.hd123.sardine.wms.api.in.receive.ReceiveBillMethod;
import com.hd123.sardine.wms.api.in.receive.ReceiveBillService;
import com.hd123.sardine.wms.api.in.receive.ReceiveBillState;
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
 * @author yangwenzhu
 *
 */
@RestController
@RequestMapping("/in/receive")
public class ReceiveBillController extends BaseController {

    @Autowired
    private ReceiveBillService service;

    @Autowired
    private ArticleService articleService;

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public @ResponseBody RespObject query(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false,
                    defaultValue = "asc") String sortDirection,
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "billNumber", required = false) String billNumber,
            @RequestParam(value = "supplier", required = false) String supplier,
            @RequestParam(value = "wrh", required = false) String wrh,
            @RequestParam(value = "orderBill", required = false) String orderBill,
            @RequestParam(value = "state", required = false) String state) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            PageQueryDefinition definition = new PageQueryDefinition();
            definition.setPage(page);
            definition.setPageSize(pageSize);
            definition.setSortField(sort);
            definition.setOrderDir(OrderDir.valueOf(sortDirection));
            definition.put(ReceiveBillService.QUERY_BILLNO_FIELD, billNumber);
            definition.put(ReceiveBillService.QUERY_ORDERBILL_FIELD, orderBill);
            definition.put(ReceiveBillService.QUERY_SUPPLIER_FIELD, supplier);
            definition.put(ReceiveBillService.QUERY_WRH_FIELD, wrh);
            definition.put(ReceiveBillService.QUERY_STATE_FIELD,
                    StringUtil.isNullOrBlank(state) ? null : ReceiveBillState.valueOf(state));
            definition.setCompanyUuid(getLoginCompany(token).getUuid());
            PageQueryResult<ReceiveBill> result = service.query(definition);
            resp.setObj(result);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("分页查询失败：" + e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public @ResponseBody RespObject insert(
            @RequestParam(value = "token", required = true) String token,
            @RequestBody ReceiveBill receiveBill) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            ApplicationContextUtil.setOperateContext(getOperateContext(token));
            if (receiveBill.getMethod() == null)
                receiveBill.setMethod(ReceiveBillMethod.ManualBill);
            receiveBill.setCompanyUuid(getLoginCompany(token).getUuid());
            List<ReceiveBillItem> items = receiveBill.getItems();
            for (ReceiveBillItem receiveBillItem : items) {
                receiveBillItem.setQpc(QpcHelper.qpcStrToQpc(receiveBillItem.getQpcStr()));
                String uuid = receiveBillItem.getArticle().getUuid();
                Article article = articleService.get(uuid);
                int expDays = article.getExpDays();
                Date date = receiveBillItem.getProduceDate();
                receiveBillItem.getValidDate()
                        .setTime(date.getTime() + (24 * 60 * 60 * 1000) * expDays);
            }
            String uuid = service.insert(receiveBill);
            resp.setObj(uuid);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("新增收货单失败：" + e.getMessage());
        }
        return resp;

    }

    @RequestMapping(value = "/getByBillNo", method = RequestMethod.GET)
    public @ResponseBody RespObject getByBillNo(
            @RequestParam(value = "billNumber", required = true) String billNumber,
            @RequestParam(value = "token", required = true) String token) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            ReceiveBill receiveBill = service.getByBillNo(billNumber);
            resp.setObj(receiveBill);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("获取收货单失败：" + e.getMessage());
        }
        return resp;

    }

    @RequestMapping(value = "/remove", method = RequestMethod.DELETE)
    public @ResponseBody RespObject remove(
            @RequestParam(value = "uuid", required = true) String uuid,
            @RequestParam(value = "version", required = true) long version,
            @RequestParam(value = "token", required = true) String token) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setOperateContext(getOperateContext(token));
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            service.remove(uuid, version);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("删除收货单失败：" + e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/audit", method = RequestMethod.PUT)
    public @ResponseBody RespObject audit(
            @RequestParam(value = "uuid", required = true) String uuid,
            @RequestParam(value = "version", required = true) long version,
            @RequestParam(value = "token", required = true) String token) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            ApplicationContextUtil.setOperateContext(getOperateContext(token));
            service.audit(uuid, version);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("审核收货单失败：" + e.getMessage());
        }
        return resp;

    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public @ResponseBody RespObject update(
            @RequestParam(value = "token", required = true) String token,
            @RequestBody ReceiveBill receiveBill) {
        RespObject resp = new RespObject();
        try {
            for (ReceiveBillItem item : receiveBill.getItems()) {
                item.setQpc(QpcHelper.qpcStrToQpc(item.getQpcStr()));
            }
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            ApplicationContextUtil.setOperateContext(getOperateContext(token));
            service.update(receiveBill);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("修改收货单失败：" + e.getMessage());
        }
        return resp;

    }
}
