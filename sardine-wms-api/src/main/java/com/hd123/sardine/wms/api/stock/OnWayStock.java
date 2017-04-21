/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	OnWayStock.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月11日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.stock;

import java.math.BigDecimal;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.task.TaskType;
import com.hd123.sardine.wms.common.entity.Entity;

/**
 * 在途库存
 * 
 * @author zhangsai
 *
 */
public class OnWayStock extends Entity {
  private static final long serialVersionUID = 6565887818125100165L;

  private String stockUuid;
  private String binCode;
  private String containerBarcode;
  private String stockBatch;
  private String articleUuid;
  private BigDecimal qty;
  private String taskNo;
  private TaskType taskType;

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

  public String getStockBatch() {
    return stockBatch;
  }

  public void setStockBatch(String stockBatch) {
    this.stockBatch = stockBatch;
  }

  public TaskType getTaskType() {
    return taskType;
  }

  public void setTaskType(TaskType taskType) {
    this.taskType = taskType;
  }

  /** 在途库存对应的库存UUID */
  public String getStockUuid() {
    return stockUuid;
  }

  public void setStockUuid(String stockUuid) {
    this.stockUuid = stockUuid;
  }

  /** 在途库存数量，可正可负不能为0,0的时候就没有这一条了 */
  public BigDecimal getQty() {
    return qty;
  }

  public void setQty(BigDecimal qty) {
    this.qty = qty;
  }

  /** 产生该在途库存的指令，上架指令、移库指令、拣货指令、补货指令、装车指令、退货交接指令 */
  public String getTaskNo() {
    return taskNo;
  }

  public void setTaskNo(String taskNo) {
    this.taskNo = taskNo;
  }

  public void validate() {
    Assert.assertArgumentNotNull(qty, "qty");
    if (qty.compareTo(BigDecimal.ZERO) == 0)
      throw new IllegalArgumentException("入库的库存数量不能等于0！");
    Assert.assertArgumentNotNull(taskNo, "taskNo");
    Assert.assertArgumentNotNull(taskType, "taskType");

    if (StringUtil.isNullOrBlank(stockUuid) == false)
      return;

    if (StringUtil.isNullOrBlank(binCode) || StringUtil.isNullOrBlank(containerBarcode)
        || StringUtil.isNullOrBlank(stockBatch) || StringUtil.isNullOrBlank(articleUuid))
      throw new IllegalArgumentException("库存主键不能为空！");
  }
}
