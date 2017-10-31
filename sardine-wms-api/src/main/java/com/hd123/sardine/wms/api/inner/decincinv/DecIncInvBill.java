/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	DecIncInvBill.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月17日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.inner.decincinv;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.StandardEntity;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * 损溢单实体
 * 
 * @author fanqingqing
 *
 */
public class DecIncInvBill extends StandardEntity {
    private static final long serialVersionUID = -5910025909877465618L;
    public static final String CAPTION = "损溢单";

    private String billNumber;
    private DecIncInvBillType type;
    private DecIncInvBillState state;
    private String sourceBillNumber;
    private String sourceBillType;
    private UCN wrh;
    private UCN operator;
    private BigDecimal totalAmount;
    private String totalCaseQtyStr;
    private String remark;
    private String companyUuid;
    private List<DecIncInvBillItem> items = new ArrayList<>();

    /** 单号 */
    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    /** 类型 */
    public DecIncInvBillType getType() {
        return type;
    }

    public void setType(DecIncInvBillType type) {
        this.type = type;
    }

    /** 状态 */
    public DecIncInvBillState getState() {
        return state;
    }

    public void setState(DecIncInvBillState state) {
        this.state = state;
    }

    /** 仓位 */
    public UCN getWrh() {
        return wrh;
    }

    public void setWrh(UCN wrh) {
        this.wrh = wrh;
    }

    /** 损溢人 */
    public UCN getOperator() {
        return operator;
    }

    public void setOperator(UCN operator) {
        this.operator = operator;
    }

    /** 总金额 */
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    /** 备注 */
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /** 明细 */
    public List<DecIncInvBillItem> getItems() {
        return items;
    }

    public void setItems(List<DecIncInvBillItem> items) {
        this.items = items;
    }

    public String getCompanyUuid() {
        return companyUuid;
    }

    public void setCompanyUuid(String companyUuid) {
        this.companyUuid = companyUuid;
    }

    public String getTotalCaseQtyStr() {
        return totalCaseQtyStr;
    }

    public void setTotalCaseQtyStr(String totalCaseQtyStr) {
        this.totalCaseQtyStr = totalCaseQtyStr;
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

    public void validate() {
        Assert.assertArgumentNotNull(type, "type");
        Assert.assertArgumentNotNull(state, "state");
        Assert.assertArgumentNotNull(wrh, "wrh");
        Assert.assertArgumentNotNull(operator, "operator");
        Assert.assertArgumentNotNull(operator, "operator");

        if (CollectionUtils.isEmpty(items))
            throw new IllegalArgumentException("明细行不能为空");

        for (int i = 0; i < items.size(); i++) {
            DecIncInvBillItem item = items.get(i);
            if (DecIncInvBillType.Inc.equals(type) && BigDecimal.ZERO.compareTo(item.getQty()) >= 0)
                throw new IllegalArgumentException("商品" + item.getArticle().getCode() + "数量必须大于0");
            if (DecIncInvBillType.Dec.equals(type) && BigDecimal.ZERO.compareTo(item.getQty()) <= 0)
                throw new IllegalArgumentException("商品" + item.getArticle().getCode() + "数量必须小于0");
            item.validate();
            for (int j = i + 1; j < items.size(); j++) {
                DecIncInvBillItem jItem = items.get(j);
                if (item.getArticle().getUuid().equals(jItem.getArticle().getUuid())
                        && item.getQpcStr().equals(jItem.getQpcStr()))
                    throw new IllegalArgumentException(
                            "商品" + item.getArticle().getCode() + "规格" + item.getQpcStr() + "重复！");
            }
        }
    }
}
