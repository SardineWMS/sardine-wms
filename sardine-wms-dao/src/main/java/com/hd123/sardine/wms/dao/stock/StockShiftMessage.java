/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-core
 * 文件名：	StockExceptionMessage.java
 * 模块说明：	
 * 修改历史：
 * 2014-4-10 - WUJING - 创建。
 */
package com.hd123.sardine.wms.dao.stock;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hd123.rumba.commons.json.JsonObject;

/**
 * @author WUJING
 * 
 */
public class StockShiftMessage implements Serializable {
  private static final long serialVersionUID = 300100L;

  private String workId;
  private String lineUuid;
  private String lineNumber;
  private String itemNo;
  private String success;
  private String articleUuid;
  private BigDecimal stockQty;
  private BigDecimal shiftQty;
  private String message;

  private static final String LINEUUID = "lineUuid";
  private static final String LINENUMBER = "lineNumber";
  private static final String ARTICLEUUID = "articleUuid";
  private static final String ITEMNO = "itemNo";
  private static final String SUCCESS = "success";
  private static final String STOCKQTY = "stockQty";
  private static final String SHIFTQTY = "shiftQty";
  private static final String MSG = "msg";

  /** 单据行标识 */
  public String getLineUuid() {
    return lineUuid;
  }

  public void setLineUuid(String lineUuid) {
    this.lineUuid = lineUuid;
  }

  /** 商品标识 */
  public String getArticleUuid() {
    return articleUuid;
  }

  public void setArticleUuid(String articleUuid) {
    this.articleUuid = articleUuid;
  }

  /** 信息 */
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  /** 行号 */
  public String getLineNumber() {
    return lineNumber;
  }

  public void setLineNumber(String lineNumber) {
    this.lineNumber = lineNumber;
  }
  
  public String getWorkId() {
    return workId;
  }

  public void setWorkId(String workId) {
    this.workId = workId;
  }

  public String getItemNo() {
    return itemNo;
  }

  public void setItemNo(String itemNo) {
    this.itemNo = itemNo;
  }

  public String getSuccess() {
    return success;
  }

  public void setSuccess(String success) {
    this.success = success;
  }

  public BigDecimal getStockQty() {
    return stockQty;
  }

  public void setStockQty(BigDecimal stockQty) {
    this.stockQty = stockQty;
  }

  public BigDecimal getShiftQty() {
    return shiftQty;
  }

  public void setShiftQty(BigDecimal shiftQty) {
    this.shiftQty = shiftQty;
  }

  public void toJSON(JsonObject json) {
    json.putOpt(LINEUUID, getLineUuid());
    json.putOpt(LINENUMBER, getLineNumber());
    json.putOpt(ARTICLEUUID, getArticleUuid());
    json.putOpt(SUCCESS, getSuccess());
    json.putOpt(ITEMNO, getItemNo());
    json.putOpt(STOCKQTY, getStockQty());
    json.putOpt(SHIFTQTY, getShiftQty());
    json.putOpt(MSG, getMessage());
  }
}
