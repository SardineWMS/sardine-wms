/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	OnWayStockOutRule.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月20日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.stock;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hd123.sardine.wms.api.task.TaskType;

/**
 * 在途库存出库规则
 * <p>
 * 在途库存出库时需修改对应的主库存，通过以下两个方式定位主库存:<br>
 * 1，stockUuid<br>
 * 2，binCode + containerBarcode + stockBatch + articleUuid
 * 
 * @author zhangsai
 *
 */
public class OnWayStockOutRule implements Serializable {
  private static final long serialVersionUID = -1371991284975934100L;

  private String stockUuid;
  private String binCode;
  private String containerBarcode;
  private String stockBatch;
  private String articleUuid;
  private String taskNo;
  private TaskType taskType;
  private BigDecimal qty;

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

  public String getStockBatch() {
    return stockBatch;
  }

  public void setStockBatch(String stockBatch) {
    this.stockBatch = stockBatch;
  }

  public String getArticleUuid() {
    return articleUuid;
  }

  public void setArticleUuid(String articleUuid) {
    this.articleUuid = articleUuid;
  }

  public String getTaskNo() {
    return taskNo;
  }

  public void setTaskNo(String taskNo) {
    this.taskNo = taskNo;
  }

  public TaskType getTaskType() {
    return taskType;
  }

  public void setTaskType(TaskType taskType) {
    this.taskType = taskType;
  }

  public BigDecimal getQty() {
    return qty;
  }

  public void setQty(BigDecimal qty) {
    this.qty = qty;
  }
}
