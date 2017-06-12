/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	SupplierController.java
 * 模块说明：	
 * 修改历史：
 * 2017年1月10日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.web.basicInfo.supplier;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.supplier.Supplier;
import com.hd123.sardine.wms.api.basicInfo.supplier.SupplierService;
import com.hd123.sardine.wms.api.basicInfo.supplier.SupplierState;
import com.hd123.sardine.wms.common.http.ErrorRespObject;
import com.hd123.sardine.wms.common.http.RespObject;
import com.hd123.sardine.wms.common.http.RespStatus;
import com.hd123.sardine.wms.common.query.OrderDir;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.web.base.BaseController;

/**
 * @author fanqingqing
 *
 */
@RestController
@RequestMapping("/basicinfo/supplier")
public class SupplierController extends BaseController {

  @Autowired
  private SupplierService supplierService;

  @RequestMapping(value = "/get", method = RequestMethod.GET)
  public @ResponseBody RespObject get(@RequestParam(value = "supplierUuid") String supplierUuid,
      @RequestParam(value = "token", required = true) String token) {
    RespObject resp = new RespObject();
    try {
      Supplier supplier = supplierService.get(supplierUuid);
      resp.setObj(supplier);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("查询失败", e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/getbycode", method = RequestMethod.GET)
  public @ResponseBody RespObject getByCode(
      @RequestParam(value = "supplierCode") String supplierCode,
      @RequestParam(value = "token", required = true) String token) {
    RespObject resp = new RespObject();
    try {
      Supplier supplier = supplierService.getByCode(supplierCode);
      resp.setObj(supplier);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("查询失败", e.getMessage());
    }
    return resp;
  }

  @RequiresPermissions("supplier:view")
  @RequestMapping(value = "/querybypage", method = RequestMethod.GET)
  public @ResponseBody RespObject query(
      @RequestParam(value = "page", required = false, defaultValue = "1") int page,
      @RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize,
      @RequestParam(value = "sort", required = false) String sort,
      @RequestParam(value = "order", required = false, defaultValue = "asc") String sortDirection,
      @RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "code", required = false) String code,
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "state", required = false) String state) {
    RespObject resp = new RespObject();
    try {
      PageQueryDefinition definition = new PageQueryDefinition();
      definition.setPage(page);
      definition.setPageSize(pageSize);
      definition.setSortField(sort);
      definition.setOrderDir(OrderDir.valueOf(sortDirection));
      definition.put(SupplierService.QUERY_CODE_FIELD, code);
      definition.put(SupplierService.QUERY_NAME_FIELD, name);
      if (StringUtil.isNullOrBlank(state) == false && "all".equals(state) == false)
        definition.put(SupplierService.QUERY_STATE_FIELD, SupplierState.valueOf(state));
      PageQueryResult<Supplier> result = supplierService.query(definition);

      resp.setObj(result);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("分页查询失败", e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/savenew", method = RequestMethod.POST)
  public @ResponseBody RespObject saveNew(
      @RequestParam(value = "token", required = true) String token,
      @RequestBody Supplier supplier) {
    RespObject resp = new RespObject();
    try {
      supplier.setCompanyUuid(getLoginCompany(token).getUuid());
      String supplierUuid = supplierService.saveNew(supplier);
      resp.setObj(supplierUuid);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("新增供应商失败", e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/savemodify", method = RequestMethod.PUT)
  public @ResponseBody RespObject saveModify(
      @RequestParam(value = "token", required = true) String token,
      @RequestBody Supplier supplier) {
    RespObject resp = new RespObject();
    try {
      supplierService.saveModify(supplier);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("编辑供应商失败", e.getMessage());
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
      supplierService.remove(uuid, version);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("删除供应商失败", e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/recover", method = RequestMethod.PUT)
  public @ResponseBody RespObject recover(
      @RequestParam(value = "uuid", required = true) String uuid,
      @RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "version", required = true) long version) {
    RespObject resp = new RespObject();
    try {
      supplierService.recover(uuid, version);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("启用供应商失败", e.getMessage());
    }
    return resp;
  }

}
