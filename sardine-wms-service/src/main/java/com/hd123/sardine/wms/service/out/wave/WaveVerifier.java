/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	WaveVerifier.java
 * 模块说明：	
 * 修改历史：
 * 2017年7月21日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.out.wave;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.out.alcntc.AlcNtcBill;
import com.hd123.sardine.wms.api.out.alcntc.AlcNtcBillService;
import com.hd123.sardine.wms.api.out.alcntc.AlcNtcBillState;
import com.hd123.sardine.wms.api.out.wave.WaveBill;
import com.hd123.sardine.wms.api.out.wave.WaveNtcItem;
import com.hd123.sardine.wms.api.tms.serialarch.SerialArch;
import com.hd123.sardine.wms.api.tms.serialarch.SerialArchService;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.exception.WMSException;

/**
 * @author zhangsai
 *
 */
public class WaveVerifier {

  @Autowired
  private AlcNtcBillService alcNtcService;
  @Autowired
  private SerialArchService serialArchService;

  public void verifyWaveBill(WaveBill waveBill) throws WMSException {
    Assert.assertArgumentNotNull(waveBill, "waveBill");

    waveBill.validate();
    SerialArch arch = serialArchService.get("3eeb552e059647d4ac62ed97650c55c5");// TODO
                                                                                // 去掉线路体系的概念，暂时指定一个线路体系
    if (arch == null)
      throw new WMSException("波次对应的线路体系" + waveBill.getSerialArch().getCode() + "不存在！");

    waveBill.setSerialArch(new UCN(arch.getUuid(), arch.getCode(), arch.getName()));

    for (WaveNtcItem waveItem : waveBill.getNtcItems()) {
      AlcNtcBill alcNtcBill = alcNtcService.getByBillNumber(waveItem.getNtcBillNumber());
      if (alcNtcBill == null)
        throw new WMSException("配货通知单" + waveItem.getNtcBillNumber() + "不存在！");
      if (alcNtcBill.getState().equals(AlcNtcBillState.initial) == false
          && alcNtcBill.getState().equals(AlcNtcBillState.used) == false)
        throw new WMSException("配货通知单" + waveItem.getNtcBillNumber() + "状态不是初始或者已使用！");
      if (StringUtil.isNullOrBlank(alcNtcBill.getWaveBillNumber()) == false
          && StringUtil.isNullOrBlank(waveBill.getBillNumber()) == false
          && alcNtcBill.getWaveBillNumber().equals(waveBill.getBillNumber()) == false)
        throw new WMSException("配货通知单" + waveItem.getNtcBillNumber() + "已被波次单"
            + alcNtcBill.getWaveBillNumber() + "占用！");
    }
  }
}
