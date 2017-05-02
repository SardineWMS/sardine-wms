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
import com.hd123.sardine.wms.common.entity.OperateContext;
import com.hd123.sardine.wms.common.entity.OperateInfo;
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
import com.hd123.sardine.wms.service.util.FlowCodeGenerator;

/**
 * @author Jing
 *
 */
public class ContainerServiceImpl implements ContainerService {
  @Autowired
  private ContainerDao dao;
  @Autowired
  private ContainerTypeService containerTypeService;

  @Override
  public void saveNew(String containerTypeUuid, OperateContext operateContext) throws WMSException {
    Assert.assertArgumentNotNull(containerTypeUuid, "containerTypeUuid");

    ContainerType containerType = containerTypeService.get(containerTypeUuid);
    if (containerType == null)
      throw new WMSException("容器类型不存在。");

    Container container = new Container();
    container.setUuid(UUIDGenerator.genUUID());
    String flowCode = FlowCodeGenerator.getInstance().allocate(containerType.getBarCodePrefix(),
        containerType.getCompanyUuid(), containerType.getBarCodeLength());
    container.setBarcode(containerType.getBarCodePrefix() + flowCode);
    container.setContainerType(
        new UCN(containerTypeUuid, containerType.getCode(), containerType.getName()));
    container.setCompanyUuid(containerType.getCompanyUuid());
    container.setCreateInfo(OperateInfo.newInstance(operateContext));
    container.setLastModifyInfo(OperateInfo.newInstance(operateContext));
    dao.insert(container);
  }

  @Override
  public Container getByBarcode(String barcode, String companyUuid) {
    Assert.assertArgumentNotNull(barcode, "barcode");
    Assert.assertArgumentNotNull(companyUuid, "companyUuid");

    return dao.getByBarcode(barcode, companyUuid);
  }

  @Override
  public PageQueryResult<Container> query(PageQueryDefinition definition) {
    PageQueryResult<Container> pgr = new PageQueryResult<Container>();
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

    if (state != null)
      container.setState(state);
    if (StringUtil.isNullOrBlank(position) == false)
      container.setPosition(position);
    container.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());

    dao.update(container);
  }
}
