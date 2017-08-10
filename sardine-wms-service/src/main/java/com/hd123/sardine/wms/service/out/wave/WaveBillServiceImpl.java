/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	WaveBillServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月21日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.service.out.wave;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.out.alcntc.AlcNtcBill;
import com.hd123.sardine.wms.api.out.alcntc.AlcNtcBillService;
import com.hd123.sardine.wms.api.out.alcntc.AlcNtcBillState;
import com.hd123.sardine.wms.api.out.wave.WaveBill;
import com.hd123.sardine.wms.api.out.wave.WaveBillItem;
import com.hd123.sardine.wms.api.out.wave.WaveBillService;
import com.hd123.sardine.wms.api.out.wave.WaveBillState;
import com.hd123.sardine.wms.api.out.wave.WaveType;
import com.hd123.sardine.wms.api.tms.serialarch.SerialArch;
import com.hd123.sardine.wms.api.tms.serialarch.SerialArchService;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.CollectionUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.dao.out.wave.WaveBillDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.log.EntityLogger;

/**
 * @author yangwenzhu
 *
 */
public class WaveBillServiceImpl extends BaseWMSService implements WaveBillService {
  @Autowired
  private WaveBillDao dao;
  @Autowired
  private EntityLogger logger;
  @Autowired
  private AlcNtcBillService alcNtcService;
  @Autowired
  private SerialArchService serialArchService;

  @Override
  public String saveNew(WaveBill bill) throws IllegalArgumentException, WMSException {
    Assert.assertArgumentNotNull(bill, "bill");

    bill.validate();
    SerialArch serialArch = null;
    if (Objects.nonNull(bill.getSerialArch().getUuid())) {
      serialArch = serialArchService.get(bill.getSerialArch().getUuid());
      bill.setSerialArch(new UCN(serialArch.getUuid(), serialArch.getCode(), serialArch.getName()));
    } else if (WaveType.eCommerce.equals(bill.getWaveType()) == false)
      throw new WMSException("非电商波次，线路体系不能为空");

    bill.setBillNumber(billNumberGenerator.allocateNextBillNumber(WaveBill.class.getSimpleName()));
    bill.setState(WaveBillState.initial);
    bill.setUuid(UUIDGenerator.genUUID());
    bill.setCreateInfo(ApplicationContextUtil.getOperateInfo());
    bill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());

    alcNtcJoinWave(bill);

    dao.insert(bill);
    for (WaveBillItem item : bill.getItems()) {
      item.setUuid(UUIDGenerator.genUUID());
      item.setWaveBillUuid(bill.getUuid());
      item.setAlcNtcBillState(AlcNtcBillState.inAlc);
    }
    dao.insertItems(bill.getItems());
    logger.injectContext(this, bill.getUuid(), WaveBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_ADDNEW, "新增波次单");
    return bill.getUuid();
  }

  private void alcNtcJoinWave(WaveBill bill) throws WMSException {/*
    assert bill != null;
    List<AlcNtcBill> list = alcNtcService.getByTaskBillNumber(bill.getBillNumber());
    List<String> uuids = CollectionUtil.toUuids(list);
    List<String> existUuids = new ArrayList<>();

    for (WaveBillItem item : bill.getItems()) {
      AlcNtcBill alcNtcBill = alcNtcService.getByBillNumber(item.getAlcNtcBillNumber());
      if (Objects.isNull(alcNtcBill))
        throw new IllegalArgumentException("通知单" + item.getAlcNtcBillNumber() + "不存在");

      if (uuids.contains(alcNtcBill.getUuid()) == false)
        alcNtcService.joinWave(alcNtcBill.getBillNumber(), alcNtcBill.getVersion(),
            bill.getBillNumber());

      existUuids.add(alcNtcBill.getUuid());
    }

    for (AlcNtcBill alcNtcBill : list) {
      if (existUuids.contains(alcNtcBill.getUuid()) == false)
        alcNtcService.leaveWave(alcNtcBill.getBillNumber(), alcNtcBill.getVersion());
    }
  */}

  @Override
  public void saveModify(WaveBill bill)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    Assert.assertArgumentNotNull(bill, "bill");

    bill.validate();
    WaveBill waveBill = dao.get(bill.getUuid());
    if (Objects.isNull(waveBill))
      throw new IllegalArgumentException("要修改的波次单" + waveBill.getUuid() + "不存在");
    PersistenceUtils.checkVersion(bill.getVersion(), waveBill, WaveBill.CAPTION, bill.getUuid());
    if (WaveBillState.initial.equals(bill.getState()) == false)
      throw new WMSException(
          MessageFormat.format("该波次单的状态是{0}，不能编辑，只有状态的补货单才能拿编辑", waveBill.getState().getCaption()));
    SerialArch serialArch = serialArchService.get(bill.getSerialArch().getUuid());
    bill.setSerialArch(new UCN(serialArch.getUuid(), serialArch.getCode(), serialArch.getName()));
    bill.setState(WaveBillState.initial);
    bill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());

    alcNtcJoinWave(bill);

    for (WaveBillItem item : bill.getItems()) {
      item.setUuid(UUIDGenerator.genUUID());
      item.setWaveBillUuid(bill.getUuid());
      item.setAlcNtcBillState(AlcNtcBillState.inAlc);
    }
    dao.update(bill);
    dao.removeItems(bill.getUuid());
    dao.insertItems(bill.getItems());

    logger.injectContext(this, waveBill.getUuid(), WaveBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_MODIFY, "修改波次单");
  }

  @Override
  public void remove(String uuid, long version) throws WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");
    Assert.assertArgumentNotNull(version, "version");

    WaveBill bill = get(uuid);
    if (Objects.isNull(bill))
      throw new IllegalArgumentException(MessageFormat.format("要删除的波次单{0}不存在", uuid));
    if (WaveBillState.initial.equals(bill.getState()) == false)
      throw new IllegalArgumentException(
          MessageFormat.format("当前波次单的状态是{0},不是初始，不能删除", bill.getState()));

    alcNtcLeaveWave(bill);

    dao.removeItems(uuid);
    dao.remove(uuid, version);

    logger.injectContext(this, uuid, WaveBill.class.getName(),
        ApplicationContextUtil.getOperateContext());
    logger.log(EntityLogger.EVENT_REMOVE, "删除波次单");
  }

  private void alcNtcLeaveWave(WaveBill bill) throws WMSException {
    assert bill != null;
    List<WaveBillItem> items = bill.getItems();
    for (WaveBillItem item : items) {
      AlcNtcBill alcNtcBill = alcNtcService.getByBillNumber(item.getAlcNtcBillNumber());
      if (Objects.isNull(alcNtcBill))
        throw new IllegalArgumentException(
            MessageFormat.format("通知单{0}不存在 ", item.getAlcNtcBillNumber()));

      alcNtcService.leaveWave(alcNtcBill.getBillNumber(), alcNtcBill.getVersion());
    }

  }

  @Override
  public WaveBill get(String uuid) {
    if (StringUtil.isNullOrBlank(uuid))
      return null;
    List<WaveBillItem> items = dao.getItemsByWaveBillUuid(uuid);
    WaveBill bill = dao.get(uuid);
    if (Objects.isNull(bill))
      return null;
    bill.setItems(items);
    return bill;
  }

  @Override
  public WaveBill getByBillNumber(String billNumber) {
    if (StringUtil.isNullOrBlank(billNumber))
      return null;
    WaveBill bill = dao.getByBillNumber(billNumber);
    if (Objects.isNull(bill))
      return null;
    List<WaveBillItem> items = dao.getItemsByWaveBillUuid(bill.getUuid());
    bill.setItems(items);
    return bill;
  }

  @Override
  public PageQueryResult<WaveBill> query(PageQueryDefinition definition) {
    Assert.assertArgumentNotNull(definition, "definition");
    List<WaveBill> list = dao.query(definition);
    PageQueryResult<WaveBill> pqr = new PageQueryResult<>();
    PageQueryUtil.assignPageInfo(pqr, definition);
    pqr.setRecords(list);
    return pqr;
  }

  @Override
  public List<String> start(String uuid, long version)
      throws IllegalArgumentException, VersionConflictException, WMSException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void executeArticle(String waveBillNumber, List<String> articleUuids) throws WMSException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void startComplete(String billNumber) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void startException(String billNumber, String errorMessage) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void rollBack(String uuid, long version) throws WMSException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void confirm(String uuid, long version) throws WMSException {
    // TODO Auto-generated method stub
    
  }

}
