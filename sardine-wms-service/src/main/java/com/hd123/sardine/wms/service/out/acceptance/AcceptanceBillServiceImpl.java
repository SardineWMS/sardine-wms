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
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.customer.Customer;
import com.hd123.sardine.wms.api.basicInfo.customer.CustomerService;
import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBill;
import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBillItem;
import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBillService;
import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBillState;
import com.hd123.sardine.wms.api.out.alcntc.AlcNtcBill;
import com.hd123.sardine.wms.common.entity.UCN;
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

  @Autowired
  private CustomerService customerService;
  
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
	  verifyCustomer(acceptanceBill);
    acceptanceVerifier.verifyAcceptanceBill(acceptanceBill);

    acceptanceBill.setUuid(UUIDGenerator.genUUID());
    acceptanceBill.setState(AcceptanceBillState.Initial);
    acceptanceBill.setBillNumber(
        billNumberGenerator.allocateNextBillNumber(AcceptanceBill.class.getSimpleName()));
    acceptanceBill.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
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
    verifyCustomer(acceptanceBill);
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

    acceptanceHandler.lockStock(acceptanceBill);
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
    if (AcceptanceBillState.Initial.equals(acceptanceBill.getState()) == false)
      throw new WMSException("领用单" + acceptanceBill.getBillNumber() + "状态是"
          + acceptanceBill.getState().getCaption() + "，不能完成！");

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
      acceptanceHandler.rollBackStock(acceptanceBill);

    acceptanceBill.setState(AcceptanceBillState.Aborted);
    acceptanceBill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    billDao.update(acceptanceBill);

    logger.injectContext(this, uuid, AcceptanceBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_ABORT, "作废领用单");
  }

  @Override
  public void pickUp(String itemUuid, BigDecimal qty)
      throws IllegalArgumentException, WMSException {
    Assert.assertArgumentNotNull(itemUuid, "itemUuid");
    Assert.assertArgumentNotNull(qty, "qty");

    AcceptanceBill acceptanceBill = billDao.getByItemUuid(itemUuid);
    if (acceptanceBill == null)
      throw new WMSException("拣货的来源单据不存在！");
    if (acceptanceBill.getState().equals(AcceptanceBillState.InAlc) == false)
      throw new WMSException("要货单状态不是待配货，无法拣货！");

    List<AcceptanceBillItem> items = billDao.queryItems(acceptanceBill.getUuid());
    for (AcceptanceBillItem item : items) {
      if (item.getUuid().equals(itemUuid) == false)
        continue;
      item.setRealQty(item.getRealQty().add(qty));
      item.setRealCaseQtyStr(QpcHelper.qtyToCaseQtyStr(item.getRealQty(), item.getQpcStr()));
      billDao.updateItem(item);
    }
    acceptanceBill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    billDao.update(acceptanceBill);

    logger.injectContext(this, acceptanceBill.getUuid(), AcceptanceBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log("拣货", "明细：" + itemUuid + "，数量：" + qty);
  }
  
  private void verifyCustomer(AcceptanceBill acceptanceBill) throws WMSException {
	    assert acceptanceBill != null;
	    assert acceptanceBill.getItems() != null && acceptanceBill.getItems().isEmpty();

	    Customer customer = customerService.getByCode(acceptanceBill.getCustomer().getCode());
	    if (Objects.isNull(customer))
	      throw new WMSException(
	          MessageFormat.format("不存在代码是{0}的客户！", acceptanceBill.getCustomer().getCode()));
	    acceptanceBill.setCustomer(new UCN(customer.getUuid(), customer.getCode(), customer.getName()));
	  }
}