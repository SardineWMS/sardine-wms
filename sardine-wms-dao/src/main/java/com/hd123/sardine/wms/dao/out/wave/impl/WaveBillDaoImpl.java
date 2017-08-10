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

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.out.alcntc.WaveAlcNtcItem;
import com.hd123.sardine.wms.api.out.wave.WaveBill;
import com.hd123.sardine.wms.api.out.wave.WaveBillItem;
import com.hd123.sardine.wms.common.dao.impl.BaseDaoImpl;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.dao.out.wave.WaveBillDao;

/**
 * @author yangwenzhu
 *
 */
public class WaveBillDaoImpl extends BaseDaoImpl<WaveBill> implements WaveBillDao {
  public static final String MAPPER_GETBYBILLNUMBER = "getByBillNumber";
  public static final String MAPPER_INSERTITEM = "insertItem";
  public static final String MAPPER_REMOVEITEMS = "removeItems";
  public static final String MAPPER_GETITEMSBYWAVEBILLUUID = "getItemsByWaveBillUuid";
  public static final String QUERYWAVEALCNTCITEMS = "queryWaveAlcNtcItems";
  public static final String UPDATEWAVEALCNTCITEMSSTATE = "updateWaveAlcNtcItemsState";
  public static final String SAVEWAVEALCNTCITEMS = "saveWaveAlcNtcItems";
  public static final String SAVEPICKTASK = "savePickTask";
  public static final String SAVERPLTASK = "saveRplTask";
  public static final String LOCKSTOCK = "lockStock";

  @Override
  public WaveBill getByBillNumber(String billNumber) {
    if (StringUtil.isNullOrBlank(billNumber))
      return null;
    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("billNumber", billNumber);
    return getSqlSession().selectOne(generateStatement(MAPPER_GETBYBILLNUMBER), map);
  }

  @Override
  public void insertItems(List<WaveBillItem> items) {
    Assert.assertArgumentNotNull(items, "items");
    for (WaveBillItem item : items) {
      getSqlSession().insert(generateStatement(MAPPER_INSERTITEM), item);
    }
  }

  @Override
  public void removeItems(String waveBillUuid) {
    Assert.assertArgumentNotNull(waveBillUuid, "waveBillUuid");

    getSqlSession().delete(generateStatement(MAPPER_REMOVEITEMS), waveBillUuid);

  }

  @Override
  public List<WaveBillItem> getItemsByWaveBillUuid(String waveBillUuid) {
    if (StringUtil.isNullOrBlank(waveBillUuid))
      return new ArrayList<>();

    return getSqlSession().selectList(generateStatement(MAPPER_GETITEMSBYWAVEBILLUUID),
        waveBillUuid);
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
  public void saveWaveAlcNtcItems(String sql) {
    if (StringUtil.isNullOrBlank(sql))
      return;

    insert(SAVEWAVEALCNTCITEMS, sql);
  }

  @Override
  public void savePickTask(String sql) {
    if (StringUtil.isNullOrBlank(sql))
      return;

    insert(SAVEPICKTASK, sql);
  }

  @Override
  public void saveRplTask(String sql) {
    if (StringUtil.isNullOrBlank(sql))
      return;

    insert(SAVERPLTASK, sql);
  }

  @Override
  public void lockStock(String waveBillNumber, String waveBillUuid) {
    if (StringUtil.isNullOrBlank(waveBillUuid) || StringUtil.isNullOrBlank(waveBillNumber))
      return;

    Map<String, Object> map = ApplicationContextUtil.map();
    map.put("waveBillNumber", waveBillNumber);
    map.put("waveBillUuid", waveBillUuid);
    selectList(LOCKSTOCK, map);
  }
}
