/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	CategoryConfig.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月13日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.config.categorystorageareaconfig;

import java.io.Serializable;

import com.hd123.sardine.wms.common.entity.HasVersion;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * @author fanqingqing
 *
 */
public class CategoryStorageAreaConfig implements Serializable, HasVersion {
    private static final long serialVersionUID = 2866454247765016900L;

    private UCN category;
    private String storageArea;
    private String companyUuid;
    private long version = HasVersion.START_VERSION;

    public UCN getCategory() {
        return category;
    }

    public void setCategory(UCN category) {
        this.category = category;
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

    @Override
    public long getVersion() {
        return version;
    }

    @Override
    public void setVersion(long version) throws UnsupportedOperationException {
        this.version = version;
    }
}
