/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	ReturnBill.java
 * 模块说明：	
 * 修改历史：
 * 2017年7月4日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.rtn.customerreturn;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.OperateMode;
import com.hd123.sardine.wms.common.entity.StandardEntity;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * 退仓单|实体类
 * 
 * @author yangwenzhu
 *
 */
public class ReturnBill extends StandardEntity {
    private static final long serialVersionUID = 6970133377043134212L;
    public static final String CAPTION = "退仓单";
    public static final int LENGTH_BILLNUMBER = 30;
    public static final int LENGTH_REMARK = 255;

    private String billNumber;
    private ReturnBillState state;
    private String returnNtcBillNumber;
    private UCN wrh;
    private String companyUuid;
    private UCN customer;
    private UCN returnor;
    private String totalCaseQtyStr;
    private BigDecimal totalAmount;
    private String remark;
    private OperateMode type = OperateMode.ManualBill;
    private List<ReturnBillItem> items = new ArrayList<>();

    public String getBillNumber() {
        return billNumber;
    }

    /** 单号 */
    public void setBillNumber(String billNumber) {
        Assert.assertArgumentNotNull(billNumber, "billNumber");
        Assert.assertStringNotTooLong(billNumber, LENGTH_BILLNUMBER, "billNumber");
        this.billNumber = billNumber;
    }

    public ReturnBillState getState() {
        return state;
    }

    /** 状态 */
    public void setState(ReturnBillState state) {
        Assert.assertArgumentNotNull(state, "state");
        this.state = state;
    }

    public String getReturnNtcBillNumber() {
        return returnNtcBillNumber;
    }

    /** 退仓通知单单号 */
    public void setReturnNtcBillNumber(String returnNtcBillNumber) {
        Assert.assertArgumentNotNull(returnNtcBillNumber, "retunNtcBillNumber");
        Assert.assertStringNotTooLong(returnNtcBillNumber, LENGTH_BILLNUMBER,
                "returnNtcBillNumber");
        this.returnNtcBillNumber = returnNtcBillNumber;
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

    /** 公司UUID */
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

    public UCN getReturnor() {
        return returnor;
    }

    /** 退仓员 */
    public void setReturnor(UCN returnor) {
        Assert.assertArgumentNotNull(returnor, "returnor");
        this.returnor = returnor;
    }

    public String getTotalCaseQtyStr() {
        return totalCaseQtyStr;
    }

    /** 退仓总件数 */
    public void setTotalCaseQtyStr(String totalCaseQtyStr) {
        Assert.assertArgumentNotNull(totalCaseQtyStr, "totalCaseQtyStr");
        Assert.assertStringNotTooLong(totalCaseQtyStr, LENGTH_BILLNUMBER, "totalCaseQtyStr");
        this.totalCaseQtyStr = totalCaseQtyStr;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    /** 退仓总金额 */
    public void setTotalAmount(BigDecimal totalAmount) {
        Assert.assertArgumentNotNull(totalAmount, "totalAmount");
        this.totalAmount = totalAmount;
    }

    public String getRemark() {
        return remark;
    }

    /** 说明 */
    public void setRemark(String remark) {
        Assert.assertStringNotTooLong(remark, LENGTH_REMARK, "remark");
        this.remark = remark;
    }

    public List<ReturnBillItem> getItems() {
        return items;
    }

    /** 明细 */
    public void setItems(List<ReturnBillItem> items) {
        Assert.assertArgumentNotNull(items, "items");
        this.items = items;
    }

    public OperateMode getType() {
        return type;
    }

    /** 单据类型，APP和手工单据 */
    public void setType(OperateMode type) {
        this.type = type;
    }

    public void validate() {
        Assert.assertArgumentNotNull(companyUuid, "companyUuid");
        Assert.assertArgumentNotNull(customer, "customer");
        Assert.assertArgumentNotNull(returnNtcBillNumber, "returnNtcBillNumber");
        Assert.assertArgumentNotNull(returnor, "returnor");
        Assert.assertArgumentNotNull(totalAmount, "totalAmount");
        Assert.assertArgumentNotNull(totalCaseQtyStr, "totalCaseQtyStr");
        Assert.assertArgumentNotNull(wrh, "wrh");
        Assert.assertStringNotTooLong(returnNtcBillNumber, LENGTH_BILLNUMBER,
                "returnNtcBillNumber");
        Assert.assertStringNotTooLong(totalCaseQtyStr, LENGTH_BILLNUMBER, "totalCaseQtyStr");
        Assert.assertStringNotTooLong(remark, LENGTH_REMARK, "remark");
        if (items == null || items.isEmpty())
            throw new IllegalArgumentException("明细不能为空。");

        for (int i = 0; i < items.size(); i++) {
            ReturnBillItem item = items.get(i);
            item.validate();
            for (int j = i + 1; j < items.size(); j++) {
                ReturnBillItem jItem = items.get(j);
                jItem.validate();
                if (item.getArticle().getUuid().equals(jItem.getArticle().getUuid())
                        && item.getQpcStr().equals(jItem.getQpcStr())
                        && item.getSupplier().getUuid().equals(jItem.getSupplier().getUuid()))
                    throw new IllegalArgumentException("第" + i + "行" + "与第" + j + "行商品规格重复");
            }
        }
    }

}
