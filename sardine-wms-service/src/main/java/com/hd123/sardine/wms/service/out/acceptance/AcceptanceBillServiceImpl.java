/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	AcceptanceBillServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月5日 - fanqingqing - 创建。
 */
package com.hd123.sardine.wms.service.out.acceptance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.bin.Bin;
import com.hd123.sardine.wms.api.basicInfo.bin.BinService;
import com.hd123.sardine.wms.api.basicInfo.bin.Wrh;
import com.hd123.sardine.wms.api.basicInfo.container.Container;
import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBill;
import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBillItem;
import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBillService;
import com.hd123.sardine.wms.api.out.acceptance.AcceptanceBillState;
import com.hd123.sardine.wms.api.stock.OnWayStock;
import com.hd123.sardine.wms.api.stock.OnWayStockOutRule;
import com.hd123.sardine.wms.api.stock.Stock;
import com.hd123.sardine.wms.api.stock.StockExtendInfo;
import com.hd123.sardine.wms.api.stock.StockFilter;
import com.hd123.sardine.wms.api.stock.StockService;
import com.hd123.sardine.wms.api.task.Task;
import com.hd123.sardine.wms.api.task.TaskService;
import com.hd123.sardine.wms.api.task.TaskType;
import com.hd123.sardine.wms.common.entity.OperateInfo;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.exception.VersionConflictException;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.query.PageQueryUtil;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.Constants;
import com.hd123.sardine.wms.common.utils.PersistenceUtils;
import com.hd123.sardine.wms.common.utils.QpcHelper;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.dao.out.acceptance.AcceptanceBillDao;
import com.hd123.sardine.wms.dao.stock.StockDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.log.EntityLogger;

/**
 * @author fanqingqing
 *
 */
public class AcceptanceBillServiceImpl extends BaseWMSService implements AcceptanceBillService {

    @Autowired
    private EntityLogger logger;
    @Autowired
    private AcceptanceBillDao billDao;
    @Autowired
    private BinService binService;
    @Autowired
    private StockService stockService;
    @Autowired
    private StockDao stockDao;
    @Autowired
    private TaskService taskService;

    @Override
    public AcceptanceBill get(String uuid) {
        if (StringUtil.isNullOrBlank(uuid))
            return null;
        AcceptanceBill bill = billDao.get(uuid);
        if (bill == null)
            return null;
        bill.setItems(billDao.queryItems(uuid));
        return bill;
    }

    @Override
    public AcceptanceBill getByBillNumber(String billNumber) {
        if (StringUtil.isNullOrBlank(billNumber))
            return null;
        AcceptanceBill bill = billDao.getByBillNumber(billNumber);
        if (bill == null)
            return null;
        bill.setItems(billDao.queryItems(bill.getUuid()));
        return bill;
    }

    @Override
    public PageQueryResult<AcceptanceBill> query(PageQueryDefinition definition) {
        Assert.assertArgumentNotNull(definition, "definition");

        PageQueryResult<AcceptanceBill> pgr = new PageQueryResult<AcceptanceBill>();
        List<AcceptanceBill> list = billDao.query(definition);
        PageQueryUtil.assignPageInfo(pgr, definition);
        pgr.setRecords(list);
        return pgr;
    }

    @Override
    public String insert(AcceptanceBill acceptanceBill)
            throws IllegalArgumentException, WMSException {
        AcceptanceBill bill = billDao.getByBillNumber(acceptanceBill.getBillNumber());
        if (bill != null)
            return bill.getUuid();
        verifyAcceptanceBill(acceptanceBill);

        acceptanceBill.setUuid(UUIDGenerator.genUUID());
        acceptanceBill
                .setBillNumber(billNumberGenerator.allocate(Constants.ACCEPTANCE_NUMBER_TYPE));
        acceptanceBill
                .setCreateInfo(OperateInfo.newInstance(ApplicationContextUtil.getOperateContext()));
        acceptanceBill.setLastModifyInfo(
                OperateInfo.newInstance(ApplicationContextUtil.getOperateContext()));

        for (AcceptanceBillItem item : acceptanceBill.getItems()) {
            item.setUuid(UUIDGenerator.genUUID());
            item.setAcceptanceBillUuid(acceptanceBill.getUuid());
        }
        billDao.insert(acceptanceBill);
        billDao.insertItems(acceptanceBill.getItems());

        logger.injectContext(this, acceptanceBill.getUuid(), AcceptanceBill.class.getName(),
                ApplicationContextUtil.getOperateContext());
        logger.log(EntityLogger.EVENT_ADDNEW, "新建领用单");
        return acceptanceBill.getUuid();
    }

    @Override
    public void update(AcceptanceBill acceptanceBill)
            throws IllegalArgumentException, VersionConflictException, WMSException {
        Assert.assertArgumentNotNull(acceptanceBill, "acceptanceBill");
        verifyAcceptanceBill(acceptanceBill);

        if (AcceptanceBillState.Initial.equals(acceptanceBill.getState()) == false)
            throw new WMSException("领用单" + acceptanceBill.getBillNumber() + "状态不是初始，不能编辑！");
        acceptanceBill.setLastModifyInfo(
                OperateInfo.newInstance(ApplicationContextUtil.getOperateContext()));
        for (AcceptanceBillItem item : acceptanceBill.getItems()) {
            item.setUuid(UUIDGenerator.genUUID());
            item.setAcceptanceBillUuid(acceptanceBill.getUuid());
        }
        billDao.update(acceptanceBill);
        billDao.removeItems(acceptanceBill.getUuid());
        billDao.insertItems(acceptanceBill.getItems());

        logger.injectContext(this, acceptanceBill.getUuid(), AcceptanceBill.class.getName(),
                ApplicationContextUtil.getOperateContext());
        logger.log(EntityLogger.EVENT_MODIFY, "修改领用单");
    }

    private void verifyAcceptanceBill(AcceptanceBill acceptanceBill) throws WMSException {
        assert acceptanceBill != null;
        acceptanceBill.validate();

        Wrh wrh = binService.getWrh(acceptanceBill.getWrh().getUuid());
        if (wrh == null)
            throw new WMSException("仓位" + acceptanceBill.getWrh().getCode() + "不存在");
        StringBuffer errorMessage = new StringBuffer();
        for (AcceptanceBillItem item : acceptanceBill.getItems()) {
            List<StockExtendInfo> stockExtendInfos = getStockExtendInfo(item);
            if (CollectionUtils.isEmpty(stockExtendInfos))
                errorMessage.append("第" + item.getLine() + "行明细，没有对应的库存");
            BigDecimal stockQty = BigDecimal.ZERO;
            for (StockExtendInfo stockExtendInfo : stockExtendInfos)
                stockQty = stockQty.add(stockExtendInfo.getQty());
            if (item.getQty().compareTo(stockQty) > 0)
                errorMessage.append("第" + item.getLine() + "行明细，领用数量不能大于库存数量" + stockQty);
        }

    }

    private List<StockExtendInfo> getStockExtendInfo(AcceptanceBillItem item) {
        StockFilter filter = new StockFilter();
        filter.setPageSize(50);
        filter.setPage(1);
        filter.setBinCode(item.getBinCode());
        filter.setContainerBarcode(item.getContainerBarCode() == null
                ? Container.VIRTUALITY_CONTAINER : item.getContainerBarCode());
        filter.setArticleUuid(item.getArticle().getUuid());
        filter.setSupplierUuid(item.getSupplier().getUuid());
        filter.setQpcStr(item.getQpcStr());
        filter.setStockBatch(item.getStockBatch());
        List<StockExtendInfo> stockExtendInfos = stockService.queryStocks(filter);
        return stockExtendInfos;
    }

    @Override
    public void remove(String uuid, long version)
            throws IllegalArgumentException, VersionConflictException, WMSException {
        Assert.assertArgumentNotNull(uuid, "uuid");
        Assert.assertArgumentNotNull(version, "version");

        AcceptanceBill acceptanceBill = billDao.get(uuid);
        if (acceptanceBill == null)
            throw new WMSException("领用单不存在");
        PersistenceUtils.checkVersion(version, acceptanceBill, "领用单",
                acceptanceBill.getBillNumber());

        if (acceptanceBill.getState().equals(AcceptanceBillState.Initial) == false)
            throw new WMSException("领用单" + acceptanceBill.getBillNumber() + "状态不是初始，不能删除！");

        billDao.remove(uuid, version);
        billDao.removeItems(uuid);

        logger.injectContext(this, uuid, AcceptanceBill.class.getName(),
                ApplicationContextUtil.getOperateContext());
        logger.log(EntityLogger.EVENT_REMOVE, "删除领用单");
    }

    @Override
    public void approve(String uuid, long version)
            throws IllegalArgumentException, VersionConflictException, WMSException {
        Assert.assertArgumentNotNull(uuid, "uuid");
        Assert.assertArgumentNotNull(version, "version");

        AcceptanceBill acceptanceBill = billDao.get(uuid);
        if (acceptanceBill == null)
            throw new WMSException("领用单不存在");
        acceptanceBill.setItems(billDao.queryItems(uuid));
        verifyAcceptanceBill(acceptanceBill);
        PersistenceUtils.checkVersion(version, acceptanceBill, "领用单",
                acceptanceBill.getBillNumber());
        if (AcceptanceBillState.Initial.equals(acceptanceBill.getState()) == false)
            throw new WMSException("领用单" + acceptanceBill.getBillNumber() + "状态不是初始，不能批准！");

        List<OnWayStock> onWayStocks = createOnWayStocks(acceptanceBill.getItems(),
                acceptanceBill.getBillNumber());
        stockService.shiftInOnWayStock(onWayStocks);

        acceptanceBill.setState(AcceptanceBillState.Approved);
        billDao.update(acceptanceBill);

        logger.injectContext(this, uuid, AcceptanceBill.class.getName(),
                ApplicationContextUtil.getOperateContext());
        logger.log(EntityLogger.EVENT_APPROVE, "批准领用单");
    }

    private List<OnWayStock> createOnWayStocks(List<AcceptanceBillItem> items, String billNumber) {
        List<OnWayStock> onWayStocks = new ArrayList<>();
        for (AcceptanceBillItem item : items) {
            OnWayStock onWayStock = new OnWayStock();
            onWayStock.setArticleUuid(item.getArticle().getUuid());
            onWayStock.setBinCode(item.getBinCode());
            onWayStock.setContainerBarcode(item.getContainerBarCode() == null
                    ? Container.VIRTUALITY_CONTAINER : item.getContainerBarCode());
            onWayStock.setStockBatch(item.getStockBatch());
            onWayStock.setQty(item.getQty().multiply(BigDecimal.valueOf(-1)));
            onWayStock.setTaskNo(billNumber);
            onWayStock.setTaskType(TaskType.Pickup);
            onWayStocks.add(onWayStock);
        }
        return onWayStocks;
    }

    @Override
    public void beginAlc(String uuid, long version)
            throws IllegalArgumentException, VersionConflictException, WMSException {
        Assert.assertArgumentNotNull(uuid, "uuid");
        Assert.assertArgumentNotNull(version, "version");

        AcceptanceBill acceptanceBill = billDao.get(uuid);
        if (acceptanceBill == null)
            throw new WMSException("领用单不存在");

        acceptanceBill.setItems(billDao.queryItems(uuid));
        PersistenceUtils.checkVersion(version, acceptanceBill, "领用单",
                acceptanceBill.getBillNumber());
        if (AcceptanceBillState.Approved.equals(acceptanceBill.getState()) == false)
            throw new WMSException("领用单" + acceptanceBill.getBillNumber() + "状态不是已批准，不能配货！");

        List<Task> tasks = generatePickUpTasks(acceptanceBill);
        if (CollectionUtils.isEmpty(tasks) == false)
            // taskService.insert(tasks);

            acceptanceBill.setState(AcceptanceBillState.InAlc);
        billDao.update(acceptanceBill);

        logger.injectContext(this, uuid, AcceptanceBill.class.getName(),
                ApplicationContextUtil.getOperateContext());
        logger.log("配货", "领用单配货");
    }

    private List<Task> generatePickUpTasks(AcceptanceBill acceptanceBill) {
        assert acceptanceBill != null;
        List<Task> pickTasks = new ArrayList<>();
        for (AcceptanceBillItem item : acceptanceBill.getItems()) {
            StockFilter filter = new StockFilter();
            filter.setPageSize(50);
            filter.setPage(1);
            filter.setBinCode(item.getBinCode());
            filter.setContainerBarcode(item.getContainerBarCode() == null
                    ? Container.VIRTUALITY_CONTAINER : item.getContainerBarCode());
            filter.setArticleUuid(item.getArticle().getUuid());
            filter.setQpcStr(item.getQpcStr());
            filter.setStockBatch(item.getStockBatch());
            List<Stock> stocks = stockDao.query(filter);

            for (Stock stock : stocks) {
                BigDecimal planPickQty = BigDecimal.ZERO;
                OnWayStock onWayStock = stockDao.getByTaskNo(stock.getUuid(),
                        acceptanceBill.getBillNumber());
                planPickQty = planPickQty.add(onWayStock.getQty());

                Task task = new Task();
                task.setArticle(item.getArticle());
                task.setFromBinCode(stock.getBinCode());
                task.setFromContainerBarcode(stock.getContainerBarcode());
                task.setToBinCode(Bin.VIRTUALITY_BIN);
                task.setToContainerBarcode(Container.VIRTUALITY_CONTAINER);
                task.setProductionDate(stock.getProductionDate());
                task.setQpcStr(stock.getQpcStr());
                task.setQty(planPickQty);
                task.setCaseQtyStr(QpcHelper.qtyToCaseQtyStr(task.getQty(), task.getQpcStr()));
                task.setSourceBillLine(item.getLine());
                task.setSourceBillNumber(acceptanceBill.getBillNumber());
                task.setSourceBillUuid(acceptanceBill.getUuid());
                task.setSourceBillType(Constants.ACCEPTANCE_NUMBER_TYPE);
                task.setStockBatch(stock.getStockBatch());
                task.setSupplier(item.getSupplier());
                task.setTaskType(TaskType.Pickup);
                task.setValidDate(stock.getValidDate());
                task.setOwner("-");
                UCN creator = new UCN(ApplicationContextUtil.getOperateInfo().getOperator().getId(),
                        ApplicationContextUtil.getOperateInfo().getOperator().getCode(),
                        ApplicationContextUtil.getOperateInfo().getOperator().getFullName());
                task.setCreator(creator);
                task.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
                pickTasks.add(task);
            }
        }
        return pickTasks;
    }

    @Override
    public void finish(String uuid, long version)
            throws IllegalArgumentException, VersionConflictException, WMSException {
        Assert.assertArgumentNotNull(uuid, "uuid");
        Assert.assertArgumentNotNull(version, "version");

        AcceptanceBill acceptanceBill = billDao.get(uuid);
        if (acceptanceBill == null)
            throw new WMSException("领用单不存在");

        acceptanceBill.setItems(billDao.queryItems(uuid));
        PersistenceUtils.checkVersion(version, acceptanceBill, "领用单",
                acceptanceBill.getBillNumber());
        if (AcceptanceBillState.Aborted.equals(acceptanceBill.getState())
                || AcceptanceBillState.Finished.equals(acceptanceBill.getState()))
            throw new WMSException("领用单" + acceptanceBill.getBillNumber() + "状态是"
                    + acceptanceBill.getState().getCaption() + "，不能完成！");

        if (AcceptanceBillState.Approved.equals(acceptanceBill.getState()))
            stockService.shiftOutOnWayStock(createOnWayStockOutRules(acceptanceBill.getItems(),
                    acceptanceBill.getBillNumber()));

        acceptanceBill.setState(AcceptanceBillState.Finished);
        billDao.update(acceptanceBill);

        logger.injectContext(this, uuid, AcceptanceBill.class.getName(),
                ApplicationContextUtil.getOperateContext());
        logger.log("完成", "完成领用单");
    }

    @Override
    public void abort(String uuid, long version)
            throws IllegalArgumentException, VersionConflictException, WMSException {
        Assert.assertArgumentNotNull(uuid, "uuid");
        Assert.assertArgumentNotNull(version, "version");

        AcceptanceBill acceptanceBill = billDao.get(uuid);
        if (acceptanceBill == null)
            throw new WMSException("领用单不存在");

        acceptanceBill.setItems(billDao.queryItems(uuid));
        PersistenceUtils.checkVersion(version, acceptanceBill, "领用单",
                acceptanceBill.getBillNumber());
        if (acceptanceBill.getState().equals(AcceptanceBillState.Initial) == false
                && acceptanceBill.getState().equals(AcceptanceBillState.Approved) == false)
            throw new WMSException("领用单" + acceptanceBill.getBillNumber() + "状态不是初始或已批准，不能作废！");

        if (AcceptanceBillState.Approved.equals(acceptanceBill.getState()))
            stockService.shiftOutOnWayStock(createOnWayStockOutRules(acceptanceBill.getItems(),
                    acceptanceBill.getBillNumber()));

        acceptanceBill.setState(AcceptanceBillState.Aborted);
        billDao.update(acceptanceBill);

        logger.injectContext(this, uuid, AcceptanceBill.class.getName(),
                ApplicationContextUtil.getOperateContext());
        logger.log(EntityLogger.EVENT_ABORT, "作废领用单");
    }

    private List<OnWayStockOutRule> createOnWayStockOutRules(List<AcceptanceBillItem> items,
            String billNumber) {
        List<OnWayStockOutRule> onWayStocks = new ArrayList<>();
        for (AcceptanceBillItem item : items) {
            OnWayStockOutRule onWayStock = new OnWayStockOutRule();
            onWayStock.setArticleUuid(item.getArticle().getUuid());
            onWayStock.setBinCode(item.getBinCode());
            onWayStock.setContainerBarcode(item.getContainerBarCode() == null
                    ? Container.VIRTUALITY_CONTAINER : item.getContainerBarCode());
            onWayStock.setStockBatch(item.getStockBatch());
            onWayStock.setQty(item.getQty());
            onWayStock.setTaskNo(billNumber);
            onWayStock.setTaskType(TaskType.Pickup);
            onWayStocks.add(onWayStock);
        }
        return onWayStocks;
    }

}
