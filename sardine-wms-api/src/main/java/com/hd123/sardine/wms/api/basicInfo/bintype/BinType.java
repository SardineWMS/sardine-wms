/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	BinType.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月6日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.bintype;

import java.math.BigDecimal;

import com.hd123.sardine.wms.common.entity.StandardEntity;

/**
 * 货位类型|实体类
 * 
 * @author yangwenzhu
 *
 */
public class BinType extends StandardEntity {
    private static final long serialVersionUID = 1L;

    private String code;
    private String name;
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal height;
    private BigDecimal plotRatio;
    private BigDecimal bearing;
    private String companyUuid;

    public String getCompanyUuid() {
      return companyUuid;
    }

    public void setCompanyUuid(String companyUuid) {
      this.companyUuid = companyUuid;
    }

    /** 货位类型 代码 */
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /** 货位类型 名称 */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** 长度 cm */
    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    /** 宽度cm */
    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    /** 高度cm */
    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    /** 容积率 百分比 */
    public BigDecimal getPlotRatio() {
        return plotRatio;
    }

    public void setPlotRatio(BigDecimal plotRatio) {
        this.plotRatio = plotRatio;
    }

    /** 承重 kg */
    public BigDecimal getBearing() {
        return bearing;
    }

    public void setBearing(BigDecimal bearing) {
        this.bearing = bearing;
    }

}
