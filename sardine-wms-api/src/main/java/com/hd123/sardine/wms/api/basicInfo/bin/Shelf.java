/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	Shelf.java
 * 模块说明：	
 * 修改历史：
 * 2017-1-17 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.bin;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.Entity;

/**
 * 货架
 * 
 * @author zhangsai
 * 
 */
public class Shelf extends Entity {

  private static final long serialVersionUID = 7255972784883088358L;
  public static final int LENGTH_CODE = 6;
  /** 说明信息的最大长度限制 */
  public static final int LENGTH_NOTE = 254;
  private String code;
  private String pathUuid;
  private String note;

  private String companyUuid;

  /** 货架代码 */
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    Assert.assertStringNotTooLong(code, LENGTH_CODE, "code");
    this.code = code;
  }

  /** 所属货道uuid */
  public String getPathUuid() {
    return pathUuid;
  }

  public void setPathUuid(String pathUuid) {
    Assert.assertArgumentNotNull(pathUuid, "pathUuid");

    this.pathUuid = pathUuid;
  }

  /** 说明 */
  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    Assert.assertStringNotTooLong(note, LENGTH_NOTE, "note");
    this.note = note;
  }

  public String getCompanyUuid() {
    return companyUuid;
  }

  public void setCompanyUuid(String companyUuid) {
    this.companyUuid = companyUuid;
  }
}
