/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	CategoryStorageAreaConfigControll.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月17日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.web.basicInfo.config.categorystorageareaconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.sardine.wms.api.basicInfo.config.categorystorageareaconfig.CategoryStorageAreaConfig;
import com.hd123.sardine.wms.api.basicInfo.config.categorystorageareaconfig.CategoryStorageAreaConfigService;
import com.hd123.sardine.wms.common.http.ErrorRespObject;
import com.hd123.sardine.wms.common.http.RespObject;
import com.hd123.sardine.wms.common.http.RespStatus;
import com.hd123.sardine.wms.common.query.OrderDir;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.web.base.BaseController;

/**
 * 商品类别存储区域
 * 
 * @author fanqingqing
 *
 */

@RestController
@RequestMapping("/basicinfo/categorystorageareaconfig")
public class CategoryStorageAreaConfigControll extends BaseController {

    @Autowired
    private CategoryStorageAreaConfigService service;

    @RequestMapping(value = "/querybypage", method = RequestMethod.GET)
    public @ResponseBody RespObject query(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false,
                    defaultValue = "asc") String sortDirection,
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "categoryCode", required = false) String categoryCode,
            @RequestParam(value = "categoryName", required = false) String categoryName,
            @RequestParam(value = "categoryUpperCode", required = false) String categoryUpperCode) {
        RespObject resp = new RespObject();
        try {
            PageQueryDefinition definition = new PageQueryDefinition();
            definition.setPage(page);
            definition.setPageSize(pageSize);
            definition.setSortField(sort);
            definition.setOrderDir(OrderDir.valueOf(sortDirection));
            definition.put(CategoryStorageAreaConfigService.QUERY_CATEGORYCODE_FIELD, categoryCode);
            definition.put(CategoryStorageAreaConfigService.QUERY_CATEGORYNAME_FIELD, categoryName);
            definition.put(CategoryStorageAreaConfigService.QUERY_CATEGORYUPPERCODE_FIELD, categoryUpperCode);

            PageQueryResult<CategoryStorageAreaConfig> result = service.query(definition);

            resp.setObj(result);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("分页查询失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/setcategorystoragearea", method = RequestMethod.PUT)
    public @ResponseBody RespObject setArticleStorageArea(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "categoryUuid", required = true) String categoryUuid,
            @RequestParam(value = "storageArea", required = false) String storageArea,
            @RequestParam(value = "version", required = false) long version) {
        RespObject resp = new RespObject();
        try {
            service.setCategoryStorageAreaConfig(categoryUuid, storageArea, version);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("商品类别存储区域设置", e.getMessage());
        }
        return resp;
    }
}
