/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	OrderBillServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年4月13日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.in.order;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.bin.BinService;
import com.hd123.sardine.wms.api.basicInfo.bin.Wrh;
import com.hd123.sardine.wms.api.basicInfo.supplier.Supplier;
import com.hd123.sardine.wms.api.basicInfo.supplier.SupplierService;
import com.hd123.sardine.wms.api.in.order.OrderBill;
import com.hd123.sardine.wms.api.in.order.OrderBillItem;
import com.hd123.sardine.wms.api.in.order.OrderBillService;
import com.hd123.sardine.wms.api.in.order.OrderBillState;
import com.hd123.sardine.wms.common.entity.OperateInfo;
import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.common.utils.QpcHelper;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.dao.in.order.OrderBillDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.log.EntityLogger;

/**
 * 订单服务：实现
 * 
 * @author zhangsai
 *
 */
public class OrderBillServiceImpl extends BaseWMSService implements OrderBillService {

  @Autowired
  private BinService binService;

  @Autowired
  private SupplierService supplierService;

  @Autowired
  private OrderBillDao orderBillDao;

  @Autowired
  private EntityLogger logger;

  @Override
  public String insert(OrderBill orderBill) throws IllegalArgumentException, WMSException {
    Assert.assertArgumentNotNull(orderBill, "orderBill");

    orderBill.validate();
    verifyOrderBill(orderBill);

    orderBill.refreshCaseQtyStr();
    orderBill.setUuid(UUIDGenerator.genUUID());
    orderBill
        .setBillNumber(billNumberGenerator.allocateNextBillNumber(OrderBill.class.getSimpleName()));
    orderBill.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    orderBill.setState(OrderBillState.Initial);
    orderBill.setCreateInfo(OperateInfo.newInstance(ApplicationContextUtil.getOperateContext()));
    orderBill
        .setLastModifyInfo(OperateInfo.newInstance(ApplicationContextUtil.getOperateContext()));
    orderBillDao.insert(orderBill);

    for (OrderBillItem item : orderBill.getItems()) {
      item.setUuid(UUIDGenerator.genUUID());
      item.setOrderBillUuid(orderBill.getUuid());
    }
    orderBillDao.insertItems(orderBill.getItems());

    logger.injectContext(this, orderBill.getUuid(), OrderBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_ADDNEW, "新建订单");
    return orderBill.getUuid();
  }

  private void verifyOrderBill(OrderBill orderBill) throws WMSException {
    assert orderBill != null;

    Supplier supplier = supplierService.get(orderBill.getSupplier().getUuid());
    if (supplier == null)
      throw new WMSException("供应商" + orderBill.getSupplier().getCode() + "不存在。");

    Wrh wrh = binService.getWrh(orderBill.getWrh().getUuid());
    if (wrh == null)
      throw new WMSException("仓位" + orderBill.getWrh().getCode() + "不存在。");
  }

  @Override
  public void update(OrderBill orderBill)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(orderBill, "orderBill");
    Assert.assertArgumentNotNull(orderBill.getUuid(), "orderBill.uuid");
    Assert.assertArgumentNotNull(orderBill.getBillNumber(), "orderBill.billnumber");

    orderBill.validate();
    verifyOrderBill(orderBill);

    OrderBill oldOrderBill = orderBillDao.get(orderBill.getUuid());
    if (oldOrderBill == null)
      throw new WMSException("编辑的订单不存在！");
    PersistenceUtils.checkVersion(orderBill.getVersion(), oldOrderBill, "订单",
        orderBill.getBillNumber());

    orderBill.refreshCaseQtyStr();
    orderBill.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    orderBill.setState(OrderBillState.Initial);
    orderBill
        .setLastModifyInfo(OperateInfo.newInstance(ApplicationContextUtil.getOperateContext()));
    orderBillDao.update(orderBill);

    for (OrderBillItem item : orderBill.getItems()) {
      item.setUuid(UUIDGenerator.genUUID());
      item.setOrderBillUuid(orderBill.getUuid());
    }
    orderBillDao.removeItems(orderBill.getUuid());
    orderBillDao.insertItems(orderBill.getItems());

    logger.injectContext(this, orderBill.getUuid(), OrderBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "修改订单");
  }

  @Override
  public void remove(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");

    OrderBill oldOrderBill = orderBillDao.get(uuid);
    if (oldOrderBill == null)
      return;
    PersistenceUtils.checkVersion(version, oldOrderBill, "订单", oldOrderBill.getBillNumber());

    if (oldOrderBill.getState().equals(OrderBillState.Initial) == false)
      throw new WMSException("订单" + oldOrderBill.getBillNumber() + "状态不是初始，不能删除！");

    orderBillDao.remove(uuid, version);
    orderBillDao.removeItems(uuid);

    logger.injectContext(this, uuid, OrderBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_REMOVE, "删除订单");
  }

  @Override
  public OrderBill get(String uuid) {
    if (StringUtil.isNullOrBlank(uuid))
      return null;

    OrderBill orderBill = orderBillDao.get(uuid);
    if (orderBill == null)
      return null;

    orderBill.setItems(orderBillDao.queryItems(uuid));
    return orderBill;
  }

  @Override
  public OrderBill getByBillNumber(String billNumber) {
    if (StringUtil.isNullOrBlank(billNumber))
      return null;

    OrderBill orderBill = orderBillDao.getByBillNumber(billNumber);
    if (orderBill == null)
      return null;

    orderBill.setItems(orderBillDao.queryItems(orderBill.getUuid()));
    return orderBill;
  }

  @Override
  public PageQueryResult<OrderBill> query(PageQueryDefinition definition) {
    Assert.assertArgumentNotNull(definition, "definition");

    PageQueryResult<OrderBill> pgr = new PageQueryResult<OrderBill>();
    List<OrderBill> list = orderBillDao.query(definition);
    PageQueryUtil.assignPageInfo(pgr, definition);
    pgr.setRecords(list);
    return pgr;
  }

  @Override
  public void uploadStateToPreBookReg(String uuid, long version, Date bookDate)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");

    OrderBill oldOrderBill = orderBillDao.get(uuid);
    if (oldOrderBill == null)
      throw new WMSException("预约的订单不存在！");
    PersistenceUtils.checkVersion(version, oldOrderBill, "订单", oldOrderBill.getBillNumber());

    if (oldOrderBill.getState().equals(OrderBillState.Initial) == false)
      throw new WMSException("订单" + oldOrderBill.getBillNumber() + "状态不是初始，不能预约！");

    oldOrderBill.setBookedDate(bookDate);
    oldOrderBill
        .setLastModifyInfo(OperateInfo.newInstance(ApplicationContextUtil.getOperateContext()));
    oldOrderBill.setState(OrderBillState.PreBookReg);
    orderBillDao.update(oldOrderBill);

    logger.injectContext(this, uuid, OrderBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "订单预约");
  }

  @Override
  public void uploadStateToPreChecked(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");

    OrderBill oldOrderBill = orderBillDao.get(uuid);
    if (oldOrderBill == null)
      throw new WMSException("预检的订单不存在！");
    PersistenceUtils.checkVersion(version, oldOrderBill, "订单", oldOrderBill.getBillNumber());

    if (oldOrderBill.getState().equals(OrderBillState.Initial) == false
        && oldOrderBill.getState().equals(OrderBillState.PreBookReg) == false)
      throw new WMSException("订单" + oldOrderBill.getBillNumber() + "状态不是初始或者已预约，不能预检！");

    oldOrderBill
        .setLastModifyInfo(OperateInfo.newInstance(ApplicationContextUtil.getOperateContext()));
    oldOrderBill.setState(OrderBillState.PreChecked);
    orderBillDao.update(oldOrderBill);

    logger.injectContext(this, uuid, OrderBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "订单预检");
  }

  @Override
  public void upload_finish(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");

    OrderBill oldOrderBill = orderBillDao.get(uuid);
    if (oldOrderBill == null)
      throw new WMSException("完成的订单不存在！");
    PersistenceUtils.checkVersion(version, oldOrderBill, "订单", oldOrderBill.getBillNumber());

    if (oldOrderBill.getState().equals(OrderBillState.Finished))
      return;
    if (oldOrderBill.getState().equals(OrderBillState.Aborted))
      throw new WMSException("订单" + oldOrderBill.getBillNumber() + "已作废，不能完成！");

    oldOrderBill
        .setLastModifyInfo(OperateInfo.newInstance(ApplicationContextUtil.getOperateContext()));
    oldOrderBill.setState(OrderBillState.Finished);
    orderBillDao.update(oldOrderBill);

    logger.injectContext(this, uuid, OrderBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "完成订单");

    // TODO 完成进行中的收货单，针对app收货
  }

  @Override
  public void uploadStateToAborted(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");

    OrderBill oldOrderBill = orderBillDao.get(uuid);
    if (oldOrderBill == null)
      throw new WMSException("作废的订单不存在！");
    PersistenceUtils.checkVersion(version, oldOrderBill, "订单", oldOrderBill.getBillNumber());

    if (oldOrderBill.getState().equals(OrderBillState.Initial) == false
        && oldOrderBill.getState().equals(OrderBillState.PreBookReg) == false)
      throw new WMSException("订单" + oldOrderBill.getBillNumber() + "状态不是初始或者已预约，不能作废！");

    oldOrderBill
        .setLastModifyInfo(OperateInfo.newInstance(ApplicationContextUtil.getOperateContext()));
    oldOrderBill.setState(OrderBillState.Aborted);
    orderBillDao.update(oldOrderBill);

    logger.injectContext(this, uuid, OrderBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "订单作废");
  }

  @Override
  public void receive(String billNumber, String articleUuid, String qpcStr, BigDecimal qty)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(billNumber, "billNumber");
    Assert.assertArgumentNotNull(articleUuid, "articleUuid");
    Assert.assertArgumentNotNull(qpcStr, "qpcStr");
    Assert.assertArgumentNotNull(qty, "qty");

    if (qty.compareTo(BigDecimal.ZERO) <= 0)
      throw new WMSException("收货数量" + qty + "必须大于0！");

    OrderBill orderBill = orderBillDao.getByBillNumber(billNumber);
    if (orderBill == null)
      throw new WMSException("订单" + billNumber + "不存在！");
    if (orderBill.getState().equals(OrderBillState.Aborted)
        || orderBill.getState().equals(OrderBillState.Finished))
      throw new WMSException("订单" + billNumber + "已完成或者已作废，不能再收货！");
    List<OrderBillItem> items = orderBillDao.queryItems(orderBill.getUuid());
    for (OrderBillItem item : items) {
      if (item.getArticle().getUuid().equals(articleUuid) && item.getQpcStr().equals(qpcStr)) {
        if (item.getQty().subtract(item.getReceivedQty()).compareTo(qty) < 0)
          throw new WMSException("商品" + item.getArticle().getCode() + "待收貨數量"
              + item.getQty().subtract(item.getReceivedQty()) + "小於本次收貨數量" + qty);
        item.setReceivedQty(item.getReceivedQty().add(qty));
        item.setReceivedCaseQtyStr(QpcHelper.qtyToCaseQtyStr(item.getReceivedQty(), qpcStr));
        orderBillDao.updateItem(item.getUuid(), item.getReceivedQty(),
            item.getReceivedCaseQtyStr());
        break;
      }
    }

    boolean receiveFinish = true;
    for (OrderBillItem item : items) {
      if (item.getQty().subtract(item.getReceivedQty()).compareTo(BigDecimal.ZERO) > 0)
        receiveFinish = false;
    }

    if (receiveFinish)
      orderBill.setState(OrderBillState.Finished);
    else
      orderBill.setState(OrderBillState.InProgress);
    orderBill.setReceivedCaseQtyStr(
        QpcHelper.caseQtyStrAdd(StringUtil.isNullOrBlank(orderBill.getReceivedCaseQtyStr()) ? "0"
            : orderBill.getReceivedCaseQtyStr(), QpcHelper.qtyToCaseQtyStr(qty, qpcStr)));
    orderBill
        .setLastModifyInfo(OperateInfo.newInstance(ApplicationContextUtil.getOperateContext()));
    orderBillDao.update(orderBill);

    logger.injectContext(this, orderBill.getUuid(), OrderBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "订单收货");
  }
}
