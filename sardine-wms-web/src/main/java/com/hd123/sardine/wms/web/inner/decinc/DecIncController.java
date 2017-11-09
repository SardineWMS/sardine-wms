/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	DecIncController.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月23日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.web.inner.decinc;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.article.Article;
import com.hd123.sardine.wms.api.basicInfo.article.ArticleQpc;
import com.hd123.sardine.wms.api.basicInfo.article.ArticleService;
import com.hd123.sardine.wms.api.ia.user.User;
import com.hd123.sardine.wms.api.ia.user.UserService;
import com.hd123.sardine.wms.api.inner.decincinv.DecIncInvBill;
import com.hd123.sardine.wms.api.inner.decincinv.DecIncInvBillItem;
import com.hd123.sardine.wms.api.inner.decincinv.DecIncInvBillService;
import com.hd123.sardine.wms.api.inner.decincinv.DecIncInvBillState;
import com.hd123.sardine.wms.api.inner.decincinv.DecIncInvBillType;
import com.hd123.sardine.wms.api.stock.Stock;
import com.hd123.sardine.wms.api.stock.StockFilter;
import com.hd123.sardine.wms.api.stock.StockService;
import com.hd123.sardine.wms.common.entity.UCN;
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
@RequestMapping("/inner/decInc")
public class DecIncController extends BaseController {

    @Autowired
    private DecIncInvBillService service;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private StockService stockService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public @ResponseBody RespObject insert(
            @RequestParam(value = "token", required = true) String token,
            @RequestBody DecIncInvBill decIncInvBill) {
        RespObject resp = new RespObject();
        try {
            for (DecIncInvBillItem item : decIncInvBill.getItems()) {
                Article article = articleService.get(item.getArticle().getUuid());

                assert item.getProductionDate() != null;
                Calendar c = Calendar.getInstance();
                c.setTime(item.getProductionDate());
                c.add(Calendar.DATE, article.getExpDays());
                item.setExpireDate(c.getTime());
                for (ArticleQpc qpc : article.getQpcs()) {
                    if (qpc.getQpcStr().equals(item.getQpcStr())) {
                        item.setMeasureUnit(qpc.getMunit());
                    }
                }
                item.setAmount(item.getPrice().subtract(item.getQty()).abs());
            }
            User user = userService.getByCode(decIncInvBill.getOperator().getCode());
            decIncInvBill.setOperator(new UCN(user.getUuid(), user.getCode(), user.getName()));
            decIncInvBill.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
            String uuid = service.saveNew(decIncInvBill);
            resp.setObj(uuid);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("新建损溢单失败：" + e.getMessage());
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
            @RequestParam(value = "wrh", required = false) String wrh,
            @RequestParam(value = "operator", required = false) String operator,
            @RequestParam(value = "binCode", required = false) String binCode,
            @RequestParam(value = "articleCode", required = false) String articleCode,
            @RequestParam(value = "containerBarcode", required = false) String containerBarcode) {
        RespObject resp = new RespObject();
        try {
            PageQueryDefinition definition = new PageQueryDefinition();
            definition.setPage(page);
            definition.setPageSize(pageSize);
            definition.setSortField(sort);
            definition.setOrderDir(OrderDir.valueOf(sortDirection));
            definition.put(DecIncInvBillService.QUERY_BILLNUMBER_LIKE, billNumber);
            definition.put(DecIncInvBillService.QUERY_STATE_EQUALS,
                    StringUtil.isNullOrBlank(state) ? null : DecIncInvBillState.valueOf(state));
            definition.put(DecIncInvBillService.QUERY_WRHUUID_EQUALS, wrh);
            definition.put(DecIncInvBillService.QUERY_TYPE_EQUALS,
                    StringUtil.isNullOrBlank(type) ? null : DecIncInvBillType.valueOf(type));
            definition.put(DecIncInvBillService.QUERY_OPERATORCODE_EQUALS, operator);
            definition.put(DecIncInvBillService.QUERY_BINCODE_LIKE, binCode);
            definition.put(DecIncInvBillService.QUERY_ARTICLECODE_LIKE, articleCode);
            definition.put(DecIncInvBillService.QUERY_CONTAINERBARCODE_LIKE, containerBarcode);

            definition.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
            PageQueryResult<DecIncInvBill> result = service.query(definition);
            resp.setObj(result);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("分页查询损溢单失败：" + e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public @ResponseBody RespObject get(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "uuid") String uuid) {
        RespObject resp = new RespObject();
        try {
            DecIncInvBill bill = service.get(uuid);
            resp.setObj(bill);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("获取损溢单失败：" + e.getMessage());
        }
        return resp;

    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public @ResponseBody RespObject update(
            @RequestParam(value = "token", required = true) String token,
            @RequestBody DecIncInvBill decIncInvBill) {
        RespObject resp = new RespObject();
        try {
            for (DecIncInvBillItem item : decIncInvBill.getItems()) {
                Article article = articleService.get(item.getArticle().getUuid());

                assert item.getProductionDate() != null;
                Calendar c = Calendar.getInstance();
                c.setTime(item.getProductionDate());
                c.add(Calendar.DATE, article.getExpDays());
                item.setExpireDate(c.getTime());
                for (ArticleQpc qpc : article.getQpcs()) {
                    if (qpc.getQpcStr().equals(item.getQpcStr())) {
                        item.setMeasureUnit(qpc.getMunit());
                    }
                }
                item.setAmount(item.getPrice().subtract(item.getQty()).abs());
            }
            
            service.saveModify(decIncInvBill);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (NotLoginInfoException e) {
            return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
        } catch (Exception e) {
            return new ErrorRespObject("修改损溢单失败：" + e.getMessage());
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
            return new ErrorRespObject("删除损溢单失败：" + e.getMessage());
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
            service.audit(uuid, version);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("审核损溢单失败：" + e.getMessage());
        }
        return resp;

    }

    @RequestMapping(value = "/queryStockExtendInfo", method = RequestMethod.GET)
    public @ResponseBody RespObject queryStockExtendInfo(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "binCode", required = false) String binCode,
            @RequestParam(value = "containerBarcode", required = false) String containerBarcode,
            @RequestParam(value = "articleUuid", required = false) String articleUuid,
            @RequestParam(value = "qpcStr", required = false) String qpcStr) {
        RespObject resp = new RespObject();
        try {
            StockFilter filter = new StockFilter();
            filter.setArticleUuid(articleUuid);
            filter.setBinCode(binCode);
            filter.setContainerBarcode(containerBarcode);
            filter.setQpcStr(qpcStr);
            filter.setPageSize(0);
            List<Stock> stocks = stockService.query(filter);

            Set<UCN> suppliers = new HashSet<>();
            for (Stock info : stocks) {
                suppliers.add(info.getStockComponent().getSupplier());
            }
            resp.setObj(suppliers);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (NotLoginInfoException e) {
            return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
        } catch (Exception e) {
            return new ErrorRespObject("查询库存信息失败：" + e.getMessage());
        }
        return resp;

    }

}
