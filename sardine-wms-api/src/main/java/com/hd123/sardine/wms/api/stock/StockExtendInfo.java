/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	StockExtendInfo.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月11日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.stock;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.hd123.sardine.wms.common.entity.UCN;

/**
 * 库存扩展信息
 * <p>
 * 包含：商品ucn、供应商ucn 数量：实际可用数量
 * 
 * @author zhangsai
 *
 */
public class StockExtendInfo implements Serializable {
    private static final long serialVersionUID = -7844153588152183422L;

    private String companyUuid;
    private UCN supplier;
    private String binCode;
    private String containerBarcode;
    private UCN article;
    private String articleSpec;
    private String stockBatch;
    private Date productionDate;
    private Date validDate;
    private String sourceBillUuid;
    private String sourceBillNumber;
    private String sourceBillType;
    private int sourceLineNumber;
    private String sourceLineUuid;
    // private BigDecimal qpc;
    private BigDecimal qty;
    private String qpcStr;
    private String measureUnit;
    private Date instockTime = new Date();
    private BigDecimal price;

    public String getCompanyUuid() {
        return companyUuid;
    }

    public void setCompanyUuid(String companyUuid) {
        this.companyUuid = companyUuid;
    }

    public UCN getSupplier() {
        return supplier;
    }

    public void setSupplier(UCN supplier) {
        this.supplier = supplier;
    }

    public String getBinCode() {
        return binCode;
    }

    public void setBinCode(String binCode) {
        this.binCode = binCode;
    }

    public String getContainerBarcode() {
        return containerBarcode;
    }

    public void setContainerBarcode(String containerBarCode) {
        this.containerBarcode = containerBarCode;
    }

    public UCN getArticle() {
        return article;
    }

    public void setArticle(UCN article) {
        this.article = article;
    }

    public String getArticleSpec() {
        return articleSpec;
    }

    public void setArticleSpec(String articleSpec) {
        this.articleSpec = articleSpec;
    }

    public String getStockBatch() {
        return stockBatch;
    }

    public void setStockBatch(String stockBatch) {
        this.stockBatch = stockBatch;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    public Date getValidDate() {
        return validDate;
    }

    public void setValidDate(Date validDate) {
        this.validDate = validDate;
    }

    public String getSourceBillUuid() {
        return sourceBillUuid;
    }

    public void setSourceBillUuid(String sourceBillUuid) {
        this.sourceBillUuid = sourceBillUuid;
    }

    public String getSourceBillNumber() {
        return sourceBillNumber;
    }

    public void setSourceBillNumber(String sourceBillNumber) {
        this.sourceBillNumber = sourceBillNumber;
    }

    public String getSourceBillType() {
        return sourceBillType;
    }

    public void setSourceBillType(String sourceBillType) {
        this.sourceBillType = sourceBillType;
    }

    public int getSourceLineNumber() {
        return sourceLineNumber;
    }

    public void setSourceLineNumber(int sourceLineNumber) {
        this.sourceLineNumber = sourceLineNumber;
    }

    public String getSourceLineUuid() {
        return sourceLineUuid;
    }

    public void setSourceLineUuid(String sourceLineUuid) {
        this.sourceLineUuid = sourceLineUuid;
    }

    // public BigDecimal getQpc() {
    // return qpc;
    // }
    //
    // public void setQpc(BigDecimal qpc) {
    // this.qpc = qpc;
    // }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getQpcStr() {
        return qpcStr;
    }

    public void setQpcStr(String qpcStr) {
        this.qpcStr = qpcStr;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }

    public Date getInstockTime() {
        return instockTime;
    }

    public void setInstockTime(Date instockTime) {
        this.instockTime = instockTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}
