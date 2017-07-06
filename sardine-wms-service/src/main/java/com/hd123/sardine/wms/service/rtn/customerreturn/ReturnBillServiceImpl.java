/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	ReturnBillServiceImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年7月6日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.service.rtn.customerreturn;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.basicInfo.article.Article;
import com.hd123.sardine.wms.api.basicInfo.article.ArticleService;
import com.hd123.sardine.wms.api.basicInfo.bin.Bin;
import com.hd123.sardine.wms.api.basicInfo.bin.BinService;
import com.hd123.sardine.wms.api.basicInfo.bin.BinUsage;
import com.hd123.sardine.wms.api.basicInfo.container.Container;
import com.hd123.sardine.wms.api.basicInfo.container.ContainerService;
import com.hd123.sardine.wms.api.basicInfo.container.ContainerState;
import com.hd123.sardine.wms.api.rtn.customerreturn.ReturnBill;
import com.hd123.sardine.wms.api.rtn.customerreturn.ReturnBillItem;
import com.hd123.sardine.wms.api.rtn.customerreturn.ReturnBillService;
import com.hd123.sardine.wms.api.rtn.customerreturn.ReturnBillState;
import com.hd123.sardine.wms.api.rtn.customerreturn.ReturnType;
import com.hd123.sardine.wms.api.rtn.ntc.ReturnNtcBill;
import com.hd123.sardine.wms.api.rtn.ntc.ReturnNtcBillItem;
import com.hd123.sardine.wms.api.rtn.ntc.ReturnNtcBillService;
import com.hd123.sardine.wms.api.rtn.ntc.ReturnNtcBillState;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.Constants;
import com.hd123.sardine.wms.common.utils.UUIDGenerator;
import com.hd123.sardine.wms.dao.rtn.customerreturn.ReturnBillDao;
import com.hd123.sardine.wms.service.ia.BaseWMSService;
import com.hd123.sardine.wms.service.log.EntityLogger;

/**
 * @author yangwenzhu
 *
 */
public class ReturnBillServiceImpl extends BaseWMSService implements ReturnBillService {

    @Autowired
    private ReturnBillDao dao;
    @Autowired
    private ReturnNtcBillService rtnNtcBillService;
    @Autowired
    private BinService binService;
    @Autowired
    private ContainerService containerService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private EntityLogger logger;

    @Override
    public String saveNew(ReturnBill bill) throws WMSException, ParseException {
        Assert.assertArgumentNotNull(bill, "bill");

        verifyAndRefreshBill(bill);

        bill.setBillNumber(billNumberGenerator.allocate(Constants.RTN_NUMBER_TYPE));
        bill.setCreateInfo(ApplicationContextUtil.getOperateInfo());
        bill.setLastModifyInfo(ApplicationContextUtil.getOperateInfo());
        bill.setState(ReturnBillState.initial);
        bill.setUuid(UUIDGenerator.genUUID());
        dao.insert(bill);

        for (ReturnBillItem item : bill.getItems()) {
            item.setUuid(UUIDGenerator.genUUID());
            item.setReturnBillUuid(bill.getUuid());
        }
        dao.insertItems(bill.getItems());
        logger.injectContext(this, bill.getUuid(), ReturnBill.class.getName(),
                ApplicationContextUtil.getOperateContext());
        logger.log(EntityLogger.EVENT_ADDNEW, "新增退仓单");
        return bill.getUuid();
    }

    private void verifyAndRefreshBill(ReturnBill bill) throws WMSException, ParseException {
        assert bill != null;
        assert bill.getReturnNtcBillNumber() != null;
        bill.validate();

        ReturnNtcBill returnNtcBill = rtnNtcBillService
                .getByBillNumber(bill.getReturnNtcBillNumber());
        if (Objects.isNull(returnNtcBill))
            throw new WMSException(
                    MessageFormat.format("退仓通知单“{0}”不存在。", bill.getReturnNtcBillNumber()));
        if ((ReturnNtcBillState.initial.equals(returnNtcBill.getState())
                || ReturnNtcBillState.inProgress.equals(returnNtcBill.getState())) == false)
            throw new WMSException(MessageFormat.format("退仓通知单状态是{0}，不是初始或进行中的，不能操作",
                    returnNtcBill.getState().getCaption()));

        StringBuffer errorMsg = new StringBuffer();
        errorMsg.append(verifyBinAndRefreshItems(bill, returnNtcBill));
        verifyAndRefreshArticles(errorMsg, bill.getItems());
        verifyContainers(errorMsg, bill);
        if (errorMsg.length() > 0)
            throw new WMSException(errorMsg.toString());
    }

    private void verifyContainers(StringBuffer errorMsg, ReturnBill bill) {
        assert errorMsg != null;
        assert bill != null;
        assert bill.getItems() != null;

        List<ReturnBillItem> items = bill.getItems();
        if (items.isEmpty())
            return;

        for (int i = 0; i < items.size(); i++) {
            ReturnBillItem item = items.get(i);
            if (Container.VIRTUALITY_CONTAINER.equals(item.getContainerBarcode()))
                continue;
            Container container = containerService.getByBarcode(item.getContainerBarcode());
            if (Objects.isNull(container)) {
                errorMsg.append(
                        MessageFormat.format("第{0}行中，容器{1}不存在", i, item.getContainerBarcode()));
                continue;
            }
            if (ContainerState.STACONTAINERIDLE.equals(container.getState()) == false) {
                errorMsg.append(MessageFormat.format("第{0}行，容器状态是{1}，不是空闲状态", i,
                        container.getState().getCaption()));
                continue;
            }
            for (int j = i + 1; j < items.size(); j++) {
                ReturnBillItem jItem = items.get(j);
                if (Container.VIRTUALITY_CONTAINER.equals(jItem.getContainerBarcode()))
                    continue;
                Container jContainer = containerService.getByBarcode(jItem.getContainerBarcode());
                if (Objects.isNull(jContainer)) {
                    errorMsg.append(
                            MessageFormat.format("第{0}行中，容器{1}不存在", j, item.getContainerBarcode()));
                    continue;
                }
                if (ContainerState.STACONTAINERIDLE.equals(container.getState()) == false) {
                    errorMsg.append(MessageFormat.format("第{0}行，容器状态是{1}，不是空闲状态", i,
                            container.getState().getCaption()));
                    continue;
                }
                if (item.getReturnType().equals(jItem.getReturnType())
                        && ReturnType.returnToSupplier.equals(item.getReturnType())) {
                    if (item.getArticle().getUuid().equals(jItem.getArticle().getUuid())
                            && item.getSupplier().getUuid()
                                    .equals(jItem.getSupplier().getUuid()) == false) {
                        errorMsg.append(
                                MessageFormat.format("第{0}行和第{1}行，退货类型为退供应商时，不同供应商的商品不允许混载", i, j));
                        continue;
                    }
                }
                if (item.getReturnType().equals(jItem.getReturnType()) == false) {
                    errorMsg.append(MessageFormat.format("第{0}行和第{1}行，好退和退供应商的商品不允许混载", i, j));
                    continue;
                }
            }
        }

    }

    private void verifyAndRefreshArticles(StringBuffer errorMsg, List<ReturnBillItem> items)
            throws ParseException {
        assert errorMsg != null;
        assert items != null;

        for (ReturnBillItem item : items) {
            Article article = articleService.get(item.getArticle().getUuid());
            if (Objects.isNull(article))
                errorMsg.append(MessageFormat.format("第{0}行中，商品{1}不存在", item.getLine(),
                        item.getArticle().getCode()));
            item.setProductionDate(
                    article.calProductionDate(item.getProductionDate(), item.getValidDate()));
            item.setValidDate(article.calValidDate(item.getProductionDate(), item.getValidDate()));
            item.setStockBatch(stockBatchUtils.genProductionBatch(item.getProductionDate()));
            // 暂不校验到效期
        }

    }

    private StringBuffer verifyBinAndRefreshItems(ReturnBill bill, ReturnNtcBill returnNtcBill)
            throws WMSException {
        assert bill != null;
        assert returnNtcBill != null;
        assert bill.getWrh() != null;
        Bin bin = binService.getBinByWrhAndUsage(bill.getWrh().getUuid(),
                BinUsage.RtnReceiveTempBin);
        if (Objects.isNull(bin))
            throw new WMSException(
                    MessageFormat.format("当前仓位{0}下没有退仓收货暂存位，无法进行退仓 ", bill.getWrh().getCode()));
        StringBuffer errorMsg = new StringBuffer();
        for (ReturnBillItem item : bill.getItems()) {
            ReturnNtcBillItem ntcBillItem = rtnNtcBillService
                    .getItem(item.getReturnNtcBillItemUuid());
            if (Objects.isNull(ntcBillItem)) {
                errorMsg.append(MessageFormat.format("第{0}行的通知单明细不存在", item.getLine()));
                continue;
            }
            if (bin.getCode().equals(item.getBinCode()) == false) {
                errorMsg.append(MessageFormat.format("第{0}行的货位，不是当前仓位下的退仓收货暂存位", item.getLine()));
                continue;
            }
            if (item.getQty()
                    .compareTo(ntcBillItem.getQty().subtract(ntcBillItem.getRealQty())) > 0) {
                errorMsg.append(MessageFormat.format("第{0}行中，退仓数量，不能大于可退数量", item.getLine()));
                continue;
            }

        }
        return errorMsg;
    }

    @Override
    public void saveModify(ReturnBill bill) throws WMSException {
        // TODO Auto-generated method stub

    }

    @Override
    public void remove(String uuid, long version) throws WMSException {
        // TODO Auto-generated method stub

    }

    @Override
    public ReturnBill get(String uuid) {
        // TODO Auto-generated method stub
        return null;
    }

}
