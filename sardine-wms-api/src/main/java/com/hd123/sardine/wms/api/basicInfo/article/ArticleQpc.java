/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ArticleQpc.java
 * 模块说明：	
 * 修改历史：
 * 2017年1月17日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.article;

import java.math.BigDecimal;

import com.hd123.sardine.wms.common.entity.Entity;

/**
 * 商品规格
 * <p>
 * 同一商品的规格不允许重复，且只有一个默认规格
 * 
 * @author zhangsai
 *
 */
public class ArticleQpc extends Entity {
    private static final long serialVersionUID = 737395425714748306L;

    private static final String DEFAULT_MUNIT = "-";
    public static final String DEFAULT_QPCSTR = "1*1*1";

    private String articleUuid;
    private String munit = DEFAULT_MUNIT;
    private String qpcStr;
    private BigDecimal length = BigDecimal.ZERO;
    private BigDecimal width = BigDecimal.ZERO;
    private BigDecimal height = BigDecimal.ZERO;
    private BigDecimal weight = BigDecimal.ZERO;
    private boolean default_ = false;

    /** 商品uuid */
    public String getArticleUuid() {
        return articleUuid;
    }

    public void setArticleUuid(String articleUuid) {
        this.articleUuid = articleUuid;
    }

    /** 计量单位 */
    public String getMunit() {
        return munit;
    }

    public void setMunit(String munit) {
        this.munit = munit;
    }

    /** 规格，三级结构 */
    public String getQpcStr() {
        return qpcStr;
    }

    public void setQpcStr(String qpcStr) {
        this.qpcStr = qpcStr;
    }

    /** 长度，单位cm，大于等于0 */
    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    /** 宽度，单位cm，大于等于0 */
    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    /** 高度，单位cm，大于等于0 */
    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    /** 重量，单位g，大于等于0 */
    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    /** 是否默认规格 */
    public boolean isDefault_() {
        return default_;
    }

    public void setDefault_(boolean default_) {
        this.default_ = default_;
    }
}
