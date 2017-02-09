/**
 * 版权所有(C)，LHWMS项目组，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	Sequenct.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月7日 - Jing - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.seq;

import java.io.Serializable;

/**
 * @author Jing
 *
 */
public class Sequence implements Serializable {
    private static final long serialVersionUID = 6302126507602061812L;

    private String seqName;
    private String companyUuid;
    private int currentValue;
    private int increment;

    /** 序列名称 */
    public String getSeqName() {
        return seqName;
    }

    public void setSeqName(String seqName) {
        this.seqName = seqName;
    }

    /** 组织UUID */
    public String getCompanyUuid() {
        return companyUuid;
    }

    public void setCompanyUuid(String companyUuid) {
        this.companyUuid = companyUuid;
    }

    /** 当前值 */
    public int getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }

    /** 幅度 */
    public int getIncrement() {
        return increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }
}
