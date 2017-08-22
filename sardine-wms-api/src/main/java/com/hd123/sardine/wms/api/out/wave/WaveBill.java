/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	WaveBill.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月20日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.out.wave;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.StandardEntity;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * 波次单单头|实体
 * 
 * @author yangwenzhu
 *
 */
public class WaveBill extends StandardEntity {
    private static final long serialVersionUID = 179933926258996264L;
    public static final String CAPTION = "波次单";
    public static final int LENGTH_BILLNUMBER = 30;
    public static final int LENGTH_REMARK = 255;

    private String billNumber;
    private UCN serialArch;
    private WaveType waveType;
    private String remark;
    private WaveBillState state;
    private String companyUuid;
    
    private List<WaveNtcItem> ntcItems = new ArrayList<WaveNtcItem>();
    private List<WavePickUpBillItem> pickItems = new ArrayList<WavePickUpBillItem>();

    public List<WaveNtcItem> getNtcItems() {
      return ntcItems;
    }

    public void setNtcItems(List<WaveNtcItem> ntcItems) {
      this.ntcItems = ntcItems;
    }

    public List<WavePickUpBillItem> getPickItems() {
      return pickItems;
    }

    public void setPickItems(List<WavePickUpBillItem> pickItems) {
      this.pickItems = pickItems;
    }

    public String getBillNumber() {
        return billNumber;
    }

    /** 单号 */
    public void setBillNumber(String billNumber) {
        Assert.assertArgumentNotNull(billNumber, "billNumber");
        Assert.assertStringNotTooLong(billNumber, LENGTH_BILLNUMBER, "billNumber");
        this.billNumber = billNumber;
    }

    public UCN getSerialArch() {
        return serialArch;
    }

    /** 线路体系 */
    public void setSerialArch(UCN serialArch) {
        Assert.assertArgumentNotNull(serialArch, "serialArch");
        this.serialArch = serialArch;
    }

    public WaveType getWaveType() {
        return waveType;
    }

    public void setWaveType(WaveType waveType) {
        this.waveType = waveType;
    }

    public String getRemark() {
        return remark;
    }

    /** 说明 */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public WaveBillState getState() {
        return state;
    }

    /** 波次单状态 */
    public void setState(WaveBillState state) {
        Assert.assertArgumentNotNull(state, "state");
        this.state = state;
    }

    public String getCompanyUuid() {
        return companyUuid;
    }

    /** 组织UUID */
    public void setCompanyUuid(String companyUuid) {
        Assert.assertArgumentNotNull(companyUuid, "companyUuid");
        this.companyUuid = companyUuid;
    }

    public void validate() {
        Assert.assertArgumentNotNull(ntcItems, "ntcItems");
        Assert.assertArgumentNotNull(serialArch, "serialArch");
        Assert.assertArgumentNotNull(waveType, "waveType");
        Assert.assertArgumentNotNull(companyUuid, "companyUuid");

        if (CollectionUtils.isEmpty(ntcItems))
            throw new IllegalArgumentException("明细行不能为空");

        for (int i = 0; i < ntcItems.size(); i++) {
            WaveNtcItem item = ntcItems.get(i);
            item.validate();
            for (int j = i + 1; j < ntcItems.size(); j++) {
              WaveNtcItem jItem = ntcItems.get(j);
                jItem.validate();
                if (item.getNtcBillNumber().equals(jItem.getNtcBillNumber())) {
                    throw new IllegalArgumentException("第" + i + "行与第" + j + "行，明细重复");
                }
            }
        }

    }

}
