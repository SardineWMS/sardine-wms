/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	SupplierReturnBillImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月21日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.service.rtn.supplierreturn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBill;
import com.hd123.sardine.wms.api.rtn.returnsupplier.ReturnSupplierBill;
import com.hd123.sardine.wms.api.rtn.returnsupplier.ReturnSupplierBillService;
import com.hd123.sardine.wms.api.rtn.returnsupplier.ReturnSupplierBillState;
import com.hd123.sardine.wms.api.tms.shipbill.ShipBillState;
import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.dao.rtn.returnsupplier.SupplierReturnBillDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.log.EntityLogger;

/**
 * @author fanqingqing
 *
 */
public class SupplierReturnBillServiceImpl extends BaseWMSService implements ReturnSupplierBillService {
    @Autowired
    private SupplierReturnBillDao dao;
    @Autowired
    private EntityLogger logger;

    @Override
    public ReturnSupplierBill getByUuid(String uuid) {
        if (StringUtil.isNullOrBlank(uuid))
            return null;
        ReturnSupplierBill bill = dao.get(uuid);
        if (bill == null)
            return null;
        bill.setItems(dao.queryItems(uuid));
        return bill;
    }

    @Override
    public ReturnSupplierBill getByBillNumber(String billNumber) {
        if (StringUtil.isNullOrBlank(billNumber))
            return null;
        ReturnSupplierBill bill = dao.getByBillNumber(billNumber);
        if (bill == null)
            return null;
        bill.setItems(dao.queryItems(bill.getUuid()));
        return bill;
    }

    @Override
    public PageQueryResult<ReturnSupplierBill> query(PageQueryDefinition definition)
            throws IllegalArgumentException {
        Assert.assertArgumentNotNull(definition, "definition");
        definition.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
        List<ReturnSupplierBill> list = dao.query(definition);
        PageQueryResult<ReturnSupplierBill> pqr = new PageQueryResult<>();
        PageQueryUtil.assignPageInfo(pqr, definition);
        pqr.setRecords(list);
        return pqr;
    }

    @Override
    public void finish(String uuid, long version)
            throws IllegalArgumentException, VersionConflictException, WMSException {
        Assert.assertArgumentNotNull(uuid, "uuid");
        Assert.assertArgumentNotNull(version, "version");

        ReturnSupplierBill bill = dao.get(uuid);
        if (bill == null)
            throw new WMSException("不存在");

        PersistenceUtils.checkVersion(version, bill, "供应商退货单", bill.getBillNumber());
        if (ShipBillState.Finished.equals(bill.getState()))
            throw new WMSException("供应商退货单" + bill.getBillNumber() + "状态是"
                    + bill.getState().getCaption() + "，不能完成！");

        bill.setState(ReturnSupplierBillState.Finished);
        bill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
        dao.update(bill);

        logger.injectContext(this, uuid, AcceptanceBill.class.getName(),
                ApplicationContextUtil.getOperateContext());
        logger.log("完成", "完成供应商退货单");
    }

}
