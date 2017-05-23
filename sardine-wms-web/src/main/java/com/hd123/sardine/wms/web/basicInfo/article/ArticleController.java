/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名：	sardine-wms-web
 * 文件名：	ArticleController.java
 * 模块说明：	
 * 修改历史：
 * 2016年12月12日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.web.basicInfo.article;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.article.Article;
import com.hd123.sardine.wms.api.basicInfo.article.ArticleBarcode;
import com.hd123.sardine.wms.api.basicInfo.article.ArticleQpc;
import com.hd123.sardine.wms.api.basicInfo.article.ArticleService;
import com.hd123.sardine.wms.api.basicInfo.article.ArticleState;
import com.hd123.sardine.wms.api.basicInfo.article.ArticleSupplier;
import com.hd123.sardine.wms.api.basicInfo.category.Category;
import com.hd123.sardine.wms.api.basicInfo.category.CategoryService;
import com.hd123.sardine.wms.api.basicInfo.supplier.Supplier;
import com.hd123.sardine.wms.api.basicInfo.supplier.SupplierService;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.http.ErrorRespObject;
import com.hd123.sardine.wms.common.http.RespObject;
import com.hd123.sardine.wms.common.http.RespStatus;
import com.hd123.sardine.wms.common.query.OrderDir;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.web.BaseController;

/**
 * 商品管理控制层
 * 
 * @author zhangsai
 *
 */
@RestController
@RequestMapping("/basicinfo/article")
public class ArticleController extends BaseController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public @ResponseBody RespObject get(@RequestParam(value = "articleUuid") String articleUuid,
            @RequestParam(value = "token") String token) {
        RespObject resp = new RespObject();
        try {
            Article article = articleService.get(articleUuid);
            resp.setObj(article);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("查询失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/getbycode", method = RequestMethod.GET)
    public @ResponseBody RespObject getByCode(
            @RequestParam(value = "articleCode") String articleCode,
            @RequestParam(value = "token") String token) {
        RespObject resp = new RespObject();
        try {
            Article article = articleService.getByCode(articleCode);
            resp.setObj(article);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("查询失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/querybypage", method = RequestMethod.GET)
    public @ResponseBody RespObject query(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false,
                    defaultValue = "asc") String sortDirection,
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "state", required = false) String state) {
        RespObject resp = new RespObject();
        try {
            ApplicationContextUtil.setCompany(getLoginCompany(token));
            PageQueryDefinition definition = new PageQueryDefinition();
            definition.setPage(page);
            definition.setPageSize(pageSize);
            definition.setSortField(sort);
            definition.setOrderDir(OrderDir.valueOf(sortDirection));
            definition.put(ArticleService.QUERY_CODE_FIELD, code);
            definition.put(ArticleService.QUERY_NAME_FIELD, name);
            definition.put(ArticleService.QUERY_STATE_FIELD,
                    StringUtil.isNullOrBlank(state) ? null : ArticleState.valueOf(state));
            definition.put("companyUuid", ApplicationContextUtil.getParentCompanyUuid());
            PageQueryResult<Article> result = articleService.query(definition);
            resp.setObj(result);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("分页查询失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public @ResponseBody RespObject insert(
            @RequestParam(value = "token", required = true) String token,
            @RequestBody Article article) {
        RespObject resp = new RespObject();
        try {
            Category c = categoryService.getByCode(article.getCategory().getCode());
            article.setCategory(new UCN(c.getUuid(), c.getCode(), c.getName()));
            article.setCompanyUuid(getLoginCompany(token).getUuid());
            String articleUuid = article.getUuid();
            if (StringUtil.isNullOrBlank(article.getUuid()))
                articleUuid = articleService.insert(article);
            else
                articleService.update(article);
            resp.setObj(articleService.get(articleUuid));
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("新增商品失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/getAndSaveSupplier", method = RequestMethod.PUT)
    public @ResponseBody RespObject getAndSaveSupplier(
            @RequestParam(value = "articleUuid", required = true) String articleUuid,
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "uuid", required = false) String uuid,
            @RequestParam(value = "supplierCode", required = true) String supplierCode) {
        RespObject resp = new RespObject();
        try {
            Supplier supplier = supplierService.getByCode(supplierCode);
            if (supplier == null)
                throw new Exception("供应商" + supplierCode + "不存在，");
            ArticleSupplier as = new ArticleSupplier();
            as.setUuid(uuid);
            as.setArticleUuid(articleUuid);
            as.setSupplierUuid(supplier.getUuid());
            as.setSupplierCode(supplier.getCode());
            as.setSupplierName(supplier.getName());
            articleService.insertArticleSupplier(as);

            Article article = articleService.get(articleUuid);
            resp.setObj(article);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("保存商品供应商失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/addArticleSupplier", method = RequestMethod.GET)
    public @ResponseBody RespObject addArticleSupplier(
            @RequestParam(value = "articleUuid", required = true) String articleUuid,
            @RequestParam(value = "token", required = true) String token) {
        RespObject resp = new RespObject();
        try {
            Article article = articleService.get(articleUuid);
            ArticleSupplier as = new ArticleSupplier();
            as.setUuid(UUIDGenerator.genUUID());
            article.getArticleSuppliers().add(as);
            resp.setObj(article);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("新增商品供应商失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/deleteArticleSupplier", method = RequestMethod.PUT)
    public @ResponseBody RespObject deleteArticleSupplier(
            @RequestParam(value = "articleUuid", required = true) String articleUuid,
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "uuid", required = true) String uuid) {
        RespObject resp = new RespObject();
        try {
            articleService.deleteArticleSupplier(articleUuid, uuid);
            Article article = articleService.get(articleUuid);
            resp.setObj(article);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("删除商品供应商失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/setDefaultSupplier", method = RequestMethod.PUT)
    public @ResponseBody RespObject setDefaultSupplier(
            @RequestParam(value = "articleUuid", required = true) String articleUuid,
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "uuid", required = true) String uuid) {
        RespObject resp = new RespObject();
        try {
            articleService.setDefaultArticleSupplier(articleUuid, uuid);
            Article article = articleService.get(articleUuid);
            resp.setObj(article);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("设置默认商品供应商失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/setDefaultQpcStr", method = RequestMethod.PUT)
    public @ResponseBody RespObject setDefaultQpcStr(
            @RequestParam(value = "articleUuid", required = true) String articleUuid,
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "uuid", required = true) String uuid) {
        RespObject resp = new RespObject();
        try {
            articleService.setDefaultArticleQpc(articleUuid, uuid);
            Article article = articleService.get(articleUuid);
            resp.setObj(article);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("设置默认规格失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/deleteArticleQpc", method = RequestMethod.PUT)
    public @ResponseBody RespObject deleteArticleQpc(
            @RequestParam(value = "articleUuid", required = true) String articleUuid,
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "uuid", required = true) String uuid) {
        RespObject resp = new RespObject();
        try {
            articleService.deleteArticleQpc(articleUuid, uuid);
            Article article = articleService.get(articleUuid);
            resp.setObj(article);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("编辑用户失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/saveArticleQpc", method = RequestMethod.PUT)
    public @ResponseBody RespObject saveArticleQpc(
            @RequestParam(value = "articleUuid", required = true) String articleUuid,
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "uuid", required = true) String uuid,
            @RequestParam(value = "qpcStr", required = true) String qpcStr,
            @RequestParam(value = "length", required = true) BigDecimal length,
            @RequestParam(value = "width", required = true) BigDecimal width,
            @RequestParam(value = "height", required = true) BigDecimal height,
            @RequestParam(value = "weight", required = true) BigDecimal weight,
            @RequestParam(value = "munit", required = true) String munit) {
        RespObject resp = new RespObject();
        try {
            ArticleQpc qpc = new ArticleQpc();
            qpc.setHeight(height);
            qpc.setLength(length);
            qpc.setWidth(width);
            qpc.setWeight(weight);
            qpc.setQpcStr(qpcStr);
            qpc.setMunit(munit);
            qpc.setDefault_(false);
            qpc.setUuid(uuid);
            qpc.setArticleUuid(articleUuid);
            articleService.insertArticleQpc(qpc);
            Article article = articleService.get(articleUuid);
            resp.setObj(article);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("保存商品规格失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/addArticleQpc", method = RequestMethod.GET)
    public @ResponseBody RespObject addArticleQpc(
            @RequestParam(value = "articleUuid", required = true) String articleUuid,
            @RequestParam(value = "token", required = true) String token) {
        RespObject resp = new RespObject();
        try {
            Article article = articleService.get(articleUuid);
            ArticleQpc qpc = new ArticleQpc();
            qpc.setUuid(UUIDGenerator.genUUID());
            article.getQpcs().add(qpc);
            resp.setObj(article);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("新增商品规格失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/deleteArticleBarcode", method = RequestMethod.PUT)
    public @ResponseBody RespObject deleteArticleBarcode(
            @RequestParam(value = "articleUuid", required = true) String articleUuid,
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "uuid", required = true) String uuid) {
        RespObject resp = new RespObject();
        try {
            articleService.deleteArticleBarcode(articleUuid, uuid);
            Article article = articleService.get(articleUuid);
            resp.setObj(article);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("删除商品条码失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/saveArticleBarcode", method = RequestMethod.PUT)
    public @ResponseBody RespObject saveArticleBarcode(
            @RequestParam(value = "articleUuid", required = true) String articleUuid,
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "uuid", required = true) String uuid,
            @RequestParam(value = "barcode", required = true) String barcode,
            @RequestParam(value = "qpcStr", required = true) String qpcStr) {
        RespObject resp = new RespObject();
        try {
            ArticleBarcode articleBarcode = new ArticleBarcode();
            articleBarcode.setUuid(uuid);
            articleBarcode.setBarcode(barcode);
            articleBarcode.setQpcStr(qpcStr);
            articleBarcode.setArticleUuid(articleUuid);
            articleService.insertArticleBarcode(articleBarcode);
            Article article = articleService.get(articleUuid);
            resp.setObj(article);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("保存商品条码失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/addArticleBarcode", method = RequestMethod.GET)
    public @ResponseBody RespObject addArticleBarcode(
            @RequestParam(value = "articleUuid", required = true) String articleUuid,
            @RequestParam(value = "token", required = true) String token) {
        RespObject resp = new RespObject();
        try {
            Article article = articleService.get(articleUuid);
            ArticleBarcode ab = new ArticleBarcode();
            ab.setUuid(UUIDGenerator.genUUID());
            article.getBarcodes().add(ab);
            resp.setObj(article);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("增加商品条码失败", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/setArticleFixedPickBin", method = RequestMethod.PUT)
    public @ResponseBody RespObject setArticleFixedPickBin(
            @RequestParam(value = "articleUuid", required = true) String articleUuid,
            @RequestParam(value = "fixedPickBin", required = true) String fixedPickBin,
            @RequestParam(value = "token", required = true) String token) {
        RespObject resp = new RespObject();
        try {
            articleService.updateArticleFixedPickBin(articleUuid, fixedPickBin);
            resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
        } catch (Exception e) {
            return new ErrorRespObject("设置商品固定拣货位失败", e.getMessage());
        }
        return resp;
    }

}
