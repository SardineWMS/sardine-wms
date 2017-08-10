/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-core
 * 文件名：	RplCollector.java
 * 模块说明：	
 * 修改历史：
 * 2014-8-20 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.out.wave;

import java.util.List;

/**
 * 补货信息收集器
 * 
 * @author zhangsai
 * 
 */
interface RplCollector {

  /**
   * 货位待补货信息
   * <p>
   * 货位：整件拣货位、拆零拣货位<br>
   * 数量：拣货单拣货数量-货位当前可以库存数量<br>
   * 针对单个商品处理
   * 
   * @return 补货信息集合
   */
  List<RplRequestInfo> getRplInfos();

  /**
   * 补货来源库存信息
   * <p>
   * 货位：存储位<br>
   * 
   * @return 商品库存信息
   */
  List<RplStockInfo> getStockInfos();
}
