/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	AcceptanceBill.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月5日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.out.acceptance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.out.alcntc.DeliverySystem;
import com.hd123.sardine.wms.common.entity.StandardEntity;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * 领用单实体
 * 
 * @author fanqingqing
 *
 */
public class AcceptanceBill extends StandardEntity {
    private static final long serialVersionUID = -3175414367770777955L;

    private String billNumber;
    private String companyUuid;
    private AcceptanceBillState state = AcceptanceBillState.Initial;
    private String sourceBillNumber;
    private String sourceBillType;
    private UCN wrh;
    private UCN customer;
    private String acceptanceReason;
    private DeliverySystem deliverySystem;
    private String deliveryType;
    private String totalCaseQtyStr="0";
    private String alcTotalCaseQtyStr="0";
    private BigDecimal totalAmount=BigDecimal.ZERO;
    private String remark;
    private List<AcceptanceBillItem> items = new ArrayList<>();

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public AcceptanceBillState getState() {
        return state;
    }

    public void setState(AcceptanceBillState state) {
        this.state = state;
    }

    public String getSourceBillNumber() {
        return sourceBillNumber;
    }

    public void setSourceBillNumber(String sourceBillNumber) {
        this.sourceBillNumber = sourceBillNumber;
    }

    public String getSourceBillType() {
        return sourceBillType;
    }

    public void setSourceBillType(String sourceBillType) {
        this.sourceBillType = sourceBillType;
    }

    public UCN getWrh() {
        return wrh;
    }

    public void setWrh(UCN wrh) {
        this.wrh = wrh;
    }

    public UCN getCustomer() {
        return customer;
    }

    public void setCustomer(UCN customer) {
        this.customer = customer;
    }

    public String getAcceptanceReason() {
        return acceptanceReason;
    }

    public void setAcceptanceReason(String acceptanceReason) {
        this.acceptanceReason = acceptanceReason;
    }

    public DeliverySystem getDeliverySystem() {
        return deliverySystem;
    }

    public void setDeliverySystem(DeliverySystem deliverySystem) {
        this.deliverySystem = deliverySystem;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getTotalCaseQtyStr() {
        return totalCaseQtyStr;
    }

    public void setTotalCaseQtyStr(String totalCaseQtyStr) {
        this.totalCaseQtyStr = totalCaseQtyStr;
    }

    public String getAlcTotalCaseQtyStr() {
        return alcTotalCaseQtyStr;
    }

    public void setAlcTotalCaseQtyStr(String alcTotalCaseQtyStr) {
        this.alcTotalCaseQtyStr = alcTotalCaseQtyStr;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
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

    public List<AcceptanceBillItem> getItems() {
        return items;
    }

    public void setItems(List<AcceptanceBillItem> items) {
        this.items = items;
    }

    public void validate() {
        Assert.assertArgumentNotNull(state, "state");
        Assert.assertArgumentNotNull(wrh, "wrh");
        Assert.assertArgumentNotNull(customer, "customer");
        Assert.assertArgumentNotNull(acceptanceReason, "acceptanceReason");
        Assert.assertArgumentNotNull(deliveryType, "deliveryType");
        Assert.assertArgumentNotNull(items, "items");

        for (int i = 0; i < items.size(); i++) {
            AcceptanceBillItem item = items.get(i);
            item.validate();
            for (int j = i + 1; j < items.size(); j++) {
                AcceptanceBillItem jItem = items.get(j);
                if (item.getArticle().getUuid().equals(jItem.getArticle().getUuid())
                        && item.getQpcStr().equals(jItem.getQpcStr()))
                    throw new IllegalArgumentException(
                            "商品" + item.getArticle().getCode() + "规格" + item.getQpcStr() + "重复！");
            }
        }
    }
}
