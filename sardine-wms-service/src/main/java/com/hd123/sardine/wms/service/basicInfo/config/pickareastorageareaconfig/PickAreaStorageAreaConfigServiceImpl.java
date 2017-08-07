/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	PickAreaStorageAreaConfigServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月19日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.service.basicInfo.config.pickareastorageareaconfig;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.config.pickareastorageareaconfig.PickAreaStorageAreaConfig;
import com.hd123.sardine.wms.api.basicInfo.config.pickareastorageareaconfig.PickAreaStorageAreaConfigService;
import com.hd123.sardine.wms.api.basicInfo.pickarea.PickArea;
import com.hd123.sardine.wms.api.basicInfo.pickarea.PickAreaService;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;

/**
 * @author fanqingqing
 *
 */
public class PickAreaStorageAreaConfigServiceImpl implements PickAreaStorageAreaConfigService {
    @Autowired
    private PickAreaService pickAreaService;

    @Override
    public void setPickAreaStorageAreaConfig(String pickAreaUuid, String storageArea, long version)
            throws IllegalArgumentException, VersionConflictException, WMSException {
        Assert.assertArgumentNotNull(pickAreaUuid, "pickAreaUuid");

        PickArea pickArea = pickAreaService.get(pickAreaUuid);
        if (pickArea == null)
            throw new WMSException("拣货分区不存在");

        PersistenceUtils.checkVersion(version, pickArea, "拣货分区配置", pickArea.getCode());

        pickArea.setStorageArea(storageArea);
        pickAreaService.saveModify(pickArea);
    }

    @Override
    public PageQueryResult<PickAreaStorageAreaConfig> query(PageQueryDefinition definition)
            throws IllegalArgumentException {
        Assert.assertArgumentNotNull(definition, "definition");

        PageQueryResult<PickArea> pickAreaPqr = pickAreaService.query(definition);
        PageQueryResult<PickAreaStorageAreaConfig> result = new PageQueryResult<>();
        if (pickAreaPqr == null || CollectionUtils.isEmpty(pickAreaPqr.getRecords())) {
            PageQueryUtil.assignPageInfo(result, definition);
            result.setRecords(new ArrayList<>());
            return result;
        }
        List<PickAreaStorageAreaConfig> pickAreaStorageAreaConfig = new ArrayList<>();
        for (PickArea pickArea : pickAreaPqr.getRecords()) {
            PickAreaStorageAreaConfig config = new PickAreaStorageAreaConfig();
            config.setCompanyUuid(pickArea.getCompanyUuid());
            config.setPickArea(new UCN(pickArea.getUuid(), pickArea.getCode(), pickArea.getName()));
            config.setStorageArea(pickArea.getStorageArea());
            config.setVersion(pickArea.getVersion());
            pickAreaStorageAreaConfig.add(config);
        }

        result.setPage(pickAreaPqr.getPage());
        result.setPageCount(pickAreaPqr.getPageCount());
        result.setPageSize(pickAreaPqr.getPageSize());
        result.setRecordCount(pickAreaPqr.getRecordCount());
        result.setRecords(pickAreaStorageAreaConfig);
        return result;
    }

    @Override
    public String getStorageAreaByPickArea(String pickAreaUuid) {
        if (StringUtil.isNullOrBlank(pickAreaUuid))
            return null;
        PickArea pickArea = pickAreaService.get(pickAreaUuid);
        if (pickArea == null)
            return null;
        return pickArea.getStorageArea();
    }

    @Override
    public String getStorageAreaByFixedPickBin(String fixedPickBin) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PickAreaStorageAreaConfig getPickAreaStorageAreaConfig(String pickAreaUuid) {
        if (StringUtil.isNullOrBlank(pickAreaUuid))
            return null;
        PickArea pickArea = pickAreaService.get(pickAreaUuid);
        if (pickArea == null)
            return null;
        PickAreaStorageAreaConfig config = new PickAreaStorageAreaConfig();
        config.setCompanyUuid(pickArea.getCompanyUuid());
        config.setPickArea(new UCN(pickArea.getUuid(), pickArea.getCode(), pickArea.getName()));
        config.setStorageArea(pickArea.getStorageArea());
        config.setVersion(pickArea.getVersion());
        return config;
    }

}
