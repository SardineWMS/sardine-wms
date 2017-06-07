/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	CarrierServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月6日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.service.tms.carrier;

import java.text.MessageFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.tms.carrier.Carrier;
import com.hd123.sardine.wms.api.tms.carrier.CarrierService;
import com.hd123.sardine.wms.api.tms.carrier.CarrierState;
import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.dao.tms.carrier.CarrierDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.log.EntityLogger;

/**
 * @author yangwenzhu
 *
 */
public class CarrierServiceImpl extends BaseWMSService implements CarrierService {

    @Autowired
    private CarrierDao dao;
    @Autowired
    private EntityLogger logger;

    @Override
    public String saveNew(Carrier carrier) throws IllegalArgumentException, WMSException {
        Assert.assertArgumentNotNull(carrier, "carrier");

        carrier.validate();

        Carrier existCarrier = getByCode(carrier.getCode());
        if (existCarrier != null)
            throw new WMSException("已存在代码为" + carrier.getCode() + "的承运商");
        carrier.setState(CarrierState.online);
        carrier.setUuid(UUIDGenerator.genUUID());
        carrier.setCreateInfo(ApplicationContextUtil.getOperateInfo());
        carrier.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
        dao.insert(carrier);

        logger.injectContext(this, carrier.getUuid(), Carrier.class.getName(),
                ApplicationContextUtil.getOperateContext());
        logger.log(EntityLogger.EVENT_ADDNEW, "新增承运商");
        return carrier.getUuid();
    }

    @Override
    public void saveModify(Carrier carrier) throws IllegalArgumentException, WMSException {
        Assert.assertArgumentNotNull(carrier, "carrier");

        carrier.validate();

        Carrier c = dao.get(carrier.getUuid());
        if (c == null)
            throw new WMSException(MessageFormat.format("承运商{0}不存在", carrier.getUuid()));

        Carrier existCarrier = getByCode(carrier.getCode());
        if (existCarrier != null && c.getUuid().equals(existCarrier.getUuid()) == false)
            throw new WMSException(MessageFormat.format("已经存在代码为{0}的承运商", carrier.getCode()));

        if (CarrierState.offline.equals(c.getState()))
            throw new WMSException("承运商未启用，不允许修改");

        PersistenceUtils.checkVersion(carrier.getVersion(), c, Carrier.CAPTION, carrier.getCode());
        carrier.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());

        dao.update(carrier);
        logger.injectContext(this, carrier.getUuid(), Carrier.class.getName(),
                ApplicationContextUtil.getOperateContext());
        logger.log(EntityLogger.EVENT_MODIFY, "修改承运商");

    }

    @Override
    public void online(String uuid, long version)
            throws IllegalArgumentException, VersionConflictException, WMSException {
        Assert.assertArgumentNotNull(uuid, "uuid");
        Assert.assertArgumentNotNull(version, "version");

        Carrier carrier = dao.get(uuid);
        if (carrier == null)
            throw new WMSException("要启用的承运商" + uuid + "不存在");

        if (CarrierState.online.equals(carrier.getState()))
            return;

        PersistenceUtils.checkVersion(version, carrier, Carrier.CAPTION, carrier.getCode());

        carrier.setState(CarrierState.online);
        carrier.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());

        dao.update(carrier);
        logger.injectContext(this, uuid, Carrier.class.getName(),
                ApplicationContextUtil.getOperateContext());
        logger.log(EntityLogger.EVENT_MODIFY, "启用承运商");

    }

    @Override
    public void offline(String uuid, long version)
            throws IllegalArgumentException, VersionConflictException, WMSException {
        Assert.assertArgumentNotNull(uuid, "uuid");
        Assert.assertArgumentNotNull(version, "verison");

        Carrier c = dao.get(uuid);
        if (c == null)
            throw new WMSException("要停用的承运商" + uuid + "不存在");

        if (CarrierState.offline.equals(c.getState()))
            return;

        PersistenceUtils.checkVersion(version, c, Carrier.CAPTION, c.getCode());

        c.setState(CarrierState.offline);
        c.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
        dao.update(c);

        logger.injectContext(this, uuid, Carrier.class.getName(),
                ApplicationContextUtil.getOperateContext());
        logger.log(EntityLogger.EVENT_MODIFY, "停用承运商");

    }

    @Override
    public void remove(String uuid, long version) throws IllegalArgumentException, WMSException {
        Assert.assertArgumentNotNull(uuid, "uuid");
        Assert.assertArgumentNotNull(version, "verison");

        Carrier c = dao.get(uuid);
        if (c == null)
            throw new WMSException("要删除的承运商" + uuid + "不存在");

        PersistenceUtils.checkVersion(version, c, Carrier.CAPTION, uuid);

        dao.remove(uuid, version);

        logger.injectContext(this, uuid, Carrier.class.getName(),
                ApplicationContextUtil.getOperateContext());
        logger.log(EntityLogger.EVENT_REMOVE, "删除承运商");

    }

    @Override
    public Carrier getByCode(String code) throws WMSException {
        if (StringUtil.isNullOrBlank(code))
            return null;

        Carrier c = dao.getByCode(code);
        return c;
    }

    @Override
    public PageQueryResult<Carrier> query(PageQueryDefinition definition) {
        Assert.assertArgumentNotNull(definition, "definition");

        PageQueryResult<Carrier> qpr = new PageQueryResult<>();

        List<Carrier> list = dao.query(definition);
        PageQueryUtil.assignPageInfo(qpr, definition);
        qpr.setRecords(list);
        return qpr;
    }

}
