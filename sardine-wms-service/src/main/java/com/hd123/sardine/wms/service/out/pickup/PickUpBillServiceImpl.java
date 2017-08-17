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

import com.hd123.sardine.wms.api.out.pickup.PickUpBill;
import com.hd123.sardine.wms.api.out.pickup.PickUpBillFilter;
import com.hd123.sardine.wms.api.out.pickup.PickUpBillService;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.service.ia.BaseWMSService;

/**
 * @author zhangsai
 *
 */
public class PickUpBillServiceImpl extends BaseWMSService implements PickUpBillService {

  @Override
  public void saveNew(PickUpBill pickUpBill) throws WMSException {

  }

  @Override
  public PickUpBill get(String uuid) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public PickUpBill getByBillNumber(String billNumber) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public PageQueryResult<PickUpBill> query(PickUpBillFilter filter) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void approveByWaveBillNumber(String waveBillNumber) {
    // TODO Auto-generated method stub

  }

  @Override
  public void audit(String billNumber) throws WMSException {
    // TODO Auto-generated method stub

  }

  @Override
  public void removeByWaveUuid(String waveUuid) {
    // TODO Auto-generated method stub

  }

}
