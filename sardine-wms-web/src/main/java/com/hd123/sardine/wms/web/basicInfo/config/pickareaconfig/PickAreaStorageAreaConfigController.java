/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	PickAreaStorageAreaConfigControl.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月19日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.web.basicInfo.config.pickareaconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.config.pickareastorageareaconfig.PickAreaStorageAreaConfig;
import com.hd123.sardine.wms.api.basicInfo.config.pickareastorageareaconfig.PickAreaStorageAreaConfigService;
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
@RequestMapping("/basicinfo/pickareastorageareaconfig")
public class PickAreaStorageAreaConfigController extends BaseController {
    @Autowired
    private PickAreaStorageAreaConfigService service;

    @RequestMapping(value = "/querybypage", method = RequestMethod.GET)
    public @ResponseBody RespObject query(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false,
                    defaultValue = "asc") String sortDirection,
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "pickAreaCode", required = false) String pickAreaCode,
            @RequestParam(value = "pickAreaName", required = false) String pickAreaName) {
        RespObject resp = new RespObject();
        try {
            PageQueryDefinition definition = new PageQueryDefinition();
            definition.setPage(page);
            definition.setPageSize(pageSize);
            if (StringUtil.isNullOrBlank(sort) == false && "pickAreaCode".equals(sort))
                definition.setSortField("code");
            definition.setOrderDir(OrderDir.valueOf(sortDirection));
            definition.put(PickAreaStorageAreaConfigService.QUERY_PICKAREACODE_FIELD, pickAreaCode);
            definition.put(PickAreaStorageAreaConfigService.QUERY_PICKAREANAME_FIELD, pickAreaName);
            PageQueryResult<PickAreaStorageAreaConfig> result = service.query(definition);

            resp.setObj(result);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("分页查询失败：" + e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/setpickareastorageareaconfig", method = RequestMethod.PUT)
    public @ResponseBody RespObject setPickAreaStorageAreaConfig(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "pickAreaUuid", required = true) String pickAreaUuid,
            @RequestParam(value = "storageArea", required = false) String storageArea,
            @RequestParam(value = "version", required = false) long version) {
        RespObject resp = new RespObject();
        try {
            service.setPickAreaStorageAreaConfig(pickAreaUuid, storageArea, version);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("商品类别存储区域设置：" + e.getMessage());
        }
        return resp;
    }
}
