/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	ArticleServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月7日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.basicInfo.article;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.basicInfo.article.Article;
import com.hd123.sardine.wms.api.basicInfo.article.ArticleBarcode;
import com.hd123.sardine.wms.api.basicInfo.article.ArticleQpc;
import com.hd123.sardine.wms.api.basicInfo.article.ArticleService;
import com.hd123.sardine.wms.api.basicInfo.article.ArticleSupplier;
import com.hd123.sardine.wms.api.basicInfo.category.Category;
import com.hd123.sardine.wms.api.basicInfo.category.CategoryService;
import com.hd123.sardine.wms.api.basicInfo.supplier.Supplier;
import com.hd123.sardine.wms.api.basicInfo.supplier.SupplierService;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.validator.ValidateHandler;
import com.hd123.sardine.wms.common.validator.ValidateResult;
import com.hd123.sardine.wms.dao.basicInfo.article.ArticleBarcodeDao;
import com.hd123.sardine.wms.dao.basicInfo.article.ArticleDao;
import com.hd123.sardine.wms.dao.basicInfo.article.ArticleQpcDao;
import com.hd123.sardine.wms.dao.basicInfo.article.ArticleSupplierDao;
import com.hd123.sardine.wms.service.basicInfo.article.validator.ArticleInsertValidateHandler;
import com.hd123.sardine.wms.service.basicInfo.article.validator.ArticleUpdateValidateHandler;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.log.EntityLogger;

/**
 * @author zhangsai
 *
 */
public class ArticleServiceImpl extends BaseWMSService implements ArticleService {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private ArticleQpcDao articleQpcDao;

    @Autowired
    private ArticleBarcodeDao articleBarcodeDao;

    @Autowired
    private ArticleSupplierDao articleSupplierDao;

    @Autowired
    private ValidateHandler<Article> articleInsertValidateHandler;

    @Autowired
    private ValidateHandler<Article> articleUpdateValidateHandler;

    @Autowired
    private ValidateHandler<ArticleQpc> articleQpcInsertValidateHandler;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private EntityLogger logger;

    @Override
    public String insert(Article article) throws IllegalArgumentException, WMSException {
        Assert.assertArgumentNotNull(article, "article");

        Article existsArticle = articleDao.getByCode(article.getCode());
        Category category = categoryService
                .get(article.getCategory() == null ? null : article.getCategory().getUuid());

        ValidateResult insertResult = articleInsertValidateHandler
                .putAttribute(ArticleInsertValidateHandler.KEY_CODEEXISTS_ARTICLE, existsArticle)
                .putAttribute(ArticleInsertValidateHandler.KEY_CATEGORY, category)
                .validate(article);
        checkValidateResult(insertResult);

        article.setCompanyUuid(ApplicationContextUtil.getParentCompanyUuid());
        article.setUuid(article.getCompanyUuid() + article.getCode());
        article.setCreateInfo(ApplicationContextUtil.getOperateInfo());
        article.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
        articleDao.insert(article);

        logger.injectContext(this, article.getUuid(), Article.class.getName(),
                ApplicationContextUtil.getOperateContext());
        logger.log(EntityLogger.EVENT_ADDNEW, "新增商品");
        return article.getUuid();
    }

    @Override
    public void update(Article article) throws IllegalArgumentException, WMSException {
        Assert.assertArgumentNotNull(article, "article");

        Article existsArticle = articleDao.getByCode(article.getCode());
        Article updateArticle = articleDao.get(article.getUuid());
        Category category = categoryService
                .get(article.getCategory() == null ? null : article.getCategory().getUuid());

        ValidateResult insertResult = articleUpdateValidateHandler
                .putAttribute(ArticleUpdateValidateHandler.KEY_CODEEXISTS_ARTICLE, existsArticle)
                .putAttribute(ArticleUpdateValidateHandler.KEY_UPDATE_ARTICLE, updateArticle)
                .putAttribute(ArticleUpdateValidateHandler.KEY_CATEGORY, category)
                .validate(article);
        checkValidateResult(insertResult);

        article.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
        articleDao.update(article);

        logger.injectContext(this, article.getUuid(), Article.class.getName(),
                ApplicationContextUtil.getOperateContext());
        logger.log(EntityLogger.EVENT_MODIFY, "修改商品");
    }

    @Override
    public Article get(String uuid) {
        Article article = articleDao.get(uuid);
        if (article != null) {
            article.setQpcs(articleQpcDao.queryByList(uuid));
            article.setArticleSuppliers(articleSupplierDao.queryByList(uuid));
            article.setBarcodes(articleBarcodeDao.queryByList(uuid));
        }
        return article;
    }

    @Override
    public Article getByCode(String code) {
        Article article = articleDao.getByCode(code);
        if (article != null) {
            article.setQpcs(articleQpcDao.queryByList(article.getUuid()));
            article.setArticleSuppliers(articleSupplierDao.queryByList(article.getUuid()));
            article.setBarcodes(articleBarcodeDao.queryByList(article.getUuid()));
        }
        return article;
    }

    @Override
    public Article getByBarcode(String barcode) {
        Article article = articleDao.getByBarcode(barcode);
        if (article != null) {
            article.setQpcs(articleQpcDao.queryByList(article.getUuid()));
            article.setArticleSuppliers(articleSupplierDao.queryByList(article.getUuid()));
            article.setBarcodes(articleBarcodeDao.queryByList(article.getUuid()));
        }
        return article;
    }

    @Override
    public PageQueryResult<Article> query(PageQueryDefinition definition)
            throws IllegalArgumentException {
        Assert.assertArgumentNotNull(definition, "definition");

        PageQueryResult<Article> pgr = new PageQueryResult<Article>();
        List<Article> list = articleDao.query(definition);
        PageQueryUtil.assignPageInfo(pgr, definition);
        pgr.setRecords(list);
        return pgr;
    }

    @Override
    public void insertArticleQpc(ArticleQpc qpc) throws IllegalArgumentException, WMSException {
        ValidateResult insertResult = articleQpcInsertValidateHandler.validate(qpc);
        checkValidateResult(insertResult);

        Article article = articleDao.get(qpc.getArticleUuid());
        if (article == null)
            throw new WMSException("商品" + qpc.getArticleUuid() + "不存在。");

        List<ArticleQpc> qpcs = articleQpcDao.queryByList(qpc.getArticleUuid());
        ArticleQpc oldQpc = null;
        for (ArticleQpc qpcc : qpcs) {
            if (qpcc.getUuid().equals(qpc.getUuid()))
                oldQpc = qpcc;
            else {
                if (qpcc.getQpcStr().equals(qpc.getQpcStr()))
                    throw new WMSException("同一商品不能存在相同的规格。");
            }
        }
        if (oldQpc == null) {
            articleQpcDao.insert(qpc);
        } else {
            articleQpcDao.remove(qpc.getUuid());
            articleQpcDao.insert(qpc);
        }

        logger.injectContext(this, qpc.getUuid(), ArticleQpc.class.getName(),
                ApplicationContextUtil.getOperateContext());
        logger.log(EntityLogger.EVENT_ADDNEW, "新增商品规格");
    }

    @Override
    public void insertArticleBarcode(ArticleBarcode barcode)
            throws IllegalArgumentException, WMSException {
        Assert.assertArgumentNotNull(barcode, "barcode");

        ArticleQpc qpc = articleQpcDao.getByQpcStr(barcode.getArticleUuid(), barcode.getQpcStr());
        if (qpc == null)
            throw new WMSException("规格" + barcode.getQpcStr() + "不存在。");
        Article article = articleDao.get(barcode.getArticleUuid());
        if (article == null)
            throw new WMSException("商品" + barcode.getArticleUuid() + "不存在。");
        Article articleByBarcode = articleDao.getByBarcode(barcode.getBarcode());
        if (articleByBarcode != null
                && articleByBarcode.getUuid().equals(barcode.getArticleUuid()) == false)
            throw new WMSException("商品条码" + barcode.getBarcode() + "已被其他商品占用。");

        List<ArticleBarcode> barcodes = articleBarcodeDao.queryByList(barcode.getArticleUuid());
        ArticleBarcode oldAb = null;
        for (ArticleBarcode ab : barcodes) {
            if (ab.getUuid().equals(barcode.getUuid()))
                oldAb = ab;
            else {
                if (ab.getBarcode().equals(barcode.getBarcode()))
                    throw new WMSException("同一商品不能存在相同的规格。");
            }
        }
        if (oldAb == null) {
            articleBarcodeDao.insert(barcode);
        } else {
            articleBarcodeDao.remove(barcode.getUuid());
            articleBarcodeDao.insert(barcode);
        }

        logger.injectContext(this, barcode.getUuid(), ArticleBarcode.class.getName(),
                ApplicationContextUtil.getOperateContext());
        logger.log(EntityLogger.EVENT_ADDNEW, "新增商品规格");
    }

    @Override
    public void insertArticleSupplier(ArticleSupplier supplier)
            throws IllegalArgumentException, WMSException {
        Assert.assertArgumentNotNull(supplier, "supplier");

        Supplier sup = supplierService.get(supplier.getSupplierUuid());
        if (sup == null)
            throw new WMSException("供应商" + supplier.getSupplierCode() + "不存在。");
        Article article = articleDao.get(supplier.getArticleUuid());
        if (article == null)
            throw new WMSException("商品" + supplier.getArticleUuid() + "不存在。");

        List<ArticleSupplier> ass = articleSupplierDao.queryByList(supplier.getArticleUuid());
        ArticleSupplier as = null;
        for (ArticleSupplier aas : ass) {
            if (aas.getUuid().equals(supplier.getUuid()))
                as = aas;
            else if (aas.getSupplierUuid().equals(supplier.getSupplierUuid()))
                throw new WMSException("同一商品不能存在相同的供应商。");
        }

        if (as == null) {
            as = new ArticleSupplier();
            as.setUuid(supplier.getUuid());
            as.setArticleUuid(supplier.getArticleUuid());
            as.setSupplierUuid(sup.getUuid());
            as.setSupplierCode(sup.getCode());
            as.setSupplierName(sup.getName());
            as.setDefault_(false);
            articleSupplierDao.insert(as);
        } else {
            as.setSupplierUuid(sup.getUuid());
            as.setSupplierCode(sup.getCode());
            as.setSupplierName(sup.getName());
            as.setDefault_(false);
            articleSupplierDao.remove(supplier.getUuid());
            articleSupplierDao.insert(as);
        }

        logger.injectContext(this, supplier.getUuid(), ArticleSupplier.class.getName(),
                ApplicationContextUtil.getOperateContext());
        logger.log(EntityLogger.EVENT_ADDNEW, "新增商品供应商");
    }

    @Override
    public void deleteArticleQpc(String articleUuid, String qpcUuid)
            throws IllegalArgumentException, WMSException {
        Assert.assertArgumentNotNull(articleUuid, "articleUuid");
        Assert.assertArgumentNotNull(qpcUuid, "qpcUuid");

        Article article = articleDao.get(articleUuid);
        if (article == null)
            throw new WMSException("商品" + articleUuid + "不存在。");
        articleQpcDao.remove(qpcUuid);

        logger.injectContext(this, qpcUuid, ArticleQpc.class.getName(),
                ApplicationContextUtil.getOperateContext());
        logger.log(EntityLogger.EVENT_REMOVE, "删除商品规格");
    }

    @Override
    public void deleteArticleBarcode(String articleUuid, String barcodeUuid)
            throws IllegalArgumentException, WMSException {
        Assert.assertArgumentNotNull(articleUuid, "articleUuid");
        Assert.assertArgumentNotNull(barcodeUuid, "barcodeUuid");

        Article article = articleDao.get(articleUuid);
        if (article == null)
            throw new WMSException("商品" + articleUuid + "不存在。");
        articleBarcodeDao.remove(barcodeUuid);

        logger.injectContext(this, barcodeUuid, ArticleBarcode.class.getName(),
                ApplicationContextUtil.getOperateContext());
        logger.log(EntityLogger.EVENT_REMOVE, "删除商品条码");
    }

    @Override
    public void deleteArticleSupplier(String articleUuid, String articleSupplierUuid)
            throws IllegalArgumentException, WMSException {
        Assert.assertArgumentNotNull(articleUuid, "articleUuid");
        Assert.assertArgumentNotNull(articleSupplierUuid, "articleSupplierUuid");

        Article article = articleDao.get(articleUuid);
        if (article == null)
            throw new WMSException("商品" + articleUuid + "不存在。");
        articleSupplierDao.remove(articleSupplierUuid);

        logger.injectContext(this, articleSupplierUuid, ArticleSupplier.class.getName(),
                ApplicationContextUtil.getOperateContext());
        logger.log(EntityLogger.EVENT_REMOVE, "删除商品供应商");
    }

    @Override
    public void setDefaultArticleSupplier(String articleUuid, String articleSupplierUuid)
            throws IllegalArgumentException, WMSException {
        Assert.assertArgumentNotNull(articleUuid, "articleUuid");
        Assert.assertArgumentNotNull(articleSupplierUuid, "articleSupplierUuid");

        Article article = articleDao.get(articleUuid);
        if (article == null)
            throw new WMSException("商品" + articleUuid + "不存在。");
        articleSupplierDao.setUnDefault(articleUuid);
        articleSupplierDao.setDefault(articleSupplierUuid);

        logger.injectContext(this, articleUuid, Article.class.getName(),
                ApplicationContextUtil.getOperateContext());
        logger.log(EntityLogger.EVENT_MODIFY, "设置商品默认供应商");
    }

    @Override
    public void setDefaultArticleQpc(String articleUuid, String qpcUuid)
            throws IllegalArgumentException, WMSException {
        Assert.assertArgumentNotNull(articleUuid, "articleUuid");
        Assert.assertArgumentNotNull(qpcUuid, "qpcUuid");

        Article article = articleDao.get(articleUuid);
        if (article == null)
            throw new WMSException("商品" + articleUuid + "不存在。");
        articleQpcDao.setUnDefault(articleUuid);
        articleQpcDao.setDefault(qpcUuid);

        logger.injectContext(this, articleUuid, Article.class.getName(),
                ApplicationContextUtil.getOperateContext());
        logger.log(EntityLogger.EVENT_MODIFY, "设置商品默认规格");
    }

    @Override
    public PageQueryResult<UCN> queryInStocks(PageQueryDefinition definition)
            throws IllegalArgumentException {
        Assert.assertArgumentNotNull(definition, "definition");
        PageQueryResult<UCN> pgr = new PageQueryResult<>();
        List<UCN> list = articleDao.queryInStocks(definition);
        PageQueryUtil.assignPageInfo(pgr, definition);
        pgr.setRecords(list);
        return pgr;
    }
}
