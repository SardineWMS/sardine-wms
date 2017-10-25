/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	SerialArch.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月14日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.tms.serialarch;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.StandardEntity;

/**
 * 线路体系|实体
 * 
 * @author yangwenzhu
 *
 */
public class SerialArch extends StandardEntity {
  private static final long serialVersionUID = 856820675468969516L;
  public static final String DEFAULT_UUID = "-";
  public static final String CAPTION = "线路体系";
  private static final int LENGTH_CODE = 30;
  private static final int LENGTH_NAME = 100;

  private String code;
  private String name;
  private String companyUuid;

  public String getCode() {
    return code;
  }

  /** 代码 */
  public void setCode(String code) {
    Assert.assertArgumentNotNull(code, "code");
    Assert.assertStringNotTooLong(code, LENGTH_CODE, "code");
    this.code = code;
  }

  public String getName() {
    return name;
  }

  /** 名称 */
  public void setName(String name) {
    Assert.assertArgumentNotNull(name, "name");
    Assert.assertStringNotTooLong(name, LENGTH_NAME, "name");
    this.name = name;
  }

  public String getCompanyUuid() {
    return companyUuid;
  }

  public void setCompanyUuid(String companyUuid) {
    Assert.assertArgumentNotNull(companyUuid, "companyUuid");
    this.companyUuid = companyUuid;
  }

  public void validate() {
    Assert.assertArgumentNotNull(code, "code");
    Assert.assertArgumentNotNull(name, "name");
    Assert.assertArgumentNotNull(companyUuid, "companyUuid");
  }

  public String toFriendString() {
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    sb.append(code);
    sb.append("]");
    sb.append(name);
    return sb.toString();
  }

}
