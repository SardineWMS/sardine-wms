/**
 * 版权所有(C)，LHWMS项目组，2017，所有权利保留。
 * 
 * 项目名：	sardine-wms-dao
 * 文件名：	SequenceDaoImpl.java
 * 模块说明：	
 * 修改历史：
 * 2017年2月7日 - Jing - 创建。
 */
package com.hd123.sardine.wms.dao.basicInfo.seq.impl;

import java.util.HashMap;
import java.util.Map;

import com.hd123.rumba.commons.lang.Assert;
import com.hd123.sardine.wms.common.dao.NameSpaceSupport;
import com.hd123.sardine.wms.dao.basicInfo.seq.SequenceDao;

/**
 * @author Jing
 *
 */
public class SequenceDaoImpl extends NameSpaceSupport implements SequenceDao {
  public static final String GETCURRENTVALUE = "getCurrentValue";
  public static final String GETNEXTVALUE = "getNextValue";
  public static final String GETCURRENTVALUEWITHDATE = "getCurrentValueWithDate";
  public static final String GETNEXTVALUEWITHDATE = "getNextValueWithDate";

  @Override
  public int getCurrentValue(String seqName, String companyUuid) {
    Assert.assertArgumentNotNull(seqName, "seqName");
    Assert.assertArgumentNotNull(companyUuid, "companyUuid");

    Map<String, String> map = new HashMap<>();
    map.put("seqName", seqName);
    map.put("companyUuid", companyUuid);

    return selectOne(GETCURRENTVALUE, map);
  }

  @Override
  public int getNextValue(String seqName, String companyUuid) {
    Assert.assertArgumentNotNull(seqName, "seqName");
    Assert.assertArgumentNotNull(companyUuid, "companyUuid");

    Map<String, String> map = new HashMap<>();
    map.put("seqName", seqName);
    map.put("companyUuid", companyUuid);

    return selectOne(GETNEXTVALUE, map);
  }

  @Override
  public int getCurrentValueWithDate(String seqName, String companyUuid) {
    Assert.assertArgumentNotNull(seqName, "seqName");
    Assert.assertArgumentNotNull(companyUuid, "companyUuid");

    Map<String, String> map = new HashMap<>();
    map.put("seqName", seqName);
    map.put("companyUuid", companyUuid);

    return selectOne(GETCURRENTVALUEWITHDATE, map);
  }

  @Override
  public int getNextValueWithDate(String seqName, String companyUuid) {
    Assert.assertArgumentNotNull(seqName, "seqName");
    Assert.assertArgumentNotNull(companyUuid, "companyUuid");

    Map<String, String> map = new HashMap<>();
    map.put("seqName", seqName);
    map.put("companyUuid", companyUuid);

    return selectOne(GETNEXTVALUEWITHDATE, map);
  }
}
