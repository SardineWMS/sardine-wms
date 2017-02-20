/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	SupplierDao.java
 * 模块说明：	
 * 修改历史：
 * 2017年1月9日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.supplier;

import com.hd123.sardine.wms.api.basicInfo.supplier.Supplier;
import com.hd123.sardine.wms.common.dao.BaseDao;

/**
 * @author fanqingqing
 *
 */
public interface SupplierDao extends BaseDao<Supplier>{

    Supplier getByCode(String code,String companyUuid);
}
