/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	ReceiveBillServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月14日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.in.receive;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.in.receive.ReceiveBill;
import com.hd123.sardine.wms.api.in.receive.ReceiveBillItem;
import com.hd123.sardine.wms.api.in.receive.ReceiveBillMethod;
import com.hd123.sardine.wms.api.in.receive.ReceiveBillService;
import com.hd123.sardine.wms.api.in.receive.ReceiveBillState;
import com.hd123.sardine.wms.common.entity.OperateInfo;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.dao.in.receive.ReceiveBillDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.log.EntityLogger;

/**
 * @author zhangsai
 *
 */
public class ReceiveBillServiceImpl extends BaseWMSService implements ReceiveBillService {

  @Autowired
  private ReceiveBillDao receiveBillDao;

  @Autowired
  private ReceiveVerifier receiveVerifier;

  @Autowired
  private ReceiveHandler receiveHandler;

  @Autowired
  private EntityLogger logger;

  @Override
  public String insert(ReceiveBill bill) throws WMSException {
    Assert.assertArgumentNotNull(bill, "bill");
    bill.validate();

    receiveVerifier.verifyOrderBillAndRefresh(bill);
    receiveVerifier.verifyReceiveProductDate(bill);
    receiveVerifier.verifyBinAndContainer(bill);

    bill.setUuid(UUIDGenerator.genUUID());
    bill.setBillNumber(
        billNumberGenerator.allocateNextBillNumber(ReceiveBill.class.getSimpleName()));
    bill.setState(ReceiveBillState.Initial);
    bill.setMethod(ReceiveBillMethod.ManualBill);
    bill.setCreateInfo(ApplicationContextUtil.getOperateInfo());
    bill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());

    for (ReceiveBillItem item : bill.getItems()) {
      item.setUuid(UUIDGenerator.genUUID());
      item.setReceiveBillUuid(bill.getUuid());
      item.setStockBatch(stockBatchUtils.genStockBatch());
    }
    receiveBillDao.insert(bill);
    receiveBillDao.insertItems(bill.getItems());

    logger.injectContext(this, bill.getUuid(), ReceiveBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_ADDNEW, "新建收货单");
    return bill.getBillNumber();
  }

  @Override
  public void update(ReceiveBill bill) throws WMSException {
    Assert.assertArgumentNotNull(bill, "bill");
    Assert.assertArgumentNotNull(bill.getUuid(), "bill.uuid");
    Assert.assertArgumentNotNull(bill.getBillNumber(), "bill.billNumber");
    bill.validate();

    receiveVerifier.verifyOrderBillAndRefresh(bill);
    receiveVerifier.verifyReceiveProductDate(bill);
    receiveVerifier.verifyBinAndContainer(bill);

    ReceiveBill oldBill = receiveBillDao.get(bill.getUuid());
    if (oldBill == null)
      throw new WMSException("编辑的收货单" + bill.getBillNumber() + "不存在！");
    PersistenceUtils.checkVersion(bill.getVersion(), oldBill, "收货单", bill.getBillNumber());
    if (oldBill.getState().equals(ReceiveBillState.Initial) == false)
      throw new WMSException("收货单" + bill.getBillNumber() + "状态不是初始，无法编辑！");

    bill.setState(ReceiveBillState.Initial);
    bill.setMethod(ReceiveBillMethod.ManualBill);
    bill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());

    for (ReceiveBillItem item : bill.getItems()) {
      item.setUuid(UUIDGenerator.genUUID());
      item.setReceiveBillUuid(bill.getUuid());
    }
    receiveBillDao.update(bill);
    receiveBillDao.removeItems(bill.getUuid());
    receiveBillDao.insertItems(bill.getItems());

    logger.injectContext(this, bill.getUuid(), ReceiveBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "修改收货单");
  }

  @Override
  public ReceiveBill get(String uuid) {
    if (StringUtil.isNullOrBlank(uuid))
      return null;
    ReceiveBill receiveBill = receiveBillDao.get(uuid);
    if (receiveBill == null)
      return null;
    receiveBill.setItems(receiveBillDao.queryItems(uuid));
    return receiveBill;
  }

  @Override
  public ReceiveBill getByBillNo(String billno) {
    if (StringUtil.isNullOrBlank(billno))
      return null;
    ReceiveBill receiveBill = receiveBillDao.getByBillNumber(billno);
    if (receiveBill == null)
      return null;
    receiveBill.setItems(receiveBillDao.queryItems(receiveBill.getUuid()));
    return receiveBill;
  }

  @Override
  public PageQueryResult<ReceiveBill> query(PageQueryDefinition definition) {
    Assert.assertArgumentNotNull(definition, "definition");

    PageQueryResult<ReceiveBill> pgr = new PageQueryResult<ReceiveBill>();
    List<ReceiveBill> list = receiveBillDao.query(definition);
    PageQueryUtil.assignPageInfo(pgr, definition);
    pgr.setRecords(list);
    return pgr;
  }

  @Override
  public void remove(String uuid, long version) throws WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");

    ReceiveBill receiveBill = receiveBillDao.get(uuid);
    if (receiveBill == null)
      return;
    PersistenceUtils.checkVersion(version, receiveBill, "收货单", receiveBill.getBillNumber());

    if (receiveBill.getState().equals(ReceiveBillState.Initial) == false)
      throw new WMSException("收货单" + receiveBill.getBillNumber() + "状态不是初始，不能删除！");

    receiveBillDao.remove(uuid, version);
    receiveBillDao.removeItems(uuid);

    logger.injectContext(this, uuid, ReceiveBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_REMOVE, "删除收货单");
  }

  @Override
  public void audit(String uuid, long version) throws WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");

    ReceiveBill bill = get(uuid);
    if (bill == null)
      throw new WMSException("审核的收货单不存在！");
    if (bill.getState().equals(ReceiveBillState.Audited))
      return;

    PersistenceUtils.checkVersion(version, bill, "收货单", bill.getBillNumber());
    if (bill.getState().equals(ReceiveBillState.Initial)) {
      receiveVerifier.verifyOrderBillAndRefresh(bill);
      receiveVerifier.verifyReceiveProductDate(bill);
      receiveVerifier.verifyBinAndContainer(bill);

      receiveHandler.shiftIn(bill, bill.getItems());
      receiveHandler.orderBillReceive(bill.getOrderBillNumber(), bill.getItems());
    }

    List<String> containerBarcodes = receiveHandler.useReceiveBinAndContainer(bill.getItems());
    receiveHandler.generatorAndSavePutawayTask(bill, bill.getItems(), containerBarcodes);

    bill.setState(ReceiveBillState.Audited);
    bill.setLastModifyInfo(OperateInfo.newInstance(ApplicationContextUtil.getOperateContext()));
    receiveBillDao.update(bill);

    logger.injectContext(this, uuid, ReceiveBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "审核收货单");
  }
}
