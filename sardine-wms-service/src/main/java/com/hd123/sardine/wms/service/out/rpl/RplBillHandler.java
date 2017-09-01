/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	RplBillHandler.java
 * 模块说明：	
 * 修改历史：
 * 2017年8月30日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.service.out.rpl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.bin.Bin;
import com.hd123.sardine.wms.api.basicInfo.bin.BinService;
import com.hd123.sardine.wms.api.basicInfo.bin.BinState;
import com.hd123.sardine.wms.api.basicInfo.container.Container;
import com.hd123.sardine.wms.api.basicInfo.container.ContainerService;
import com.hd123.sardine.wms.api.out.rpl.RplBill;
import com.hd123.sardine.wms.api.out.rpl.RplBillItem;
import com.hd123.sardine.wms.api.stock.StockService;
import com.hd123.sardine.wms.api.stock.StockShiftRule;
import com.hd123.sardine.wms.api.stock.StockShiftTarget;
import com.hd123.sardine.wms.api.stock.StockState;
import com.hd123.sardine.wms.common.entity.SourceBill;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.service.util.StockBatchUtils;

/**
 * @author fanqingqing
 *
 */
public class RplBillHandler {

    @Autowired
    private StockService stockService;

    @Autowired
    private StockBatchUtils stockBatchUtils;

    @Autowired
    private BinService binService;

    @Autowired
    private ContainerService containerService;

    public void releaseStock(RplBill bill) throws WMSException {
        Assert.assertArgumentNotNull(bill, "bill");

        if (CollectionUtils.isEmpty(bill.getItems()))
            throw new WMSException("补货单明细不能为空");
        SourceBill sourceBill = new SourceBill(RplBill.CAPTION, bill.getUuid(),
                bill.getBillNumber());
        for (RplBillItem item : bill.getItems()) {
            StockShiftRule shiftRule = new StockShiftRule();
            shiftRule.setArticleUuid(item.getArticle().getUuid());
            shiftRule.setBinCode(item.getFromBinCode());
            shiftRule.setOwner(ApplicationContextUtil.getCompanyUuid());
            shiftRule.setContainerBarcode(item.getContainerBarcode());
            shiftRule.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
            shiftRule.setQty(item.getQty());
            shiftRule.setState(StockState.forMoveOut);
            stockService.changeState(sourceBill, Arrays.asList(shiftRule), StockState.forMoveOut,
                    StockState.normal);

            shiftRule.setBinCode(item.getToBinCode());
            shiftRule.setOwner(ApplicationContextUtil.getCompanyUuid());
            shiftRule.setContainerBarcode(item.getContainerBarcode());
            shiftRule.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
            shiftRule.setQty(item.getQty());
            shiftRule.setState(StockState.forMoveIn);
            stockService.shiftOut(sourceBill, Arrays.asList(shiftRule));
        }
    }

    public void shiftStock(RplBill bill) throws WMSException {
        Assert.assertArgumentNotNull(bill, "bill");

        releaseStock(bill);
        SourceBill sourceBill = new SourceBill(RplBill.CAPTION, bill.getUuid(),
                bill.getBillNumber());
        for (RplBillItem item : bill.getItems()) {
            if (item.getRealQty().compareTo(BigDecimal.ZERO) <= 0)
                return;
            StockShiftRule shiftRule = new StockShiftRule();
            shiftRule.setArticleUuid(item.getArticle().getUuid());
            shiftRule.setBinCode(item.getFromBinCode());
            shiftRule.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
            shiftRule.setContainerBarcode(item.getContainerBarcode());
            shiftRule.setOwner(item.getOwner());
            shiftRule.setProductionBatch(
                    stockBatchUtils.genProductionBatch(item.getProductionDate()));
            shiftRule.setQpcStr(item.getQpcStr());
            shiftRule.setQty(item.getRealQty());
            shiftRule.setStockBatch(item.getStockBatch());
            shiftRule.setSupplierUuid(item.getSupplier().getUuid());
            StockShiftTarget shiftTarget = new StockShiftTarget();
            shiftTarget.setBinCode(item.getToBinCode());
            shiftTarget.setContainerBarCode(item.getContainerBarcode());
            shiftTarget.setState(StockState.normal);
            stockService.shift(sourceBill, Arrays.asList(shiftRule), shiftTarget);
        }
    }

    public void manageBinAndContainer(RplBill bill) throws WMSException {
        Assert.assertArgumentNotNull(bill, "bill");

        Set<String> fromBinCodes = new HashSet<>();
        Set<String> toBinCodes = new HashSet<>();
        Set<String> containerBarcodes = new HashSet<>();
        for (RplBillItem item : bill.getItems()) {
            fromBinCodes.add(item.getFromBinCode());
            toBinCodes.add(item.getToBinCode());
            if (StringUtil.isNullOrBlank(item.getContainerBarcode()) == false
                    && Container.VIRTUALITY_CONTAINER.equals(item.getContainerBarcode()) == false)
                containerBarcodes.add(item.getContainerBarcode());
        }

        for (String fromBinCode : fromBinCodes) {
            Bin fromBin = binService.getBinByCode(fromBinCode);
            if (stockService.hasBinStock(fromBinCode) == false)
                binService.changeState(fromBin.getUuid(), fromBin.getVersion(), BinState.free);
        }
        for (String toBinCode : toBinCodes) {
            Bin toBin = binService.getBinByCode(toBinCode);
            if (toBin.getState().equals(BinState.free))
                binService.changeState(toBin.getUuid(), toBin.getVersion(), BinState.using);
        }
        for (String containerBarcode : containerBarcodes) {
            Container container = containerService.getByBarcode(containerBarcode);
            if (container == null)
                throw new WMSException("指令容器" + containerBarcode + "不存在！");
            if (stockService.hasContainerStock(containerBarcode) == false)
                containerService.recycle(container.getUuid(), container.getVersion());
        }

    }

}
