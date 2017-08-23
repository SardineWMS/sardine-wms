/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	SupplierReturnBill.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月21日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.rtn.returnsupplier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.tms.shipbill.OperateMethod;
import com.hd123.sardine.wms.common.entity.StandardEntity;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.validator.Validator;

/**
 * 供应商退货单实体
 * 
 * @author fanqingqing
 *
 */
public class ReturnSupplierBill extends StandardEntity implements Validator {
    private static final long serialVersionUID = 8480910581788078148L;
    public static final String CAPTION = "供应商退货单";

    private String companyUuid;
    private String billNumber;
    private String rtnSupplierNtcBillNumber;
    private UCN supplier;
    private UCN wrh;
    private UCN returner;
    private OperateMethod method = OperateMethod.ManualBill;
    private ReturnSupplierBillState state = ReturnSupplierBillState.Initial;
    private String totalCaseQty;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private Date returnSupplierDate;
    private String remark;
    private List<ReturnSupplierBillItem> items = new ArrayList<>();

    /** 公司 */
    public String getCompanyUuid() {
        return companyUuid;
    }

    public void setCompanyUuid(String companyUuid) {
        this.companyUuid = companyUuid;
    }

    /** 单号 */
    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    /** 供应商退货通知单 */
    public String getRtnSupplierNtcBillNumber() {
        return rtnSupplierNtcBillNumber;
    }

    public void setRtnSupplierNtcBillNumber(String rtnSupplierNtcBillNumber) {
        this.rtnSupplierNtcBillNumber = rtnSupplierNtcBillNumber;
    }

    /** 供应商 */
    public UCN getSupplier() {
        return supplier;
    }

    public void setSupplier(UCN supplier) {
        this.supplier = supplier;
    }

    /** 仓位 */
    public UCN getWrh() {
        return wrh;
    }

    public void setWrh(UCN wrh) {
        this.wrh = wrh;
    }

    /** 退货员 */
    public UCN getReturner() {
        return returner;
    }

    public void setReturner(UCN returner) {
        this.returner = returner;
    }

    /** 操作方式 */
    public OperateMethod getMethod() {
        return method;
    }

    public void setMethod(OperateMethod method) {
        this.method = method;
    }

    /** 状态 */
    public ReturnSupplierBillState getState() {
        return state;
    }

    public void setState(ReturnSupplierBillState state) {
        this.state = state;
    }

    /** 总件数 */
    public String getTotalCaseQty() {
        return totalCaseQty;
    }

    public void setTotalCaseQty(String totalCaseQty) {
        this.totalCaseQty = totalCaseQty;
    }

    /** 总金额 */
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    /** 退货日期 */
    public Date getReturnSupplierDate() {
        return returnSupplierDate;
    }

    public void setReturnSupplierDate(Date returnSupplierDate) {
        this.returnSupplierDate = returnSupplierDate;
    }

    /** 说明 */
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /** 明细 */
    public List<ReturnSupplierBillItem> getItems() {
        return items;
    }

    public void setItems(List<ReturnSupplierBillItem> items) {
        this.items = items;
    }

    @Override
    public void validate() {
        Assert.assertArgumentNotNull(companyUuid, "companyUuid");
        Assert.assertArgumentNotNull(rtnSupplierNtcBillNumber, "rtnSupplierNtcBillNumber");
        Assert.assertArgumentNotNull(supplier, "supplier");
        Assert.assertArgumentNotNull(supplier.getUuid(), "supplier.uuid");
        Assert.assertArgumentNotNull(supplier.getCode(), "supplier.code");
        Assert.assertArgumentNotNull(supplier.getName(), "supplier.name");
        Assert.assertArgumentNotNull(wrh, "wrh");
        Assert.assertArgumentNotNull(wrh.getUuid(), "wrh.uuid");
        Assert.assertArgumentNotNull(wrh.getCode(), "wrh.code");
        Assert.assertArgumentNotNull(wrh.getName(), "wrh.name");
        Assert.assertArgumentNotNull(returner, "returner");
        Assert.assertArgumentNotNull(returner.getUuid(), "returner.uuid");
        Assert.assertArgumentNotNull(returner.getCode(), "returner.code");
        Assert.assertArgumentNotNull(returner.getName(), "returner.name");
    }

}
