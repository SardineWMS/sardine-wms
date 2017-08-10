/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2015，所有权利保留。
 * 
 * 项目名：	wms-wms-api
 * 文件名：	StockShiftTarget.java
 * 模块说明：	
 * 修改历史：
 * 2015-11-5 - Gao JingYu - 创建。
 */
package com.hd123.sardine.wms.api.stock;

import java.io.Serializable;

import com.hd123.sardine.wms.common.validator.Validator;

/**
 * 库存转移目标。
 * <p>
 * 空值，将使用库存原值。
 * 
 * @author Gao JingYu
 */
public class StockShiftTarget implements Serializable, Validator {
  private static final long serialVersionUID = -8665333335415536404L;

  private StockState state;
  private String binCode;
  private String containerBarCode;

  public static StockShiftTarget targetState(StockState state) {
    StockShiftTarget result = new StockShiftTarget();
    result.setState(state);
    return result;
  }

  public static StockShiftTarget targetBin(String binCode) {
    StockShiftTarget result = new StockShiftTarget();
    result.setBinCode(binCode);
    return result;
  }

  public static StockShiftTarget targetContainer(String containerBarCode) {
    StockShiftTarget result = new StockShiftTarget();
    result.setContainerBarCode(containerBarCode);
    return result;
  }

  public static StockShiftTarget target(String binCode, String containerBarCode) {
    StockShiftTarget result = new StockShiftTarget();
    result.setBinCode(binCode);
    result.setContainerBarCode(containerBarCode);
    return result;
  }

  public static StockShiftTarget target(String binCode, String containerBarCode, StockState state) {
    StockShiftTarget result = new StockShiftTarget();
    result.setBinCode(binCode);
    result.setContainerBarCode(containerBarCode);
    result.setState(state);
    return result;
  }

  public StockState getState() {
    return state;
  }

  public void setState(StockState state) {
    this.state = state;
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

  @Override
  public void validate() {
    if (binCode == null && containerBarCode == null && state == null) {
      throw new IllegalArgumentException("非法库存目标");
    }
  }
}
