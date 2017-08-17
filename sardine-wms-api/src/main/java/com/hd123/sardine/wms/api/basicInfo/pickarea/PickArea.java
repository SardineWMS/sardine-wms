/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	PickArea.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月12日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.pickarea;

import java.math.BigDecimal;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.OperateMode;
import com.hd123.sardine.wms.common.entity.StandardEntity;

/**
 * 拣货分区|实体
 * 
 * @author yangwenzhu
 *
 */
public class PickArea extends StandardEntity {
    private static final long serialVersionUID = -2633283602799498732L;
    public static final String CAPTION = "拣货分区";
    public static final int LENGTH_CODE = 30;
    public static final int LENGTH_NAME = 100;

    private String code;
    private String name;
    private String binScope;
    private String storageArea;
    private OperateMode pickMode;
    private BigDecimal pickVolume;
    private OperateMode rplMode;
    private RplQtyMode rplQtyMode;
    private String companyUuid;
    private String remark;

    public String getCode() {
        return code;
    }

    /** 拣货分区代码 */
    public void setCode(String code) {
        Assert.assertArgumentNotNull(code, "code");
        Assert.assertStringNotTooLong(code, LENGTH_CODE, "code");
        this.code = code;
    }

    public String getName() {
        return name;
    }

    /** 拣货分区名称 */
    public void setName(String name) {
        Assert.assertArgumentNotNull(name, "name");
        Assert.assertStringNotTooLong(name, LENGTH_NAME, "name");
        this.name = name;
    }

    public String getBinScope() {
        return binScope;
    }

    /** 货位范围 */
    public void setBinScope(String binScope) {
        Assert.assertArgumentNotNull(binScope, "binScope");
        Assert.assertStringNotTooLong(binScope, LENGTH_NAME, "binScope");
        this.binScope = binScope;
    }

    public String getStorageArea() {
        return storageArea;
    }

    /** 存储区域 */
    public void setStorageArea(String storageArea) {
        Assert.assertStringNotTooLong(storageArea, LENGTH_NAME, "storageStock");
        this.storageArea = storageArea;
    }

    public OperateMode getPickMode() {
        return pickMode;
    }

    /** 拣货方式 */
    public void setPickMode(OperateMode pickMode) {
        Assert.assertArgumentNotNull(pickMode, "pickMode");
        this.pickMode = pickMode;
    }

    public BigDecimal getPickVolume() {
        return pickVolume;
    }

    /** 拣货分单体积 */
    public void setPickVolume(BigDecimal pickVolume) {
        Assert.assertArgumentNotNull(pickVolume, "pickVolume");
        this.pickVolume = pickVolume;
    }

    public OperateMode getRplMode() {
        return rplMode;
    }

    /** 补货方式 */
    public void setRplMode(OperateMode rplMode) {
        Assert.assertArgumentNotNull(rplMode, "rplMode");
        this.rplMode = rplMode;
    }

    public RplQtyMode getRplQtyMode() {
        return rplQtyMode;
    }

    /** 补货量模式 */
    public void setRplQtyMode(RplQtyMode rplQtyMode) {
        Assert.assertArgumentNotNull(rplQtyMode, "rplQtyMode");
        this.rplQtyMode = rplQtyMode;
    }

    public String getCompanyUuid() {
        return companyUuid;
    }

    public void setCompanyUuid(String companyUuid) {
        this.companyUuid = companyUuid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void validate() {
        Assert.assertArgumentNotNull(code, "code");
        Assert.assertArgumentNotNull(name, "name");
        Assert.assertArgumentNotNull(binScope, "binScope");
        Assert.assertArgumentNotNull(pickMode, "pickMode");
        Assert.assertArgumentNotNull(pickVolume, "pickVolume");
        Assert.assertArgumentNotNull(rplMode, "rplMode");
        Assert.assertArgumentNotNull(rplQtyMode, "rplQtyMode");
    }

}
