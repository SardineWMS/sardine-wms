/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	PutawayServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月20日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.in.putaway;

import java.math.BigDecimal;

import com.hd123.sardine.wms.api.in.putaway.PutawayService;
import com.hd123.sardine.wms.common.exception.WMSException;

/**
 * 上架算法实现
 * 
 * @author zhangsai
 *
 */
public class PutawayServiceImpl implements PutawayService {

  @Override
  public String fetchPutawayTaegetBinByArticle(String articleUuid, BigDecimal qty)
      throws IllegalArgumentException, WMSException {
    return null;
  }

  @Override
  public String fetchPutawayTaegetBinByContainer(String containerBarcode)
      throws IllegalArgumentException, WMSException {
    return null;
  }

  @Override
  public void verifyNewTargetBin(String newTargetBinCode, String articleUuid)
      throws IllegalArgumentException, WMSException {

  }
}
