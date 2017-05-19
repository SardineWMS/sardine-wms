/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	DecIncInvBillItem.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月17日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.inner.decincinv;

import java.math.BigDecimal;
import java.util.Date;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.Entity;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.utils.QpcHelper;

/**
 * 损益单明细实体
 * 
 * @author fanqingqing
 *
 */
public class DecIncInvBillItem extends Entity {
    private static final long serialVersionUID = -5616582630608721471L;

    private int line;
    private String billBillUuid;
    private String binCode;
    private String containerBarCode;
    private UCN article;
    private String measureUnit;
    private UCN supplier;
    private String qpcStr;
    private BigDecimal qty;
    private String caseQtyStr;
    private Date productionDate;
    private Date expireDate;
    private String stockBatch;
    private String reason;

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getBillBillUuid() {
        return billBillUuid;
    }

    public void setBillBillUuid(String billBillUuid) {
        this.billBillUuid = billBillUuid;
    }

    public String getBinCode() {
        return binCode;
    }

    public void setBinCode(String binCode) {
        this.binCode = binCode;
    }

    public String getContainerBarCode() {
        return containerBarCode;
    }

    public void setContainerBarCode(String containerBarCode) {
        this.containerBarCode = containerBarCode;
    }

    public UCN getArticle() {
        return article;
    }

    public void setArticle(UCN article) {
        this.article = article;
    }

    public String getQpcStr() {
        return qpcStr;
    }

    public void setQpcStr(String qpcStr) {
        this.qpcStr = qpcStr;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        setCaseQtyStr(QpcHelper.qtyToCaseQtyStr(qty, qpcStr));
        this.qty = qty;
    }

    public String getCaseQtyStr() {
        return caseQtyStr;
    }

    public void setCaseQtyStr(String caseQtyStr) {
        this.caseQtyStr = caseQtyStr;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getStockBatch() {
        return stockBatch;
    }

    public void setStockBatch(String stockBatch) {
        this.stockBatch = stockBatch;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public UCN getSupplier() {
        return supplier;
    }

    public void setSupplier(UCN supplier) {
        this.supplier = supplier;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }

    public void validate() {
        Assert.assertArgumentNotNull(article, "article");
        Assert.assertArgumentNotNull(supplier, "supplier");
        Assert.assertArgumentNotNull(qpcStr, "qpcStr");
        Assert.assertArgumentNotNull(qty, "qty");
        Assert.assertArgumentNotNull(productionDate, "productionDate");
        Assert.assertArgumentNotNull(expireDate, "expireDate");
        Assert.assertArgumentNotNull(binCode, "binCode");
    }

}
