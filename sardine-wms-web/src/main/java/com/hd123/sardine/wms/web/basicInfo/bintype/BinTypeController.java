/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	BinTypeController.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月7日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.web.basicInfo.bintype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.sardine.wms.api.basicInfo.bintype.BinType;
import com.hd123.sardine.wms.api.basicInfo.bintype.BinTypeService;
import com.hd123.sardine.wms.common.http.ErrorRespObject;
import com.hd123.sardine.wms.common.http.RespObject;
import com.hd123.sardine.wms.common.http.RespStatus;
import com.hd123.sardine.wms.common.query.OrderDir;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.web.base.BaseController;

/**
 * @author yangwenzhu
 *
 */
@RestController
@RequestMapping("/basicinfo/bintype")
public class BinTypeController extends BaseController {
    @Autowired
    private BinTypeService binTypeService;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public @ResponseBody RespObject get(@RequestParam(value = "bintypeUuid") String binTypeUuid) {
        RespObject resp = new RespObject();
        try {
            BinType binType = binTypeService.get(binTypeUuid);
            resp.setObj(binType);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("查询货位类型失败：" + e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/getbycode", method = RequestMethod.GET)
    public @ResponseBody RespObject getByCode(
            @RequestParam(value = "bintypeCode") String binTypeCode) {
        RespObject resp = new RespObject();
        try {
            BinType binType = binTypeService.get(binTypeCode);
            resp.setObj(binType);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("查询货位类型失败：" + e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public @ResponseBody RespObject query(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false,
                    defaultValue = "asc") String sortDirection,
            @RequestParam(value = "token", required = false) String token,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "name", required = false) String name) {
        RespObject resp = new RespObject();
        try {
            PageQueryDefinition definition = new PageQueryDefinition();
            definition.setPage(page);
            definition.setPageSize(pageSize);
            definition.setSortField(sort);
            definition.setOrderDir(OrderDir.valueOf(sortDirection));
            definition.put(BinTypeService.QUERY_CODE_FIELD, code);
            definition.put(BinTypeService.QUERT_NAME_FIELD, name);
            definition.setCompanyUuid("com01");
            PageQueryResult<BinType> result = binTypeService.query(definition);
            resp.setObj(result);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("分页查询货位类型失败：" + e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public @ResponseBody RespObject insert(
            @RequestParam(value = "token", required = true) String token,
            @RequestBody BinType binType) {
        RespObject resp = new RespObject();
        try {
            String uuid = binTypeService.insert(binType);
            resp.setObj(uuid);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (IllegalArgumentException e) {
            return new ErrorRespObject("参数异常：" + e.getMessage());
        } catch (Exception e) {
            return new ErrorRespObject("新增货位类型失败：" + e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public @ResponseBody RespObject update(
            @RequestParam(value = "token", required = true) String token,
            @RequestBody BinType binType) {
        RespObject resp = new RespObject();
        try {
            binTypeService.update(binType);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (IllegalArgumentException e) {
            return new ErrorRespObject("参数异常：" + e.getMessage());
        } catch (Exception e) {
            return new ErrorRespObject("修改货位类型失败：" + e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/remove", method = RequestMethod.DELETE)
    public @ResponseBody RespObject remove(
            @RequestParam(value = "uuid", required = true) String uuid,
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "version", required = true) long version) {
        RespObject resp = new RespObject();
        try {
            binTypeService.remove(uuid, version);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (IllegalArgumentException e) {
            return new ErrorRespObject("参数异常：" + e.getMessage());
        } catch (Exception e) {
            return new ErrorRespObject("删除货位类型失败：" + e.getMessage());
        }
        return resp;
    }
}
