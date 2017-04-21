/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	TaskFilter.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月12日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.task;

import java.util.Date;

import com.hd123.sardine.wms.common.query.PageQueryDefinition;

/**
 * 指令搜索条件 Filter
 * 
 * @author zhangsai
 *
 */
public class TaskFilter extends PageQueryDefinition {
  private static final long serialVersionUID = -8044557924759822873L;

  private String taskNoLike;
  private TaskType type;
  private TaskState state;
  private String articleCode;
  private String articleUuid;
  private String fromBinCode;
  private String toBinCode;
  private String fromContainerBarcode;
  private String toContainerBarcode;
  private String qpcStr;
  private String creatorUuid;
  private String operatorUuid;
  private Date operateDateBefore;
  private Date operateDateAfter;

  public String getTaskNoLike() {
    return taskNoLike;
  }

  public void setTaskNoLike(String taskNoLike) {
    this.taskNoLike = taskNoLike;
  }

  public TaskType getType() {
    return type;
  }

  public void setType(TaskType type) {
    this.type = type;
  }

  public TaskState getState() {
    return state;
  }

  public void setState(TaskState state) {
    this.state = state;
  }

  public String getArticleCode() {
    return articleCode;
  }

  public void setArticleCode(String articleCode) {
    this.articleCode = articleCode;
  }

  public String getArticleUuid() {
    return articleUuid;
  }

  public void setArticleUuid(String articleUuid) {
    this.articleUuid = articleUuid;
  }

  public String getFromBinCode() {
    return fromBinCode;
  }

  public void setFromBinCode(String fromBinCode) {
    this.fromBinCode = fromBinCode;
  }

  public String getToBinCode() {
    return toBinCode;
  }

  public void setToBinCode(String toBinCode) {
    this.toBinCode = toBinCode;
  }

  public String getFromContainerBarcode() {
    return fromContainerBarcode;
  }

  public void setFromContainerBarcode(String fromContainerBarcode) {
    this.fromContainerBarcode = fromContainerBarcode;
  }

  public String getToContainerBarcode() {
    return toContainerBarcode;
  }

  public void setToContainerBarcode(String toContainerBarcode) {
    this.toContainerBarcode = toContainerBarcode;
  }

  public String getQpcStr() {
    return qpcStr;
  }

  public void setQpcStr(String qpcStr) {
    this.qpcStr = qpcStr;
  }

  public String getCreatorUuid() {
    return creatorUuid;
  }

  public void setCreatorUuid(String creatorUuid) {
    this.creatorUuid = creatorUuid;
  }

  public String getOperatorUuid() {
    return operatorUuid;
  }

  public void setOperatorUuid(String operatorUuid) {
    this.operatorUuid = operatorUuid;
  }

  public Date getOperateDateBefore() {
    return operateDateBefore;
  }

  public void setOperateDateBefore(Date operateDateBefore) {
    this.operateDateBefore = operateDateBefore;
  }

  public Date getOperateDateAfter() {
    return operateDateAfter;
  }

  public void setOperateDateAfter(Date operateDateAfter) {
    this.operateDateAfter = operateDateAfter;
  }

}
