/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	SerialArchController.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月16日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.web.tms.serialarch;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.sardine.wms.api.basicInfo.customer.CustomerService;
import com.hd123.sardine.wms.api.tms.serialarch.SerialArch;
import com.hd123.sardine.wms.api.tms.serialarch.SerialArchInfo;
import com.hd123.sardine.wms.api.tms.serialarch.SerialArchLine;
import com.hd123.sardine.wms.api.tms.serialarch.SerialArchLineCustomer;
import com.hd123.sardine.wms.api.tms.serialarch.SerialArchService;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.exception.NotLoginInfoException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.http.ErrorRespObject;
import com.hd123.sardine.wms.common.http.RespObject;
import com.hd123.sardine.wms.common.http.RespStatus;
import com.hd123.sardine.wms.common.query.OrderDir;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.web.base.BaseController;

/**
 * @author yangwenzhu
 *
 */
@RestController
@RequestMapping(value = "/tms/serialarch")
public class SerialArchController extends BaseController {
  @Autowired
  private SerialArchService service;
  @Autowired
  private CustomerService customerService;

  @RequestMapping(value = "/createSerialArch", method = RequestMethod.POST)
  public RespObject createSerialArch(@RequestParam(value = "token", required = true) String token,
      @RequestBody SerialArch arch) {
    RespObject resp = new RespObject();
    try {
      arch.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      String uuid = service.saveNewSerialArch(arch);
      resp.setObj(uuid);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (NotLoginInfoException e) {
      return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
    } catch (Exception e) {
      return new ErrorRespObject("新增线路体系失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/queryTreeData", method = RequestMethod.GET)
  public RespObject queryTreeData(@RequestParam(value = "token", required = true) String token) {
    RespObject resp = new RespObject();
    try {
      List<SerialArchInfo> data = service.queryTreeData();
      resp.setObj(data);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (NotLoginInfoException e) {
      return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
    } catch (Exception e) {
      return new ErrorRespObject("获取线路体系失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/createLine", method = RequestMethod.POST)
  public RespObject createLine(@RequestParam(value = "token", required = true) String token,
      @RequestBody SerialArchLine line) {
    RespObject resp = new RespObject();
    try {
      line.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      String uuid = service.saveNewSerialArchLine(line);
      resp.setObj(uuid);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (NotLoginInfoException e) {
      return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
    } catch (Exception e) {
      return new ErrorRespObject("新增运输线路失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/getLineByCode", method = RequestMethod.GET)
  public RespObject getLineByCode(@RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "code", required = true) String code) {
    RespObject resp = new RespObject();
    try {
      SerialArchLine line = service.getLineByCode(code);
      resp.setObj(line);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (NotLoginInfoException e) {
      return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
    } catch (Exception e) {
      return new ErrorRespObject("获取运输线路失败：" + e.getMessage());
    }
    return resp;

  }

  @RequestMapping(value = "/addCustomer", method = RequestMethod.POST)
  public RespObject addCustomer(@RequestParam(value = "token", required = true) String token,
      @RequestBody LineCustomer lineCustomer) {
    RespObject resp = new RespObject();
    try {
      String code = service.saveLineCustomer(lineCustomer.getLineUuid(),
          lineCustomer.getCustomers());
      resp.setObj(code);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (NotLoginInfoException e) {
      return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
    } catch (Exception e) {
      return new ErrorRespObject("保存线路客户失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/queryCustomerWithoutLine", method = RequestMethod.GET)
  public RespObject queryCustomerWithoutLine(
      @RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "page", required = false, defaultValue = "1") int page,
      @RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize,
      @RequestParam(value = "sort", required = false) String sort,
      @RequestParam(value = "order", required = false, defaultValue = "asc") String sortDirection,
      @RequestParam(value = "lineUuid", required = false) String lineUuid) {
    RespObject resp = new RespObject();
    try {
      PageQueryDefinition definition = new PageQueryDefinition();
      definition.setPage(page);
      definition.setPageSize(0);
      definition.setSortField(sort);
      definition.setOrderDir(OrderDir.valueOf(sortDirection));
      List<UCN> allCustomer = customerService.queryAllCustomer();// 不分页获取当前登录组织下所有的客户
      SerialArchLine line = service.getLine(lineUuid);
      List<SerialArchLine> list = service.getLinesByArchUuid(line.getSerialArch().getUuid());
      Set<UCN> uuids = new HashSet<>();
      for (SerialArchLine l : list) {
        List<SerialArchLineCustomer> allCustomersInArch = service
            .getCustomersByLineUuid(l.getUuid());
        for (SerialArchLineCustomer c : allCustomersInArch) {
          uuids.add(c.getCustomer());// 当前线路体系下的所有客户
        }
      }
      allCustomer.removeAll(uuids);
      definition.setPageSize(pageSize);
      PageQueryResult<UCN> pqr = new PageQueryResult<>();
      PageQueryUtil.assignPageInfo(pqr, definition);
      pqr.setRecords(allCustomer);
      resp.setObj(pqr);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (NotLoginInfoException e) {
      return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
    } catch (Exception e) {
      return new ErrorRespObject("查询客户失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/getLine", method = RequestMethod.GET)
  public RespObject getLine(@RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "uuid", required = true) String uuid) {
    RespObject resp = new RespObject();
    try {
      SerialArchLine line = service.getLine(uuid);
      resp.setObj(line);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (NotLoginInfoException e) {
      return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
    } catch (Exception e) {
      return new ErrorRespObject("获取运输线路失败：" + e.getMessage());
    }
    return resp;

  }

  @RequestMapping(value = "/removeCustomer", method = RequestMethod.DELETE)
  public RespObject removeCustomer(@RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "customerUuid", required = true) String customerUuid,
      @RequestParam(value = "lineUuid", required = true) String lineUuid) {
    RespObject resp = new RespObject();
    try {
      service.removeCustomerFromLine(customerUuid, lineUuid);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (NotLoginInfoException e) {
      return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
    } catch (Exception e) {
      return new ErrorRespObject("客户踢出线路失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/upOrder", method = RequestMethod.PUT)
  public RespObject upOrder(@RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "customerUuid", required = true) String customerUuid,
      @RequestParam(value = "lineUuid", required = true) String lineUuid,
      @RequestParam(value = "order", required = true) int order) {
    RespObject resp = new RespObject();
    try {
      service.upOrder(customerUuid, lineUuid, order);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (NotLoginInfoException e) {
      return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
    } catch (Exception e) {
      return new ErrorRespObject("客户调整顺序：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/downOrder", method = RequestMethod.PUT)
  public RespObject downOrder(@RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "customerUuid", required = true) String customerUuid,
      @RequestParam(value = "lineUuid", required = true) String lineUuid,
      @RequestParam(value = "order", required = true) int order) {
    RespObject resp = new RespObject();
    try {
      service.downOrder(customerUuid, lineUuid, order);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (NotLoginInfoException e) {
      return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
    } catch (Exception e) {
      return new ErrorRespObject("客户调整顺序：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/postponeCustomer", method = RequestMethod.PUT)
  public RespObject postponeCustomer(@RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "customerUuid", required = true) String customerUuid,
      @RequestParam(value = "lineUuid", required = true) String lineUuid,
      @RequestParam(value = "order", required = true) int order) {
    RespObject resp = new RespObject();
    try {
      service.postponeCustomer(customerUuid, lineUuid, order);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (NotLoginInfoException e) {
      return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
    } catch (Exception e) {
      return new ErrorRespObject("客户顺序置后：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/stickCustomer", method = RequestMethod.PUT)
  public RespObject stickCustomer(@RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "customerUuid", required = true) String customerUuid,
      @RequestParam(value = "lineUuid", required = true) String lineUuid,
      @RequestParam(value = "order", required = true) int order) {
    RespObject resp = new RespObject();
    try {
      service.stickCustomer(customerUuid, lineUuid, order);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (NotLoginInfoException e) {
      return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
    } catch (Exception e) {
      return new ErrorRespObject("客户顺序置顶：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/queryCustomerByLine", method = RequestMethod.GET)
  public RespObject queryCustomerByLine(
      @RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "page", required = false, defaultValue = "1") int page,
      @RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize,
      @RequestParam(value = "sort", required = false) String sort,
      @RequestParam(value = "order", required = false, defaultValue = "asc") String sortDirection,
      @RequestParam(value = "lineCode", required = true) String lineCode) {
    RespObject resp = new RespObject();
    try {
      PageQueryDefinition definition = new PageQueryDefinition();
      definition.setPage(page);
      definition.setPageSize(pageSize);
      definition.setSortField(sort);
      definition.setOrderDir(OrderDir.valueOf(sortDirection));
      SerialArchLine line = service.getLineByCode(lineCode);
      if (Objects.isNull(line))
        throw new WMSException("运输线路不存在");
      definition.put("lineUuid", line.getUuid());
      PageQueryResult<SerialArchLineCustomer> result = service.queryCustomerByLine(definition);
      Map<String, Object> map = new HashMap<>();
      map.put("result", result);
      map.put("lineUuid", line.getUuid());
      resp.setObj(map);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (NotLoginInfoException e) {
      return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
    } catch (Exception e) {
      return new ErrorRespObject("查询线路中的门店失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/querySerialArch", method = RequestMethod.GET)
  public RespObject querySerialArch(@RequestParam(value = "token", required = true) String token) {
    RespObject resp = new RespObject();
    try {
      PageQueryDefinition definition = new PageQueryDefinition();
      definition.setPageSize(0);
      definition.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      PageQueryResult<SerialArch> result = service.query(definition);
      resp.setObj(result.getRecords());
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (NotLoginInfoException e) {
      return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
    } catch (Exception e) {
      return new ErrorRespObject("查询线路体系失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/removeline", method = RequestMethod.DELETE)
  public RespObject removeLine(@RequestParam(value = "uuid", required = true) String uuid,
      @RequestParam(value = "version", required = true) long version) {
    RespObject resp = new RespObject();
    try {
      service.removeLine(uuid, version);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("删除线路失败：", e.getMessage());
    }
    return resp;
  }

}
