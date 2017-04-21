/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	StockQtyLog.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月11日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.stock;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存日志
 * <p>
 * logType={@link StockLog#NORMAL_LOG}时，qty、beforeQty、afterQty为正常库存数量<br>
 * logType={@link StockLog#ONWAY_LOG}时，qty、beforeQty、afterQty为在途库存数量<br>
 * afterQty = beforeQty-qty<br>
 * 入库时qty>0，出库时qty<0，qty不能等于0
 * 
 * @author zhangsai
 *
 */
public class StockLog implements Serializable {
  private static final long serialVersionUID = 3464771239941652057L;

  public static final String ONWAY_LOG = "onWayLog";
  public static final String NORMAL_LOG = "normalLog";

  private String stockUuid;
  private String binCode;
  private String containerBarcode;
  private String articleUuid;
  private String qpcStr;
  private String supplierUuid;
  private String stockBatch;
  private BigDecimal beforeQty;
  private BigDecimal afterQty;
  private BigDecimal qty;
  private String operateType;
  private String operateNumber;
  private Date operateDate;
  private String logType;
  private String companyUuid;

  public String getCompanyUuid() {
    return companyUuid;
  }

  public void setCompanyUuid(String companyUuid) {
    this.companyUuid = companyUuid;
  }

  public String getStockUuid() {
    return stockUuid;
  }

  public void setStockUuid(String stockUuid) {
    this.stockUuid = stockUuid;
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

  public void setContainerBarcode(String containerBarcode) {
    this.containerBarcode = containerBarcode;
  }

  public String getArticleUuid() {
    return articleUuid;
  }

  public void setArticleUuid(String articleUuid) {
    this.articleUuid = articleUuid;
  }

  public String getQpcStr() {
    return qpcStr;
  }

  public void setQpcStr(String qpcStr) {
    this.qpcStr = qpcStr;
  }

  public String getSupplierUuid() {
    return supplierUuid;
  }

  public void setSupplierUuid(String supplierUuid) {
    this.supplierUuid = supplierUuid;
  }

  public String getStockBatch() {
    return stockBatch;
  }

  public void setStockBatch(String stockBatch) {
    this.stockBatch = stockBatch;
  }

  public BigDecimal getBeforeQty() {
    return beforeQty;
  }

  public void setBeforeQty(BigDecimal beforeQty) {
    this.beforeQty = beforeQty;
  }

  public BigDecimal getAfterQty() {
    return afterQty;
  }

  public void setAfterQty(BigDecimal afterQty) {
    this.afterQty = afterQty;
  }

  public BigDecimal getQty() {
    return qty;
  }

  public void setQty(BigDecimal qty) {
    this.qty = qty;
  }

  public String getOperateType() {
    return operateType;
  }

  public void setOperateType(String operateType) {
    this.operateType = operateType;
  }

  public String getOperateNumber() {
    return operateNumber;
  }

  public void setOperateNumber(String operateNumber) {
    this.operateNumber = operateNumber;
  }

  public Date getOperateDate() {
    return operateDate;
  }

  public void setOperateDate(Date operateDate) {
    this.operateDate = operateDate;
  }

  public String getLogType() {
    return logType;
  }

  public void setLogType(String logType) {
    this.logType = logType;
  }
}
