/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	SupplierReturnBillImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月21日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.service.rtn.supplierreturn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBill;
import com.hd123.sardine.wms.api.rtn.returnsupplier.HandoverTaskFilter;
import com.hd123.sardine.wms.api.rtn.returnsupplier.ReturnSupplierBill;
import com.hd123.sardine.wms.api.rtn.returnsupplier.ReturnSupplierBillItem;
import com.hd123.sardine.wms.api.rtn.returnsupplier.ReturnSupplierBillService;
import com.hd123.sardine.wms.api.rtn.returnsupplier.ReturnSupplierBillState;
import com.hd123.sardine.wms.api.tms.shipbill.ShipBillState;
import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.dao.rtn.returnsupplier.SupplierReturnBillDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.log.EntityLogger;

/**
 * @author fanqingqing
 *
 */
public class SupplierReturnBillServiceImpl extends BaseWMSService
    implements ReturnSupplierBillService {
  @Autowired
  private SupplierReturnBillDao dao;

  @Autowired
  private EntityLogger logger;

  @Autowired
  private SupplierReturnBillVerifier supplierReturnBillVerifier;

  @Autowired
  private SupplierReturnBillHandler supplierReturnBillHandler;

  @Override
  public ReturnSupplierBill getByUuid(String uuid) {
    if (StringUtil.isNullOrBlank(uuid))
      return null;
    ReturnSupplierBill bill = dao.get(uuid);
    if (bill == null)
      return null;
    bill.setItems(dao.queryItems(uuid));
    return bill;
  }

  @Override
  public ReturnSupplierBill getByBillNumber(String billNumber) {
    if (StringUtil.isNullOrBlank(billNumber))
      return null;
    ReturnSupplierBill bill = dao.getByBillNumber(billNumber);
    if (bill == null)
      return null;
    bill.setItems(dao.queryItems(bill.getUuid()));
    return bill;
  }

  @Override
  public PageQueryResult<ReturnSupplierBill> query(PageQueryDefinition definition)
      throws IllegalArgumentException {
    Assert.assertArgumentNotNull(definition, "definition");
    definition.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    List<ReturnSupplierBill> list = dao.query(definition);
    PageQueryResult<ReturnSupplierBill> pqr = new PageQueryResult<>();
    PageQueryUtil.assignPageInfo(pqr, definition);
    pqr.setRecords(list);
    return pqr;
  }

  @Override
  public void finish(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(version, "version");

    ReturnSupplierBill bill = getByUuid(uuid);
    if (bill == null)
      throw new WMSException("不存在");

    PersistenceUtils.checkVersion(version, bill, "供应商退货单", bill.getBillNumber());
    if (ShipBillState.Finished.equals(bill.getState()))
      throw new WMSException(
          "供应商退货单" + bill.getBillNumber() + "状态是" + bill.getState().getCaption() + "，不能完成！");

    if (ReturnSupplierBillState.Initial.equals(bill.getState())) {
      supplierReturnBillHandler.stockOut(bill, bill.getItems());
      supplierReturnBillHandler.releaseBinAndContainer(bill, bill.getItems());
    }

    bill.setState(ReturnSupplierBillState.Finished);
    bill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
    dao.update(bill);

    logger.injectContext(this, uuid, AcceptanceBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log("完成", "完成供应商退货单");
  }

  @Override
  public void generateReturnSupplierBill(List<ReturnSupplierBillItem> returnHandoverItems)
      throws IllegalArgumentException, WMSException {
    Assert.assertArgumentNotNull(returnHandoverItems, "returnHandoverItems");

    supplierReturnBillVerifier.verifyHandOverItems(returnHandoverItems);

    List<ReturnSupplierBill> result = supplierReturnBillHandler
        .generateReturnSupplierBill(returnHandoverItems);
    for (ReturnSupplierBill bill : result) {
      bill.setUuid(UUIDGenerator.genUUID());
      bill.setBillNumber(
          billNumberGenerator.allocateNextBillNumber(ReturnSupplierBill.class.getSimpleName()));
      bill.setCreateInfo(ApplicationContextUtil.getOperateInfo());
      bill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());

      dao.insert(bill);

      for (int i = 0; i < bill.getItems().size(); i++) {
        ReturnSupplierBillItem item = bill.getItems().get(i);
        item.setUuid(UUIDGenerator.genUUID());
        item.setReturnSupplierBillUuid(bill.getUuid());
        item.setLine(i + 1);
      }
      dao.insertItems(bill.getItems());

      logger.injectContext(this, bill.getUuid(), ReturnSupplierBill.class.getName(),
          ApplicationContextUtil.getOperateContext());
      logger.log("新增", "供应商退货单");

      finish(bill.getUuid(), bill.getVersion());
    }
  }

  @Override
  public PageQueryResult<ReturnSupplierBillItem> queryWaitHandoverItems(HandoverTaskFilter filter)
      throws IllegalArgumentException {
    Assert.assertArgumentNotNull(filter, "filter");
    filter.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    List<ReturnSupplierBillItem> list = dao.queryWaitHandoverItems(filter);
    PageQueryResult<ReturnSupplierBillItem> pqr = new PageQueryResult<ReturnSupplierBillItem>();
    PageQueryUtil.assignPageInfo(pqr, filter);
    pqr.setRecords(list);
    return pqr;
  }
}
