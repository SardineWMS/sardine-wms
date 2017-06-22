/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	TaskAreaConfig.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月13日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.api.basicInfo.config.taskareaconfig;

import java.io.Serializable;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.entity.HasVersion;
import com.hd123.sardine.wms.common.entity.UCN;

/**
 * @author fanqingqing
 *
 */
public class TaskAreaConfig implements Serializable, HasVersion {
    private static final long serialVersionUID = -2926067267045961787L;

    private UCN operator;
    private String taskArea;
    private String companyUuid;
    private long version = HasVersion.START_VERSION;
    private String uuid;

    public UCN getOperator() {
        return operator;
    }

    public void setOperator(UCN operator) {
        this.operator = operator;
    }

    public String getTaskArea() {
        return taskArea;
    }

    public void setTaskArea(String taskArea) {
        this.taskArea = taskArea;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void validate() {
        Assert.assertArgumentNotNull(operator, "operator");
        Assert.assertArgumentNotNull(operator.getUuid(), "operator.uuid");
        Assert.assertArgumentNotNull(operator.getCode(), "operator.code");
        Assert.assertArgumentNotNull(operator.getName(), "operator.name");
        Assert.assertArgumentNotNull(taskArea, "taskArea");
    }

}
