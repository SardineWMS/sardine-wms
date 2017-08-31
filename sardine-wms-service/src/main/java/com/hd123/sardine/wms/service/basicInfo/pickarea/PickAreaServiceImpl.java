/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	PickAreaServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月12日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.service.basicInfo.pickarea;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.pickarea.PickArea;
import com.hd123.sardine.wms.api.basicInfo.pickarea.PickAreaService;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.dao.basicInfo.pickarea.PickAreaDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.log.EntityLogger;

/**
 * @author yangwenzhu
 *
 */
public class PickAreaServiceImpl extends BaseWMSService implements PickAreaService {

  @Autowired
  private PickAreaDao dao;
  @Autowired
  private EntityLogger logger;

  @Override
  public String saveNew(PickArea area) throws WMSException {
    Assert.assertArgumentNotNull(area, "area");
    area.validate();

    PickArea p = dao.getByCode(area.getCode());
    if (p != null)
      throw new WMSException(MessageFormat.format("已存在代码为{0}的拣货分区", area.getCode()));

    insertValidate();

    area.setUuid(UUIDGenerator.genUUID());
    area.setCreateInfo(ApplicationContextUtil.getOperateInfo());
    area.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    dao.insert(area);

    logger.injectContext(this, area.getUuid(), PickArea.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_ADDNEW, "新增拣货分区");
    return area.getUuid();
  }

  @Override
  public void saveModify(PickArea area) throws WMSException {
    Assert.assertArgumentNotNull(area, "area");

    area.validate();
    PickArea pa = dao.get(area.getUuid());
    if (pa == null)
      throw new WMSException("要修改的拣货分区不存在");
    PickArea a = dao.getByCode(area.getCode());
    if (a != null && Objects.equals(a.getUuid(), area.getUuid()) == false)
      throw new WMSException(MessageFormat.format("已存在代码为{0}的拣货分区，不能重复使用", area.getCode()));
    area.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());

    dao.update(area);
    logger.injectContext(this, area.getUuid(), PickArea.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "修改拣货分区");

  }

  @Override
  public void remove(String uuid, long version) throws WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(version, "version");

    PickArea p = dao.get(uuid);
    if (p == null)
      throw new WMSException(MessageFormat.format("要删除的拣货分区{0}不存在", uuid));
    PersistenceUtils.checkVersion(version, p, PickArea.CAPTION, uuid);

    dao.remove(uuid, version);

    logger.injectContext(this, uuid, PickArea.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_REMOVE, "删除拣货分区");
  }

  @Override
  public PickArea get(String uuid) {
    if (StringUtil.isNullOrBlank(uuid))
      return null;
    return dao.get(uuid);
  }

  @Override
  public PickArea getByCode(String code) {
    if (StringUtil.isNullOrBlank(code))
      return null;
    return dao.getByCode(code);
  }

  @Override
  public PageQueryResult<PickArea> query(PageQueryDefinition definition) {
    Assert.assertArgumentNotNull(definition, "definition");
    PageQueryResult<PickArea> qpr = new PageQueryResult<>();
    List<PickArea> list = dao.query(definition);
    PageQueryUtil.assignPageInfo(qpr, definition);
    qpr.setRecords(list);
    return qpr;
  }

  private void insertValidate() throws WMSException {
    // TODO 校验拣货分区对应的货位范围不能交叉
  }

  @Override
  public PickArea getByStorageArea(String storageArea) {
    if (StringUtil.isNullOrBlank(storageArea))
      return null;
    return dao.getByStorageArea(storageArea);
  }
}
