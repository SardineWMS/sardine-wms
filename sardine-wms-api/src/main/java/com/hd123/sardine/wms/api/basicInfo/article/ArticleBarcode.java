/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ArticleBarcode.java
 * 模块说明：	
 * 修改历史：
 * 2017年1月17日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.article;

import com.hd123.sardine.wms.common.entity.Entity;

/**
 * 商品条码
 * <p>
 * 同一组织下商品条码不允许重复
 * 
 * @author zhangsai
 *
 */
public class ArticleBarcode extends Entity {
  private static final long serialVersionUID = 5831216729613386607L;

  private String articleUuid;
  private String barcode;
  private String qpcStr;

  /** 商品uuid */
  public String getArticleUuid() {
    return articleUuid;
  }

  public void setArticleUuid(String articleUuid) {
    this.articleUuid = articleUuid;
  }

  /** 商品条码 */
  public String getBarcode() {
    return barcode;
  }

  public void setBarcode(String barcode) {
    this.barcode = barcode;
  }

  /** 商品条码对应的规格，必须存在于商品规格中 */
  public String getQpcStr() {
    return qpcStr;
  }

  public void setQpcStr(String qpcStr) {
    this.qpcStr = qpcStr;
  }
}
