/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ContainerType.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月6日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.containertype;

import java.math.BigDecimal;

import com.hd123.sardine.wms.common.entity.StandardEntity;

/**
 * @author fanqingqing
 *
 */
public class ContainerType extends StandardEntity {
    private static final long serialVersionUID = 536894597052752135L;

    private String code;
    private String name;
    private String barCodePrefix;
    private int barCodeLength;
    private BigDecimal inLength;
    private BigDecimal inWidth;
    private BigDecimal inHeight;
    private BigDecimal outLength;
    private BigDecimal outWidth;
    private BigDecimal outHeight;
    private BigDecimal weight;
    private BigDecimal bearingWeight;
    private boolean ship;
    private BarCodeType barCodeType = BarCodeType.FOREVER;
    private BigDecimal rate;
    private String companyUuid;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** 容器条码前缀 */
    public String getBarCodePrefix() {
        return barCodePrefix;
    }

    public void setBarCodePrefix(String barCodePrefix) {
        this.barCodePrefix = barCodePrefix;
    }

    /** 条码长度 */
    public int getBarCodeLength() {
        return barCodeLength;
    }

    public void setBarCodeLength(int barCodeLength) {
        this.barCodeLength = barCodeLength;
    }

    /** 内长 */
    public BigDecimal getInLength() {
        return inLength;
    }

    public void setInLength(BigDecimal inLength) {
        this.inLength = inLength;
    }

    /** 内宽 */
    public BigDecimal getInWidth() {
        return inWidth;
    }

    public void setInWidth(BigDecimal inWidth) {
        this.inWidth = inWidth;
    }

    /** 内高 */
    public BigDecimal getInHeight() {
        return inHeight;
    }

    public void setInHeight(BigDecimal inHeight) {
        this.inHeight = inHeight;
    }

    /** 外长 */
    public BigDecimal getOutLength() {
        return outLength;
    }

    public void setOutLength(BigDecimal outLength) {
        this.outLength = outLength;
    }

    /** 外宽 */
    public BigDecimal getOutWidth() {
        return outWidth;
    }

    public void setOutWidth(BigDecimal outWidth) {
        this.outWidth = outWidth;
    }

    /** 外高 */
    public BigDecimal getOutHeight() {
        return outHeight;
    }

    public void setOutHeight(BigDecimal outHeight) {
        this.outHeight = outHeight;
    }

    /** 自重 */
    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    /** 称重 */
    public BigDecimal getBearingWeight() {
        return bearingWeight;
    }

    public void setBearingWeight(BigDecimal bearingWeight) {
        this.bearingWeight = bearingWeight;
    }

    /** 是否随处 */
    public boolean isShip() {
        return ship;
    }

    public void setShip(boolean ship) {
        this.ship = ship;
    }

    /** 条码类型 */
    public BarCodeType getBarCodeType() {
        return barCodeType;
    }

    public void setBarCodeType(BarCodeType barCodeType) {
        this.barCodeType = barCodeType;
    }

    /** 容积率 */
    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    /** 公司uuid */
    public String getCompanyUuid() {
        return companyUuid;
    }

    public void setCompanyUuid(String companyUuid) {
        this.companyUuid = companyUuid;
    }
    
}
