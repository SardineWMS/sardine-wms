/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	StockFilter.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月11日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.stock;

import java.util.ArrayList;
import java.util.List;

import com.hd123.sardine.wms.common.query.PageQueryDefinition;

/**
 * @author zhangsai
 *
 */
public class StockFilter extends PageQueryDefinition {
    private static final long serialVersionUID = -3011733549963203907L;

    private String binCode;
    private String containerBarcode;
    private String articleUuid;
    private String articleCode;
    private String supplierUuid;
    private String supplierCode;
    private String qpcStr;
    private String stockBatch;
    private String sourceBillUuid;
    private String sourcebillNumber;
    private String productionBatch;
    private StockState state;
    private String operateBillUuid;
    private String stockUuid;
    private String owner;
    private String wrhUuid;
    private List<String> binCodes = new ArrayList<>();

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
        put("owner", owner);
    }

    public StockState getState() {
        return state;
    }

    public void setState(StockState state) {
        this.state = state;
        put("state", state == null ? null : state);
    }

    public String getOperateBillUuid() {
        return operateBillUuid;
    }

    public void setOperateBillUuid(String operateBillUuid) {
        this.operateBillUuid = operateBillUuid;
        put("operateBillUuid", operateBillUuid);
    }

    public String getStockUuid() {
        return stockUuid;
    }

    public void setStockUuid(String stockUuid) {
        this.stockUuid = stockUuid;
        put("stockUuid", stockUuid);
    }

    public String getBinCode() {
        return binCode;
    }

    public void setBinCode(String binCode) {
        this.binCode = binCode;
        put("binCode", binCode);
    }

    public String getContainerBarcode() {
        return containerBarcode;
    }

    public void setContainerBarcode(String containerBarcode) {
        this.containerBarcode = containerBarcode;
        put("containerBarcode", containerBarcode);
    }

    public String getArticleUuid() {
        return articleUuid;
    }

    public void setArticleUuid(String articleUuid) {
        this.articleUuid = articleUuid;
        put("articleUuid", articleUuid);
    }

    public String getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(String articleCode) {
        this.articleCode = articleCode;
        put("articleCode", articleCode);
    }

    public String getSupplierUuid() {
        return supplierUuid;
    }

    public void setSupplierUuid(String supplierUuid) {
        this.supplierUuid = supplierUuid;
        put("supplierUuid", supplierUuid);
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
        put("supplierCode", supplierCode);
    }

    public String getQpcStr() {
        return qpcStr;
    }

    public void setQpcStr(String qpcStr) {
        this.qpcStr = qpcStr;
        put("qpcStr", qpcStr);
    }

    public String getStockBatch() {
        return stockBatch;
    }

    public void setStockBatch(String stockBatch) {
        this.stockBatch = stockBatch;
        put("stockBatch", stockBatch);
    }

    public String getSourceBillUuid() {
        return sourceBillUuid;
    }

    public void setSourceBillUuid(String sourceBillUuid) {
        this.sourceBillUuid = sourceBillUuid;
        put("sourceBillUuid", sourceBillUuid);
    }

    public String getSourcebillNumber() {
        return sourcebillNumber;
    }

    public void setSourcebillNumber(String sourcebillNumber) {
        this.sourcebillNumber = sourcebillNumber;
        put("sourcebillNumber", sourcebillNumber);
    }

    public String getProductionBatch() {
        return productionBatch;
    }

    public void setProductionBatch(String productionBatch) {
        this.productionBatch = productionBatch;
        put("productionBatch", productionBatch);
    }

    public String getWrhUuid() {
        return wrhUuid;
    }

    public void setWrhUuid(String wrhUuid) {
        this.wrhUuid = wrhUuid;
        put("wrhUuid", wrhUuid);
    }

    public List<String> getBinCodes() {
        return binCodes;
    }

    public void setBinCodes(List<String> binCodes) {
        this.binCodes = binCodes;
        put("binCodes", binCodes);
    }

}
