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
import com.hd123.sardine.wms.api.task.Task;
import com.hd123.sardine.wms.common.dao.impl.BaseDaoImpl;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.dao.out.wave.WaveBillDao;

/**
 * @author yangwenzhu
 *
 */
public class WaveBillDaoImpl extends BaseDaoImpl<WaveBill> implements WaveBillDao {
  public static final String MAPPER_GETBYBILLNUMBER = "getByBillNumber";
  public static final String QUERYWAVEALCNTCITEMS = "queryWaveAlcNtcItems";
  public static final String UPDATEWAVEALCNTCITEMSSTATE = "updateWaveAlcNtcItemsState";
  public static final String SAVEWAVEALCNTCITEMS = "saveWaveAlcNtcItems";
  public static final String REMOVEWAVEALCNTCITEMS = "removeWaveAlcNtcItems";
  public static final String QUERYWAVEARTICLEUUIDS = "queryWaveArticleUuids";
  public static final String SAVEWAVEPICKUPITEMS = "saveWavePickUpItems";
  public static final String QUERYPICKRULES = "queryPickRules";
  public static final String QUERYPICKITEM = "queryPickItem";
  public static final String INSERTRPLTASKS = "insertRplTasks";
  public static final String QUERYRPLTASKS = "queryRplTasks";
  public static final String INSERTRPLTASKTOTASK = "insertRplTaskToTask";
  public static final String QUERYPICKITEMBYPICKRULE = "queryPickItemByPickRule";
  public static final String REMOVERPLTASKS = "removeRplTasks";
  public static final String REMOVEWAVEPICKUPITEMS = "removeWavePickUpItems";

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
  public void saveWaveAlcNtcItems(String waveUuid) {
    if (StringUtil.isNullOrBlank(waveUuid))
      return;

    insert(SAVEWAVEALCNTCITEMS, waveUuid);
  }

  @Override
  public void removeWaveAlcNtcItems(String waveBillUuid) {
    if (StringUtil.isNullOrBlank(waveBillUuid))
      return;

    delete(REMOVEWAVEALCNTCITEMS, waveBillUuid);
  }

  @Override
  public List<String> queryWaveArticleUuids(String waveBillUuid) {
    if (StringUtil.isNullOrBlank(waveBillUuid))
      return new ArrayList<String>();

    return selectList(QUERYWAVEARTICLEUUIDS, waveBillUuid);
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
  public void insertRplTasks(List<Task> rplTasks) {
    if (rplTasks.isEmpty())
      return;

    for (Task task : rplTasks)
      insert(INSERTRPLTASKS, task);
  }

  @Override
  public List<Task> queryRplTasks(String waveUuid) {
    if (StringUtil.isNullOrBlank(waveUuid))
      return new ArrayList<Task>();

    return selectList(QUERYRPLTASKS, waveUuid);
  }

  @Override
  public void insertRplTaskToTask(String waveUuid) {
    selectList(INSERTRPLTASKTOTASK, waveUuid);
  }

  @Override
  public List<WavePickUpItem> queryPickItemByPickRule(PickRule pickRule) {
    if (pickRule == null)
      return new ArrayList<WavePickUpItem>();
    return selectList(QUERYPICKITEMBYPICKRULE, pickRule);
  }

  @Override
  public void removeRplTasks(String waveUuid) {
    delete(REMOVERPLTASKS, waveUuid);
  }

  @Override
  public void removeWavePickUpItems(String waveUuid) {
    delete(REMOVEWAVEPICKUPITEMS, waveUuid);
  }
}
