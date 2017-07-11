/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	CompanyController.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月10日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.web.ia.company;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.ia.user.Company;
import com.hd123.sardine.wms.api.ia.user.CompanyService;
import com.hd123.sardine.wms.api.ia.user.CompanyType;
import com.hd123.sardine.wms.common.http.ErrorRespObject;
import com.hd123.sardine.wms.common.http.RespObject;
import com.hd123.sardine.wms.common.http.RespStatus;
import com.hd123.sardine.wms.web.base.BaseController;

/**
 * @author zhangsai
 *
 */
@RestController
@RequestMapping("/ia/company")
public class CompanyController extends BaseController {

  @Autowired
  private CompanyService companyService;

  @RequestMapping(value = "/createDC", method = RequestMethod.POST)
  public @ResponseBody RespObject creatDC(
      @RequestParam(value = "token", required = true) String token, @RequestBody Company company) {
    RespObject resp = new RespObject();
    try {
      if (StringUtil.isNullOrBlank(company.getUuid())) {
        company.setCompanyType(CompanyType.deliveryCenter);
        String uuid = companyService.insert(company);
        resp.setObj(uuid);
      } else {
        companyService.update(company);
        resp.setObj(company.getUuid());
      }
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("仓库创建失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/update", method = RequestMethod.PUT)
  public @ResponseBody RespObject update(
      @RequestParam(value = "token", required = true) String token, @RequestBody Company company) {
    RespObject resp = new RespObject();
    try {
      companyService.update(company);
      resp.setObj(company.getUuid());
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("仓库编辑失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/get", method = RequestMethod.GET)
  public @ResponseBody RespObject get(@RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "uuid", required = true) String uuid) {
    RespObject resp = new RespObject();
    try {
      Company company = companyService.get(uuid);
      resp.setObj(company);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("仓库查询失败：" + e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/query", method = RequestMethod.GET)
  public @ResponseBody RespObject query(
      @RequestParam(value = "token", required = true) String token) {
    RespObject resp = new RespObject();
    try { 
      List<Company> companys = companyService.queryCompanys();
      resp.setObj(companys);
    } catch (Exception e) {
      return new ErrorRespObject("仓库创建失败：" + e.getMessage());
    }
    return resp;
  }
}
