/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-api
 * 文件名：	PutawayService.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月19日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.api.in.putaway;

import java.math.BigDecimal;

import com.hd123.sardine.wms.common.exception.WMSException;

/**
 * 上架算法接口
 * 
 * @author zhangsai
 *
 */
public interface PutawayService {

  /**
   * 根据商品uuid获取商品的上架目标货位并锁定该货位
   * <p>
   * <ul>
   * <li>查询商品的上架属性：存储位、拣货位、优先拣货位</li>
   * <li>存储位：按照商品存储区域、商品类别存储区域、拣货分区的先后顺序查找对应的存储区域内距离商品拣货位最近的空闲存储位，如果没有则返回null
   * </li>
   * <li>拣货位：返回商品的固定拣货位</li>
   * <li>优先拣货位：拣货位该商品库存+待上架库存>商品最高库存 ？按存储位算法查找 : 返回固定拣货位</li>
   * </ul>
   * 
   * @param articleUuid
   *          商品UUID， not null
   * @param qty
   *          待上架数量， not null
   * @return 找到的上架目标货位，找不到返回null
   * @throws IllegalArgumentException
   *           参数为空
   * @throws WMSException
   *           商品无固定拣货位、商品和商品类别和拣货分区都未设置对应的存储区域
   */
  String fetchPutawayTargetBinByArticle(String articleUuid, BigDecimal qty)
      throws IllegalArgumentException, WMSException;

  /**
   * 根据容器取上架目标货位并锁定该货位
   * <p>
   * 取容器中商品和库存数量按商品上架获取目标位置
   * 
   * @param containerBarcode
   *          容器条码， not null
   * @return 上架目标位置，找不到返回null
   * @throws IllegalArgumentException
   * @throws WMSException
   *           容器混载时抛出
   */
  String fetchPutawayTargetBinByContainer(String containerBarcode)
      throws IllegalArgumentException, WMSException;

  /**
   * 当实际上架的目标货位和推荐的货位不一致时进行校验
   * 
   * @param newTargetBinCode
   *          新的目标位置， not null
   * @param articleUuid
   *          上架的商品UUID， not null
   * @throws IllegalArgumentException
   * @throws WMSException
   *           新目标货位不符合上架规则
   */
  void verifyNewTargetBin(String newTargetBinCode, String articleUuid)
      throws IllegalArgumentException, WMSException;
}
