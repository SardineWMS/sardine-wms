/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	ShipBillServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月16日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.service.tms.shipbill;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBill;
import com.hd123.sardine.wms.api.tms.shipbill.ShipBill;
import com.hd123.sardine.wms.api.tms.shipbill.ShipBillService;
import com.hd123.sardine.wms.api.tms.shipbill.ShipBillState;
import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.dao.tms.shipbill.ShipBillDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.log.EntityLogger;

/**
 * @author fanqingqing
 *
 */

public class ShipBillServiceImpl extends BaseWMSService implements ShipBillService {
    @Autowired
    private ShipBillDao dao;
    @Autowired
    private EntityLogger logger;

    @Override
    public ShipBill getByUuid(String uuid) {
        if (StringUtil.isNullOrBlank(uuid))
            return null;
        ShipBill bill = dao.get(uuid);
        if (bill == null)
            return null;
        bill.setCustomerItems(dao.queryCustomerItems(uuid));
        bill.setContainerStocks(dao.queryContainerStockItems(uuid));
        return bill;
    }

    @Override
    public ShipBill getByBillNumber(String billNumber) {
        if (StringUtil.isNullOrBlank(billNumber))
            return null;
        ShipBill bill = dao.getByBillNumber(billNumber);
        if (bill == null)
            return null;
        bill.setCustomerItems(dao.queryCustomerItems(bill.getUuid()));
        bill.setContainerStocks(dao.queryContainerStockItems(bill.getUuid()));
        return bill;
    }

    @Override
    public PageQueryResult<ShipBill> query(PageQueryDefinition definition)
            throws IllegalArgumentException {
        Assert.assertArgumentNotNull(definition, "definition");

        List<ShipBill> list = dao.query(definition);
        PageQueryResult<ShipBill> pqr = new PageQueryResult<>();
        PageQueryUtil.assignPageInfo(pqr, definition);
        pqr.setRecords(list);
        return pqr;
    }

    @Override
    public void finish(String uuid, long version)
            throws IllegalArgumentException, VersionConflictException, WMSException {
        Assert.assertArgumentNotNull(uuid, "uuid");
        Assert.assertArgumentNotNull(version, "version");

        ShipBill shipBill = dao.get(uuid);
        if (shipBill == null)
            throw new WMSException("不存在");

        PersistenceUtils.checkVersion(version, shipBill, "装车单", shipBill.getBillNumber());
        if (ShipBillState.Finished.equals(shipBill.getState()))
            throw new WMSException("装车单" + shipBill.getBillNumber() + "状态是"
                    + shipBill.getState().getCaption() + "，不能完成！");

        shipBill.setState(ShipBillState.Finished);
        shipBill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
        dao.update(shipBill);

        logger.injectContext(this, uuid, AcceptanceBill.class.getName(),
                ApplicationContextUtil.getOperateContext());
        logger.log("完成", "完成装车单");
    }

}
