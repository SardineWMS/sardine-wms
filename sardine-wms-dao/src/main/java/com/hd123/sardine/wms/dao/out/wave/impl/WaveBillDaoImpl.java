/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	WaveBillDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月21日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.dao.out.wave.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.out.alcntc.WaveAlcNtcItem;
import com.hd123.sardine.wms.api.out.wave.PickRule;
import com.hd123.sardine.wms.api.out.wave.WaveBill;
import com.hd123.sardine.wms.api.out.wave.WavePickUpItem;
import com.hd123.sardine.wms.common.dao.impl.BaseDaoImpl;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.dao.out.wave.WaveBillDao;

/**
 * @author yangwenzhu
 *
 */
public class WaveBillDaoImpl extends BaseDaoImpl<WaveBill> implements WaveBillDao {
  private static final String MAPPER_GETBYBILLNUMBER = "getByBillNumber";
  private static final String QUERYWAVEALCNTCITEMS = "queryWaveAlcNtcItems";
  private static final String UPDATEWAVEALCNTCITEMSSTATE = "updateWaveAlcNtcItemsState";
  private static final String SAVEWAVEALCNTCITEMS = "saveWaveAlcNtcItems";
  private static final String REMOVEWAVEALCNTCITEMS = "removeWaveAlcNtcItems";
  private static final String QUERYWAVEARTICLEUUIDS = "queryWaveArticleUuids";
  private static final String SAVEWAVEPICKUPITEMS = "saveWavePickUpItems";
  private static final String QUERYPICKRULES = "queryPickRules";
  private static final String QUERYPICKITEM = "queryPickItem";
  private static final String QUERYPICKITEMBYPICKRULE = "queryPickItemByPickRule";
  private static final String REMOVEWAVEPICKUPITEMS = "removeWavePickUpItems";

  @Override
  public WaveBill getByBillNumber(String billNumber) {
    if (StringUtil.isNullOrBlank(billNumber))
      return null;
    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("billNumber", billNumber);
    return getSqlSession().selectOne(generateStatement(MAPPER_GETBYBILLNUMBER), map);
  }

  @Override
  public List<WaveAlcNtcItem> queryWaveAlcNtcItems(String waveBillNumber,
      List<String> articleUuids) {
    if (StringUtil.isNullOrBlank(waveBillNumber) || CollectionUtils.isEmpty(articleUuids))
      return new ArrayList<WaveAlcNtcItem>();

    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("waveBillNumber", waveBillNumber);
    map.put("articleUuids", articleUuids);
    return selectList(QUERYWAVEALCNTCITEMS, map);
  }

  @Override
  public int updateWaveAlcNtcItemsState(String waveBillNumber, List<String> articleUuids) {
    if (StringUtil.isNullOrBlank(waveBillNumber) || CollectionUtils.isEmpty(articleUuids))
      return 0;

    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("waveBillNumber", waveBillNumber);
    map.put("articleUuids", articleUuids);
    return update(UPDATEWAVEALCNTCITEMSSTATE, map);
  }

  @Override
  public void saveWaveAlcNtcItems(String waveUuid, String waveBillNumber) {
    if (StringUtil.isNullOrBlank(waveUuid) || StringUtil.isNullOrBlank(waveBillNumber))
      return;

    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("waveBillNumber", waveBillNumber);
    map.put("waveUuid", waveUuid);
    insert(SAVEWAVEALCNTCITEMS, map);
  }

  @Override
  public void removeWaveAlcNtcItems(String waveBillNumber) {
    if (StringUtil.isNullOrBlank(waveBillNumber))
      return;
    
    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("waveBillNumber", waveBillNumber);
    delete(REMOVEWAVEALCNTCITEMS, map);
  }

  @Override
  public List<String> queryWaveArticleUuids(String waveBillNumber) {
    if (StringUtil.isNullOrBlank(waveBillNumber))
      return new ArrayList<String>();

    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("waveBillNumber", waveBillNumber);
    
    return selectList(QUERYWAVEARTICLEUUIDS, map);
  }

  @Override
  public void saveWavePickUpItems(List<WavePickUpItem> pickResult) {
    if (pickResult.isEmpty())
      return;

    for (WavePickUpItem pitem : pickResult)
      insert(SAVEWAVEPICKUPITEMS, pitem);
  }

  @Override
  public List<PickRule> queryPickRules(String uuid) {
    if (StringUtil.isNullOrBlank(uuid))
      return new ArrayList<PickRule>();

    return selectList(QUERYPICKRULES, uuid);
  }

  @Override
  public List<WavePickUpItem> queryPickItem(String waveUuid) {
    if (StringUtil.isNullOrBlank(waveUuid))
      return new ArrayList<WavePickUpItem>();

    return selectList(QUERYPICKITEM, waveUuid);
  }

  @Override
  public List<WavePickUpItem> queryPickItemByPickRule(PickRule pickRule) {
    if (pickRule == null)
      return new ArrayList<WavePickUpItem>();
    return selectList(QUERYPICKITEMBYPICKRULE, pickRule);
  }

  @Override
  public void removeWavePickUpItems(String waveUuid) {
    delete(REMOVEWAVEPICKUPITEMS, waveUuid);
  }
}
