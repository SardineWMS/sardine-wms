/**
 * 版权所有(C)，LHWMS项目组，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	Container.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月7日 - Jing - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.container;

import com.hd123.sardine.wms.common.entity.StandardEntity;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * @author Jing
 *
 */
public class Container extends StandardEntity {
    private static final long serialVersionUID = 5797526926737361627L;
    public static final String UNKNOWN_POSITION = "-";
    public static final String VIRTUALITY_CONTAINER = "-";

    private String barcode;
    private String companyUuid;
    private UCN containerType;
    private ContainerState state = ContainerState.STACONTAINERIDLE;
    private String position = UNKNOWN_POSITION;
    private String toPosition = UNKNOWN_POSITION;
    
    private String remark;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCompanyUuid() {
        return companyUuid;
    }

    public void setCompanyUuid(String companyUuid) {
        this.companyUuid = companyUuid;
    }

    public UCN getContainerType() {
        return containerType;
    }

    public void setContainerType(UCN containerType) {
        this.containerType = containerType;
    }

    public ContainerState getState() {
        return state;
    }

    public void setState(ContainerState state) {
        this.state = state;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getToPosition() {
        return toPosition;
    }

    public void setToPosition(String toPosition) {
        this.toPosition = toPosition;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
