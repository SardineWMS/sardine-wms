/**
 * 版权所有(C)，LHWMS项目组，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	ContainerServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月7日 - Jing - 创建。
 */
package com.hd123.sardine.wms.service.basicInfo.container;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.container.Container;
import com.hd123.sardine.wms.api.basicInfo.container.ContainerService;
import com.hd123.sardine.wms.api.basicInfo.container.ContainerState;
import com.hd123.sardine.wms.api.basicInfo.containertype.ContainerType;
import com.hd123.sardine.wms.api.basicInfo.containertype.ContainerTypeService;
import com.hd123.sardine.wms.api.stock.StockService;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.dao.basicInfo.container.ContainerDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.log.EntityLogger;

/**
 * @author Jing
 *
 */
public class ContainerServiceImpl extends BaseWMSService implements ContainerService {
  @Autowired
  private ContainerDao dao;
  @Autowired
  private ContainerTypeService containerTypeService;
  @Autowired
  private StockService stockService;

  @Autowired
  private EntityLogger logger;

  @Override
  public void saveNew(String containerTypeUuid) throws WMSException {
    Assert.assertArgumentNotNull(containerTypeUuid, "containerTypeUuid");

    ContainerType containerType = containerTypeService.get(containerTypeUuid);
    if (containerType == null)
      throw new WMSException("容器类型不存在。");

    Container container = new Container();
    container.setUuid(UUIDGenerator.genUUID());
    container.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    String barcode = billNumberGenerator.allocateNextContainerBarcode(
        containerType.getBarCodePrefix(), containerType.getBarCodeLength());
    container.setBarcode(barcode);
    container.setContainerType(
        new UCN(containerTypeUuid, containerType.getCode(), containerType.getName()));
    container.setCompanyUuid(containerType.getCompanyUuid());
    container.setCreateInfo(ApplicationContextUtil.getOperateInfo());
    container.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    dao.insert(container);

    logger.injectContext(this, container.getUuid(), Container.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_ADDNEW, "新增容器");
  }

  @Override
  public Container getByBarcode(String barcode) {
    Assert.assertArgumentNotNull(barcode, "barcode");

    return dao.getByBarcode(barcode, ApplicationContextUtil.getCompanyUuid());
  }

  @Override
  public PageQueryResult<Container> query(PageQueryDefinition definition) {
    Assert.assertArgumentNotNull(definition, "definition");

    definition.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    PageQueryResult<Container> pgr = new PageQueryResult<Container>();
    definition.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    List<Container> list = dao.query(definition);
    PageQueryUtil.assignPageInfo(pgr, definition);
    pgr.setRecords(list);
    return pgr;
  }

  @Override
  public void change(String uuid, long version, ContainerState state, String position)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    if (state == null && StringUtil.isNullOrBlank(position))
      throw new IllegalArgumentException("修改容器的位置和状态不能同时为空！");

    Container container = dao.get(uuid);
    if (container == null)
      throw new WMSException("指定的容器不存在！");
    PersistenceUtils.checkVersion(version, container, "容器", container.getBarcode());

    ContainerState oldState = container.getState();
    String oldPosition = container.getPosition();
    if (state != null)
      container.setState(state);
    if (StringUtil.isNullOrBlank(position) == false)
      container.setPosition(position);
    container.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());

    dao.update(container);

    logger.injectContext(this, uuid, Container.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY,
        "修改容器状态，原状态：" + oldState.getCaption() + "，新状态：" + container.getState().getCaption()
            + "；老位置：" + oldPosition + "，新位置：" + container.getPosition());
  }

  @Override
  public void recycle(String uuid, long version) throws WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");

    Container container = dao.get(uuid);
    if (container == null)
      throw new WMSException("指定的容器不存在！");
    PersistenceUtils.checkVersion(version, container, "容器", container.getBarcode());

    ContainerState oldState = container.getState();
    if (stockService.hasContainerStock(container.getBarcode()))
      container.setState(ContainerState.STACONTAINERUSEING);
    else {
      container.setState(ContainerState.STACONTAINERIDLE);
      container.setPosition(Container.UNKNOWN_POSITION);
    }
    container.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    dao.update(container);

    logger.injectContext(this, uuid, Container.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY,
        "修改容器状态，原状态：" + oldState.getCaption() + "，新状态：" + container.getState().getCaption());
  }

  @Override
  public void lock(String uuid, long version) throws WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");

    Container container = dao.get(uuid);
    if (container == null)
      throw new WMSException("指定的容器不存在！");
    PersistenceUtils.checkVersion(version, container, "容器", container.getBarcode());
    ContainerState oldState = container.getState();
    container.setState(ContainerState.STACONTAINERLOCK);
    container.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    dao.update(container);
    logger.injectContext(this, uuid, Container.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY,
        "修改容器状态，原状态：" + oldState.getCaption() + "，新状态：" + container.getState().getCaption());
  }

  @Override
  public void using(String uuid, long version, String position) throws WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");

    Container container = dao.get(uuid);
    if (container == null)
      throw new WMSException("指定的容器不存在！");
    PersistenceUtils.checkVersion(version, container, "容器", container.getBarcode());

    ContainerState oldState = container.getState();
    if (stockService.hasContainerStock(container.getBarcode())) {
      container.setState(ContainerState.STACONTAINERUSEING);
      container.setPosition(position);
    } else
      container.setState(ContainerState.STACONTAINERIDLE);
    container.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    dao.update(container);

    logger.injectContext(this, uuid, Container.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY,
        "修改容器状态，原状态：" + oldState.getCaption() + "，新状态：" + container.getState().getCaption());
  }
}
