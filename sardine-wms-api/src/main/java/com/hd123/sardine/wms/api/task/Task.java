/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	Task.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月12日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.task;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.OperateMode;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.entity.VersionedEntity;

/**
 * 指令：实体
 * 
 * @author zhangsai
 *
 */
public class Task extends VersionedEntity {
  private static final long serialVersionUID = 1126133098926836810L;

  private String taskGroupNumber;
  private String taskNo;
  private TaskType taskType;
  private UCN article;
  private String articleSpec;
  private String munit;
  private TaskState state;
  private String qpcStr;
  private BigDecimal qty;
  private String caseQtyStr;
  private BigDecimal realQty;
  private String realCaseQtyStr;
  private Date productionDate;
  private Date validDate;
  private String productionBatch;
  private String stockBatch;
  private UCN supplier;
  private UCN wrh;
  private String fromBinCode;
  private String fromContainerBarcode;
  private String toBinCode;
  private String toContainerBarcode;
  private String owner;
  private String sourceBillType;
  private String sourceBillNumber;
  private String sourceBillUuid;
  private String sourceBillLineUuid;
  private UCN creator;
  private OperateMode type;
  private UCN operator;
  private Date createTime;
  private Date beginOperateTime;
  private Date endOperateTime;
  private String companyUuid;

  private List<TaskStockItem> items = new ArrayList<TaskStockItem>();

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

  public String getMunit() {
    return munit;
  }

  public void setMunit(String munit) {
    this.munit = munit;
  }

  public TaskState getState() {
    return state;
  }

  public void setState(TaskState state) {
    this.state = state;
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
    this.qty = qty;
  }

  public String getCaseQtyStr() {
    return caseQtyStr;
  }

  public void setCaseQtyStr(String caseQtyStr) {
    this.caseQtyStr = caseQtyStr;
  }

  public BigDecimal getRealQty() {
    return realQty;
  }

  public void setRealQty(BigDecimal realQty) {
    this.realQty = realQty;
  }

  public String getRealCaseQtyStr() {
    return realCaseQtyStr;
  }

  public void setRealCaseQtyStr(String realCaseQtyStr) {
    this.realCaseQtyStr = realCaseQtyStr;
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

  public String getProductionBatch() {
    return productionBatch;
  }

  public void setProductionBatch(String productionBatch) {
    this.productionBatch = productionBatch;
  }

  public String getStockBatch() {
    return stockBatch;
  }

  public void setStockBatch(String stockBatch) {
    this.stockBatch = stockBatch;
  }

  public UCN getSupplier() {
    return supplier;
  }

  public void setSupplier(UCN supplier) {
    this.supplier = supplier;
  }

  public String getFromBinCode() {
    return fromBinCode;
  }

  public void setFromBinCode(String fromBinCode) {
    this.fromBinCode = fromBinCode;
  }

  public String getFromContainerBarcode() {
    return fromContainerBarcode;
  }

  public void setFromContainerBarcode(String fromContainerBarcode) {
    this.fromContainerBarcode = fromContainerBarcode;
  }

  public String getToBinCode() {
    return toBinCode;
  }

  public void setToBinCode(String toBinCode) {
    this.toBinCode = toBinCode;
  }

  public String getToContainerBarcode() {
    return toContainerBarcode;
  }

  public void setToContainerBarcode(String toContainerBarcode) {
    this.toContainerBarcode = toContainerBarcode;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getSourceBillType() {
    return sourceBillType;
  }

  public void setSourceBillType(String sourceBillType) {
    this.sourceBillType = sourceBillType;
  }

  public String getSourceBillNumber() {
    return sourceBillNumber;
  }

  public void setSourceBillNumber(String sourceBillNumber) {
    this.sourceBillNumber = sourceBillNumber;
  }

  public String getSourceBillUuid() {
    return sourceBillUuid;
  }

  public void setSourceBillUuid(String sourceBillUuid) {
    this.sourceBillUuid = sourceBillUuid;
  }

  public UCN getCreator() {
    return creator;
  }

  public void setCreator(UCN creator) {
    this.creator = creator;
  }

  public OperateMode getType() {
    return type;
  }

  public void setType(OperateMode type) {
    this.type = type;
  }

  public UCN getOperator() {
    return operator;
  }

  public void setOperator(UCN operator) {
    this.operator = operator;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getBeginOperateTime() {
    return beginOperateTime;
  }

  public void setBeginOperateTime(Date beginOperateTime) {
    this.beginOperateTime = beginOperateTime;
  }

  public Date getEndOperateTime() {
    return endOperateTime;
  }

  public void setEndOperateTime(Date endOperateTime) {
    this.endOperateTime = endOperateTime;
  }

  public String getCompanyUuid() {
    return companyUuid;
  }

  public void setCompanyUuid(String companyUuid) {
    this.companyUuid = companyUuid;
  }

  public String getSourceBillLineUuid() {
    return sourceBillLineUuid;
  }

  public void setSourceBillLineUuid(String sourceBillLineUuid) {
    this.sourceBillLineUuid = sourceBillLineUuid;
  }

  public String getTaskGroupNumber() {
    return taskGroupNumber;
  }

  public void setTaskGroupNumber(String taskGroupNumber) {
    this.taskGroupNumber = taskGroupNumber;
  }

  public UCN getWrh() {
    return wrh;
  }

  public void setWrh(UCN wrh) {
    this.wrh = wrh;
  }

  public List<TaskStockItem> getItems() {
    return items;
  }

  public void setItems(List<TaskStockItem> items) {
    this.items = items;
  }

  public void validate() {
    Assert.assertArgumentNotNull(taskType, "taskType");
    Assert.assertArgumentNotNull(article, "article");
    Assert.assertArgumentNotNull(qpcStr, "qpcStr");
    Assert.assertArgumentNotNull(qty, "qty");
    Assert.assertArgumentNotNull(caseQtyStr, "caseQtyStr");
    Assert.assertArgumentNotNull(productionDate, "productionDate");
    Assert.assertArgumentNotNull(validDate, "validDate");
    Assert.assertArgumentNotNull(stockBatch, "stockBatch");
    Assert.assertArgumentNotNull(supplier, "supplier");
    Assert.assertArgumentNotNull(fromBinCode, "fromBinCode");
    Assert.assertArgumentNotNull(fromContainerBarcode, "fromContainerBarcode");
    Assert.assertArgumentNotNull(toContainerBarcode, "toContainerBarcode");
    Assert.assertArgumentNotNull(owner, "owner");
    Assert.assertArgumentNotNull(sourceBillType, "sourceBillType");
    Assert.assertArgumentNotNull(sourceBillNumber, "sourceBillNumber");
    Assert.assertArgumentNotNull(sourceBillUuid, "sourceBillUuid");
    Assert.assertArgumentNotNull(creator, "creator");
    Assert.assertArgumentNotNull(companyUuid, "companyUuid");
  }
}
