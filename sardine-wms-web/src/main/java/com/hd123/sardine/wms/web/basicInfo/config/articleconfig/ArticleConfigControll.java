/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	ArticleConfigControll.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月15日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.web.basicInfo.config.articleconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.sardine.wms.api.basicInfo.config.articleconfig.ArticleConfig;
import com.hd123.sardine.wms.api.basicInfo.config.articleconfig.ArticleConfigService;
import com.hd123.sardine.wms.api.basicInfo.config.articleconfig.PickBinStockLimit;
import com.hd123.sardine.wms.common.http.ErrorRespObject;
import com.hd123.sardine.wms.common.http.RespObject;
import com.hd123.sardine.wms.common.http.RespStatus;
import com.hd123.sardine.wms.common.query.OrderDir;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.web.base.BaseController;

/**
 * @author fanqingqing
 *
 */

@RestController
@RequestMapping("/basicinfo/articleconfig")
public class ArticleConfigControll extends BaseController {

    @Autowired
    private ArticleConfigService service;

    @RequestMapping(value = "/querybypage", method = RequestMethod.GET)
    public @ResponseBody RespObject query(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false,
                    defaultValue = "asc") String sortDirection,
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "articleCode", required = false) String articleCode,
            @RequestParam(value = "fixedPickBin", required = false) String fixedPickBin) {
        RespObject resp = new RespObject();
        try {

            PageQueryDefinition definition = new PageQueryDefinition();
            definition.setPage(page);
            definition.setPageSize(pageSize);
            definition.setSortField(sort);
            definition.setOrderDir(OrderDir.valueOf(sortDirection));
            definition.put(ArticleConfigService.QUERY_ARTICLECODE_FIELD, articleCode);
            definition.put(ArticleConfigService.QUERY_FIXEDPICKBIN_FIELD, fixedPickBin);
            PageQueryResult<ArticleConfig> result = service.queryArticleConfigquery(definition);

            resp.setObj(result);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("分页查询失败：" + e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public @ResponseBody RespObject get(@RequestParam(value = "articleUuid") String articleUuid,
            @RequestParam(value = "token", required = true) String token) {
        RespObject resp = new RespObject();
        try {
            ArticleConfig articleConfig = service.getArticleConfig(articleUuid);
            resp.setObj(articleConfig);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("查询失败：" + e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/setarticlefixedpickbin", method = RequestMethod.PUT)
    public @ResponseBody RespObject setArticleFixedPickBin(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "articleUuid", required = true) String articleUuid,
            @RequestParam(value = "fixedPickBin", required = false) String fixedPickBin,
            @RequestParam(value = "version", required = false) long version) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            service.setArticleFixedPickBin(articleUuid, fixedPickBin, version);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("商品固定拣货位设置失败：" + e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/setarticlestoragearea", method = RequestMethod.PUT)
    public @ResponseBody RespObject setArticleStorageArea(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "articleUuid", required = true) String articleUuid,
            @RequestParam(value = "storageArea", required = false) String storageArea,
            @RequestParam(value = "version", required = false) long version) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            service.setArticleStorageArea(articleUuid, storageArea, version);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("商品存储区域设置失败：" + e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/setpickbinstocklimit", method = RequestMethod.PUT)
    public @ResponseBody RespObject setPickBinStockLimit(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "articleUuid", required = true) String articleUuid,
            @RequestParam(value = "version", required = false) long version,
            @RequestBody PickBinStockLimit pickBinStockLimit) {
        RespObject resp = new RespObject();
        try {

            service.setPickBinStockLimit(articleUuid, pickBinStockLimit, version);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("商品最高最低库存设置失败：" + e.getMessage());
        }
        return resp;
    }

}
