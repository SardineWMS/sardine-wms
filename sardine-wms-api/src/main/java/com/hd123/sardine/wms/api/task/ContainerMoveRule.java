/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ContainerMoveRule.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月13日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.task;

import java.io.Serializable;

import com.hd123.rumba.commons.lang.Assert;

/**
 * 容器移库规则
 * 
 * @author zhangsai
 *
 */
public class ContainerMoveRule implements Serializable {
  private static final long serialVersionUID = -933262632480910846L;

  private String fromContainerBarcode;
  private String fromBinCode;
  private String toContainerBarcode;
  private String toBinCode;
  private boolean containFromContainer;

  public String getFromContainerBarcode() {
    return fromContainerBarcode;
  }

  public void setFromContainerBarcode(String fromContainerBarcode) {
    this.fromContainerBarcode = fromContainerBarcode;
  }

  public String getFromBinCode() {
    return fromBinCode;
  }

  public void setFromBinCode(String fromBinCode) {
    this.fromBinCode = fromBinCode;
  }

  public String getToContainerBarcode() {
    return toContainerBarcode;
  }

  public void setToContainerBarcode(String toContainerBarcode) {
    this.toContainerBarcode = toContainerBarcode;
  }

  public String getToBinCode() {
    return toBinCode;
  }

  public void setToBinCode(String toBinCode) {
    this.toBinCode = toBinCode;
  }

  /** 包含来源容器时，移库后将来源容器放到目标容器上 ， 否则释放来源容器 */
  public boolean isContainFromContainer() {
    return containFromContainer;
  }

  public void setContainFromContainer(boolean containFromContainer) {
    this.containFromContainer = containFromContainer;
  }

  public void validate() {
    Assert.assertArgumentNotNull(fromContainerBarcode, "fromContainerBarcode");
    Assert.assertArgumentNotNull(fromBinCode, "fromBinCode");
    Assert.assertArgumentNotNull(toContainerBarcode, "toContainerBarcode");
    Assert.assertArgumentNotNull(toBinCode, "toBinCode");
  }
}
