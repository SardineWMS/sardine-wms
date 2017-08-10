/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2014，所有权利保留。
 * 
 * 项目名：	wms-wms-core
 * 文件名：	StockDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2014-4-10 - WUJING - 创建。
 */
package com.hd123.sardine.wms.dao.stock.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.api.stock.Stock;
import com.hd123.sardine.wms.api.stock.StockChangement;
import com.hd123.sardine.wms.api.stock.StockExtendInfo;
import com.hd123.sardine.wms.api.stock.StockFilter;
import com.hd123.sardine.wms.api.stock.StockMajorFilter;
import com.hd123.sardine.wms.api.stock.StockMajorInfo;
import com.hd123.sardine.wms.api.stock.StockState;
import com.hd123.sardine.wms.common.dao.NameSpaceSupport;
import com.hd123.sardine.wms.dao.stock.PStockShiftIn;
import com.hd123.sardine.wms.dao.stock.PStockShiftRule;
import com.hd123.sardine.wms.dao.stock.StockCmd;
import com.hd123.sardine.wms.dao.stock.StockDao;
import com.hd123.sardine.wms.dao.stock.StockShiftMessage;

/**
 * @author WUJING
 * 
 */
@Repository
public class StockDaoImpl extends NameSpaceSupport implements StockDao {
  private static String CLEARPROCEDURE = "clearProcedures";
  private static String EXCUTESHIFTIN = "executeShiftIn";
  private static String EXCUTESHIFTOUT = "executeShiftOut";
  private static String EXECUTESHIFT = "executeShift";
  private static String EXCUTECHANGEMENT = "executeChangement";

  private static String INSERTSTOCK = "insert_Stock";
  private static String INSERTSTOCKRULE = "insert_StockRule";
  private static String QUERYSTOCK = "queryStock";
  private static String QUERYSTOCKEXTENDINFO = "queryStockextendinfo";
  private static String QUERYSTOCKCHANGEMENT = "queryStockChangement";

  private static String GETSTOCKMESSAGE = "getStockMessage";

  private static String SAVESTOCKCMD = "insert_StockCmd";

  private static String QUERY_STOCKMAJORINFO = "queryMajorStockInfo";

  @Override
  public void executeClearProcedure(String workId) {
    Assert.assertArgumentNotNull(workId, "workId");
    selectOne(CLEARPROCEDURE, workId);
  }

  @Override
  public int executeShiftIn(String workId) {
    Assert.assertArgumentNotNull(workId, "workId");

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("workId", workId);
    map.put("result", 0);

    List<Map<String, Object>> list = selectList(EXCUTESHIFTIN, map);
    if (list != null && list.size() > 0)
      return (Integer) list.get(0).get("result");
    return 0;
  }

  @Override
  public int executeShiftOut(String workId) {
    Assert.assertArgumentNotNull(workId, "workId");

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("workId", workId);
    map.put("result", 0);
    List<Integer> list = selectList(EXCUTESHIFTOUT, map);
    if (list != null && list.size() > 0)
      return list.get(0);
    return 0;
  }

  @Override
  public int executeShift(String workId, String binCode, String containerBarcode,
      StockState state) {
    Assert.assertArgumentNotNull(workId, "workId");

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("workId", workId);
    map.put("binCode", binCode);
    map.put("containerBarcode", containerBarcode);
    map.put("state", state);

    map.put("result", 0);
    List<Integer> list = selectList(EXECUTESHIFT, map);
    if (list != null && list.size() > 0)
      return list.get(0);
    return 0;

  }

  @Override
  public int executeChange(String workId, String targetState) {
    Assert.assertArgumentNotNull(workId, "workId");
    Assert.assertArgumentNotNull(targetState, "targetState");

    Map<String, String> map = new HashMap<String, String>();
    map.put("workId", workId);
    map.put("targetState", targetState);

    List<Integer> list = selectList(EXCUTECHANGEMENT, map);
    if (list != null && list.size() > 0)
      return list.get(0);
    return 0;
  }

  @Override
  public void saveStockShiftIn(PStockShiftIn stockShiftIn) {
    Assert.assertArgumentNotNull(stockShiftIn, "stockShiftIn");
    selectOne(INSERTSTOCK, stockShiftIn);
  }

  @Override
  public List<Stock> queryStocks(StockFilter filter) {
    Assert.assertArgumentNotNull(filter, "filter");

    return selectList(QUERYSTOCK, filter);
  }

  @Override
  public List<StockChangement> queryStocksChangement(String workId) {
    Assert.assertArgumentNotNull(workId, "workId");
    List<StockChangement> list = selectList(QUERYSTOCKCHANGEMENT, workId);
    return list;
  }

  @Override
  public List<StockShiftMessage> getStockMessage(String workId) {
    Assert.assertArgumentNotNull(workId, "workId");
    return selectList(GETSTOCKMESSAGE, workId);
  }

  @Override
  public void saveStockShiftRule(PStockShiftRule rule) {
    Assert.assertArgumentNotNull(rule, "rule");
    selectOne(INSERTSTOCKRULE, rule);
  }

  @Override
  public void saveStockCmd(StockCmd stockCmd) {
    Assert.assertArgumentNotNull(stockCmd, "stockCmd");
    selectOne(SAVESTOCKCMD, stockCmd);
  }

  @Override
  public List<StockExtendInfo> queryStockExtendInfo(StockFilter filter) {
    Assert.assertArgumentNotNull(filter, "filter");

    return selectList(QUERYSTOCKEXTENDINFO, filter);
  }

  @Override
  public List<StockMajorInfo> queryMajorInfo(StockMajorFilter filter) {
    Assert.assertArgumentNotNull(filter, "filter");

    List<StockMajorInfo> result = selectList(QUERY_STOCKMAJORINFO, filter);
    return result;
  }
}
