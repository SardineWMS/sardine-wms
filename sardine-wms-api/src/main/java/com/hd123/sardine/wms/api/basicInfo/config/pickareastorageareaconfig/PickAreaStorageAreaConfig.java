/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	PickAreaConfig.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月13日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.config.pickareastorageareaconfig;

import java.io.Serializable;

import com.hd123.sardine.wms.common.entity.HasVersion;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * @author fanqingqing
 *
 */
public class PickAreaStorageAreaConfig implements Serializable {
    private static final long serialVersionUID = -1221210122838987521L;

    private UCN pickArea;
    private String storageArea;
    private String companyUuid;
    private long version = HasVersion.START_VERSION;

    public UCN getPickArea() {
        return pickArea;
    }

    public void setPickArea(UCN pickArea) {
        this.pickArea = pickArea;
    }

    public String getStorageArea() {
        return storageArea;
    }

    public void setStorageArea(String storageArea) {
        this.storageArea = storageArea;
    }

    public String getCompanyUuid() {
        return companyUuid;
    }

    public void setCompanyUuid(String companyUuid) {
        this.companyUuid = companyUuid;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
