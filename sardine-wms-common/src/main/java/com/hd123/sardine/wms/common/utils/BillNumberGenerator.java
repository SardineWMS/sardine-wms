package com.hd123.sardine.wms.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class BillNumberGenerator {

  /** 单据号中日期部分的格式。 */
  private static final SimpleDateFormat DATE_PART_FORMAT = new SimpleDateFormat("yyMMdd");
  /** 单据号中固定流水部分的长度 */
  private static final int LENGTH_FIXED_FLOW_PART = 6;
  /** 流水号起始值 */
  private static final long FLOW_START = 1L;
  /** 流水号最大值 */
  private static final long FLOW_MAX = 999999L;
  /** 实体的标识只能由数字和字母组成 */
  private static final String PATTERN_ENTITYID = "^[\\w]+$";
  /** 实体的标识的最大长度 */
  private static final int MAX_ENTITYID_LENGTH = 30;

  private static BillNumberGenerator instance = new BillNumberGenerator();

  public static BillNumberGenerator getInstance() {
    return instance;
  }

  public String allocate(String entityId, String orgId) {
    if (!Pattern.matches(PATTERN_ENTITYID, entityId))
      throw new IllegalArgumentException("实体标识只能由数字或字母组成。");
    if (entityId.length() > MAX_ENTITYID_LENGTH)
      throw new IllegalArgumentException("实体标识不能超过" + MAX_ENTITYID_LENGTH + "位");

    String datePart = DATE_PART_FORMAT.format(new Date());
    SequenceSpec seq = new SequenceSpec();
    seq.setDbId(getDBId(entityId));
    seq.setStartValue(FLOW_START);
    seq.setMaxValue(FLOW_MAX);
    seq.setIncrement(1L);
    String flowCode = String.valueOf(new OracleSequenceGenerator().nextValue(seq));
    String billnumber = orgId + datePart
        + StringUtils.repeat("0", LENGTH_FIXED_FLOW_PART - flowCode.length()) + flowCode;
    return billnumber;
  }

  public String getDBId(String entityId) {
    assert entityId != null;

    return DATE_PART_FORMAT.format(new Date()) + entityId;
  }
}
