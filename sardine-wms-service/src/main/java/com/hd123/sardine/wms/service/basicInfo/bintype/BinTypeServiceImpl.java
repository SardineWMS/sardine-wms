/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	BinTypeServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月6日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.service.basicInfo.bintype;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.bintype.BinType;
import com.hd123.sardine.wms.api.basicInfo.bintype.BinTypeService;
import com.hd123.sardine.wms.common.entity.OperateContext;
import com.hd123.sardine.wms.common.entity.OperateInfo;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.common.validator.ValidateHandler;
import com.hd123.sardine.wms.common.validator.ValidateResult;
import com.hd123.sardine.wms.common.validator.routines.NullValidator;
import com.hd123.sardine.wms.common.validator.routines.VersionValidator;
import com.hd123.sardine.wms.dao.basicInfo.bintype.BinTypeDao;
import com.hd123.sardine.wms.service.basicInfo.bintype.validator.BinTypeInsertValidateHandler;
import com.hd123.sardine.wms.service.basicInfo.bintype.validator.BinTypeRemoveValidateHandler;
import com.hd123.sardine.wms.service.basicInfo.bintype.validator.BinTypeUpdateValidateHandler;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.log.EntityLogger;

/**
 * @author yangwenzhu
 *
 */
public class BinTypeServiceImpl extends BaseWMSService implements BinTypeService {

    @Autowired
    private BinTypeDao binTypeDao;

    @Autowired
    private ValidateHandler<BinType> binTypeInsertValidateHandler;

    @Autowired
    private ValidateHandler<OperateContext> operateContextValidateHandler;

    @Autowired
    private ValidateHandler<String> binTypeRemoveValidateHandler;

    @Autowired
    private ValidateHandler<BinType> binTypeUpdateValidateHandler;

    @Autowired
    private EntityLogger logger;

    @Override
    public BinType get(String uuid) {
        if (StringUtil.isNullOrBlank(uuid))
            return null;
        return binTypeDao.get(uuid);
    }

    @Override
    public BinType getByCode(String code) {
        if (StringUtil.isNullOrBlank(code))
            return null;
        return binTypeDao.getByCode(code);
    }

    @Override
    public String insert(BinType binType, OperateContext operCtx)
            throws IllegalArgumentException, WMSException {
        BinType dbBinType = binTypeDao.getByCode(binType == null ? null : binType.getCode());
        ValidateResult insertResult = binTypeInsertValidateHandler
                .putAttribute(BinTypeInsertValidateHandler.KEY_CODEEXISTS_BINTYPE, dbBinType)
                .validate(binType);
        checkValidateResult(insertResult);
        ValidateResult operCtxResult = operateContextValidateHandler.validate(operCtx);
        checkValidateResult(operCtxResult);

        binType.setUuid(UUIDGenerator.genUUID());
        binType.setCreateInfo(OperateInfo.newInstance(operCtx));
        binType.setLastModifyInfo(OperateInfo.newInstance(operCtx));
        binTypeDao.insert(binType);

        logger.injectContext(this, binType.getUuid(), BinType.class.getName(), operCtx);
        logger.log(EntityLogger.EVENT_ADDNEW, "新增货位类型");
        return binType.getUuid();
    }

    @Override
    public void remove(String uuid, long version, OperateContext operCtx)
            throws IllegalArgumentException, WMSException {
        BinType deleteBinType = binTypeDao.get(uuid);
        ValidateResult removeResult = binTypeRemoveValidateHandler
                .putAttribute(BinTypeRemoveValidateHandler.KEY_OPERATOR_BINTYPE, deleteBinType)
                .putAttribute(VersionValidator.ATTR_KEY_VERSION, version).validate(uuid);
        checkValidateResult(removeResult);
        ValidateResult operCtxResult = operateContextValidateHandler.validate(operCtx);
        checkValidateResult(operCtxResult);
        binTypeDao.remove(uuid, version);

        logger.injectContext(this, uuid, BinType.class.getName(), operCtx);
        logger.log(EntityLogger.EVENT_REMOVE, "删除货位类型");
    }

    @Override
    public void update(BinType binType, OperateContext operCtx)
            throws IllegalArgumentException, WMSException {
        BinType updateBinType = binTypeDao.get(binType == null ? null : binType.getUuid());
        BinType existsBinType = binTypeDao.getByCode(binType == null ? null : binType.getCode());
        ValidateResult updateResult = binTypeUpdateValidateHandler
                .putAttribute(BinTypeUpdateValidateHandler.KEY_CODEEXISTS_BINTYPE, existsBinType)
                .putAttribute(BinTypeUpdateValidateHandler.KEY_UPDATE_BINTYPE, updateBinType)
                .putAttribute(NullValidator.KEY_CURRENTOPERATOR_UUID,
                        binType == null ? null : binType.getUuid())
                .putAttribute(VersionValidator.ATTR_KEY_VERSION, binType.getVersion())
                .validate(binType);
        checkValidateResult(updateResult);
        ValidateResult operCtxResult = operateContextValidateHandler.validate(operCtx);
        checkValidateResult(operCtxResult);

        binType.setLastModifyInfo(OperateInfo.newInstance(operCtx));
        binTypeDao.update(binType);

        logger.injectContext(this, binType.getUuid(), BinType.class.getName(), operCtx);
        logger.log(EntityLogger.EVENT_MODIFY, "修改货位类型");
    }

    @Override
    public PageQueryResult<BinType> query(PageQueryDefinition definition) {
        Assert.assertArgumentNotNull(definition, "definition");

        PageQueryResult<BinType> pgr = new PageQueryResult<BinType>();
        List<BinType> list = binTypeDao.query(definition);
        PageQueryUtil.assignPageInfo(pgr, definition);
        pgr.setRecords(list);
        return pgr;
    }

}
