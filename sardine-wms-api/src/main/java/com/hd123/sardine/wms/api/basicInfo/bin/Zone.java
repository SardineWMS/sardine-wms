/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	Zone.java
 * 模块说明：	
 * 修改历史：
 * 2017-1-17 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.bin;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.Entity;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * 货区
 * 
 * @author zhangsai
 * 
 */
public class Zone extends Entity {

  private static final long serialVersionUID = -3842809380184638538L;
  public static final int LENGTH_CODE = 2;
  /** 名称的最大长度限制 */
  public static final int LENGTH_NAME = 100;

  /** 说明信息的最大长度限制 */
  public static final int LENGTH_NOTE = 254;

  private String code;
  private String name;
  private UCN wrh;
  private String note;

  private String companyUuid;

  /** 货区代码 */
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    Assert.assertArgumentNotNull(code, "code");
    Assert.assertStringNotTooLong(code, LENGTH_CODE, "code");
    this.code = code;
  }

  /** 货区名称 */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    Assert.assertArgumentNotNull(name, "name");
    Assert.assertStringNotTooLong(name, LENGTH_NAME, "name");
    this.name = name;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    Assert.assertStringNotTooLong(note, LENGTH_NOTE, "note");
    this.note = note;
  }

  /** 所属仓位uuid */
  public UCN getWrh() {
    return wrh;
  }

  public void setWrh(UCN wrh) {
    this.wrh = wrh;
  }

  public String getCompanyUuid() {
    return companyUuid;
  }

  public void setCompanyUuid(String companyUuid) {
    this.companyUuid = companyUuid;
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
