/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	PickUpBillServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月17日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.out.pickup;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.out.pickup.PickUpBill;
import com.hd123.sardine.wms.api.out.pickup.PickUpBillItem;
import com.hd123.sardine.wms.api.out.pickup.PickUpBillService;
import com.hd123.sardine.wms.api.out.pickup.PickUpBillState;
import com.hd123.sardine.wms.api.task.TaskView;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.dao.out.pickup.PickUpBillDao;
import com.hd123.sardine.wms.dao.out.pickup.PickUpBillItemDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;

/**
 * @author zhangsai
 *
 */
public class PickUpBillServiceImpl extends BaseWMSService implements PickUpBillService {

  @Autowired
  private PickUpBillDao pickUpBillDao;

  @Autowired
  private PickUpBillItemDao pickUpBillItemDao;

  @Override
  public void saveNew(PickUpBill pickUpBill) throws WMSException {
    Assert.assertArgumentNotNull(pickUpBill, "pickUpBill");

    pickUpBill.validate();
    pickUpBill.setUuid(UUIDGenerator.genUUID());
    pickUpBill.setBillNumber(
        billNumberGenerator.allocateNextBillNumber(PickUpBill.class.getSimpleName()));
    pickUpBill.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
    pickUpBill.setCreateInfo(ApplicationContextUtil.getOperateInfo());
    pickUpBill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());

    for (int i = 0; i < pickUpBill.getItems().size(); i++) {
      PickUpBillItem pickItem = pickUpBill.getItems().get(i);
      pickItem.setUuid(UUIDGenerator.genUUID());
      pickItem.setLine(i + 1);
      pickItem.setPickUpBillUuid(pickUpBill.getUuid());
    }

    pickUpBillDao.saveNew(pickUpBill);
    pickUpBillItemDao.saveNew(pickUpBill.getItems());
  }

  @Override
  public PickUpBill get(String uuid) {
    if (StringUtil.isNullOrBlank(uuid))
      return null;

    PickUpBill bill = pickUpBillDao.get(uuid);
    if (bill == null)
      return null;
    bill.setItems(pickUpBillItemDao.queryByPickUpBill(uuid));
    return bill;
  }

  @Override
  public PickUpBill getByBillNumber(String billNumber) {
    if (StringUtil.isNullOrBlank(billNumber))
      return null;

    PickUpBill bill = pickUpBillDao.getByBillNumber(billNumber);
    if (bill == null)
      return null;
    bill.setItems(pickUpBillItemDao.queryByPickUpBill(bill.getUuid()));
    return bill;
  }

  @Override
  public void approveByWaveBillNumber(String waveBillNumber) {
    Assert.assertArgumentNotNull(waveBillNumber, "waveBillNumber");

    pickUpBillDao.approveByWaveBillNumber(waveBillNumber);
  }

  @Override
  public void audit(String uuid, long version) throws WMSException {
    Assert.assertArgumentNotNull(uuid, "uuid");
  }

  @Override
  public void removeByWaveUuid(String waveUuid) throws WMSException {
    if (StringUtil.isNullOrBlank(waveUuid))
      return;

    List<PickUpBill> pickUpBills = pickUpBillDao.queryByWaveUuid(waveUuid);
    for (PickUpBill bill : pickUpBills) {
      if (bill.getState().equals(PickUpBillState.inConfirm) == false)
        throw new WMSException("拣货单状态不是待确认，不能删除！");

      pickUpBillDao.remove(bill.getUuid(), bill.getVersion());
      pickUpBillItemDao.removeByPickUpBill(bill.getUuid());
    }
  }

  @Override
  public List<TaskView> queryPickTaskView(String waveBillNumber) {
    if (StringUtil.isNullOrBlank(waveBillNumber))
      return new ArrayList<TaskView>();
    return pickUpBillDao.queryPickTaskView(waveBillNumber);
  }
}
