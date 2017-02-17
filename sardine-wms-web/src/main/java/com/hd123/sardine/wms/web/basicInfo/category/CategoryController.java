/**
 * 版权所有(C)，LHWMS项目组，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	CategoryController.java
 * 模块说明：	
 * 修改历史：
 * 2017年1月12日 - Jing - 创建。
 */
package com.hd123.sardine.wms.web.basicInfo.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.sardine.wms.api.basicInfo.category.Category;
import com.hd123.sardine.wms.api.basicInfo.category.CategoryService;
import com.hd123.sardine.wms.common.http.ErrorRespObject;
import com.hd123.sardine.wms.common.http.RespObject;
import com.hd123.sardine.wms.common.http.RespStatus;
import com.hd123.sardine.wms.web.BaseController;

/**
 * @author Jing
 *
 */
@RestController
@RequestMapping("/basicinfo/category")
public class CategoryController extends BaseController {

  @Autowired
  private CategoryService categoryService;

  @RequestMapping(value = "/query", method = RequestMethod.GET)
  public @ResponseBody RespObject getRootCategorys(
      @RequestParam(value = "token", required = true) String token) {
    try {
      RespObject resp = new RespObject();
      List<Category> categorys = categoryService.getRootCategorys(getLoginCompany(token).getUuid());
      resp.setObj(categorys);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
      return resp;
    } catch (Exception e) {
      return new ErrorRespObject("查询失败", e.getMessage());
    }
  }

  @RequestMapping(value = "/get", method = RequestMethod.GET)
  public @ResponseBody RespObject get(@RequestParam(value = "uuid") String uuid) {
    try {
      RespObject resp = new RespObject();
      Category category = categoryService.get(uuid);
      resp.setObj(category);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
      return resp;
    } catch (Exception e) {
      return new ErrorRespObject("查询失败", e.getMessage());
    }
  }

  @RequestMapping(value = "/savenew", method = RequestMethod.POST)
  public @ResponseBody RespObject saveNew(
      @RequestParam(value = "token", required = true) String token,
      @RequestBody Category category) {
    RespObject resp = new RespObject();
    try {
      category.setCompanyUuid(getLoginCompany(token).getUuid());
      categoryService.saveNew(category, getOperateContext(token));
      resp.setObj(category.getCode());
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("新增商品类别失败。。。", e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/savemodify", method = RequestMethod.POST)
  public @ResponseBody RespObject saveModify(
      @RequestParam(value = "token", required = true) String token,
      @RequestBody Category category) {
    try {
      RespObject resp = new RespObject();
      category.setCompanyUuid(getLoginCompany(token).getUuid());
      categoryService.saveModify(category, getOperateContext(token));
      resp.setObj(category.getCode());
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
      return resp;
    } catch (Exception e) {
      return new ErrorRespObject("修改商品类别失败", e.getMessage());
    }
  }

  @RequestMapping(value = "/remove", method = RequestMethod.DELETE)
  public @ResponseBody RespObject remove(@RequestParam(value = "uuid", required = true) String uuid,
      @RequestParam(value = "version", required = true) long version,
      @RequestParam(value = "token", required = true) String token) {
    try {
      RespObject resp = new RespObject();
      categoryService.remove(uuid, version, getOperateContext(token));
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
      return resp;
    } catch (Exception e) {
      return new ErrorRespObject("删除商品类别失败", e.getMessage());
    }
  }
}
