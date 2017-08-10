/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	WaveHandler.java
 * 模块说明：	
 * 修改历史：
 * 2017年7月21日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.out.wave;

import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.out.alcntc.AlcNtcBill;
import com.hd123.sardine.wms.api.out.alcntc.AlcNtcBillService;
import com.hd123.sardine.wms.api.out.alcntc.AlcNtcBillState;
import com.hd123.sardine.wms.api.out.wave.WaveBill;
import com.hd123.sardine.wms.api.out.wave.WaveBillItem;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.dao.out.wave.PickTask;
import com.hd123.sardine.wms.dao.out.wave.RplTask;

/**
 * @author zhangsai
 *
 */
public class WaveHandler {

  @Autowired
  private AlcNtcBillService alcNtcService;

  public void joinWave(WaveBill waveBill, WaveBill oldBill) throws WMSException {
    Assert.assertArgumentNotNull(waveBill, "waveBill");

    for (WaveBillItem waveItem : waveBill.getItems()) {
      AlcNtcBill alcNtcBill = alcNtcService.getByBillNumber(waveItem.getAlcNtcBillNumber());
      if (alcNtcBill == null)
        throw new WMSException("配货通知单" + waveItem.getAlcNtcBillNumber() + "不存在！");
      if (alcNtcBill.getState().equals(AlcNtcBillState.used)
          && alcNtcBill.getWaveBillNumber().equals(waveBill.getBillNumber()))
        continue;
      alcNtcService.joinWave(alcNtcBill.getBillNumber(), alcNtcBill.getVersion(),
          waveBill.getBillNumber());
    }

    if (oldBill == null)
      return;

    for (WaveBillItem waveItem : oldBill.getItems()) {
      if (contain(waveBill.getItems(), waveItem))
        continue;

      AlcNtcBill alcNtcBill = alcNtcService.getByBillNumber(waveItem.getAlcNtcBillNumber());
      if (alcNtcBill == null)
        throw new WMSException("配货通知单" + waveItem.getAlcNtcBillNumber() + "不存在！");
      alcNtcService.leaveWave(alcNtcBill.getBillNumber(), alcNtcBill.getVersion());
    }
  }

  private boolean contain(List<WaveBillItem> nowItems, WaveBillItem item) {
    assert nowItems != null;
    assert item != null;

    for (WaveBillItem waveItem : nowItems) {
      if (waveItem.getAlcNtcBillNumber().equals(item.getAlcNtcBillNumber()))
        return true;
    }
    return false;
  }

  public void leaveWave(WaveBill bill) throws WMSException {
    assert bill != null;
    for (WaveBillItem item : bill.getItems()) {
      AlcNtcBill alcNtcBill = alcNtcService.getByBillNumber(item.getAlcNtcBillNumber());
      if (alcNtcBill == null)
        throw new IllegalArgumentException(
            MessageFormat.format("通知单{0}不存在 ", item.getAlcNtcBillNumber()));

      alcNtcService.leaveWave(alcNtcBill.getBillNumber(), alcNtcBill.getVersion());
    }
  }

  public String generatorPickTaskInsertSql(List<PickTask> tasks) {
    if (CollectionUtils.isEmpty(tasks))
      return null;

    StringBuffer sql = new StringBuffer();
    for (int i = 0; i < tasks.size(); i++) {
      PickTask task = tasks.get(i);
      sql.append("(");
      sql.append(task.getUuid() + ",");
      sql.append(task.getCompanyUuid() + ",");
      sql.append(task.getArticle().getUuid() + ",");
      sql.append(task.getFromBinCode() + ",");
      sql.append(task.getFromContainerBarcode() + ",");
      sql.append(task.getOwner() + ",");
      sql.append(task.getToBinCode() + ",");
      sql.append(task.getToContainerBarcode() + ",");
      sql.append(task.getQty() + ",");
      sql.append(task.getCaseQtyStr() + ",");
      sql.append(task.getProductionDate() + ",");
      sql.append(task.getValidDate() + ",");
      sql.append(task.getStockBatch() + ",");
      
      sql.append(")");
      if (i < tasks.size() - 1)
        sql.append(",");
    }
    return sql.toString();
  }
  
  public String generatorRplTaskInsertSql(List<RplTask> tasks) {
    if (CollectionUtils.isEmpty(tasks))
      return null;

    StringBuffer sql = new StringBuffer();
    for (int i = 0; i < tasks.size(); i++) {
      RplTask task = tasks.get(i);
      sql.append("(");
      sql.append(task.getUuid() + ",");
      sql.append(task.getCompanyUuid() + ",");
      sql.append(task.getArticle().getUuid() + ",");
      sql.append(task.getFromBinCode() + ",");
      sql.append(task.getFromContainerBarcode() + ",");
      sql.append(task.getOwner() + ",");
      sql.append(task.getToBinCode() + ",");
      sql.append(task.getToContainerBarcode() + ",");
      sql.append(task.getQty() + ",");
      sql.append(task.getCaseQtyStr() + ",");
      sql.append(task.getProductionDate() + ",");
      sql.append(task.getValidDate() + ",");
      sql.append(task.getStockBatch() + ",");
      
      sql.append(")");
      if (i < tasks.size() - 1)
        sql.append(",");
    }
    return sql.toString();
  }
}
