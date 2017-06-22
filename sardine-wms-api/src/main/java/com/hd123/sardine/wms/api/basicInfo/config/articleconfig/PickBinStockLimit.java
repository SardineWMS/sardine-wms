/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	PickBinStockLimit.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月13日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.config.articleconfig;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author fanqingqing
 *
 */
public class PickBinStockLimit implements Serializable {
    private static final long serialVersionUID = 6092530839721976391L;

    private BigDecimal highQty = BigDecimal.ZERO;
    private BigDecimal lowQty = BigDecimal.ZERO;
    private String pickUpQpcStr;

    public BigDecimal getHighQty() {
        return highQty;
    }

    public void setHighQty(BigDecimal highQty) {
        this.highQty = highQty;
    }

    public BigDecimal getLowQty() {
        return lowQty;
    }

    public void setLowQty(BigDecimal lowQty) {
        this.lowQty = lowQty;
    }

    public String getPickUpQpcStr() {
        return pickUpQpcStr;
    }

    public void setPickUpQpcStr(String pickUpQpcStr) {
        this.pickUpQpcStr = pickUpQpcStr;
    }

}
