/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	SerialArchDao.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月14日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.dao.tms.serialarch;

import java.util.List;

import com.hd123.sardine.wms.api.tms.serialarch.SerialArch;
import com.hd123.sardine.wms.api.tms.serialarch.SerialArchLine;
import com.hd123.sardine.wms.api.tms.serialarch.SerialArchLineCustomer;
import com.hd123.sardine.wms.common.dao.BaseDao;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;

/**
 * @author yangwenzhu
 *
 */
public interface SerialArchDao extends BaseDao<SerialArch> {
  SerialArch getByCode(String code);

  int insertSerialArchLine(SerialArchLine line);

  SerialArchLine getLineByCode(String lineCode);

  SerialArchLine getLine(String uuid);

  List<SerialArchLine> getLineByCustomerUuid(String customerUuid);

  int insertLineCustomer(SerialArchLineCustomer c);

  List<SerialArchLine> getLineByArchUuid(String archUuid);

  List<SerialArchLineCustomer> getCustomerByLine(String lineUuid);

  SerialArchLineCustomer getCustomer(String lineUuid, String customerUuid);

  int removeCustomer(String lineUuid, String customerUuid);

  void updateLineCustomer(SerialArchLineCustomer lineCustomer);

  SerialArchLineCustomer getCustomerByLineAndOrder(String lineUuid, int order);

  List<SerialArchLineCustomer> queryCustomerOrderLess(String lineUuid, int order);

  List<SerialArchLineCustomer> queryCustomerOrderMore(String lineUuid, int order);

  List<SerialArchLineCustomer> queryCustomerByLine(PageQueryDefinition definition);

  List<SerialArchLine> getLineByCompanyUuid();

  int removeLine(String uuid, long version);
}
