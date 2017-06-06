/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	DeliveryArticleInfo.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月31日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.api.out.alcntc;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author yangwenzhu
 *
 */
public class DeliveryArticleInfo implements Serializable {
    private static final long serialVersionUID = 7911289857607220194L;

    private String itemUuid;
    private BigDecimal qty = BigDecimal.ZERO;

    public String getItemUuid() {
        return itemUuid;
    }

    public void setItemUuid(String itemUuid) {
        this.itemUuid = itemUuid;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

}
