/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	WaveBillDao.java
 * 模块说明：	
 * 修改历史：
 * 2017年6月21日 - yangwenzhu - 创建。
 */
package com.hd123.sardine.wms.dao.out.wave;

import java.util.List;

import com.hd123.sardine.wms.api.out.alcntc.WaveAlcNtcItem;
import com.hd123.sardine.wms.api.out.wave.PickRule;
import com.hd123.sardine.wms.api.out.wave.WaveBill;
import com.hd123.sardine.wms.api.out.wave.WavePickUpItem;
import com.hd123.sardine.wms.api.task.Task;
import com.hd123.sardine.wms.common.dao.BaseDao;

/**
 * @author yangwenzhu
 *
 */
public interface WaveBillDao extends BaseDao<WaveBill> {

  WaveBill getByBillNumber(String billNumber);

  List<WaveAlcNtcItem> queryWaveAlcNtcItems(String waveBillNumber, List<String> articleUuids);

  int updateWaveAlcNtcItemsState(String waveBillNumber, List<String> articleUuids);

  void removeWaveAlcNtcItems(String waveBillUuid);
  
  void saveWaveAlcNtcItems(String waveBillUuid);

  List<String> queryWaveArticleUuids(String waveBillUuid);
  
  void saveWavePickUpItems(List<WavePickUpItem> pickResult);
  
  List<PickRule> queryPickRules(String uuid);
  
  List<WavePickUpItem> queryPickItem(String waveUuid);
  
  void insertRplTasks(List<Task> rplTasks);
  
  List<Task> queryRplTasks(String waveUuid);
  
  void insertRplTaskToTask(String waveUuid);
  
  List<WavePickUpItem> queryPickItemByPickRule(PickRule pickRule);
  
  void removeRplTasks(String waveUuid);
  
  void removeWavePickUpItems(String waveUuid);
}
