/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	sardine-wms-service
 * 文件名：	PickUpGenerator.java
 * 模块说明：	
 * 修改历史：
 * 2014-8-21 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.service.out.wave;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.article.Article;
import com.hd123.sardine.wms.api.basicInfo.article.ArticleQpc;
import com.hd123.sardine.wms.api.basicInfo.bin.BinUsage;
import com.hd123.sardine.wms.api.basicInfo.config.articleconfig.ArticleConfig;
import com.hd123.sardine.wms.api.basicInfo.container.Container;
import com.hd123.sardine.wms.api.basicInfo.pickarea.PickArea;
import com.hd123.sardine.wms.api.basicInfo.pickarea.RplQtyMode;
import com.hd123.sardine.wms.api.out.alcntc.WaveAlcNtcItem;
import com.hd123.sardine.wms.api.out.pickup.PickType;
import com.hd123.sardine.wms.api.out.wave.WaveBinUsage;
import com.hd123.sardine.wms.api.out.wave.WavePickUpItem;
import com.hd123.sardine.wms.api.stock.StockMajorInfo;
import com.hd123.sardine.wms.api.stock.StockState;
import com.hd123.sardine.wms.common.entity.OperateMode;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.exception.WMSException;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.common.utils.QpcHelper;
import com.hd123.sardine.wms.common.utils.ScopeUtils;
import com.hd123.sardine.wms.common.utils.StockConstants;

/**
 * 拣货单生成器。
 * 
 * @author zhangsai
 * 
 */
public class PickUpGenerator {
  private String waveBillNumber;
  private String companyUuid;
  private Article article;
  private List<WaveAlcNtcItem> alcNtcItems = new ArrayList<WaveAlcNtcItem>();
  private List<WaveProcessorItem> waveProcessorItems = new ArrayList<WaveProcessorItem>();
  private List<WavePickUpItem> result = new ArrayList<WavePickUpItem>();

  private List<StockMajorInfo> stocks = new ArrayList<StockMajorInfo>();
  private List<WaveStock> waveStocks = new ArrayList<WaveStock>();
  private List<WavePickBin> storageBins = new ArrayList<WavePickBin>();
  private List<WavePickBin> dynamicPickBins = new ArrayList<WavePickBin>();
  private List<WavePickBin> fixBins = new ArrayList<WavePickBin>();

  private List<PickArea> areas;
  private ArticleConfig articleConfig;

  public void setArticleConfig(ArticleConfig articleConfig) {
    this.articleConfig = articleConfig;
  }

  /**
   * 拣货单生成器构造方法。
   * 
   * @param stockAllocateStrategy
   *          not null
   * @param roundingStrategy
   *          not null
   * @param alcNtcItems
   *          not null, not empty
   * @throws IllegalArgumentException
   */
  public PickUpGenerator(Article article, String waveBillNumber, String companyUuid) {
    Assert.assertArgumentNotNull(article, "article");
    // Assert.assertArgumentNotNull(waveUuid, "waveUuid");
    Assert.assertArgumentNotNull(waveBillNumber, "waveBillNumber");
    Assert.assertArgumentNotNull(companyUuid, "companyUuid");

    this.article = article;
    this.waveBillNumber = waveBillNumber;
    this.companyUuid = companyUuid;
    // this.waveUuid = waveUuid;
  }

  public void setStocks(List<StockMajorInfo> infos) {
    Assert.assertArgumentNotNull(infos, "infos");
    this.stocks = infos;
  }

  public void setWaveAlcNtcItems(List<WaveAlcNtcItem> items) {
    Assert.assertArgumentNotNull(items, "items");
    this.alcNtcItems = items;
  }

  public void setPickareas(List<PickArea> areas) {
    Assert.assertArgumentNotNull(areas, "areas");
    this.areas = areas;
  }

  /** 获取生成的拣货明细 */
  public List<WavePickUpItem> getPickResult() {
    return result;
  }

  /** 获取波次通知单明细 */
  public List<WaveAlcNtcItem> getWaveNtcItems() {
    return alcNtcItems;
  }

  public void build() throws WMSException {
    buildStock();
    buildWavePickBins();
    buildWaveProcessorItems();

    result.clear();

    for (WaveProcessorItem item : waveProcessorItems) {
      pickFromStorageBin(item);
    }

    for (WaveProcessorItem item : waveProcessorItems) {
      pickFromDynamicBin(item);
      pickFromFixBin(item);
    }
  }

  /**
   * 统计商品拣货位上的可以库存，以及货位类型、是否可整托拣等信息
   */
  private void buildStock() {
    String fixBinCode = articleConfig == null ? null : articleConfig.getFixedPickBin();
    for (StockMajorInfo majorInfo : stocks) {
      if (majorInfo.getArticle().getUuid().equals(article.getUuid()) == false)
        continue;

      if (majorInfo.getState().equals(StockState.normal) == false
          && Objects.equals(fixBinCode, majorInfo.getBinCode()) == false)
        continue;

      WaveStock waveStock = findWaveStock(majorInfo.getBinCode(), majorInfo.getContainerBarcode(),
          majorInfo.getQpcStr());
      if (waveStock == null) {
        waveStock = new WaveStock();
        if (Objects.equals(fixBinCode, majorInfo.getBinCode()))
          waveStock.setWaveBinUsage(WaveBinUsage.FixBin);
        else if (BinUsage.StorageBin.equals(majorInfo.getBinUsgae()))
          waveStock.setWaveBinUsage(WaveBinUsage.StorageBin);
        else if (BinUsage.PickUpStorageBin.equals(majorInfo.getBinUsgae()))
          waveStock.setWaveBinUsage(WaveBinUsage.DynamicPickBin);
        else
          continue;
        waveStock.setArticleUuid(majorInfo.getArticle().getUuid());
        waveStock.setBinCode(majorInfo.getBinCode());
        waveStock.setContainerBarcode(majorInfo.getContainerBarcode());
        waveStock.setProduceDate(majorInfo.getProductionDate());
        waveStock.setQpcStr(majorInfo.getQpcStr());
        waveStock.setMunit(majorInfo.getMunit());
        waveStock.setWarehouse(majorInfo.getWarehouse());
        waveStock.setWholeContainerUsable(
            isWholeContainerPickUsable(majorInfo.getBinCode(), majorInfo.getContainerBarcode()));
        waveStock.setUsableQty(caculateNormalQty(waveStock.getWaveBinUsage(),
            majorInfo.getBinCode(), majorInfo.getContainerBarcode(), majorInfo.getQpcStr()));
        waveStocks.add(waveStock);
      } else {
        if (majorInfo.getProductionDate().before(waveStock.getProduceDate()))
          waveStock.setProduceDate(majorInfo.getProductionDate());
      }
    }
  }

  private BigDecimal caculateNormalQty(WaveBinUsage binUsage, String binCode,
      String containerBarcode, String qpcStr) {
    assert binUsage != null;
    assert binCode != null;
    assert qpcStr != null;

    BigDecimal totalQty = BigDecimal.ZERO;
    for (StockMajorInfo majorInfo : stocks) {
      if (majorInfo.getBinCode().equals(binCode) == false)
        continue;

      if (Objects.equals(majorInfo.getContainerBarcode(), containerBarcode) == false)
        continue;

      if (majorInfo.getArticle().getUuid().equals(article.getUuid()) == false)
        continue;

      if (majorInfo.getQpcStr().equals(qpcStr) == false)
        continue;

      if (majorInfo.isConformAlclmtdays(article.getAlcLmtDays()) == false
          && binUsage.equals(WaveBinUsage.StorageBin))
        continue;

      if (binUsage.equals(WaveBinUsage.DynamicPickBin)
          || binUsage.equals(WaveBinUsage.StorageBin)) {
        if (majorInfo.getState().equals(StockState.normal))
          totalQty = totalQty.add(majorInfo.getQty());
      } else {
        if (majorInfo.getState().equals(StockState.forMoveIn))
          totalQty = totalQty.add(majorInfo.getQty());
        else if (majorInfo.getState().equals(StockState.forPick))
          totalQty = totalQty.subtract(majorInfo.getQty());
        else if (majorInfo.getState().equals(StockState.normal))
          totalQty = totalQty.add(majorInfo.getQty());
      }
    }

    return totalQty;
  }

  /**
   * 判断货位是否可整托补
   * <p>
   * 整托补条件：<br>
   * <ul>
   * <li>货位类型为存储位</li>
   * <li>货位库存非混载</li>
   * <li>只有正常状态的库存，且全部符合配送控制天数</li>
   * </ul>
   * 
   * @param binCode
   *          货位条码 not null
   * @param articleUuid
   *          商品uuid，not null
   * @param qpcStr
   *          商品拣货规格，not null
   * @return true：可整托补。false：不能整托补
   */
  private boolean isWholeContainerRplUsable(String binCode, String containerBarcode,
      String qpcStr) {
    assert binCode != null;
    assert qpcStr != null;
    assert containerBarcode != null;

    if (Objects.equals(StockConstants.VISUAL_STOCK_CONTAINERBARCODE, containerBarcode))
      return false;

    for (StockMajorInfo majorInfo : stocks) {
      if (majorInfo.getBinCode().equals(binCode) == false)
        continue;

      if (majorInfo.getBinUsgae().equals(BinUsage.StorageBin) == false)
        return false;

      if (Objects.equals(majorInfo.getContainerBarcode(), containerBarcode) == false)
        continue;

      if (majorInfo.getArticle().getUuid().equals(article.getUuid()) == false)
        return false;

      if (majorInfo.getQpcStr().equals(qpcStr) == false)
        return false;

      if (majorInfo.getState().equals(StockState.normal) == false)
        return false;

      if (majorInfo.isConformAlclmtdays(article.getAlcLmtDays()) == false)
        return false;
    }
    return true;
  }

  /**
   * 判断货位是否可整托拣
   * <p>
   * 整托拣条件：<br>
   * <ul>
   * <li>货位类型为存储位</li>
   * <li>货位库存非混载</li>
   * <li>只有正常状态的库存，且全部符合配送控制天数</li>
   * </ul>
   * 
   * @param binCode
   *          货位条码 not null
   * @param articleUuid
   *          商品uuid，not null
   * @param qpcStr
   *          商品拣货规格，not null
   * @return true：可整托拣。false：不能整托拣
   */
  private boolean isWholeContainerPickUsable(String binCode, String containerBarcode) {
    assert binCode != null;
    assert containerBarcode != null;

    if (Objects.equals(StockConstants.VISUAL_STOCK_CONTAINERBARCODE, containerBarcode))
      return false;

    BigDecimal totalQty = BigDecimal.ZERO;
    String qpcStr = null;
    Set<String> qpcStrs = new HashSet<String>();
    for (StockMajorInfo majorInfo : stocks) {
      if (majorInfo.getBinCode().equals(binCode) == false)
        continue;

      if (majorInfo.getBinUsgae().equals(BinUsage.StorageBin) == false)
        return false;

      if (Objects.equals(majorInfo.getContainerBarcode(), containerBarcode) == false)
        continue;

      if (majorInfo.getArticle().getUuid().equals(article.getUuid()) == false)
        return false;

      if (majorInfo.getState().equals(StockState.normal) == false)
        return false;

      if (majorInfo.isConformAlclmtdays(article.getAlcLmtDays()) == false)
        return false;

      totalQty = totalQty.add(majorInfo.getQty());
      qpcStrs.add(majorInfo.getQpcStr());
      qpcStr = majorInfo.getQpcStr();
    }
    if (qpcStrs.size() > 1 || qpcStrs.size() < 1)
      return false;
    if (totalQty.remainder(QpcHelper.qpcStrToQpc(qpcStr)).compareTo(BigDecimal.ZERO) == 0)
      return true;
    return false;
  }

  private WaveStock findWaveStock(String binCode, String containerBarcode, String qpcStr) {
    assert binCode != null;
    assert qpcStr != null;

    for (WaveStock waveStock : waveStocks) {
      if (Objects.equals(waveStock.getContainerBarcode(), containerBarcode) == false)
        continue;
      if (waveStock.getBinCode().equals(binCode) && qpcStr.equals(waveStock.getQpcStr()))
        return waveStock;
    }
    return null;
  }

  /**
   * 按批号构造拣货位。
   * <p>
   * <ol>
   * <li>存储位：存在可整托容器(参见 {@link WaveStock#isWholeContainerUsable()}
   * )存储位，按生产日期从小到大排序；拣货规格为库存规格；</li>
   * <li>动态拣货位：按生产日期从小到大排序，拣货规格为库存规格；</li>
   * <li>整件拣货位：整件拣货位存在库存，按生产日期从小到大排序，拣货规格为库存规格；整件拣货位无库存，按拣货方案排序，拣货规格由实际生成拣货单时决定。
   * </li>
   * <li>拆零拣货位：拆零拣货位存在库存，按生产日期从小到大排序，拣货规格为库存规格；拆零拣货位无库存，按拣货方案排序，拣货规格由实际生成拣货单时决定。
   * </li>
   * </ol>
   * 
   */
  private void buildWavePickBins() {
    String fixBin = articleConfig == null
        || StringUtil.isNullOrBlank(articleConfig.getFixedPickBin()) ? null
            : articleConfig.getFixedPickBin();
    Collections.sort(waveStocks);
    for (WaveStock waveStock : waveStocks) {
      WavePickBin pickBin = findWavePickBin(waveStock.getBinCode(), waveStock.getQpcStr(),
          waveStock.getWaveBinUsage(), waveStock.getWarehouse().getUuid());

      if (pickBin == null) {
        pickBin = new WavePickBin();
        if (Objects.equals(waveStock.getBinCode(), fixBin)) {
          pickBin.binCode = fixBin;
          pickBin.binUsage = WaveBinUsage.FixBin;
        } else if (StringUtil.isNullOrBlank(waveStock.getContainerBarcode()) == false && Objects
            .equals(waveStock.getContainerBarcode(), Container.VIRTUALITY_CONTAINER) == false) {
          pickBin.binCode = waveStock.getBinCode();
          pickBin.binUsage = WaveBinUsage.StorageBin;
        } else if (waveStock.getWaveBinUsage().equals(WaveBinUsage.DynamicPickBin)) {
          pickBin.binCode = waveStock.getBinCode();
          pickBin.binUsage = WaveBinUsage.DynamicPickBin;
        } else
          continue;
        pickBin.pickQpcStr = waveStock.getQpcStr();
        pickBin.munit = waveStock.getMunit();
        pickBin.warehouse = waveStock.getWarehouse();

        if (waveStock.getWaveBinUsage().equals(WaveBinUsage.DynamicPickBin)) {
          dynamicPickBins.add(pickBin);
        } else if (waveStock.getWaveBinUsage().equals(WaveBinUsage.FixBin)) {
          fixBins.add(pickBin);
        } else {
          storageBins.add(pickBin);
        }

        refreshPickConfig(pickBin);
      }
    }

    for (WaveStock waveStock : waveStocks) {
      if (waveStock.getWaveBinUsage().equals(WaveBinUsage.DynamicPickBin)
          || waveStock.getWaveBinUsage().equals(WaveBinUsage.FixBin))
        continue;

      WavePickBin pickBin = findWavePickBin(fixBin, waveStock.getQpcStr(), WaveBinUsage.FixBin,
          waveStock.getWarehouse().getUuid());
      if (pickBin == null) {
        pickBin = new WavePickBin();
        pickBin.binCode = fixBin;
        pickBin.binUsage = WaveBinUsage.FixBin;
        pickBin.munit = waveStock.getMunit();
        pickBin.pickQpcStr = waveStock.getQpcStr();
        pickBin.warehouse = waveStock.getWarehouse();
        refreshPickConfig(pickBin);
        fixBins.add(pickBin);
      }
    }
  }

  private void refreshPickConfig(WavePickBin pickBin) {
    assert pickBin != null;

    PickArea area = findPickArea(pickBin.binCode);
    if (area == null)
      area = new PickArea();

    pickBin.pickArea = new UCN(area.getUuid() == null ? "-" : area.getUuid(), area.getCode(),
        area.getName());
    pickBin.operateMethod = area.getPickMode();
    pickBin.splitVolume = area.getPickVolume();
    pickBin.rplMode = area.getRplMode();
    pickBin.rplQtyMode = area.getRplQtyMode();
  }

  private PickArea findPickArea(String binCode) {
    assert binCode != null;

    for (PickArea area : areas) {
      if (ScopeUtils.contains(area.getBinScope(), binCode))
        return area;
    }
    return null;
  }

  private WavePickBin findWavePickBin(String binCode, String qpcStr, WaveBinUsage binUsage,
      String wrhUuid) {
    assert binUsage != null;

    List<WavePickBin> pickBins = null;
    if (binUsage.equals(WaveBinUsage.DynamicPickBin))
      pickBins = dynamicPickBins;
    else if (binUsage.equals(WaveBinUsage.StorageBin))
      pickBins = storageBins;
    else if (binUsage.equals(WaveBinUsage.FixBin))
      pickBins = fixBins;
    for (WavePickBin pickBin : pickBins) {
      if (StringUtil.isNullOrBlank(binCode) == false && binCode.equals(pickBin.binCode) == false)
        continue;

      if (StringUtil.isNullOrBlank(qpcStr) == false && qpcStr.equals(pickBin.pickQpcStr) == false)
        continue;

      if (Objects.equals(wrhUuid, pickBin.warehouse.getUuid()) == false)
        continue;

      return pickBin;
    }
    return null;
  }

  /**
   * 构造波次拣货明细，并根据商品和门店设置起要量
   * 
   * @throws WMSException
   */
  private void buildWaveProcessorItems() throws WMSException {
    for (WaveAlcNtcItem alcItem : alcNtcItems) {
      WaveProcessorItem processorItem = findWaveProcessorItem(alcItem);
      if (processorItem == null) {
        processorItem = new WaveProcessorItem();
        processorItem.setArticleUuid(article.getUuid());
        processorItem.setCustomer(alcItem.getCustomer());
        processorItem.setDeliveryType(alcItem.getDeliveryType());
        processorItem.setWarehouse(alcItem.getWrh());
        waveProcessorItems.add(processorItem);
      }
      processorItem.setAlcQty(processorItem.getAlcQty().add(alcItem.getAlcQty()));
    }
  }

  private WaveProcessorItem findWaveProcessorItem(WaveAlcNtcItem alcItem) {
    assert alcItem != null;

    for (WaveProcessorItem processorItem : waveProcessorItems) {
      if (processorItem.getCustomer().getUuid().equals(alcItem.getCustomer().getUuid()) == false)
        continue;
      if (Objects.equals(processorItem.getDeliveryType(), alcItem.getDeliveryType()) == false)
        continue;
      if (Objects.equals(processorItem.getWarehouse().getUuid(),
          alcItem.getWrh().getUuid()) == false)
        continue;
      return processorItem;
    }
    return null;
  }

  private void pickFromStorageBin(WaveProcessorItem item) {
    assert item != null;
    if (item.needPickUp() == false) {
      return;
    }

    for (WavePickBin pickBin : storageBins) {
      if (pickBin.warehouse.getUuid().equals(item.getWarehouse().getUuid()) == false) {
        continue;
      }

      for (WaveStock stock : waveStocks) {
        if (item.needPickUp() == false)
          return;

        if (StringUtil.isNullOrBlank(stock.getContainerBarcode())
            || Objects.equals(stock.getContainerBarcode(), Container.VIRTUALITY_CONTAINER))
          continue;

        if (pickBin.binCode.equals(stock.getBinCode()) == false)
          continue;

        if (pickBin.pickQpcStr.equals(stock.getQpcStr()) == false) {
          continue;
        }

        if (stock.isWholeContainerUsable() == false || stock.isAvailable() == false) {
          continue;
        }

        if (item.getToPickUpQty().compareTo(stock.getAvailableQty()) >= 0) {
          BigDecimal pickQty = stock.getAvailableQty();
          item.addPickQty(pickQty);
          stock.addPickQty(pickQty);
          pickBin.pickQty = pickBin.pickQty.add(pickQty);
          genPickTasks(item, pickBin, pickQty, stock.getContainerBarcode());
        }
      }
    }
  }

  private void pickFromDynamicBin(WaveProcessorItem item) {
    assert item != null;
    if (item.needPickUp() == false) {
      return;
    }

    for (WavePickBin pickBin : dynamicPickBins) {
      for (WaveStock stock : waveStocks) {
        if (item.needPickUp() == false) {
          return;
        }

        if (pickBin.pickQpcStr.equals(stock.getQpcStr()) == false)
          continue;

        if (pickBin.binCode.equals(stock.getBinCode()) == false) {
          continue;
        }

        BigDecimal pickQty = BigDecimal.ZERO;
        if (item.getToPickUpQty().compareTo(stock.getAvailableQty()) > 0) {
          pickQty = stock.getAvailableQty();
        } else {
          pickQty = item.getToPickUpQty();
        }
        item.addPickQty(pickQty);

        if (pickQty.compareTo(BigDecimal.ZERO) > 0) {
          stock.addPickQty(pickQty);
          pickBin.pickQty = pickBin.pickQty.add(pickQty);
          genPickTasks(item, pickBin, pickQty, Container.VIRTUALITY_CONTAINER);
        }
      }
    }
  }

  private List<WaveStock> findWaveStocks(String binCode, WaveBinUsage binUsage, String qpcStr,
      UCN warehouse) {
    assert binCode != null;
    assert warehouse != null;
    assert binUsage != null;

    if (StringUtil.isNullOrBlank(qpcStr)) {
      for (WaveStock waveStock : waveStocks) {
        if (waveStock.getWarehouse().equals(warehouse) == false)
          continue;

        if (waveStock.getWaveBinUsage().equals(WaveBinUsage.StorageBin) == false)
          continue;

        if (fetchQpcStrFromPickBin().contains(waveStock.getQpcStr()))
          continue;

        if (waveStock.isAvailable()) {
          qpcStr = waveStock.getQpcStr();
          break;
        }
      }
    }

    List<WaveStock> pickStocks = new ArrayList<WaveStock>();
    for (WaveStock waveStock : waveStocks) {
      if (waveStock.getBinCode().equals(binCode) && waveStock.getQpcStr().equals(qpcStr))
        pickStocks.add(waveStock);

      if (waveStock.isAvailable() == false)
        continue;

      if (waveStock.getQpcStr().equals(qpcStr) && waveStock.getWarehouse().equals(warehouse)
          && (waveStock.getWaveBinUsage().equals(WaveBinUsage.StorageBin))
          && waveStock.isAvailable())
        pickStocks.add(waveStock);
    }
    return pickStocks;
  }

  private List<String> fetchQpcStrFromPickBin() {
    List<String> qpcStrs = new ArrayList<String>();
    for (WavePickBin pickBin : fixBins) {
      if (StringUtil.isNullOrBlank(pickBin.pickQpcStr) == false)
        qpcStrs.add(pickBin.pickQpcStr);
    }

    return qpcStrs;
  }

  private BigDecimal caculateStockQty(List<WaveStock> rplStocks) {
    assert rplStocks != null;
    BigDecimal stockQty = BigDecimal.ZERO;
    for (WaveStock waveStock : rplStocks) {
      stockQty = stockQty.add(waveStock.getAvailableQty());
    }
    return stockQty;
  }

  private void pickFromFixBin(WaveProcessorItem item) throws WMSException {
    assert item != null;

    for (WavePickBin pickBin : fixBins) {
      if (item.needPickUp() == false) {
        return;
      }

      List<WaveStock> pickStocks = findWaveStocks(pickBin.binCode, WaveBinUsage.FixBin,
          pickBin.pickQpcStr, item.getWarehouse());

      if (StringUtil.isNullOrBlank(pickBin.pickQpcStr)) {
        pickBin.pickQpcStr = pickStocks.get(0).getQpcStr();
      }

      BigDecimal stockQty = caculateStockQty(pickStocks);
      stockQty = stockQty.subtract(pickBin.pickQty);

      BigDecimal pickQty = null;
      if (item.getToPickUpQty().compareTo(stockQty) > 0) {
        pickQty = stockQty;
      } else {
        pickQty = item.getToPickUpQty();
      }
      item.addPickQty(pickQty);

      if (pickQty.compareTo(BigDecimal.ZERO) > 0) {
        pickBin.pickQty = pickBin.pickQty.add(pickQty);
        genPickTasks(item, pickBin, pickQty, Container.VIRTUALITY_CONTAINER);
      }
    }
  }

  private void genPickTasks(WaveProcessorItem processItem, WavePickBin pickBin, BigDecimal pickQty,
      String containerBarcode) {
    assert processItem != null;
    assert pickBin != null;
    assert pickQty != null;

    List<WaveAlcNtcItem> alcItems = findAlcNtcItems(processItem);
    if (alcItems.isEmpty())
      return;

    for (WaveAlcNtcItem alcItem : alcItems) {
      if (pickQty.compareTo(BigDecimal.ZERO) <= 0)
        break;

      WavePickUpItem pickItem = new WavePickUpItem();
      pickItem.setAlcNtcBillNumber(alcItem.getAlcNtcBillNumber());
      pickItem.setAlcNtcBillUuid(alcItem.getAlcNtcBillUuid());
      pickItem.setAlcNtcBillItemUuid(alcItem.getAlcNtcItemUuid());
      pickItem.setArticle(new UCN(article.getUuid(), article.getCode(), article.getName()));
      pickItem.setArticleSpec(article.getSpec());
      pickItem.setBinCode(pickBin.binCode);
      pickItem.setContainerBarcode(containerBarcode);
      pickItem.setDeliveryType(alcItem.getDeliveryType());
      pickItem.setOperateMethod(pickBin.operateMethod);
      pickItem.setPickArea(pickBin.pickArea);
      pickItem.setQpcStr(pickBin.pickQpcStr);
      pickItem.setMunit(pickBin.munit);
      if (alcItem.getAlcQty().compareTo(pickQty) >= 0)
        pickItem.setQty(pickQty);
      else
        pickItem.setQty(alcItem.getAlcQty());
      pickItem.setCaseQtyStr(QpcHelper.qtyToCaseQtyStr(pickItem.getQty(), pickItem.getQpcStr()));
      pickItem.setCustomer(processItem.getCustomer());
      pickItem.setVolume(pickBin.splitVolume);
      pickItem.setWaveBillNumber(waveBillNumber);
      pickItem.setBinUsage(pickBin.binUsage);
      pickItem.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
      if (pickBin.binUsage.equals(WaveBinUsage.StorageBin))
        pickItem.setType(PickType.WholeContainer);
      else
        pickItem.setType(PickType.PartContainer);
      pickItem.setItemVolume(caculateVolume(pickItem.getQpcStr(), pickItem.getQty()));
      pickItem.setPickOrder(alcItem.getPickOrder());
      pickItem.setAlcNtcBillUuid(alcItem.getAlcNtcBillUuid());
      pickItem.setAlcNtcBillNumber(alcItem.getAlcNtcBillNumber());
      result.add(pickItem);

      pickQty = pickQty.subtract(pickItem.getQty());
    }
  }

  private BigDecimal caculateVolume(String qpcStr, BigDecimal qty) {
    assert qpcStr != null;
    assert qty != null;

    for (ArticleQpc qpc : article.getQpcs()) {
      if (Objects.equals(qpc.getQpcStr(), qpcStr)) {
        BigDecimal volumn = qpc.getLength().multiply(qpc.getWidth()).multiply(qpc.getHeight());
        BigDecimal itemVolumn = qty.divide(QpcHelper.qpcStrToQpc(qpcStr)).multiply(volumn);
        return itemVolumn;
      }
    }
    return BigDecimal.ZERO;
  }

  private List<WaveAlcNtcItem> findAlcNtcItems(WaveProcessorItem processItem) {
    List<WaveAlcNtcItem> result = new ArrayList<WaveAlcNtcItem>();
    for (WaveAlcNtcItem alcItem : alcNtcItems) {
      if (alcItem.getCustomer().getUuid().equals(processItem.getCustomer().getUuid()) == false)
        continue;

      if (Objects.equals(alcItem.getDeliveryType(), processItem.getDeliveryType()) == false)
        continue;

      if (alcItem.getWrh().getUuid().equals(processItem.getWarehouse().getUuid()) == false)
        continue;
      result.add(alcItem);
    }
    return result;
  }

  private BigDecimal caculateStockQty(String binCode, String qpcStr) {
    assert binCode != null;

    if (StringUtil.isNullOrBlank(qpcStr))
      return BigDecimal.ZERO;

    BigDecimal stockQty = BigDecimal.ZERO;
    for (WaveStock waveStock : waveStocks) {
      if (waveStock.getBinCode().equals(binCode) == false)
        continue;

      if (waveStock.getQpcStr().equals(qpcStr) == false)
        continue;

      stockQty = stockQty.add(waveStock.getAvailableQty());
    }

    return stockQty;
  }

  /********************************
   * ****** 风骚的风隔线
   ******************************************/

  /** 获取补货信息收集器。 */
  public RplCollector getRplCollector() {
    return new WaveRplCollector();
  }

  private class WaveRplCollector implements RplCollector {

    @Override
    public List<RplRequestInfo> getRplInfos() {
      List<RplRequestInfo> rplInfos = new ArrayList<RplRequestInfo>();

      for (WavePickBin pickBin : fixBins) {
        RplRequestInfo rplInfo = createRplInfo(pickBin);
        if (rplInfo != null)
          rplInfos.add(rplInfo);
      }
      return rplInfos;
    }

    @Override
    public List<RplStockInfo> getStockInfos() {
      List<RplStockInfo> rplStockInfos = new ArrayList<RplStockInfo>();
      for (StockMajorInfo majorInfo : stocks) {
        if (majorInfo.getBinUsgae().equals(BinUsage.StorageBin) == false)
          continue;

        if (majorInfo.getArticle().getUuid().equals(article.getUuid()) == false)
          continue;

        if (majorInfo.getState().equals(StockState.normal) == false)
          continue;

        if (majorInfo.isConformAlclmtdays(article.getAlcLmtDays()) == false)
          continue;

        if (isWholeContainerPickUp(majorInfo.getBinCode(), majorInfo.getContainerBarcode()))
          continue;

        RplStockInfo rplStockInfo = createRplStockInfo(majorInfo);
        rplStockInfo.setWholeContainerRplUse(isWholeContainerRplUsable(majorInfo.getBinCode(),
            majorInfo.getContainerBarcode(), majorInfo.getQpcStr()));
        rplStockInfos.add(rplStockInfo);
      }
      return rplStockInfos;
    }

    private boolean isWholeContainerPickUp(String binCode, String containerBarcode) {
      assert binCode != null;

      for (WavePickUpItem item : result) {
        if (item.getBinCode().equals(binCode)
            && item.getContainerBarcode().equals(containerBarcode))
          return true;
      }

      return false;
    }

    private RplStockInfo createRplStockInfo(StockMajorInfo majorInfo) {
      assert majorInfo != null;

      RplStockInfo rplStockInfo = new RplStockInfo();
      rplStockInfo.setArticle(majorInfo.getArticle());
      rplStockInfo.setArticleSpec(majorInfo.getArticleSpec());
      rplStockInfo.setBinCode(majorInfo.getBinCode());
      if (majorInfo.getBinUsgae().equals(BinUsage.StorageBin))
        rplStockInfo.setBinUsage(WaveBinUsage.StorageBin);
      rplStockInfo.setContainerBarcode(majorInfo.getContainerBarcode());
      rplStockInfo.setCompanyUuid(majorInfo.getCompanyUuid());
      rplStockInfo.setOwner(majorInfo.getOwner());
      rplStockInfo.setProductionDate(majorInfo.getProductionDate());
      rplStockInfo.setQpcStr(majorInfo.getQpcStr());
      rplStockInfo.setUsableQty(majorInfo.getQty());
      rplStockInfo.setStockBatch(majorInfo.getStockBatch());
      rplStockInfo.setSupplier(majorInfo.getSupplier());
      rplStockInfo.setProductionBatch(majorInfo.getProductionBatch());
      rplStockInfo.setValidDate(majorInfo.getValidDate());
      rplStockInfo.setWarehouse(majorInfo.getWarehouse());
      rplStockInfo.setMunit(majorInfo.getMunit());

      return rplStockInfo;
    }

    private RplRequestInfo createRplInfo(WavePickBin pickBin) {
      assert pickBin != null;

      BigDecimal stockQty = caculateStockQty(pickBin.binCode, pickBin.pickQpcStr);
      if (pickBin.pickQty.compareTo(stockQty) <= 0)
        return null;
      RplRequestInfo rplInfo = new RplRequestInfo();
      rplInfo.setArticleUuid(article.getUuid());
      rplInfo.setBinCode(pickBin.binCode);
      rplInfo.setCompanyUuid(companyUuid);
      rplInfo.setPickArea(pickBin.pickArea);
      rplInfo.setQpcStr(pickBin.pickQpcStr);
      rplInfo.setRplBinUsage(pickBin.binUsage);
      rplInfo.setWaveBillNumber(waveBillNumber);
      rplInfo.setWareHouse(pickBin.warehouse);
      rplInfo.setStockQty(stockQty.subtract(pickBin.pickQty));
      rplInfo.setNeedRplQty(pickBin.pickQty.subtract(stockQty));
      rplInfo.setMethod(pickBin.rplMode);
      rplInfo.setRplQtyMode(pickBin.rplQtyMode);
      return rplInfo;
    }
  }

  private class WavePickBin {
    public UCN warehouse;
    public UCN pickArea;
    public OperateMode operateMethod;
    public BigDecimal splitVolume;
    public String binCode;
    public WaveBinUsage binUsage;
    public String pickQpcStr;
    public String munit;
    public BigDecimal pickQty = BigDecimal.ZERO;
    public OperateMode rplMode;
    public RplQtyMode rplQtyMode;
  }
}
