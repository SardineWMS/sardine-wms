/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	Stock.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月11日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.stock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.VersionedEntity;

/**
 * 库存实体
 * <p>
 * 业务主键：批次+货位+容器+组织<br>
 * 有效数量：qty和qty+operateQty的最小值<br>
 * 库存来源单据：收货单、损益单、退仓单<br>
 * qty和onWayQty不能为空，且不能同时为0，否则无意义<br>
 * onWayQty可正可负，正数表示待入库的在途库存，负数则是待出库的在途库存
 * 
 * @author zhangsai
 *
 */
public class Stock extends VersionedEntity {
    private static final long serialVersionUID = 6976161514506150256L;

    public static final String DEFAULT_SUPPLIER = "unknow";
    public static final String DEFAULT_STOCKBATCH = "unknow";
    public static final String DEFAULT_SOURCEBILL = "unknow";

    private String companyUuid;
    private String supplierUuid;
    private String binCode;
    private String containerBarcode;
    private String articleUuid;
    private String stockBatch;
    private Date productionDate;
    private Date validDate;
    private String sourceBillUuid;
    private String sourceBillNumber;
    private String sourceBillType;
    private int sourceLineNumber;
    private String sourceLineUuid;
    private BigDecimal qty = BigDecimal.ZERO;
    private String qpcStr;
    private String measureUnit;
    private Date instockTime = new Date();
    private Date modifyTime = new Date();
    private BigDecimal onWayQty = BigDecimal.ZERO;
    private BigDecimal price;
    // private BigDecimal qpc;

    private List<OnWayStock> onWayStocks = new ArrayList<OnWayStock>();

    /** 组织ID */
    public String getCompanyUuid() {
        return companyUuid;
    }

    public void setCompanyUuid(String companyUuid) {
        this.companyUuid = companyUuid;
    }

    /** 供应商ID */
    public String getSupplierUuid() {
        return supplierUuid;
    }

    public void setSupplierUuid(String supplierUuid) {
        this.supplierUuid = supplierUuid;
    }

    /** 货位代码 */
    public String getBinCode() {
        return binCode;
    }

    public void setBinCode(String binCode) {
        this.binCode = binCode;
    }

    /** 容器条码 */
    public String getContainerBarcode() {
        return containerBarcode;
    }

    public void setContainerBarcode(String containerBarcode) {
        this.containerBarcode = containerBarcode;
    }

    /** 商品UUID */
    public String getArticleUuid() {
        return articleUuid;
    }

    public void setArticleUuid(String articleUuid) {
        this.articleUuid = articleUuid;
    }

    /** 批次 */
    public String getStockBatch() {
        return stockBatch;
    }

    public void setStockBatch(String stockBatch) {
        this.stockBatch = stockBatch;
    }

    /** 生产日期 */
    public Date getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    /** 到效期 */
    public Date getValidDate() {
        return validDate;
    }

    public void setValidDate(Date validDate) {
        this.validDate = validDate;
    }

    /** 库存入库的来源单据UUID */
    public String getSourceBillUuid() {
        return sourceBillUuid;
    }

    public void setSourceBillUuid(String sourceBillUuid) {
        this.sourceBillUuid = sourceBillUuid;
    }

    /** 库存入库的来源单据单号 */
    public String getSourceBillNumber() {
        return sourceBillNumber;
    }

    public void setSourceBillNumber(String sourceBillNumber) {
        this.sourceBillNumber = sourceBillNumber;
    }

    /** 库存入库的来源单据类型 */
    public String getSourceBillType() {
        return sourceBillType;
    }

    public void setSourceBillType(String sourceBillType) {
        this.sourceBillType = sourceBillType;
    }

    /** 库存对应来源单据的行号 */
    public int getSourceLineNumber() {
        return sourceLineNumber;
    }

    public void setSourceLineNumber(int sourceLineNumber) {
        this.sourceLineNumber = sourceLineNumber;
    }

    /** 库存对应来源单据的行UUID */
    public String getSourceLineUuid() {
        return sourceLineUuid;
    }

    public void setSourceLineUuid(String sourceLineUuid) {
        this.sourceLineUuid = sourceLineUuid;
    }

    /** 库存数量 */
    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    /** qocStr */
    public String getQpcStr() {
        return qpcStr;
    }

    public void setQpcStr(String qpcStr) {
        this.qpcStr = qpcStr;
    }

    /** 计量单位 */
    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }

    /** 入库时间 */
    public Date getInstockTime() {
        return instockTime;
    }

    public void setInstockTime(Date instockTime) {
        this.instockTime = instockTime;
    }

    /** 在途库存 */
    public BigDecimal getOnWayQty() {
        return onWayQty;
    }

    public void setOnWayQty(BigDecimal onWayQty) {
        this.onWayQty = onWayQty;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /** 在途库存明细集合 */
    public List<OnWayStock> getOnWayStocks() {
        return onWayStocks;
    }

    public void setOnWayStocks(List<OnWayStock> onWayStocks) {
        this.onWayStocks = onWayStocks;
    }

    /** 取库存有效数量 */
    public BigDecimal fetchAavailableQty() {
        if (BigDecimal.ZERO.compareTo(qty) >= 0)
            return BigDecimal.ZERO;

        if (BigDecimal.ZERO.compareTo(qty.add(onWayQty)) >= 0)
            return BigDecimal.ZERO;

        return qty.compareTo(qty.add(onWayQty)) >= 0 ? qty.add(onWayQty) : qty;
    }

    // public void setQpc(BigDecimal qpc) {
    // this.qpc = qpc;
    // }
    //
    //
    // public BigDecimal getQpc() {
    // return qpc;
    // }

    public void validate() {
        Assert.assertArgumentNotNull(binCode, "binCode");
        Assert.assertArgumentNotNull(containerBarcode, "containerBarcode");
        Assert.assertArgumentNotNull(productionDate, "productionDate");
        Assert.assertArgumentNotNull(validDate, "validDate");
        Assert.assertArgumentNotNull(articleUuid, "articleUuid");
        Assert.assertArgumentNotNull(supplierUuid, "supplierUuid");
        Assert.assertArgumentNotNull(stockBatch, "stockBatch");
        Assert.assertArgumentNotNull(sourceBillUuid, "sourceBillUuid");
        Assert.assertArgumentNotNull(sourceBillType, "sourceBillType");
        Assert.assertArgumentNotNull(sourceBillNumber, "sourceBillNumber");
        Assert.assertArgumentNotNull(sourceLineUuid, "sourceLineUuid");
        Assert.assertArgumentNotNull(companyUuid, "companyUuid");
        Assert.assertArgumentNotNull(qpcStr, "qpcStr");
        Assert.assertArgumentNotNull(qty, "qty");
        Assert.assertArgumentNotNull(onWayQty, "onWayQty");
        Assert.assertArgumentNotNull(measureUnit, "measureUnit");

        if (qty.compareTo(BigDecimal.ZERO) == 0 && onWayQty.compareTo(BigDecimal.ZERO) == 0)
            throw new IllegalArgumentException("库存数量和在途数量不能同时为0");

        if (BigDecimal.ZERO.compareTo(qty) > 0)
            throw new IllegalArgumentException("库存实际数量不能小于0");
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
