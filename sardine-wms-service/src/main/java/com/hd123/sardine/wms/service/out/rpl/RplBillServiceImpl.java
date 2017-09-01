/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	RplBillServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月23日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.out.rpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.out.rpl.RplBill;
import com.hd123.sardine.wms.api.out.rpl.RplBillItem;
import com.hd123.sardine.wms.api.out.rpl.RplBillService;
import com.hd123.sardine.wms.api.out.rpl.RplBillState;
import com.hd123.sardine.wms.api.task.TaskView;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.dao.out.rpl.RplBillDao;
import com.hd123.sardine.wms.dao.out.rpl.RplBillItemDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;

/**
 * @author zhangsai
 *
 */
public class RplBillServiceImpl extends BaseWMSService implements RplBillService {

    @Autowired
    private RplBillDao rplBillDao;

    @Autowired
    private RplBillItemDao rplBillItemDao;

    @Autowired
    private RplBillHandler rplBillHandler;

    @Override
    public void saveNew(RplBill rplBill) throws WMSException {
        Assert.assertArgumentNotNull(rplBill, "rplBill");

        rplBill.validate();

        rplBill.setUuid(UUIDGenerator.genUUID());
        rplBill.setBillNumber(
                billNumberGenerator.allocateNextBillNumber(RplBill.class.getSimpleName()));
        rplBill.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
        rplBill.setCreateInfo(ApplicationContextUtil.getOperateInfo());
        rplBill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());

        for (int i = 0; i < rplBill.getItems().size(); i++) {
            RplBillItem item = rplBill.getItems().get(i);
            item.setUuid(UUIDGenerator.genUUID());
            item.setLine(i + 1);
            item.setRplUuid(rplBill.getUuid());
        }

        rplBillDao.saveNew(rplBill);
        rplBillItemDao.saveNew(rplBill.getItems());
    }

    @Override
    public RplBill get(String uuid) {
        if (StringUtil.isNullOrBlank(uuid))
            return null;

        RplBill rplBill = rplBillDao.get(uuid);
        if (rplBill == null)
            return null;

        rplBill.setItems(rplBillItemDao.queryByRplUuid(rplBill.getUuid()));
        return rplBill;
    }

    @Override
    public RplBill getByBillNumber(String billNumber) {
        if (StringUtil.isNullOrBlank(billNumber))
            return null;

        RplBill rplBill = rplBillDao.getByBillNumber(billNumber);
        if (rplBill == null)
            return null;

        rplBill.setItems(rplBillItemDao.queryByRplUuid(rplBill.getUuid()));
        return rplBill;
    }

    @Override
    public void approveByWaveBillNumber(String waveBillNumber) {
        if (StringUtil.isNullOrBlank(waveBillNumber))
            return;

        rplBillDao.approve(waveBillNumber);
    }

    @Override
    public List<TaskView> queryByWaveBillNumber(String waveBillNumber) {
        if (StringUtil.isNullOrBlank(waveBillNumber))
            return new ArrayList<TaskView>();
        return rplBillDao.queryByWaveBillNumber(waveBillNumber);
    }

    @Override
    public List<RplBillItem> queryRplItems(String waveBillNumber) {
        if (StringUtil.isNullOrBlank(waveBillNumber))
            return new ArrayList<RplBillItem>();
        return rplBillItemDao.queryRplItems(waveBillNumber);
    }

    @Override
    public void removeByWaveBillNumber(String waveBillNumber) {
        if (StringUtil.isNullOrBlank(waveBillNumber))
            return;

        rplBillItemDao.removeByWaveBillNumber(waveBillNumber);
        rplBillDao.removeByWaveBillNumber(waveBillNumber);
    }

    @Override
    public void rpl(String rplBillUuid, long version, UCN rpler) throws WMSException {
        Assert.assertArgumentNotNull(rplBillUuid, "rplBillUuid");
        Assert.assertArgumentNotNull(version, "version");

        RplBill bill = rplBillDao.get(rplBillUuid);
        if (bill == null)
            throw new WMSException("补货指令不存在");
        PersistenceUtils.checkVersion(version, bill, RplBill.CAPTION, rplBillUuid);
        if (RplBillState.approved.equals(bill.getState()) == false)
            throw new WMSException("补货指令对应的补货单状态不是" + RplBillState.approved.getCaption() + "。");

        List<RplBillItem> items = rplBillItemDao.queryByRplUuid(bill.getUuid());
        for (RplBillItem item : items) {
            item.setRealQty(item.getQty());
            item.setRealCaseQtyStr(item.getCaseQtyStr());
            rplBillItemDao.saveModify(item);
        }
        bill.setItems(items);
        bill.setState(RplBillState.audited);
        bill.setRpler(rpler == null
                ? new UCN(ApplicationContextUtil.getOperateInfo().getOperator().getId(),
                        ApplicationContextUtil.getOperateInfo().getOperator().getCode(),
                        ApplicationContextUtil.getOperateInfo().getOperator().getFullName())
                : rpler);
        bill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
        rplBillDao.saveModify(bill);

        rplBillHandler.shiftStock(bill);
        rplBillHandler.manageBinAndContainer(bill);
    }
}
