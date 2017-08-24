/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	UtilController.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月10日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.web.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.stock.Stock;
import com.hd123.sardine.wms.api.stock.StockFilter;
import com.hd123.sardine.wms.api.stock.StockService;
import com.hd123.sardine.wms.common.exception.NotLoginInfoException;
import com.hd123.sardine.wms.common.http.ErrorRespObject;
import com.hd123.sardine.wms.common.http.RespObject;
import com.hd123.sardine.wms.common.http.RespStatus;
import com.hd123.sardine.wms.common.utils.QpcHelper;
import com.hd123.sardine.wms.web.base.BaseController;

/**
 * @author yangwenzhu
 *
 */
@RestController
@RequestMapping("/common/helper")
public class UtilController extends BaseController {

  @Autowired
  private StockService stockService;

  @RequestMapping(value = "/qtyToCaseQtyStr", method = RequestMethod.PUT)
  public @ResponseBody RespObject qtyToCaseQtyStr(
      @RequestParam(value = "qty", required = true) BigDecimal qty,
      @RequestParam(value = "qpcStr", required = true) String qpcStr) {
    RespObject resp = new RespObject();
    try {
      String caseQtyStr = QpcHelper.qtyToCaseQtyStr(qty, qpcStr);
      resp.setObj(caseQtyStr);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("件数转化失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/caseQtyStrAdd", method = RequestMethod.PUT)
  public @ResponseBody RespObject caseQtyStrAdd(
      @RequestParam(value = "addend", required = true) String addend,
      @RequestParam(value = "augend", required = true) String augend) {
    RespObject resp = new RespObject();
    try {
      String add = QpcHelper.caseQtyStrAdd(addend, augend);
      resp.setObj(add);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("件数相加失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/queryStock", method = RequestMethod.GET)
  public @ResponseBody RespObject queryStock(
      @RequestParam(value = "articleUuid") String articleUuid,
      @RequestParam(value = "qpcStr", required = false) String qpcStr,
      @RequestParam(value = "supplierUuid", required = false) String supplierUuid,
      @RequestParam(value = "page", required = false, defaultValue = "1") int page,
      @RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize,
      @RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "binCode", required = false) String binCode,
      @RequestParam(value = "containerBarcode", required = false) String containerBarcode) {
    RespObject resp = new RespObject();
    try {
      StockFilter filter = new StockFilter();
      filter.setArticleUuid(articleUuid);
      filter.setQpcStr(qpcStr);
      filter.setSupplierUuid(supplierUuid);
      filter.setPage(page);
      filter.setPageSize(pageSize);
      filter.setContainerBarcode(containerBarcode);
      filter.setBinCode(binCode);
      BigDecimal totalQty = BigDecimal.ZERO;
      List<Stock> stocks = stockService.query(filter);
      List<Date> productionDates = new ArrayList<Date>();
      List<String> qpcStrs = new ArrayList<>();
      for (Stock stockExtendInfo : stocks) {
        totalQty = stockExtendInfo.getStockComponent().getQty().add(totalQty);
        productionDates.add(stockExtendInfo.getStockComponent().getProductionDate());
        qpcStrs.add(stockExtendInfo.getStockComponent().getQpcStr());
      }
      String caseQtyStr = null;
      if (StringUtil.isNullOrBlank(qpcStr) == false)
        caseQtyStr = QpcHelper.qtyToCaseQtyStr(totalQty, qpcStr);
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("totalQty", totalQty);
      map.put("caseQtyStr", caseQtyStr);
      map.put("price", stocks.size() > 0 ? stocks.get(0).getStockComponent().getPrice() : 0);
      map.put("productionDates", productionDates);
      map.put("qpcStrs", qpcStrs);
      resp.setObj(map);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("查询库存信息失败：" + e.getMessage());
    }
    return resp;

  }

  @RequestMapping(value = "/caseQtyStrSubtract", method = RequestMethod.PUT)
  public @ResponseBody RespObject caseQtyStrSubtract(
      @RequestParam(value = "subStr", required = true) String subStr,
      @RequestParam(value = "subedStr", required = true) String subedStr) {
    RespObject resp = new RespObject();
    try {
      String result = QpcHelper.subtract(subStr, subedStr);
      resp.setObj(result);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("件数相减出错：" + e.getMessage());
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

      resp.setObj(stocks);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (NotLoginInfoException e) {
      return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
    } catch (Exception e) {
      return new ErrorRespObject("查询库存信息失败：" + e.getMessage());
    }

    return resp;

  }
}
