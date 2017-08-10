/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	AcceptanceBillServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月5日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.service.out.acceptance;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBill;
import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBillItem;
import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBillService;
import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBillState;
import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.common.utils.QpcHelper;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.dao.out.acceptance.AcceptanceBillDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.log.EntityLogger;

/**
 * @author fanqingqing
 *
 */
public class AcceptanceBillServiceImpl extends BaseWMSService implements AcceptanceBillService {

  @Autowired
  private EntityLogger logger;

  @Autowired
  private AcceptanceBillDao billDao;

  @Autowired
  private AcceptanceVerifier acceptanceVerifier;

  @Autowired
  private AcceptanceHandler acceptanceHandler;

  @Override
  public AcceptanceBill get(String uuid) {
    if (StringUtil.isNullOrBlank(uuid))
      return null;
    AcceptanceBill bill = billDao.get(uuid);
    if (bill == null)
      return null;
    bill.setItems(billDao.queryItems(uuid));
    return bill;
  }

  @Override
  public AcceptanceBill getByBillNumber(String billNumber) {
    if (StringUtil.isNullOrBlank(billNumber))
      return null;
    AcceptanceBill bill = billDao.getByBillNumber(billNumber);
    if (bill == null)
      return null;
    bill.setItems(billDao.queryItems(bill.getUuid()));
    return bill;
  }

  @Override
  public PageQueryResult<AcceptanceBill> query(PageQueryDefinition definition) {
    Assert.assertArgumentNotNull(definition, "definition");

    PageQueryResult<AcceptanceBill> pgr = new PageQueryResult<AcceptanceBill>();
    List<AcceptanceBill> list = billDao.query(definition);
    PageQueryUtil.assignPageInfo(pgr, definition);
    pgr.setRecords(list);
    return pgr;
  }

  @Override
  public String insert(AcceptanceBill acceptanceBill)
      throws IllegalArgumentException, WMSException {
    acceptanceVerifier.verifyAcceptanceBill(acceptanceBill);

    acceptanceBill.setUuid(UUIDGenerator.genUUID());
    acceptanceBill.setState(AcceptanceBillState.Initial);
    acceptanceBill.setBillNumber(
        billNumberGenerator.allocateNextBillNumber(AcceptanceBill.class.getSimpleName()));
    acceptanceBill.setCreateInfo(ApplicationContextUtil.getOperateInfo());
    acceptanceBill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());

    for (AcceptanceBillItem item : acceptanceBill.getItems()) {
      item.setUuid(UUIDGenerator.genUUID());
      item.setAcceptanceBillUuid(acceptanceBill.getUuid());
    }
    billDao.insert(acceptanceBill);
    billDao.insertItems(acceptanceBill.getItems());

    logger.injectContext(this, acceptanceBill.getUuid(), AcceptanceBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_ADDNEW, "新建领用单");
    return acceptanceBill.getUuid();
  }

  @Override
  public void update(AcceptanceBill acceptanceBill)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(acceptanceBill, "acceptanceBill");
    Assert.assertArgumentNotNull(acceptanceBill.getUuid(), "acceptanceBill.uuid");
    Assert.assertArgumentNotNull(acceptanceBill.getBillNumber(), "acceptanceBill.billnumber");
    acceptanceVerifier.verifyAcceptanceBill(acceptanceBill);

    AcceptanceBill oldBill = billDao.get(acceptanceBill.getUuid());
    if (oldBill == null)
      throw new WMSException("要修改的要货单不存在！");

    PersistenceUtils.checkVersion(acceptanceBill.getVersion(), oldBill, "要货单",
        acceptanceBill.getBillNumber());
    acceptanceBill.setState(AcceptanceBillState.Initial);
    acceptanceBill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    for (AcceptanceBillItem item : acceptanceBill.getItems()) {
      item.setUuid(UUIDGenerator.genUUID());
      item.setAcceptanceBillUuid(acceptanceBill.getUuid());
    }
    billDao.update(acceptanceBill);
    billDao.removeItems(acceptanceBill.getUuid());
    billDao.insertItems(acceptanceBill.getItems());

    logger.injectContext(this, acceptanceBill.getUuid(), AcceptanceBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "修改领用单");
  }

  @Override
  public void remove(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(version, "version");

    AcceptanceBill acceptanceBill = billDao.get(uuid);
    if (acceptanceBill == null)
      throw new WMSException("领用单不存在");
    PersistenceUtils.checkVersion(version, acceptanceBill, "领用单", acceptanceBill.getBillNumber());

    if (acceptanceBill.getState().equals(AcceptanceBillState.Initial) == false)
      throw new WMSException("领用单" + acceptanceBill.getBillNumber() + "状态不是初始，不能删除！");

    billDao.remove(uuid, version);
    billDao.removeItems(uuid);

    logger.injectContext(this, uuid, AcceptanceBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_REMOVE, "删除领用单");
  }

  @Override
  public void approve(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(version, "version");

    AcceptanceBill acceptanceBill = get(uuid);
    if (acceptanceBill == null)
      throw new WMSException("领用单不存在");
    acceptanceVerifier.verifyAcceptanceBill(acceptanceBill);

    PersistenceUtils.checkVersion(version, acceptanceBill, "领用单", acceptanceBill.getBillNumber());
    if (AcceptanceBillState.Initial.equals(acceptanceBill.getState()) == false)
      throw new WMSException("领用单" + acceptanceBill.getBillNumber() + "状态不是初始，不能批准！");

    acceptanceHandler.shiftInOnWayStock(acceptanceBill);
    for (AcceptanceBillItem acceptanceItem : acceptanceBill.getItems()) {
      if (acceptanceItem.getPlanQty().compareTo(BigDecimal.ZERO) <= 0)
        continue;
      billDao.updateItem(acceptanceItem);
    }

    acceptanceBill.setState(AcceptanceBillState.Approved);
    acceptanceBill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    billDao.update(acceptanceBill);

    logger.injectContext(this, uuid, AcceptanceBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_APPROVE, "批准领用单");
  }

  @Override
  public void beginAlc(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(version, "version");

    AcceptanceBill acceptanceBill = billDao.get(uuid);
    if (acceptanceBill == null)
      throw new WMSException("领用单不存在");

    acceptanceBill.setItems(billDao.queryItems(uuid));
    PersistenceUtils.checkVersion(version, acceptanceBill, "领用单", acceptanceBill.getBillNumber());
    if (AcceptanceBillState.Approved.equals(acceptanceBill.getState()) == false
        && AcceptanceBillState.Initial.equals(acceptanceBill.getState()) == false)
      throw new WMSException("领用单" + acceptanceBill.getBillNumber() + "状态不是初始或者已批准，不能配货！");

    acceptanceHandler.generateAndSavePickUpTasks(acceptanceBill);
    for (AcceptanceBillItem acceptanceItem : acceptanceBill.getItems()) {
      if (acceptanceItem.getPlanQty().compareTo(BigDecimal.ZERO) <= 0)
        continue;
      billDao.updateItem(acceptanceItem);
    }

    acceptanceBill.setState(AcceptanceBillState.InAlc);
    acceptanceBill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    billDao.update(acceptanceBill);

    logger.injectContext(this, uuid, AcceptanceBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log("配货", "领用单配货");
  }

  @Override
  public void finish(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(version, "version");

    AcceptanceBill acceptanceBill = get(uuid);
    if (acceptanceBill == null)
      throw new WMSException("领用单不存在");

    PersistenceUtils.checkVersion(version, acceptanceBill, "领用单", acceptanceBill.getBillNumber());
    if (AcceptanceBillState.Aborted.equals(acceptanceBill.getState())
        || AcceptanceBillState.Finished.equals(acceptanceBill.getState()))
      throw new WMSException("领用单" + acceptanceBill.getBillNumber() + "状态是"
          + acceptanceBill.getState().getCaption() + "，不能完成！");

    if (AcceptanceBillState.Approved.equals(acceptanceBill.getState()))
      acceptanceHandler.shiftOutOnWayStock(acceptanceBill);
    if (AcceptanceBillState.InAlc.equals(acceptanceBill.getState()))
      acceptanceHandler.abortUnFinishPickTasks(acceptanceBill.getUuid());

    acceptanceBill.setState(AcceptanceBillState.Finished);
    acceptanceBill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    billDao.update(acceptanceBill);

    logger.injectContext(this, uuid, AcceptanceBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log("完成", "完成领用单");
  }

  @Override
  public void abort(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(version, "version");

    AcceptanceBill acceptanceBill = billDao.get(uuid);
    if (acceptanceBill == null)
      throw new WMSException("领用单不存在");

    acceptanceBill.setItems(billDao.queryItems(uuid));
    PersistenceUtils.checkVersion(version, acceptanceBill, "领用单", acceptanceBill.getBillNumber());
    if (acceptanceBill.getState().equals(AcceptanceBillState.Initial) == false
        && acceptanceBill.getState().equals(AcceptanceBillState.Approved) == false)
      throw new WMSException("领用单" + acceptanceBill.getBillNumber() + "状态不是初始或已批准，不能作废！");

    if (AcceptanceBillState.Approved.equals(acceptanceBill.getState()))
      acceptanceHandler.shiftOutOnWayStock(acceptanceBill);

    acceptanceBill.setState(AcceptanceBillState.Aborted);
    acceptanceBill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    billDao.update(acceptanceBill);

    logger.injectContext(this, uuid, AcceptanceBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_ABORT, "作废领用单");
  }

  @Override
  public void pickUp(String uuid, Map<String, BigDecimal> itemPickQty)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(itemPickQty, "itemPickQty");

    AcceptanceBill acceptanceBill = get(uuid);
    if (acceptanceBill == null)
      throw new WMSException("拣货的来源单据不存在！");
    if (acceptanceBill.getState().equals(AcceptanceBillState.InAlc))
      throw new WMSException("要货单状态不是待配货，无法拣货！");

    for (String itemUuid : itemPickQty.keySet()) {
      AcceptanceBillItem pickItem = findAcceptanceItem(acceptanceBill.getItems(), itemUuid);
      if (pickItem == null)
        throw new WMSException("拣货的要货单明细不存在！");
      pickItem.setRealQty(pickItem.getRealQty().add(itemPickQty.get(itemUuid)));
      pickItem.setRealCaseQtyStr(
          QpcHelper.qtyToCaseQtyStr(pickItem.getRealQty(), pickItem.getQpcStr()));
      billDao.updateItem(pickItem);
    }
    acceptanceBill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    billDao.update(acceptanceBill);
    
    logger.injectContext(this, uuid, AcceptanceBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log("拣货", "拣货明细：" + itemPickQty.toString());
  }

  private AcceptanceBillItem findAcceptanceItem(List<AcceptanceBillItem> items, String itemUuid) {
    assert items != null;
    assert itemUuid != null;

    for (AcceptanceBillItem item : items) {
      if (item.getUuid().equals(itemUuid))
        return item;
    }
    return null;
  }
}