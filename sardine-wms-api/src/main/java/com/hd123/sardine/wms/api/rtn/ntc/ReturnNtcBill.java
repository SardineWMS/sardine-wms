/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ReturnNtcBill.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月27日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.rtn.ntc;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.StandardEntity;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * 退仓通知单|实体
 * 
 * @author yangwenzhu
 *
 */
public class ReturnNtcBill extends StandardEntity {
    private static final long serialVersionUID = 8605463334975197337L;

    public static final String CATPION = "退仓通知单";

    public static final int LENGTH_CODE = 30;
    public static final int LENGTH_NAME = 100;
    public static final int LENGTH_REMARK = 255;

    private String billNumber;
    private ReturnNtcBillState state = ReturnNtcBillState.initial;
    private String sourceBillType;
    private String sourceBillNumber;
    private UCN wrh;
    private String companyUuid;
    private UCN customer;
    private Date returnDate;
    private String totalCaseQtyStr;
    private String totalReturnedCaseQtyStr;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private BigDecimal totalReturnedAmount = BigDecimal.ZERO;
    private String remark;

    private List<ReturnNtcBillItem> items = new ArrayList<>();

    public String getBillNumber() {
        return billNumber;
    }

    /** 单号 */
    public void setBillNumber(String billNumber) {
        Assert.assertArgumentNotNull(billNumber, "billNumber");
        Assert.assertStringNotTooLong(billNumber, LENGTH_CODE, "billNumber");
        this.billNumber = billNumber;
    }

    public ReturnNtcBillState getState() {
        return state;
    }

    /** 状态 */
    public void setState(ReturnNtcBillState state) {
        Assert.assertArgumentNotNull(state, "state");
        this.state = state;
    }

    public String getSourceBillType() {
        return sourceBillType;
    }

    /** 来源单据类型 */
    public void setSourceBillType(String sourceBillType) {
        Assert.assertStringNotTooLong(sourceBillType, LENGTH_NAME, "sourceBillType");
        this.sourceBillType = sourceBillType;
    }

    public String getSourceBillNumber() {
        return sourceBillNumber;
    }

    /** 来源单据单号 */
    public void setSourceBillNumber(String sourceBillNumber) {
        Assert.assertStringNotTooLong(sourceBillNumber, LENGTH_CODE, "sourceBillNumber");
        this.sourceBillNumber = sourceBillNumber;
    }

    public UCN getWrh() {
        return wrh;
    }

    /** 仓位 */
    public void setWrh(UCN wrh) {
        Assert.assertArgumentNotNull(wrh, "wrh");
        this.wrh = wrh;
    }

    public String getCompanyUuid() {
        return companyUuid;
    }

    /** 组织UUID */
    public void setCompanyUuid(String companyUuid) {
        Assert.assertArgumentNotNull(companyUuid, "companyUuid");
        this.companyUuid = companyUuid;
    }

    public UCN getCustomer() {
        return customer;
    }

    /** 客户 */
    public void setCustomer(UCN customer) {
        Assert.assertArgumentNotNull(customer, "customer");
        this.customer = customer;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    /** 退货时间 */
    public void setReturnDate(Date returnDate) {
        Assert.assertArgumentNotNull(returnDate, "returnDate");
        this.returnDate = returnDate;
    }

    public String getTotalCaseQtyStr() {
        return totalCaseQtyStr;
    }

    /** 退仓总件数 */
    public void setTotalCaseQtyStr(String totalCaseQtyStr) {
        Assert.assertArgumentNotNull(totalCaseQtyStr, "totalCaseQtyStr");
        Assert.assertStringNotTooLong(totalCaseQtyStr, LENGTH_CODE, "totalCaseQtyStr");
        this.totalCaseQtyStr = totalCaseQtyStr;
    }

    public String getTotalReturnedCaseQtyStr() {
        return totalReturnedCaseQtyStr;
    }

    /** 已退总件数 */
    public void setTotalReturnedCaseQtyStr(String totalReturnedCaseQtyStr) {
        Assert.assertArgumentNotNull(totalReturnedCaseQtyStr, "totalReturnedCaseQtyStr");
        Assert.assertStringNotTooLong(totalReturnedCaseQtyStr, LENGTH_CODE,
                "totalReturnedCaseQtyStr");
        this.totalReturnedCaseQtyStr = totalReturnedCaseQtyStr;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    /** 退仓总金额 */
    public void setTotalAmount(BigDecimal totalAmount) {
        Assert.assertArgumentNotNull(totalAmount, "totalAmount");
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalReturnedAmount() {
        return totalReturnedAmount;
    }

    /** 已退总金额 */
    public void setTotalReturnedAmount(BigDecimal totalReturnedAmount) {
        Assert.assertArgumentNotNull(totalReturnedAmount, "totalReturnedAmount");
        this.totalReturnedAmount = totalReturnedAmount;
    }

    public String getRemark() {
        return remark;
    }

    /** 说明 */
    public void setRemark(String remark) {
        Assert.assertStringNotTooLong(remark, LENGTH_REMARK, "remark");
        this.remark = remark;
    }

    public List<ReturnNtcBillItem> getItems() {
        return items;
    }

    /** 退仓单明细 */
    public void setItems(List<ReturnNtcBillItem> items) {
        Assert.assertArgumentNotNull(items, "items");
        this.items = items;
    }

    public void validate() {
        Assert.assertArgumentNotNull(companyUuid, "companyUuid");
        Assert.assertArgumentNotNull(customer, "customer");
        Assert.assertArgumentNotNull(items, "items");
        Assert.assertArgumentNotNull(returnDate, "returnDate");
        Assert.assertArgumentNotNull(totalAmount, "totalAmount");
        Assert.assertArgumentNotNull(totalCaseQtyStr, "totalCaseQtyStr");
        Assert.assertArgumentNotNull(wrh, "wrh");

        if (CollectionUtils.isEmpty(items))
            throw new IllegalArgumentException("明细行不能为空");
        for (int i = 0; i < items.size(); i++) {
            ReturnNtcBillItem item = items.get(i);
            item.validate();
            for (int j = i + 1; j < items.size(); j++) {
                ReturnNtcBillItem jItem = items.get(j);
                jItem.validate();
                if (item.getArticle().getUuid().equals(jItem.getArticle().getUuid())
                        && item.getQpcStr().equals(jItem.getQpcStr()))
                    throw new IllegalArgumentException(
                            MessageFormat.format("第{0}行和第{1}行，商品“{2}”，规格“{3}”不能重复", i, j,
                                    item.getArticle().getName(), item.getQpcStr()));
            }
        }
    }
}
