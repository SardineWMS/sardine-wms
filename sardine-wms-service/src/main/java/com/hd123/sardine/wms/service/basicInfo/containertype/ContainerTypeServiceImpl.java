/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	ContainerTypeServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月7日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.service.basicInfo.containertype;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.containertype.ContainerType;
import com.hd123.sardine.wms.api.basicInfo.containertype.ContainerTypeService;
import com.hd123.sardine.wms.common.entity.OperateContext;
import com.hd123.sardine.wms.common.entity.OperateInfo;
import com.hd123.sardine.wms.common.exception.EntityNotFoundException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.common.validator.ValidateHandler;
import com.hd123.sardine.wms.common.validator.ValidateResult;
import com.hd123.sardine.wms.dao.basicInfo.containertype.ContainerTypeDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;

/**
 * @author fanqingqing
 *
 */
public class ContainerTypeServiceImpl extends BaseWMSService implements ContainerTypeService {

    @Autowired
    private ContainerTypeDao dao;

    @Autowired
    private ValidateHandler<ContainerType> containerTypeSaveNewValidateHandler;

    @Autowired
    private ValidateHandler<ContainerType> containerTypeSaveModifyValidateHandler;

    @Autowired
    private ValidateHandler<OperateContext> operateContextValidateHandler;

    @Override
    public PageQueryResult<ContainerType> query(PageQueryDefinition definition)
            throws IllegalArgumentException {
        Assert.assertArgumentNotNull(definition, "definition");
        PageQueryResult<ContainerType> pgr = new PageQueryResult<ContainerType>();
        List<ContainerType> list = dao.query(definition);
        PageQueryUtil.assignPageInfo(pgr, definition);
        pgr.setRecords(list);
        return pgr;
    }

    @Override
    public ContainerType get(String uuid) {
        if (StringUtil.isNullOrBlank(uuid))
            return null;
        return dao.get(uuid);
    }

    @Override
    public String saveNew(ContainerType containerType, OperateContext operCtx)
            throws IllegalArgumentException, WMSException {
        ValidateResult saveNewResult = containerTypeSaveNewValidateHandler.validate(containerType);
        checkValidateResult(saveNewResult);
        ValidateResult operCtxResult = operateContextValidateHandler.validate(operCtx);
        checkValidateResult(operCtxResult);

        ContainerType dbContainerType = dao.getByCode(containerType.getCode());
        if (dbContainerType != null)
            throw new WMSException("已存在代码为" + containerType.getCode() + "的容器类型");

        containerType.setUuid(UUIDGenerator.genUUID());
        containerType.setCreateInfo(OperateInfo.newInstance(operCtx));
        containerType.setLastModifyInfo(OperateInfo.newInstance(operCtx));
        dao.insert(containerType);
        return containerType.getUuid();
    }

    @Override
    public void saveModify(ContainerType containerType, OperateContext operCtx)
            throws IllegalArgumentException, WMSException {
        ValidateResult saveModifyResult = containerTypeSaveModifyValidateHandler
                .validate(containerType);
        checkValidateResult(saveModifyResult);
        ValidateResult operCtxResult = operateContextValidateHandler.validate(operCtx);
        checkValidateResult(operCtxResult);

        ContainerType oldContainerType = dao.get(containerType.getUuid());
        if (oldContainerType == null)
            throw EntityNotFoundException.create(ContainerType.class.getName(), "uuid",
                    containerType.getUuid());
        PersistenceUtils.checkVersion(containerType.getVersion(), oldContainerType, "容器类型",
                containerType.getUuid());

        ContainerType dbContainerType = dao.getByCode(containerType.getCode());
        if (dbContainerType != null
                && ObjectUtils.notEqual(containerType.getCode(), dbContainerType.getCode()))
            throw new WMSException("已存在代码为" + containerType.getCode() + "的容器类型");

        containerType.setLastModifyInfo(OperateInfo.newInstance(operCtx));
        dao.update(containerType);
    }

    @Override
    public void remove(String uuid, long version, OperateContext operCtx)
            throws IllegalArgumentException, WMSException {
        Assert.assertArgumentNotNull(uuid, "uuid");
        Assert.assertArgumentNotNull(version, "version");
        ValidateResult operCtxResult = operateContextValidateHandler.validate(operCtx);
        checkValidateResult(operCtxResult);

        ContainerType containerType = dao.get(uuid);
        if (containerType == null)
            throw EntityNotFoundException.create(ContainerType.class.getName(), "uuid", uuid);
        PersistenceUtils.checkVersion(version, containerType, "容器类型", uuid);
        dao.remove(uuid, version);
    }

}
