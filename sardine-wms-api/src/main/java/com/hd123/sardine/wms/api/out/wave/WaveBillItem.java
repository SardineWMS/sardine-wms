/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	WaveBillItem.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月20日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.out.wave;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.out.alcntc.AlcNtcBillState;
import com.hd123.sardine.wms.common.entity.Entity;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * 波次单明细|实体
 * 
 * @author yangwenzhu
 *
 */
public class WaveBillItem extends Entity {
    private static final long serialVersionUID = 934795195807816381L;
    public static final int LENGTH_WAVEBILLUUID = 32;
    public static final int LENGTH_ALCNTCBILLNUMBER = 30;

    private String waveBillUuid;
    private int line;
    private String alcNtcBillNumber;
    private AlcNtcBillState alcNtcBillState;
    private UCN customer;

    public String getWaveBillUuid() {
        return waveBillUuid;
    }

    /** 波次单UUID */
    public void setWaveBillUuid(String waveBillUuid) {
        Assert.assertArgumentNotNull(waveBillUuid, "waveBillUuid");
        Assert.assertStringNotTooLong(waveBillUuid, LENGTH_WAVEBILLUUID, "waveBillUuid");
        this.waveBillUuid = waveBillUuid;
    }

    public int getLine() {
        return line;
    }

    /** 行号 */
    public void setLine(int line) {
        this.line = line;
    }

    public String getAlcNtcBillNumber() {
        return alcNtcBillNumber;
    }

    /** 配单单号 */
    public void setAlcNtcBillNumber(String alcNtcBillNumber) {
        Assert.assertArgumentNotNull(alcNtcBillNumber, "alcNtcBillNumber");
        Assert.assertStringNotTooLong(alcNtcBillNumber, LENGTH_ALCNTCBILLNUMBER,
                "alcNtcBillNumber");
        this.alcNtcBillNumber = alcNtcBillNumber;
    }

    public AlcNtcBillState getAlcNtcBillState() {
        return alcNtcBillState;
    }

    /** 配单状态 */
    public void setAlcNtcBillState(AlcNtcBillState alcNtcBillState) {
        Assert.assertArgumentNotNull(alcNtcBillState, "alcNtcBillState");
        this.alcNtcBillState = alcNtcBillState;
    }

    public UCN getCustomer() {
        return customer;
    }

    /** 客户 */
    public void setCustomer(UCN customer) {
        Assert.assertArgumentNotNull(customer, "customer");
        this.customer = customer;
    }

    public void validate() {
        Assert.assertArgumentNotNull(alcNtcBillState, "alcNtcBillState");
        Assert.assertArgumentNotNull(customer, "customer");
        Assert.assertArgumentNotNull(alcNtcBillNumber, "alcNtcBillNumber");
    }
}
