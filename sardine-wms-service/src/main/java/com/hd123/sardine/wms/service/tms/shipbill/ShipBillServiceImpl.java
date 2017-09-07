/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	ShipBillServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月16日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.service.tms.shipbill;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBill;
import com.hd123.sardine.wms.api.tms.shipbill.DeliveryType;
import com.hd123.sardine.wms.api.tms.shipbill.OperateMethod;
import com.hd123.sardine.wms.api.tms.shipbill.ShipBill;
import com.hd123.sardine.wms.api.tms.shipbill.ShipBillContainerStock;
import com.hd123.sardine.wms.api.tms.shipbill.ShipBillCustomerItem;
import com.hd123.sardine.wms.api.tms.shipbill.ShipBillService;
import com.hd123.sardine.wms.api.tms.shipbill.ShipBillState;
import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.common.utils.QpcHelper;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.dao.tms.shipbill.ShipBillDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.log.EntityLogger;

/**
 * @author fanqingqing
 *
 */

public class ShipBillServiceImpl extends BaseWMSService implements ShipBillService {
  @Autowired
  private ShipBillDao dao;
  @Autowired
  private EntityLogger logger;

  @Autowired
  private ShipBillVerifier shipBillVerifier;

  @Autowired
  private ShipBillHandler shipBillHandler;

  @Override
  public ShipBill getByUuid(String uuid) {
    if (StringUtil.isNullOrBlank(uuid))
      return null;
    ShipBill bill = dao.get(uuid);
    if (bill == null)
      return null;
    bill.setCustomerItems(dao.queryCustomerItems(uuid));
    bill.setContainerStocks(dao.queryContainerStockItems(uuid));
    return bill;
  }

  @Override
  public ShipBill getByBillNumber(String billNumber) {
    if (StringUtil.isNullOrBlank(billNumber))
      return null;
    ShipBill bill = dao.getByBillNumber(billNumber);
    if (bill == null)
      return null;
    bill.setCustomerItems(dao.queryCustomerItems(bill.getUuid()));
    bill.setContainerStocks(dao.queryContainerStockItems(bill.getUuid()));
    return bill;
  }

  @Override
  public PageQueryResult<ShipBill> query(PageQueryDefinition definition)
      throws IllegalArgumentException {
    Assert.assertArgumentNotNull(definition, "definition");

    List<ShipBill> list = dao.query(definition);
    PageQueryResult<ShipBill> pqr = new PageQueryResult<>();
    PageQueryUtil.assignPageInfo(pqr, definition);
    pqr.setRecords(list);
    return pqr;
  }

  @Override
  public void finish(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(version, "version");

    ShipBill shipBill = dao.get(uuid);
    if (shipBill == null)
      throw new WMSException("不存在");

    PersistenceUtils.checkVersion(version, shipBill, "装车单", shipBill.getBillNumber());
    if (ShipBillState.Finished.equals(shipBill.getState()))
      throw new WMSException(
          "装车单" + shipBill.getBillNumber() + "状态是" + shipBill.getState().getCaption() + "，不能完成！");

    shipBillVerifier.verifyShipBill(shipBill);

    shipBill.setState(ShipBillState.Finished);
    shipBill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    dao.update(shipBill);

    List<ShipBillContainerStock> items = dao.queryContainerStockItems(uuid);

    shipBillHandler.stockOut(shipBill, items);
    shipBillHandler.releaseBinAndContainer(shipBill, items);

    logger.injectContext(this, uuid, AcceptanceBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log("完成", "完成装车单");
  }

  @Override
  public String saveNew(ShipBill shipBill) throws IllegalArgumentException, WMSException {
    Assert.assertArgumentNotNull(shipBill, "shipBill");

    shipBillVerifier.verifyShipBill(shipBill);
    shipBill.clearTotalInfo();

    List<ShipBillCustomerItem> customerItems = shipBillHandler.fetchContainerItems(shipBill);

    shipBill.setUuid(UUIDGenerator.genUUID());
    shipBill
        .setBillNumber(billNumberGenerator.allocateNextBillNumber(ShipBill.class.getSimpleName()));
    shipBill.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    shipBill.setMethod(OperateMethod.ManualBill);
    shipBill.setDeliveryType(DeliveryType.warehouse);
    shipBill.setCreateInfo(ApplicationContextUtil.getOperateInfo());
    shipBill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    shipBill.setState(ShipBillState.Initial);
    shipBill.setCustomerCount(customerItems.size());

    for (ShipBillCustomerItem ci : customerItems) {
      ci.setUuid(UUIDGenerator.genUUID());
      ci.setShipBillUuid(shipBill.getUuid());
      shipBill.setTotalAmount(shipBill.getTotalAmount().add(ci.getTotalAmount()));
      shipBill.setTotalCaseQty(
          QpcHelper.caseQtyStrAdd(shipBill.getTotalCaseQty(), ci.getTotalCaseQty()));
      shipBill.setTotalVolume(shipBill.getTotalVolume().add(ci.getTotalAmount()));
      shipBill.setTotalWeight(shipBill.getTotalWeight().add(ci.getTotalWeight()));
      shipBill.setContainerCount(shipBill.getContainerCount() + ci.getContainerCount());
    }

    dao.insertCustomerItems(customerItems);

    for (ShipBillContainerStock cs : shipBill.getContainerStocks()) {
      cs.setUuid(UUIDGenerator.genUUID());
      cs.setShipBillUuid(shipBill.getUuid());
      cs.setShiper(ApplicationContextUtil.getLoginUser());
    }

    dao.insertContainerStockItems(shipBill.getContainerStocks());

    dao.insert(shipBill);
    dao.updateShipOrder(shipBill.getUuid());
    return shipBill.getBillNumber();
  }

  @Override
  public void saveModify(ShipBill shipBill)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(shipBill, "shipBill");

    ShipBill oldBill = dao.get(shipBill.getUuid());
    if (oldBill == null)
      throw new WMSException("装车单" + shipBill.getBillNumber() + "不存在！");
    PersistenceUtils.checkVersion(shipBill.getVersion(), oldBill, ShipBill.CAPTION,
        shipBill.getBillNumber());
    if (oldBill.getState().equals(ShipBillState.Initial) == false)
      throw new WMSException("装车单" + shipBill.getBillNumber() + "已经开始装车，无法编辑！");

    shipBillVerifier.verifyShipBill(shipBill);
    shipBill.clearTotalInfo();

    List<ShipBillCustomerItem> customerItems = shipBillHandler.fetchContainerItems(shipBill);

    shipBill.setMethod(OperateMethod.ManualBill);
    shipBill.setDeliveryType(DeliveryType.warehouse);
    shipBill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    shipBill.setState(ShipBillState.Initial);

    for (ShipBillCustomerItem ci : customerItems) {
      ci.setUuid(UUIDGenerator.genUUID());
      ci.setShipBillUuid(shipBill.getUuid());
      shipBill.setTotalAmount(shipBill.getTotalAmount().add(ci.getTotalAmount()));
      shipBill.setTotalCaseQty(
          QpcHelper.caseQtyStrAdd(shipBill.getTotalCaseQty(), ci.getTotalCaseQty()));
      shipBill.setTotalVolume(shipBill.getTotalVolume().add(ci.getTotalAmount()));
      shipBill.setTotalWeight(shipBill.getTotalWeight().add(ci.getTotalWeight()));
    }

    dao.removeCustomerItems(shipBill.getUuid());
    dao.insertCustomerItems(customerItems);

    for (ShipBillContainerStock cs : shipBill.getContainerStocks()) {
      cs.setUuid(UUIDGenerator.genUUID());
      cs.setShipBillUuid(shipBill.getUuid());
      cs.setShiper(ApplicationContextUtil.getLoginUser());
    }

    dao.removeContainerStockItems(shipBill.getUuid());
    dao.insertContainerStockItems(shipBill.getContainerStocks());

    dao.update(shipBill);
    dao.updateShipOrder(shipBill.getUuid());
  }

  @Override
  public String saveAndFinish(ShipBill shipBill) throws IllegalArgumentException, WMSException {
    Assert.assertArgumentNotNull(shipBill, "shipBill");

    String billNumber = saveNew(shipBill);

    shipBill.setState(ShipBillState.Finished);
    shipBill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    dao.update(shipBill);

    shipBillHandler.stockOut(shipBill, shipBill.getContainerStocks());
    shipBillHandler.releaseBinAndContainer(shipBill, shipBill.getContainerStocks());
    return billNumber;
  }

  @Override
  public void remove(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");

    ShipBill oldBill = dao.get(uuid);
    if (oldBill == null)
      throw new WMSException("装车单" + uuid + "不存在！");
    PersistenceUtils.checkVersion(version, oldBill, ShipBill.CAPTION, oldBill.getBillNumber());
    if (oldBill.getState().equals(ShipBillState.Initial) == false)
      throw new WMSException("装车单" + oldBill.getBillNumber() + "已经开始装车，无法删除！");

    dao.remove(uuid, version);
    dao.removeCustomerItems(uuid);
    dao.removeContainerStockItems(uuid);
  }

  @Override
  public List<ShipBillContainerStock> queryWaitShipStocks() {
    return dao.queryWaitShipStocks();
  }
}
