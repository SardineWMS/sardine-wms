/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	VehicleType.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月6日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.tms.vehicletype;

import java.math.BigDecimal;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.StandardEntity;

/**
 * 车型|实体
 * 
 * @author yangwenzhu
 *
 */
public class VehicleType extends StandardEntity {
    private static final long serialVersionUID = 3670008280320059858L;
    public static final String CAPTION = "车型";

    private static final int LENGTH_CODE = 30;
    private static final int LENGTH_NAME = 100;

    private String code;
    private String name;
    private String companyUuid;
    private BigDecimal bearWeight;
    private BigDecimal weight;
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal height;
    private BigDecimal volume;
    private BigDecimal bearVolume;

    /** 代码 */
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        Assert.assertArgumentNotNull(code, "code");
        Assert.assertStringNotTooLong(code, LENGTH_CODE, "code");
        this.code = code;
    }

    /** 名称 */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        Assert.assertArgumentNotNull(name, "name");
        Assert.assertStringNotTooLong(name, LENGTH_NAME, "name");
        this.name = name;
    }

    public String getCompanyUuid() {
        return companyUuid;
    }

    /** 公司 */
    public void setCompanyUuid(String companyUuid) {
        Assert.assertArgumentNotNull(companyUuid, "companyUuid");
        this.companyUuid = companyUuid;
    }

    /** 承重 */
    public BigDecimal getBearWeight() {
        return bearWeight;
    }

    public void setBearWeight(BigDecimal bearWeight) {
        Assert.assertArgumentNotNull(bearWeight, "bearWeight");
        if (BigDecimal.ZERO.compareTo(bearWeight) >= 0)
            throw new IllegalArgumentException("车辆承重必须大于0");
        this.bearWeight = bearWeight;
    }

    /** 自重 */
    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        Assert.assertArgumentNotNull(weight, "weight");
        if (BigDecimal.ZERO.compareTo(weight) >= 0)
            throw new IllegalArgumentException("车辆自重必须大于0");
        this.weight = weight;
    }

    /** 长 */
    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        Assert.assertArgumentNotNull(length, "length");
        if (BigDecimal.ZERO.compareTo(length) >= 0)
            throw new IllegalArgumentException("车辆长度必须大于0");
        this.length = length;
    }

    /** 宽 */
    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        Assert.assertArgumentNotNull(width, "width");
        if (BigDecimal.ZERO.compareTo(width) >= 0)
            throw new IllegalArgumentException("车辆宽度必须大于0");
        this.width = width;
    }

    /** 高 */
    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        Assert.assertArgumentNotNull(height, "height");
        if (BigDecimal.ZERO.compareTo(height) >= 0)
            throw new IllegalArgumentException("车辆高度必须大于0");
        this.height = height;
    }

    /** 体积 */
    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        Assert.assertArgumentNotNull(volume, "volume");
        if (BigDecimal.ZERO.compareTo(volume) >= 0)
            throw new IllegalArgumentException("车辆体积必须大于0");
        this.volume = volume;
    }

    /** 容积 */
    public BigDecimal getBearVolume() {
        return bearVolume;
    }

    public void setBearVolume(BigDecimal bearVolume) {
        Assert.assertArgumentNotNull(bearVolume, "bearVolume");
        if (BigDecimal.ZERO.compareTo(bearVolume) >= 0)
            throw new IllegalArgumentException("车辆容积必须大于0");
        this.bearVolume = bearVolume;
    }

    public void validate() {
        Assert.assertArgumentNotNull(code, "code");
        Assert.assertArgumentNotNull(name, "name");
        Assert.assertArgumentNotNull(bearVolume, "bearVolume");
        Assert.assertArgumentNotNull(bearWeight, "bearWeight");
        Assert.assertArgumentNotNull(height, "height");
        Assert.assertArgumentNotNull(length, "length");
        Assert.assertArgumentNotNull(volume, "volume");
        Assert.assertArgumentNotNull(weight, "weight");
        Assert.assertArgumentNotNull(width, "width");
        Assert.assertArgumentNotNull(companyUuid, "companyUuid");
    }

}
