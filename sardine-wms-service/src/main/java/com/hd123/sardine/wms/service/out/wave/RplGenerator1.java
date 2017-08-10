/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名： wms-wms-core
 * 文件名： RplGenerator.java
 * 模块说明：    
 * 修改历史：
 * 2014-8-20 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.out.wave;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.basicInfo.config.articleconfig.ArticleConfig;
import com.hd123.sardine.wms.api.basicInfo.config.articleconfig.PickBinStockLimit;
import com.hd123.sardine.wms.api.basicInfo.pickarea.PickArea;
import com.hd123.sardine.wms.api.basicInfo.pickarea.RplQtyMode;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.QpcHelper;
import com.hd123.sardine.wms.common.utils.StockConstants;
import com.hd123.sardine.wms.dao.out.wave.RplTask;

/**
 * 补货单生成器
 * 
 * @author zhangsai
 * 
 */
public class RplGenerator1 {
  // 补货信息采集器，由调用者实现、传入
  private RplCollector collector;

  /** 补货单集合 */
  private List<RplTask> rplTasks = new ArrayList<RplTask>();

  /** 可用于补货的存储位库存信息 */
  private List<RplStockInfo> rplStockInfos = new ArrayList<RplStockInfo>();

  /** 整件拣货位补货信息 */
  private List<RplRequestInfo> rplInfos = new ArrayList<RplRequestInfo>();

  // private String articleUuid;

  private PickArea pickArea;

  private ArticleConfig articleConfig;

  /**
   * 补货生成器构造器
   * 
   * @param collector
   *          补货信息收集器，not null
   */
  public RplGenerator1(RplCollector collector, ArticleConfig articleConfig) {
    Assert.assertArgumentNotNull(collector, "collector");

    this.collector = collector;
    this.articleConfig = articleConfig;
  }

  // public void setArticle(String articleUuid) {
  // this.articleUuid = articleUuid;
  // }

  /**
   * 根据补货来源库存信息{@link RplCollector#getStockInfos()}、
   * {@link RplCollector#getRplInfos()}生成补货单
   * 
   * @return 补货单集合
   */
  public void build() throws WMSException {
    List<RplRequestInfo> rplInfos = collector.getRplInfos();
    if (rplInfos.isEmpty())
      return;

    this.rplStockInfos = collector.getStockInfos();

    // 非整托补到拆零拣货位
    rplToSplitBin();
  }

  /** 获取补货生成的补货单集合 */
  public List<RplTask> getRplTasks() {
    return rplTasks;
  }

  private void generateRplTask(List<RplStockInfo> infos, RplRequestInfo rplInfo,
      BigDecimal rplQty) {
    assert rplInfo != null;
    assert infos != null;
    assert rplQty != null;

    if (BigDecimal.ZERO.compareTo(rplQty) >= 0)
      return;

    for (RplStockInfo info : infos) {
      if (BigDecimal.ZERO.compareTo(rplQty) >= 0)
        break;
      RplTask task = new RplTask();
      task.setArticle(info.getArticle());
      task.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      task.setFromBinCode(info.getBinCode());
      task.setFromContainerBarcode(info.getContainerBarcode());
      task.setProductionDate(info.getProductionDate());
      task.setQpcStr(info.getQpcStr());
      task.setStockBatch(info.getStockBatch());
      task.setSupplier(info.getSupplier());
      task.setValidDate(info.getValidDate());
      task.setToBinCode(rplInfo.getBinCode());
      task.setToContainerBarcode(StockConstants.VISUAL_STOCK_CONTAINERBARCODE);
      if (rplQty.compareTo(info.getAvailableQty()) >= 0)
        task.setQty(info.getAvailableQty());
      else
        task.setQty(rplQty);
      task.setCaseQtyStr(QpcHelper.qtyToCaseQtyStr(task.getQty(), task.getQpcStr()));
      rplTasks.add(task);

      rplQty = rplQty.subtract(task.getQty());
    }
  }

  private List<RplStockInfo> findCanRplInfos(String qpcStr, String wrhUuid) {
    assert qpcStr != null;
    assert wrhUuid != null;

    List<RplStockInfo> infos = new ArrayList<RplStockInfo>();
    for (RplStockInfo info : rplStockInfos) {
      if (info.getAvailableQty().compareTo(BigDecimal.ZERO) <= 0)
        continue;

      if (info.getWarehouse().getUuid().equals(wrhUuid) == false)
        continue;

      if (info.getQpcStr().equals(qpcStr) == false)
        continue;

      infos.addAll(findStockInfos(info.getBinCode(), info.getContainerBarcode(), qpcStr));
    }

    return infos;
  }

  /**
   * 拆零拣货位补货
   * <p>
   * 补货规则
   * <ul>
   * <li>补货来源=存储位，与整件拣货位补货一致</li>
   * <li>补货来源=优先整件拣货位，最后托盘处理不考虑“整托入”和“优先整托入”，剩余数量从整件拣货位补</li>
   * </ul>
   * 
   * @throws WMSException
   * 
   */
  private void rplToSplitBin() throws WMSException {
    if (rplInfos.isEmpty())
      return;

    PickBinStockLimit limit = articleConfig == null ? null : articleConfig.getPickBinStockLimit();
    BigDecimal binHighQty = limit == null || limit.getHighQty() == null ? BigDecimal.ZERO
        : limit.getHighQty();
    RplQtyMode rplConfig = pickArea == null || pickArea.getRplQtyMode() == null
        ? RplQtyMode.enoughShipments : pickArea.getRplQtyMode();
    for (RplRequestInfo rplInfo : rplInfos) {
      if (rplInfo.needRpl() == false)
        continue;

      List<RplStockInfo> infos = findCanRplInfos(rplInfo.getQpcStr(),
          rplInfo.getWareHouse().getUuid());
      BigDecimal canRplQty = caculateTotalQty(infos);
      BigDecimal toRplQty = rplInfo.getNeedRplQty();
      if (rplConfig.equals(RplQtyMode.highestStock)) {
        toRplQty = toRplQty.add(binHighQty);
      }

      if (canRplQty.compareTo(toRplQty) < 0)
        toRplQty = canRplQty;

      generateRplTask(infos, rplInfo, toRplQty);
    }
  }

  /**
   * 根据货位代码找出所有的该货位库存信息
   * 
   * @return 库存信息集合
   */
  private List<RplStockInfo> findStockInfos(String binCode, String containerBarcode,
      String qpcStr) {
    assert binCode != null;
    assert containerBarcode != null;
    assert qpcStr != null;

    List<RplStockInfo> stockInfos = new ArrayList<RplStockInfo>();
    for (RplStockInfo info : this.rplStockInfos) {
      if (Objects.equals(info.getContainerBarcode(), containerBarcode) == false)
        continue;
      if (info.getQpcStr().equals(qpcStr) == false)
        continue;
      if (Objects.equals(info.getBinCode(), binCode) == false)
        continue;
      if (info.isAvailable() == false)
        continue;
      stockInfos.add(info);
    }
    return stockInfos;
  }

  private BigDecimal caculateTotalQty(List<RplStockInfo> infos) {
    assert infos != null;

    BigDecimal totalQty = BigDecimal.ZERO;

    for (RplStockInfo info : infos) {
      totalQty = totalQty.add(info.getAvailableQty());
    }

    return totalQty;
  }
}
