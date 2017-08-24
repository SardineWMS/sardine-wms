/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-core
 * 文件名：	RplBillDao.java
 * 模块说明：	
 * 修改历史：
 * 2014-8-12 - WUJING - 创建。
 */
package com.hd123.sardine.wms.dao.out.rpl;

import java.util.List;

import com.hd123.sardine.wms.api.out.rpl.RplBill;
import com.hd123.sardine.wms.api.task.TaskView;

/**
 * @author WUJING
 * 
 */
public interface RplBillDao {
  void saveNew(RplBill rplBill);

  void saveModify(RplBill rplBill);

  RplBill get(String uuid);

  RplBill getByBillNumber(String billNumber);

  void removeByWaveBillNumber(String waveBillNumber);

  void approve(String waveBillNumber);
  
  List<TaskView> queryByWaveBillNumber(String waveBillNumber);
}
