/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	BinController.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月12日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.web.basicInfo.bin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.sardine.wms.api.basicInfo.bin.Bin;
import com.hd123.sardine.wms.api.basicInfo.bin.BinInfo;
import com.hd123.sardine.wms.api.basicInfo.bin.BinService;
import com.hd123.sardine.wms.api.basicInfo.bin.BinUsage;
import com.hd123.sardine.wms.api.basicInfo.bin.Path;
import com.hd123.sardine.wms.api.basicInfo.bin.Shelf;
import com.hd123.sardine.wms.api.basicInfo.bin.Wrh;
import com.hd123.sardine.wms.api.basicInfo.bin.Zone;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.http.ErrorRespObject;
import com.hd123.sardine.wms.common.http.RespObject;
import com.hd123.sardine.wms.common.http.RespStatus;
import com.hd123.sardine.wms.common.query.OrderDir;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.web.BaseController;

/**
 * 货位控制层
 * 
 * @author zhangsai
 *
 */
@RestController
@RequestMapping("/basicinfo/bin")
public class BinController extends BaseController {
  @Autowired
  private BinService binService;

  @RequestMapping(value = "/query", method = RequestMethod.GET)
  public @ResponseBody RespObject query(
      @RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "page", required = false, defaultValue = "1") int page,
      @RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize,
      @RequestParam(value = "sort", required = false) String sort,
      @RequestParam(value = "code", required = false) String code,
      @RequestParam(value = "state", required = false) String state,
      @RequestParam(value = "usage", required = false) String usage,
      @RequestParam(value = "order", required = false, defaultValue = "asc") String sortDirection) {
    RespObject resp = new RespObject();
    try {
      PageQueryDefinition definition = new PageQueryDefinition();
      definition.setPage(page);
      definition.setPageSize(pageSize);
      definition.setSortField(sort);
      definition.setOrderDir(OrderDir.valueOf(sortDirection));
      definition.setCompanyUuid(getLoginCompany(token).getUuid());
      PageQueryResult<Bin> result = binService.queryBin(definition);
      List<BinInfo> infos = binService.queryTreeData(getLoginCompany(token).getUuid());
      BinQueryResult resultData = new BinQueryResult();
      resultData.setPageData(result);
      for (BinInfo info : infos) {
        BinTreeData treeData = new BinTreeData();
        treeData.setTitle(info.getCode());
        treeData.setKey(info.getUuid());
        // treeData.setChildren(info.getChilders());
        resultData.getTreeData().add(treeData);
      }
      resp.setObj(resultData);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("分页查询失败", e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/queryWrhs", method = RequestMethod.GET)
  public @ResponseBody RespObject queryWrhs(
      @RequestParam(value = "token", required = true) String token) {
    RespObject resp = new RespObject();
    try {
      List<Wrh> result = binService.queryWrhs(getLoginCompany(token).getUuid());
      resp.setObj(result);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("查询仓位信息失败", e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/queryZones", method = RequestMethod.GET)
  public @ResponseBody RespObject queryZones(
      @RequestParam(value = "token", required = true) String token) {
    RespObject resp = new RespObject();
    try {
      List<Zone> result = binService.queryZones(getLoginCompany(token).getUuid());
      resp.setObj(result);
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("查询货区信息失败", e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/insertWrh", method = RequestMethod.PUT)
  public @ResponseBody RespObject insertWrh(
      @RequestParam(value = "code", required = true) String code,
      @RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "name", required = true) String name,
      @RequestParam(value = "note", required = false) String note) {
    RespObject resp = new RespObject();
    try {
      Wrh wrh = new Wrh();
      wrh.setCode(code);
      wrh.setName(name);
      wrh.setCompanyUuid(getLoginCompany(token).getUuid());
      wrh.setNote(note);
      binService.insertWrh(wrh, getOperateContext(token));
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("保存仓位失败", e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/insertZone", method = RequestMethod.PUT)
  public @ResponseBody RespObject insertZone(
      @RequestParam(value = "code", required = true) String code,
      @RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "name", required = true) String name,
      @RequestParam(value = "wrhUuid", required = true) String wrhUuid,
      @RequestParam(value = "note", required = false) String note) {
    RespObject resp = new RespObject();
    try {
      Zone zone = new Zone();
      zone.setCode(code);
      zone.setName(name);
      zone.setCompanyUuid(getLoginCompany(token).getUuid());
      zone.setNote(note);
      zone.setWrh(new UCN(wrhUuid, null, null));
      binService.insertZone(zone, getOperateContext(token));
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("保存货区失败", e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/insertPath", method = RequestMethod.PUT)
  public @ResponseBody RespObject insertPath(
      @RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "zoneUuid", required = true) String zoneUuid) {
    RespObject resp = new RespObject();
    try {
      Path path = new Path();
      path.setCompanyUuid(getLoginCompany(token).getUuid());
      path.setZoneUuid(zoneUuid);
      binService.insertPath(path, getOperateContext(token));
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("保存货道失败", e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/insertShelf", method = RequestMethod.PUT)
  public @ResponseBody RespObject insertShelf(
      @RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "pathUuid", required = true) String pathUuid) {
    RespObject resp = new RespObject();
    try {
      Shelf shelf = new Shelf();
      shelf.setCompanyUuid(getLoginCompany(token).getUuid());
      shelf.setPathUuid(pathUuid);
      binService.insertShelf(shelf, getOperateContext(token));
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("保存货架失败", e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/insertBin", method = RequestMethod.PUT)
  public @ResponseBody RespObject insertBin(
      @RequestParam(value = "binLevel", required = true) String binLevel,
      @RequestParam(value = "binColumn", required = true) String binColumn,
      @RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "shelfUuid", required = true) String shelfUuid,
      @RequestParam(value = "binTypeUuid", required = true) String binTypeUuid,
      @RequestParam(value = "binUsage", required = true) String binUsage) {
    RespObject resp = new RespObject();
    try {
      Bin bin = new Bin();
      bin.setBinLevel(binLevel);
      bin.setBinColumn(binColumn);
      bin.setCompanyUuid(getLoginCompany(token).getUuid());
      bin.setShelfUuid(shelfUuid);
      bin.setUsage(BinUsage.valueOf(binUsage));
      bin.setBinType(new UCN(binTypeUuid, null, null));
      binService.insertBin(bin, getOperateContext(token));
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("保存货位失败", e.getMessage());
    }
    return resp;
  }

  @RequestMapping(value = "/delete", method = RequestMethod.PUT)
  public @ResponseBody RespObject delete(@RequestParam(value = "uuid", required = true) String uuid,
      @RequestParam(value = "version", required = false) long version,
      @RequestParam(value = "token", required = true) String token,
      @RequestParam(value = "type", required = true) String type) {
    RespObject resp = new RespObject();
    try {
      binService.remove(uuid, version, getLoginCompany(token).getUuid(), getOperateContext(token));
      resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
    } catch (Exception e) {
      return new ErrorRespObject("删除货位失败", e.getMessage());
    }
    return resp;
  }
}
