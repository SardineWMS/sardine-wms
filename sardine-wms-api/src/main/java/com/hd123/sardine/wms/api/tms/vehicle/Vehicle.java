/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	Vehicle.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月6日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.tms.vehicle;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.StandardEntity;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * 车辆|实体
 * 
 * @author yangwenzhu
 *
 */
public class Vehicle extends StandardEntity {
    private static final long serialVersionUID = 8000399501692816578L;
    public static final String CAPTION = "车辆";

    private static final int LENGTH_CODE = 30;

    private String code;
    private String vehicleNo;
    private VehicleState state;
    private UCN vehicleType;
    private UCN carrier;
    private String remark;
    private String companyUuid;

    /** 代码 */
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        Assert.assertArgumentNotNull(code, "code");
        Assert.assertStringNotTooLong(code, LENGTH_CODE, "code");
        this.code = code;
    }

    /** 车牌号 */
    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        Assert.assertArgumentNotNull(vehicleNo, "vehicleNo");
        Assert.assertStringNotTooLong(vehicleNo, LENGTH_CODE, "vehicleNo");
        this.vehicleNo = vehicleNo;
    }

    /** 状态 */
    public VehicleState getState() {
        return state;
    }

    public void setState(VehicleState state) {
        Assert.assertArgumentNotNull(state, "state");
        this.state = state;
    }

    /** 车型 */
    public UCN getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(UCN vehicleType) {
        Assert.assertArgumentNotNull(vehicleType, "vehicleType");
        this.vehicleType = vehicleType;
    }

    /** 承运商 */
    public UCN getCarrier() {
        return carrier;
    }

    public void setCarrier(UCN carrier) {
        Assert.assertArgumentNotNull(carrier, "carrier");
        this.carrier = carrier;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCompanyUuid() {
        return companyUuid;
    }

    public void setCompanyUuid(String companyUuid) {
        Assert.assertArgumentNotNull(companyUuid, "companyUuid");
        this.companyUuid = companyUuid;
    }

    public void validate() {
        Assert.assertArgumentNotNull(carrier, "carrier");
        Assert.assertArgumentNotNull(state, "state");
        Assert.assertArgumentNotNull(vehicleNo, "vehicleNo");
        Assert.assertArgumentNotNull(vehicleType, "vehicleType");
        Assert.assertArgumentNotNull(code, "code");
        Assert.assertArgumentNotNull(companyUuid, "companyUuid");
    }

}
