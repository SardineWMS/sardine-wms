/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-core
 * 文件名：	RplBillItemDao.java
 * 模块说明：	
 * 修改历史：
 * 2014-8-12 - WUJING - 创建。
 */
package com.hd123.sardine.wms.dao.out.rpl;

import java.util.List;

import com.hd123.sardine.wms.api.out.rpl.RplBillItem;

/**
 * @author WUJING
 *
 */
public interface RplBillItemDao {

  void saveNew(List<RplBillItem> items);

  List<RplBillItem> queryByRplUuid(String rplUuid);

  void removeByWaveBillNumber(String waveBillNumber);

  void saveModify(RplBillItem item);

  List<RplBillItem> queryRplItems(String waveBillNumber);
  
  List<RplBillItem> queryByUuids(List<String> uuids);
}
