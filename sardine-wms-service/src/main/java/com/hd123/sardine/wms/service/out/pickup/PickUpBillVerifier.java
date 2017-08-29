/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	PickUpBillVerifer.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月25日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.out.pickup;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.basicInfo.bin.Bin;
import com.hd123.sardine.wms.api.basicInfo.bin.BinService;
import com.hd123.sardine.wms.api.basicInfo.bin.BinUsage;
import com.hd123.sardine.wms.api.basicInfo.container.Container;
import com.hd123.sardine.wms.api.basicInfo.container.ContainerService;
import com.hd123.sardine.wms.api.basicInfo.container.ContainerState;
import com.hd123.sardine.wms.common.exception.WMSException;

/**
 * @author zhangsai
 *
 */
public class PickUpBillVerifier {

  @Autowired
  private BinService binService;

  @Autowired
  private ContainerService containerService;

  public void verifyPickBinAndContainer(String toBinCode, String containerBarcode)
      throws WMSException {
    Assert.assertArgumentNotNull(toBinCode, "toBinCode");
    Assert.assertArgumentNotNull(containerBarcode, "containerBarcode");

    Bin toBin = binService.getBinByCode(toBinCode);
    if (toBin == null)
      throw new WMSException("拣货的目标货位" + toBinCode + "不存在！");
    if (toBin.getUsage().equals(BinUsage.CollectBin) == false)
      throw new WMSException("拣货的目标货位" + toBinCode + "不是集货位！");

    Container container = containerService.getByBarcode(containerBarcode);
    if (container == null)
      throw new WMSException("拣货的目标容器" + containerBarcode + "不存在！");
    if (container.getState().equals(ContainerState.STACONTAINERIDLE) == false)
      throw new WMSException("拣货的目标容器" + containerBarcode + "不是空闲容器！");

    containerService.lock(container.getUuid(), container.getVersion());
  }
}
